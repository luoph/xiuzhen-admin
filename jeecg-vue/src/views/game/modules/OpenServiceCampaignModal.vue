<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称"></a-input>
                </a-form-item>
                <a-form-item label="活动图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img v-if="model.icon" :src="getImgView(model.icon)" :alt="getImgView(model.icon)" class="icon-image" />
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
            </a-form>

            <a-tabs v-if="isEdit" defaultActiveKey="1">
                <a-tab-pane tab="活动类型配置" key="1">
                    <open-service-campaign-type-list ref="typeList"></open-service-campaign-type-list>
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
import GameServerSelector from "@/components/gameserver/GameServerSelector";
import GameImageSelector from "../components/GameImageSelector";
import OpenServiceCampaignTypeList from "../OpenServiceCampaignTypeList";

export default {
    name: "OpenServiceCampaignModal",
    components: {
        JDate,
        GameImageSelector,
        GameServerSelector,
        OpenServiceCampaignTypeList
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 1200,
            visible: false,
            isEdit: false,
            isAutoOpen: false,
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
            console.log("OpenServiceCampaignModal, model:", JSON.stringify(this.model));

            if (this.isEdit) {
                this.isAutoOpen = this.model.autoOpen === 1;
            } else {
                this.isAutoOpen = false;
                this.model.status = 1;
                this.model.autoOpen = 0;
            }

            this.$nextTick(() => {
                if (this.isEdit) {
                    this.$refs.typeList.edit(record);
                }
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
.icon-image {
    width: 100%;
    max-width: 200px;
    max-height: 200px;
    object-fit: scale-down;
}

.image {
    width: 100%;
    height: 100px;
    object-fit: contain;
}

/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>
