<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="图片类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select :disabled="isEdit" placeholder="选择图片类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                        <a-select-option :value="1">1-icon</a-select-option>
                        <a-select-option :value="2">2-banner</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-upload
                        listType="picture-card"
                        class="image-uploader"
                        :showUploadList="false"
                        :action="uploadAction"
                        :data="{ isup: 1 }"
                        :headers="headers"
                        :beforeUpload="beforeUpload"
                        @change="handleChange"
                    >
                        <img v-if="picUrl" :src="getImageView()" alt="图片" style="height:104px;max-width:300px" />
                        <div v-else>
                            <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
                            <div class="ant-upload-text">上传</div>
                        </div>
                    </a-upload>
                </a-form-item>
                <a-form-item label="图片名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入图片名"></a-input>
                </a-form-item>
                <a-form-item label="相对路径" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['url', validatorRules.url]" placeholder="请输入相对路径"></a-input>
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注"></a-input>
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
import moment from "moment";
import Vue from "vue";
import { ACCESS_TOKEN } from "@/store/mutation-types";

export default {
    name: "GameImageModal",
    components: {},
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
            visible: false,
            isEdit: false,
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
            uploadLoading: false,
            picUrl: "",
            headers: {},
            validatorRules: {
                type: { rules: [{ required: true, message: "请输入图片类型!" }] },
                name: { rules: [{ required: true, message: "请输入图片名!" }] },
                url: { rules: [{ required: false, message: "请输入相对路径!" }] },
                width: { rules: [{ required: false, message: "请输入图片宽px!" }] },
                height: { rules: [{ required: false, message: "请输入图片高px!" }] },
                remark: { rules: [{ required: true, message: "请输入备注!" }] }
            },
            url: {
                add: "game/gameImage/add",
                edit: "game/gameImage/edit",
                fileUpload: window._CONFIG["domainURL"] + "/game/gameImage/upload",
                imgServer: window._CONFIG["domainURL"] + "/sys/common/view"
            }
        };
    },
    created() {
        const token = Vue.ls.get(ACCESS_TOKEN);
        this.headers = { "X-Access-Token": token };
    },
    computed: {
        uploadAction: function() {
            return this.url.fileUpload;
        }
    },
    methods: {
        add() {
            this.picUrl = "";
            this.edit({});
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "type", "name", "url", "width", "height", "remark"));
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
            this.form.setFieldsValue(pick(row, "type", "name", "url", "width", "height", "remark"));
        },
        beforeUpload: function(file) {
            var fileType = file.type;
            if (fileType.indexOf("image") < 0) {
                this.$message.warning("请上传图片");
                return false;
            }
            return true;
        },
        handleChange(info) {
            this.picUrl = "";
            if (info.file.status === "uploading") {
                this.uploadLoading = true;
                return;
            }
            if (info.file.status === "done") {
                var response = info.file.response;
                this.uploadLoading = false;
                console.log(response);
                if (response.success) {
                    this.model.avatar = response.message;
                    this.picUrl = "Has no pic url yet";
                } else {
                    this.$message.warning(response.message);
                }
            }
        },
        getImageView() {
            return this.url.imgServer + "/" + this.model.avatar;
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
.image-uploader > .ant-upload {
    width: 104px;
    height: 104px;
}
</style>
