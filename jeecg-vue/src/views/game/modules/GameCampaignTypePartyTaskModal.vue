<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
             @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]"
                                    placeholder="请输入campaign.id, 活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]"
                                    placeholder="请输入game_campaign_type.id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['type', validatorRules.type]" placeholder="请输入任务类型"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务模块id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['moduleId', validatorRules.moduleId]" placeholder="请输入任务模块id"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['args', validatorRules.args]" placeholder="请输入参数"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入任务描述"></a-input>
                </a-form-item>
                <a-form-item label="任务规定数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['target', validatorRules.target]" placeholder="请输入任务规定数量"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="直接消耗数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['costNum', validatorRules.costNum]" placeholder="请输入直接消耗数量"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="跳转id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['jumpId', validatorRules.jumpId]" placeholder="请输入跳转id"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['reward']" rows="4" placeholder="请输入任务奖励" />
                </a-form-item>
                <a-form-item label="最小世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['minLevel', validatorRules.minLevel]" placeholder="请输入最小世界等级" style="width: 100%" />
                </a-form-item>
                <a-form-item label="最大世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['maxLevel', validatorRules.maxLevel]" placeholder="请输入最大世界等级" style="width: 100%" />
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

export default {
    name: "GameCampaignTypePartyTaskModal",
    components: {},
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
                campaignId: { rules: [{ required: true, message: "请输入campaign.id, 活动id!" }] },
                typeId: { rules: [{ required: true, message: "请输入game_campaign_type.id!" }] },
                type: { rules: [{ required: true, message: "请输入任务类型!" }] },
                moduleId: { rules: [{ required: true, message: "请输入任务模块id!" }] },
                args: { rules: [{ required: true, message: "请输入参数!" }] },
                remark: { rules: [{ required: true, message: "请输入任务描述!" }] },
                target: { rules: [{ required: true, message: "请输入任务规定数量!" }] },
                costNum: { rules: [{ required: true, message: "请输入直接消耗数量!" }] },
                jumpId: { rules: [{ required: true, message: "请输入跳转id!" }] },
                reward: { rules: [{ required: true, message: "请输入任务奖励!" }] },
                minLevel: {rules: [{ required: true, message: "请输入最小世界等级!" }]},
                maxLevel: {rules: [{ required: true, message: "请输入最大世界等级!" }]},
            },
            url: {
                add: "game/gameCampaignTypePartyTask/add",
                edit: "game/gameCampaignTypePartyTask/edit"
            }
        };
    },
    created() {
    },
    methods: {
        add(record) {
            this.edit(record);
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.isEdit = this.model.id != null;
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "type", "moduleId", "args", "remark", "target", "costNum", "jumpId", "reward", "minLevel", "maxLevel"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "type", "moduleId", "args", "remark", "target", "costNum", "jumpId", "reward", "minLevel", "maxLevel"));
        }
    }
};
</script>

//
<style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>
