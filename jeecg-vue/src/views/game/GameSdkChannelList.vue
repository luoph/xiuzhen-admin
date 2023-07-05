<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="渠道">
              <j-search-select-tag placeholder="请选择渠道" v-model="queryParam.channel" dict="game_channel,name,simple_name" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="Sdk渠道">
              <a-input placeholder="请输入Sdk渠道" v-model="queryParam.sdkChannel" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="上线时间">
              <a-range-picker v-model="queryParam.onlineTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus"> </template>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
              <!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
              <!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
              <!--              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleSync" type="primary" icon="sync">同步Sdk渠道</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('SDK渠道信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>
            :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" -->

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
        <span slot="splitTags" slot-scope="text, record">
          <a-tag v-if="!text" class="ant-tag-no-margin">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="sdkChannelTitle">Sdk渠道 <a-icon type="copy" /></span>
        <span slot="channelTitle">父渠道 <a-icon type="copy" /></span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <game-sdk-channel-modal ref="modalForm" @ok="modalFormOk"></game-sdk-channel-modal>
  </a-card>
</template>

<script>
import { filterObj } from '@/utils/util';
import JInput from '@/components/jeecg/JInput';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameSdkChannelModal from '@views/game/modules/GameSdkChannelModal.vue';

export default {
  name: 'GameSdkChannelList',
  mixins: [JeecgListMixin],
  components: { JInput, GameSdkChannelModal },
  data() {
    return {
      description: '游戏Sdk渠道管理页面',
      gameList: [],
      isorter: {
        column: 'onlineTime',
        order: 'desc'
      },
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
          title: '名称',
          align: 'center',
          dataIndex: 'name',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          // title: 'Sdk渠道',
          align: 'center',
          dataIndex: 'sdkChannel',
          slots: { title: 'sdkChannelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '父渠道',
          align: 'center',
          dataIndex: 'channel',
          slots: { title: 'channelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '上线时间',
          align: 'center',
          width: 240,
          dataIndex: 'onlineTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark',
          customRender: (value) => {
            return value || '--';
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
        list: 'game/sdkChannel/list',
        sync: 'game/sdkChannel/sync',
        delete: 'game/sdkChannel/delete',
        deleteBatch: 'game/sdkChannel/deleteBatch'
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  created() {},
  methods: {
    getQueryParams() {
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;

      // 范围参数不传递后台
      delete param.onlineTimeRange;
      return filterObj(param);
    },
    onDateChange: function (value, dateString) {
      // console.log(dateString[0], dateString[1]);
      this.queryParam.onlineTime_begin = dateString[0];
      this.queryParam.onlineTime_end = dateString[1];
    },
    handleSync() {
      this.handleConfrimRequest(this.url.sync, {}, '是否同步Sdk渠道信息？', '点击确定同步');
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

.ant-tag-no-margin {
  margin-right: auto !important;
}
</style>
