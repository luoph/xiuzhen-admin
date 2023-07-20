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
            <a-form-item label="封禁值">
              <a-input placeholder="请输入封禁值" v-model="queryParam.banValue" />
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
            <a-form-item label="封禁期限">
              <a-select placeholder="封禁期限" v-model="queryParam.isForever" initialValue="0">
                <a-select-option :value="0">临时</a-select-option>
                <a-select-option :value="1">永久</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="操作人员">
              <j-search-select-tag placeholder="请选择操作人员" v-model="queryParam.createBy" dict="sys_user,realname,username" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="开始时间">
                <a-range-picker v-model="queryParam.startTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onStartTimeChange" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="结束时间">
                <a-range-picker v-model="queryParam.endTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onEndTimeChange" />
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
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('封禁列表')">导出</a-button>
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
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-container">
            <span @click="copyText(text)" class="large-text">{{ text || '--' }}</span>
          </div>
        </template>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span" @click="copyText(tag)">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleCopy(record)">复制</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <span slot="serverIdTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="banValueTitle" class="copy-text">封禁值 <a-icon type="copy" /></span>
        <span slot="reasonTitle" class="copy-text">封禁原因 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <gameForbidden-modal ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameForbiddenModal from './modules/GameForbiddenModal';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import { getAction } from '@/api/manage';
import { filterObj } from '@/utils/util';

export default {
  name: 'GameForbiddenList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameForbiddenModal,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      description: '封号禁言管理页面',
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
          width: 80,
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          title: '封禁功能',
          align: 'center',
          width: 80,
          dataIndex: 'type',
          customRender: (value) => {
            let text;
            if (value === 1) {
              text = '登录';
            } else if (value === 2) {
              text = '聊天';
            }
            return text || '--';
          }
        },
        {
          title: '封禁依据',
          align: 'center',
          width: 100,
          dataIndex: 'banKey',
          customRender: (value) => {
            let text = '--';
            if (value === 'ip') {
              text = 'ip地址';
            } else if (value === 'playerId') {
              text = '玩家ID';
            } else if (value === 'deviceId') {
              text = '设备id';
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
          width: 80,
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
          align: 'center',
          width: 240,
          dataIndex: 'reason',
          slots: { title: 'reasonTitle' },
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'startTime',
          customRender: (text) => {
            return text || '--';
          }
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endTime',
          customRender: (text) => {
            return text || '--';
          }
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
          customRender: (text) => {
            return text || '--';
          }
        },
        {
          title: '更新时间',
          align: 'center',
          dataIndex: 'updateTime',
          customRender: (text) => {
            return text || '--';
          }
        },
        {
          title: '操作人',
          align: 'center',
          width: 100,
          dataIndex: 'createBy',
          customRender: (text) => {
            return text || '--';
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 200,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/forbidden/list',
        delete: 'game/forbidden/delete',
        deleteBatch: 'game/forbidden/deleteBatch',
        exportXlsUrl: 'game/forbidden/exportXls',
        importExcelUrl: 'game/forbidden/importExcel'
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
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    getQueryParams() {
      console.log(this.queryParam.createTimeRange);
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.startTimeRange;
      delete param.endTimeRange;
      return filterObj(param);
    },
    onStartTimeChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.startTime_begin = dateString[0];
      this.queryParam.startTime_end = dateString[1];
    },
    onEndTimeChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.endTime_begin = dateString[0];
      this.queryParam.endTime_end = dateString[1];
    }
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
