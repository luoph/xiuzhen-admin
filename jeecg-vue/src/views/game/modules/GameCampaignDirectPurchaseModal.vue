<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入主活动id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动页签" style="width: 100%" />
                </a-form-item>
                <a-form-item label="限购数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['limitNum', validatorRules.limitNum]" placeholder="请输入已购数量" style="width: 100%" />
                </a-form-item>
                <a-form-item label="商品" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <game-goods-selector @change="selectGoods" :shopId="model.goodsId"></game-goods-selector>
                </a-form-item>
                <a-form-item label="礼包名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入礼包名"></a-input>
                </a-form-item>
                <a-form-item label="礼包组类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['type', validatorRules.type]" placeholder="请输入礼包组类型" style="width: 100%" />
                </a-form-item>
                <a-form-item label="组排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sort', validatorRules.sort]" placeholder="请输入组排序" style="width: 100%" />
                </a-form-item>
                <a-form-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['reward']" rows="4" placeholder="请输入奖励" />
                </a-form-item>
                <a-form-item label="礼包折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['discount', validatorRules.discount]" placeholder="请输入礼包折扣" style="width: 100%" />
                </a-form-item>
                <a-form-item label="图标颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['color', validatorRules.color]" placeholder="请输入图标颜色" style="width: 100%" />
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
import GameGoodsSelector from "@comp/game/GameGoodsSelector";

export default {
    name: "GameCampaignDirectPurchaseModal",
    components: {
        GameGoodsSelector
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
                campaignId: { rules: [{ required: true, message: "请输入主活动ID!" }] },
                typeId: { rules: [{ required: true, message: "请输入子活动页签!" }] },
                limitNum: { rules: [{ required: true, message: "请输入已购数量!" }] },
                name: { rules: [{ required: true, message: "请输入礼包名!" }] },
                type: { rules: [{ required: true, message: "请输入礼包组类型!" }] },
                sort: { rules: [{ required: true, message: "请输入组排序!" }] },
                reward: { rules: [{ required: true, message: "请输入任务奖励!" }] },
                discount: { rules: [{ required: true, message: "请输入礼包折扣!" }] },
                color: { rules: [{ required: true, message: "请输入图标颜色!" }] },
                goodsId: { rules: [{ required: true, message: "请选择商品Id" }] },
                minLevel: {rules: [{ required: true, message: "请输入最小世界等级!" }]},
                maxLevel: {rules: [{ required: true, message: "请输入最大世界等级!" }]},
            },
            url: {
                add: "game/gameCampaignDirectPurchase/add",
                edit: "game/gameCampaignDirectPurchase/edit"
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
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "goodsId", "limitNum", "price", "discount", "amount", "name", "type", "sort", "reward", "color", "discount", "minLevel", "maxLevel"));
            });
        },
        close() {
            this.$emit("close");
            this.visible = false;
            this.model.goodsId = null;
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
            this.form.setFieldsValue(pick(row, "campaignId", "typeId", "goodsId", "limitNum", "price", "discount", "amount", "name", "type", "sort", "reward", "color", "discount", "minLevel", "maxLevel"));
        },
        selectGoods(e) {
            this.model.goodsId = e;
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
