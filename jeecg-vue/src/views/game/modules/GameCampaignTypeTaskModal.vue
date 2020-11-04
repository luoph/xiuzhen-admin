<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="typeIds" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入typeIds" style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['taskId', validatorRules.taskId]" placeholder="请输入任务id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['description', validatorRules.description]" placeholder="请输入描述"></a-input>
                </a-form-item>
                <a-form-item label="task_module_type.json.module_id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['moduleId', validatorRules.moduleId]" placeholder="请输入task_module_type.json.module_id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务完成条件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['target', validatorRules.target]" placeholder="请输入任务完成条件" style="width: 100%" />
                </a-form-item>
                <a-form-item label="任务参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['args', validatorRules.args]" placeholder="请输入任务参数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励列表"></a-input>
                </a-form-item>
                <a-form-item label="createTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择createTime" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="updateTime" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择updateTime" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
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

export default {
    name: "GameCampaignTypeTaskModal",
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
                campaignId: { rules: [{ required: true, message: "请输入活动id!" }] },
                typeId: { rules: [{ required: true, message: "请输入game_campaign_type.id!" }] },
                taskId: { rules: [{ required: true, message: "请输入任务id!" }] },
                description: { rules: [{ required: true, message: "请输入描述!" }] },
                moduleId: { rules: [{ required: true, message: "请输入task_module_type.json.module_id!" }] },
                target: { rules: [{ required: true, message: "请输入任务完成条件!" }] },
                args: { rules: [{ required: true, message: "请输入任务参数!" }] },
                reward: { rules: [{ required: true, message: "请输入奖励列表!" }] },
                createTime: {},
                updateTime: {}
            },
            url: {
                add: "game/gameCampaignTypeTask/add",
                edit: "game/gameCampaignTypeTask/edit"
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
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "taskId", "description", "moduleId", "target", "args", "reward"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "taskId", "description", "moduleId", "target", "reward"));
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
