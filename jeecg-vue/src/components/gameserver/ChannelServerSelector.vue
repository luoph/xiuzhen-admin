<template>
  <a-row :gutter="24">
    <a-col :span="8">
      <a-form-item label="渠道" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-select
          :mode="multiChannel ? 'multiple' : 'default'"
          placeholder="请选择渠道"
          v-model="channel"
          :maxTagCount="2"
          :maxTagTextLength="24"
          @change="onSelectChannel"
          :filterOption="filterOption"
          showSearch
          allowClear
          style="width: 100%"
        >
          <a-select-option v-for="it in channelList" :key="it.name" :value="it.simpleName">
            {{ it.name + ' [' + it.simpleName + ']' }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-col>
    <a-col :span="8" v-if="showSdkChannel">
      <a-form-item label="SDK渠道" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-select
          :mode="multiSdkChannel ? 'multiple' : 'default'"
          placeholder="请选择SDK渠道"
          v-model="sdkChannel"
          :maxTagCount="2"
          :maxTagTextLength="24"
          @change="onSelectSdkChannel"
          :filterOption="filterOption"
          showSearch
          allowClear
          style="width: 100%"
        >
          <a-select-option v-for="it in sdkChannelList" :key="it.name" :value="it.sdkChannel">
            {{ it.sdkChannel && it.name !== it.sdkChannel ? it.name + ' [' + it.sdkChannel + ']' : it.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-col>
    <a-col :span="8" v-if="showServer">
      <a-form-item label="区服" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <a-select
          :mode="multiServer ? 'multiple' : 'default'"
          placeholder="请选择区服"
          v-model="serverId"
          :maxTagCount="2"
          :filterOption="filterOption"
          @change="onSelectServer"
          showSearch
          allowClear
          style="width: 100%"
        >
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
    multiChannel: {
      type: Boolean,
      default: false
    },
    multiSdkChannel: {
      type: Boolean,
      default: false
    },
    multiServer: {
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
    }
  },
  methods: {
    getChannelList() {
      getAction(this.url.channelUrl).then((res) => {
        this.onChannelDataChanged(res.result);
      });
    },
    findInArray(array, key, other) {
      for (const item of array) {
        if (other.includes(item[key])) {
          return item;
        }
      }
      return null;
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    onChannelDataChanged(result) {
      this.channelList = result;
      this.getSdkChannelList();
      this.getChannelServerList();
    },
    getSdkChannelList() {
      this.sdkChannelList = [];
      let tmpMap = {};
      let selectedChannels = this.multiChannel ? this.channel : [this.channel];
      if (selectedChannels.length > 0) {
        const item = this.findInArray(this.channelList, 'simpleName', selectedChannels);
        for (const it of item.sdkChannelList) {
          tmpMap[it.sdkChannel] = it;
        }
      } else {
        for (const item of this.channelList) {
          for (const it of item.sdkChannelList) {
            tmpMap[it.sdkChannel] = it;
          }
        }
      }

      this.sdkChannelList = Object.values(tmpMap);
    },
    getChannelServerList() {
      this.serverList = [];
      let tmpMap = {};
      let selectedChannels = this.multiChannel ? this.channel : [this.channel];
      if (selectedChannels.length > 0) {
        const item = this.findInArray(this.channelList, 'simpleName', selectedChannels);
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
    },
    // select的事件绑定
    onSelectChannel(value) {
      // 触发父容器的 selectChannel 方法
      let result = this.multiChannel ? value.join(',') : value;
      this.$emit('onSelectChannel', result);
    },
    onSelectSdkChannel(value) {
      // 触发父容器的 onSelectSdkChannel 方法
      let result = this.multiSdkChannel ? value.join(',') : value;
      this.$emit('onSelectSdkChannel', result);
    },
    onSelectServer(value) {
      let result = this.multiServer ? value.join(',') : value;
      // 触发父容器的 selectServer 方法
      this.$emit('onSelectServer', result);
    },
    reset() {
      this.channel = this.multiChannel ? [] : null;
      this.sdkChannel = this.multiSdkChannel ? [] : null;
      this.serverId = this.multiServer ? [] : null;
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
<style lang="less" scoped>
</style>
