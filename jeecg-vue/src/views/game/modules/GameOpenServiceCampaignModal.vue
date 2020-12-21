<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
                </a-form-item>
                <a-form-item label="服务器id，使用,分割" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['serverIds', validatorRules.serverIds]" placeholder="请输入服务器id，使用,分割"></a-input>
                </a-form-item>
                <a-form-item label="活动图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['icon', validatorRules.icon]" placeholder="请输入活动图标"></a-input>
                </a-form-item>
                <a-form-item label="活动状态: 0.关闭, 1.开启（备用字段，默认全部为开启）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['status', validatorRules.status]" placeholder="请输入活动状态: 0.关闭, 1.开启（备用字段，默认全部为开启）" style="width: 100%" />
                </a-form-item>
                <a-form-item label="到达活动时间后自动开启" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['autoOpen', validatorRules.autoOpen]" placeholder="请输入到达活动时间后自动开启" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入活动备注"></a-input>
                </a-form-item>
                <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择更新时间" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="修改人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['updateBy', validatorRules.updateBy]" placeholder="请输入修改人"></a-input>
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
    name: "GameOpenServiceCampaignModal",
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
                name: { rules: [{ required: true, message: "请输入活动名称!" }] },
                serverIds: { rules: [{ required: true, message: "请输入服务器id，使用,分割!" }] },
                icon: { rules: [{ required: true, message: "请输入活动图标!" }] },
                status: { rules: [{ required: true, message: "请输入活动状态: 0.关闭, 1.开启（备用字段，默认全部为开启）!" }] },
                autoOpen: { rules: [{ required: true, message: "请输入到达活动时间后自动开启!" }] },
                remark: { rules: [{ required: true, message: "请输入活动备注!" }] },
                createTime: { rules: [{ required: true, message: "请输入创建时间!" }] },
                createBy: { rules: [{ required: true, message: "请输入创建人!" }] },
                updateTime: { rules: [{ required: true, message: "请输入更新时间!" }] },
                updateBy: { rules: [{ required: true, message: "请输入修改人!" }] },
            },
            url: {
                add: "game/openServiceCampaign/add",
                edit: "game/openServiceCampaign/edit"
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
                this.form.setFieldsValue(pick(this.model, "name", "serverIds", "icon", "status", "autoOpen", "remark", "createTime", "createBy", "updateTime", "updateBy"));
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
            this.form.setFieldsValue(pick(row, "name", "serverIds", "icon", "status", "autoOpen", "remark", "createTime", "createBy", "updateTime", "updateBy"));
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