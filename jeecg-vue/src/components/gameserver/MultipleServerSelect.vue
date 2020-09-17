<template>
    <a-select mode="multiple" :default-value="[]" style="width: 100%" placeholder="请选择指定的服务器" @change="handleChange">
        <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">{{ server.name }} </a-select-option>
    </a-select>
</template>
<script>
import { getAction } from "@/api/manage";
export default {
    name: "MultipleServerSelect",
    components: {
        getAction
    },
    mounted() {
        this.initServerList();
    },
    data() {
        return {
            serverList: [],
            url: {
                serverUrl: "game/gameServer/list"
            }
        };
    },
    methods: {
        initServerList() {
            getAction(this.url.serverUrl).then(res => {
                this.serverList = res.result.records;
            });
        },
        handleChange(value) {
            console.log(value);
            this.$emit("changeSelect", value);
        }
    }
};
</script>
<style lang="less" scoped></style>
