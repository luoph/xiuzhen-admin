<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="支付玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入支付玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="己方订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['orderId', validatorRules.orderId]" placeholder="请输入己方订单号"></a-input>
                </a-form-item>
                <a-form-item label="平台方订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['queryId', validatorRules.queryId]" placeholder="请输入平台方订单号"></a-input>
                </a-form-item>
                <a-form-item label="商品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['goodsId', validatorRules.goodsId]" placeholder="请输入商品id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="下发的商品" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['items', validatorRules.items]" placeholder="请输入下发的商品"></a-input>
                </a-form-item>
                <a-form-item label="首次额外赠送" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['addition', validatorRules.addition]" placeholder="请输入首次额外赠送"></a-input>
                </a-form-item>
                <a-form-item label="ip地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remoteIp', validatorRules.remoteIp]" placeholder="请输入ip地址"></a-input>
                </a-form-item>
                <a-form-item label="0 - 未处理，1 - 已处理" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['status', validatorRules.status]" placeholder="请输入0 - 未处理，1 - 已处理" style="width: 100%" />
                </a-form-item>
                <a-form-item label="1 - 正常充值 2 - 虚拟充值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['type', validatorRules.type]" placeholder="请输入1 - 正常充值 2 - 虚拟充值" style="width: 100%" />
                </a-form-item>
                <a-form-item label="实际支付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['payAmount', validatorRules.payAmount]" placeholder="请输入实际支付金额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="扩展自定义字段" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['custom', validatorRules.custom]" placeholder="请输入扩展自定义字段"></a-input>
                </a-form-item>
                <a-form-item label="发货时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择发货时间" v-decorator="['sendTime', validatorRules.sendTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择更新时间" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择创建时间戳" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
            </a-form>
        </a-spin>
    <!-- </a-modal> -->
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
    name: "RechargeOrderModal",
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
                playerId: { rules: [{ required: true, message: "请输入支付玩家id!" }] },
                orderId: {},
                queryId: { rules: [{ required: true, message: "请输入平台方订单号!" }] },
                goodsId: {},
                items: {},
                addition: {},
                remoteIp: { rules: [{ required: true, message: "请输入ip地址!" }] },
                status: { rules: [{ required: true, message: "请输入0 - 未处理，1 - 已处理!" }] },
                type: { rules: [{ required: true, message: "请输入1 - 正常充值 2 - 虚拟充值!" }] },
                payAmount: { rules: [{ required: true, message: "请输入实际支付金额!" }] },
                custom: {},
                sendTime: {},
                updateTime: {},
                createTime: {},
            },
            url: {
                add: "game/rechargeOrder/add",
                edit: "game/rechargeOrder/edit"
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
                this.form.setFieldsValue(pick(this.model, "playerId", "orderId", "queryId", "goodsId", "items", "addition", "remoteIp", "status", "type", "payAmount", "custom", "sendTime", "updateTime", "createTime"));
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
            this.form.setFieldsValue(pick(row, "playerId", "orderId", "queryId", "goodsId", "items", "addition", "remoteIp", "status", "type", "payAmount", "custom", "sendTime", "updateTime", "createTime"));
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