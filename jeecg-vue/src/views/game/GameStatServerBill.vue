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
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"/>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import {getAction} from '@/api/manage';
import ChannelServerSelector from "@comp/gameserver/ChannelServerSelector";
import {filterObj} from "@/utils/util";

export default {
  name: 'GameStatServerBill',
  description: '服务器流水',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      timeout: 90000,
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
          title: '开始时间',
          align: 'center',
          dataIndex: 'startDate',
          customRender: function (text) {
            return !text ? '--' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endDate',
          customRender: function (text) {
            return !text ? '--' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          title: '流水总额',
          align: 'center',
          dataIndex: 'totalAmount'
        }
      ],
      url: {
        list: 'game/stat/serverBill/list'
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
      console.log(this.queryParam.countDateRange);
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
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
