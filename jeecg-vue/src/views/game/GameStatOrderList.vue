<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="12" :sm="16">
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"/>
            <!-- <game-server-selector @onSelectServer="change" /> -->
          </a-col>
          <a-col :md="12" :sm="16">
            <a-form-item label="统计日期">
              <a-range-picker format="YYYY-MM-DD" :disabled-date="disabledDate" @calendarChange="calendarChange"
                              :placeholder="['开始日期', '结束日期']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="16">
            <a-form-item label="日期范围">
              <a-radio-group v-model="queryParam.daysType" :default-value="7">
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
        rowKey="date"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
        :scroll="tableScroll"
      >
      </a-table>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import GameServerSelector from '@comp/gameserver/GameServerSelector';
import JDate from '@/components/jeecg/JDate.vue';
import moment from 'moment';

export default {
  name: 'GameStatOrderList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    GameServerSelector
  },
  data() {
    return {
      queryParam: {
        daysType: 7
      },
      description: 'game_stat_order管理页面',
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
          title: '统计日期',
          align: 'center',
          dataIndex: 'date',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
          }
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
          dataIndex: 'amount'
        },
        {
          title: '付费率',
          align: 'center',
          dataIndex: 'payRate',
          customRender: text => {
            return !text ? '' : text + '%';
          }
        },
        {
          title: 'arpu',
          align: 'center',
          dataIndex: 'arpu'
        },
        {
          title: 'arppu',
          align: 'center',
          dataIndex: 'arppu'
        },
        {
          title: '服均充值',
          align: 'center',
          dataIndex: 'serverAverageAmount'
        }
      ],
      url: {
        list: 'game/gameStatOrder/list',
        exportXlsUrl: 'game/gameStatOrder/exportXls'
      },
      dictOptions: {},
      tableScroll: {x: 9 * 147 + 50},
      choiceDate: ''
    };
  },
  computed: {},
  methods: {
    moment,
    initDictConfig() {
    },
    onSelectServer(serverId) {
      this.queryParam.serverId = serverId;
    },
    onSelectChannel(channelId) {
      this.queryParam.channelId = channelId;
    },
    onDateChange(dates, dateStrings) {
      this.queryParam.startDate = dateStrings[0];
      this.queryParam.endDate = dateStrings[1];
      this.choiceDate = '';
    },
    change(value) {
      this.queryParam.serverId = value.length > 0 ? value.join(',') : value;
    },
    calendarChange([startTime]) {
      this.choiceDate = startTime;
    },
    disabledDate(current) {
      if (this.choiceDate) {
        return current.month() + 1 != moment(this.choiceDate).format('MM') || current > moment().endOf('day');
      } else {
        return current && current > moment().endOf('day');
      }
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
