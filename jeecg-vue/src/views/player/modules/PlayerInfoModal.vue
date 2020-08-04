<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['id', validatorRules.id]" placeholder="请输入玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="角色昵称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['nickname', validatorRules.nickname]" placeholder="请输入角色昵称"></a-input>
                </a-form-item>
                <a-form-item label="角色头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['avatar', validatorRules.avatar]" placeholder="请输入角色头像"></a-input>
                </a-form-item>
                <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sex', validatorRules.sex]" placeholder="请输入性别" style="width: 100%" />
                </a-form-item>
                <a-form-item label="音乐开关" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['openMusic', validatorRules.openMusic]" placeholder="请输入音乐开关" style="width: 100%" />
                </a-form-item>
                <a-form-item label="音效开关" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['openSound', validatorRules.openSound]" placeholder="请输入音效开关" style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否初始化" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['initialized', validatorRules.initialized]" placeholder="请输入是否初始化" style="width: 100%" />
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

export default {
    name: "PlayerInfoModal",
    components: {},
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
                id: {},
                nickname: {},
                avatar: {},
                sex: {},
                openMusic: {},
                openSound: {}
            },
            url: {
                add: "player/playerInfo/add",
                edit: "player/playerInfo/edit"
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
                this.form.setFieldsValue(pick(this.model, "id", "nickname", "avatar", "sex", "openMusic", "openSound", "birthId", "initialized"));
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
            this.form.setFieldsValue(pick(row, "id", "nickname", "avatar", "sex", "openMusic", "openSound", "birthId", "initialized"));
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
