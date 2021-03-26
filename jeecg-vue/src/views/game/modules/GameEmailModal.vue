<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
             @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['title', validatorRules.title]" placeholder="请输入标题"></a-input>
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['remark', validatorRules.remark]" placeholder="请输入描述"
                                :autosize="{ minRows: 2, maxRows: 6 }" />
                </a-form-item>
                <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group @change="contentType($event.target.value)"
                                   v-decorator="['emailType', { initialValue: 1 }, validatorRules.emailType]"
                                   style="width: 100%;">
                        <a-radio-button :value="1">无附件</a-radio-button>
                        <a-radio-button :value="2">有附件</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="contentData" :visible.sync="contentData" label="附件" :labelCol="labelCol"
                             :wrapperCol="wrapperCol">
                    <a-button type="danger" icon="plus" @click="handleAddItem">奖励选择</a-button>
                    <a-textarea
                        v-decorator="['content', { initialValue: itemTree }, validatorRules.content]"
                        placeholder="请输入附件" :autoSize="{ minRows: 2, maxRows: 6 }" />
                    <game-email-item-tree-modal ref="gameEmailItemTreeModal"
                                                @func="getItemTreeJson"></game-email-item-tree-modal>
                </a-form-item>
                <a-form-item v-show="false" label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group v-decorator="['validState', { initialValue: 0 }]" dict style="width: 100%;">
                        <a-radio-button :value="0">有效</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item label="目标类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group @change="selectTarget($event.target.value)"
                                   v-decorator="['targetBodyType', { initialValue: 1 }]" dict
                                   style="width: 100%;">
                        <a-radio-button :value="1">玩家</a-radio-button>
                        <a-radio-button :value="2">服务器</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="playerType" label="玩家ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea
                        v-decorator="['targetBody', { initialValue: '' }]"
                        placeholder="请以英文“,”分割输入多个玩家ID"
                        style="width: 100%;"
                        :autoSize="{ minRows: 2, maxRows: 6 }"
                    />
                </a-form-item>
                <a-form-item v-if="serverType" label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <game-server-selector v-decorator="['targetBodyIds', { initialValue: '' }]"
                                          @onSelectServer="change" />
                </a-form-item>
                <a-form-item label="生效时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker
                        placeholder="请选择生效时间"
                        showTime
                        format="YYYY-MM-DD HH:mm:ss"
                        v-decorator="['sendTime', validatorRules.sendTime]"
                        style="width: 100%;" />
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker
                        placeholder="请选择开始时间"
                        showTime
                        format="YYYY-MM-DD HH:mm:ss"
                        v-decorator="['validStarTime', validatorRules.validStarTime]"
                        style="width: 100%;"
                    />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker
                        placeholder="请选择结束时间"
                        showTime
                        format="YYYY-MM-DD HH:mm:ss"
                        v-decorator="['validEndTime', validatorRules.validEndTime]"
                        style="width: 100%;"
                    />
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
import JSearchSelectTag from "@/components/dict/JSearchSelectTag";
import { Button } from "ant-design-vue";
import GameEmailItemTreeModal from "./GameEmailItemTreeModal";
import ServerSelect from "@/components/gameserver/ServerSelect";
import GameServerSelector from "@comp/gameserver/GameServerSelector";
import moment from "moment";

