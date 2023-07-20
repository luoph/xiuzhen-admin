<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="5" :md="5" :sm="5">
            <a-form-item label="停服区服ID">
              <j-search-select-tag placeholder="请选择区服" v-model="queryParam.sourceServerId" dict="game_server,name,id" />
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="5" :md="5" :sm="5">
            <a-form-item label="停服玩家ID">
              <a-input placeholder="请输入停服的玩家ID" v-model="queryParam.sourcePlayerId" />
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="5" :md="5" :sm="5">
            <a-form-item label="返还区服ID">
              <j-search-select-tag placeholder="请选择区服" v-model="queryParam.targetServerId" dict="game_server,name,id" />
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="5" :md="5" :sm="5">
            <a-form-item label="返还玩家ID">
              <a-input placeholder="请输入返还的玩家ID" v-model="queryParam.targetPlayerId" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="5" :lg="5" :md="5" :sm="5">
              <a-form-item label="充值总金额">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.sourceAmount_begin" />
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.sourceAmount_end" />
              </a-form-item>
            </a-col>
            <a-col :xl="5" :lg="5" :md="5" :sm="5">
              <a-form-item label="返还总仙玉">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.targetNum_begin" />
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.targetNum_end" />
              </a-form-item>
            </a-col>
            <a-col :xl="5" :lg="5" :md="5" :sm="5">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
                <!-- <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date> -->
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('删档返还')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span" @click="copyText(tag)">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="sourceServerIdTitle" class="copy-text">停服区服ID <a-icon type="copy" /></span>
        <span slot="sourcePlayerIdTitle" class="copy-text">停服玩家ID <a-icon type="copy" /></span>
        <span slot="targetServerIdTitle" class="copy-text">返还区服ID <a-icon type="copy" /></span>
        <span slot="targetPlayerIdTitle" class="copy-text">返还的玩家ID <a-icon type="copy" /></span>
        <span slot="accountTitle" class="copy-text">返还玩家账号 <a-icon type="copy" /></span>
        <span slot="nicknameTitle" class="copy-text">返还玩家角色名 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <game-stop-server-refund-record-modal ref="modalForm" @ok="modalFormOk"></game-stop-server-refund-record-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less';
import { mixinDevice } from '@/utils/mixin';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import GameStopServerRefundRecordModal from './modules/GameStopServerRefundRecordModal';

export default {
  name: 'GameStopServerRefundRecordList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    GameStopServerRefundRecordModal
  },
  data() {
    return {
      description: '删档返还管理页面',
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
          // title: '停服区服ID',
          align: 'center',
          dataIndex: 'sourceServerId',
          slots: { title: 'sourceServerIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          // title: '停服玩家ID',
          align: 'center',
          dataIndex: 'sourcePlayerId',
          slots: { title: 'sourcePlayerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '返还区服ID',
          align: 'center',
          dataIndex: 'targetServerId',
          slots: { title: 'targetServerIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          // title: '返还的玩家ID',
          align: 'center',
          dataIndex: 'targetPlayerId',
          slots: { title: 'targetPlayerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '返还玩家账号',
          align: 'center',
          dataIndex: 'account',
          slots: { title: 'accountTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '返还玩家角色名',
          align: 'center',
          dataIndex: 'nickname',
          slots: { title: 'nicknameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '充值总金额',
          align: 'center',
          dataIndex: 'sourceAmount'
        },
        {
          title: '停服区服版本类型',
          align: 'center',
          dataIndex: 'sourceServerVersionType',
          customRender: (text) => {
            return text === 1 ? '普通服' : text === 2 ? 'BT服' : 'N/A';
          }
        },
        {
          title: '返还总仙玉/代金券',
          align: 'center',
          dataIndex: 'targetNum'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        }
      ],
      url: {
        list: '/game/gameStopServerRefundRecord/list',
        delete: '/game/gameStopServerRefundRecord/delete',
        deleteBatch: '/game/gameStopServerRefundRecord/deleteBatch',
        exportXlsUrl: '/game/gameStopServerRefundRecord/exportXls',
        importExcelUrl: 'game/gameStopServerRefundRecord/importExcel'
      },
      dictOptions: {},
      superFieldList: []
    };
  },
  created() {
    this.getSuperFieldList();
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
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
    onDateOk(value) {
      console.log(value);
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: 'int', value: 'sourceServerId', text: '停服区服ID', dictCode: '' });
      fieldList.push({ type: 'int', value: 'sourcePlayerId', text: '停服的玩家ID', dictCode: '' });
      fieldList.push({ type: 'int', value: 'targetServerId', text: '返还区服ID', dictCode: '' });
      fieldList.push({ type: 'int', value: 'targetPlayerId', text: '返还的玩家ID', dictCode: '' });
      fieldList.push({ type: 'BigDecimal', value: 'sourceAmount', text: '充值总金额', dictCode: '' });
      fieldList.push({ type: 'int', value: 'targetNum', text: '返还总仙玉', dictCode: '' });
      fieldList.push({ type: 'date', value: 'createTime', text: '创建时间' });
      this.superFieldList = fieldList;
    }
  }
};
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>