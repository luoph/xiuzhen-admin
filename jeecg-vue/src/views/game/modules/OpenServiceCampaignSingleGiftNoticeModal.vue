<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignTypeId', validatorRules.campaignTypeId]" placeholder="请输入typeId" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签详情id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['giftDetailId', validatorRules.giftDetailId]" placeholder="请输入giftDetailId" style="width: 100%" />
                </a-form-item>
                <a-form-item label="消息内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['message']" rows="4" placeholder="请输入消息内容" />
                </a-form-item>
                <a-form-item label="播放次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['num', validatorRules.num]" placeholder="请输入播放次数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否发送邮件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="是否发送邮件" v-decorator="['type', validatorRules.email]" initialValue="1">
                        <a-select-option :value="0">否</a-select-option>
                        <a-select-option :value="1">是</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="发送时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker placeholder="传闻推送时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['sendTime', validatorRules.sendTime]" style="width: 100%;" />
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
import moment from "moment";

export default {
    name: "OpenServiceCampaignSingleGiftNoticeModal",
    components: {
        JDate
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
                giftDetailId: { rules: [{ required: true, message: "请输入页签详情id!" }] },
                num: { rules: [{ required: true, message: "请输入播放次数!" }] },
                message: { rules: [{ required: true, message: "请输入传闻内容!" }] },
                email: { rules: [{ required: true, message: "请输入是否发送邮件!" }] },
                sendTime: { rules: [{ required: true, message: "请输入是否发送邮件!" }] }
            },
            url: {
                add: "game/openServiceCampaignSingleGiftNotice/add",
                edit: "game/openServiceCampaignSingleGiftNotice/edit"
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
            console.log("OpenServiceCampaignSingleGiftNoticeModal, model:", JSON.stringify(this.model));
            this.visible = true;

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "campaignTypeId", "giftDetailId", "num", "message", "email", "sendTime"));
                this.form.setFieldsValue({ sendTime: this.model.sendTime ? moment(this.model.sendTime) : null });
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
                    formData.sendTime = formData.sendTime ? formData.sendTime.format("YYYY-MM-DD HH:mm:ss") : null;
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
            this.form.setFieldsValue(pick(row, "campaignId", "campaignTypeId", "giftDetailId", "num", "message", "email", "sendTime"));
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
