<template>
  <a-row type="flex" :gutter="24">
    <a-col :flex="2">
      <a-form-item label="渠道" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-select placeholder="请选择渠道" v-model="channel" @change="onSelectChannel">
          <a-select-option v-for="it in channelList" :key="it.name" :value="it.simpleName">
            {{ it.name + ' [' + it.simpleName + ']' }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-col>
    <a-col :flex="2" v-if="showSdkChannel">
      <a-form-item label="SDK渠道" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-select placeholder="请选择SDK渠道" v-model="sdkChannel" @change="onSelectSdkChannel" showSearch allowClear style="width: 100%">
          <a-select-option v-for="it in sdkChannelList" :key="it.name" :value="it.sdkChannel">
            {{ it.sdkChannel && it.name !== it.sdkChannel ? it.name + ' [' + it.sdkChannel + ']' : it.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-col>
    <a-col :flex="2" v-if="showServer">
      <a-form-item label="区服" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-select :mode="multiple ? 'multiple' : '-'" placeholder="请选择区服" v-model="serverId" @change="onSelectServer" showSearch allowClear style="width: 100%">
          <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">
            {{ server.id > 0 ? server.name + ' [' + server.id + ']' : server.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-col>
  </a-row>
</template>

<script>
import { getAction } from '@/api/manage';

export default {
  name: 'ChannelServerSelector',
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
      colSpan: 8,
      colSpace: 2,
      // 渠道列表
      channelList: [],
      // SDK渠道列表
      sdkChannelList: [],
      // 区服列表
      serverList: [],
      url: {
        channelUrl: 'game/channelServer/channelList'
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
      getAction(this.url.channelUrl).then((res) => {
        this.onChannelDataChanged(res.result);
      });
    },
    findInArray(array, key, value) {
      for (const item of array) {
        if (item[key] === value) {
          return item;
        }
      }
      return null;
    },
    onChannelDataChanged(result) {
      this.channelList = result;
      this.getSdkChannelList();
      this.getChannelServerList();
    },
    getSdkChannelList() {
      this.sdkChannelList = [];
      let tmpMap = {};
      if (this.channel) {
        const item = this.findInArray(this.channelList, 'simpleName', this.channel);
        for (const it of item.sdkChannelList) {
          tmpMap[it.sdkChannel] = it;
        }
      } else {
        for (const iterator of this.channelList) {
          for (const it of iterator.sdkChannelList) {
            tmpMap[it.sdkChannel] = it;
          }
        }
      }

      this.sdkChannelList = Object.values(tmpMap);
      // 手动插入一条全部的记录
      this.sdkChannelList.splice(0, 0, { sdkChannel: '', name: '全部' });
    },
    getChannelServerList() {
      this.serverList = [];
      let tmpMap = {};
      if (this.channel) {
        const item = this.findInArray(this.channelList, 'simpleName', this.channel);
        for (const it of item.serverList) {
          tmpMap[it.id] = it;
        }
      } else {
        for (const item of this.channelList) {
          for (const it of item.serverList) {
            tmpMap[it.id] = it;
          }
        }
      }
      this.serverList = Object.values(tmpMap);
      // 手动插入一条全部的记录
      this.serverList.splice(0, 0, { id: 0, name: '全部' });
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
    }
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
