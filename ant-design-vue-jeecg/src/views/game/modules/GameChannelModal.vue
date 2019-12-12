<template>
    <a-drawer :title="title" :width="800" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="800" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="渠道名称">
                    <a-input placeholder="请输入渠道名称" v-decorator="['name', validatorRules.name]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="唯一标识">
                    <a-input placeholder="请输入唯一标识" v-decorator="['simpleName', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="排序字段">
                    <a-input-number v-decorator="['position', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告id">
                    <a-input placeholder="请输入公告id" v-decorator="['noticeId', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="大渠道描述">
                    <a-input placeholder="请输入大渠道描述" v-decorator="['remark', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="扩展字段">
                    <a-input placeholder="请输入扩展字段" v-decorator="['extra', {}]" />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏编号">
                    <a-select placeholder="请选择游戏编号" v-decorator="['gameId', {}]">
                        <a-select-option v-for="game in gameList" :key="game.id" :value="game.id"> {{ game.name }}({{ game.id }}) </a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分组">
                    <a-input placeholder="请输入分组" v-decorator="['groupName', {}]" />
                </a-form-item>
            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { getAction, putAction, httpAction } from "@/api/manage";
import pick from "lodash.pick";
import moment from "moment";
import Vue from "vue";

export default {
    name: "GameChannelModal",
    data() {
        return {
            title: "操作",
            visible: false,
            model: {},
            gameList: [],
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },

            confirmLoading: false,
            form: this.$form.createForm(this),
            validatorRules: {
                name: { rules: [{ required: true, message: "请输入渠道名称!" }] }
            },
            url: {
                add: "/game/gameChannel/add",
                edit: "/game/gameChannel/edit",
                gameInfoListUrl: "/game/gameInfo/list"
            }
        };
    },
    created() {
        this.queryGameInfoList();
    },
    methods: {
        queryGameInfoList() {
            let that = this;
            getAction(that.url.gameInfoListUrl).then(res => {
                if (res.success) {
                    if (res.result instanceof Array) {
                        this.gameList = res.result;
                    } else if (res.result.records instanceof Array) {
                        this.gameList = res.result.records;
                    }
                } else {
                    this.gameList = [];
                }
            });
        },
        add() {
            this.edit({});
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "name", "simpleName", "position", "noticeId", "remark", "extra", "gameId", "groupName"));
                // 时间格式化
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
                    // 时间格式化

                    console.log(formData);
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
