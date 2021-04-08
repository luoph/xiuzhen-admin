<template>
    <div>
        <a-select mode="multiple" style="width: 100%" :placeholder="tips" @change="handlerSelect">
            <a-select-option v-for="(item,i) in selectArray" :key="Math.random()+i" :value="item.id">
                {{ item.name }}
            </a-select-option>
        </a-select>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "ASelectReadJsonSome",
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
            this.$emit("onSelectOptionSome", v);
        },
        readJson() {
            let jsonUrl = "/json/" + this.jsonFile + ".json";
            axios.get(jsonUrl).then((res) => {
                if (this.selectArray !== null && this.selectArray.length > 0) {
                    this.selectArray.splice(0);
                }
                this.selectArray = res.data;
            });
        },
        handleChange(value) {
            console.log(`selected ${value}`);
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
