<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['title', validatorRules.title]" placeholder="请输入标题"></a-input>
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['descri', validatorRules.descri]" placeholder="请输入描述"></a-input>
                </a-form-item>
                <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['emailType', validatorRules.emailType]" placeholder="请输入类型" style="width: 100%" />
                </a-form-item>
                <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['content', validatorRules.content]" placeholder="请输入附件"></a-input>
                </a-form-item>
                <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-search-select-tag v-decorator="['validState']" dict="" />
                </a-form-item>
                <a-form-item label="目标类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-search-select-tag v-decorator="['targetBodyType']" dict="" />
                </a-form-item>
                <a-form-item label="目标主体" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['targetBodyId', validatorRules.targetBodyId]" placeholder="请输入目标主体" style="width: 100%" />
                </a-form-item>
                <a-form-item label="生效时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择生效时间" v-decorator="['sendTime', validatorRules.sendTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择开始时间" v-decorator="['validStarTime', validatorRules.validStarTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择结束时间" v-decorator="['validEndTime', validatorRules.validEndTime]" :trigger-change="true" style="width: 100%" />
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
import JDate from "@/components/jeecg/JDate";
import JSearchSelectTag from "@/components/dict/JSearchSelectTag";

export default {
    name: "GameEmailModal",
    components: {
        JDate,
        JSearchSelectTag,
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
                title: { rules: [{ required: true, message: "请输入标题!" }] },
                descri: { rules: [{ required: true, message: "请输入描述!" }] },
                emailType: { rules: [{ required: true, message: "请输入类型!" }] },
                content: {},
                validState: { rules: [{ required: true, message: "请输入状态!" }] },
                targetBodyType: { rules: [{ required: true, message: "请输入目标类型!" }] },
                targetBodyId: { rules: [{ required: true, message: "请输入目标主体!" }] },
                sendTime: { rules: [{ required: true, message: "请输入生效时间!" }] },
                validStarTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                validEndTime: {},
            },
            url: {
                add: "game/gameEmail/add",
                edit: "game/gameEmail/edit"
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
                this.form.setFieldsValue(pick(this.model, "title", "descri", "emailType", "content", "validState", "targetBodyType", "targetBodyId", "sendTime", "validStarTime", "validEndTime", "createBy", "createTime", "updateBy", "updateTime"));
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
            this.form.setFieldsValue(pick(row, "title", "descri", "emailType", "content", "validState", "targetBodyType", "targetBodyId", "sendTime", "validStarTime", "validEndTime", "createBy", "createTime", "updateBy", "updateTime"));
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