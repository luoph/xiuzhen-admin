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
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item label="选择就近天数">
              <a-select placeholder="天数" v-model="queryParam.days">
                <a-select-option :value="0">不选择天数</a-select-option>
                <a-select-option :value="7">近7天</a-select-option>
                <a-select-option :value="15">近15天</a-select-option>
                <a-select-option :value="30">近一个月</a-select-option>
                <a-select-option :value="60">近两个月</a-select-option>
              </a-select>
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
        <a-button type="primary" icon="download" @click="handleExportXls('数据报表')">导出</a-button>
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

export default {
  name: 'GameDataReportCountList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      description: '数据报表管理页面',
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
          title: '日期',
          align: 'center',
          dataIndex: 'dateStr'
        },
        {
          title: '活跃玩家',
          align: 'center',
          dataIndex: 'loginNum'
        },
        {
          title: '活跃付费数',
          align: 'center',
          dataIndex: 'payNum'
        },
        {
          title: '活跃付费率',
          align: 'center',
          dataIndex: 'payRate',
          customRender: function (text) {
            return text + '%';
          }
        },
        {
          title: '充值金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        {
          title: 'ARPU',
          align: 'center',
          dataIndex: 'arpu'
        },
        {
          title: 'ARPPU',
          align: 'center',
          dataIndex: 'arppu'
        },
        {
          title: '新增玩家',
          align: 'center',
          dataIndex: 'addNum'
        },
        {
          title: '新增付费数',
          align: 'center',
          dataIndex: 'addPayNum'
        },
        {
          title: '新增付费率',
          align: 'center',
          dataIndex: 'addPayRate',
          customRender: function (text) {
            return text + '%';
          }
        },
        {
          title: '新增充值金额',
          align: 'center',
          dataIndex: 'addPayAmount'
        },
        {
          title: '新增ARPU',
          align: 'center',
          dataIndex: 'addArpu'
        },
        {
          title: '新增ARPPU',
          align: 'center',
          dataIndex: 'addArppu'
        },
        {
          title: '老玩家',
          align: 'center',
          dataIndex: 'oldNum'
        },
        {
          title: '老玩家付费数',
          align: 'center',
          dataIndex: 'oldPayNum'
        },
        {
          title: '老玩家付费率',
          align: 'center',
          dataIndex: 'oldPayRate',
          customRender: function (text) {
            return text + '%';
          }
        },
        {
          title: '老玩家充值金额',
          align: 'center',
          dataIndex: 'oldPayAmount'
        },
        {
          title: '老玩家ARPU',
          align: 'center',
          dataIndex: 'oldArpu'
        },
        {
          title: '老玩家ARPPU',
          align: 'center',
          dataIndex: 'oldArppu'
        }
      ],
      url: {
        list: 'game/gameDataReportCount/list',
        delete: 'game/gameDataReportCount/delete',
        deleteBatch: 'game/gameDataReportCount/deleteBatch',
        exportXlsUrl: 'game/gameDataReportCount/exportXls',
        importExcelUrl: 'game/gameDataReportCount/importExcel'
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
        days: this.queryParam.days,
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
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
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
