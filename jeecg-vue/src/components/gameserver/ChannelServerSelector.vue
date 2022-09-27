<template>
  <div>
    <a-row :gutter="24">
      <a-col :span="12">
        <a-form-item label="渠道名称" :label-col="{ span: 10 }" :wrapper-col="{ span: 16 }">
          <a-select placeholder="请选择渠道" v-model="channel" @change="onSelectChannel">
            <a-select-option v-for="it in channelList" :key="it.name" :value="it.simpleName">
              {{ it.name + '（' +  it.simpleName + '）' }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="10">
        <a-form-item label="服务器名" :label-col="{ span: 12 }" :wrapper-col="{ span: 16 }">
          <!--     多选模式     -->
          <a-select v-if="multiple" mode="multiple" allowClear show-search placeholder="请选择区服" v-model="serverId"
                    @change="onSelectServer">
            <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">
              {{ server.id > 0 ? server.name + '（' + server.id + '）' : server.name }}
            </a-select-option>
          </a-select>
          <a-select v-else placeholder="请选择区服" v-model="serverId" :initialValue="serverId"
                    @change="onSelectServer">
            <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">
              {{ server.id > 0 ? server.name + '（' + server.id + '）' : server.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {getAction} from "@/api/manage";

export default {
  name: "ChannelServerSelector",
  components: {
    getAction
  },
  // 编译成html后加载初始化页面的数据
  created() {
    this.getChannelList();
  },
  // 数据加载 return方式 局限于本页面内 防止污染其他页面的数据
  data() {
    return {
      channel: undefined,
      serverId: undefined,
      // 渠道列表
      channelList: [],
      // 服务器数据
      serverList: [],
      url: {
        channelUrl: "game/gameChannel/list",
        serverListUrl: "game/gameChannelServer/serverList"
      }
    };
  },
  props: {
    multiple: {
      type: Boolean,
      default: false
    },
    serverAll: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    getChannelList() {
      getAction(this.url.channelUrl).then(res => {
        this.channelList = res.result.records;
      });
    },
    getChannelServerList() {
      getAction(this.url.serverListUrl, {channel: this.channel}).then(res => {
        // 手动插入一条全部的记录
        this.serverList = res.result;
        if (this.serverAll) {
          this.serverList.splice(0, 0, {id: 0, name: "全部"});
        }
      });
    },
    // select的事件绑定
    onSelectChannel(value) {
      // 触发父容器的 selectChannel 方法
      this.$emit("onSelectChannel", value);
      this.channel = value;
    },
    onSelectServer(value) {
      let result = value;
      if (this.multiple) {
        result = value.join(",");
      }
      // 触发父容器的 selectServer 方法
      this.$emit("onSelectServer", result);
    },
    reset() {
      this.channel = null;
      this.serverId = null;
    },
  },
  watch: {
    channel: function () {
      this.serverList = [];
      this.getChannelServerList();
      this.serverId = this.serverList.length > 0 ? this.serverList[0].id : "";
    }
  }
};
</script>
<style lang="less" scoped></style>
