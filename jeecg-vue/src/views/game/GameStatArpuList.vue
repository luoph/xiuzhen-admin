<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
        <a-row :gutter="45">
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="8">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayType" @change="onDayTypeChange">
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
              <a-button type="danger" icon="sync" style="margin-left: 8px" @click="onClickUpdate">刷新</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
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
  name: 'GameStatArpuList',
  description: 'ARPU统计',
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
      dayType: 6,
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
          title: '活跃玩家',
          align: 'center',
          dataIndex: 'loginNum'
        },
        {
          title: '付费数',
          align: 'center',
          dataIndex: 'payNum'
        },
        {
          title: '活跃付费率',
          align: 'center',
          dataIndex: 'payRate',
          customRender: (text, record) => {
            return this.calcRate(record.loginPayNum, record.loginNum);
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
          dataIndex: 'newPlayerNum'
        },
        {
          title: '新增付费数',
          align: 'center',
          dataIndex: 'newPayNum'
        },
        {
          title: '新增付费率',
          align: 'center',
          dataIndex: 'newPayRate',
          customRender: (text, record) => {
            return this.calcRate(record.newPayNum, record.newPlayerNum);
          }
        },
        {
          title: '新增充值金额',
          align: 'center',
          dataIndex: 'newPayAmount'
        },
        {
          title: '新增ARPU',
          align: 'center',
          dataIndex: 'newArpu'
        },
        {
          title: '新增ARPPU',
          align: 'center',
          dataIndex: 'newArppu'
        },
        {
          title: '老玩家数',
          align: 'center',
          dataIndex: 'oldPlayerNum'
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
          customRender: (text, record) => {
            return this.calcRate(record.oldPayNum, record.oldPlayerNum);
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
        list: 'game/stat/arpu/list',
        update: 'game/stat/arpu/update'
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
      if (this.dayType > 0) {
        this.selectDayType(this.dayType);
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
    },
    onDateChange(date, dateString) {
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
      this.dayType = 0;
    },
    onDayTypeChange(e) {
      if (e.target.value > 0) {
        this.selectDayType(e.target.value);
      }
    },
    selectDayType(dayType) {
      if (dayType > 0) {
        const start = moment().subtract(dayType, 'days').format('YYYY-MM-DD');
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
    calcRate: function (num, total, fixed = 2) {
      if (num === null || num === undefined) {
        return '--';
      }

      let rate = total > 0 ? parseFloat(num / total) : 0;
      return Number(parseFloat(rate * 100).toFixed(fixed)) + '%';
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
