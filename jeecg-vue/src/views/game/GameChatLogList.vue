<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
          </a-col>
          <a-col :md="4" :sm="4">
            <a-form-item label="聊天频道">
              <a-select placeholder="聊天频道" v-model="queryParam.type">
                <a-select-option value="4">全部</a-select-option>
                <a-select-option value="1">世界聊天</a-select-option>
                <a-select-option value="2">仙盟聊天</a-select-option>
                <a-select-option value="3">私聊</a-select-option>
                <a-select-option value="5">跨服</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="4">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="4">
            <a-form-item label="玩家名">
              <a-input placeholder="请输入玩家名" v-model="queryParam.nickname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="内容">
              <a-input placeholder="内容" v-model="queryParam.message"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 
            <div class="table-operator">
                <a-button type="primary" icon="download" @click="handleExportXls('登录流水')">导出</a-button>
            </div> -->
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <template slot="onBannedOperation" slot-scope="text, record">
          {{ text.substr(0, text.lastIndexOf('.') + 1) }}
          <a-popconfirm v-if="dataSource.length" title="是否封禁账号?" @confirm="() => onBanned(record)">
            <a href="javascript:;">{{ text.substr(text.lastIndexOf('.') + 1) }}</a>
          </a-popconfirm>
        </template>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import { getAction, putAction } from '@/api/manage';

export default {
  name: 'GameChatLogList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      description: '聊天日志',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '聊天频道',
          align: 'center',
          dataIndex: 'chatChannel'
        },
        {
          title: '发送方',
          align: 'center',
          dataIndex: 'sendPlayerName',
          scopedSlots: { customRender: 'onBannedOperation' }
        },
        {
          title: '接收方',
          align: 'center',
          dataIndex: 'receivePlayerName'
        },
        {
          title: '发送内容',
          align: 'center',
          dataIndex: 'message'
        },
        {
          title: '发送时间',
          align: 'center',
          dataIndex: 'messageTime'
        }
      ],
      url: {
        list: 'game/chatLog/list',
        banned: 'game/gameForbidden/onBanned'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange: function (value, dateStr) {
      this.queryParam.rangeTimeBegin = dateStr[0];
      this.queryParam.rangeTimeEnd = dateStr[1];
    },
    searchQuery() {
      let param = {
        playerId: this.queryParam.playerId,
        message: this.queryParam.message,
        nickname: this.queryParam.nickname,
        type: this.queryParam.type,
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
        rangeTimeBegin: this.queryParam.rangeTimeBegin,
        rangeTimeEnd: this.queryParam.rangeTimeEnd,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      };
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.current = res.result.current;
          this.ipagination.size = res.result.size.toString();
          this.ipagination.total = res.result.total;
          this.ipagination.pages = res.result.pages;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    onBanned(record) {
      this.loading = true;
      let obj = {
        banValue: record.sendPlayerId,
        serverId: record.serverId,
        isForever: 1,
        type: 1,
        banKey: 'playerId',
        reason: record.chatChannel + '（聊天频道）发表不正当言论。'
      };
      putAction(this.url.banned, obj).then((res) => {
        if (res.success) {
          this.$message.success('封禁成功。');
        } else {
          this.$message.warn(res.message);
        }
        this.loading = false;
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
