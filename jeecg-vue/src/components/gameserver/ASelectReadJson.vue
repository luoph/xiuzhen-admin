<template>
    <a-select :placeholder="tips" @change="handlerSelect">
        <a-select-option v-for="(item,i) in selectArray" :key="Math.random()+i" :value="item.type">
            {{ item.name }}
        </a-select-option>
    </a-select>
</template>

<script>
import axios from "axios";

export default {
    name: "ASelectReadJson",
    props: {
        jsonFile: {
            type: String,
            required: true,
            default: ""
        },
        placeholder: {
            type: String,
            default: "请选择"
        }
    },
    data() {
        return {
            tips: this.placeholder,
            selectArray: []
        };
    },
    created() {
        this.readJson();
    },
    methods: {
        handlerSelect(v) {
            this.$emit("onSelectOption", v);
        },
        readJson() {
            let that = this;
            let jsonUrl = "/json/" + this.jsonFile + ".json";
            axios.get(jsonUrl).then(res => {
                if (that.selectArray !== null && that.selectArray.length > 0) {
                    that.selectArray.splice(0);
                }
                that.selectArray = res.data;
            });
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
