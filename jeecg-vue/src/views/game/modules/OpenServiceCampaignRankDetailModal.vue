<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]"
                          placeholder="请输入开服活动id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignTypeId', validatorRules.campaignTypeId]"
                          placeholder="请输入页签id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"/>
        </a-form-item>
        <a-form-item label="页签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['tabName', validatorRules.tabName]" placeholder="请输入页签名称"/>
        </a-form-item>
        <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="排行类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select :disabled="isEdit" placeholder="请选择排行类型" v-decorator="['rankType', validatorRules.rankType]"
                    initialValue="1">
            <a-select-option :value="1">1-境界排行</a-select-option>
            <a-select-option :value="2">2-仙兽排行</a-select-option>
            <a-select-option :value="3">3-义戒排行</a-select-option>
            <a-select-option :value="4">4-飞剑排行</a-select-option>
            <a-select-option :value="5">5-天书排行</a-select-option>
            <a-select-option :value="6">6-圣灵排行</a-select-option>
            <a-select-option :value="7">7-法宝排行</a-select-option>
            <a-select-option :value="8">8-情饰排行</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="时间类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择活动类型" @change="handleTimeTypeChange"
                    v-decorator="['timeType', validatorRules.timeType]" initialValue="1">
            <a-select-option :value="1">1-时间范围</a-select-option>
            <a-select-option :value="2">2-开服第N天</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-show="model.timeType == 1" label="活动时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-form-item>
            <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss"
                           v-decorator="['startTime', validatorRules.startTime]"/>
          </a-form-item>
          <a-form-item>
            <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss"
                           v-decorator="['endTime', validatorRules.endTime]"/>
          </a-form-item>
        </a-form-item>
        <a-form-item v-show="model.timeType == 2" label="开始天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['startDay', validatorRules.startDay]"
                          placeholder="请输入开始天数(开服第n天, 0表示开服第1天)" style="width: 100%"/>
        </a-form-item>
        <a-form-item v-show="model.timeType == 2" label="持续天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续天数"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <img v-if="model.banner" :src="getImgView(model.banner)" :alt="getImgView(model.banner)"
               class="banner-image"/>
          <game-image-selector placeholder="请选择活动宣传图" v-model="model.banner"/>
        </a-form-item>
        <a-form-item label="奖励图" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <img v-if="model.rewardImg" :src="getImgView(model.rewardImg)" :alt="getImgView(model.rewardImg)"
               class="icon-image"/>
          <game-image-selector placeholder="请选择活动宣传图" v-model="model.rewardImg"/>
        </a-form-item>
        <a-form-item label="活动宣传仙力" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['combatPower', validatorRules.combatPower]" placeholder="请输入活动宣传仙力"
                          style="width: 100%"/>
        </a-form-item>
        <!-- <a-form-item label="排行玩家数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rankNum', validatorRules.rankNum]" placeholder="请输入排行玩家数量" style="width: 100%" />
                </a-form-item> -->
        <a-form-item label="排名奖励邮件id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['rankRewardEmail', validatorRules.rankRewardEmail]"
                          placeholder="请输入排名奖励邮件id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="达标奖励邮件id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['standardRewardEmail', validatorRules.standardRewardEmail]"
                          placeholder="请输入达标奖励邮件id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="跳转id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['jump', validatorRules.jump]" placeholder="请输入跳转id"/>
        </a-form-item>
        <a-form-item label="帮助信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['helpMsg', validatorRules.helpMsg]" placeholder="请输入帮助信息"/>
        </a-form-item>
      </a-form>

      <a-tabs v-if="isEdit" defaultActiveKey="1">
        <a-tab-pane tab="传闻消息" forceRender key="1">
          <open-service-campaign-rank-detail-message-list
            ref="messageList"></open-service-campaign-rank-detail-message-list>
        </a-tab-pane>
        <a-tab-pane tab="达标奖励" forceRender key="2">
          <open-service-campaign-rank-detail-standard-list
            ref="standardList"></open-service-campaign-rank-detail-standard-list>
        </a-tab-pane>
        <a-tab-pane tab="消耗道具" forceRender key="3">
          <open-service-campaign-rank-detail-score-list ref="scoreList"></open-service-campaign-rank-detail-score-list>
        </a-tab-pane>
        <a-tab-pane tab="榜单配置" forceRender key="4">
          <open-service-campaign-rank-detail-ranking-list
            ref="rankingList"></open-service-campaign-rank-detail-ranking-list>
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
import {httpAction} from '@/api/manage';
import pick from 'lodash.pick';
import moment from 'moment';
import JDate from '@/components/jeecg/JDate';
import GameImageSelector from '../components/GameImageSelector';

