<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="礼包id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['giftPackageId', validatorRules.giftPackageId]" placeholder="请输入礼包id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="购买日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择购买日期" v-decorator="['buyDate', validatorRules.buyDate]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
                </a-form-item>
                <a-form-item label="buyTimes" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['buyTimes', validatorRules.buyTimes]" placeholder="请输入buyTimes" style="width: 100%" />
                </a-form-item>
                <a-form-item label="奖励物品" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励物品"></a-input>
                </a-form-item>
                <a-form-item label="充值金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rechargeAmount', validatorRules.rechargeAmount]" placeholder="请输入充值金额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择更新时间" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
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
    name: "DailyGiftPackageBuyModal",
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
                playerId: { rules: [{ required: true, message: "请输入玩家id!" }] },
                giftPackageId: { rules: [{ required: true, message: "请输入礼包id!" }] },
                buyDate: { rules: [{ required: true, message: "请输入购买日期!" }] },
                buyTimes: {},
                reward: {},
                rechargeAmount: { rules: [{ required: true, message: "请输入充值金额!" }] },
                createTime: {},
                updateTime: {},
            },
            url: {
                add: "game/dailyGiftPackageBuy/add",
                edit: "game/dailyGiftPackageBuy/edit"
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
                this.form.setFieldsValue(pick(this.model, "playerId", "giftPackageId", "buyDate", "buyTimes", "reward", "rechargeAmount", "createTime", "updateTime"));
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
            this.form.setFieldsValue(pick(row, "playerId", "giftPackageId", "buyDate", "buyTimes", "reward", "rechargeAmount", "createTime", "updateTime"));
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