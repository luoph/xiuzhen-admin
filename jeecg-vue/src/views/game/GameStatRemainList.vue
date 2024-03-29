<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
        <a-row :gutter="45">
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
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="danger" icon="sync" style="margin-left: 8px" @click="onClickUpdate">刷新</a-button>
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
import { filterObj } from '@/utils/util';
import { getAction } from '@/api/manage';
import ChannelServerSelector from '@comp/gameserver/ChannelServerSelector';
import moment from 'moment';
export default {
  description: '留存统计',
  name: 'GameStatRemainList',
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
      dayRange: 6,
      columns: [
        {
          title: '#',
          dataIndex: '',
          align: 'center',
          width: '60',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          dataIndex: 'countDate',
          align: 'center',
          width: '140',
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
          align: 'center',
          width: '60',
          dataIndex: 'registerNum'
        },
        {
          title: '首日免费角色',
          dataIndex: 'freeNum',
          width: '60',
          align: 'center'
        },
        {
          title: '首日付费角色',
          dataIndex: 'payNum',
          width: '60',
          align: 'center'
        },
        {
          title: '首日付费率',
          align: 'center',
          width: '60',
          dataIndex: 'payNum',
          customRender: (text, record) => {
            return this.toRate(record.payNum, record.registerNum);
          }
        },
        {
          title: '免费次留率',
          align: 'center',
          width: '60',
          dataIndex: 'freeRemain',
          customRender: (text, record) => {
            return this.toRate(record.freeRemain, record.freeNum);
          }
        },
        {
          title: '付费次留率',
          align: 'center',
          width: '60',
          dataIndex: 'payRemain',
          customRender: (text, record) => {
            return this.toRate(record.payRemain, record.payNum);
          }
        },
        {
          title: '总次留率',
          align: 'center',
          width: '60',
          dataIndex: 'registerRemain',
          customRender: (text, record) => {
            return this.toRate(record.registerRemain, record.registerNum);
          }
        }
      ],
      url: {
        list: 'game/stat/remain/list',
        update: 'game/stat/remain/update'
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
    onClickUpdate() {
      // 查询条件
      const params = this.getQueryParams();
      this.loading = true;
      getAction(this.url.update, params, this.timeout)
        .then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message);
          }
        })
        .finally(() => {
          this.loading = false;
          this.searchQuery();
        });
    },
    toRate: function (n, r) {
      if (n === null || n === undefined) {
        return '--';
      }
      let rate = r > 0 ? parseFloat((n / r) * 100).toFixed(2) : parseFloat(0).toFixed(2);
      return rate + '%';
    },
    calcRate: function (n, record) {
      let baseNum = record.registerNum;
      if (record.roleType === 1) {
        baseNum = record.payNum;
      }
      return this.toRate(n, baseNum);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
