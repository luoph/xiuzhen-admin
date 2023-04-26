<template>
  <div>
    <a-row :gutter="30">
      <a-col :span="8">
        <a-form-item label="渠道" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
          <a-select placeholder="请选择渠道" v-model="channel" @change="onSelectChannel">
            <a-select-option v-for="it in channelList" :key="it.name" :value="it.simpleName">
              {{ it.name + ' [' + it.simpleName + ']' }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" v-if="showSdkChannel">
        <a-form-item label="SDK渠道" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
          <a-select placeholder="请选择SDK渠道" v-model="sdkChannel" @change="onSelectSdkChannel">
            <a-select-option v-for="it in sdkChannelList" :key="it.name" :value="it.sdkChannel">
              {{ it.sdkChannel && it.name !== it.sdkChannel ? it.name + ' [' + it.sdkChannel + ']' : it.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8" v-if="showServer">
        <a-form-item label="区服" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
          <a-select :mode="multiple ? 'multiple' : '-'" allowClear show-search
                    placeholder="请选择区服" v-model="serverId"
                    @change="onSelectServer">
            <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">
              {{ server.id > 0 ? server.name + ' [' + server.id + ']' : server.name }}
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
      sdkChannel: undefined,
      serverId: undefined,
      // 渠道列表
      channelList: [],
      // SDK渠道列表
      sdkChannelList: [],
      // 区服列表
      serverList: [],
      url: {
        channelUrl: "game/channelServer/channelList",
      }
    };
  },
  props: {
    multiple: {
      type: Boolean,
      default: false
    },
    showSdkChannel: {
      type: Boolean,
      default: false
    },
    showServer: {
      type: Boolean,
      default: true
    },
    serverAll: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    getChannelList() {
      getAction(this.url.channelUrl).then(res => {
        this.channelList = res.result;
      });
    },
    getSdkChannelList() {
      this.sdkChannelList = [];
      for (const item of this.channelList) {
        if (item.simpleName === this.channel) {
          this.sdkChannelList = item.sdkChannelList;
        }
      }
      // 手动插入一条全部的记录
      this.sdkChannelList.splice(0, 0, {sdkChannel: '', name: '全部'});
    },
    getChannelServerList() {
      this.serverList = [];
      for (const item of this.channelList) {
        if (item.simpleName === this.channel) {
          this.serverList = item.serverList;
        }
      }
      // 手动插入一条全部的记录
      this.serverList.splice(0, 0, {id: 0, name: '全部'});
    },
    // select的事件绑定
    onSelectChannel(value) {
      // 触发父容器的 selectChannel 方法
      this.$emit('onSelectChannel', value);
      this.channel = value;
    },
    onSelectSdkChannel(value) {
      // 触发父容器的 onSelectSdkChannel 方法
      this.$emit('onSelectSdkChannel', value);
      this.sdkChannel = value;
    },
    onSelectServer(value) {
      let result = value;
      if (this.multiple) {
        result = value.join(',');
      }
      // 触发父容器的 selectServer 方法
      this.$emit('onSelectServer', result);
    },
    reset() {
      this.channel = null;
      this.sdkChannel = null;
      this.serverId = null;
    },
  },
  watch: {
    channel: function () {
      this.getSdkChannelList();
      this.getChannelServerList();
    }
  }
};
</script>
<style lang="less" scoped></style>
