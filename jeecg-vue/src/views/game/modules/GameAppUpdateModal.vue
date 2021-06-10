<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="游戏id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择游戏id" v-decorator="['gameId', validatorRules.gameId]">
                        <a-select-option :value="1000">修真成仙</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="应用名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['appName', validatorRules.appName]" placeholder="请输入应用名称"></a-input>
                </a-form-item>
                <a-form-item label="应用包名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['packageName', validatorRules.packageName]" placeholder="请输入应用包名"></a-input>
                </a-form-item>
                <a-form-item label="版本号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['versionCode', validatorRules.versionCode]" placeholder="请输入版本号" style="width: 100%" />
                </a-form-item>
                <a-form-item label="版本名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['versionName', validatorRules.versionName]" placeholder="请输入版本名"></a-input>
                </a-form-item>
                <a-form-item label="平台" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择平台" v-decorator="['platform', validatorRules.platform]">
                        <a-select-option value="android">Android</a-select-option>
                        <a-select-option value="ios">iOS</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="包渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择包渠道" v-decorator="['channel', validatorRules.channel]">
                        <a-select-option value="test">开发(develop)</a-select-option>
                        <a-select-option value="test">测试(test)</a-select-option>
                        <a-select-option value="plan">策划(plan)</a-select-option>
                        <a-select-option value="preview">预览(preview)</a-select-option>
                        <a-select-option value="youdian">优点(youdian)</a-select-option>
                        <a-select-option value="chenglong">乘龙(chenglong)</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="更新标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['updateTitle', validatorRules.updateTitle]" placeholder="请输入更新标题" />
                </a-form-item>
                <a-form-item label="更新内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['updateContent', validatorRules.updateContent]" placeholder="请输入更新内容" />
                </a-form-item>
                <a-form-item label="下载地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['downloadUrl', validatorRules.downloadUrl]" placeholder="请输入下载地址" />
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注"></a-input>
                </a-form-item>
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
    name: "GameAppUpdateModal",
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
                gameId: { rules: [{ required: true, message: "请输入游戏id!" }] },
                appName: { rules: [{ required: true, message: "请输入应用名称!" }] },
                packageName: { rules: [{ required: true, message: "请输入应用包名!" }] },
                versionCode: { rules: [{ required: true, message: "请输入版本号!" }] },
                versionName: { rules: [{ required: true, message: "请输入版本名!" }] },
                platform: { rules: [{ required: true, message: "请输入平台!" }] },
                channel: { rules: [{ required: true, message: "请输入包渠道!" }] },
                downloadUrl: { rules: [{ required: true, message: "请输入下载地址!" }] },
                updateTitle: { rules: [{ required: true, message: "请输入更新标题!" }] },
                updateContent: { rules: [{ required: true, message: "请输入更新内容!" }] },
                remark: {}
            },
            url: {
                add: "game/gameAppUpdate/add",
                edit: "game/gameAppUpdate/edit"
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
                        "gameId",
                        "appName",
                        "packageName",
                        "versionCode",
                        "versionName",
                        "platform",
                        "channel",
                        "downloadUrl",
                        "updateTitle",
                        "updateContent",
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
                pick(row, "gameId", "appName", "packageName", "versionCode", "versionName", "platform", "channel", "downloadUrl", "updateTitle", "updateContent", "remark")
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
