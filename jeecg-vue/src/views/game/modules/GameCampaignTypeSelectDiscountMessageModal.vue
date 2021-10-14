<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入主活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="传闻推送时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['pushTime', validatorRules.pushTime]" placeholder="请输入推送时间 时分秒 12:00:00"></a-input>
                </a-form-item>
                <a-form-item label="传闻内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['content', validatorRules.content]" placeholder="请输入传闻内容"></a-input>
                </a-form-item>
                <a-form-item label="传闻广播次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['num', validatorRules.num]" placeholder="请输入传闻广播次数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="全服广播邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['emailTitle']" rows="4" placeholder="请输入全服广播邮件标题"/>
                </a-form-item>
                <a-form-item label="全服广播邮件内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['emailContent']" rows="4" placeholder="请输入全服广播邮件内容"/>
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
    name: "GameCampaignTypeSelectDiscountMessageModal",
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
                campaignId: { rules: [{ required: true, message: "请输入主活动id!" }] },
                typeId: { rules: [{ required: true, message: "请输入子活动id!" }] },
                pushTime: {},
                content: {},
                num: {},
                emailTitle: {},
                emailContent: {},
            },
            url: {
                add: "game/gameCampaignTypeSelectDiscountMessage/add",
                edit: "game/gameCampaignTypeSelectDiscountMessage/edit"
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
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "pushTime", "content", "num", "emailTitle", "emailContent"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "pushTime", "content", "num", "emailTitle", "emailContent"));
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
