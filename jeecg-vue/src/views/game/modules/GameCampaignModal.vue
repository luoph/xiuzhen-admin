<template>
    <a-modal :title="title" :width="1400" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-tabs defaultActiveKey="1">
                    <a-tab-pane tab="活动信息" key="1">
                        <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-select placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                                <a-select-option :value="1">1-节日活动</a-select-option>
                            </a-select>
                        </a-form-item>
                        <a-form-item label="活动名称（备注）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称（备注）"></a-input>
                        </a-form-item>
                        <!-- <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-select placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                                <a-select-option :value="1">1-登录礼包</a-select-option>
                                <a-select-option :value="2">2-累计充值</a-select-option>
                                <a-select-option :value="3">3-兑换</a-select-option>
                                <a-select-option :value="4">4-节日任务</a-select-option>
                                <a-select-option :value="5">5-Buff-修为加成</a-select-option>
                                <a-select-option :value="6">6-Buff-灵气加成</a-select-option>
                            </a-select>
                        </a-form-item> -->
                        <a-form-item label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <game-server-selector v-decorator="['serverIds', { initialValue: '' }]" @changeSelect="changeSelect" />
                        </a-form-item>
                        <!-- <a-form-item label="活动状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-select placeholder="选择活动状态" v-decorator="['status', validatorRules.status]" initialValue="1">
                                <a-select-option :value="1">有效</a-select-option>
                                <a-select-option :value="0">无效</a-select-option>
                            </a-select>
                        </a-form-item> -->
                        <a-form-item label="活动时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-col :md="7" :sm="8">
                                <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" />
                            </a-col>
                            <a-col :md="7" :sm="8">
                                <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" />
                            </a-col>
                        </a-form-item>
                        <a-form-item label="自动开启" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onAutoOpenChose" v-model="isAutoOpen" />
                        </a-form-item>
                    </a-tab-pane>

                    <a-tab-pane tab="参数配置" key="2" forceRender>
                        <a-form-item label="活动标语（描述）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['description', validatorRules.description]" placeholder="请输入活动标语（描述）"></a-input>
                        </a-form-item>
                        <a-form-item label="活动展示名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['showName', validatorRules.showName]" placeholder="请输入活动展示名称"></a-input>
                        </a-form-item>
                        <a-form-item label="活动图标" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['icon', validatorRules.icon]" placeholder="请输入活动图标"></a-input>
                        </a-form-item>
                        <a-form-item label="活动宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['banner', validatorRules.banner]" placeholder="请输入活动宣传图"></a-input>
                        </a-form-item>
                        <a-form-item label="节日活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <div>
                                <a-row :gutter="16">
                                    <a-col :span="5">活动类型</a-col>
                                    <a-col :span="5">活动图片</a-col>
                                    <a-col :span="4">开始时间</a-col>
                                    <a-col :span="4">结束时间</a-col>
                                    <a-col :span="3">页签顺序</a-col>
                                    <a-col :span="2">操作</a-col>
                                </a-row>
                                <a-row type="flex" :gutter="16" v-for="(item, index) in campaignTypeList" :key="index">
                                    <a-col :span="5">
                                        <a-form-item>
                                            <a-select placeholder="活动类型" v-decorator="['campaignTypeList[' + index + '].type', { initialValue: 1 }]">
                                                <a-select-option :value="1">1-登录礼包</a-select-option>
                                                <a-select-option :value="2">2-累计充值</a-select-option>
                                                <a-select-option :value="3">3-兑换</a-select-option>
                                                <a-select-option :value="4">4-节日任务</a-select-option>
                                                <a-select-option :value="5">5-Buff-修为加成</a-select-option>
                                                <a-select-option :value="6">6-Buff-灵气加成</a-select-option>
                                            </a-select>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :span="5">
                                        <a-form-item>
                                            <a-input v-decorator="['typeImage', item.typeImage]" placeholder="活动图片"></a-input>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :span="4">
                                        <a-form-item>
                                            <a-date-picker
                                                style="width: 100%;"
                                                placeholder="开始时间"
                                                showTime
                                                format="YYYY-MM-DD HH:mm:ss"
                                                v-decorator="['startTime', item.startTime]"
                                            />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :span="4">
                                        <a-form-item>
                                            <a-date-picker
                                                style="width: 100%;"
                                                placeholder="结束时间"
                                                showTime
                                                format="YYYY-MM-DD HH:mm:ss"
                                                v-decorator="['endTime', item.endTime]"
                                            />
                                        </a-form-item>
                                    </a-col>
                                    <a-col :span="3">
                                        <a-form-item>
                                            <a-input-number v-decorator="['sort', item.sort]" placeholder="页签顺序"></a-input-number>
                                        </a-form-item>
                                    </a-col>
                                    <a-col :span="2">
                                        <a-button style="width: 60%; margin: 3px 10px" @click="delRowCustom(index)" icon="minus"></a-button>
                                    </a-col>
                                </a-row>
                                <a-button style="width: 100%; margin-top: 8px; margin-bottom: 8px" type="dashed" icon="plus" @click="addRowCustom">新增活动类型</a-button>
                            </div>
                        </a-form-item>
                    </a-tab-pane>

                    <a-tab-pane tab="活动状态" key="3" forceRender> </a-tab-pane>
                </a-tabs>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import moment from "moment";
import JDate from "@/components/jeecg/JDate";
import GameServerSelector from "@/components/gameserver/GameServerSelector";

export default {
    name: "GameCampaignModal",
    components: {
        JDate,
        GameServerSelector
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
            visible: false,
            model: {},
            // 活动子类型列表
            campaignTypeList: [],
            isAutoOpen: false,
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
                showName: { rules: [{ required: true, message: "请输入活动展示名称!" }] },
                icon: { rules: [{ required: true, message: "请输入活动图标!" }] },
                banner: { rules: [{ required: true, message: "请输入活动宣传图!" }] },
                startTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] }
            },
            url: {
                add: " game/gameCampaign/add",
                edit: " game/gameCampaign/edit"
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
                this.form.setFieldsValue(
                    pick(this.model, "type", "name", "description", "showName", "icon", "banner", "status", "autoOpen", "startTime", "endTime", "createTime", "updateTime")
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
            this.form.setFieldsValue(
                pick(row, "type", "name", "description", "showName", "icon", "banner", "status", "autoOpen", "startTime", "endTime", "createTime", "updateTime")
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
        addRowCustom() {
            this.campaignTypeList.push({});
            this.$forceUpdate();
        },
        delRowCustom(index) {
            console.log(index);
            this.campaignTypeList.splice(index, 1);
            this.$forceUpdate();
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
