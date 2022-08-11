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
          <a-input-number :disabled="true" v-decorator="['consumeDetailId', validatorRules.consumeDetailId]" placeholder="请输入详情id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
        </a-form-item>
        <a-form-item label="统计类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="请选择统计类型" v-decorator="['consumeType', validatorRules.consumeType]" initialValue="1">
            <a-select-option :value="0">个人</a-select-option>
            <a-select-option :value="1">全服</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="开始时间(子活动开始第n天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始时间(子活动开始第n天, 0表示子活动开始第1天)" style="width: 100%" />
        </a-form-item>
        <a-form-item label="开启前是否统计" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="开启前是否统计" v-decorator="['statisticsNotStart', validatorRules.statisticsNotStart]" initialValue="1">
            <a-select-option :value="0">否</a-select-option>
            <a-select-option :value="1">是</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="跳转" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['jump', validatorRules.jump]" placeholder="请输入跳转"></a-input>
        </a-form-item>
        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['description', validatorRules.description]" placeholder="请输入描述"></a-input>
        </a-form-item>
        <a-form-item label="总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['num', validatorRules.num]" placeholder="请输入总数量"></a-input-number>
        </a-form-item>
        <a-form-item label="消耗道具" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['consumeItems', validatorRules.consumeItems]" placeholder="请输入消耗道具 e.g. [1001, 1002, 1003]"></a-input>
        </a-form-item>
        <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reward', validatorRules.reward]" placeholder='请输入奖励列表 e.g. [{"itemId":1001, "num":1}]'></a-input>
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
  name: 'OpenServiceCampaignConsumeDetailItemModal',
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
        campaignId: { rules: [{ required: true, message: '请输入开服活动id, open_service_campaign.id!' }] },
        campaignTypeId: { rules: [{ required: true, message: '请输入open_service_campaign_type.id!' }] },
        consumeDetailId: { rules: [{ required: true, message: '请输入open_service_campaign_consume_detail.id!' }] },
        sort: { rules: [{ required: true, message: '请输入排序!' }] },
        consumeType: { rules: [{ required: true, message: '请输入统计类型!' }] },
        startDay: { rules: [{ required: true, message: '请输入开始时间(子活动开始第n天)!' }] },
        statisticsNotStart: { rules: [{ required: true, message: '请输入开启前是否统计!' }] },
        jump: { rules: [{ required: false, message: '请输入跳转!' }] },
        description: { rules: [{ required: true, message: '请输入描述!' }] },
        consumeItems: { rules: [{ required: true, message: '请输入消耗道具!' }] },
        num: { rules: [{ required: true, message: '请输入总数量!' }] },
        reward: { rules: [{ required: true, message: '请输入奖励列表!' }] }
      },
      url: {
        add: 'game/openServiceCampaignConsumeDetailItem/add',
        edit: 'game/openServiceCampaignConsumeDetailItem/edit'
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
      console.log('OpenServiceCampaignConsumeDetailItemModal, model:', JSON.stringify(this.model));
      this.visible = true;

      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'campaignId',
            'campaignTypeId',
            'consumeDetailId',
            'sort',
            'consumeType',
            'startDay',
            'statisticsNotStart',
            'description',
            'consumeItems',
            'jump',
            'num',
            'reward'
          )
        );
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
      this.form.setFieldsValue(
        pick(
          row,
          'campaignId',
          'campaignTypeId',
          'consumeDetailId',
          'sort',
          'consumeType',
          'startDay',
          'statisticsNotStart',
          'description',
          'consumeItems',
          'jump',
          'num',
          'reward'
        )
      );
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
