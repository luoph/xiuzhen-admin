<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['title', validatorRules.title]" placeholder="请输入标题"></a-input>
        </a-form-item>
        <a-form-item label="正文" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-editor v-model="contentHtml" />
        </a-form-item>
        <a-form-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励"></a-input>
        </a-form-item>
        <a-form-item label="服务器" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-show="isEdit" v-decorator="['serverIds', validatorRules.serverIds]" placeholder="请输入服务器"></a-input>
          <game-server-selector v-model="model.serverIds" @onSelectServer="changeSelect" />
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="请选择状态" v-decorator="['status', validatorRules.status]" initialValue="0">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            placeholder="请选择开始时间"
            v-decorator="['startTime', validatorRules.startTime]"
            :trigger-change="true"
            :show-time="true"
            date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            placeholder="请选择结束时间"
            v-decorator="['endTime', validatorRules.endTime]"
            :trigger-change="true"
            :show-time="true"
            date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!--
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
        </a-drawer>
     -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import GameServerSelector from '@/components/gameserver/GameServerSelector';
import moment from 'moment';
import JEditor from '@/components/jeecg/JEditor';

export default {
  name: 'GameUpgradeNoticeModal',
  components: {
    JDate,
    GameServerSelector,
    JEditor
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
      visible: false,
      isEdit: false,
      contentHtml: '',
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {
        title: { rules: [{ required: true, message: '请输入标题!' }] },
        noticeMsg: { rules: [{ required: true, message: '请输入正文!' }] },
        reward: { rules: [{ required: true, message: '请输入奖励!' }] },
        serverIds: { rules: [{ required: true, message: '请输入服务器!' }] },
        status: { rules: [{ required: true, message: '请选择状态!' }] },
        startTime: { rules: [{ required: true, message: '请输入开始时间!' }] },
        endTime: { rules: [{ required: true, message: '请输入结束时间!' }] }
      },
      url: {
        add: 'game/gameUpgradeNotice/add',
        edit: 'game/gameUpgradeNotice/edit'
      }
    };
  },
  created() {},
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.isEdit = this.model.id != null;
      if (this.model.noticeMsg) {
        this.contentHtml = this.model.noticeMsg;
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'title', 'noticeMsg', 'reward', 'serverIds', 'status', 'startTime', 'endTime'));
      });
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpUrl = '';
          let method = '';
          if (!this.model.id) {
            httpUrl += this.url.add;
            method = 'post';
          } else {
            httpUrl += this.url.edit;
            method = 'put';
          }
          this.model.noticeMsg = this.contentHtml;

          let formData = Object.assign(this.model, values);
          console.log('表单提交数据', formData);
          httpAction(httpUrl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
              that.close();
            });
        }
      });
    },
    handleCancel() {
      this.close();
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'title', 'noticeMsg', 'reward', 'serverIds', 'status', 'startTime', 'endTime'));
    },
    changeSelect(value) {
      this.form.setFieldsValue({
        serverIds: value.join(',')
      });
    }
  }
};
</script>

// <style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
  float: right;
}
</style>
