<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="玩家ID">
              <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="道具id">
              <a-input placeholder="请输入道具id" v-model="queryParam.itemId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="途径id">
              <a-input placeholder="请输入途径id" v-model="queryParam.way"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产销类型">
              <a-select placeholder="产销类型" v-model="queryParam.type" @change="resetWays">
                <a-select-option value="0">全部</a-select-option>
                <a-select-option value="1">获得</a-select-option>
                <a-select-option value="2">消耗</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item v-if="queryParam.type === '1'" key="1" label="产出途径">
              <json-selector file="item_fall_rule" :multiple="true" value="id" name="title" placeholder="请选择产出途径" @onJsonValueSelected="onSelectWays" />
            </a-form-item>
            <a-form-item v-else-if="queryParam.type === '2'" key="2" label="消耗途径">
              <json-selector file="item_expend" :multiple="true" value="id" name="name" placeholder="请选择消耗途径" @onJsonValueSelected="onSelectWays" />
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
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('道具日志')">导出</a-button>
    </div>
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
import moment from 'moment';
import JsonSelector from '@comp/game/JsonSelector';

export default {
  name: 'PlayerItemLogList',
  mixins: [JeecgListMixin],
  description: '道具日志',
  components: {
    JDate,
    JsonSelector,
    moment
  },
  data() {
    return {
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
          title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '角色名',
          align: 'center',
          dataIndex: 'nickname',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '区服id',
          align: 'center',
          dataIndex: 'serverId'
        },
        {
          title: '创角区服',
          align: 'center',
          dataIndex: 'sid'
        },
        {
          title: '道具id',
          align: 'center',
          dataIndex: 'itemId'
        },
        {
          title: '道具名',
          align: 'center',
          dataIndex: 'itemName',
          customRender: function (text) {
            return text || '未知';
          }
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'num'
        },
        {
          title: '途径',
          align: 'center',
          dataIndex: 'way'
        },
        {
          title: '途径名',
          align: 'center',
          dataIndex: 'wayName',
          customRender: function (text) {
            return text || '未知';
          }
        },
        {
          title: '更新前数量',
          align: 'center',
          dataIndex: 'beforeNum'
        },
        {
          title: '更新后数量',
          align: 'center',
          dataIndex: 'afterNum'
        },
        {
          title: '方式',
          align: 'center',
          dataIndex: 'type',
          customRender: function (text) {
            return text === 1 ? '获得' : '消耗';
          }
        },
        {
          title: '统计日期',
          align: 'center',
          dataIndex: 'createTime'
        }
      ],
      url: {
        list: 'player/backpackLog/list',
        exportXlsUrl: 'player/backpackLog/exportXls'
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
    getQueryParams() {
      console.log(this.queryParam.createTimeRange);
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createTimeRange;
      return filterObj(param);
    },
    onDateChange(dates, dateStrings) {
      // console.log(dateStrings[0], dateStrings[1]);
      this.queryParam.createTime_begin = dateStrings[0];
      this.queryParam.createTime_end = dateStrings[1];
    },
    onSelectWays(list) {
      if (list.length > 0) {
        this.queryParam.ways = list.join(',');
      } else {
        this.resetWays();
      }
    },
    resetWays() {
      this.queryParam.ways = '';
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
