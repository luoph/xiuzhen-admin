<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignTypeId', validatorRules.campaignTypeId]" placeholder="请输入页签id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="详情id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['lotteryDetailId', validatorRules.lotteryDetailId]" placeholder="请输入详情id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="奖池id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['poolId', validatorRules.poolId]" placeholder="请输入奖池id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reward', validatorRules.reward]" placeholder='请输入奖励列表 e.g. [{"itemId":1001, "num":1}]'></a-input>
        </a-form-item>
        <a-form-item label="掉落权重" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['weight', validatorRules.weight]" placeholder="请输入掉落权重" style="width: 100%" />
        </a-form-item>
        <a-form-item label="是否记录" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="是否记录" v-decorator="['record', validatorRules.record]" initialValue="1">
            <a-select-option :value="0">否</a-select-option>
            <a-select-option :value="1">是</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="是否传闻" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="是否传闻" v-decorator="['message', validatorRules.message]" initialValue="0">
            <a-select-option :value="0">否</a-select-option>
            <a-select-option :value="1">是</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="是否大奖弹窗" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="是否传闻" v-decorator="['showReward', validatorRules.showReward]" initialValue="0">
            <a-select-option :value="0">否</a-select-option>
            <a-select-option :value="1">是</a-select-option>
          </a-select>
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
  name: 'OpenServiceCampaignLotteryDetailPoolModal',
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
        campaignTypeId: { rules: [{ required: true, message: '请输入页签id' }] },
        lotteryDetailId: { rules: [{ required: true, message: '请输入详情id' }] },
        poolId: { rules: [{ required: true, message: '请输入奖池id!' }] },
        reward: {},
        weight: { rules: [{ required: true, message: '请输入掉落权重!' }] },
        record: { rules: [{ required: true, message: '请输入是否记录!' }] },
        message: {},
        showReward: {}
      },
      url: {
        add: 'game/openServiceCampaignLotteryDetailPool/add',
        edit: 'game/openServiceCampaignLotteryDetailPool/edit'
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
      console.log('OpenServiceCampaignLotteryDetailPoolModal, model:', JSON.stringify(this.model));
      this.visible = true;

      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'campaignId', 'campaignTypeId', 'lotteryDetailId', 'poolId', 'reward', 'weight', 'record', 'message', 'showReward'));
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
      this.form.setFieldsValue(pick(row, 'campaignId', 'campaignTypeId', 'lotteryDetailId', 'poolId', 'reward', 'weight', 'record', 'message', 'showReward'));
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
