<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="游戏编号">
              <j-search-select-tag placeholder="请选择游戏编号" v-model="queryParam.gameId" dict="game_info,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="Sdk渠道名">
              <j-input placeholder="请输入Sdk渠道名" v-model="queryParam.sdkChannel" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="版本号">
              <j-input placeholder="请输入版本号" v-model="queryParam.version" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="备注">
                <j-input placeholder="请输入备注" v-model="queryParam.remark" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="refreshConfig" type="primary" icon="sync">审核配置</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏渠道')">导出</a-button> -->
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
          <a v-if="record.status === 0" @click="changeStatus(record, 1)">开启审核</a>
          <a v-else @click="changeStatus(record, 0)">关闭审核</a>
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
        <span slot="switchSlot" slot-scope="text, record">
          <a-switch checked-children="开" un-checked-children="关" :checked="text === 1" />
        </span>
        <span slot="profileSlot" slot-scope="text, record">
          <a-tag v-if="!text" class="ant-tag-no-margin">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="sdkChannelTitle">Sdk渠道 <a-icon type="copy" /></span>
        <span slot="versionTitle">版本号 <a-icon type="copy" /></span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <game-review-modal ref="modalForm" @ok="modalFormOk"></game-review-modal>
  </a-card>
</template>

<script>
import { filterObj } from '@/utils/util';
import JInput from '@/components/jeecg/JInput';
import GameReviewModal from './modules/GameReviewModal';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction } from '@/api/manage';

function filterGameIdText(options, text) {
  if (options instanceof Array) {
    for (let game of options) {
      if (text === game.id) {
        return game.name + '(' + game.id + ')';
      }
    }
  }
  return text;
}

export default {
  name: 'GameReviewList',
  mixins: [JeecgListMixin],
  components: { JInput, GameReviewModal },
  data() {
    return {
      description: '游戏审核配置管理页面',
      gameList: [],
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
          title: '游戏编号',
          align: 'center',
          width: 120,
          dataIndex: 'gameId',
          customRender: (text) => {
            return filterGameIdText(this.gameList, text);
          }
        },
        {
          title: '名称',
          align: 'center',
          width: 100,
          dataIndex: 'name'
        },
        {
          // title: 'Sdk渠道',
          align: 'center',
          width: 100,
          dataIndex: 'sdkChannel',
          slots: { title: 'sdkChannelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '版本号',
          align: 'center',
          width: 100,
          dataIndex: 'version',
          slots: { title: 'versionTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '审核区服配置',
          align: 'center',
          width: 100,
          dataIndex: 'profile',
          scopedSlots: { customRender: 'profileSlot' }
        },
        {
          title: '审核开关',
          align: 'center',
          width: 80,
          dataIndex: 'status',
          scopedSlots: { customRender: 'switchSlot' }
        },
        {
          title: '备注',
          align: 'center',
          width: 120,
          dataIndex: 'remark'
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
        list: 'game/review/list',
        delete: 'game/review/delete',
        deleteBatch: 'game/review/deleteBatch',
        changeStatus: 'game/review/changeStatus',
        refreshConfig: 'game/info/refreshConfig',
        // 游戏列表
        gameInfoList: 'game/info/list'
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  created() {
    this.queryGameInfoList();
  },
  methods: {
    getQueryParams() {
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    queryGameInfoList() {
      let that = this;
      getAction(that.url.gameInfoList).then((res) => {
        if (res.success) {
          if (res.result instanceof Array) {
            this.gameList = res.result;
          } else if (res.result.records instanceof Array) {
            this.gameList = res.result.records;
          }
        } else {
          this.gameList = [];
        }
      });
    },
    changeStatus(record, value) {
      getAction(this.url.changeStatus, { id: record.id, status: value }).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.error(res.message);
        }
        this.loadData();
      });
    },
    refreshConfig() {
      // 开始刷新游戏配置
      getAction(this.url.refreshConfig).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.error(res.message);
        }
      });
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
