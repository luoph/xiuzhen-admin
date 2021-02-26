<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
             @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]"
                                    placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]"
                                    placeholder="请输入页签id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="关卡id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['checkpointId', validatorRules.checkpointId]" placeholder="请输入关卡id"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="怪物id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['monsterId', validatorRules.monsterId]" placeholder="请输入怪物id"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="推荐战力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['combatPower', validatorRules.combatPower]" placeholder="请输入推荐战力"
                                    style="width: 100%" />
                </a-form-item>
                <a-form-item label="解锁关卡" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['unlockCheckpointId', validatorRules.unlockCheckpointId]"
                                    placeholder="请输入解锁关卡" style="width: 100%" />
                </a-form-item>
                <a-form-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['reward']" rows="4" placeholder="请输入奖励" />
                </a-form-item>
                <a-form-item label="关卡名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['checkpointName',validatorRules.checkpointName]"
                             placeholder="请输入关卡名"></a-input>
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
    name: "GameCampaignTypeSwordModal",
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
                campaignId: { rules: [{ required: true, message: "请输入活动id!" }] },
                typeId: { rules: [{ required: true, message: "请输入页签id!" }] },
                checkpointId: { rules: [{ required: true, message: "请输入关卡ID!" }] },
                monsterId: { rules: [{ required: true, message: "请输入怪物ID!" }] },
                combatPower: { rules: [{ required: true, message: "请输入推荐战力!" }] },
                unlockCheckpointId: { rules: [{ required: true, message: "请输入解锁关卡!" }] },
                reward: { rules: [{ required: true, message: "请输入奖励!" }] },
                checkpointName: { rules: [{ required: true, message: "请输入关卡名!" }] }
            },
            url: {
                add: "game/gameCampaignTypeSword/add",
                edit: "game/gameCampaignTypeSword/edit"
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
            console.log("GameCampaignTypeSwordModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "checkpointId", "monsterId", "combatPower", "unlockCheckpointId", "reward", "checkpointName"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "checkpointId", "monsterId", "combatPower", "unlockCheckpointId", "reward", "checkpointName"));
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
