<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="typeIds" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入typeIds" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                        <a-select-option :value="1">1-登录礼包</a-select-option>
                        <a-select-option :value="2">2-累计充值</a-select-option>
                        <a-select-option :value="3">3-兑换</a-select-option>
                        <a-select-option :value="4">4-节日任务</a-select-option>
                        <a-select-option :value="5">5-Buff-修为加成</a-select-option>
                        <a-select-option :value="6">6-Buff-灵气加成</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="buff id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['buffId', validatorRules.buffId]" placeholder="请输入buff id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择开始时间" v-decorator="['startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择结束时间" v-decorator="['endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['description', validatorRules.description]" placeholder="请输入描述"></a-input>
                </a-form-item>
                <a-form-item label="加成" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['addition', validatorRules.addition]" placeholder="请输入加成" style="width: 100%" />
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
    <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer> -->
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
    name: "GameCampaignTypeBuffModal",
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
                type: { rules: [{ required: true, message: "请输入活动项类型!" }] },
                buffId: { rules: [{ required: true, message: "请输入buff id!" }] },
                startTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] },
                description: { rules: [{ required: true, message: "请输入描述!" }] },
                addition: { rules: [{ required: true, message: "请输入加成!" }] }
            },
            url: {
                add: "game/gameCampaignTypeBuff/add",
                edit: "game/gameCampaignTypeBuff/edit"
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
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "type", "buffId", "startTime", "endTime", "description", "addition", "createTime", "updateTime"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "type", "buffId", "startTime", "endTime", "description", "addition", "createTime", "updateTime"));
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
