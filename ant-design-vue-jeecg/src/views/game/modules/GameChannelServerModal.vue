<template>
    <a-drawer :title="title" :width="800" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="800" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务器id">
                    <a-input placeholder="请输入服务器id" v-decorator="['severId', validatorRules.severId]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="渠道id">
                    <a-input placeholder="请输入渠道id" v-decorator="['channelId', validatorRules.channelId]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="删除状态">
                    <a-input-number v-decorator="['delFlag', {}]" />
                </a-form-item>
            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { getAction, putAction, httpAction } from "@/api/manage";
import pick from "lodash.pick";
import moment from "moment";

export default {
    name: "GameChannelServerModal",
    data() {
        return {
            title: "操作",
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
            form: this.$form.createForm(this),
            validatorRules: {
                severId: { rules: [{ required: true, message: "请输入服务器id!" }] },
                channelId: { rules: [{ required: true, message: "请输入渠道id!" }] }
            },
            url: {
                add: "/game/gameChannelServer/add",
                edit: "/game/gameChannelServer/edit"
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
                this.form.setFieldsValue(pick(this.model, "severId", "channelId", "delFlag"));
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
