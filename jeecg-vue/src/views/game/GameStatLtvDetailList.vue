<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel"
                                     @onSelectServer="onSelectServer"/>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="16">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayType" @change="onDayTypeChange">
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
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :scroll="{ x: 'max-content' }"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"/>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import {filterObj} from '@/utils/util';
import {getAction} from '@/api/manage';
import moment from 'moment';
import ChannelServerSelector from "@comp/gameserver/ChannelServerSelector";

export default {
  description: '留存率',
  name: 'GameStatLtvDetailList',
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
      dayType: 7,
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
          align: 'center',
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
          title: '新用户数',
          dataIndex: 'num',
          width: '60',
          align: 'center'
        },
        {
          title: '1日',
          align: 'center',
          width: '60',
          dataIndex: 'd1',
          customRender: (text, record) => {
            return this.countRate(record.d1, record);
          }
        },
        {
          title: '2日',
          align: 'center',
          width: '60',
          dataIndex: 'd2',
          customRender: (text, record) => {
            return this.countRate(record.d2, record);
          }
        },
        {
          title: '3日',
          align: 'center',
          width: '60',
          dataIndex: 'd3',
          customRender: (text, record) => {
            return this.countRate(record.d3, record);
          }
        },
        {
          title: '4日',
          align: 'center',
          width: '60',
          dataIndex: 'd4',
          customRender: (text, record) => {
            return this.countRate(record.d4, record);
          }
        },
        {
          title: '5日',
          align: 'center',
          width: '60',
          dataIndex: 'd5',
          customRender: (text, record) => {
            return this.countRate(record.d5, record);
          }
        },
        {
          title: '6日',
          align: 'center',
          width: '60',
          dataIndex: 'd6',
          customRender: (text, record) => {
            return this.countRate(record.d6, record);
          }
        },
        {
          title: '7日',
          align: 'center',
          width: '60',
          dataIndex: 'd7',
          customRender: (text, record) => {
            return this.countRate(record.d7, record);
          }
        },
        {
          title: '8日',
          align: 'center',
          width: '60',
          dataIndex: 'd8',
          customRender: (text, record) => {
            return this.countRate(record.d8, record);
          }
        },
        {
          title: '9日',
          align: 'center',
          width: '60',
          dataIndex: 'd9',
          customRender: (text, record) => {
            return this.countRate(record.d9, record);
          }
        },
        {
          title: '10日',
          align: 'center',
          width: '60',
          dataIndex: 'd10',
          customRender: (text, record) => {
            return this.countRate(record.d10, record);
          }
        },
        {
          title: '11日',
          align: 'center',
          width: '60',
          dataIndex: 'd11',
          customRender: (text, record) => {
            return this.countRate(record.d11, record);
          }
        },
        {
          title: '12日',
          align: 'center',
          width: '60',
          dataIndex: 'd12',
          customRender: (text, record) => {
            return this.countRate(record.d12, record);
          }
        },
        {
          title: '13日',
          align: 'center',
          width: '60',
          dataIndex: 'd13',
          customRender: (text, record) => {
            return this.countRate(record.d13, record);
          }
        },
        {
          title: '14日',
          align: 'center',
          width: '60',
          dataIndex: 'd14',
          customRender: (text, record) => {
            return this.countRate(record.d14, record);
          }
        },
        {
          title: '15日',
          align: 'center',
          width: '60',
          dataIndex: 'd15',
          customRender: (text, record) => {
            return this.countRate(record.d15, record);
          }
        },
        {
          title: '16日',
          align: 'center',
          width: '60',
          dataIndex: 'd16',
          customRender: (text, record) => {
            return this.countRate(record.d16, record);
          }
        },
        {
          title: '17日',
          align: 'center',
          width: '60',
          dataIndex: 'd17',
          customRender: (text, record) => {
            return this.countRate(record.d17, record);
          }
        },
        {
          title: '18日',
          align: 'center',
          width: '60',
          dataIndex: 'd18',
          customRender: (text, record) => {
            return this.countRate(record.d18, record);
          }
        },
        {
          title: '19日',
          align: 'center',
          width: '60',
          dataIndex: 'd19',
          customRender: (text, record) => {
            return this.countRate(record.d19, record);
          }
        },
        {
          title: '20日',
          align: 'center',
          width: '60',
          dataIndex: 'd20',
          customRender: (text, record) => {
            return this.countRate(record.d20, record);
          }
        },
        {
          title: '21日',
          align: 'center',
          width: '60',
          dataIndex: 'd21',
          customRender: (text, record) => {
            return this.countRate(record.d21, record);
          }
        },
        {
          title: '22日',
          align: 'center',
          width: '60',
          dataIndex: 'd22',
          customRender: (text, record) => {
            return this.countRate(record.d22, record);
          }
        },
        {
          title: '23日',
          align: 'center',
          width: '60',
          dataIndex: 'd23',
          customRender: (text, record) => {
            return this.countRate(record.d23, record);
          }
        },
        {
          title: '24日',
          align: 'center',
          width: '60',
          dataIndex: 'd24',
          customRender: (text, record) => {
            return this.countRate(record.d24, record);
          }
        },
        {
          title: '25日',
          align: 'center',
          width: '60',
          dataIndex: 'd25',
          customRender: (text, record) => {
            return this.countRate(record.d25, record);
          }
        },
        {
          title: '26日',
          align: 'center',
          width: '60',
          dataIndex: 'd26',
          customRender: (text, record) => {
            return this.countRate(record.d26, record);
          }
        },
        {
          title: '27日',
          align: 'center',
          width: '60',
          dataIndex: 'd27',
          customRender: (text, record) => {
            return this.countRate(record.d27, record);
          }
        },
        {
          title: '28日',
          align: 'center',
          width: '60',
          dataIndex: 'd28',
          customRender: (text, record) => {
            return this.countRate(record.d28, record);
          }
        },
        {
          title: '29日',
          align: 'center',
          width: '60',
          dataIndex: 'd29',
          customRender: (text, record) => {
            return this.countRate(record.d29, record);
          }
        },
        {
          title: '30日',
          align: 'center',
          width: '60',
          dataIndex: 'd30',
          customRender: (text, record) => {
            return this.countRate(record.d30, record);
          }
        },
        {
          title: '45日',
          align: 'center',
          width: '60',
          dataIndex: 'd45',
          customRender: (text, record) => {
            return this.countRate(record.d45, record);
          }
        },
        {
          title: '60日',
          align: 'center',
          width: '60',
          dataIndex: 'd60',
          customRender: (text, record) => {
            return this.countRate(record.d60, record);
          }
        },
        {
          title: '90日',
          align: 'center',
          width: '60',
          dataIndex: 'd90',
          customRender: (text, record) => {
            return this.countRate(record.d90, record);
          }
        },
        {
          title: '120日',
          align: 'center',
          width: '60',
          dataIndex: 'd120',
          customRender: (text, record) => {
            return this.countRate(record.d120, record);
          }
        },
        {
          title: '150日',
          align: 'center',
          width: '60',
          dataIndex: 'd150',
          customRender: (text, record) => {
            return this.countRate(record.d150, record);
          }
        },
        {
          title: '180日',
          align: 'center',
          width: '60',
          dataIndex: 'd180',
          customRender: (text, record) => {
            return this.countRate(record.d180, record);
          }
        },
        {
          title: '270日',
          align: 'center',
          width: '60',
          dataIndex: 'd270',
          customRender: (text, record) => {
            return this.countRate(record.d270, record);
          }
        },
        {
          title: '360日',
          align: 'center',
          width: '60',
          dataIndex: 'd360',
          customRender: (text, record) => {
            return this.countRate(record.d360, record);
          }
        }
      ],
      url: {
        list: 'game/stat/ltvDetail/list',
        update: 'game/stat/ltvDetail/update'
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
    searchReset() {
      this.queryParam = {}
      this.$refs.channelServerSelector.reset();
      this.loadData(1);
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
      getAction(this.url.update, params, this.timeout).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
        this.searchQuery();
      })
    },
    toLtv: function (n, r) {
      if (n === null || n === undefined || r === 0) {
        return '--';
      }
      return parseFloat(n / r).toFixed(2);
    },
    countRate: function (n, record) {
      return this.toLtv(n, record.num);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
