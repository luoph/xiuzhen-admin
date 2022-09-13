<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"/>
          </a-col>
          <a-col :md="4" :sm="4">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="4">
            <a-form-item label="物品id">
              <a-input placeholder="请输入物品id" v-model="queryParam.itemId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="3" :sm="3">
            <a-form-item label="产销类型">
              <a-select placeholder="产销类型" v-model="queryParam.type">
                <a-select-option value="1">产出</a-select-option>
                <a-select-option value="2">消耗</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item v-if="queryParam.type === '1'" key="1" label="产销点">
              <a-select-read-json json-file="item_fall_rule" placeholder="途径"
                                  @onSelectOption="change"></a-select-read-json>
            </a-form-item>
            <a-form-item v-if="queryParam.type === '2'" key="2" label="产销点">
              <a-select-read-json json-file="item_expend" placeholder="途径"
                                  @onSelectOption="change"></a-select-read-json>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>

      <div class="table-operator">
        <a-button type="primary" icon="download" @click="handleExportXls('物品流水')">导出</a-button>
      </div>
    </div>
    <!-- 查询区域-END -->

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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
      </a-table>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import {getAction} from '@/api/manage';
import ASelectReadJson from '@comp/gameserver/ASelectReadJson';

export default {
  name: 'ItemBillList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction,
    ASelectReadJson
  },
  data() {
    return {
      description: '物品流水管理页面',
      type: 0,
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
          title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '玩家名',
          align: 'center',
          dataIndex: 'playerName'
        },
        {
          title: '物品ID',
          align: 'center',
          dataIndex: 'itemId'
        },
        {
          title: '物品名称',
          align: 'center',
          dataIndex: 'itemName'
        },
        {
          title: '产销类型',
          align: 'center',
          dataIndex: 'typeName'
        },
        {
          title: '产销点',
          align: 'center',
          dataIndex: 'wayName'
        },
        {
          title: '变更数量',
          align: 'center',
          dataIndex: 'num'
        },
        {
          title: '原数量',
          align: 'center',
          dataIndex: 'beforeNum'
        },
        {
          title: '现数量',
          align: 'center',
          dataIndex: 'afterNum'
        },
        {
          title: '操作时间',
          align: 'center',
          dataIndex: 'syncTime'
        }
      ],
      url: {
        list: 'player/playerItemLog/itemBillList'
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
    initDictConfig() {
    },
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange: function (value, dateStr) {
      this.queryParam.rangeDateBegin = dateStr[0];
      this.queryParam.rangeDateEnd = dateStr[1];
    },
    searchQuery() {
      let param = {
        way: this.queryParam.way,
        itemId: this.queryParam.itemId,
        type: this.queryParam.type,
        serverId: this.queryParam.serverId,
        playerId: this.queryParam.playerId,
        rangeDateBegin: this.queryParam.rangeDateBegin,
        rangeDateEnd: this.queryParam.rangeDateEnd,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      };
      getAction(this.url.list, param).then(res => {
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
    change(v) {
      this.queryParam.way = v;
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
