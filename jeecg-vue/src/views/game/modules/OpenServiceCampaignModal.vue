<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
                </a-form-item>
                <a-form-item label="活动图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img v-if="model.icon" :src="getImgView(model.icon)" height="80px" :alt="getImgView(model.icon)" style="max-width:100%;font-size: 12px;font-style: italic;" />
                    <game-image-selector placeholder="请选择活动图标" v-decorator="['icon', validatorRules.icon]" />
                </a-form-item>
                <a-form-item label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-if="isEdit" v-decorator="['serverIds', validatorRules.serverIds]" placeholder="区服id"></a-input>
                    <game-server-selector v-model="model.serverIds" @onSelectServer="changeSelect" />
                </a-form-item>
                <a-form-item label="活动备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入活动备注"></a-input>
                </a-form-item>
                <a-form-item label="活动状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择活动状态" v-decorator="['status', validatorRules.status]" initialValue="1">
                        <a-select-option :value="0">无效</a-select-option>
                        <a-select-option :value="1">有效</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="自动开启" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择自动开启状态" v-decorator="['autoOpen', validatorRules.autoOpen]" initialValue="0">
                        <a-select-option :value="0">关闭</a-select-option>
                        <a-select-option :value="1">开启</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="开服活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <div>
                        <a-row :gutter="24">
                            <a-col :span="4" style="text-align:center;">#</a-col>
                            <a-col :span="8" style="text-align:center;">活动类型</a-col>
                            <a-col :span="6" style="text-align:center;">页签顺序</a-col>
                            <a-col :span="6" style="text-align:center;">操作</a-col>
                        </a-row>
                        <a-row :gutter="24" v-for="(item, index) in typeList" :key="index">
                            <a-col :span="4">{{ "活动类型" + (index + 1) }} </a-col>
                            <a-col :span="8">
                                <a-form-item>
                                    <!-- 已经存在的type不允许修改活动类型 -->
                                    <a-select :disabled="isEdit && item.id != null" placeholder="活动类型" v-model="item.type">
                                        <a-select-option :value="1">1-开服排行</a-select-option>
                                        <a-select-option :value="2">2-开服礼包</a-select-option>
                                        <a-select-option :value="3">3-单笔充值</a-select-option>
                                        <a-select-option :value="4">4-寻宝</a-select-option>
                                        <a-select-option :value="5">5-道具消耗</a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :span="6">
                                <a-form-item>
                                    <a-input-number v-model="item.sort" placeholder="页签顺序" style="width: 100%;"></a-input-number>
                                </a-form-item>
                            </a-col>
                            <a-col :span="6">
                                <a-button style="width: 50px; margin: 3px" @click="delRowCustom(index)" icon="minus"></a-button>
                                <a-button style="width: 50px; margin: 3px" @click="editRowCustom(index)" icon="setting"></a-button>
                            </a-col>
                        </a-row>
                        <a-button style="width: 100%; margin-top: 8px; margin-bottom: 8px" type="dashed" icon="plus" @click="addRowCustom">添加</a-button>
                    </div>
                </a-form-item>
            </a-form>
        </a-spin>

        <open-service-campaign-gift-detail-list-modal ref="giftDetailListModal"></open-service-campaign-gift-detail-list-modal>
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
import GameServerSelector from "@/components/gameserver/GameServerSelector";
import GameImageSelector from "../components/GameImageSelector";
import OpenServiceCampaignGiftDetailListModal from "./OpenServiceCampaignGiftDetailListModal";

export default {
    name: "OpenServiceCampaignModal",
    components: {
        JDate,
        GameServerSelector,
        GameImageSelector,
        OpenServiceCampaignGiftDetailListModal
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 1000,
            visible: false,
            isEdit: false,
            isAutoOpen: false,
            model: {},
            // 活动子类型列表
            typeList: [],
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
                serverIds: { rules: [{ required: true, message: "请输入服务器id!" }] },
                icon: { rules: [{ required: true, message: "请输入活动图标!" }] },
                status: { rules: [{ required: true, message: "请输入活动状态!" }] },
                autoOpen: { rules: [{ required: true, message: "请输入自动开启!" }] },
                remark: { rules: [{ required: true, message: "请输入活动备注!" }] }
            },
            url: {
                add: "game/openServiceCampaign/add",
                edit: "game/openServiceCampaign/edit"
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
            this.isEdit = this.model.id != null;
            this.visible = true;
            console.log("OpenServiceCampaignModal, mode:", JSON.stringify(this.model));

            if (this.isEdit) {
                this.isAutoOpen = this.model.autoOpen === 1;
                this.typeList = this.model.typeList;
            } else {
                this.isAutoOpen = false;
                this.model.status = 1;
                this.model.autoOpen = 0;
                this.typeList = [];
            }

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "name", "serverIds", "icon", "status", "autoOpen", "remark"));
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

                    // 子页签列表
                    formData.typeList = this.typeList ? this.typeList : [];
                    formData.autoOpen = this.isAutoOpen ? 1 : 0;
                    // 创建时间参数不传递后台
                    delete formData.createTime;
                    delete formData.updateTime;

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
            this.form.setFieldsValue(pick(row, "name", "serverIds", "icon", "status", "autoOpen", "remark"));
        },
        changeSelect(value) {
            this.form.setFieldsValue({
                serverIds: value.join(",")
            });
        },
        onAutoOpenChose(value) {
            console.log(value);
        },
        addRowCustom() {
            this.typeList.push({ type: 1, sort: this.typeList.length + 1 });
            this.$forceUpdate();
        },
        delRowCustom(index) {
            console.log(index);
            this.typeList.splice(index, 1);
            this.$forceUpdate();
        },
        editRowCustom(index) {
            console.log(this.typeList[index]);
            let typeInfo = this.typeList[index];
            if (typeInfo.type === 1) {
                // 1-开服排行
            } else if (typeInfo.type === 2) {
                // 2-开服礼包
                this.$refs.giftDetailListModal.title = "开服礼包配置";
                this.$refs.giftDetailListModal.visible = true;
                this.$refs.giftDetailListModal.edit(typeInfo);
            } else if (typeInfo.type === 3) {
                // 3-单笔充值
            } else if (typeInfo.type === 4) {
                // 4-寻宝
            } else if (typeInfo.type === 5) {
                // 5-道具消耗
            }
            this.$forceUpdate();
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return `${window._CONFIG["domainURL"]}/${text}`;
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
