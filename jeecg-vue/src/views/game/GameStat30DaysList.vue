<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
        <a-row :gutter="45">
          <a-col :md="5" :sm="5">
            <a-form-item label="统计类型">
              <a-select placeholder="统计类型" v-model="queryParam.type">
                <a-select-option :value="1">留存</a-select-option>
                <a-select-option :value="2">LTV</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayRange" @change="onDayRangeChange">
                <a-radio :value="0">自定义</a-radio>
                <a-radio :value="6">近7天</a-radio>
                <a-radio :value="14">近15天</a-radio>
                <a-radio :value="29">近1月</a-radio>
                <a-radio :value="59">近2月</a-radio>
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
    <!--查询区域结束-->
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
  name: 'GameStat30DaysList',
  description: '30日数据',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      isorter: {
        column: 'countDate',
        order: 'desc'
      },
      timeout: 90000,
      dayRange: 6,
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: '100',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          dataIndex: 'countDate',
          width: '120',
          align: 'center',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          title: '渠道',
          dataIndex: 'channel',
          width: '80',
          align: 'center'
        },
        {
          title: '区服',
          dataIndex: 'serverId',
          width: '80',
          align: 'center',
          customRender: function (text) {
            return text === 0 ? '全部' : text;
          }
        },
        {
          title: '新增角色',
          dataIndex: 'registerNum',
          align: 'center',
          width: '120'
        },
        {
          title: '类型',
          dataIndex: 'type',
          align: 'center',
          width: '120',
          customRender: function (text) {
            return text === 1 ? '留存' : 'LTV';
          }
        },
        {
          title: '1日',
          dataIndex: 'd1',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d1, record.registerNum, record.type);
          }
        },
        {
          title: '2日',
          dataIndex: 'd2',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d2, record.registerNum, record.type);
          }
        },
        {
          title: '3日',
          dataIndex: 'd3',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d3, record.registerNum, record.type);
          }
        },
        {
          title: '4日',
          dataIndex: 'd4',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d4, record.registerNum, record.type);
          }
        },
        {
          title: '5日',
          dataIndex: 'd5',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d5, record.registerNum, record.type);
          }
        },
        {
          title: '6日',
          dataIndex: 'd6',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d6, record.registerNum, record.type);
          }
        },
        {
          title: '7日',
          dataIndex: 'd7',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d7, record.registerNum, record.type);
          }
        },
        {
          title: '8日',
          dataIndex: 'd8',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d8, record.registerNum, record.type);
          }
        },
        {
          title: '9日',
          dataIndex: 'd9',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d9, record.registerNum, record.type);
          }
        },
        {
          title: '10日',
          dataIndex: 'd10',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d10, record.registerNum, record.type);
          }
        },
        {
          title: '11日',
          dataIndex: 'd11',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d11, record.registerNum, record.type);
          }
        },
        {
          title: '12日',
          dataIndex: 'd12',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d12, record.registerNum, record.type);
          }
        },
        {
          title: '13日',
          dataIndex: 'd13',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d13, record.registerNum, record.type);
          }
        },
        {
          title: '14日',
          dataIndex: 'd14',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d14, record.registerNum, record.type);
          }
        },
        {
          title: '15日',
          dataIndex: 'd15',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d15, record.registerNum, record.type);
          }
        },
        {
          title: '16日',
          dataIndex: 'd16',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d16, record.registerNum, record.type);
          }
        },
        {
          title: '17日',
          dataIndex: 'd17',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d17, record.registerNum, record.type);
          }
        },
        {
          title: '18日',
          dataIndex: 'd18',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d18, record.registerNum, record.type);
          }
        },
        {
          title: '19日',
          dataIndex: 'd19',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d19, record.registerNum, record.type);
          }
        },
        {
          title: '20日',
          dataIndex: 'd20',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d20, record.registerNum, record.type);
          }
        },
        {
          title: '21日',
          dataIndex: 'd21',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d21, record.registerNum, record.type);
          }
        },
        {
          title: '22日',
          dataIndex: 'd22',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d22, record.registerNum, record.type);
          }
        },
        {
          title: '23日',
          dataIndex: 'd23',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d23, record.registerNum, record.type);
          }
        },
        {
          title: '24日',
          dataIndex: 'd24',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d24, record.registerNum, record.type);
          }
        },
        {
          title: '25日',
          dataIndex: 'd25',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d25, record.registerNum, record.type);
          }
        },
        {
          title: '26日',
          dataIndex: 'd26',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d26, record.registerNum, record.type);
          }
        },
        {
          title: '27日',
          dataIndex: 'd27',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d27, record.registerNum, record.type);
          }
        },
        {
          title: '28日',
          dataIndex: 'd28',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d28, record.registerNum, record.type);
          }
        },
        {
          title: '29日',
          dataIndex: 'd29',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d29, record.registerNum, record.type);
          }
        },
        {
          title: '30日',
          dataIndex: 'd30',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.calcRate(record.d30, record.registerNum, record.type);
          }
        }
      ],
      url: {
        list: 'game/stat/30days/list'
      },
      dictOptions: {}
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
      this.dayRange = 6;
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
    calcRate: function (n, r, t) {
      if (n === null || n === undefined) {
        return '--';
      }

      let rate = r > 0 ? parseFloat(n / r) : 0;
      if (t === 1) {
        return Number(parseFloat(rate * 100).toFixed(2)) + '%';
      } else {
        return Number(parseFloat(rate).toFixed(2));
      }
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
