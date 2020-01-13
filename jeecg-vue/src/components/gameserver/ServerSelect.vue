<template>
    <a-select
        v-model="queryParam.serverId"
        :initialValue="queryParam.serverId"
        @change="selectServer"
    >
        <a-select-option value>---请选择区服ID---</a-select-option>
        <a-select-option
            v-for="server in serverList"
            :key="server.name"
            :value="server.id"
        >{{ server.name }}</a-select-option>
    </a-select>
</template>
<script>
import { getAction } from "@/api/manage";
export default {
    name: "ServerSelect",
    components: {
        getAction
    },
    mounted() {
        this.initServerList();
    },

    data() {
        return {
            queryParam: {
                serverId: ""
            },
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
        selectServer() {
            this.$emit("select", this.queryParam.serverId);
            console.log(this.queryParam.serverId);
        }
    }
};
</script>
