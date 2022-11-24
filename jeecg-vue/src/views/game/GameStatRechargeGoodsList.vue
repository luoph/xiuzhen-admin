<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="商品分类">
              <a-select placeholder="请选择商品分类" v-model="queryParam.rechargeGroup" initialValue="0">
                <a-select-option :value="0">全部</a-select-option>
                <a-select-option :value="1">直充</a-select-option>
                <a-select-option :value="2">礼包</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
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
  name: 'GameStatRechargeGoodsList',
  description: '直充道具统计',
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
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '充值道具id',
          align: 'center',
          dataIndex: 'productId'
        },
        {
          title: '充值道具名称',
          align: 'center',
          dataIndex: 'productName'
        },
        {
          title: '消费次数',
          align: 'center',
          dataIndex: 'payNum'
        },
        {
          title: '消费次数比例',
          align: 'center',
          dataIndex: 'payNumRate',
          customRender: (text, record) => {
            return this.countRate(record.payNum, record.totalNum);
          }
        },
        {
          title: '消费玩家数',
          align: 'center',
          dataIndex: 'playerNum'
        },
        {
          title: '消费玩家比例',
          align: 'center',
          dataIndex: 'playerNumRate',
          customRender: (text, record) => {
            return this.countRate(record.playerNum, record.totalPlayerNum);
          }
        },
        {
          title: '消费金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        {
          title: '消费金额比例',
          align: 'center',
          dataIndex: 'amountRate',
          customRender: (text, record) => {
            return this.countRate(record.payAmount, record.totalAmount);
          }
        }
      ],
      url: {
        list: 'game/stat/rechargeGoods/list'
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
