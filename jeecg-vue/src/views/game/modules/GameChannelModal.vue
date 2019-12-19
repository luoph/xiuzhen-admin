<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="渠道名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入渠道名称"></a-input>
                </a-form-item>
                <a-form-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['simpleName', validatorRules.simpleName]" placeholder="请输入唯一标识"></a-input>
                </a-form-item>
                <a-form-item label="排序字段" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['position', validatorRules.position]" placeholder="请输入排序字段" style="width: 100%" />
                </a-form-item>
                <a-form-item label="公告id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['noticeId', validatorRules.noticeId]" placeholder="请输入公告id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="大渠道描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入大渠道描述"></a-input>
                </a-form-item>
                <a-form-item label="扩展字段" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['extra', validatorRules.extra]" placeholder="请输入扩展字段"></a-input>
                </a-form-item>
                <a-form-item label="游戏编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['gameId', validatorRules.gameId]" placeholder="请输入游戏编号" style="width: 100%" />
                </a-form-item>
                <a-form-item label="分组" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['groupName', validatorRules.groupName]" placeholder="请输入分组"></a-input>
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
    name: "GameChannelModal",
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
                name: { rules: [{ required: true, message: "请输入渠道名称!" }] },
                simpleName: {},
                position: {},
                noticeId: {},
                remark: {},
                extra: {},
                gameId: {},
                groupName: {}
            },
            url: {
                add: "game/gameChannel/add",
                edit: "game/gameChannel/edit"
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
                this.form.setFieldsValue(pick(this.model, "name", "simpleName", "position", "noticeId", "remark", "extra", "gameId", "groupName"));
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
            this.form.setFieldsValue(pick(row, "name", "simpleName", "position", "noticeId", "remark", "extra", "gameId", "groupName"));
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
