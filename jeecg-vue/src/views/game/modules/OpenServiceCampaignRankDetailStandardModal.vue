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
                <a-form-item label="详情id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['rankDetailId', validatorRules.rankDetailId]" placeholder="请输入详情id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="达标奖励积分" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['score', validatorRules.score]" placeholder="请输入达标奖励积分" style="width: 100%" />
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['description', validatorRules.description]" placeholder="请输入描述"></a-input>
                </a-form-item>
                <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励列表"></a-textarea>
                </a-form-item>
                <a-form-item label="传闻内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['message', validatorRules.message]" placeholder="请输入传闻内容"></a-textarea>
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
    name: "OpenServiceCampaignRankDetailStandardModal",
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
                rankDetailId: { rules: [{ required: true, message: "请输入详情id!" }] },
                score: { rules: [{ required: true, message: "请输入达标奖励积分!" }] },
                description: { rules: [{ required: true, message: "请输入描述!" }] },
                reward: { rules: [{ required: true, message: "请输入奖励列表!" }] },
                message: { rules: [{ required: true, message: "请输入传闻内容!" }] }
            },
            url: {
                add: "game/openServiceCampaignRankDetailStandard/add",
                edit: "game/openServiceCampaignRankDetailStandard/edit"
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
            this.isEdit = this.model.id != null;
            console.log("OpenServiceCampaignRankDetailStandardModal, model:", JSON.stringify(this.model));
            this.visible = true;

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "campaignTypeId", "rankDetailId", "score", "description", "reward", "message"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "campaignTypeId", "rankDetailId", "score", "description", "reward", "message"));
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
