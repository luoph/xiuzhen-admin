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
        <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
        </a-form-item>
        <a-form-item label="页签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['tabName', validatorRules.tabName]" placeholder="请输入页签名称"></a-input>
        </a-form-item>
        <a-form-item label="时间类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择活动类型" @change="handleTimeTypeChange" v-decorator="['timeType', validatorRules.timeType]" initialValue="1">
            <a-select-option :value="1">1-时间范围</a-select-option>
            <a-select-option :value="2">2-开服第N天</a-select-option>
          </a-select>
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
        <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <img v-if="model.banner" :src="getImgView(model.banner)" :alt="getImgView(model.banner)" class="banner-image" />
          <game-image-selector placeholder="请选择活动宣传图" v-model="model.banner" />
        </a-form-item>
        <a-form-item label="骨骼动画资源图" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['skeleton', validatorRules.skeleton]" placeholder="请输入骨骼动画资源图" />
        </a-form-item>
        <a-form-item label="免费抽奖次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['freeNum', validatorRules.freeNum]" placeholder="请输入免费抽奖次数" style="width: 100%" />
        </a-form-item>
        <a-form-item label="获奖记录显示数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['rewardRecordNum', validatorRules.rewardRecordNum]" placeholder="请输入获奖记录显示数量" style="width: 100%" />
        </a-form-item>
        <a-form-item label="获奖记录内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['rewardRecordMsg', validatorRules.rewardRecordMsg]" placeholder="请输入获奖记录内容" />
        </a-form-item>
        <a-form-item label="获奖传闻内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['rewardMsg', validatorRules.rewardMsg]" placeholder="请输入获奖传闻内容" />
        </a-form-item>
        <a-form-item label="概率公示" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['probabilityMsg', validatorRules.probabilityMsg]" placeholder="请输入概率公示" />
        </a-form-item>
        <a-form-item label="抽奖设置(单抽/多抽)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea
            v-decorator="['lotteryType', validatorRules.lotteryType]"
            placeholder='请输入抽奖设置(单抽/多抽) e.g. [{"itemId":1001, "num":1, "lotteryNum":1, "score":10}]'
          />
        </a-form-item>
        <a-form-item label="展示特奖" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['ssrShowReward', validatorRules.ssrShowReward]" placeholder='请输入展示特奖 e.g. [{"itemId":1001, "num":1}]'></a-input>
        </a-form-item>
        <a-form-item label="展示大奖" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['srShowReward', validatorRules.srShowReward]" placeholder='请输入展示大奖 e.g. [{"itemId":1001, "num":1}]'></a-input>
        </a-form-item>
        <a-form-item label="展示奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['showReward', validatorRules.showReward]" placeholder='请输入展示奖励 e.g. [{"itemId":1001, "num":1}]'></a-input>
        </a-form-item>
        <a-form-item label="重置大奖" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['resetReward', validatorRules.resetReward]" placeholder="请输入重置大奖 e.g. [1002, 1010, 1021, 1031]"></a-input>
        </a-form-item>
        <a-form-item label="奖池" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            v-decorator="['rewardPool', validatorRules.rewardPool]"
            placeholder='请输入奖池, e.g. [{"timeMin":1, "timeMax":20, "rewardPool":1}, {"timeMin":21, "timeMax":30, "rewardPool":2}]'
          ></a-input>
        </a-form-item>
        <a-form-item label="排名奖励邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['rankRewardEmailTitle', validatorRules.rankRewardEmailTitle]" placeholder="请输入排名奖励邮件标题" />
        </a-form-item>
        <a-form-item label="排名奖励邮件内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['rankRewardEmailContent', validatorRules.rankRewardEmailContent]" placeholder="请输入排名奖励邮件内容" />
        </a-form-item>
        <a-form-item label="积分奖励邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['scoreRewardEmailTitle', validatorRules.scoreRewardEmailTitle]" placeholder="请输入积分奖励邮件标题" />
        </a-form-item>
        <a-form-item label="积分奖励邮件内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['scoreRewardEmailContent', validatorRules.scoreRewardEmailContent]" placeholder="请输入积分奖励邮件内容" />
        </a-form-item>
        <a-form-item label="帮助信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['helpMsg', validatorRules.helpMsg]" placeholder="请输入帮助信息" />
        </a-form-item>
      </a-form>

      <a-tabs v-if="isEdit" defaultActiveKey="1">
        <a-tab-pane tab="奖池配置" forceRender key="1">
          <open-service-campaign-lottery-detail-pool-list ref="poolList"></open-service-campaign-lottery-detail-pool-list>
        </a-tab-pane>
        <a-tab-pane tab="积分道具" forceRender key="2">
          <open-service-campaign-lottery-detail-score-list ref="scoreList"></open-service-campaign-lottery-detail-score-list>
        </a-tab-pane>
        <a-tab-pane tab="榜单配置" forceRender key="3">
          <open-service-campaign-lottery-detail-ranking-list ref="rankingList"></open-service-campaign-lottery-detail-ranking-list>
        </a-tab-pane>
      </a-tabs>
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
import moment from 'moment';
import JDate from '@/components/jeecg/JDate';
import GameImageSelector from '../components/GameImageSelector';

