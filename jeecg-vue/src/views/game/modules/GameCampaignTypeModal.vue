<template>
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select :disabled="isEdit" placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1" @change="selectType">
                        <a-select-option :value="1">1-登录礼包</a-select-option>
                        <a-select-option :value="2">2-累计充值</a-select-option>
                        <a-select-option :value="3">3-节日兑换</a-select-option>
                        <a-select-option :value="4">4-节日任务</a-select-option>
                        <a-select-option :value="5">5-修为加成</a-select-option>
                        <a-select-option :value="6">6-灵气加成</a-select-option>
                        <a-select-option :value="7">7-节日掉落</a-select-option>
                        <a-select-option :value="8">8-节日烟花</a-select-option>
                        <a-select-option :value="9">9-消费排行</a-select-option>
                        <a-select-option :value="10">10-限时仙剑</a-select-option>
                        <a-select-option :value="11">11-节日砸蛋</a-select-option>
                        <a-select-option :value="12">12-砸蛋排行</a-select-option>
                        <a-select-option :value="13">13-砸蛋礼包</a-select-option>
                        <a-select-option :value="14">14-节日派对</a-select-option>
                        <a-select-option :value="15">15-直购礼包</a-select-option>
                        <a-select-option :value="16">16-返利狂欢</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="页签名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入页签名" />
                </a-form-item>
                <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img v-if="model.typeImage" :src="getImgView(model.typeImage)" :alt="getImgView(model.typeImage)" class="banner-image" />
                    <game-image-selector placeholder="请选择活动宣传图" v-model="model.typeImage" />
                </a-form-item>
                <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
                </a-form-item>
                <a-form-item label="额外参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['extra', validatorRules.extra]" placeholder="请输入额外参数" />
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
                <a-form-item label="时间类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="选择活动类型" v-decorator="['timeType', validatorRules.timeType]" initialValue="1">
                        <a-select-option :value="1">1-时间范围</a-select-option>
                        <a-select-option :value="2">2-开服第N天</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="开始天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始天数(开服第n天, 0表示开服第1天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="持续天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续天数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" style="width: 100%" />
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
            <game-campaign-direct-purchase-list v-if="isEdit && model.type === 15" ref="directPurchaseList" />
            <game-campaign-type-rebate-recharge-list v-if="isEdit && model.type === 16" ref="rebateRechargeList" />
        </a-spin>
    </a-modal>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";
import moment from "moment";
import GameImageSelector from "../components/GameImageSelector";
import GameCampaignTypeLoginList from "../GameCampaignTypeLoginList";
import GameCampaignTypeRechargeList from "../GameCampaignTypeRechargeList";
import GameCampaignTypeExchangeList from "../GameCampaignTypeExchangeList";
import GameCampaignTypeTaskList from "../GameCampaignTypeTaskList";
import GameCampaignTypeBuffList from "../GameCampaignTypeBuffList";
import GameCampaignTypeFallList from "../GameCampaignTypeFallList";
import GameCampaignTypeFallRewardList from "../GameCampaignTypeFallRewardList";
import GameCampaignTypeFireworkList from "../GameCampaignTypeFireworkList";
import GameCampaignTypeReduceList from "../GameCampaignTypeReduceList";
import GameCampaignTypeSwordList from "../GameCampaignTypeSwordList";
import GameCampaignTypeThrowingEggsList from "../GameCampaignTypeThrowingEggsList";
import GameCampaignTypeThrowingEggsRankList from "../GameCampaignTypeThrowingEggsRankList";
import GameCampaignTypeThrowingEggsGiftList from "../GameCampaignTypeThrowingEggsGiftList";
import GameCampaignTypePartyProgressList from "../GameCampaignTypePartyProgressList";
import GameCampaignTypePartyTaskList from "../GameCampaignTypePartyTaskList";
import GameCampaignDirectPurchaseList from "../GameCampaignDirectPurchaseList";
import GameCampaignTypeRebateRechargeList from "../GameCampaignTypeRebateRechargeList";

export default {
    name: "GameCampaignTypeModal",
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
        GameCampaignTypeRebateRechargeList
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
                campaignId: { rules: [{ required: true, message: "请输入活动id!" }] },
                type: { rules: [{ required: true, message: "请输入活动类型" }] },
                typeImage: { rules: [{ required: true, message: "请输入活动类型图片!" }] },
                sort: { rules: [{ required: true, message: "请输入排序!" }] },
                extra: { rules: [{ required: false, message: "请输入额外参数!" }] },
                resType: { rules: [{ required: false, message: "请选择资源类型" }] },
                animation: { rules: [{ required: false, message: "请输入资源类型!" }] },
                timeType: { rules: [{ required: true, message: "请输入时间类型!" }] },
                startDay: { rules: [{ required: false, message: "请输入开始时间(开服第n天)!" }] },
                duration: { rules: [{ required: false, message: "请输入持续天数!" }] },
                startTime: { rules: [{ required: false, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: false, message: "请输入结束时间!" }] },
                eggsIntegralGoods: { rules: [{ required: this.type === 11, message: "该活动需要对应的商品" }] }
            },
            url: {
                add: "game/gameCampaignType/add",
                edit: "game/gameCampaignType/edit"
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
            console.log("GameCampaignTypeModal, model:", JSON.stringify(this.model));

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
                }

                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "campaignId",
                        "name",
                        "type",
                        "typeImage",
                        "sort",
                        "extra",
                        "resType",
                        "animation",
                        "eggsIntegralGoods",
                        "timeType",
                        "startDay",
                        "duration",
                        "startTime",
                        "endTime"
                    )
                );
                // 时间格式化
                this.form.setFieldsValue({ startTime: this.model.startTime ? moment(this.model.startTime) : null });
                this.form.setFieldsValue({ endTime: this.model.endTime ? moment(this.model.endTime) : null });
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
                    // 时间格式化
                    formData.startTime = formData.startTime ? formData.startTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.endTime = formData.endTime ? formData.endTime.format("YYYY-MM-DD HH:mm:ss") : null;

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
                    "name",
                    "type",
                    "typeImage",
                    "sort",
                    "extra",
                    "resType",
                    "animation",
                    "eggsIntegralGoods",
                    "timeType",
                    "startDay",
                    "duration",
                    "startTime",
                    "endTime"
                )
            );
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return `${window._CONFIG["domainURL"]}/${text}`;
        },
        selectType(value) {
            this.model.type = value;
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
