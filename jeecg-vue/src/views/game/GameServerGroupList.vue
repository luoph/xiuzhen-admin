<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="跨服ID">
              <a-input placeholder="请输入跨服ID" v-model="queryParam.id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="区服ID">
              <a-input placeholder="请输入区服ID" v-model="queryParam.serverId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="IP">
              <j-search-select-tag placeholder="请选择IP" v-model="queryParam.host" dict="game_vps,hostname,ip" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="跨服地址">
              <a-input placeholder="请输入跨服地址" v-model="queryParam.crossServerUrl" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="GM地址">
              <a-input placeholder="请输入GM地址" v-model="queryParam.gmUrl" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="跨服结算时间">
                <a-range-picker v-model="queryParam.crossSettleTimeTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onSettleTimeChange" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('跨服分组')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
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
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
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
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="blueTags" slot-scope="text">
          <a-tag v-if="!text" class="ant-tag">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue" class="ant-tag">{{ tag }}</a-tag>
        </span>
        <span slot="greenTags" slot-scope="text">
          <a-tag v-if="!text" class="ant-tag">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="green" class="ant-tag">{{ tag }}</a-tag>
        </span>
        <span slot="idTitle">跨服ID <a-icon type="copy" /></span>
        <span slot="serverIdTitle">区服ID <a-icon type="copy" /></span>
        <span slot="hostTitle">IP <a-icon type="copy" /></span>
        <span slot="gmUrlTitle">GM地址 <a-icon type="copy" /></span>
        <span slot="crossServerUrlTitle">跨服地址 <a-icon type="copy" /></span>
        <span slot="chatServerUrlTitle">聊天服地址 <a-icon type="copy" /></span>
        <span slot="nameTitle">名称 <a-icon type="copy" /></span>
        <span slot="hostnameTitle">主机名 <a-icon type="copy" /></span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <!-- 表单区域 -->
    <game-server-group-modal ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JInput from '@/components/jeecg/JInput';
import GameServerGroupModal from '@views/game/modules/GameServerGroupModal.vue';
import { filterObj } from '@/utils/util';

export default {
  name: 'GameServerGroupList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameServerGroupModal
  },
  data() {
    return {
      description: '跨服分组列表页面',
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
          // title: 'id',
          align: 'center',
          dataIndex: 'id',
          slots: { title: 'idTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '区服ID',
          align: 'center',
          width: 160,
          dataIndex: 'serverIds',
          scopedSlots: { customRender: 'blueTags' }
        },
        {
          title: 'IP',
          align: 'center',
          dataIndex: 'host',
          customRender: function (text, record) {
            return `${text}（${record.hostname}）`;
          }
        },
        {
          // title: 'GM地址',
          align: 'left',
          dataIndex: 'gmUrl',
          slots: { title: 'gmUrlTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '跨服地址',
          align: 'left',
          dataIndex: 'crossServerUrl',
          slots: { title: 'crossServerUrlTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '聊天服地址',
          align: 'left',
          dataIndex: 'chatServerUrl',
          slots: { title: 'chatServerUrlTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '区服数',
          align: 'center',
          dataIndex: 'serverNum'
        },
        {
          title: '在线数',
          align: 'center',
          dataIndex: 'onlineNum'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/group/list',
        delete: 'game/group/delete',
        deleteBatch: 'game/group/deleteBatch'
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    getQueryParams() {
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;

      // 范围参数不传递后台
      delete param.createTimeRange;
      delete param.crossSettleTimeRange;
      return filterObj(param);
    },
    onCreateDateChange: function (value, dateString) {
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
    onSettleTimeChange: function (value, dateString) {
      this.queryParam.crossSettleTime_begin = dateString[0];
      this.queryParam.crossSettleTime_end = dateString[1];
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.copy-text {
  white-space: nowrap;
  color: rgba(0, 0, 0, 0.65);
}

.ant-divider-horizontal {
  margin: 6px 0 6px 0;
  padding: 0 10px 0 10px;
}

.ant-tag {
  margin-left: 4px;
  margin-right: 4px;
}

.ant-tag-no-margin {
  margin-right: auto !important;
}
</style>
