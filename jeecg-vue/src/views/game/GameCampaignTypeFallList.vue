<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('节日掉落')">导出</a-button>
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
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div> -->

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

    <game-campaign-type-fall-modal ref="modalForm" @ok="modalFormOk"></game-campaign-type-fall-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction } from '../../api/manage';
import { filterObj } from '@/utils/util';
import GameCampaignTypeFallModal from './modules/GameCampaignTypeFallModal';

export default {
  name: 'GameCampaignTypeFallList',
  mixins: [JeecgListMixin],
  components: {
    GameCampaignTypeFallModal
  },
  data() {
    return {
      description: '节日掉落管理页面',
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
          dataIndex: 'campaignId'
        },
        {
          title: '页签id',
          align: 'center',
          dataIndex: 'typeId'
        },
        {
          title: '模块',
          align: 'center',
          dataIndex: 'module',
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '1-仙器秘境';
            } else if (value === 2) {
              text = '2-仙兽秘境';
            } else if (value === 3) {
              text = '3-丹药秘境';
            } else if (value === 4) {
              text = '4-修为秘境';
            } else if (value === 5) {
              text = '5-灵石秘境';
            } else if (value === 6) {
              text = '6-北冥魔海';
            } else if (value === 7) {
              text = '7-不死魔巢/特权BOSS';
            } else if (value === 8) {
              text = '8-蛇陵魔窟';
            } else if (value === 9) {
              text = '9-魔王入侵';
            } else if (value === 10) {
              text = '10-剧情挂机';
            } else if (value === 11) {
              text = '11-法宝秘境';
            } else if (value === 12) {
              text = '12-仙盟妖灵';
            }
            return text;
          }
        },
        {
          title: '奖励类型',
          align: 'center',
          dataIndex: 'rewardType',
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '1-按比例加成';
            } else if (value === 2) {
              text = '2-额外的活动掉落组';
            } else if (value === 3) {
              text = '3-剧情挂机奖励';
            }
            return text;
          }
        },
        {
          title: '奖励',
          align: 'center',
          dataIndex: 'reward'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameCampaignTypeFall/list',
        delete: 'game/gameCampaignTypeFall/delete',
        deleteBatch: 'game/gameCampaignTypeFall/deleteBatch',
        exportXlsUrl: 'game/gameCampaignTypeFall/exportXls',
        importExcelUrl: 'game/gameCampaignTypeFall/importExcel'
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
      this.$refs.modalForm.title = '新增节日掉落配置';
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
