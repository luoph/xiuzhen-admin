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
            <a-form-item label="操作类型">
              <a-select placeholder="操作类型" v-model="queryParam.operation" initialValue="add">
                <a-select-option value="add">新增</a-select-option>
                <a-select-option value="update">更新</a-select-option>
                <a-select-option value="delete">删除</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="封禁功能">
              <a-select placeholder="请选择封禁功能" v-model="queryParam.type" initialValue="0">
                <a-select-option :value="1">登录</a-select-option>
                <a-select-option :value="2">聊天</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="封禁依据">
              <a-select placeholder="请选择封禁依据" v-model="queryParam.banKey" initialValue="playerId">
                <a-select-option value="playerId">玩家ID</a-select-option>
                <a-select-option value="ip">ip</a-select-option>
                <a-select-option value="deviceId">设备号</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="封禁值">
              <j-input placeholder="请输入封禁值模糊查询" v-model="queryParam.banValue" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="操作人员">
              <j-search-select-tag placeholder="请选择操作人员" v-model="queryParam.createBy" dict="sys_user,realname,username" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="封禁原因">
                <j-input placeholder="请输入封禁原因" v-model="queryParam.reason" />
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="封禁期限">
                <a-select placeholder="封禁期限" v-model="queryParam.isForever" initialValue="1">
                  <a-select-option :value="0">临时</a-select-option>
                  <a-select-option :value="1">永久</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <!-- <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('封禁记录')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
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
          <span class="scroll-span" @click="copyText(tag)">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-container">
            <span @click="copyText(text)" class="large-text">{{ text || '--' }}</span>
          </div>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
        </span>
        <span slot="serverIdTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="banValueTitle" class="copy-text">封禁值 <a-icon type="copy" /></span>
        <span slot="reasonTitle" class="copy-text">封禁原因 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <!-- <game-forbidden-record-modal ref="modalForm" @ok="modalFormOk"></game-forbidden-record-modal> -->
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameForbiddenRecordModal from './modules/GameForbiddenRecordModal';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameForbiddenRecordList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameForbiddenRecordModal
  },
  data() {
    return {
      description: '封禁记录管理页面',
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
          title: '操作类型',
          align: 'center',
          dataIndex: 'operation',
          customRender: (value) => {
            let text = '--';
            if (value == 'add') {
              text = '新增';
            } else if (value == 'update') {
              text = '更新';
            } else if (value == 'delete') {
              text = '删除';
            }
            return text;
          }
        },
        {
          title: '封禁id',
          align: 'center',
          dataIndex: 'forbiddenId'
        },
        {
          // title: '区服ID',
          align: 'center',
          width: 80,
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          title: '封禁功能',
          align: 'center',
          dataIndex: 'type',
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '登录';
            } else if (value === 2) {
              text = '聊天';
            }
            return text;
          }
        },
        {
          title: '封禁依据',
          align: 'center',
          dataIndex: 'banKey',
          customRender: (value) => {
            let text = '--';
            if (value == 'ip') {
              text = 'IP';
            } else if (value == 'playerId') {
              text = '玩家ID';
            } else if (value == 'deviceId') {
              text = '设备号';
            }
            return text;
          }
        },
        {
          // title: '封禁值',
          align: 'center',
          dataIndex: 'banValue',
          slots: { title: 'banValueTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '封禁期限',
          align: 'center',
          dataIndex: 'isForever',
          customRender: (value) => {
            let text = '--';
            if (value === 0) {
              text = '临时';
            } else if (value === 1) {
              text = '永久';
            }
            return text;
          }
        },
        {
          // title: '封禁原因',
          width: 280,
          dataIndex: 'reason',
          slots: { title: 'reasonTitle' },
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'startTime'
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endTime'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '操作人',
          align: 'center',
          dataIndex: 'createBy'
        }
        // {
        //     title: "操作",
        //     dataIndex: "action",
        //     align: "center",
        //     scopedSlots: { customRender: "action" }
        // }
      ],
      url: {
        list: 'game/forbiddenRecord/list',
        delete: 'game/forbiddenRecord/delete',
        deleteBatch: 'game/forbiddenRecord/deleteBatch',
        exportXlsUrl: 'game/forbiddenRecord/exportXls',
        importExcelUrl: 'game/forbiddenRecord/importExcel'
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
    initDictConfig() {}
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.large-text-container {
  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
  max-height: 200px;
}

.large-text {
  text-align: left;
  white-space: normal;
  word-break: break-word;
}
</style>
