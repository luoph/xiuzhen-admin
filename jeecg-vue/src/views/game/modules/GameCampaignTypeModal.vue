<template>
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select :disabled="isEdit" placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                        <a-select-option :value="1">1-登录礼包</a-select-option>
                        <a-select-option :value="2">2-累计充值</a-select-option>
                        <a-select-option :value="3">3-兑换</a-select-option>
                        <a-select-option :value="4">4-节日任务</a-select-option>
                        <a-select-option :value="5">5-Buff-修为加成</a-select-option>
                        <a-select-option :value="6">6-Buff-灵气加成</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="页签名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入页签名" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img v-if="model.typeImage" :src="getImgView(model.typeImage)" :alt="getImgView(model.typeImage)" class="banner-image" />
                    <game-image-selector placeholder="请选择活动宣传图" v-model="model.typeImage" />
                </a-form-item>
                <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
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

export default {
    name: "GameCampaignTypeModal",
    components: {
        JDate,
        GameImageSelector,
        GameCampaignTypeLoginList,
        GameCampaignTypeRechargeList,
        GameCampaignTypeExchangeList,
        GameCampaignTypeTaskList,
        GameCampaignTypeBuffList
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
                startTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] }
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
                    } else if (this.$refs.rechargeList) {
                        this.$refs.rechargeList.edit(record);
                    } else if (this.$refs.exchangeList) {
                        this.$refs.exchangeList.edit(record);
                    } else if (this.$refs.taskList) {
                        this.$refs.taskList.edit(record);
                    } else if (this.$refs.buffList) {
                        this.$refs.buffList.edit(record);
                    }
                }

                this.form.setFieldsValue(pick(this.model, "campaignId", "name", "type", "typeImage", "sort", "startTime", "endTime"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "name", "type", "typeImage", "sort", "startTime", "endTime"));
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
