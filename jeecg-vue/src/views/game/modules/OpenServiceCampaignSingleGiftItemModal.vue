<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignTypeId', validatorRules.campaignTypeId]" placeholder="请输入typeId" style="width: 100%" />
        </a-form-item>
        <a-form-item label="详情id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['giftDetailId', validatorRules.giftDetailId]" placeholder="请输入详情id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="任务金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['amount', validatorRules.amount]" placeholder="请输入任务金额" style="width: 100%" />
        </a-form-item>
        <a-form-item label="领取上限次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['limitTimes', validatorRules.limitTimes]" placeholder="请输入领取上限次数" style="width: 100%" />
        </a-form-item>
        <a-form-item label="任务描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['remark', validatorRules.remark]" placeholder="请输入任务描述" />
        </a-form-item>
        <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励列表" />
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

export default {
  name: 'OpenServiceCampaignSingleGiftItemModal',
  components: {
    JDate
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 1200,
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
        campaignId: { rules: [{ required: true, message: '请输入开服活动id!' }] },
        campaignTypeId: { rules: [{ required: true, message: '请输入typeId!' }] },
        giftDetailId: { rules: [{ required: true, message: '请输入详情id' }] },
        amount: { rules: [{ required: true, message: '请输入任务金额!' }] },
        limitTimes: { rules: [{ required: true, message: '请输入领取上限次数!' }] },
        remark: { rules: [{ required: true, message: '请输入任务描述!' }] },
        reward: { rules: [{ required: true, message: '请输入奖励列表!' }] }
      },
      url: {
        add: 'game/openServiceCampaignSingleGiftItem/add',
        edit: 'game/openServiceCampaignSingleGiftItem/edit'
      }
    };
  },
  created() {},
  methods: {
    add(record) {
      this.edit(record);
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.isEdit = this.model.id != null;
      console.log('OpenServiceCampaignSingleGiftItemModal, model:', JSON.stringify(this.model));
      this.visible = true;

      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'campaignId', 'campaignTypeId', 'giftDetailId', 'amount', 'limitTimes', 'remark', 'reward'));
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
      this.form.setFieldsValue(pick(row, 'campaignId', 'campaignTypeId', 'giftDetailId', 'amount', 'limitTimes', 'remark', 'reward'));
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
