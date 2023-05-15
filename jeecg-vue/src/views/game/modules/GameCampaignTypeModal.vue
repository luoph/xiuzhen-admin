<template>
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select
            :disabled="isEdit"
            placeholder="选择活动类型"
            v-decorator="['type', validatorRules.type]"
            initialValue="1"
            @change="selectType"
            showSearch
            allowClear
            style="width: 100%"
          >
            <a-select-option :value="1">1-登录礼包</a-select-option>
            <a-select-option :value="2">2-累计充值</a-select-option>
            <a-select-option :value="3">3-节日兑换</a-select-option>
            <a-select-option :value="4">4-节日任务</a-select-option>
            <a-select-option :value="5">5-修为加成</a-select-option>
            <a-select-option :value="6">6-灵气加成</a-select-option>
            <a-select-option :value="7">7-节日掉落</a-select-option>
            <a-select-option :value="8">8-节日烟花</a-select-option>
            <a-select-option :value="9">9-消耗排行</a-select-option>
            <a-select-option :value="10">10-限时仙剑</a-select-option>
            <a-select-option :value="11">11-节日砸蛋</a-select-option>
            <a-select-option :value="12">12-砸蛋排行</a-select-option>
            <a-select-option :value="13">13-砸蛋礼包</a-select-option>
            <a-select-option :value="14">14-节日派对</a-select-option>
            <a-select-option :value="15">15-直购礼包</a-select-option>
            <a-select-option :value="16">16-返利狂欢</a-select-option>
            <a-select-option :value="17">17-赠酒排行榜</a-select-option>
            <a-select-option :value="18">18-魅力值排行榜</a-select-option>
            <a-select-option :value="20">20-自选特惠</a-select-option>
            <a-select-option :value="21">21-累充排行</a-select-option>
            <a-select-option :value="22">22-邮件活动</a-select-option>
            <a-select-option :value="23">23-遗迹夺宝</a-select-option>
            <a-select-option :value="24">24-阶段任务</a-select-option>
            <a-select-option :value="25">25-夺宝战令</a-select-option>
            <a-select-option :value="26">26-召唤活动</a-select-option>
            <a-select-option :value="27">27-召唤排行</a-select-option>
            <a-select-option :value="28">28-超值礼包</a-select-option>
            <a-select-option :value="29">29-单日仙玉返利</a-select-option>
            <a-select-option :value="30">30-单日道具返利</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称" />
        </a-form-item>
        <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <img v-if="model.typeImage" :src="getImgView(model.typeImage)" :alt="getImgView(model.typeImage)" class="banner-image" />
          <game-image-selector placeholder="请选择活动宣传图" v-model="model.typeImage" />
        </a-form-item>
        <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
        </a-form-item>
        <a-form-item label="额外参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-if="model.type === 16" v-decorator="['extra', validatorRules.extra]" placeholder='活动类型"16-狂欢返利" 时必填,格式如下:"{"emailId":71,"aaa":"bbb"}"' />
          <a-textarea v-else v-decorator="['extra', validatorRules.extra]" placeholder="请输入额外参数" />
        </a-form-item>

        <a-form-item label="资源类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="资源类型" v-decorator="['resType', validatorRules.resType]" initialValue="1">
            <a-select-option :value="1">骨骼</a-select-option>
            <a-select-option :value="2">序列帧</a-select-option>
            <a-select-option :value="3">图片</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="骨骼动画" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['animation', validatorRules.animation]" placeholder="骨骼动画"></a-input>
        </a-form-item>

        <a-form-item v-if="model.type === 11" label="砸蛋积分商品" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea
            v-decorator="['eggsIntegralGoods', validatorRules.eggsIntegralGoods]"
            rows="4"
            placeholder='活动类型"11-砸蛋" 时必填,格式如下:"[{"goodsId":1,"itemId":1001,"integral":1,"stack":1,"num":100}]"'
          />
        </a-form-item>

        <a-form-item v-if="model.type === 14" label="节日派对大奖" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea
            v-decorator="['eggsIntegralGoods', validatorRules.eggsIntegralGoods]"
            rows="4"
            placeholder='活动类型"14-节日派对" 时必填,格式如下:"[{"itemId":1001,"num":100}]"'
          />
        </a-form-item>
        <a-form-item label="是否跨服" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择是否跨服" v-decorator="['cross', validatorRules.cross]" initialValue="0">
            <a-select-option :value="0">本服</a-select-option>
            <a-select-option :value="1">跨服</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="时间类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择活动类型" @change="handleTimeTypeChange" v-decorator="['timeType', validatorRules.timeType]" initialValue="1">
            <a-select-option :value="1">1-时间范围</a-select-option>
            <a-select-option :value="2">2-开服第N天</a-select-option>
          </a-select>
          <a-form-item v-if="model.type === 29" style="color: red">单日仙玉返利活动，（按日期配置时间）结束时间需配置多1天5分钟；（按开服配置时间）需配置多1天</a-form-item>
        </a-form-item>
        <a-form-item v-show="model.timeType == 1" label="活动时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-form-item>
            <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" />
          </a-form-item>
          <a-form-item>
            <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" />
          </a-form-item>
        </a-form-item>
        <a-form-item v-show="model.timeType == 2" label="开始天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始天数(开服第n天, 0表示开服第1天)" style="width: 100%" />
        </a-form-item>
        <a-form-item v-show="model.timeType == 2" label="持续天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续天数" style="width: 100%" />
        </a-form-item>
      </a-form>

      <game-campaign-type-login-list v-if="isEdit && model.type === 1" ref="loginList" />
      <game-campaign-type-recharge-list v-if="isEdit && model.type === 2" ref="rechargeList" />
      <game-campaign-type-exchange-list v-if="isEdit && model.type === 3" ref="exchangeList" />
      <game-campaign-type-task-list v-if="isEdit && model.type === 4" ref="taskList" />
      <game-campaign-type-buff-list v-if="isEdit && (model.type === 5 || model.type === 6)" ref="buffList" />
      <game-campaign-type-fall-list v-if="isEdit && model.type === 7" ref="fallList" />
      <game-campaign-type-fall-reward-list v-if="isEdit && model.type === 7" ref="rewardList" />
      <game-campaign-type-firework-list v-if="isEdit && model.type === 8" ref="fireworkList" />
      <game-campaign-type-reduce-list v-if="isEdit && model.type === 9" ref="reduceList" />
      <game-campaign-type-sword-list v-if="isEdit && model.type === 10" ref="swordList" />
      <game-campaign-type-throwing-eggs-list v-if="isEdit && model.type === 11" ref="throwingEggsList" />
      <game-campaign-type-throwing-eggs-rank-list v-if="isEdit && model.type === 12" ref="throwingEggsRankList" />
      <game-campaign-type-throwing-eggs-gift-list v-if="isEdit && model.type === 13" ref="throwingEggsGiftList" />
      <game-campaign-type-party-task-list v-if="isEdit && model.type === 14" ref="partyTaskList" />
      <game-campaign-type-party-progress-list v-if="isEdit && model.type === 14" ref="partyProgressList" />
      <game-campaign-direct-purchase-list v-if="isEdit && (model.type === 15 || model.type === 28)" ref="directPurchaseList" />
      <game-campaign-type-rebate-recharge-list v-if="isEdit && model.type === 16" ref="rebateRechargeList" />
      <game-campaign-type-marry-rank-list v-if="isEdit && (model.type === 17 || model.type === 18 || model.type === 21 || model.type === 27)" ref="marryRankList" />
      <game-campaign-type-marry-rank-reward-list v-if="isEdit && (model.type === 17 || model.type === 18 || model.type === 21 || model.type === 27)" ref="marryRankRewardList" />
      <game-campaign-type-select-discount-item-list v-if="isEdit && model.type === 20" ref="selectDiscountItemList" />
      <game-campaign-type-select-discount-message-list v-if="isEdit && model.type === 20" ref="selectDiscountMessageList" />
      <game-campaign-type-email-item-list v-if="isEdit && model.type === 22" ref="emailItemList" />
      <game-campaign-type-relic-lottery-list v-if="isEdit && model.type === 23" ref="relicLotteryList" />
      <game-campaign-type-relic-lottery-message-list v-if="isEdit && model.type === 23" ref="relicLotteryMessageList" />
      <game-campaign-type-stage-task-list v-if="isEdit && model.type === 24" ref="stageTaskList" />
      <game-campaign-type-stage-task-item-list v-if="isEdit && model.type === 24" ref="stageTaskItemList" />
      <game-campaign-type-lottery-token-list v-if="isEdit && model.type === 25" ref="lotteryTokenList" />
      <game-campaign-type-summon-list v-if="isEdit && model.type === 26" ref="summonList" />
      <game-campaign-type-summon-message-list v-if="isEdit && model.type === 26" ref="summonMessageList" />
      <game-campaign-type-single-day-recharge-jade-rebate-list v-if="isEdit && model.type === 29" ref="singleDayRechargeJadeRebateList" />
      <game-campaign-type-single-day-recharge-item-rebate-list v-if="isEdit && model.type === 30" ref="singleDayRechargeItemRebateList" />
    </a-spin>
  </a-modal>
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import moment from 'moment';
import GameImageSelector from '../components/GameImageSelector';
import GameCampaignTypeLoginList from '../GameCampaignTypeLoginList';
import GameCampaignTypeRechargeList from '../GameCampaignTypeRechargeList';
import GameCampaignTypeExchangeList from '../GameCampaignTypeExchangeList';
import GameCampaignTypeTaskList from '../GameCampaignTypeTaskList';
import GameCampaignTypeBuffList from '../GameCampaignTypeBuffList';
import GameCampaignTypeFallList from '../GameCampaignTypeFallList';
import GameCampaignTypeFallRewardList from '../GameCampaignTypeFallRewardList';
import GameCampaignTypeFireworkList from '../GameCampaignTypeFireworkList';
import GameCampaignTypeReduceList from '../GameCampaignTypeReduceList';
import GameCampaignTypeSwordList from '../GameCampaignTypeSwordList';
import GameCampaignTypeThrowingEggsList from '../GameCampaignTypeThrowingEggsList';
import GameCampaignTypeThrowingEggsRankList from '../GameCampaignTypeThrowingEggsRankList';
import GameCampaignTypeThrowingEggsGiftList from '../GameCampaignTypeThrowingEggsGiftList';
import GameCampaignTypePartyProgressList from '../GameCampaignTypePartyProgressList';
import GameCampaignTypePartyTaskList from '../GameCampaignTypePartyTaskList';
import GameCampaignDirectPurchaseList from '../GameCampaignDirectPurchaseList';
import GameCampaignTypeRebateRechargeList from '../GameCampaignTypeRebateRechargeList';
import GameCampaignTypeMarryRankList from '../GameCampaignTypeMarryRankList';
import GameCampaignTypeMarryRankRewardList from '../GameCampaignTypeMarryRankRewardList';
import GameCampaignTypeSelectDiscountItemList from '../GameCampaignTypeSelectDiscountItemList';
import GameCampaignTypeSelectDiscountMessageList from '../GameCampaignTypeSelectDiscountMessageList';
import GameCampaignTypeEmailItemList from '../GameCampaignTypeEmailItemList';
import GameCampaignTypeRelicLotteryList from '../GameCampaignTypeRelicLotteryList';
import GameCampaignTypeRelicLotteryMessageList from '../GameCampaignTypeRelicLotteryMessageList';
import GameCampaignTypeStageTaskList from '../GameCampaignTypeStageTaskList';
import GameCampaignTypeStageTaskItemList from '../GameCampaignTypeStageTaskItemList';
import GameCampaignTypeLotteryTokenList from '../GameCampaignTypeLotteryTokenList';
import GameCampaignTypeSummonList from '../GameCampaignTypeSummonList';
import GameCampaignTypeSummonMessageList from '../GameCampaignTypeSummonMessageList';
import GameCampaignTypeSingleDayRechargeJadeRebateList from '../GameCampaignTypeSingleDayRechargeJadeRebateList';
import GameCampaignTypeSingleDayRechargeItemRebateList from '../GameCampaignTypeSingleDayRechargeItemRebateList';

