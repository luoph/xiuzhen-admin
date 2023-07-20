<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="区服">
              <j-search-select-tag placeholder="请选择区服" v-model="queryParam.serverId" dict="game_server,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="玩家ID">
              <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="商品ID">
              <a-input placeholder="请输商品ID" v-model="queryParam.goodsId" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="8" :sm="8">
              <a-form-item label="时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a style="margin-left: 8px" @click="handleToggleSearch">
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
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('虚拟充值订单')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown> -->
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
        @change="handleTableChange"
      >
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="statusSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
        </span>
        <span slot="serverIdTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="playerIdTitle" class="copy-text">玩家ID <a-icon type="copy" /></span>
        <span slot="playerNameTitle" class="copy-text">玩家名 <a-icon type="copy" /></span>
        <span slot="goodsIdTitle" class="copy-text">商品ID <a-icon type="copy" /></span>
        <span slot="goodsNameTitle" class="copy-text">商品名称 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <game-virtual-order-modal ref="modalForm" @ok="modalFormOk"></game-virtual-order-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameVirtualOrderModal from './modules/GameVirtualOrderModal';
import JDate from '@/components/jeecg/JDate.vue';
import { filterObj } from '@/utils/util';

export default {
  name: 'GameVirtualOrderList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameVirtualOrderModal
  },
  data() {
    return {
      description: '虚拟充值订单管理页面',
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
          // title: '区服ID',
          align: 'center',
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          // title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId',
          slots: { title: 'playerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '玩家名',
          align: 'center',
          dataIndex: 'playerName',
          slots: { title: 'playerNameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '商品ID',
          align: 'center',
          dataIndex: 'goodsId',
          slots: { title: 'goodsIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '商品名称',
          align: 'center',
          dataIndex: 'goodsName',
          slots: { title: 'goodsNameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'statusSlot' }
        },
        {
          title: '备注',
          dataIndex: 'remark'
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        }
        // {
        //     title: "操作",
        //     dataIndex: "action",
        //     align: "center",
        //     scopedSlots: { customRender: "action" }
        // }
      ],
      url: {
        list: 'game/gameVirtualOrder/list'
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
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
    onDateOk(value) {
      console.log(value);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
