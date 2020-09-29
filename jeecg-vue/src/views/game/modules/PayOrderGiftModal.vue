<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="自己方订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['orderId', validatorRules.orderId]" placeholder="请输入自己方订单号"></a-input>
                </a-form-item>
                <a-form-item label="平台方订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['queryId', validatorRules.queryId]" placeholder="请输入平台方订单号"></a-input>
                </a-form-item>
                <a-form-item label="渠道id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['channel', validatorRules.channel]" placeholder="请输入渠道id"></a-input>
                </a-form-item>
                <a-form-item label="渠道key" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['channelKey', validatorRules.channelKey]" placeholder="请输入渠道key"></a-input>
                </a-form-item>
                <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="支付玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入支付玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="商品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['productId', validatorRules.productId]" placeholder="请输入商品id"></a-input>
                </a-form-item>
                <a-form-item label="ip地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remoteIp', validatorRules.remoteIp]" placeholder="请输入ip地址"></a-input>
                </a-form-item>
                <a-form-item label="0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['orderStatus', validatorRules.orderStatus]" placeholder="请输入0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放" style="width: 100%" />
                </a-form-item>
                <a-form-item label="订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['orderAmount', validatorRules.orderAmount]" placeholder="请输入订单金额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="实际支付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['payAmount', validatorRules.payAmount]" placeholder="请输入实际支付金额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="折扣金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discountAmount', validatorRules.discountAmount]" placeholder="请输入折扣金额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['custom', validatorRules.custom]" placeholder="请输入备注"></a-input>
                </a-form-item>
                <a-form-item label="充值货币(CNY:人民币)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['currency', validatorRules.currency]" placeholder="请输入充值货币(CNY:人民币)"></a-input>
                </a-form-item>
                <a-form-item label="订单创建时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择订单创建时间戳" v-decorator="['payTime', validatorRules.payTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
                </a-form-item>
                <a-form-item label="发货时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择发货时间" v-decorator="['sendTime', validatorRules.sendTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择更新时间" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="订单创建时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择订单创建时间戳" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
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
    name: "PayOrderGiftModal",
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
                orderId: { rules: [{ required: true, message: "请输入自己方订单号!" }] },
                queryId: {},
                channel: {},
                channelKey: { rules: [{ required: true, message: "请输入渠道key!" }] },
                serverId: { rules: [{ required: true, message: "请输入服务器id!" }] },
                playerId: { rules: [{ required: true, message: "请输入支付玩家id!" }] },
                productId: {},
                remoteIp: { rules: [{ required: true, message: "请输入ip地址!" }] },
                orderStatus: { rules: [{ required: true, message: "请输入0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放!" }] },
                orderAmount: { rules: [{ required: true, message: "请输入订单金额!" }] },
                payAmount: { rules: [{ required: true, message: "请输入实际支付金额!" }] },
                discountAmount: {},
                custom: {},
                currency: {},
                payTime: {},
                sendTime: {},
                updateTime: {},
                createTime: {},
            },
            url: {
                add: "game/payOrderGift/add",
                edit: "game/payOrderGift/edit"
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
                this.form.setFieldsValue(pick(this.model, "orderId", "queryId", "channel", "channelKey", "serverId", "playerId", "productId", "remoteIp", "orderStatus", "orderAmount", "payAmount", "discountAmount", "custom", "currency", "payTime", "sendTime", "updateTime", "createTime"));
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
            this.form.setFieldsValue(pick(row, "orderId", "queryId", "channel", "channelKey", "serverId", "playerId", "productId", "remoteIp", "orderStatus", "orderAmount", "payAmount", "discountAmount", "custom", "currency", "payTime", "sendTime", "updateTime", "createTime"));
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