<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="开服活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入开服活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="开服活动项类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select :disabled="isEdit" placeholder="请选择开服活动项类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                        <!-- 1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗 -->
                        <a-select-option :value="1">1-开服排行</a-select-option>
                        <a-select-option :value="2">2-开服礼包</a-select-option>
                        <a-select-option :value="3">3-单笔充值</a-select-option>
                        <a-select-option :value="4">4-寻宝</a-select-option>
                        <a-select-option :value="5">5-道具消耗</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入排序" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入活动备注"></a-input>
                </a-form-item>
            </a-form>

            <a-tabs v-if="isEdit" defaultActiveKey="1">
                <!-- 1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗 -->
                <a-tab-pane v-if="model.type === 1" tab="开服排行配置" key="1">
                    <open-service-campaign-rank-detail-list ref="rankList" />
                </a-tab-pane>
                <a-tab-pane v-if="model.type === 2" tab="开服礼包配置" key="2">
                    <open-service-campaign-gift-detail-list ref="giftList" />
                </a-tab-pane>
                <a-tab-pane v-if="model.type === 3" tab="单笔充值配置" key="3">
                    <open-service-campaign-single-gift-detail-list ref="singleGiftList" />
                </a-tab-pane>
            </a-tabs>
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
import OpenServiceCampaignGiftDetailList from "../OpenServiceCampaignGiftDetailList";
import OpenServiceCampaignRankDetailList from "../OpenServiceCampaignRankDetailList";
import OpenServiceCampaignSingleGiftDetailList from "../OpenServiceCampaignSingleGiftDetailList";

export default {
    name: "OpenServiceCampaignTypeModal",
    components: {
        JDate,
        OpenServiceCampaignRankDetailList,
        OpenServiceCampaignGiftDetailList,
        OpenServiceCampaignSingleGiftDetailList
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 1200,
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
                campaignId: { rules: [{ required: true, message: "请输入开服活动id!" }] },
                type: { rules: [{ required: true, message: "请输入开服活动项类型!" }] },
                sort: { rules: [{ required: true, message: "请输入排序!" }] },
                remark: { rules: [{ required: true, message: "请输入活动备注!" }] }
            },
            url: {
                add: "game/openServiceCampaignType/add",
                edit: "game/openServiceCampaignType/edit"
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
            console.log("OpenServiceCampaignTypeModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                if (this.isEdit) {
                    if (this.$refs.rankList) {
                        this.$refs.rankList.edit(record);
                    }
                    if (this.$refs.giftList) {
                        this.$refs.giftList.edit(record);
                    }
                    if (this.$refs.singleGiftList) {
                        this.$refs.singleGiftList.edit(record);
                    }
                }

                this.form.setFieldsValue(pick(this.model, "campaignId", "type", "sort", "remark"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "type", "sort", "remark"));
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
