<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="道具id">
              <a-input placeholder="请输入道具id" v-model="queryParam.itemId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="产销类型">
              <a-select placeholder="产销类型" v-model="queryParam.type" @change="resetWay">
                <a-select-option value="">全部</a-select-option>
                <a-select-option value="1">获得</a-select-option>
                <a-select-option value="2">消耗</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="16">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="16">
            <a-form-item v-if="queryParam.type === '1'" key="1" label="产出途径">
              <a-select-read-json-some json-file="item_fall_rule" placeholder="请选择途径"
                                       @onSelectOptionSome="selectWay"></a-select-read-json-some>
            </a-form-item>
            <a-form-item v-else-if="queryParam.type === '2'" key="2" label="消耗途径">
              <a-select-read-json-some json-file="item_expend" placeholder="请选择途径"
                                       @onSelectOptionSome="selectWay"></a-select-read-json-some>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
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
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource"
               :pagination="ipagination" :loading="loading" @change="handleTableChange">
      </a-table>
    </div>

    <playerItemLog-modal ref="modalForm" @ok="modalFormOk"></playerItemLog-modal>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import PlayerItemLogModal from './modules/PlayerItemLogModal';
import ServerSelect from '@/components/gameserver/ServerSelect';
import ASelectReadJsonSome from '@comp/gameserver/ASelectReadJsonSome';
import JDate from '@/components/jeecg/JDate.vue';
import {filterObj} from '@/utils/util';
import moment from 'moment';

export default {
  name: 'PlayerItemLogList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    PlayerItemLogModal,
    ServerSelect,
    ASelectReadJsonSome,
    moment
  },
  data() {
    return {
      description: 'player_item_log管理页面',
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
          title: '玩家id',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '道具id',
          align: 'center',
          dataIndex: 'itemId'
        },
        {
          title: '道具名',
          align: 'center',
          dataIndex: 'itemName'
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
          dataIndex: 'wayName'
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
        list: 'player/playerItemLog/list',
        exportXlsUrl: 'player/playerItemLog/download'
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
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createTimeRange;
      return filterObj(param);
    },
    selectServerId(serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange(dates, dateStrings) {
      console.log(dateStrings[0], dateStrings[1]);
      this.queryParam.createTime_begin = dateStrings[0];
      this.queryParam.createTime_end = dateStrings[1];
    },
    selectWay(way) {
      if (way.length > 0) {
        this.queryParam.wayName = way.join(',');
      } else {
        this.resetWay();
      }
    },
    resetWay() {
      this.queryParam.wayName = '';
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
