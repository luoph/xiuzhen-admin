<template>
    <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="确定">
        <a-spin :spinning="confirmLoading">
            <div v-html="model" class="noticeContent"></div>
        </a-spin>
    </a-modal>
</template>

<script>
export default {
    name: "GameHtmlPreviewModal",
    components: {},
    data() {
        return {
            title: "HTML预览",
            visible: false,
            model: "",
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            confirmLoading: false
        };
    },
    created() {},
    methods: {
        edit(text) {
            this.model = text;
            this.visible = true;
        },
        close() {
            this.$emit("close");
            this.visible = false;
        },
        handleOk() {},
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

.noticeContent {
    max-height: 800px;
    overflow-y: auto;
    overflow-x: hidden;
}
</style>
