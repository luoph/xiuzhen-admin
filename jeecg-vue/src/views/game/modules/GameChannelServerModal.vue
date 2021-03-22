<template>
    <a-modal :title="title" :width="600" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <!-- <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="渠道id">
                    <a-input :disabled="true" placeholder="请输入渠道id" v-decorator="['channelId', validatorRules.channelId]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服Id">
                    <a-select :disabled="isEdit" placeholder="请选择区服Id" v-decorator="['serverId', {}]">
                        <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">{{ server.id + " - " + server.name }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="顺序">
                    <a-input-number v-decorator="['position', validatorRules.position]" placeholder="请输入顺序" style="width: 100%" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
                    <a-select v-decorator="['delFlag', {}]" placeholder="请选择状态" :initialValue="0">
                        <a-select-option :value="0">正常</a-select-option>
                        <a-select-option :value="1">已删除</a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
import { getAction, httpAction } from "@/api/manage";
import pick from "lodash.pick";

export default {
    name: "GameChannelServerModal",
    data() {
        return {
            title: "操作",
            visible: false,
            isEdit: false,
            model: {},
            serverList: [],
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
                serverId: { rules: [{ required: true, message: "请输入区服Id!" }] },
                position: { rules: [{ required: true, message: "请输入顺序!" }] }
            },
            url: {
                add: "game/gameChannelServer/add",
                edit: "game/gameChannelServer/edit",
                // 游戏服列表
                serverListUrl: "game/gameServer/all"
            }
        };
    },
    created() {
        this.queryServerList();
    },
    methods: {
        add(channelId) {
            // 从上层传递过来的参数，默认选择未删除
            this.edit({ channelId: channelId, delFlag: 0 });
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.isEdit = this.model.id != null;
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "serverId", "channelId", "delFlag", "position"));
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

                    console.log(formData);
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
        queryServerList() {
            let that = this;
            getAction(that.url.serverListUrl).then(res => {
                if (res.success) {
                    if (res.result instanceof Array) {
                        this.serverList = res.result;
                    } else if (res.result.records instanceof Array) {
                        this.serverList = res.result.records;
                    }
                } else {
                    this.serverList = [];
                }
            });
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
