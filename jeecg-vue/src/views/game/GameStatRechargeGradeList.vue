<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
        <a-row :gutter="45">
          <a-col :md="6" :sm="8">
            <a-form-item label="充值档位">
              <a-select placeholder="充值档位" v-model="queryParam.grade" showSearch allowClear style="width: 100%">
                <a-select-option :value="0">不限制</a-select-option>
                <a-select-option :value="1">0~6</a-select-option>
                <a-select-option :value="2">7~29</a-select-option>
                <a-select-option :value="3">30~67</a-select-option>
                <a-select-option :value="4">68~97</a-select-option>
                <a-select-option :value="5">98~197</a-select-option>
                <a-select-option :value="6">198~327</a-select-option>
                <a-select-option :value="7">328~647</a-select-option>
                <a-select-option :value="8">648以上</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="8">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayRange" @change="onDayRangeChange">
                <a-radio :value="-1">自定义</a-radio>
                <a-radio :value="0">今天</a-radio>
                <a-radio :value="2">近3天</a-radio>
                <a-radio :value="6">近7天</a-radio>
                <a-radio :value="14">近15天</a-radio>
                <a-radio :value="29">近1月</a-radio>
                <a-radio :value="59">近2月</a-radio>
                <a-radio :value="9999">开服以来</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
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
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
      />
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import { getAction } from '@/api/manage';
import { filterObj } from '@/utils/util';
import moment from 'moment';
import ChannelServerSelector from '@comp/gameserver/ChannelServerSelector';

export default {
  name: 'GameStatRechargeGradeList',
  description: '付费结构统计',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      /* 排序参数 */
      isorter: {
        column: 'countDate',
        order: 'desc'
      },
      timeout: 90000,
      dayRange: 0,
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
          title: '充值档位',
          align: 'center',
          dataIndex: 'grade',
          customRender: function (text) {
            if (text === 1) {
              return '0~6';
            } else if (text === 2) {
              return '7~29';
            } else if (text === 3) {
              return '30~67';
            } else if (text === 4) {
              return '68~97';
            } else if (text === 5) {
              return '98~197';
            } else if (text === 6) {
              return '198~327';
            } else if (text === 7) {
              return '328~647';
            } else if (text === 8) {
              return '>=648';
            }
            return '--';
          }
        },
        {
          title: '付费人数',
          align: 'center',
          dataIndex: 'playerNum'
        },
        // {
        //   title: '付费总人数',
        //   align: 'center',
        //   dataIndex: 'totalPlayerNum'
        // },
        {
          title: '人数占比',
          align: 'center',
          dataIndex: 'playerNumRate',
          customRender: (text, record) => {
            return this.calcRate(record.playerNum, record.totalPlayerNum);
          }
        },
        {
          title: '付费金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        // {
        //   title: '付费总金额',
        //   align: 'center',
        //   dataIndex: 'totalAmount'
        // },
        {
          title: '金额占比',
          align: 'center',
          dataIndex: 'payAmountRate',
          customRender: (text, record) => {
            return this.calcRate(record.payAmount, record.totalAmount);
          }
        },
        {
          title: 'ARPPU',
          align: 'center',
          dataIndex: 'arppu',
          customRender: (text, record) => {
            return this.calcRate(record.payAmount, record.playerNum, false);
          }
        }
      ],
      url: {
        list: 'game/stat/rechargeGrade/list'
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
      if (this.dayRange >= 0) {
        this.selectDayRange(this.dayRange);
      }
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.countDateRange;
      return filterObj(param);
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
      this.dayRange = -1;
    },
    onDateChange(date, dateString) {
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
      this.dayRange = -1;
    },
    onDayRangeChange(e) {
      if (e.target.value >= 0) {
        this.selectDayRange(e.target.value);
      }
    },
    selectDayRange(dayRange) {
      if (dayRange >= 0) {
        const start = dayRange >= 9999 ? '' : moment().subtract(dayRange, 'days').format('YYYY-MM-DD');
        const end = moment().format('YYYY-MM-DD');
        this.queryParam.countDateRange = [start, end];
        this.queryParam.countDate_begin = start;
        this.queryParam.countDate_end = end;
      }
    },
    calcRate: function (n, r, t = true) {
      if (n === null || n === undefined) {
        return '--';
      }

      let rate = r > 0 ? parseFloat(n / r) : 0;
      if (t) {
        return Number(parseFloat(rate * 100).toFixed(2)) + '%';
      }
      return Number(parseFloat(rate).toFixed(2));
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
