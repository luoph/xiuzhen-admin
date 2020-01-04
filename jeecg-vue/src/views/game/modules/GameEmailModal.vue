<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['title', validatorRules.title]" placeholder="请输入邮件标题"></a-input>
                </a-form-item>
                <a-form-item label="邮件描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['descri', validatorRules.descri]" placeholder="请输入邮件描述"></a-input>
                </a-form-item>
                <a-form-item label="1有奖励附件content不为空 2无奖励附件contents是null" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['emailType', validatorRules.emailType]" placeholder="请输入1有奖励附件content不为空 2无奖励附件contents是null" style="width: 100%" />
                </a-form-item>
                <a-form-item label="附件内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['content', validatorRules.content]" placeholder="请输入附件内容"></a-input>
                </a-form-item>
                <a-form-item label="1有效 0无效" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-dict-select-tag type="list" v-decorator="['validState']" :trigger-change="true" dictCode="" placeholder="请选择1有效 0无效"/>
                </a-form-item>
                <a-form-item label="目标主体类型1玩家 2服务器" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['targetBodyType', validatorRules.targetBodyType]" placeholder="请输入目标主体类型1玩家 2服务器" style="width: 100%" />
                </a-form-item>
                <a-form-item label="目标主体id playerId serverId" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['targetBodyId', validatorRules.targetBodyId]" placeholder="请输入目标主体id playerId serverId" style="width: 100%" />
                </a-form-item>
                <a-form-item label="向目标主体发送的时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择向目标主体发送的时间" v-decorator="['sendTime', validatorRules.sendTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="有效日期开始" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择有效日期开始" v-decorator="['validStarTime', validatorRules.validStarTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="有效日期结束" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择有效日期结束" v-decorator="['validEndTime', validatorRules.validEndTime]" :trigger-change="true" style="width: 100%" />
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
import JDictSelectTag from "@/components/dict/JDictSelectTag";

export default {
    name: "GameEmailModal",
    components: {
        JDate,
        JDictSelectTag,
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
                title: { rules: [{ required: true, message: "请输入邮件标题!" }] },
                descri: { rules: [{ required: true, message: "请输入邮件描述!" }] },
                emailType: { rules: [{ required: true, message: "请输入1有奖励附件content不为空 2无奖励附件contents是null!" }] },
                content: {},
                validState: { rules: [{ required: true, message: "请输入1有效 0无效!" }] },
                targetBodyType: { rules: [{ required: true, message: "请输入目标主体类型1玩家 2服务器!" }] },
                targetBodyId: { rules: [{ required: true, message: "请输入目标主体id playerId serverId!" }] },
                sendTime: { rules: [{ required: true, message: "请输入向目标主体发送的时间!" }] },
                validStarTime: {},
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