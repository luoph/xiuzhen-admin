<template>
    <a-card :bordered="false">
        <a-button type="danger" icon="plus" @click="handleAddItem">奖励选择</a-button>
        <a-textarea
      placeholder="请输入附件"
      :autosize="{ minRows: 2, maxRows: 6 }"
      read-only
      v-model="itemTree"
    />
        <gameEmailItemTree-modal ref="gameEmailItemTreeModal" @func="getItemTreeJson" ></gameEmailItemTree-modal>
    </a-card>
</template>
<script>
import GameEmailItemTreeModal from "./modules/GameEmailItemTreeModal";
import { Button } from 'ant-design-vue';
export default {
    name: "DemoTest",
    components: {
        GameEmailItemTreeModal,
        Button
    },
    data(){
        return{
            itemTree:"",
        }
    },
    dictOptions: {},
    methods: {
        initDictConfig() {},
        handleAddItem(){
            this.$refs.gameEmailItemTreeModal.visible = true;
            this.$refs.gameEmailItemTreeModal.$emit("getItemTree");
            this.itemTree = "";
        },
        getItemTreeJson(item){
            console.log(item);
            this.itemTree = item;
        }
    }
}
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>