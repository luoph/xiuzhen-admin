<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('任务活动')">导出</a-button> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" alt="图片不存在" class="list-image" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-container">
            <span class="large-text">{{ text }}</span>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
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
      </a-table>
    </div>

    <game-campaign-type-task-modal ref="modalForm" @ok="modalFormOk"></game-campaign-type-task-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction } from '../../api/manage';
import { filterObj } from '@/utils/util';
import GameCampaignTypeTaskModal from './modules/GameCampaignTypeTaskModal';

export default {
  name: 'GameCampaignTypeTaskList',
  mixins: [JeecgListMixin],
  components: {
    GameCampaignTypeTaskModal
  },
  data() {
    return {
      description: '任务活动管理页面',
      model: {},
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
          title: '活动id',
          align: 'center',
          width: 80,
          dataIndex: 'campaignId'
        },
        {
          title: '页签id',
          align: 'center',
          width: 80,
          dataIndex: 'typeId'
        },
        {
          title: '任务id',
          align: 'center',
          width: 80,
          dataIndex: 'taskId'
        },
        {
          title: '描述',
          align: 'center',
          width: 120,
          dataIndex: 'description',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '模块id',
          align: 'center',
          width: 80,
          dataIndex: 'moduleId'
        },
        {
          title: '任务完成条件',
          align: 'center',
          width: 80,
          dataIndex: 'target'
        },
        {
          title: '任务参数',
          align: 'center',
          width: 80,

          dataIndex: 'args'
        },
        {
          title: '奖励列表',
          align: 'center',
          width: 240,
          dataIndex: 'reward',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '跳转id',
          align: 'center',
          dataIndex: 'jumpId'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameCampaignTypeTask/list',
        delete: 'game/gameCampaignTypeTask/delete',
        deleteBatch: 'game/gameCampaignTypeTask/deleteBatch',
        exportXlsUrl: 'game/gameCampaignTypeTask/exportXls',
        importExcelUrl: 'game/gameCampaignTypeTask/importExcel'
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
    loadData(arg) {
      if (!this.model.id) {
        return;
      }

      if (!this.url.list) {
        this.$message.error('请设置url.list属性!');
        return;
      }

      // 加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }

      // 查询条件
      var params = this.getQueryParams();
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success && res.result && res.result.records) {
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
        }
        if (res.code === 510) {
          this.$message.warning(res.message);
        }
        this.loading = false;
      });
    },
    edit(record) {
      this.model = record;
      this.loadData();
    },
    handleAdd() {
      this.$refs.modalForm.add({ typeId: this.model.id, campaignId: this.model.campaignId });
      this.$refs.modalForm.title = '新增任务活动配置';
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // typeId、活动id
      param.typeId = this.model.id;
      param.campaignId = this.model.campaignId;
      return filterObj(param);
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.list-image {
  width: 100%;
  height: 100px;
  object-fit: scale-down;
}

.large-text-container {
  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
  max-height: 200px;
}

.large-text {
  white-space: normal;
  word-break: break-word;
}
</style>
