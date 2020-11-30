<template>
    <a-select :placeholder="tips" @change="handlerSelect">
        <a-select-option v-for="select in selectArray" :key="select.name" :value="select.id">
            {{ select.name }}
        </a-select-option>
    </a-select>
</template>

<script>
import { getAction } from "@/api/manage";
import axios from "axios";

export default {
    name: "ASelectReadJson",
    components: {
        getAction
    },
    props: {
        jsonFile: {
            type: String,
            required: true,
            default: ""
        },
        placeholder: {
            type: String,
            required: true,
            default: "下拉选择"
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
            let jsonUrl = "/" + this.jsonFile + ".json";
            axios.get(jsonUrl).then(res => {
                if (this.selectArray !== null && this.selectArray.length > 0) {
                    this.selectArray.splice(0);
                }
                this.selectArray = res.data;
            });
        }
    }
};
</script>

<style scoped>

</style>
