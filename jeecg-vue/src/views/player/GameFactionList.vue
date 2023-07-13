<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="仙宗id">
              <a-input placeholder="请输入仙宗id" v-model="queryParam.factionId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="仙宗名字">
              <a-input placeholder="请输入仙宗名字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('仙宗信息')">导出</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">改名</a>
          <a-divider type="vertical" />
          <a @click="handleDelete(record)">删除</a>
          <!-- <a-popconfirm title="确定删除仙宗吗?" @confirm="() => handleDelete(record)">
            <a>删除</a>
          </a-popconfirm> -->
        </span>
      </a-table>
    </div>

    <game-faction-modal ref="modalForm" @ok="modalFormOk"></game-faction-modal>
  </a-card>
</template>

<script>
import JInput from '@/components/jeecg/JInput';
import JDate from '@/components/jeecg/JDate.vue';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import moment from 'moment';
import JsonSelector from '@comp/game/JsonSelector';
import ChannelServerSelector from '@comp/gameserver/ChannelServerSelector';
import GameFactionModal from './modules/GameFactionModal';

export default {
  name: 'PlayerItemLogList',
  mixins: [JeecgListMixin],
  description: '道具日志',
  components: {
    JInput,
    JDate,
    JsonSelector,
    ChannelServerSelector,
    moment,
    GameFactionModal
  },
  data() {
    return {
      // 表头
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key: 'rowIndex',
        //   width: 60,
        //   align: 'center',
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1;
        //   }
        // },
        {
          title: '区服ID',
          align: 'center',
          dataIndex: 'serverId'
        },
        {
          title: '仙宗id',
          align: 'center',
          dataIndex: 'factionId'
        },
        {
          title: '仙宗名字',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '宗主id',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '宗主名称',
          align: 'center',
          dataIndex: 'playerName',
          customRender: function (text) {
            return text || '未知';
          }
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 240,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'player/faction/list',
        delete: 'player/faction/delete',
        exportXlsUrl: 'player/faction/exportXls'
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
    onSelectChannel: function (channel) {
      this.queryParam.channel = channel;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    getQueryParams() {
      console.log(this.queryParam.createTimeRange);
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createTimeRange;
      return filterObj(param);
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
    },
    handleDelete(record) {
      this.handleConfrimRequest(this.url.delete, { serverId: record.serverId, factionId: record.factionId }, '是否删除仙宗？', '点击确定删除仙宗', 'delete');
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
