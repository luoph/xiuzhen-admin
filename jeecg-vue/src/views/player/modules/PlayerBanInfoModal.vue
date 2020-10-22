<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="玩家昵称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['nickname', validatorRules.nickname]" placeholder="请输入玩家昵称"></a-input>
                </a-form-item>
                <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="ip标识禁用" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['ipBan', validatorRules.ipBan]" placeholder="请输入ip"></a-input>
                </a-form-item>
                <a-form-item label="玩家id标识禁用" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['playerIdBan', validatorRules.playerIdBan]" placeholder="请输入玩家id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="设备标识禁用" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['identifierBan', validatorRules.identifierBan]" placeholder="请输入设备唯一标识禁用:IMEI或者MAC"></a-input>
                </a-form-item>
                <a-form-item label="封号类型:1-临时,2-永久" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['type', validatorRules.type]" placeholder="请输入封号类型(数字):1-临时封号, 2-永久封号" style="width: 100%" />
                </a-form-item>
                <a-form-item label="封禁原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['banReason', validatorRules.banReason]" placeholder="请输入封禁原因"></a-input>
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择开始时间" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择结束时间" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
            </a-form>
        </a-spin>
    <!-- </a-modal> -->
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";
import ARangePicker from "ant-design-vue/es/date-picker/RangePicker";

export default {
    name: "PlayerBanInfoModal",
    components: {
        ARangePicker,
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
                playerId: { rules: [{ required: true, message: "请输入玩家id!" }] },
                nickname: {},
                serverId: {},
                ipBan: {},
                playerIdBan: {},
                identifierBan: {},
                type: { rules: [{ required: true, message: "请输入封号类型 1-临时封号, 2-永久封号!" }] },
                banReason: {},
                startTime: {},
                endTime: {},
                createTime: {},
                updateTime: {},
                createDate: { rules: [{ required: true, message: "请输入创建日期!" }] },
            },
            url: {
                add: "player/playerBanInfo/add",
                edit: "player/playerBanInfo/edit"
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
                this.form.setFieldsValue(pick(this.model, "playerId", "nickname", "serverId", "ipBan", "playerIdBan", "identifierBan", "type", "banReason", "startTime", "endTime", "createTime", "updateTime", "createDate"));
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
            this.form.setFieldsValue(pick(row, "playerId", "nickname", "serverId", "ipBan", "playerIdBan", "identifierBan", "type", "banReason", "startTime", "endTime", "createTime", "updateTime", "createDate"));
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
