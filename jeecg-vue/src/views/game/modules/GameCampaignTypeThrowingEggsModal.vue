<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="砸蛋类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="['eggType', validatorRules.eggType]" placeholder="请输入砸蛋类型" style="width: 100%">
            <a-select-option :value="1">金蛋</a-select-option>
            <a-select-option :value="2">铂金蛋</a-select-option>
            <a-select-option :value="3">钻石蛋</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="抽奖所需道具" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['costItemId', validatorRules.costItemId]" placeholder="请输入抽奖所需道具" style="width: 100%" />
        </a-form-item>
        <a-form-item label="幸运值上限" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['limitLuckyValue', validatorRules.limitLuckyValue]" placeholder="请输入幸运值上限" style="width: 100%" />
        </a-form-item>
        <a-form-item label="抽奖道具数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['costNum', validatorRules.costNum]" placeholder="请输入抽奖所需道具数量，同时也是每次抽奖掉落的幸运值" style="width: 100%" />
        </a-form-item>
        <a-form-item label="砸蛋值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['throwingEggsValue', validatorRules.throwingEggsValue]" placeholder="请输入砸蛋值" style="width: 100%" />
        </a-form-item>
        <a-form-item label="抽奖积分最小值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['lotteryIntegralMin', validatorRules.lotteryIntegralMin]" placeholder="请输入抽奖积分最小值" style="width: 100%" />
        </a-form-item>
        <a-form-item label="抽奖积分最大值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['lotteryIntegralMax', validatorRules.lotteryIntegralMax]" placeholder="请输入抽奖积分最大值" style="width: 100%" />
        </a-form-item>
        <a-form-item label="幸运奖池概率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['luckyProbability', validatorRules.luckyProbability]" placeholder="请输入幸运奖池概率" style="width: 100%" />
        </a-form-item>
        <a-form-item label="概率公示" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['probabilityPublicity']" rows="4" placeholder='概率公示:[{"itemId":1001,"pro":"1.5%"}]' />
        </a-form-item>
        <a-form-item label="玩法规则" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['rule']" rows="4" placeholder="请输入玩法规则" />
        </a-form-item>
        <a-form-item label="普通奖池" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['ordinaryPool', validatorRules.ordinaryPool]" placeholder="请输入普通奖池" />
        </a-form-item>
        <a-form-item label="幸运奖池" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['luckyPool', validatorRules.luckyPool]" placeholder="幸运奖池:[1001,1002]" />
        </a-form-item>
        <a-form-item label="普通奖池掉落" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['ordinaryPoolItem']" rows="4" placeholder='普通奖池掉落:[{"rewardId":1001,"itemId":1001,"fallNum":100,"weight":100}]' />
        </a-form-item>
        <a-form-item label="幸运奖池掉落" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['luckyPoolItem']" rows="4" placeholder='幸运奖池掉落:[{"rewardId":1001,"itemId":1001,"fallNum":100,"weight":100}]' />
        </a-form-item>
        <a-form-item label="大奖动画" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['rewardAnim', validatorRules.rewardAnim]" placeholder='动画:{"name": "pet_019", "offsetY": 440, "offsetX": 400, "scale": 0.6,"itemId":1001 }' />
        </a-form-item>
        <a-form-item label="普通奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['showOrdinaryReward']" rows="4" placeholder='普通奖励:[{"itemId":1001,"num":1001}]' />
        </a-form-item>
        <a-form-item label="幸运奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['showLuckyReward']" rows="4" placeholder='幸运奖励:[{"itemId":1001,"num":1001}]' />
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

export default {
  name: 'GameCampaignTypeThrowingEggsModal',
  components: {},
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
        campaignId: { rules: [{ required: true, message: '请输入活动id!' }] },
        typeId: { rules: [{ required: true, message: '请输入子活动id!' }] },
        eggType: { rules: [{ required: true, message: '请输入砸蛋类型!' }] },
        costItemId: { rules: [{ required: true, message: '请输入抽奖所需道具!' }] },
        limitLuckyValue: { rules: [{ required: true, message: '请输入幸运值上限!' }] },
        costNum: { rules: [{ required: true, message: '请输入抽奖所需道具数量，同时也是每次抽奖掉落的砸蛋值和幸运值!' }] },
        lotteryIntegralMin: { rules: [{ required: true, message: '请输入抽奖积分最小值!' }] },
        lotteryIntegralMax: { rules: [{ required: true, message: '请输入抽奖积分最大值!' }] },
        luckyProbability: { rules: [{ required: true, message: '请输入幸运奖池概率!' }] },
        probabilityPublicity: { rules: [{ required: true, message: '请输入概率公示!' }] },
        rule: { rules: [{ required: true, message: '请输入玩法规则!' }] },
        ordinaryPool: { rules: [{ required: true, message: '请输入普通奖池!' }] },
        luckyPool: { rules: [{ required: true, message: '请输入幸运奖池!' }] },
        ordinaryPoolItem: { rules: [{ required: true, message: '请输入普通奖池掉落!' }] },
        luckyPoolItem: { rules: [{ required: true, message: '请输入幸运奖池掉落!' }] },
        rewardAnim: { rules: [{ required: true, message: '请输入大奖动画!' }] },
        showOrdinaryReward: { rules: [{ required: true, message: '请输入普通奖励!' }] },
        showLuckyReward: { rules: [{ required: true, message: '请输入幸运奖励!' }] },
        throwingEggsValue: { rules: [{ required: true, message: '请输入砸蛋值!' }] },
        minLevel: { rules: [{ required: true, message: '请输入最小世界等级!' }] },
        maxLevel: { rules: [{ required: true, message: '请输入最大世界等级!' }] }
      },
      url: {
        add: 'game/gameCampaignTypeThrowingEggs/add',
        edit: 'game/gameCampaignTypeThrowingEggs/edit'
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
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'campaignId',
            'typeId',
            'eggType',
            'costItemId',
            'limitLuckyValue',
            'costNum',
            'throwingEggsValue',
            'lotteryIntegralMin',
            'lotteryIntegralMax',
            'luckyProbability',
            'probabilityPublicity',
            'rule',
            'ordinaryPool',
            'luckyPool',
            'ordinaryPoolItem',
            'luckyPoolItem',
            'rewardAnim',
            'showOrdinaryReward',
            'showLuckyReward',
            'minLevel',
            'maxLevel'
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
          'typeId',
          'eggType',
          'costItemId',
          'limitLuckyValue',
          'costNum',
          'throwingEggsValue',
          'lotteryIntegralMin',
          'lotteryIntegralMax',
          'luckyProbability',
          'probabilityPublicity',
          'rule',
          'ordinaryPool',
          'luckyPool',
          'ordinaryPoolItem',
          'luckyPoolItem',
          'rewardAnim',
          'showOrdinaryReward',
          'showLuckyReward',
          'minLevel',
          'maxLevel'
        )
      );
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
