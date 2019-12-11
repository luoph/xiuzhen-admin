<template>
    <a-drawer :title="title" :width="800" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="800" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器名字">
                    <a-input placeholder="请输入服务器名字" v-decorator="['name', validatorRules.name]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器路径">
                    <a-input placeholder="请输入服务器路径" v-decorator="['host', validatorRules.host]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器端口">
                    <a-input-number v-decorator="['port', validatorRules.port]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="登录地址和端口">
                    <a-input placeholder="请输入登录地址和端口" v-decorator="['loginUrl', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器状态">
                    <a-select placeholder="请选择服务器状态" v-decorator="['status', {}]">
                        <a-select-option :value="0">正常</a-select-option>
                        <a-select-option :value="1">流畅</a-select-option>
                        <a-select-option :value="2">火爆</a-select-option>
                        <a-select-option :value="3">维护</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="推荐标识">
                    <a-select placeholder="请选择推荐标识" v-decorator="['recommend', {}]">
                        <a-select-option :value="0">普遍</a-select-option>
                        <a-select-option :value="1">推荐</a-select-option>
                        <a-select-option :value="2">新服</a-select-option>
                        <a-select-option :value="3">推荐新服</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="出错提示信息">
                    <a-input placeholder="请输入出错提示信息" v-decorator="['warning', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户端最小版本号">
                    <a-input-number v-decorator="['minVersion', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户端最大版本号">
                    <a-input-number v-decorator="['maxVersion', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库路径">
                    <a-input placeholder="请输入数据库路径" v-decorator="['dbHost', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库端口">
                    <a-input-number v-decorator="['dbPort', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库用户名">
                    <a-input placeholder="请输入数据库用户名" v-decorator="['dbUser', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库密码">
                    <a-input-password placeholder="请输入数据库密码" v-decorator="['dbPassword', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库名">
                    <a-input placeholder="请输入数据库名" v-decorator="['dbName', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="后台HTTP端口">
                    <a-input-number v-decorator="['httpPort', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="排序字段">
                    <a-input-number v-decorator="['position', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器类型">
                    <a-select v-decorator="['type', {}]" placeholder="请选择服务器类型">
                        <a-select-option :value="0">混服</a-select-option>
                        <a-select-option :value="1">专服</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="合服时母服id">
                    <a-input-number v-decorator="['pid', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="合服时间">
                    <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['mergeTime', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="扩展字段">
                    <a-input placeholder="请输入扩展字段" v-decorator="['extra', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器开服时间">
                    <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['openTime', {}]" />
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
import moment from "moment";
import { stringify } from "querystring";

export default {
    name: "GameServerModal",
    data() {
        return {
            title: "操作",
            visible: false,
            model: {},
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },

            confirmLoading: false,
            form: this.$form.createForm(this),
            validatorRules: {
                name: { rules: [{ required: true, message: "请输入服务器名字!" }] },
                host: { rules: [{ required: true, message: "请输入服务器路径!" }] },
                port: { rules: [{ required: true, message: "请输入服务器端口!" }] },
                status: { rules: [{ required: true, message: "请输入服务器状态!" }] },
                type: { rules: [{ required: true, message: "请输入服务器类型!" }] },
                dbHost: { rules: [{ required: true, message: "请输入数据库地址!" }] },
                dbUser: { rules: [{ required: true, message: "请输入数据库帐号!" }] },
                dbPassword: { rules: [{ required: true, message: "请输入数据库密码!" }] },
                dbName: { rules: [{ required: true, message: "请输入数据库名称!" }] },
            },
            url: {
                add: "/game/gameServer/add",
                edit: "/game/gameServer/edit",
            },
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
                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "name",
                        "host",
                        "port",
                        "loginUrl",
                        "status",
                        "recommend",
                        "warning",
                        "minVersion",
                        "maxVersion",
                        "dbHost",
                        "dbPort",
                        "dbUser",
                        "dbPassword",
                        "dbName",
                        "httpPort",
                        "position",
                        "type",
                        "pid",
                        "extra"
                    )
                );
                //时间格式化
                this.form.setFieldsValue({ mergeTime: this.model.mergeTime ? moment(this.model.mergeTime) : null });
                this.form.setFieldsValue({ openTime: this.model.openTime ? moment(this.model.openTime) : null });
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
                    let httpurl = "";
                    let method = "";
                    if (!this.model.id) {
                        httpurl += this.url.add;
                        method = "post";
                    } else {
                        httpurl += this.url.edit;
                        method = "put";
                    }
                    let formData = Object.assign(this.model, values);
                    //时间格式化
                    formData.mergeTime = formData.mergeTime ? formData.mergeTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.openTime = formData.openTime ? formData.openTime.format("YYYY-MM-DD HH:mm:ss") : null;

                    console.log(formData);
                    httpAction(httpurl, formData, method)
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
    },
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
