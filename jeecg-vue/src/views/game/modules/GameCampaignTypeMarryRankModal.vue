<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="campaign.id, 活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入campaign.id, 活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="game_campaign_type.id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入game_campaign_type.id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="大奖展示" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['bigReward', validatorRules.bigReward]" placeholder="请输入大奖展示"></a-input>
                </a-form-item>
                <a-form-item label="大奖战力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['bigRewardFight', validatorRules.bigRewardFight]" placeholder="请输入大奖战力" style="width: 100%" />
                </a-form-item>
                <a-form-item label="排行玩家数量(上榜人数限制)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rankNum', validatorRules.rankNum]" placeholder="请输入排行玩家数量(上榜人数限制)" style="width: 100%" />
                </a-form-item>
                <a-form-item label="排名奖励邮件id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['rankRewardEmail', validatorRules.rankRewardEmail]" placeholder="请输入排名奖励邮件id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="号召赠酒传闻id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['callOnMessage', validatorRules.callOnMessage]" placeholder="请输入号召赠酒传闻id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="帮助信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['helpMsg', validatorRules.helpMsg]" placeholder="请输入帮助信息"></a-input>
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
    name: "GameCampaignTypeMarryRankModal",
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
                typeId: {},
                bigReward: {},
                bigRewardFight: {},
                rankNum: {},
                rankRewardEmail: {},
                callOnMessage: {},
                helpMsg: {},
                createTime: {},
                createBy: {},
                updateTime: {},
                updateBy: {},
            },
            url: {
                add: "game/gameCampaignTypeMarryRank/add",
                edit: "game/gameCampaignTypeMarryRank/edit"
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
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "bigReward", "bigRewardFight", "rankNum", "rankRewardEmail", "callOnMessage", "helpMsg", "createTime", "createBy", "updateTime", "updateBy"));
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "bigReward", "bigRewardFight", "rankNum", "rankRewardEmail", "callOnMessage", "helpMsg", "createTime", "createBy", "updateTime", "updateBy"));
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