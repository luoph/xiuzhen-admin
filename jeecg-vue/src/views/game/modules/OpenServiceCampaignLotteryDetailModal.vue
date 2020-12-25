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
                <a-form-item label="活动页签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['tabName', validatorRules.tabName]" placeholder="请输入活动页签名称"></a-input>
                </a-form-item>
                <a-form-item label="开始时间(开服第n天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始时间(开服第n天, e.g. 0表示开服第1天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="持续时间(天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续时间(天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img
                        v-if="model.banner"
                        :src="getImgView(model.banner)"
                        height="100px"
                        :alt="getImgView(model.banner)"
                        style="max-width:100%;font-size: 12px;font-style: italic;"
                    />
                    <game-image-selector placeholder="请选择活动宣传图" v-model="model.banner" />
                </a-form-item>
                <a-form-item label="骨骼动画资源图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['skeleton', validatorRules.skeleton]" placeholder="请输入骨骼动画资源图"></a-input>
                </a-form-item>
                <a-form-item label="获奖记录显示数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rewardRecordNum', validatorRules.rewardRecordNum]" placeholder="请输入获奖记录显示数量" style="width: 100%" />
                </a-form-item>
                <a-form-item label="获奖记录内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['rewardRecordMsg', validatorRules.rewardRecordMsg]" placeholder="请输入获奖记录内容"></a-input>
                </a-form-item>
                <a-form-item label="获奖传闻内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['rewardMsg', validatorRules.rewardMsg]" placeholder="请输入获奖传闻内容"></a-input>
                </a-form-item>
                <a-form-item label="概率公示" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['probabilityMsg', validatorRules.probabilityMsg]" placeholder="请输入概率公示"></a-textarea>
                </a-form-item>
                <a-form-item label="抽奖设置(单抽/多抽)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input
                        v-decorator="['lotteryType', validatorRules.lotteryType]"
                        placeholder='请输入抽奖设置(单抽/多抽) e.g. [{"itemId":1001, "num":1, "lotteryNum":1, "score":10}]'
                    ></a-input>
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
                <a-form-item label="帮助信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['helpMsg', validatorRules.helpMsg]" placeholder="请输入帮助信息"></a-textarea>
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
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";
import GameImageSelector from "../components/GameImageSelector";

import OpenServiceCampaignLotteryDetailPoolList from "../OpenServiceCampaignLotteryDetailPoolList";
import OpenServiceCampaignLotteryDetailScoreList from "../OpenServiceCampaignLotteryDetailScoreList";
import OpenServiceCampaignLotteryDetailRankingList from "../OpenServiceCampaignLotteryDetailRankingList";

export default {
    name: "OpenServiceCampaignLotteryDetailModal",
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
            title: "操作",
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
                campaignId: { rules: [{ required: true, message: "请输入开服活动id, open_service_campaign.id!" }] },
                campaignTypeId: { rules: [{ required: true, message: "请输入open_service_campaign_type.id!" }] },
                startDay: { rules: [{ required: true, message: "请输入开始时间(开服第n天, e.g. 0表示开服第1天)!" }] },
                duration: { rules: [{ required: true, message: "请输入持续时间(天)!" }] },
                tabName: { rules: [{ required: true, message: "请输入活动页签名称!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称!" }] },
                banner: {},
                skeleton: {},
                rewardRecordNum: { rules: [{ required: true, message: "请输入获奖记录显示数量!" }] },
                rewardRecordMsg: { rules: [{ required: true, message: "请输入获奖记录内容!" }] },
                rewardMsg: { rules: [{ required: true, message: "请输入获奖传闻内容!" }] },
                probabilityMsg: { rules: [{ required: true, message: "请输入概率公示!" }] },
                lotteryType: { rules: [{ required: true, message: "请输入抽奖设置(单抽/多抽)!" }] },
                ssrShowReward: { rules: [{ required: true, message: "请输入展示特奖!" }] },
                srShowReward: { rules: [{ required: true, message: "请输入展示大奖!" }] },
                showReward: { rules: [{ required: true, message: "请输入展示奖励!" }] },
                resetReward: { rules: [{ required: true, message: "请输入重置大奖" }] },
                rewardPool: { rules: [{ required: true, message: "请输入奖池!" }] },
                helpMsg: {}
            },
            url: {
                add: "game/openServiceCampaignLotteryDetail/add",
                edit: "game/openServiceCampaignLotteryDetail/edit"
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
            console.log("OpenServiceCampaignLotteryDetailModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                if (this.isEdit) {
                    this.$refs.poolList.edit(record);
                    this.$refs.scoreList.edit(record);
                    this.$refs.rankingList.edit(record);
                }

                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "campaignId",
                        "campaignTypeId",
                        "startDay",
                        "duration",
                        "tabName",
                        "name",
                        "banner",
                        "skeleton",
                        "rewardRecordNum",
                        "rewardRecordMsg",
                        "rewardMsg",
                        "probabilityMsg",
                        "lotteryType",
                        "ssrShowReward",
                        "srShowReward",
                        "showReward",
                        "resetReward",
                        "rewardPool",
                        "helpMsg"
                    )
                );
            });
        },
        close() {
            this.$emit("close");
            this.visible = false;
        },
        handleOk() {
            const that = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    that.confirmLoading = true;
                    let httpUrl = "";
                    let method = "";
                    if (!this.model.id) {
                        httpUrl += this.url.add;
                        method = "post";
                    } else {
                        httpUrl += this.url.edit;
                        method = "put";
                    }
                    let formData = Object.assign(this.model, values);
                    console.log("表单提交数据", formData);
                    httpAction(httpUrl, formData, method)
                        .then(res => {
                            if (res.success) {
                                that.$message.success(res.message);
                                that.$emit("ok");
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
                    "campaignId",
                    "campaignTypeId",
                    "startDay",
                    "duration",
                    "tabName",
                    "name",
                    "banner",
                    "skeleton",
                    "rewardRecordNum",
                    "rewardRecordMsg",
                    "rewardMsg",
                    "probabilityMsg",
                    "lotteryType",
                    "ssrShowReward",
                    "srShowReward",
                    "showReward",
                    "resetReward",
                    "rewardPool",
                    "helpMsg"
                )
            );
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return `${window._CONFIG["domainURL"]}/${text}`;
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
