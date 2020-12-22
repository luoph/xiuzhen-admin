<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="typeId" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignTypeId', validatorRules.campaignTypeId]" placeholder="请输入typeId" style="width: 100%" />
                </a-form-item>
                <a-form-item label="open_service_single_recharge_gift_detail.id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['giftDetailId', validatorRules.giftDetailId]" placeholder="请输入open_service_single_recharge_gift_detail.id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="播放次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['num', validatorRules.num]" placeholder="请输入播放次数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="消息内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['message']" rows="4" placeholder="请输入消息内容"/>
                </a-form-item>
                <a-form-item label="发送方式 0-非邮件 1-邮件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['email', validatorRules.email]" placeholder="请输入发送方式 0-非邮件 1-邮件" style="width: 100%" />
                </a-form-item>
                <a-form-item label="发送时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择发送时间" v-decorator="['sendTime', validatorRules.sendTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="createTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择createTime" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="updateTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择updateTime" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建者" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['createBy', validatorRules.createBy]" placeholder="请输入创建者"></a-input>
                </a-form-item>
                <a-form-item label="修改者" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['updateBy', validatorRules.updateBy]" placeholder="请输入修改者"></a-input>
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
    name: "OpenServiceCampaignSingleGiftNoticeModal",
    components: {
        JDate,
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
                campaignId: {},
                campaignTypeId: { rules: [{ required: true, message: "请输入typeId!" }] },
                giftDetailId: { rules: [{ required: true, message: "请输入open_service_single_recharge_gift_detail.id!" }] },
                num: { rules: [{ required: true, message: "请输入播放次数!" }] },
                message: {},
                email: { rules: [{ required: true, message: "请输入发送方式 0-非邮件 1-邮件!" }] },
                sendTime: {},
                createTime: {},
                updateTime: {},
                createBy: {},
                updateBy: {},
            },
            url: {
                add: "game/openServiceCampaignSingleGiftNotice/add",
                edit: "game/openServiceCampaignSingleGiftNotice/edit"
            }
        };
    },
    created() {
    },
    methods: {
        add() {
            this.edit({});
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "campaignTypeId", "giftDetailId", "num", "message", "email", "sendTime", "createTime", "updateTime", "createBy", "updateBy"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "campaignTypeId", "giftDetailId", "num", "message", "email", "sendTime", "createTime", "updateTime", "createBy", "updateBy"));
        },
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