export default {
    name: "GameEmailModal",
    components: {
        JDate,
        JSearchSelectTag,
        Button,
        GameEmailItemTreeModal,
        ServerSelect,
        GameServerSelector,
        moment
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
            visible: false,
            model: {},
            itemTree: null,
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
                title: { rules: [{ required: true, message: "请输入标题!" }] },
                remark: { rules: [{ required: true, message: "请输入描述!" }] },
                emailType: { rules: [{ required: true, message: "请选择类型!" }] },
                content: { rules: [{ required: true, message: "请添加附件!" }] },
                validState: { rules: [{ required: true, message: "请选择状态!" }] },
                targetBodyType: { rules: [{ required: true, message: "请选择目标类型!" }] },
                targetBodyIds: { rules: [{ required: false, message: "请以英文“,”分割输入多个玩家ID！" }] },
                sendTime: { rules: [{ required: true, message: "请输入生效时间!" }] },
                validStarTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                validEndTime: { rules: [{ required: false, message: "请输入结束时间!" }] }
            },
            serverType: false,
            playerType: true,
            contentData: false,
            url: {
                add: "game/gameEmail/add",
                edit: "game/gameEmail/edit"
            }
        };
    },
    created() {
        this.$form.createForm(this);
    },
    methods: {
        add(record) {
            if (record != null) {
                delete record.id;
                delete record.createTime;
                delete record.createBy;
                delete record.updateTime;
                delete record.updateBy;
                this.edit(record);
            } else {
                this.edit({});
            }
        },
        edit(record) {
            this.visible = true;
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.$nextTick(() => {
                this.form.setFieldsValue(
                    pick(
                        this.model,
                        "id",
                        "title",
                        "remark",
                        "emailType",
                        "content",
                        "validState",
                        "targetBodyType",
                        "targetBodyIds",
                        "targetBody"
                    )
                );
                this.form.setFieldsValue({ sendTime: this.model.sendTime ? moment(this.model.sendTime) : null });
                this.form.setFieldsValue({ validStarTime: this.model.validStarTime ? moment(this.model.validStarTime) : null });
                this.form.setFieldsValue({ validEndTime: this.model.validEndTime ? moment(this.model.validEndTime) : null });
                this.form.setFieldsValue({ targetBody: this.model.targetBodyIds ? this.model.targetBodyIds : null });
            });

            this.selectTarget(record.targetBodyType);
            this.contentType(record.emailType);
        },
        close() {
            this.$emit("close");
            this.visible = false;
            this.serverType = false;
            this.playerType = true;
            this.validatorRules.content = null;
            this.contentData = false;
            this.itemTree = null;
            this.targetBody = "";
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
                    // 时间格式化
                    formData.sendTime = formData.sendTime ? formData.sendTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.validStarTime = formData.validStarTime ? formData.validStarTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.validEndTime = formData.validEndTime ? formData.validEndTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    if (formData.validState === 1) {
                        formData.validState = 0;
                    }

                    this.inputTargetBody(formData);
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
            if (this.validatorRules.content !== null) {
                this.form.setFieldsValue({
                    content: null
                });
            }
        },
        handleCancel() {
            this.close();
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(
                    row,
                    "id",
                    "title",
                    "remark",
                    "emailType",
                    "content",
                    "validState",
                    "targetBodyType",
                    "targetBodyIds",
                    "sendTime",
                    "validStarTime",
                    "validEndTime",
                    "targetBody"
                )
            );
        },
        selectTarget(e) {
            // 1-玩家 2-服务器
            if (e === 1) {
                this.serverType = false;
                this.playerType = true;
            } else if (e === 2) {
                this.serverType = true;
                this.playerType = false;
            }
            this.validatorRules.targetBodyIds = "";
            this.targetBody = "";
        },

        contentType(e) {
            if (e === 1) {
                this.contentData = false;
            } else if (e === 2) {
                this.contentData = true;
                this.validatorRules.content = { rules: [{ required: true, message: "请添加附件!" }] };
                this.validatorRules.content = "";
            }
        },

        handleAddItem() {
            this.$refs.gameEmailItemTreeModal.$emit("getItemTree");
            this.content = null;
        },

        getItemTreeJson(item) {
            this.itemTree = null;
            this.itemTree = item;
            this.form.setFieldsValue({
                content: item
            });
        },

        change(value) {
            this.form.setFieldsValue({
                targetBodyIds: value.length > 0 ? value.join(",") : value
            });
        },

        inputTargetBody(formData) {
            if (this.playerType) {
                let a = this.form.getFieldValue("targetBody");
                if (a !== null && a !== "" && a !== undefined) {
                    formData.targetBodyIds = a;
                }
            }
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
