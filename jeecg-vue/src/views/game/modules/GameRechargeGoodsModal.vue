<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="商品Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['goodsId', validatorRules.goodsId]" placeholder="请输入商品Id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="商品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入商品名称" />
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注" />
        </a-form-item>
        <a-form-item label="内购SKU" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sku', validatorRules.sku]" placeholder="请输入内购SKU" style="width: 100%" />
        </a-form-item>
        <a-form-item label="网页支付SKU" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['webSku', validatorRules.webSku]" placeholder="请输网页支付SKU" style="width: 100%" />
        </a-form-item>
        <a-form-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['price', validatorRules.price]" placeholder="请输入单价(创建订单实际价格)" style="width: 100%" />
        </a-form-item>
        <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入折扣后的价格" style="width: 100%" />
        </a-form-item>
        <a-form-item label="当地货币价格(内购)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['localPrice', validatorRules.localPrice]" placeholder="当地货币价格(内购)" style="width: 100%" />
        </a-form-item>
        <a-form-item label="当地货币价格(网页)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['webLocalPrice', validatorRules.webLocalPrice]" placeholder="当地货币价格(网页)" style="width: 100%" />
        </a-form-item>
        <a-form-item label="显示价格(内购)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['displayPrice', validatorRules.displayPrice]" placeholder="显示价格(内购)" style="width: 100%" />
        </a-form-item>
        <a-form-item label="显示价格(网页)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['webDisplayPrice', validatorRules.webDisplayPrice]" placeholder="显示价格(网页)" style="width: 100%" />
        </a-form-item>
        <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['items']" rows="4" placeholder="请输入奖励列表" />
        </a-form-item>
        <a-form-item label="首次额外赠送" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['addition']" rows="4" placeholder="首次额外赠送" />
        </a-form-item>
        <a-form-item label="商品组别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择商品组别" v-decorator="['goodsGroup', validatorRules.goodsGroup]" initialValue="1">
            <a-select-option :value="1">直充</a-select-option>
            <a-select-option :value="2">礼包</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="商品类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择商品类型" v-decorator="['goodsType', validatorRules.goodsType]" initialValue="0" showSearch allowClear style="width: 100%">
            <a-select-option :value="0">0-仙玉</a-select-option>
            <a-select-option :value="1">1-仙职</a-select-option>
            <a-select-option :value="2">2-月卡</a-select-option>
            <a-select-option :value="3">3-每日礼包</a-select-option>
            <a-select-option :value="4">4-首充</a-select-option>
            <a-select-option :value="5">5-周卡</a-select-option>
            <a-select-option :value="6">6-六道剑阵</a-select-option>
            <a-select-option :value="7">7-招财进宝</a-select-option>
            <a-select-option :value="8">8-高级天道令</a-select-option>
            <a-select-option :value="9">9-节日派对</a-select-option>
            <a-select-option :value="10">10-节日直购礼包</a-select-option>
            <a-select-option :value="11">11-精准礼包</a-select-option>
            <a-select-option :value="12">12-结义礼包</a-select-option>
            <a-select-option :value="13">13-自选特惠</a-select-option>
            <a-select-option :value="14">14-灵兽抽奖礼包</a-select-option>
            <a-select-option :value="15">15-开服目标活动-签到令牌</a-select-option>
            <a-select-option :value="16">16-开服目标活动-任务礼包</a-select-option>
            <a-select-option :value="17">17-系统直购礼包</a-select-option>
            <a-select-option :value="18">18-成长基金</a-select-option>
            <a-select-option :value="19">19-节日活动-夺宝战令</a-select-option>
            <a-select-option :value="20">20-新战令</a-select-option>
            <a-select-option :value="21">21-超值礼包</a-select-option>
            <a-select-option :value="22">22-神游特权卡</a-select-option>
            <a-select-option :value="23">23-GM特权卡</a-select-option>
            <a-select-option :value="24">24-无限真充</a-select-option>
            <a-select-option :value="25">25-无限神充</a-select-option>
            <a-select-option :value="26">26-GM充值工具-每日礼包</a-select-option>
            <a-select-option :value="27">27-GM充值工具-GM专属资源礼包</a-select-option>
            <a-select-option :value="28">28-仙缘神通-礼包</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="购买类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select mode="multiple" placeholder="选择购买类型" v-decorator="['buyType', validatorRules.buyType]" @change="onSelectBuyType">
            <a-select-option value="1">1-真实充值</a-select-option>
            <a-select-option value="2">2-GM额度</a-select-option>
            <a-select-option value="3">3-代金券</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="GM额度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['gmCoin', validatorRules.gmCoin]" placeholder="请输入GM额度" style="width: 100%" />
        </a-form-item>
        <a-form-item label="代金券" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['cashCoupon', validatorRules.cashCoupon]" placeholder="请输入代金券" style="width: 100%" />
        </a-form-item>
        <a-form-item label="是否计入累充" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="请输入是否计入累充" v-decorator="['amountStat', validatorRules.amountStat]" initialValue="1">
            <a-select-option :value="0">是</a-select-option>
            <a-select-option :value="1">否</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="统计" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['amountStatTypes', validatorRules.amountStatTypes]" placeholder="e.g. [1, 2, 3]" style="width: 100%" />
        </a-form-item>
        <a-form-item label="兑换比例" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择兑换比例" v-decorator="['exchange', validatorRules.exchange]" initialValue="0">
            <a-select-option :value="10">10</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="特殊标签" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择特殊标签" v-decorator="['recommend', validatorRules.recommend]" initialValue="0">
            <a-select-option :value="0">无</a-select-option>
            <a-select-option :value="1">推荐</a-select-option>
            <a-select-option :value="2">礼包</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="货币" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择货币" v-decorator="['currency', validatorRules.currency]" initialValue="CNY">
            <a-select-option value="CNY">人民币</a-select-option>
            <a-select-option value="TWD">台币</a-select-option>
            <a-select-option value="VND">越南盾</a-select-option>
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

