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
                <a-form-item label="页签详情id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['giftDetailId', validatorRules.giftDetailId]" placeholder="请输入页签详情id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
                </a-form-item>
                <a-form-item label="购买数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['buyNum', validatorRules.buyNum]" placeholder="请输入购买数量" style="width: 100%" />
                </a-form-item>
                <a-form-item label="礼包类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <!-- 0.普通礼包, 1.大奖礼包 -->
                    <a-select placeholder="请选择礼包类型" v-decorator="['giftType', validatorRules.giftType]" initialValue="1">
                        <a-select-option :value="0">普通礼包</a-select-option>
                        <a-select-option :value="1">大奖礼包</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入折扣" style="width: 100%" />
                </a-form-item>
                <a-form-item label="价格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['price', validatorRules.price]" placeholder="请输入价格"></a-input>
                </a-form-item>
                <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励列表"></a-textarea>
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
    name: "OpenServiceCampaignGiftDetailItemModal",
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
                buyNum: { rules: [{ required: true, message: "请输入购买数量" }] },
                sort: { rules: [{ required: true, message: "请输入排序!" }] },
                giftType: { rules: [{ required: true, message: "请选择礼包类型!" }] },
                discount: { rules: [{ required: true, message: "请输入折扣!" }] },
                price: { rules: [{ required: false, message: "请输入价格!" }] },
                reward: { rules: [{ required: true, message: "请输入奖励列表!" }] }
            },
            url: {
                add: "game/openServiceCampaignGiftDetailItem/add",
                edit: "game/openServiceCampaignGiftDetailItem/edit"
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
            console.log("OpenServiceCampaignGiftDetailListModal, model:", JSON.stringify(this.model));
            this.visible = true;

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "campaignTypeId", "giftDetailId", "sort", "buyNum", "giftType", "discount", "price", "reward"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "campaignTypeId", "giftDetailId", "sort", "buyNum", "giftType", "discount", "price", "reward"));
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
