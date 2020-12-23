<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="封禁类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择状态" v-decorator="['type', validatorRules.type]">
                        <a-select-option :value="1">登陆</a-select-option>
                        <a-select-option :value="2">聊天</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="封禁值类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择类型" v-decorator="['banKey', validatorRules.banKey]">
                        <a-select-option :value="'playerId'">playerId</a-select-option>
                        <a-select-option :value="'ip'">ip</a-select-option>
                        <a-select-option :value="'deviceId'">deviceId</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="封禁值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['banValue', validatorRules.banValue]" placeholder="请输入对应 ban_type 的值"></a-input>
                </a-form-item>
                <a-form-item label="封禁原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['reason', validatorRules.reason]" placeholder="请输入封禁原因"></a-input>
                </a-form-item>
                <!-- <a-form-item label="数据状态	" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input disabled v-decorator="['delFlag', validatorRules.delFlag]" placeholder="请输入删除状态：0-未删除 1-已删除	" initialValue="0" style="width: 100%" />
                </a-form-item> -->
                <a-form-item label="封禁时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择状态" v-decorator="['isForever', validatorRules.isForever]">
                        <a-select-option :value="0">临时</a-select-option>
                        <a-select-option :value="1">永久</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择开始时间" v-decorator="['startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择结束时间" v-decorator="['endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <!-- <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择更新时间" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['createBy', validatorRules.createBy]" placeholder="请输入操作人"></a-input>
                </a-form-item>
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['updateBy', validatorRules.updateBy]" placeholder="请输入操作人"></a-input>
                </a-form-item> -->
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
    name: "GameForbiddenModal",
    components: {
        JDate,
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
                serverId: { rules: [{ required: true, message: "请输入服务器id!" }] },
                type: {},
                banKey: { rules: [{ required: true, message: "请输入playerId/ip/deviceId!" }] },
                banValue: { rules: [{ required: true, message: "请输入对应 ban_type 的值!" }] },
                reason: { rules: [{ required: true, message: "请输入封禁原因!" }] },
                // delFlag: { rules: [{ required: true, message: "请输入删除状态：0-未删除 1-已删除	!" }] },
                isForever: { rules: [{ required: true, message: "请输入0-临时 1-永久!" }] },
                startTime: {},
                endTime: {},
                createTime: {},
                updateTime: {},
                createBy: {},
                updateBy: {},
            },
            url: {
                add: "game/gameForbidden/add",
                edit: "game/gameForbidden/edit"
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
                this.form.setFieldsValue(pick(this.model, "serverId", "type", "banKey", "banValue", "reason", "delFlag", "isForever", "startTime", "endTime", "createTime", "updateTime", "createBy", "updateBy"));
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
            this.form.setFieldsValue(pick(row, "serverId", "type", "banKey", "banValue", "reason", "delFlag", "isForever", "startTime", "endTime", "createTime", "updateTime", "createBy", "updateBy"));
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