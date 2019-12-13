<template>
    <a-drawer :title="title" :width="800" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="800" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告类型 1 - 渠道公告 2 - 滚动公告">
                    <a-input-number v-decorator="[ 'noticeType', validatorRules.noticeType]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标题">
                    <a-input placeholder="请输入标题" v-decorator="['title', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告内容">
                    <a-input placeholder="请输入公告内容" v-decorator="['content', validatorRules.content]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始时间">
                    <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'beginTime', validatorRules.beginTime]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束时间">
                    <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'endTime', validatorRules.endTime]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态 1 - 可用 0 - 不可用">
                    <a-input placeholder="请输入状态 1 - 可用 0 - 不可用" v-decorator="['status', validatorRules.status]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="滚动公告间隔">
                    <a-input-number v-decorator="[ 'interval', {}]" />
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
import Vue from "vue";

export default {
    name: "GameNoticeModal",
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
                noticeType: { rules: [{ required: true, message: "请输入公告类型 1 - 渠道公告 2 - 滚动公告!" }] },
                content: { rules: [{ required: true, message: "请输入公告内容!" }] },
                beginTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] },
                status: { rules: [{ required: true, message: "请输入状态 1 - 可用 0 - 不可用!" }] },
            },
            url: {
                add: "/game/gameNotice/add",
                edit: "/game/gameNotice/edit",
            },
        }
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
                this.form.setFieldsValue(pick(this.model, "noticeType", "title", "content", "status", "interval"))
                // 时间格式化
                this.form.setFieldsValue({beginTime : this.model.beginTime ? moment(this.model.beginTime) : null})
                this.form.setFieldsValue({endTime : this.model.endTime ? moment(this.model.endTime) : null})
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
                    formData.beginTime=formData.beginTime?formData.beginTime.format('YYYY-MM-DD HH:mm:ss'):null;
                    formData.endTime=formData.endTime?formData.endTime.format('YYYY-MM-DD HH:mm:ss'):null;

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
}
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