import OpenServiceCampaignRankDetailMessageList from '../OpenServiceCampaignRankDetailMessageList';
import OpenServiceCampaignRankDetailRankingList from '../OpenServiceCampaignRankDetailRankingList';
import OpenServiceCampaignRankDetailScoreList from '../OpenServiceCampaignRankDetailScoreList';
import OpenServiceCampaignRankDetailStandardList from '../OpenServiceCampaignRankDetailStandardList';

export default {
  name: 'OpenServiceCampaignRankDetailModal',
  components: {
    JDate,
    GameImageSelector,
    OpenServiceCampaignRankDetailMessageList,
    OpenServiceCampaignRankDetailRankingList,
    OpenServiceCampaignRankDetailScoreList,
    OpenServiceCampaignRankDetailStandardList
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
        xs: {span: 24},
        sm: {span: 5}
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16}
      },
      confirmLoading: false,
      validatorRules: {
        campaignId: {rules: [{required: true, message: '请输入开服活动id!'}]},
        campaignTypeId: {rules: [{required: true, message: '请输入typeId!'}]},
        name: {rules: [{required: true, message: '请输入活动名称!'}]},
        tabName: {rules: [{required: true, message: '请输入页签名称!'}]},
        rankType: {rules: [{required: true, message: '请输入排行类型!'}]},
        timeType: {rules: [{required: true, message: '请输入时间类型!'}]},
        startTime: {rules: [{required: this.timeType == 1, message: '请输入开始时间!'}]},
        endTime: {rules: [{required: this.timeType == 1, message: '请输入结束时间!'}]},
        startDay: {rules: [{required: this.timeType == 2, message: '请输入开始时间(开服第n天)!'}]},
        duration: {rules: [{required: this.timeType == 2, message: '请输入持续天数!'}]},
        sort: {rules: [{required: true, message: '请输入排序!'}]},
        banner: {rules: [{required: true, message: '请输入活动宣传图!'}]},
        rewardImg: {rules: [{required: true, message: '请输入奖励图!'}]},
        combatPower: {rules: [{required: true, message: '请输入活动宣传仙力!'}]},
        // rankNum: { rules: [{ required: true, message: "请输入排行玩家数量!" }] },
        helpMsg: {rules: [{required: true, message: '请输入帮助信息!'}]},
        jump: {rules: [{required: false, message: '请输入跳转id!'}]},
        rankRewardEmail: {rules: [{required: true, message: '请输入排名奖励邮件id!'}]},
        standardRewardEmail: {rules: [{required: true, message: '请输入达标奖励邮件id!'}]}
      },
      url: {
        add: 'game/openServiceCampaignRankDetail/add',
        edit: 'game/openServiceCampaignRankDetail/edit'
      }
    };
  },
  created() {
  },
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
      console.log('OpenServiceCampaignRankDetailModal, model:', JSON.stringify(this.model));

      this.$nextTick(() => {
        if (this.isEdit) {
          this.$refs.messageList.edit(record);
          this.$refs.standardList.edit(record);
          this.$refs.scoreList.edit(record);
          this.$refs.rankingList.edit(record);
        }

        this.form.setFieldsValue(
          pick(
            this.model,
            'campaignId',
            'campaignTypeId',
            'name',
            'tabName',
            'sort',
            'rankType',
            'timeType',
            'startDay',
            'duration',
            'startTime',
            'endTime',
            'banner',
            'rewardImg',
            'combatPower',
            'rankNum',
            'rankRewardEmail',
            'standardRewardEmail',
            'helpMsg',
            'jump'
          )
        );

        // 时间格式化
        this.form.setFieldsValue({startTime: this.model.startTime ? moment(this.model.startTime) : null});
        this.form.setFieldsValue({endTime: this.model.endTime ? moment(this.model.endTime) : null});
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
          'name',
          'tabName',
          'sort',
          'rankType',
          'timeType',
          'startDay',
          'duration',
          'startTime',
          'endTime',
          'banner',
          'rewardImg',
          'combatPower',
          'rankNum',
          'rankRewardEmail',
          'standardRewardEmail',
          'helpMsg',
          'jump'
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
.icon-image {
  width: auto;
  height: auto;
  display: block;
  max-width: 180px;
  max-height: 180px;
  object-fit: scale-down;
}

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
