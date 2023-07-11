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
          <a-input-number :disabled="true" v-decorator="['rankDetailId', validatorRules.rankDetailId]" placeholder="请输入详情id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="排行消耗道具id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['itemId', validatorRules.itemId]" placeholder="请输入排行消耗道具id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="排行消耗道具数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['num', validatorRules.num]" placeholder="请输入排行消耗道具数量" style="width: 100%" />
        </a-form-item>
        <a-form-item label="消耗对应积分" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['score', validatorRules.score]" placeholder="请输入消耗对应积分" style="width: 100%" />
        </a-form-item>
        <a-form-item label="道具积分分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['itemType', validatorRules.itemType]" placeholder="请输入道具积分分类" style="width: 100%" />
        </a-form-item>
        <a-form-item label="道具积分分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['itemTypeName', validatorRules.itemTypeName]" placeholder="请输入道具积分分类名称" />
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
  name: 'OpenServiceCampaignRankDetailScoreModal',
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
        campaignTypeId: { rules: [{ required: true, message: '请输入页签id!' }] },
        rankDetailId: { rules: [{ required: true, message: '请输入详情id!' }] },
        itemId: { rules: [{ required: true, message: '请输入排行消耗道具id!' }] },
        num: { rules: [{ required: true, message: '请输入排行消耗道具数量!' }] },
        score: { rules: [{ required: true, message: '请输入消耗对应积分!' }] },
        itemType: { rules: [{ required: true, message: '请输入道具积分分类!' }] },
        itemTypeName: { rules: [{ required: true, message: '请输入道具积分分类名称!' }] }
      },
      url: {
        add: 'game/openServiceCampaignRankDetailScore/add',
        edit: 'game/openServiceCampaignRankDetailScore/edit'
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
      console.log('OpenServiceCampaignRankDetailScoreModal, model:', JSON.stringify(this.model));
      this.visible = true;

      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'campaignId', 'campaignTypeId', 'rankDetailId', 'itemId', 'num', 'score', 'itemType', 'itemTypeName'));
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
      this.form.setFieldsValue(pick(row, 'campaignId', 'campaignTypeId', 'rankDetailId', 'itemId', 'num', 'score', 'itemType', 'itemTypeName'));
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
