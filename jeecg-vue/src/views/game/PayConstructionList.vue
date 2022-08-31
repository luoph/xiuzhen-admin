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
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item label="选择就近天数">
              <a-select placeholder="天数" v-model="queryParam.days">
                <a-select-option value="0">不选择天数</a-select-option>
                <a-select-option value="7">近7天</a-select-option>
                <a-select-option value="15">近15天</a-select-option>
                <a-select-option value="30">近一个月</a-select-option>
                <a-select-option value="60">近两个月</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="选择充值档位">
              <a-select placeholder="档位" v-model="queryParam.payRank">
                <a-select-option value="">不选择档位</a-select-option>
                <a-select-option value="0-6">0-6</a-select-option>
                <a-select-option value="7-29">7-29</a-select-option>
                <a-select-option value="30-67">30-67</a-select-option>
                <a-select-option value="68-97">68-97</a-select-option>
                <a-select-option value="98-197">98-197</a-select-option>
                <a-select-option value="198-327">198-327</a-select-option>
                <a-select-option value="328-647">328-647</a-select-option>
                <a-select-option value="648-9999">648-9999</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>

      <div class="table-operator">
        <a-button type="primary" icon="download" @click="downloadExcel('付费结构')">导出</a-button>
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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
      </a-table>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import {getAction} from '@/api/manage';
import Vue from 'vue';
import {ACCESS_TOKEN} from '@/store/mutation-types';

export default {
  name: 'PayUserRankList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      description: '付费结构管理页面',
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
          dataIndex: 'payRank'
        },
        {
          title: '付费人数',
          align: 'center',
          dataIndex: 'payNumSum'
        },
        {
          title: '人数占比',
          align: 'center',
          dataIndex: 'payNumSumRate',
          customRender: function (text) {
            return text + '%';
          }
        },
        {
          title: '付费金额',
          align: 'center',
          dataIndex: 'payAmountSum'
        },
        {
          title: '金额占比',
          align: 'center',
          dataIndex: 'payAmountSumRate',
          customRender: function (text) {
            return text + '%';
          }
        },
        {
          title: 'ARPPU',
          align: 'center',
          dataIndex: 'arppu'
        }
      ],
      url: {
        list: 'game/payOrderBill/payConstruction',
        exportXlsUrl: 'game/payOrderBill/exportXls',
        downloadExcela: '/game/payOrderBill/download'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {
    },
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange: function (value, dateStr) {
      this.queryParam.rangeDateBegin = dateStr[0];
      this.queryParam.rangeDateEnd = dateStr[1];
    },
    searchQuery() {
      let param = {
        days: this.queryParam.days,
        payRank: this.queryParam.payRank,
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
        rangeDateBegin: this.queryParam.rangeDateBegin,
        rangeDateEnd: this.queryParam.rangeDateEnd,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      };
      getAction(this.url.list, param).then(res => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.current = res.result.current;
          this.ipagination.size = res.result.size.toString();
          this.ipagination.total = res.result.total;
          this.ipagination.pages = res.result.pages;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    downloadExcel(filename) {
      var xhr = new XMLHttpRequest();
      xhr.open('post', window._CONFIG['domianURL'] + this.url.downloadExcela, true);
      xhr.responseType = 'blob';
      xhr.setRequestHeader('Content-Type', 'application/json');
      const token = Vue.ls.get(ACCESS_TOKEN);
      console.log(token);
      xhr.setRequestHeader('X-Access-Token', token);
      xhr.onload = function () {
        var blob = this.response;
        var reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onload = function (e) {
          var a = document.createElement('a');
          a.download = filename + '.xlsx';
          a.href = e.target.result;
          a.click();
        };
      };
      var a = this.queryParam;
      var param = JSON.stringify(a);
      console.log(param);
      xhr.send(param);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
