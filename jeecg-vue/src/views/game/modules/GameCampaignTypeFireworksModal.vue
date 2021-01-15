<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入页签id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="礼包id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['giftId', validatorRules.giftId]" placeholder="请输入礼包id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="购买次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['times', validatorRules.times]" placeholder="请输入购买次数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="价格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['price', validatorRules.price]" placeholder="请输入价格"></a-input>
                </a-form-item>
                <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入折扣" style="width: 100%" />
                </a-form-item>
                <a-form-item label="单次购买数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['num', validatorRules.num]" placeholder="请输入单次购买数量" style="width: 100%" />
                </a-form-item>
                <a-form-item label="按钮标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['btnName', validatorRules.btnName]" placeholder="请输入按钮标题"></a-input>
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
    name: "GameCampaignTypeFireworksModal",
    components: {
        JDate
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
                typeId: { rules: [{ required: true, message: "请输入页签id!" }] },
                giftId: { rules: [{ required: true, message: "请输入礼包id!" }] },
                times: { rules: [{ required: true, message: "请输入购买次数!" }] },
                price: { rules: [{ required: true, message: "请输入价格!" }] },
                discount: { rules: [{ required: true, message: "请输入折扣!" }] },
                num: { rules: [{ required: true, message: "请输入单次购买数量!" }] },
                btnName: { rules: [{ required: true, message: "请输入按钮标题!" }] }
            },
            url: {
                add: "game/gameCampaignTypeFireworks/add",
                edit: "game/gameCampaignTypeFireworks/edit"
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
            console.log("GameCampaignTypeFireworksModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "giftId", "times", "price", "discount", "num", "btnName"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "giftId", "times", "price", "discount", "num", "btnName"));
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
