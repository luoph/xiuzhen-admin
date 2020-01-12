<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
                </a-form-item>
                <a-form-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['activity', validatorRules.activity]" placeholder="请输入唯一标识"></a-input>
                </a-form-item>
                <a-form-item label="活动标语" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['slogan', validatorRules.slogan]" placeholder="请输入活动标语"></a-input>
                </a-form-item>
                <a-form-item label="活动入口的icon" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['icon', validatorRules.icon]" placeholder="请输入活动入口的icon"></a-input>
                </a-form-item>
                <a-form-item label="活动状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择状态" v-decorator="['status', validatorRules.status]" initialValue="1">
                        <a-select-option :value="1">启用</a-select-option>
                        <a-select-option :value="0">禁用</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="开始时的传闻id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['startRumor', validatorRules.startRumor]" placeholder="请输入开始时的传闻id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="结束时的传闻id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['endRumor', validatorRules.endRumor]" placeholder="请输入结束时的传闻id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="图标显示类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择图标显示类型" v-decorator="['iconDisplay', validatorRules.iconDisplay]" initialValue="0">
                        <a-select-option :value="0">0 - 图标常驻</a-select-option>
                        <a-select-option :value="1">1 - 预告时才显示，平时隐藏</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="提前预告时间(秒)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['noticeTime', validatorRules.noticeTime]" placeholder="请输入提前预告时间(秒)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="跑马灯显示周期(秒)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['noticePeriod', validatorRules.noticePeriod]" placeholder="请输入跑马灯显示周期(秒)，0表示不显示跑马灯" style="width: 100%" />
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" />
                </a-form-item>
                <a-form-item label="自定义json参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea rows="4" v-decorator="['custom', validatorRules.custom]" placeholder="请输入自定义json参数" />
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea rows="4" v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注" />
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
import JDate from "@/components/jeecg/JDate";

export default {
    name: "GameActivityModal",
    components: {
        JDate
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
                activity: { rules: [{ required: true, message: "请输入唯一标识!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称!" }] },
                slogan: { rules: [{ required: true, message: "请输入活动标语!" }] },
                icon: { rules: [{ required: true, message: "请输入活动入口的icon!" }] },
                status: { rules: [{ required: true, message: "请输入活动状态!" }] },
                iconDisplay: { rules: [{ required: true, message: "请输入图标显示类型" }] },
                noticeTime: { rules: [{ required: true, message: "请输入提前预告时间(秒)!" }] },
                noticePeriod: { rules: [{ required: true, message: "请输入跑马灯显示周期(秒)!" }] },
                startTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] }
            },
            url: {
                add: "game/gameActivity/add",
                edit: "game/gameActivity/edit"
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
                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "activity",
                        "name",
                        "slogan",
                        "icon",
                        "status",
                        "startRumor",
                        "endRumor",
                        "iconDisplay",
                        "noticeTime",
                        "noticePeriod",
                        "startTime",
                        "endTime",
                        "custom",
                        "remark",
                        "createTime",
                        "updateTime"
                    )
                );
                // 时间格式化
                this.form.setFieldsValue({ startTime: this.model.startTime ? moment(this.model.startTime) : null });
                this.form.setFieldsValue({ endTime: this.model.endTime ? moment(this.model.endTime) : null });
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
                    formData.startTime = formData.startTime ? formData.startTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.endTime = formData.endTime ? formData.endTime.format("YYYY-MM-DD HH:mm:ss") : null;

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
            this.form.setFieldsValue(
                pick(
                    row,
                    "activity",
                    "name",
                    "slogan",
                    "icon",
                    "status",
                    "startRumor",
                    "endRumor",
                    "iconDisplay",
                    "noticeTime",
                    "noticePeriod",
                    "startTime",
                    "endTime",
                    "custom",
                    "remark",
                    "createTime",
                    "updateTime"
                )
            );
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
