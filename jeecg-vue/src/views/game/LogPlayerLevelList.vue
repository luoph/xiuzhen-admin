<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="区服">
              <j-search-select-tag placeholder="请选择区服" v-model="queryParam.serverId"
                                   dict="game_server,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="昵称">
              <a-input placeholder="请输入昵称模糊查询" v-model="queryParam.nickname"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="8">
            <a-form-item label="境界等级">
              <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.level_begin"/>
              <span class="query-group-split-cust"></span>
              <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.level_end"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="16">
              <a-form-item label="战力范围">
                <a-input placeholder="最小战力值" class="query-group-cust" v-model="queryParam.combatPower_begin"/>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="最大战力值" class="query-group-cust" v-model="queryParam.combatPower_end"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD"
                                :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('境界日志')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <log-player-level-modal ref="modalForm" @ok="modalFormOk"></log-player-level-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import LogPlayerLevelModal from './modules/LogPlayerLevelModal'
import {filterObj} from '@/utils/util';

export default {
  name: 'LogPlayerLevelList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    LogPlayerLevelModal
  },
  data() {
    return {
      description: '境界日志管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '服务器id',
          align: "center",
          dataIndex: 'serverId'
        },
        {
          title: '玩家id',
          align: "center",
          dataIndex: 'playerId'
        },
        {
          title: '玩家昵称',
          align: "center",
          dataIndex: 'nickname'
        },
        {
          title: '境界等级',
          align: "center",
          dataIndex: 'level'
        },
        {
          title: '战力',
          align: "center",
          dataIndex: 'combatPower'
        },
        {
          title: '战力补偿',
          align: "center",
          dataIndex: 'combatPowerCompensation'
        },
        {
          title: '总战力',
          align: "center",
          dataIndex: 'totalCombatPower'
        },
        {
          title: '累计充值',
          align: "center",
          dataIndex: 'rechargeAmount'
        },
        {
          title: '创角时间',
          align: "center",
          dataIndex: 'registerTime'
        },
        {
          title: '创角天数',
          align: "center",
          dataIndex: 'playDays'
        },
        {
          title: '创建时间',
          align: "center",
          dataIndex: 'createTime'
        }
      ],
      url: {
        list: "/stat/logPlayerLevel/list",
        delete: "/stat/logPlayerLevel/delete",
        deleteBatch: "/stat/logPlayerLevel/deleteBatch",
        exportXlsUrl: "/stat/logPlayerLevel/exportXls",
        importExcelUrl: "stat/logPlayerLevel/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
    },
    getQueryParams() {
      console.log(this.queryParam.createDateRange);
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createDateRange;
      return filterObj(param);
    },
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createDate_begin = dateString[0];
      this.queryParam.createDate_end = dateString[1];
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'int', value: 'playerId', text: '玩家id', dictCode: ''})
      fieldList.push({type: 'int', value: 'serverId', text: '服务器id', dictCode: ''})
      fieldList.push({type: 'int', value: 'level', text: '境界等级', dictCode: ''})
      fieldList.push({type: 'int', value: 'combatPower', text: '战力', dictCode: ''})
      fieldList.push({type: 'int', value: 'combatPowerCompensation', text: '战力补偿', dictCode: ''})
      fieldList.push({type: 'date', value: 'createTime', text: '创建时间'})
      fieldList.push({type: 'date', value: 'createDate', text: '创建日期'})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>