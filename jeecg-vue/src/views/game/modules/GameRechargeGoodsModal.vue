<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="商品Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['goodsId', validatorRules.goodsId]" placeholder="请输入商品Id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="商品Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sku', validatorRules.sku]" placeholder="请输入sku" style="width: 100%" />
                </a-form-item>
                <a-form-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['price', validatorRules.price]" placeholder="请输入单价(创建订单实际价格)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入折扣后的价格" style="width: 100%" />
                </a-form-item>
                <a-form-item label="商品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入商品名称"></a-input>
                </a-form-item>
                <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['items']" rows="4" placeholder="请输入奖励列表" />
                </a-form-item>
                <a-form-item label="首次额外赠送" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['addition']" rows="4" placeholder="首次额外赠送" />
                </a-form-item>
                <a-form-item label="商品分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="选择商品分类" v-decorator="['goodsType', validatorRules.goodsType]" initialValue="0">
                        <a-select-option :value="0">0-普通类型</a-select-option>
                        <a-select-option :value="1">1-仙职</a-select-option>
                        <a-select-option :value="2">2-月卡</a-select-option>
                        <a-select-option :value="3">3-每日礼包</a-select-option>
                        <a-select-option :value="4">4-首充</a-select-option>
                        <a-select-option :value="5">5-周卡</a-select-option>
                        <a-select-option :value="6">6-六道剑阵</a-select-option>
                        <a-select-option :value="7">7-招财进宝</a-select-option>
                        <a-select-option :value="8">8-高级天道令</a-select-option>
                        <a-select-option :value="9">9-节日派对</a-select-option>
                        <a-select-option :value="10">10-节日直购礼包</a-select-option>
                        <a-select-option :value="11">11-精准礼包</a-select-option>
                        <a-select-option :value="12">12-结义礼包</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="是否记入累充" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请输入是否记入累充" v-decorator="['amountStat', validatorRules.amountStat]" initialValue="1">
                        <a-select-option :value="0">是</a-select-option>
                        <a-select-option :value="1">否</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="兑换比例" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="选择兑换比例" v-decorator="['exchange', validatorRules.exchange]" initialValue="0">
                        <a-select-option :value="10">10</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="特殊标签" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="选择特殊标签" v-decorator="['recommend', validatorRules.recommend]" initialValue="0">
                        <a-select-option :value="0">无</a-select-option>
                        <a-select-option :value="1">推荐</a-select-option>
                        <a-select-option :value="2">礼包</a-select-option>
                    </a-select>
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
            width: 1000,
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
                price: { rules: [{ required: true, message: "请输入单价!" }] },
                discount: { rules: [{ required: false, message: "请输入折扣!" }] },
                name: { rules: [{ required: true, message: "请输入商品名称!" }] },
                items: { rules: [{ required: false, message: "请输入奖励列表!" }] },
                goodsType: { rules: [{ required: true, message: "请输入充值分类!" }] },
                amountStat: { rules: [{ required: true, message: "请输入是否记入累充！" }] },
                exchange: { rules: [{ required: true, message: "请输入游戏币与人民币(元)的兑换比例!" }] },
                recommend: { rules: [{ required: false, message: "请输入特殊标记前端用!" }] },
                goodsId: { rules: [{ required: true, message: "请输入商品Id" }] },
                sku: { rules: [{ required: true, message: "请输入sku" }] }
            },
            url: {
                add: "game/gameRechargeGoods/add",
                edit: "game/gameRechargeGoods/edit"
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
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "goodsId", "sku", "price", "discount", "name", "items", "goodsType", "amountStat", "addition", "exchange", "recommend"));
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
            this.form.setFieldsValue(pick(row, "goodsId", "sku", "price", "discount", "name", "items", "goodsType", "amountStat", "addition", "exchange", "createBy"));
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