export default {
  name: 'GameCampaignTypeModal',
  components: {
    JDate,
    GameImageSelector,
    GameCampaignTypeLoginList,
    GameCampaignTypeRechargeList,
    GameCampaignTypeExchangeList,
    GameCampaignTypeTaskList,
    GameCampaignTypeBuffList,
    GameCampaignTypeFallList,
    GameCampaignTypeFallRewardList,
    GameCampaignTypeFireworkList,
    GameCampaignTypeReduceList,
    GameCampaignTypeSwordList,
    GameCampaignTypeThrowingEggsList,
    GameCampaignTypeThrowingEggsRankList,
    GameCampaignTypeThrowingEggsGiftList,
    GameCampaignTypePartyProgressList,
    GameCampaignTypePartyTaskList,
    GameCampaignDirectPurchaseList,
    GameCampaignTypeRebateRechargeList,
    GameCampaignTypeMarryRankList,
    GameCampaignTypeMarryRankRewardList,
    GameCampaignTypeSelectDiscountItemList,
    GameCampaignTypeSelectDiscountMessageList,
    GameCampaignTypeEmailItemList,
    GameCampaignTypeRelicLotteryList,
    GameCampaignTypeRelicLotteryMessageList,
    GameCampaignTypeStageTaskList,
    GameCampaignTypeStageTaskItemList,
    GameCampaignTypeLotteryTokenList,
    GameCampaignTypeSummonList,
    GameCampaignTypeSummonMessageList,
    GameCampaignTypeSingleDayRechargeJadeRebateList,
    GameCampaignTypeSingleDayRechargeItemRebateList
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
        campaignId: { rules: [{ required: true, message: '请输入活动id!' }] },
        type: { rules: [{ required: true, message: '请输入活动类型' }] },
        typeImage: { rules: [{ required: true, message: '请输入活动类型图片!' }] },
        sort: { rules: [{ required: true, message: '请输入排序!' }] },
        extra: { rules: [{ required: false, message: '请输入额外参数!' }] },
        resType: { rules: [{ required: false, message: '请选择资源类型' }] },
        cross: { rules: [{ required: true, message: '请选是否跨服' }] },
        animation: { rules: [{ required: false, message: '请输入资源类型!' }] },
        timeType: { rules: [{ required: true, message: '请输入时间类型!' }] },
        startDay: { rules: [{ required: false, message: '请输入开始时间(开服第n天)!' }] },
        duration: { rules: [{ required: false, message: '请输入持续天数!' }] },
        startTime: { rules: [{ required: false, message: '请输入开始时间!' }] },
        endTime: { rules: [{ required: false, message: '请输入结束时间!' }] },
        eggsIntegralGoods: { rules: [{ required: this.type === 11, message: '该活动需要对应的商品' }] }
      },
      url: {
        add: 'game/gameCampaignType/add',
        edit: 'game/gameCampaignType/edit'
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
      if (!this.model.timeType) {
        this.model.timeType = 1;
      }
      this.visible = true;
      console.log('GameCampaignTypeModal, model:', JSON.stringify(this.model));

      this.$nextTick(() => {
        if (this.isEdit) {
          if (this.$refs.loginList) {
            this.$refs.loginList.edit(record);
          }
          if (this.$refs.rechargeList) {
            this.$refs.rechargeList.edit(record);
          }
          if (this.$refs.exchangeList) {
            this.$refs.exchangeList.edit(record);
          }
          if (this.$refs.taskList) {
            this.$refs.taskList.edit(record);
          }
          if (this.$refs.buffList) {
            this.$refs.buffList.edit(record);
          }
          if (this.$refs.fallList) {
            this.$refs.fallList.edit(record);
          }
          if (this.$refs.rewardList) {
            this.$refs.rewardList.edit(record);
          }
          if (this.$refs.fireworkList) {
            this.$refs.fireworkList.edit(record);
          }
          if (this.$refs.reduceList) {
            this.$refs.reduceList.edit(record);
          }
          if (this.$refs.swordList) {
            this.$refs.swordList.edit(record);
          }
          if (this.$refs.throwingEggsList) {
            this.$refs.throwingEggsList.edit(record);
          }
          if (this.$refs.throwingEggsRankList) {
            this.$refs.throwingEggsRankList.edit(record);
          }
          if (this.$refs.throwingEggsGiftList) {
            this.$refs.throwingEggsGiftList.edit(record);
          }
          if (this.$refs.partyTaskList) {
            this.$refs.partyTaskList.edit(record);
          }
          if (this.$refs.partyProgressList) {
            this.$refs.partyProgressList.edit(record);
          }
          if (this.$refs.directPurchaseList) {
            this.$refs.directPurchaseList.edit(record);
          }
          if (this.$refs.rebateRechargeList) {
            this.$refs.rebateRechargeList.edit(record);
          }
          if (this.$refs.marryRankList) {
            this.$refs.marryRankList.edit(record);
          }
          if (this.$refs.marryRankRewardList) {
            this.$refs.marryRankRewardList.edit(record);
          }
          if (this.$refs.selectDiscountItemList) {
            this.$refs.selectDiscountItemList.edit(record);
          }
          if (this.$refs.selectDiscountMessageList) {
            this.$refs.selectDiscountMessageList.edit(record);
          }
          if (this.$refs.emailItemList) {
            this.$refs.emailItemList.edit(record);
          }
          if (this.$refs.relicLotteryList) {
            this.$refs.relicLotteryList.edit(record);
          }
          if (this.$refs.relicLotteryMessageList) {
            this.$refs.relicLotteryMessageList.edit(record);
          }
          if (this.$refs.stageTaskList) {
            this.$refs.stageTaskList.edit(record);
          }
          if (this.$refs.stageTaskItemList) {
            this.$refs.stageTaskItemList.edit(record);
          }
          if (this.$refs.lotteryTokenList) {
            this.$refs.lotteryTokenList.edit(record);
          }
          if (this.$refs.summonList) {
            this.$refs.summonList.edit(record);
          }
          if (this.$refs.summonMessageList) {
            this.$refs.summonMessageList.edit(record);
          }
          if (this.$refs.singleDayRechargeJadeRebateList) {
            this.$refs.singleDayRechargeJadeRebateList.edit(record);
          }
          if (this.$refs.singleDayRechargeItemRebateList) {
            this.$refs.singleDayRechargeItemRebateList.edit(record);
          }
        }

        this.form.setFieldsValue(
          pick(
            this.model,
            'campaignId',
            'name',
            'type',
            'typeImage',
            'sort',
            'extra',
            'resType',
            'cross',
            'animation',
            'eggsIntegralGoods',
            'timeType',
            'startDay',
            'duration',
            'startTime',
            'endTime'
          )
        );
        // 时间格式化
        this.form.setFieldsValue({ startTime: this.model.startTime ? moment(this.model.startTime) : null });
        this.form.setFieldsValue({ endTime: this.model.endTime ? moment(this.model.endTime) : null });
      });
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
    handleOk() {
      const that = this;
      if (this.model.cross == 1 && this.model.timeType == 2) {
        that.$message.error('跨服的时间类型必须为时间范围');
        return;
      }

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
          // 时间格式化
          formData.startTime = formData.startTime ? formData.startTime.format('YYYY-MM-DD HH:mm:ss') : null;
          formData.endTime = formData.endTime ? formData.endTime.format('YYYY-MM-DD HH:mm:ss') : null;
          // 时间类型校验
          if (formData.timeType == 1) {
            if (formData.startTime === undefined || formData.startTime === null) {
              that.$message.error('请输入开始时间');
              that.confirmLoading = false;
              return;
            }
            if (formData.endTime === undefined || formData.endTime === null) {
              that.$message.error('请输入结束时间');
              that.confirmLoading = false;
              return;
            }
          } else if (formData.timeType == 2) {
            if (formData.startDay === undefined || formData.startDay === null) {
              that.$message.error('请输入开始天数');
              that.confirmLoading = false;
              return;
            }
            if (formData.duration === undefined || formData.duration === null) {
              that.$message.error('请输入持续天数');
              that.confirmLoading = false;
              return;
            }
          }

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
          'name',
          'type',
          'typeImage',
          'sort',
          'extra',
          'resType',
          'cross',
          'animation',
          'eggsIntegralGoods',
          'timeType',
          'startDay',
          'duration',
          'startTime',
          'endTime'
        )
      );
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    },
    selectType(value) {
      this.model.type = value;
    },
    handleTimeTypeChange(value) {
      this.model.timeType = value;
    }
    // 自定义校验函数，要求输入的是一个正整数
    // checkGoods() {
    //     this.form.validateFields(eggsIntegralGoods => {
    //         if (this.type === 11) {
    //             if (value == null || value === "") {
    //                 // 如果需要返回 error msg，就把它传给 `callback()`
    //                 callback("请输入该活动必须的参数'砸蛋积分商品'");
    //             } else {
    //                 // 如果通过校验，调用无参数的 `callback()` 即可
    //                 callback();
    //             }
    //         }
    //     });
    // },
    // selectActivityType(e) {
    //     this.checkNick = e.checked;
    //     this.$nextTick(() => {
    //         this.form.validateFields(["eggsIntegralGoods"], { force: true });
    //     });
    // }
  }
};
</script>

//
<style lang="less" scoped></style>
<style lang="less" scoped>
.banner-image {
  width: auto;
  height: auto;
  display: block;
  max-width: 600px;
  max-height: 180px;
  object-fit: scale-down;
}

/** Button按钮间距 */
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
  float: right;
}
</style>
