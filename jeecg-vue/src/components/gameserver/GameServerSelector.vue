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
    <a-select mode="multiple" allowClear show-search v-model="selectValue" style="width: 100%" placeholder="请选择区服" @change="handleServerChange">
      <a-select-option value="all">所有</a-select-option>
      <a-select-option v-for="server in serverList" :key="server.name" :value="server.id + ''">
        {{ server.id + "-" + server.name }}
      </a-select-option>
    </a-select>
  </div>
</template>
<script>
import {getAction} from "@/api/manage";

export default {
  name: "GameServerSelector",
  props: {
    value: {
      type: String,
      default: null
    }
  },
  components: {
    getAction
  },
  mounted() {
    this.loadServerList();
  },
  data() {
    return {
      channelId: "all",
      // 渠道列表
      channelList: [],
      selectValue: [],
      // 服务器数据
      serverList: [],
      allServerList: [],
      url: {
        channelUrl: "game/channel/allChannel",
        serverUrl: "game/channel/allServer",
        channelServerUrl: "game/channelServer/channelWithServer?channelId="
      }
    };
  },
  methods: {
    loadServerList() {
      let that = this;
      getAction(that.url.channelUrl).then(res => {
        that.channelList = res.result;
      });
      getAction(that.url.serverUrl).then(res => {
        that.allServerList = res.result;
        if (that.channelId === "all") {
          that.serverList = that.allServerList;
        }
      });
    },
    getServerList() {
      let that = this;
      if (that.channelId === "all") {
        that.serverList = that.allServerList;
      } else {
        getAction(that.url.channelServerUrl + that.channelId).then(res => {
          that.serverList = res.result;
        });
      }
    },
    // select的事件绑定
    handleChannelChange(value) {
      this.channelId = value;
    },
    handleServerChange(value) {
      if (value.includes("all")) {
        let that = this;
        value = that.serverList.map((value, index, array) => value.id);
        this.selectValue = value;
      }
      // 触发父容器的 selectServer 方法
      this.$emit("onSelectServer", value);
    },
    updateSelected() {
      this.defaultValue = [];
      if (!this.initValue) {
        return;
      }
      for (let index = 0; index < this.initValue.length; index++) {
        for (const element of this.serverList) {
          console.log(element);
          if (element.id === this.initValue[index]) {
            this.defaultValue.push(this.initValue[index]);
          }
        }
      }
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(value) {
        this.selectValue = value != null ? value.split(",").sort() : [];
        this.getServerList();
      }
    },
    channelId: function () {
      this.getServerList();
    },
    serverList: function () {
      this.updateSelected();
    }
  }
};
</script>
<style lang="less" scoped></style>
