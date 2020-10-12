<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['channel', validatorRules.channel]" placeholder="请输入渠道"></a-input>
                </a-form-item>
                <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="每天登陆玩家数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['loginNum', validatorRules.loginNum]" placeholder="请输入每天登陆玩家数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="每天支付总额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['payAmount', validatorRules.payAmount]" placeholder="请输入每天支付总额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="每天支付玩家数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['payNum', validatorRules.payNum]" placeholder="请输入每天支付玩家数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="每天支付率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['payRate', validatorRules.payRate]" placeholder="请输入每天支付率" style="width: 100%" />
                </a-form-item>
                <a-form-item label="arpu" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['arpu', validatorRules.arpu]" placeholder="请输入arpu" style="width: 100%" />
                </a-form-item>
                <a-form-item label="arppu" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['arppu', validatorRules.arppu]" placeholder="请输入arppu" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册玩家数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addNum', validatorRules.addNum]" placeholder="请输入新增注册玩家数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册玩家支付总额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addPayAmount', validatorRules.addPayAmount]" placeholder="请输入新增注册玩家支付总额" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册玩家支付数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addPayNum', validatorRules.addPayNum]" placeholder="请输入新增注册玩家支付数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册玩家支付率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addPayRate', validatorRules.addPayRate]" placeholder="请输入新增注册玩家支付率" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册arpu" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addArpu', validatorRules.addArpu]" placeholder="请输入新增注册arpu" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册arppu" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addArppu', validatorRules.addArppu]" placeholder="请输入新增注册arppu" style="width: 100%" />
                </a-form-item>
                <a-form-item label="新增注册二次付费玩家数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['doublePay', validatorRules.doublePay]" placeholder="请输入新增注册二次付费玩家数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="二次付费率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['doublePayRate', validatorRules.doublePayRate]" placeholder="请输入二次付费率" style="width: 100%" />
                </a-form-item>
                <a-form-item label="统计日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择统计日期" v-decorator="['countDate', validatorRules.countDate]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
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
    name: "GameDataReportCountModal",
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
                channel: {},
                serverId: {},
                loginNum: {},
                payAmount: {},
                payNum: {},
                payRate: {},
                arpu: {},
                arppu: {},
                addNum: {},
                addPayAmount: {},
                addPayNum: {},
                addPayRate: {},
                addArpu: {},
                addArppu: {},
                doublePay: {},
                doublePayRate: {},
                countDate: {},
                createTime: {},
            },
            url: {
                add: "game/gameDataReportCount/add",
                edit: "game/gameDataReportCount/edit"
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
                this.form.setFieldsValue(pick(this.model, "channel", "serverId", "loginNum", "payAmount", "payNum", "payRate", "arpu", "arppu", "addNum", "addPayAmount", "addPayNum", "addPayRate", "addArpu", "addArppu", "doublePay", "doublePayRate", "countDate", "createTime"));
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
            this.form.setFieldsValue(pick(row, "channel", "serverId", "loginNum", "payAmount", "payNum", "payRate", "arpu", "arppu", "addNum", "addPayAmount", "addPayNum", "addPayRate", "addArpu", "addArppu", "doublePay", "doublePayRate", "countDate", "createTime"));
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