<template>
  <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="激活码名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入激活码名称" />
        </a-form-item>
        <a-form-item label="礼包说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['summary', validatorRules.summary]" placeholder="请输入礼包说明" />
        </a-form-item>
        <a-form-item label="限制类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['limitType', validatorRules.limitType]" placeholder="请输入限制类型" style="width: 100%" />
        </a-form-item>
        <a-form-item label="分组ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['groupId', validatorRules.groupId]" placeholder="请输入分组ID" style="width: 100%" />
        </a-form-item>
        <a-form-item label="限制渠道id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['channelIds', validatorRules.channelIds]" placeholder="请输入限制渠道id, 使用半角','分割多个id" />
        </a-form-item>
        <a-form-item label="限制区服id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['serverIds', validatorRules.serverIds]" placeholder="请输入限制区服id, 使用半角','分割多个id" />
        </a-form-item>
        <a-form-item label="活动状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['status', validatorRules.status]" placeholder="请输入活动状态" style="width: 100%" />
        </a-form-item>
        <a-form-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励" />
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注" />
        </a-form-item>
        <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择开始时间" v-decorator="['startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
        <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择结束时间" v-decorator="['endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- </a-modal> -->
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'RedeemActivityModal',
  components: {
    JDate
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
      visible: false,
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
        name: { rules: [{ required: true, message: '请输入激活码名称!' }] },
        summary: { rules: [{ required: true, message: '请输入礼包说明!' }] },
        limitType: { rules: [{ required: true, message: '请输入限制类型!' }] },
        groupId: {},
        channelIds: {},
        serverIds: {},
        status: { rules: [{ required: true, message: '请输入活动状态!' }] },
        reward: { rules: [{ required: true, message: '请输入奖励!' }] },
        remark: { rules: [{ required: true, message: '请输入备注!' }] },
        startTime: {},
        endTime: {}
      },
      url: {
        add: 'game/redeemActivity/add',
        edit: 'game/redeemActivity/edit'
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
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'name', 'summary', 'limitType', 'groupId', 'channelIds', 'serverIds', 'status', 'reward', 'remark', 'startTime', 'endTime'));
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
      this.form.setFieldsValue(pick(row, 'name', 'summary', 'limitType', 'groupId', 'channelIds', 'serverIds', 'status', 'reward', 'remark', 'startTime', 'endTime'));
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
