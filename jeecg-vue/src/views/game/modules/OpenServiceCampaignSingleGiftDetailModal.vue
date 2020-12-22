<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="typeId" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignTypeId', validatorRules.campaignTypeId]" placeholder="请输入typeId" style="width: 100%" />
                </a-form-item>
                <a-form-item label="开始时间(开服第n天, e.g. 0表示开服第1天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始时间(开服第n天, e.g. 0表示开服第1天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="持续时间(天)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续时间(天)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动页签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['tabName', validatorRules.tabName]" placeholder="请输入活动页签名称"></a-input>
                </a-form-item>
                <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
                </a-form-item>
                <a-form-item label="活动背景图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['banner', validatorRules.banner]" placeholder="请输入活动背景图"></a-input>
                </a-form-item>
                <a-form-item label="邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['emailTitle', validatorRules.emailTitle]" placeholder="请输入邮件标题"></a-input>
                </a-form-item>
                <a-form-item label="邮件描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['emailRemark']" rows="4" placeholder="请输入邮件描述"/>
                </a-form-item>
                <a-form-item label="createTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择createTime" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="updateTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择updateTime" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建者" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['createBy', validatorRules.createBy]" placeholder="请输入创建者"></a-input>
                </a-form-item>
                <a-form-item label="更新者" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['updateBy', validatorRules.updateBy]" placeholder="请输入更新者"></a-input>
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
    name: "OpenServiceCampaignSingleGiftDetailModal",
    components: {
        JDate,
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
                campaignId: {},
                campaignTypeId: { rules: [{ required: true, message: "请输入typeId!" }] },
                startDay: { rules: [{ required: true, message: "请输入开始时间(开服第n天, e.g. 0表示开服第1天)!" }] },
                duration: { rules: [{ required: true, message: "请输入持续时间(天)!" }] },
                tabName: { rules: [{ required: true, message: "请输入活动页签名称!" }] },
                sort: { rules: [{ required: true, message: "请输入排序!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称!" }] },
                banner: { rules: [{ required: true, message: "请输入活动背景图!" }] },
                emailTitle: { rules: [{ required: true, message: "请输入邮件标题!" }] },
                emailRemark: { rules: [{ required: true, message: "请输入邮件描述!" }] },
                createTime: {},
                updateTime: {},
                createBy: {},
                updateBy: {},
            },
            url: {
                add: "game/openServiceCampaignSingleGiftDetail/add",
                edit: "game/openServiceCampaignSingleGiftDetail/edit"
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
                this.form.setFieldsValue(pick(this.model, "campaignId", "campaignTypeId", "startDay", "duration", "tabName", "sort", "name", "banner", "emailTitle", "emailRemark", "createTime", "updateTime", "createBy", "updateBy"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "campaignTypeId", "startDay", "duration", "tabName", "sort", "name", "banner", "emailTitle", "emailRemark", "createTime", "updateTime", "createBy", "updateBy"));
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