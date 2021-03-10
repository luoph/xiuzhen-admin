<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="活动" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="子活动" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="道具id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['itemId', validatorRules.itemId]" placeholder="请输入道具id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="消耗道具" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['costItemId', validatorRules.costItemId]" placeholder="请输入消耗道具" style="width: 100%" />
                </a-form-item>
                <a-form-item label="消耗道具数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['costNum', validatorRules.costNum]" placeholder="请输入消耗道具数量" style="width: 100%" />
                </a-form-item>
                <a-form-item label="库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['stack', validatorRules.stack]" placeholder="请输入库存" style="width: 100%" />
                </a-form-item>
                <a-form-item label="原价" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['amount', validatorRules.amount]" placeholder="请输入原价" style="width: 100%" />
                </a-form-item>
                <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入折扣" style="width: 100%" />
                </a-form-item>
                <a-form-item label="限购条件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['limitCondition', validatorRules.limitCondition]" placeholder="请输入限购条件"></a-input>
                </a-form-item>
                <a-form-item label="显示奖励内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['showReward', validatorRules.showReward]" placeholder="请输入显示奖励内容"></a-input>
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

export default {
    name: "GameCampaignTypeThrowingEggsGiftModal",
    components: {
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
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
                typeId: { rules: [{ required: true, message: "请输入子活动id!" }] },
                itemId: { rules: [{ required: true, message: "请输入道具id!" }] },
                costItemId: { rules: [{ required: true, message: "请输入消耗道具!" }] },
                costNum: { rules: [{ required: true, message: "请输入消耗道具数量!" }] },
                stack: { rules: [{ required: true, message: "请输入库存!" }] },
                amount: {},
                discount: {},
                limitCondition: {},
                showReward: { rules: [{ required: true, message: "请输入显示奖励内容!" }] },
            },
            url: {
                add: "game/gameCampaignTypeThrowingEggsGift/add",
                edit: "game/gameCampaignTypeThrowingEggsGift/edit"
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
            this.isEdit = this.model.id != null;
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "itemId", "costItemId", "costNum", "stack", "amount", "discount", "limitCondition", "showReward"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "itemId", "costItemId", "costNum", "stack", "amount", "discount", "limitCondition", "showReward"));
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
