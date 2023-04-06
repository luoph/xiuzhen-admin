<template>
  <a-select showSearch placeholder="请选择商品id" v-model="goodsId" :initialValue="goodsId" @change="handleInput">
    <a-select-option value="">请选择</a-select-option>
    <a-select-option v-for="item in goodsOptions" :key="item.name" :value="item.goodsId">
      {{ item.goodsId + "-" + item.name }}
    </a-select-option>
  </a-select>
</template>

<script>
import {loadGoodsOptions} from "@/api/api";

export default {
  name: "GameGoodsSelector",
  props: {
    shopId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      goodsId: this.shopId,
      goodsOptions: []
    };
  },

  mounted() {
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
