<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel"
                                     @onSelectServer="onSelectServer"/>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="8">
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
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import {getAction} from '@/api/manage';
import {filterObj} from '@/utils/util';
import moment from 'moment';
import ChannelServerSelector from '@comp/gameserver/ChannelServerSelector';

export default {
  name: 'GameStatRechargeRankList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      description: '充值用户排行',
      timeout: 90000,
      dayType: 7,
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '玩家id',
          align: 'center',
          dataIndex: 'playerId'
        },

        {
          title: '区服',
          align: 'center',
          dataIndex: 'serverId'
        },
        {
          title: '渠道',
          align: 'channel',
          dataIndex: 'channel'
        },
        {
          title: 'Sdk渠道',
          align: 'channel',
          dataIndex: 'sdkChannel'
        },
        {
          title: '玩家昵称',
          align: 'center',
          dataIndex: 'nickname'
        },
        {
          title: '排名',
          align: 'center',
          dataIndex: 'rank'
        },
        {
          title: '玩家等级',
          align: 'center',
          dataIndex: 'level'
        },
        {
          title: '充值总金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        {
          title: '最近充值',
          align: 'center',
          dataIndex: 'lastPay'
        },
        {
          title: '创角时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '最后登录时间',
          align: 'center',
          dataIndex: 'lastLoginTime'
        },
        {
          title: '最后充值时间',
          align: 'center',
          dataIndex: 'lastPayTime'
        },
        {
          title: '创角天数',
          align: 'center',
          dataIndex: 'playDays'
        },
        {
          title: '登录预警天数',
          align: 'center',
          dataIndex: 'lastLoginDays'
        },
        {
          title: '充值预警天数',
          align: 'center',
          dataIndex: 'lastPayDays'
        }
      ],
      url: {
        list: 'game/stat/rechargeRank/list'
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
    searchReset() {
      this.queryParam = {};
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
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
