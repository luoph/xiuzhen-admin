<template>
  <a-select placeholder="请选择区服ID" v-model="queryParam.serverId" :initialValue="queryParam.serverId"
            @change="selectServer">
    <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">
      {{ server.id + "-" + server.name }}
    </a-select-option>
  </a-select>
</template>
<script>
import {getAction} from "@/api/manage";

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
        serverId: undefined
      },
      serverList: [],
      url: {
        serverUrl: "game/gameServer/all"
      }
    };
  },
  methods: {
    initServerList() {
      getAction(this.url.serverUrl).then(res => {
        this.serverList = res.result;
      });
    },
    selectServer() {
      this.$emit("select", this.queryParam.serverId);
    }
  }
};
</script>

<style lang="less" scoped></style>
