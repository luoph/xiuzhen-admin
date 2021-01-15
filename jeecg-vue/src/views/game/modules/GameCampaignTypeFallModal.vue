<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入页签id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="模块" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="模块" v-decorator="['module', validatorRules.module]" initialValue="1">
                        <a-select-option :value="1">仙器秘境</a-select-option>
                        <a-select-option :value="2">仙兽秘境</a-select-option>
                        <a-select-option :value="3">丹药秘境</a-select-option>
                        <a-select-option :value="4">修为秘境</a-select-option>
                        <a-select-option :value="5">灵石秘境</a-select-option>
                        <a-select-option :value="6">北冥魔海</a-select-option>
                        <a-select-option :value="7">不死魔巢</a-select-option>
                        <a-select-option :value="8">蛇陵魔窟</a-select-option>
                        <a-select-option :value="9">魔王入侵</a-select-option>
                        <a-select-option :value="10">剧情挂机</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="奖励类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="奖励类型" v-decorator="['rewardType', validatorRules.rewardType]" initialValue="1">
                        <a-select-option :value="1">按比例加成</a-select-option>
                        <a-select-option :value="2">额外的活动掉落组</a-select-option>
                        <a-select-option :value="3">剧情挂机奖励</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['reward', validatorRules.reward]" placeholder='请输入奖励类型对应的奖励值, e.g. 5,  [1, 2],  [{"time":300, "reward":1}]'></a-input>
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
    name: "GameCampaignTypeFallModal",
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
                campaignId: { rules: [{ required: true, message: "请输入活动id!" }] },
                typeId: { rules: [{ required: true, message: "请输入页签id!" }] },
                module: { rules: [{ required: true, message: "请输入模块!" }] },
                rewardType: { rules: [{ required: true, message: "请输入奖励类型!" }] },
                reward: { rules: [{ required: true, message: "请输入奖励!" }] }
            },
            url: {
                add: "game/gameCampaignTypeFall/add",
                edit: "game/gameCampaignTypeFall/edit"
            }
        };
    },
    created() {},
    methods: {
        add(record) {
            this.edit(record);
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.isEdit = this.model.id != null;
            this.visible = true;
            console.log("GameCampaignTypeFallModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "module", "rewardType", "reward"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "module", "rewardType", "reward"));
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
