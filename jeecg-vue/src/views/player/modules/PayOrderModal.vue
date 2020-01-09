<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="己方单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['queryId', validatorRules.queryId]" placeholder="请输入己方单号"></a-input>
                </a-form-item>
                <a-form-item label="平台方订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['orderId', validatorRules.orderId]" placeholder="请输入平台方订单号"></a-input>
                </a-form-item>
                <a-form-item label="渠道key" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['channelKey', validatorRules.channelKey]" placeholder="请输入渠道key"></a-input>
                </a-form-item>
                <a-form-item label="渠道id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['channelId', validatorRules.channelId]" placeholder="请输入渠道id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="区服Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入区服Id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="商品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['goodsId', validatorRules.goodsId]" placeholder="请输入商品id"></a-input>
                </a-form-item>
                <a-form-item label="ip地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['remoteIp', validatorRules.remoteIp]" placeholder="请输入ip地址"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="订单状态">
                    <a-select :disabled="isEdit" placeholder="请选择订单状态" v-decorator="['orderStatus', validatorRules.orderStatus]">
                        <a-select-option value="0">待支付</a-select-option>
                        <a-select-option value="1">已支付</a-select-option>
                        <a-select-option value="2">已转发</a-select-option>
                        <a-select-option value="3">发放中</a-select-option>
                        <a-select-option value="4">已发放</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['realAmount', validatorRules.realAmount]" placeholder="请输入充值金额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="extra" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['extra', validatorRules.extra]" placeholder="请输入extra" style="width: 100%" />
                </a-form-item>
                <a-form-item label="充值货币" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['currency', validatorRules.currency]" placeholder="请输入充值货币"></a-input>
                </a-form-item>
                <a-form-item label="回调时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        :disabled="isEdit"
                        placeholder="请选择回调时间"
                        v-decorator="['postTime', validatorRules.postTime]"
                        :trigger-change="true"
                        :show-time="true"
                        date-format="YYYY-MM-DD HH:mm:ss"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="发货时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        :disabled="isEdit"
                        placeholder="请选择发货时间"
                        v-decorator="['sendTime', validatorRules.sendTime]"
                        :trigger-change="true"
                        :show-time="true"
                        date-format="YYYY-MM-DD HH:mm:ss"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        :disabled="isEdit"
                        placeholder="请选择更新时间"
                        v-decorator="['updateTime', validatorRules.updateTime]"
                        :trigger-change="true"
                        :show-time="true"
                        date-format="YYYY-MM-DD HH:mm:ss"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        :disabled="isEdit"
                        placeholder="请选择创建时间"
                        v-decorator="['createTime', validatorRules.createTime]"
                        :trigger-change="true"
                        :show-time="true"
                        date-format="YYYY-MM-DD HH:mm:ss"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['custom', validatorRules.custom]" placeholder="请输入备注"></a-input>
                </a-form-item>
            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <a-button type="primary" @click="handleCancel">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
    name: "PayOrderModal",
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
                queryId: { rules: [{ required: true, message: "请输入己方单号!" }] },
                orderId: {},
                channelKey: { rules: [{ required: true, message: "请输入渠道key!" }] },
                channelId: {},
                serverId: { rules: [{ required: true, message: "请输入区服Id!" }] },
                playerId: { rules: [{ required: true, message: "请输入玩家id!" }] },
                goodsId: {},
                remoteIp: { rules: [{ required: true, message: "请输入ip地址!" }] },
                orderStatus: { rules: [{ required: true, message: "请选择订单状态!" }] },
                realAmount: { rules: [{ required: true, message: "请输入充值金额!" }] },
                custom: {},
                extra: {},
                currency: {},
                postTime: {},
                sendTime: {},
                updateTime: {},
                createTime: {}
            },
            url: {
                add: "player/payOrder/add",
                edit: "player/payOrder/edit"
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
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "queryId",
                        "orderId",
                        "channelKey",
                        "channelId",
                        "serverId",
                        "playerId",
                        "goodsId",
                        "remoteIp",
                        "orderStatus",
                        "realAmount",
                        "custom",
                        "extra",
                        "currency",
                        "postTime",
                        "sendTime",
                        "updateTime",
                        "createTime"
                    )
                );
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
            this.form.setFieldsValue(
                pick(
                    row,
                    "queryId",
                    "orderId",
                    "channelKey",
                    "channelId",
                    "serverId",
                    "playerId",
                    "goodsId",
                    "remoteIp",
                    "orderStatus",
                    "realAmount",
                    "custom",
                    "extra",
                    "currency",
                    "postTime",
                    "sendTime",
                    "updateTime",
                    "createTime"
                )
            );
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
