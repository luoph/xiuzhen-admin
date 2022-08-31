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
          <a-col :md="4" :sm="8">
            <a-form-item label="用户类型">
              <a-select placeholder="请选择用户类型" v-model="queryParam.roleType">
                <a-select-option value="0">所有用户</a-select-option>
                <a-select-option value="1">付费用户</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
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
        @change="handleTableChange"
      ></a-table>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import {filterObj} from '@/utils/util';
import {getAction} from '@/api/manage';

export default {
  description: '留存率',
  name: 'GameDataRemainList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
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
          title: '区服id',
          align: '用户类型',
          width: '80',
          dataIndex: 'roleType',
          customRender: value => {
            let text = '--';
            if (value === 0) {
              text = '所有用户';
            } else if (value === 1) {
              text = '付费用户';
            }
            return text;
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
          title: '次留率',
          align: 'center',
          width: '60',
          dataIndex: 'd2Remain',
          customRender: (text, record) => {
            return this.countRate(record.d2Remain, record);
          }
        },
        {
          title: '3留率',
          align: 'center',
          width: '60',
          dataIndex: 'd3Remain',
          customRender: (text, record) => {
            return this.countRate(record.d3Remain, record);
          }
        },
        {
          title: '4留率',
          align: 'center',
          width: '60',
          dataIndex: 'd4Remain',
          customRender: (text, record) => {
            return this.countRate(record.d4Remain, record);
          }
        },
        {
          title: '5留率',
          align: 'center',
          width: '60',
          dataIndex: 'd5Remain',
          customRender: (text, record) => {
            return this.countRate(record.d5Remain, record);
          }
        },
        {
          title: '6留率',
          align: 'center',
          width: '60',
          dataIndex: 'd6Remain',
          customRender: (text, record) => {
            return this.countRate(record.d6Remain, record);
          }
        },
        {
          title: '7留率',
          align: 'center',
          width: '60',
          dataIndex: 'd7Remain',
          customRender: (text, record) => {
            return this.countRate(record.d7Remain, record);
          }
        },
        {
          title: '15留率',
          align: 'center',
          width: '60',
          dataIndex: 'd15Remain',
          customRender: (text, record) => {
            return this.countRate(record.d15Remain, record);
          }
        },
        {
          title: '30留率',
          align: 'center',
          width: '60',
          dataIndex: 'd30Remain',
          customRender: (text, record) => {
            return this.countRate(record.d30Remain, record);
          }
        },
        {
          title: '60留率',
          align: 'center',
          width: '60',
          dataIndex: 'd60Remain',
          customRender: (text, record) => {
            return this.countRate(record.d60Remain, record);
          }
        },
        {
          title: '90留率',
          align: 'center',
          width: '60',
          dataIndex: 'd90Remain',
          customRender: (text, record) => {
            return this.countRate(record.d90Remain, record);
          }
        },
        {
          title: '120留率',
          align: 'center',
          width: '60',
          dataIndex: 'd120Remain',
          customRender: (text, record) => {
            return this.countRate(record.d120Remain, record);
          }
        },
        {
          title: '180留率',
          align: 'center',
          width: '60',
          dataIndex: 'd180Remain',
          customRender: (text, record) => {
            return this.countRate(record.d180Remain, record);
          }
        },
        {
          title: '360留率',
          align: 'center',
          width: '60',
          dataIndex: 'd360Remain',
          customRender: (text, record) => {
            return this.countRate(record.d360Remain, record);
          }
        }
      ],
      isorter: {
        column: 'countDate',
        order: 'desc'
      },
      url: {
        list: 'gameStat/remain/list'
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
    initDictConfig() {
    },
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    getQueryParams() {
      console.log(this.queryParam.countDateRange);
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.countDateRange;
      return filterObj(param);
    },
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
    },
    toRate: function (n, r) {
      if (n === null || n === undefined) {
        return '--';
      }
      let rate = r > 0 ? parseFloat((n / r) * 100).toFixed(2) : parseFloat(0).toFixed(2);
      return rate + '%';
    },
    countRate: function (n, record) {
      let baseNum = record.registerNum;
      if (record.roleType == 1) {
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
