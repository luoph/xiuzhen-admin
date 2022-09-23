<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="12" :sm="16">
            <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel"
                                     @onSelectServer="onSelectServer"/>
            <!-- <game-server-selector @onSelectServer="change" /> -->
          </a-col>
          <a-col :md="8" :sm="16">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="16">
            <a-form-item label="日期范围">
              <a-radio-group v-model="queryParam.dayType" :default-value="7" @change="onTypeChange">
                <a-radio :value="0">自定义</a-radio>
                <a-radio :value="7">近7天</a-radio>
                <a-radio :value="15">近15天</a-radio>
                <a-radio :value="30">近1月</a-radio>
                <a-radio :value="60">近2月</a-radio>
              </a-radio-group>
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
      <a-button type="primary" icon="download" @click="handleExportXls('game_stat_order')">导出</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :rowKey="record => (record.id != null ? record.id : '0')"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"/>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import GameServerSelector from '@comp/gameserver/GameServerSelector';
import JDate from '@/components/jeecg/JDate.vue';
import {filterObj} from "@/utils/util";
import moment from 'moment';
import ChannelServerSelector from "@comp/gameserver/ChannelServerSelector";

export default {
  name: 'GameStatOrderList',
  description: '充值统计',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    GameServerSelector
  },
  data() {
    return {
      isorter: {
        column: 'countDate',
        order: 'desc'
      },
      timeout: 90000,
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
          dataIndex: 'statTime',
          width: '120',
          align: 'center',
        },
        {
          title: '区服数',
          align: 'center',
          dataIndex: 'serverNum'
        },
        {
          title: '活跃角色数',
          align: 'center',
          dataIndex: 'activeNum'
        },
        {
          title: '付费角色数',
          align: 'center',
          dataIndex: 'payNum'
        },
        {
          title: '付费金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        {
          title: '付费率',
          align: 'center',
          dataIndex: 'payRate',
          customRender: (text, record) => {
            return this.countRate(record.payNum, record.activeNum);
          }
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
          title: '服均充值',
          align: 'center',
          dataIndex: 'averageAmount'
        }
      ],
      url: {
        list: 'game/stat/order/list',
        exportXlsUrl: 'game/stat/order/exportXls'
      },
      dictOptions: {},
    };
  },
  computed: {},
  methods: {
    onSelectChannel: function (channel) {
      this.queryParam.channel = channel;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    getQueryParams() {
      console.log(this.queryParam.countDateRange);
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.countDateRange;
      return filterObj(param);
    },
    searchReset() {
      this.queryParam = {}
      this.$refs.channelServerSelector.reset();
      this.loadData(1);
    },
    onDateChange(date, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
      this.queryParam.dayType = 0;
    },
    onTypeChange(e) {
      console.log('radio checked', e.target.value);
      this.queryParam.countDateRange = [null, null];
    },
    countRate: function (n, r) {
      if (n === null || n === undefined) {
        return '--';
      }

      let rate = r > 0 ? parseFloat(n / r) : 0;
      return Number(parseFloat(rate * 100).toFixed(2)) + '%';
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
