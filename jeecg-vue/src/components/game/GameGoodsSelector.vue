<template>
    <a-select :placeholder="placeholder" :disabled="disabled" :value="value" @change="handleInput">
        <a-select-option value="">请选择</a-select-option>
        <a-select-option v-for="(item, key) in goodsOptions" :key="key" :value="item.id">
            <span style="display: inline-block;width: 100%" :title="item.name">
                {{ item.name }}
            </span>
        </a-select-option>
    </a-select>
</template>

<script>
import { loadGoodsOptions } from "@/api/api";

export default {
    name: "GameGoodsSelector",
    props: {
        placeholder: String,
        disabled: Boolean,
        value: String
    },
    data() {
        return {
            goodsOptions: []
        };
    },
    watch: {},
    created() {
        // 获取商品列表
        this.initDictData();
    },
    methods: {
        initDictData() {
            loadGoodsOptions().then(res => {
                if (res.success) {
                    this.goodsOptions = res.result;
                }
                if (res.code === 510 || res.code === 500) {
                    this.$message.warning(res.message);
                }
            });
        },
        handleInput(e) {
            let val = e;
            console.log(val);
            this.$emit("change", val);
        }
    }
};
</script>

<style scoped></style>
