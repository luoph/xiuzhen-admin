<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['noticeTitle', validatorRules.noticeTitle]" placeholder="请输入标题" />
        </a-form-item>
        <a-form-item label="正文" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['noticeText']" rows="4" placeholder="请输入正文" />
        </a-form-item>
        <a-form-item label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-if="isEdit" v-decorator="['gameServerList', validatorRules.gameServerList]" placeholder="区服ID" />
          <game-server-selector v-model="model.gameServerList" @onSelectServer="changeSelect" />
        </a-form-item>
        <a-form-item label="播放频率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :min="1" v-decorator="['frequency', validatorRules.frequency]" placeholder="请输入播放频率" style="width: 100%" />
        </a-form-item>
        <a-form-item label="循环播放周期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :min="1" v-decorator="['cyclePeriod', validatorRules.cyclePeriod]" placeholder="请输入循环播放周期" style="width: 100%" />
        </a-form-item>
        <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            placeholder="请选择开始时间"
            v-decorator="['beginTime', validatorRules.beginTime]"
            :trigger-change="true"
            :showTime="true"
            dateFormat="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            placeholder="请选择结束时间"
            v-decorator="['endTime', validatorRules.endTime]"
            :trigger-change="true"
            :showTime="true"
            dateFormat="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer> -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import MultipleServerSelect from '@/components/gameserver/MultipleServerSelect';
import GameServerSelector from '@/components/gameserver/GameServerSelector';

export default {
  name: 'GameLampNoticeModal',
  components: {
    JDate,
    MultipleServerSelect,
    GameServerSelector
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
      visible: false,
      isEdit: false,
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
        noticeTitle: { rules: [{ required: true, message: '请输入标题!' }] },
        noticeText: { rules: [{ required: true, message: '请输入正文!' }] },
        gameServerList: { rules: [{ required: true, message: '请输入投放服务器!' }] },
        frequency: { rules: [{ required: true, message: '请输入播放频率!' }] },
        cyclePeriod: { rules: [{ required: true, message: '请输入循环播放周期!' }] },
        beginTime: { rules: [{ required: true, message: '请输入开始时间!' }] },
        endTime: { rules: [{ required: true, message: '请输入结束时间!' }] }
      },
      url: {
        add: 'game/gameLampNotice/add',
        edit: 'game/gameLampNotice/edit'
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
      this.isEdit = this.model.id != null;
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'noticeTitle', 'noticeText', 'gameServerList', 'frequency', 'cyclePeriod', 'beginTime', 'endTime'));
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
      this.form.setFieldsValue(pick(row, 'noticeTitle', 'noticeText', 'gameServerList', 'frequency', 'cyclePeriod', 'beginTime', 'endTime'));
    },
    changeSelect(value) {
      this.form.setFieldsValue({
        gameServerList: value.join(',')
      });
    }
  }
};
</script>

//
<style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
  float: right;
}
</style>
