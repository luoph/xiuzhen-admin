<template>
    <!-- <a-drawer :title="title" :width="1000" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏名称">
                    <a-input placeholder="请输入游戏名称" v-decorator="['name', validatorRules.name]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="唯一标识">
                    <a-input :disabled="isEdit" placeholder="请输入唯一标识" v-decorator="['yaSimpleName', validatorRules.yaSimpleName]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="YA_APPID">
                    <a-input placeholder="请输入YA_APPID" v-decorator="['yaAppId', validatorRules.yaAppId]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="YA_APPKEY">
                    <a-input placeholder="请输入YA_APPKEY" v-decorator="['yaAppKey', validatorRules.yaAppKey]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="gameAppKey">
                    <a-input placeholder="请输入gameAppKey" v-decorator="['yaGameKey', validatorRules.yaGameKey]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="帐号登录地址">
                    <a-input placeholder="请输入帐号登录地址(不包含域名)" v-decorator="['loginUrl', validatorRules.loginUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="角色信息地址">
                    <a-input placeholder="请输入角色信息地址(不包含域名)" v-decorator="['roleUrl', validatorRules.roleUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="实名认证地址">
                    <a-input placeholder="请输入实名认证地址(不包含域名)" v-decorator="['authUrl', validatorRules.authUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器列表地址">
                    <a-input placeholder="请输入服务器列表地址(不包含域名)" v-decorator="['serverUrl', validatorRules.serverUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告列表地址">
                    <a-input placeholder="请输入公告列表地址(不包含域名)" v-decorator="['noticeUrl', validatorRules.noticeUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="支付验证地址">
                    <a-input placeholder="请输入支付验证地址(不包含域名)" v-decorator="['payUrl', validatorRules.payUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="苹果登录回调">
                    <a-input placeholder="请输入苹果登录回调(不包含域名)" v-decorator="['oauthRedirectUrl', validatorRules.oauthRedirectUrl]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="关闭注册天数">
                    <a-input placeholder="请输入关闭注册天数" v-decorator="['offRegisterDay', validatorRules.offRegisterDay]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
                    <a-textarea rows="4" placeholder="请输入描述" v-decorator="['remark', validatorRules.remark]" />
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
    <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer> -->
</template>

<script>
import { getAction, putAction, httpAction } from "@/api/manage";
import pick from "lodash.pick";
import moment from "moment";

export default {
    name: "GameInfoModal",
    data() {
        return {
            title: "操作",
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
            form: this.$form.createForm(this),
            validatorRules: {
                name: { rules: [{ required: true, message: "请输入游戏名称!" }] },
                yaAppId: { rules: [{ required: true, message: "请输入YA_APPID!" }] },
                yaAppKey: { rules: [{ required: true, message: "请输入YA_APPKEY!" }] },
                yaGameKey: { rules: [{ required: true, message: "请输入gameAppKey!" }] },
                yaSimpleName: { rules: [{ required: true, message: "请输入gameSimpleName!" }] },
                loginUrl: { rules: [{ required: true, message: "请输入帐号登录地址!" }] },
                roleUrl: { rules: [{ required: true, message: "请输入角色信息地址!" }] },
                payUrl: { rules: [{ required: true, message: "请输入支付验证地址!" }] },
                authUrl: { rules: [{ required: true, message: "请输入实名认证地址!" }] },
                oauthRedirectUrl: { rules: [{ required: true, message: "请输入苹果登录回调地址!" }] },
                serverUrl: { rules: [{ required: true, message: "请输入服务器列表地址!" }] },
                noticeUrl: { rules: [{ required: true, message: "请输入公告列表地址!" }] },
                offRegisterDay: { rules: [{ required: true, message: "请输入关闭注册天数!" }] },
                remark: { rules: [{ required: false, message: "请输入描述!" }] }
            },
            url: {
                add: "game/gameInfo/add",
                edit: "game/gameInfo/edit"
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
                        "name",
                        "yaAppId",
                        "yaAppKey",
                        "yaSimpleName",
                        "yaGameKey",
                        "loginUrl",
                        "roleUrl",
                        "payUrl",
                        "serverUrl",
                        "noticeUrl",
                        "authUrl",
                        "oauthRedirectUrl",
                        "offRegisterDay",
                        "remark"
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
                    // 时间格式化

                    console.log(formData);
                    httpAction(httpUrl, formData, method)
                        .then((res) => {
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
