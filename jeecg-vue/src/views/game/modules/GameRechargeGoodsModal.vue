<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
             @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="单价(创建订单实际价格)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['price', validatorRules.price]" placeholder="请输入单价(创建订单实际价格)"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入折扣"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="商品名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入商品名"></a-input>
                </a-form-item>
                <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['items']" rows="4" placeholder="请输入奖励列表" />
                </a-form-item>
                <a-form-item label="充值分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['goodsType', validatorRules.goodsType]" placeholder="请输入充值分类"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否记入累充（0 - 不计入 1 - 记入）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['amountStat', validatorRules.amountStat]"
                                    placeholder="请输入是否记入累充（0 - 不计入 1 - 记入）" style="width: 100%" />
                </a-form-item>
                <a-form-item label="游戏币与人民币(元)的兑换比例" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['exchange', validatorRules.exchange]" placeholder="请输入游戏币与人民币(元)的兑换比例"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="特殊标记前端用" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['recommend']" placeholder="请输入特殊标记前端用" style="width: 100%" />
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
    name: "GameRechargeGoodsModal",
    components: {},
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 1100,
            visible: false,
            model: {},
            labelCol: {
                xs: { span: 26 },
                sm: { span: 7 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            confirmLoading: false,
            validatorRules: {
                price: { rules: [{ required: true, message: "请输入单价(创建订单实际价格)!" }] },
                discount: { rules: [{ required: true, message: "请输入折扣!" }] },
                amount: { rules: [{ required: true, message: "请输入原价!" }] },
                name: { rules: [{ required: true, message: "请输入商品名!" }] },
                items: { rules: [{ required: true, message: "请输入奖励列表!" }] },
                goodsType: { rules: [{ required: true, message: "请输入充值分类!" }] },
                amountStat: { rules: [{ required: true, message: "请输入是否记入累充（0 - 不计入 1 - 记入）!" }] },
                exchange: { rules: [{ required: true, message: "请输入游戏币与人民币(元)的兑换比例!" }] },
                recommend: { rules: [{ required: true, message: "请输入特殊标记前端用!" }] }
            },
            url: {
                add: "game/gameRechargeGoods/add",
                edit: "game/gameRechargeGoods/edit"
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
                this.form.setFieldsValue(pick(this.model, "price", "discount", "name", "items", "goodsType", "amountStat", "addition", "exchange", "createBy"));
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
            this.form.setFieldsValue(pick(row, "price", "discount", "name", "items", "goodsType", "amountStat", "addition", "exchange", "createBy"));
        }
    }
};
</script>

//
<style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>