import OpenServiceCampaignLotteryDetailPoolList from '../OpenServiceCampaignLotteryDetailPoolList';
import OpenServiceCampaignLotteryDetailScoreList from '../OpenServiceCampaignLotteryDetailScoreList';
import OpenServiceCampaignLotteryDetailRankingList from '../OpenServiceCampaignLotteryDetailRankingList';

export default {
  name: 'OpenServiceCampaignLotteryDetailModal',
  components: {
    JDate,
    GameImageSelector,
    OpenServiceCampaignLotteryDetailPoolList,
    OpenServiceCampaignLotteryDetailScoreList,
    OpenServiceCampaignLotteryDetailRankingList
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
        campaignTypeId: { rules: [{ required: true, message: '请输入页签id！' }] },
        timeType: { rules: [{ required: true, message: '请输入时间类型!' }] },
        startDay: { rules: [{ required: false, message: '请输入开始时间(开服第n天)!' }] },
        duration: { rules: [{ required: false, message: '请输入持续天数!' }] },
        startTime: { rules: [{ required: false, message: '请输入开始时间!' }] },
        endTime: { rules: [{ required: false, message: '请输入结束时间!' }] },
        tabName: { rules: [{ required: true, message: '请输入页签名称!' }] },
        name: { rules: [{ required: true, message: '请输入活动名称!' }] },
        freeNum: { rules: [{ required: true, message: '请输入免费抽奖次数!' }] },
        rewardRecordNum: { rules: [{ required: true, message: '请输入获奖记录显示数量!' }] },
        rewardRecordMsg: { rules: [{ required: true, message: '请输入获奖记录内容!' }] },
        rewardMsg: { rules: [{ required: true, message: '请输入获奖传闻内容!' }] },
        probabilityMsg: { rules: [{ required: true, message: '请输入概率公示!' }] },
        lotteryType: { rules: [{ required: true, message: '请输入抽奖设置(单抽/多抽)!' }] },
        ssrShowReward: { rules: [{ required: true, message: '请输入展示特奖!' }] },
        srShowReward: { rules: [{ required: true, message: '请输入展示大奖!' }] },
        showReward: { rules: [{ required: true, message: '请输入展示奖励!' }] },
        resetReward: { rules: [{ required: true, message: '请输入重置大奖' }] },
        rewardPool: { rules: [{ required: true, message: '请输入奖池!' }] },
        rankRewardEmailTitle: { rules: [{ required: true, message: '请输入排名奖励邮件标题!' }] },
        rankRewardEmailContent: { rules: [{ required: true, message: '请输入排名奖励邮件内容!' }] },
        scoreRewardEmailTitle: { rules: [{ required: true, message: '请输入积分奖励邮件标题!' }] },
        scoreRewardEmailContent: { rules: [{ required: true, message: '请输入积分奖励邮件内容!' }] },
        banner: {},
        skeleton: {},
        helpMsg: {}
      },
      url: {
        add: 'game/openServiceCampaignLotteryDetail/add',
        edit: 'game/openServiceCampaignLotteryDetail/edit'
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
      console.log('OpenServiceCampaignLotteryDetailModal, model:', JSON.stringify(this.model));

      this.$nextTick(() => {
        if (this.isEdit) {
          this.$refs.poolList.edit(record);
          this.$refs.scoreList.edit(record);
          this.$refs.rankingList.edit(record);
        }

        this.form.setFieldsValue(
          pick(
            this.model,
            'campaignId',
            'campaignTypeId',
            'timeType',
            'startDay',
            'duration',
            'startTime',
            'endTime',
            'tabName',
            'name',
            'banner',
            'skeleton',
            'freeNum',
            'rewardRecordNum',
            'rewardRecordMsg',
            'rewardMsg',
            'probabilityMsg',
            'lotteryType',
            'ssrShowReward',
            'srShowReward',
            'showReward',
            'resetReward',
            'rewardPool',
            'rankRewardEmailTitle',
            'rankRewardEmailContent',
            'scoreRewardEmailTitle',
            'scoreRewardEmailContent',
            'helpMsg'
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
          'campaignTypeId',
          'timeType',
          'startDay',
          'duration',
          'startTime',
          'endTime',
          'tabName',
          'name',
          'banner',
          'skeleton',
          'freeNum',
          'rewardRecordNum',
          'rewardRecordMsg',
          'rewardMsg',
          'probabilityMsg',
          'lotteryType',
          'ssrShowReward',
          'srShowReward',
          'showReward',
          'resetReward',
          'rewardPool',
          'rankRewardEmailTitle',
          'rankRewardEmailContent',
          'scoreRewardEmailTitle',
          'scoreRewardEmailContent',
          'helpMsg'
        )
      );
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    },
    handleTimeTypeChange(value) {
      this.model.timeType = value;
    }
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
