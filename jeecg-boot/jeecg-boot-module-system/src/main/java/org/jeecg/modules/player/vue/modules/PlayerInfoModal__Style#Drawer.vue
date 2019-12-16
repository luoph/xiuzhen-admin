<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="全局uuid" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['uuid', validatorRules.uuid]" placeholder="请输入全局uuid"></a-input>
                </a-form-item>
                <a-form-item label="玩家Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家Id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="角色昵称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['nickname', validatorRules.nickname]" placeholder="请输入角色昵称"></a-input>
                </a-form-item>
                <a-form-item label="角色头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['avatar', validatorRules.avatar]" placeholder="请输入角色头像"></a-input>
                </a-form-item>
                <a-form-item label="性别 1男 0女" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sex', validatorRules.sex]" placeholder="请输入性别 1男 0女" style="width: 100%" />
                </a-form-item>
                <a-form-item label="设置中是否开启 音乐 0否 1是" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['openMusic', validatorRules.openMusic]" placeholder="请输入设置中是否开启 音乐 0否 1是" style="width: 100%" />
                </a-form-item>
                <a-form-item label="设置中是否开启 音效 0否 1是" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['openSound', validatorRules.openSound]" placeholder="请输入设置中是否开启 音效 0否 1是" style="width: 100%" />
                </a-form-item>
                <a-form-item label="出身id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['birthId', validatorRules.birthId]" placeholder="请输入出身id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否初始化" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['initialized', validatorRules.initialized]" placeholder="请输入是否初始化" style="width: 100%" />
                </a-form-item>
                <a-form-item label="createTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择createTime" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="updateTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择updateTime" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
            </a-form>
        </a-spin>
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
    name: "PlayerInfoModal",
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
                uuid: { rules: [{ required: true, message: "请输入全局uuid!" }] },
                playerId: {},
                nickname: {},
                avatar: {},
                sex: {},
                openMusic: {},
                openSound: {},
                birthId: { rules: [{ required: true, message: "请输入出身id!" }] },
                initialized: { rules: [{ required: true, message: "请输入是否初始化!" }] },
                createTime: {},
                updateTime: {},
            },
            url: {
                add: "/player/playerInfo/add",
                edit: "/player/playerInfo/edit"
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
                this.form.setFieldsValue(pick(this.model, "uuid", "playerId", "nickname", "avatar", "sex", "openMusic", "openSound", "birthId", "initialized", "createTime", "updateTime"));
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
            this.form.setFieldsValue(pick(row, "uuid", "playerId", "nickname", "avatar", "sex", "openMusic", "openSound", "birthId", "initialized", "createTime", "updateTime"));
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