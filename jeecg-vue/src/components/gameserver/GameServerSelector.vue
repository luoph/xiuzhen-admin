<template>
    <div>
        <span> 渠道 </span>
        <a-select placeholder="请选择渠道" v-model="channelId" initialValue="all" @change="handleChannelChange">
            <a-select-option value="all">所有</a-select-option>
            <a-select-option v-for="channel in channelList" :key="channel.name" :value="channel.id">
                {{ channel.name }}
            </a-select-option>
        </a-select>
        <span> 区服 </span>
        <a-select mode="multiple" allowClear show-search :default-value="[]" style="width: 100%" placeholder="请选择区服" @change="handleServerChange">
            <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">{{ server.id + "-" + server.name }} </a-select-option>
        </a-select>
    </div>
</template>
<script>
import { getAction } from "@/api/manage";
export default {
    name: "GameServerSelector",
    components: {
        getAction
    },
    mounted() {
        this.loadServerList();
    },
    data() {
        return {
            channelId: undefined,
            // 渠道列表
            channelList: [],
            // 服务器数据
            serverList: [],
            allServerList: [],
            url: {
                channelUrl: "game/gameChannel/list",
                serverUrl: "game/gameServer/list",
                channelServerUrl: "game/gameChannelServer/channelWithServer?channelId="
            }
        };
    },
    methods: {
        loadServerList() {
            getAction(this.url.channelUrl).then(res => {
                this.channelList = res.result.records;
            });
            getAction(this.url.serverUrl).then(res => {
                this.allServerList = res.result.records;
            });
        },
        getServerList() {
            if (this.channelId === "all") {
                this.serverList = this.allServerList;
            } else {
                getAction(this.url.channelServerUrl + this.channelId).then(res => {
                    this.serverList = res.result;
                });
            }
        },
        // select的事件绑定
        handleChannelChange(value) {
            this.channelId = value;
        },
        handleServerChange(value) {
            console.log(value);
            // 触发父容器的 selectServer 方法
            this.$emit("onSelectServer", value);
        }
    },
    watch: {
        channelId: function() {
            this.serverList = [];
            this.getServerList();
        }
    }
};
</script>
<style lang="less" scoped></style>
