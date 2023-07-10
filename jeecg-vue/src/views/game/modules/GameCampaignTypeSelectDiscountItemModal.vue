<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入主活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="位置序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['showOrder', validatorRules.showOrder]" placeholder="请输入位置序号" style="width: 100%" />
        </a-form-item>
        <a-form-item label="商品描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['itemDesc', validatorRules.itemDesc]" placeholder="请输入商品描述" />
        </a-form-item>
        <a-form-item label="商品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['goodsId', validatorRules.goodsId]" placeholder="请输入商品id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="免费领取" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-checkbox v-decorator="['free', { initialValue: false }]" :checked="form.getFieldValue('free')"> 本组商品是否免费领取 </a-checkbox>
        </a-form-item>
        <a-form-item
          :label="form.getFieldValue('free') ? '免费领取次数' : '限购次数'"
          :extra="form.getFieldValue('free') ? '' : '0不限购'"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input-number v-decorator="['limitNum', validatorRules.limitNum]" :min="form.getFieldValue('free') ? 1 : 0" style="width: 100%" />
        </a-form-item>
        <a-form-item
          label="可选物品"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          extra='格式(1、2为第N个自选组)：{"1":[{ "itemId":1,"num":2},{"itemId":5,"num":6}],"2":[{"itemId":8,"num":9}]}'
        >
          <a-textarea v-decorator="['chooseItems', validatorRules.chooseItems]" rows="4" placeholder="请输入可选物品" />
        </a-form-item>
        <a-form-item label="免费物品" :labelCol="labelCol" :wrapperCol="wrapperCol" extra='格式list：[{"itemId":1,"num":2},{"itemId":3,"num":4}]'>
          <a-textarea v-decorator="['freeItems']" rows="4" />
        </a-form-item>
        <a-form-item label="最小世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['minLevel', validatorRules.minLevel]" placeholder="请输入最小世界等级" style="width: 100%" />
        </a-form-item>
        <a-form-item label="最大世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['maxLevel', validatorRules.maxLevel]" placeholder="请输入最大世界等级" style="width: 100%" />
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
  name: 'GameCampaignTypeSelectDiscountItemModal',
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
        campaignId: { rules: [{ required: true, message: '请输入主活动id!' }] },
        typeId: { rules: [{ required: true, message: '请输入子活动id!' }] },
        showOrder: { rules: [{ required: true, message: '请输入位置序号!' }] },
        itemDesc: { rules: [{ required: true, message: '请输入商品描述!' }] },
        limitNum: { initialValue: 0, rules: [{ required: true, message: '请输入限购次数!' }] },
        goodsId: { rules: [{ required: true, message: '请输入商品id!' }] },
        chooseItems: { rules: [{ required: true, message: '请输入可选物品!' }] },
        minLevel: { rules: [{ required: true, message: '请输入最小世界等级!' }] },
        maxLevel: { rules: [{ required: true, message: '请输入最大世界等级!' }] }
      },
      url: {
        add: 'game/gameCampaignTypeSelectDiscountItem/add',
        edit: 'game/gameCampaignTypeSelectDiscountItem/edit'
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
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(this.model, 'campaignId', 'typeId', 'showOrder', 'itemDesc', 'free', 'limitNum', 'goodsId', 'chooseItems', 'freeItems', 'minLevel', 'maxLevel')
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
      this.form.setFieldsValue(pick(row, 'campaignId', 'typeId', 'showOrder', 'itemDesc', 'free', 'limitNum', 'goodsId', 'chooseItems', 'freeItems', 'minLevel', 'maxLevel'));
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
