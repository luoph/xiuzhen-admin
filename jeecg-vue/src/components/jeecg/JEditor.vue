<template>
    <div class="tinymce-editor">
        <editor v-model="myValue" :init="init" :disabled="disabled" @onClick="onClick"> </editor>
    </div>
</template>

<script>
import tinymce from "tinymce/tinymce";
import Editor from "@tinymce/tinymce-vue";
// theme
import "tinymce/themes/silver";

// plugins
import "tinymce/plugins/image"; // 插入上传图片插件
import "tinymce/plugins/media"; // 插入视频插件
import "tinymce/plugins/table"; // 插入表格插件
import "tinymce/plugins/lists"; // 列表插件
import "tinymce/plugins/wordcount"; // 字数统计插件
import "tinymce/plugins/colorpicker"; // 取色器
import "tinymce/plugins/fullscreen"; // 全屏

export default {
    components: {
        Editor
    },
    props: {
        value: {
            type: String,
            required: false
        },
        baseUrl: {
            type: String,
            default: ""
        },
        triggerChange: {
            type: Boolean,
            default: false,
            required: false
        },
        disabled: {
            type: Boolean,
            default: false
        },
        plugins: {
            type: [String, Array],
            default: "lists image media table wordcount fullscreen"
        },
        toolbar: {
            type: [String, Array],
            default:
                "undo redo |  formatselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists image media table | removeformat | fullscreen"
        }
    },
    data() {
        return {
            // 初始化配置
            init: {
                language_url: `${this.baseUrl}/tinymce/langs/zh_CN.js`,
                language: "zh_CN",
                skin_url: `${this.baseUrl}/tinymce/skins/ui/oxide`,
                content_css: `${this.baseUrl}/tinymce/skins/content/default/content.css`,
                // skin_url: `${this.baseUrl}/tinymce/skins/ui/oxide-dark`, // 暗色系
                // content_css: `${this.baseUrl}/tinymce/skins/content/dark/content.css`, // 暗色系
                height: 400,
                plugins: this.plugins,
                toolbar: this.toolbar,
                branding: false,
                menubar: false,
                toolbar_drawer: false,
                images_upload_handler: (blobInfo, success) => {
                    const img = "data:image/jpeg;base64," + blobInfo.base64();
                    success(img);
                }
            },
            myValue: this.value
        };
    },
    mounted() {
        tinymce.init({});
    },
    methods: {
        // 添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
        onClick(e) {
            this.$emit("onClick", e, tinymce);
        },
        // 可以添加一些自己的自定义事件，如清空内容
        clear() {
            this.myValue = "";
        }
    },
    watch: {
        value(newValue) {
            this.myValue = newValue == null ? "" : newValue;
        },
        myValue(newValue) {
            if (this.triggerChange) {
                this.$emit("change", newValue);
            } else {
                this.$emit("input", newValue);
            }
        }
    }
};
</script>
<style scoped></style>
