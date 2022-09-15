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
                <a-select-option value="2">免费用户</a-select-option>
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
          align: 'center',
          width: '80',
          dataIndex: 'serverId'
        },
        {
          title: '用户类型',
          align: 'center',
          width: '80',
          dataIndex: 'roleType',
          customRender: value => {
            let text = '--';
            if (value === 0) {
              text = '所有用户';
            } else if (value === 1) {
              text = '付费用户';
            } else if (value === 2) {
              text = '免费用户';
            }
            return text;
          }
        },
        {
          title: '首日角色数',
          dataIndex: 'd1',
          width: '60',
          align: 'center'
        },
        {
          title: '次留率',
          align: 'center',
          width: '60',
          dataIndex: 'd2',
          customRender: (text, record) => {
            return this.countRate(record.d2, record);
          }
        },
        {
          title: '3留率',
          align: 'center',
          width: '60',
          dataIndex: 'd3',
          customRender: (text, record) => {
            return this.countRate(record.d3, record);
          }
        },
        {
          title: '4留率',
          align: 'center',
          width: '60',
          dataIndex: 'd4',
          customRender: (text, record) => {
            return this.countRate(record.d4, record);
          }
        },
        {
          title: '5留率',
          align: 'center',
          width: '60',
          dataIndex: 'd5',
          customRender: (text, record) => {
            return this.countRate(record.d5, record);
          }
        },
        {
          title: '6留率',
          align: 'center',
          width: '60',
          dataIndex: 'd6',
          customRender: (text, record) => {
            return this.countRate(record.d6, record);
          }
        },
        {
          title: '7留率',
          align: 'center',
          width: '60',
          dataIndex: 'd7',
          customRender: (text, record) => {
            return this.countRate(record.d7, record);
          }
        },
        {
          title: '8留率',
          align: 'center',
          width: '60',
          dataIndex: 'd8',
          customRender: (text, record) => {
            return this.countRate(record.d8, record);
          }
        },
        {
          title: '9留率',
          align: 'center',
          width: '60',
          dataIndex: 'd9',
          customRender: (text, record) => {
            return this.countRate(record.d9, record);
          }
        },
        {
          title: '10留率',
          align: 'center',
          width: '60',
          dataIndex: 'd10',
          customRender: (text, record) => {
            return this.countRate(record.d10, record);
          }
        },
        {
          title: '11留率',
          align: 'center',
          width: '60',
          dataIndex: 'd11',
          customRender: (text, record) => {
            return this.countRate(record.d11, record);
          }
        },
        {
          title: '12留率',
          align: 'center',
          width: '60',
          dataIndex: 'd12',
          customRender: (text, record) => {
            return this.countRate(record.d12, record);
          }
        },
        {
          title: '13留率',
          align: 'center',
          width: '60',
          dataIndex: 'd13',
          customRender: (text, record) => {
            return this.countRate(record.d13, record);
          }
        },
        {
          title: '14留率',
          align: 'center',
          width: '60',
          dataIndex: 'd14',
          customRender: (text, record) => {
            return this.countRate(record.d14, record);
          }
        },
        {
          title: '15留率',
          align: 'center',
          width: '60',
          dataIndex: 'd15',
          customRender: (text, record) => {
            return this.countRate(record.d15, record);
          }
        },
        {
          title: '16留率',
          align: 'center',
          width: '60',
          dataIndex: 'd16',
          customRender: (text, record) => {
            return this.countRate(record.d16, record);
          }
        },
        {
          title: '17留率',
          align: 'center',
          width: '60',
          dataIndex: 'd17',
          customRender: (text, record) => {
            return this.countRate(record.d17, record);
          }
        },
        {
          title: '18留率',
          align: 'center',
          width: '60',
          dataIndex: 'd18',
          customRender: (text, record) => {
            return this.countRate(record.d18, record);
          }
        },
        {
          title: '19留率',
          align: 'center',
          width: '60',
          dataIndex: 'd19',
          customRender: (text, record) => {
            return this.countRate(record.d19, record);
          }
        },
        {
          title: '20留率',
          align: 'center',
          width: '60',
          dataIndex: 'd20',
          customRender: (text, record) => {
            return this.countRate(record.d20, record);
          }
        },
        {
          title: '21留率',
          align: 'center',
          width: '60',
          dataIndex: 'd21',
          customRender: (text, record) => {
            return this.countRate(record.d21, record);
          }
        },
        {
          title: '22留率',
          align: 'center',
          width: '60',
          dataIndex: 'd22',
          customRender: (text, record) => {
            return this.countRate(record.d22, record);
          }
        },
        {
          title: '23留率',
          align: 'center',
          width: '60',
          dataIndex: 'd23',
          customRender: (text, record) => {
            return this.countRate(record.d23, record);
          }
        },
        {
          title: '24留率',
          align: 'center',
          width: '60',
          dataIndex: 'd24',
          customRender: (text, record) => {
            return this.countRate(record.d24, record);
          }
        },
        {
          title: '25留率',
          align: 'center',
          width: '60',
          dataIndex: 'd25',
          customRender: (text, record) => {
            return this.countRate(record.d25, record);
          }
        },
        {
          title: '26留率',
          align: 'center',
          width: '60',
          dataIndex: 'd26',
          customRender: (text, record) => {
            return this.countRate(record.d26, record);
          }
        },
        {
          title: '27留率',
          align: 'center',
          width: '60',
          dataIndex: 'd27',
          customRender: (text, record) => {
            return this.countRate(record.d27, record);
          }
        },
        {
          title: '28留率',
          align: 'center',
          width: '60',
          dataIndex: 'd28',
          customRender: (text, record) => {
            return this.countRate(record.d28, record);
          }
        },
        {
          title: '29留率',
          align: 'center',
          width: '60',
          dataIndex: 'd29',
          customRender: (text, record) => {
            return this.countRate(record.d29, record);
          }
        },
        {
          title: '30留率',
          align: 'center',
          width: '60',
          dataIndex: 'd30',
          customRender: (text, record) => {
            return this.countRate(record.d30, record);
          }
        },
        {
          title: '45留率',
          align: 'center',
          width: '60',
          dataIndex: 'd45',
          customRender: (text, record) => {
            return this.countRate(record.d45, record);
          }
        },
        {
          title: '60留率',
          align: 'center',
          width: '60',
          dataIndex: 'd60',
          customRender: (text, record) => {
            return this.countRate(record.d60, record);
          }
        },
        {
          title: '90留率',
          align: 'center',
          width: '60',
          dataIndex: 'd90',
          customRender: (text, record) => {
            return this.countRate(record.d90, record);
          }
        },
        {
          title: '120留率',
          align: 'center',
          width: '60',
          dataIndex: 'd120',
          customRender: (text, record) => {
            return this.countRate(record.d120, record);
          }
        },
        {
          title: '150留率',
          align: 'center',
          width: '60',
          dataIndex: 'd150',
          customRender: (text, record) => {
            return this.countRate(record.d150, record);
          }
        },
        {
          title: '180留率',
          align: 'center',
          width: '60',
          dataIndex: 'd180',
          customRender: (text, record) => {
            return this.countRate(record.d180, record);
          }
        },
        {
          title: '270留率',
          align: 'center',
          width: '60',
          dataIndex: 'd270',
          customRender: (text, record) => {
            return this.countRate(record.d270, record);
          }
        },
        {
          title: '360留率',
          align: 'center',
          width: '60',
          dataIndex: 'd360',
          customRender: (text, record) => {
            return this.countRate(record.d360, record);
          }
        }
      ],
      isorter: {
        column: 'countDate',
        order: 'desc'
      },
      url: {
        list: 'gameStat/remainDetail/list'
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
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
      return this.toRate(n, record.d1);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
