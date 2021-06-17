<template>
    <a-modal :title="title" :width="1400" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select :disabled="isEdit" placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                        <a-select-option :value="1">1-节日活动</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="活动展示名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['showName', validatorRules.showName]" placeholder="请输入活动展示名称"></a-input>
                </a-form-item>
                <a-form-item label="活动标语（描述）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['description', validatorRules.description]" placeholder="请输入活动标语（描述）"></a-input>
                </a-form-item>
                <a-form-item label="活动名称（备注）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称（备注）"></a-input>
                </a-form-item>
                <a-form-item label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-if="isEdit" v-decorator="['serverIds', validatorRules.serverIds]" placeholder="区服id"></a-input>
                    <game-server-selector v-model="model.serverIds" @onSelectServer="changeSelect" />
                </a-form-item>
                <a-form-item label="时间类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="选择活动类型" @change="handleTimeTypeChange" v-decorator="['timeType', validatorRules.timeType]" initialValue="1">
                        <a-select-option :value="1">1-时间范围</a-select-option>
                        <a-select-option :value="2">2-开服第N天</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item v-show="model.timeType == 1" label="活动时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-form-item>
                        <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" />
                    </a-form-item>
                    <a-form-item>
                        <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" />
                    </a-form-item>
                </a-form-item>
                <a-form-item v-show="model.timeType == 2" label="开始天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['startDay', validatorRules.startDay]" placeholder="请输入开始天数(开服第n天, 0表示开服第1天)" style="width: 100%" />
                </a-form-item>
                <a-form-item v-show="model.timeType == 2" label="持续天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入持续天数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="活动图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img v-if="model.icon" :src="getImgView(model.icon)" :alt="getImgView(model.icon)" style="icon-image" />
                    <game-image-selector placeholder="请选择活动图标" v-model="model.icon" />
                </a-form-item>
                <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <img v-if="model.banner" :src="getImgView(model.banner)" :alt="getImgView(model.banner)" style="banner-image" />
                    <game-image-selector placeholder="请选择活动宣传图" v-model="model.banner" />
                </a-form-item>
                <a-form-item label="自动开启" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onAutoOpenChose" v-model="isAutoOpen" />
                </a-form-item>
            </a-form>

            <a-tabs v-if="isEdit" defaultActiveKey="1">
                <a-tab-pane tab="页签配置" key="1">
                    <game-campaign-tab-list ref="tabList"></game-campaign-tab-list>
                </a-tab-pane>
            </a-tabs>
        </a-spin>
    </a-modal>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import moment from "moment";
import JDate from "@/components/jeecg/JDate";
import GameServerSelector from "@/components/gameserver/GameServerSelector";
import GameImageSelector from "../components/GameImageSelector";
import GameCampaignTabList from "../GameCampaignTabList";

export default {
    name: "GameCampaignModal",
    components: {
        JDate,
        GameServerSelector,
        GameImageSelector,
        GameCampaignTabList
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
                type: { rules: [{ required: true, message: "请输入活动类型!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称（备注）!" }] },
                description: { rules: [{ required: true, message: "请输入活动标语（描述）!" }] },
                serverIds: { rules: [{ required: true, message: "请选择区服id！" }] },
                showName: { rules: [{ required: true, message: "请输入活动展示名称!" }] },
                icon: { rules: [{ required: true, message: "请输入活动图标!" }] },
                banner: { rules: [{ required: true, message: "请输入活动宣传图!" }] },
                timeType: { rules: [{ required: true, message: "请输入时间类型!" }] },
                startDay: { rules: [{ required: false, message: "请输入开始时间(开服第n天)!" }] },
                duration: { rules: [{ required: false, message: "请输入持续天数!" }] },
                startTime: { rules: [{ required: false, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: false, message: "请输入结束时间!" }] }
            },
            url: {
                add: "game/gameCampaign/add",
                edit: "game/gameCampaign/edit"
            }
        };
    },
    created() {},
    methods: {
        add() {
            this.edit({});
        },
        edit(record) {
            this.model = Object.assign({}, record);
            this.isEdit = this.model.id != null;
            if (!this.model.timeType) {
                this.model.timeType = 1;
            }
            this.visible = true;
            console.log("GameCampaignModal, model:", JSON.stringify(this.model));

            if (this.isEdit) {
                this.isAutoOpen = this.model.autoOpen === 1;
            } else {
                this.isAutoOpen = false;
                this.model.status = 1;
                this.model.autoOpen = 0;
            }

            this.$nextTick(() => {
                if (this.isEdit) {
                    this.$refs.tabList.edit(record);
                }
                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "type",
                        "name",
                        "description",
                        "showName",
                        "serverIds",
                        "icon",
                        "banner",
                        "status",
                        "autoOpen",
                        "timeType",
                        "startDay",
                        "duration",
                        "startTime",
                        "endTime"
                    )
                );

                // 时间格式化
                this.form.setFieldsValue({ startTime: this.model.startTime ? moment(this.model.startTime) : null });
                this.form.setFieldsValue({ endTime: this.model.endTime ? moment(this.model.endTime) : null });
            });
        },
        close() {
            this.$emit("close");
            this.visible = false;
        },
        handleOk() {
            const that = this;
            // 时间类型校验
            if (this.model.timeType == 1) {
                if (this.model.startDay === undefined || this.model.startDay === null) {
                    that.$message.error("请输入开始天数");
                    return;
                }
                if (this.model.duration === undefined || this.model.duration === null) {
                    that.$message.error("请输入持续天数");
                    return;
                }
            } else if (this.model.timeType == 2) {
                if (this.model.startTime === undefined || this.model.startTime === null) {
                    that.$message.error("请输入开始时间");
                    return;
                }
                if (this.model.endTime === undefined || this.model.endTime === null) {
                    that.$message.error("请输入结束时间");
                    return;
                }
            }

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
                    formData.autoOpen = this.isAutoOpen ? 1 : 0;
                    // 时间格式化
                    formData.startTime = formData.startTime ? formData.startTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.endTime = formData.endTime ? formData.endTime.format("YYYY-MM-DD HH:mm:ss") : null;
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
            this.form.setFieldsValue(
                pick(row, "type", "name", "description", "showName", "icon", "banner", "status", "autoOpen", "timeType", "startDay", "duration", "startTime", "endTime")
            );
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
        },
        handleTimeTypeChange(value) {
            this.model.timeType = value;
        }
    }
};
</script>

// <style lang="less" scoped></style>
<style lang="less" scoped>
.icon-image {
    width: auto;
    height: auto;
    display: block;
    max-width: 180px;
    max-height: 180px;
    object-fit: scale-down;
}

.banner-image {
    width: auto;
    height: auto;
    display: block;
    max-width: 600px;
    max-height: 180px;
    object-fit: scale-down;
}

/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>