export default {
  name: 'GameRechargeGoodsModal',
  components: {},
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 1000,
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 26 },
        sm: { span: 7 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {
        price: { rules: [{ required: true, message: '请输入单价!' }] },
        discount: { rules: [{ required: false, message: '请输入折扣价格!' }] },
        name: { rules: [{ required: true, message: '请输入商品名称!' }] },
        remark: { rules: [{ required: true, message: '请输入备注!' }] },
        items: { rules: [{ required: false, message: '请输入奖励列表!' }] },
        goodsType: { rules: [{ required: true, message: '请选择商品类型!' }] },
        goodsGroup: { rules: [{ required: true, message: '请选择商品组别!' }] },
        buyType: { rules: [{ required: true, message: '请选择购买类型!' }] },
        amountStat: { rules: [{ required: true, message: '请输入是否计入累充！' }] },
        exchange: { rules: [{ required: true, message: '请输入游戏币与人民币(元)的兑换比例!' }] },
        recommend: { rules: [{ required: false, message: '请输入特殊标记前端用!' }] },
        goodsId: { rules: [{ required: true, message: '请输入商品Id' }] },
        sku: { rules: [{ required: true, message: '请输入SKU' }] },
        webSku: { rules: [{ required: false, message: '请输入网页支付SKU' }] },
        gmCoin: { rules: [{ required: false, message: '请输入GM额度' }] },
        cashCoupon: { rules: [{ required: false, message: '请输入代金券' }] },
        localPrice: { rules: [{ required: false, message: '请输入当地支付价格（内购）' }] },
        webLocalPrice: { rules: [{ required: false, message: '请输入当地支付价格（网页）' }] },
        displayPrice: { rules: [{ required: false, message: '请输入当地支付价格（内购）' }] },
        webDisplayPrice: { rules: [{ required: false, message: '请输入当地显示价格（网页）' }] },
        currency: { rules: [{ required: true, message: '请选择货币' }] }
      },
      url: {
        add: 'game/gameRechargeGoods/add',
        edit: 'game/gameRechargeGoods/edit'
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
        this.form.setFieldsValue(
          pick(
            this.model,
            'goodsId',
            'sku',
            'price',
            'discount',
            'name',
            'remark',
            'items',
            'goodsType',
            'goodsGroup',
            'buyType',
            'gmCoin',
            'cashCoupon',
            'amountStat',
            'amountStatTypes',
            'addition',
            'exchange',
            'recommend',
            'webSku',
            'localPrice',
            'webLocalPrice',
            'displayPrice',
            'webDisplayPrice',
            'currency'
          )
        );

        this.form.setFieldsValue({ buyType: null != this.model.buyType ? this.model.buyType.split(',').sort() : [] });
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
          formData.buyType = formData.buyType.join(',');

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
          'goodsId',
          'sku',
          'price',
          'discount',
          'name',
          'remark',
          'items',
          'goodsType',
          'goodsGroup',
          'buyType',
          'gmCoin',
          'cashCoupon',
          'amountStat',
          'amountStatTypes',
          'addition',
          'exchange',
          'recommend',
          'webSku',
          'localPrice',
          'webLocalPrice',
          'displayPrice',
          'webDisplayPrice',
          'currency'
        )
      );
    },
    onSelectBuyType(value) {
      // this.form.setFieldsValue({
      //   buyType: value.join(',')
      // });
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
