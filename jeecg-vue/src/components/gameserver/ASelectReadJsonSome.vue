<template>
  <div>
    <a-select mode="multiple" style="width: 100%" :placeholder="tips" @change="handlerSelect">
      <a-select-option v-for="item in selectArray" :value="item.type">
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
      let that = this;
      let jsonUrl = "/json/" + this.jsonFile + ".json";
      axios.get(jsonUrl).then((res) => {
        if (that.selectArray !== null && that.selectArray.length > 0) {
          that.selectArray.splice(0);
        }
        that.selectArray = res.data;
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
