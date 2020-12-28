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
                    <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始时间(开服第n天, 0表示开服第1天)" style="width: 100%" />
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
                <a-form-item label="消耗奖励邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['consumeRewardEmailTitle', validatorRules.consumeRewardEmailTitle]" placeholder="请输入消耗奖励邮件标题"></a-textarea>
                </a-form-item>
                <a-form-item label="消耗奖励邮件内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['consumeRewardEmailContent', validatorRules.consumeRewardEmailContent]" placeholder="请输入消耗奖励邮件内容"></a-textarea>
                </a-form-item>
                <a-form-item label="帮助信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['helpMsg', validatorRules.helpMsg]" placeholder="请输入帮助信息"></a-textarea>
                </a-form-item>
            </a-form>

            <a-tabs v-if="isEdit" defaultActiveKey="1">
                <a-tab-pane tab="消耗配置" forceRender key="1">
                    <open-service-campaign-consume-detail-item-list ref="itemList"></open-service-campaign-consume-detail-item-list>
                </a-tab-pane>
                <a-tab-pane tab="传闻配置" forceRender key="2">
                    <open-service-campaign-consume-detail-message-list ref="messageList"></open-service-campaign-consume-detail-message-list>
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

import OpenServiceCampaignConsumeDetailItemList from "../OpenServiceCampaignConsumeDetailItemList";
import OpenServiceCampaignConsumeDetailMessageList from "../OpenServiceCampaignConsumeDetailMessageList";

export default {
    name: "OpenServiceCampaignConsumeDetailModal",
    components: {
        JDate,
        GameImageSelector,
        OpenServiceCampaignConsumeDetailItemList,
        OpenServiceCampaignConsumeDetailMessageList
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
                campaignId: { rules: [{ required: true, message: "请输入开服活动id!" }] },
                campaignTypeId: { rules: [{ required: true, message: "请输入页签id!" }] },
                startDay: { rules: [{ required: true, message: "请输入开始时间(开服第n天)!" }] },
                duration: { rules: [{ required: true, message: "请输入持续时间(天)!" }] },
                tabName: { rules: [{ required: true, message: "请输入活动页签名称!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称!" }] },
                banner: { rules: [{ required: false, message: "请输入活动宣传图!" }] },
                consumeRewardEmailTitle: { rules: [{ required: true, message: "请输入消耗奖励邮件标题!" }] },
                consumeRewardEmailContent: { rules: [{ required: true, message: "请输入消耗奖励邮件内容!" }] },
                helpMsg: { rules: [{ required: true, message: "请输入帮助信息!" }] }
            },
            url: {
                add: "game/openServiceCampaignConsumeDetail/add",
                edit: "game/openServiceCampaignConsumeDetail/edit"
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
            console.log("OpenServiceCampaignConsumeDetailModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                if (this.isEdit) {
                    this.$refs.itemList.edit(record);
                    this.$refs.messageList.edit(record);
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
                        "consumeRewardEmailTitle",
                        "consumeRewardEmailContent",
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
                pick(row, "campaignId", "campaignTypeId", "startDay", "duration", "tabName", "name", "banner", "consumeRewardEmailTitle", "consumeRewardEmailContent", "helpMsg")
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
.image {
    width: 100%;
    height: 100px;
    object-fit: contain;
}

/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>
