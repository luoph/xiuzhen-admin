<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="兑换码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['code', validatorRules.code]" placeholder="请输入兑换码"></a-input>
                </a-form-item>
                <a-form-item label="渠道编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['channel', validatorRules.channel]" placeholder="请输入渠道编码"></a-input>
                </a-form-item>
                <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="分组id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['groupId', validatorRules.groupId]" placeholder="请输入分组id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="isEdit" v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="兑换ip" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :disabled="isEdit" v-decorator="['remoteIp', validatorRules.remoteIp]" placeholder="请输入兑换ip"></a-input>
                </a-form-item>
                <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date :disabled="isEdit" placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date :disabled="isEdit" placeholder="请选择创建日期" v-decorator="['createDate', validatorRules.createDate]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <!-- <a-button type="primary" @click="handleOk">确定</a-button> -->
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
    name: "RedeemCodeRecordModal",
    components: {
        JDate
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
            visible: false,
            isEdit: false,
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
                code: { rules: [{ required: true, message: "请输入兑换码!" }] },
                channel: { rules: [{ required: true, message: "请输入渠道编码!" }] },
                playerId: { rules: [{ required: true, message: "请输入玩家id!" }] },
                groupId: {},
                serverId: { rules: [{ required: true, message: "请输入服务器id!" }] },
                remoteIp: { rules: [{ required: true, message: "请输入兑换ip!" }] },
                createTime: {},
                createDate: {}
            },
            url: {
                add: "game/redeemCodeRecord/add",
                edit: "game/redeemCodeRecord/edit"
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
            this.isEdit = this.model.id != null;
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "code", "channel", "playerId", "groupId", "serverId", "remoteIp", "createTime", "createDate"));
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
            this.form.setFieldsValue(pick(row, "code", "channel", "playerId", "groupId", "serverId", "remoteIp", "createTime", "createDate"));
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
