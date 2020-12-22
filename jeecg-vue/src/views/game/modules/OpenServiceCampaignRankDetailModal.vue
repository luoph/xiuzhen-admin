<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignTypeId', validatorRules.campaignTypeId]" placeholder="请输入页签id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
                </a-form-item>
                <a-form-item label="活动页签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['tabName', validatorRules.tabName]" placeholder="请输入活动页签名称"></a-input>
                </a-form-item>
                <a-form-item label="排行类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择排行类型" v-decorator="['rankType', validatorRules.rankType]" initialValue="1">
                        <a-select-option :value="1">1-境界冲榜</a-select-option>
                        <a-select-option :value="2">2-功法冲榜</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="开始时间(开服第n天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始时间(开服第n天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="持续时间(天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续时间(天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['banner', validatorRules.banner]" placeholder="请输入活动宣传图"></a-input>
                </a-form-item>
                <a-form-item label="活动宣传奖励图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['rewardImg', validatorRules.rewardImg]" placeholder="请输入活动宣传奖励图"></a-input>
                </a-form-item>
                <a-form-item label="活动宣传仙力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['combatPower', validatorRules.combatPower]" placeholder="请输入活动宣传仙力" style="width: 100%" />
                </a-form-item>
                <a-form-item label="排行玩家数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rankNum', validatorRules.rankNum]" placeholder="请输入排行玩家数量" style="width: 100%" />
                </a-form-item>
                <a-form-item label="排名奖励邮件id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rankRewardEmail', validatorRules.rankRewardEmail]" placeholder="请输入排名奖励邮件id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="达标奖励邮件id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['standardRewardEmail', validatorRules.standardRewardEmail]" placeholder="请输入达标奖励邮件id" style="width: 100%" />
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
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
    name: "OpenServiceCampaignRankDetailModal",
    components: {
        JDate
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
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
                campaignId: { rules: [{ required: true, message: "请输入开服活动id!" }] },
                campaignTypeId: { rules: [{ required: true, message: "请输入typeId!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称!" }] },
                tabName: { rules: [{ required: true, message: "请输入活动页签名称!" }] },
                rankType: { rules: [{ required: true, message: "请输入排行类型!" }] },
                startDay: { rules: [{ required: true, message: "请输入开始时间(开服第n天)!" }] },
                duration: { rules: [{ required: true, message: "请输入持续时间(天)!" }] },
                banner: { rules: [{ required: true, message: "请输入活动宣传图!" }] },
                rewardImg: { rules: [{ required: true, message: "请输入活动宣传奖励图!" }] },
                combatPower: { rules: [{ required: true, message: "请输入活动宣传仙力!" }] },
                rankNum: { rules: [{ required: true, message: "请输入排行玩家数量!" }] },
                rankRewardEmail: { rules: [{ required: true, message: "请输入排名奖励邮件id!" }] },
                standardRewardEmail: { rules: [{ required: true, message: "请输入达标奖励邮件id!" }] }
            },
            url: {
                add: "game/openServiceCampaignRankDetail/add",
                edit: "game/openServiceCampaignRankDetail/edit"
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
            console.log("OpenServiceCampaignRankDetailModal, mode:", JSON.stringify(this.model));

            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "campaignId",
                        "campaignTypeId",
                        "name",
                        "tabName",
                        "rankType",
                        "startDay",
                        "duration",
                        "banner",
                        "rewardImg",
                        "combatPower",
                        "rankNum",
                        "rankRewardEmail",
                        "standardRewardEmail"
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
                    "name",
                    "tabName",
                    "rankType",
                    "startDay",
                    "duration",
                    "banner",
                    "rewardImg",
                    "combatPower",
                    "rankNum",
                    "rankRewardEmail",
                    "standardRewardEmail"
                )
            );
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
