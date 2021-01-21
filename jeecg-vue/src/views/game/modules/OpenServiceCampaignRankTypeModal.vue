<template>
    <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
    <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="排行类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择排行类型" v-decorator="['rankType', validatorRules.rankType]" initialValue="1">
                        <a-select-option :value="1">1-境界排行</a-select-option>
                        <a-select-option :value="2">2-仙兽排行</a-select-option>
                        <a-select-option :value="3">3-法宝排行</a-select-option>
                        <a-select-option :value="4">4-圣灵排行</a-select-option>
                        <a-select-option :value="5">5-情缘排行</a-select-option>
                        <a-select-option :value="6">6-飞剑排行</a-select-option>
                        <a-select-option :value="7">7-天书排行</a-select-option>
                        <a-select-option :value="8">8-仙器排行</a-select-option>
                        <a-select-option :value="9">9-仙兽排行</a-select-option>
                        <a-select-option :value="10">10-法宝排行</a-select-option>
                        <a-select-option :value="11">11-圣灵排行</a-select-option>
                        <a-select-option :value="12">12-情缘排行</a-select-option>
                        <a-select-option :value="13">13-飞剑排行</a-select-option>
                        <a-select-option :value="14">14-天书排行</a-select-option>
                        <a-select-option :value="15">15-仙器排行</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="排行类型名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['rankTypeName', validatorRules.rankTypeName]" placeholder="请输入排行类型名称"></a-input>
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
    name: "OpenServiceCampaignRankTypeModal",
    components: {
        JDate
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
                rankType: { rules: [{ required: true, message: "请输入排行类型!" }] },
                rankTypeName: { rules: [{ required: true, message: "请输入排行类型名称!" }] }
            },
            url: {
                add: "game/openServiceCampaignRankType/add",
                edit: "game/openServiceCampaignRankType/edit"
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
            console.log("OpenServiceCampaignRankTypeModal, model:", JSON.stringify(this.model));

            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "rankType", "rankTypeName"));
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
            this.form.setFieldsValue(pick(row, "rankType", "rankTypeName"));
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
