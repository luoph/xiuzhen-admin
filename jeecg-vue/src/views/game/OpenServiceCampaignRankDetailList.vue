<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('开服活动类型配置')">导出</a-button>
      <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
      </a-dropdown>
      <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据"></a-textarea>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
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
        @change="handleTableChange"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" alt="图片不存在" class="image" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-ontainer">
            <span class="large-text">{{ text }}</span>
          </div>
        </template>
        <span slot="timeSlot" slot-scope="text, record">
          <div v-if="record.timeType == 1">
            <a-tag color="blue">{{ record.startTime }}</a-tag>
            <a-tag color="blue">{{ record.endTime }}</a-tag>
          </div>
          <div v-if="record.timeType == 2">
            <a-tag color="green">开服第{{ record.startDay }}天</a-tag>
            <a-tag color="green">持续{{ record.duration }}天</a-tag>
          </div>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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

    <open-service-campaign-rank-detail-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-rank-detail-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction, postAction } from '../../api/manage';
import { filterObj } from '@/utils/util';
import OpenServiceCampaignRankDetailModal from './modules/OpenServiceCampaignRankDetailModal';

export default {
  name: 'OpenServiceCampaignRankDetailList',
  mixins: [JeecgListMixin],
  components: {
    OpenServiceCampaignRankDetailModal
  },
  data() {
    return {
      description: '开服活动-开服排行-活动明细(3级)管理页面',
      model: {},
      importText: '',
      // 表头
      columns: [
        // {
        //     title: "#",
        //     dataIndex: "",
        //     key: "rowIndex",
        //     width: 60,
        //     align: "center",
        //     customRender: function(t, r, index) {
        //         return parseInt(index) + 1;
        //     }
        // },
        {
          title: 'id',
          align: 'center',
          width: 60,
          dataIndex: 'id'
        },
        // {
        //     title: "开服活动id",
        //     align: "center",
        //     dataIndex: "campaignId"
        // },
        // {
        //     title: "页签id",
        //     align: "center",
        //     dataIndex: "campaignTypeId"
        // },
        {
          title: '活动名称',
          align: 'center',
          width: 100,
          dataIndex: 'name'
        },
        {
          title: '页签名称',
          align: 'center',
          width: 100,
          dataIndex: 'tabName'
        },
        {
          title: '排序',
          align: 'center',
          width: 80,
          dataIndex: 'sort'
        },
        {
          title: '排行类型',
          align: 'center',
          width: 100,
          dataIndex: 'rankType',
          customRender: value => {
            let text = '--';
            if (value === 1) {
              text = '1-境界排行';
            } else if (value === 2) {
              text = '2-仙兽排行';
            } else if (value === 3) {
              text = '3-义戒排行';
            } else if (value === 4) {
              text = '4-飞剑排行';
            } else if (value === 5) {
              text = '5-天书排行';
            } else if (value === 6) {
              text = '6-圣灵排行';
            } else if (value === 7) {
              text = '7-法宝排行';
            } else if (value === 8) {
              text = '8-情饰排行';
            } else {
              text = '大于8-过期类型';
            }
            return text;
          }
        },
        {
          title: '时间类型',
          align: 'center',
          width: 120,
          dataIndex: 'timeType',
          customRender: value => {
            let text = '--';
            if (value === 1) {
              text = '1-时间范围';
            } else if (value === 2) {
              text = '2-开服第N天';
            }
            return text;
          }
        },
        {
          title: '活动时间',
          align: 'center',
          width: 80,
          dataIndex: 'startDay',
          scopedSlots: { customRender: 'timeSlot' }
        },
        // {
        //     title: "开始天数",
        //     align: "center",
        //     width: 80,
        //     dataIndex: "startDay"
        // },
        // {
        //     title: "持续天数",
        //     align: "center",
        //     width: 80,
        //     dataIndex: "duration"
        // },
        // {
        //     title: "活动开始时间",
        //     align: "center",
        //     width: 120,
        //     dataIndex: "startTime"
        // },
        // {
        //     title: "活动结束时间",
        //     align: "center",
        //     width: 120,
        //     dataIndex: "endTime"
        // },
        {
          title: '活动宣传图',
          align: 'center',
          dataIndex: 'banner',
          width: 160,
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '奖励图',
          align: 'center',
          dataIndex: 'rewardImg',
          width: 50,
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '活动宣传仙力',
          align: 'center',
          width: 100,
          dataIndex: 'combatPower'
        },
        // {
        //     title: "排行玩家数量",
        //     align: "center",
        //     dataIndex: "rankNum"
        // },
        {
          title: '排名奖励邮件id',
          align: 'center',
          width: 80,
          dataIndex: 'rankRewardEmail'
        },
        {
          title: '达标奖励邮件id',
          align: 'center',
          width: 80,
          dataIndex: 'standardRewardEmail'
        },
        {
          title: '跳转id',
          align: 'center',
          width: 80,
          dataIndex: 'jump'
        },
        {
          title: '帮助信息',
          align: 'center',
          width: 180,
          dataIndex: 'helpMsg',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '创建时间',
          align: 'center',
          width: 120,
          dataIndex: 'createTime'
        },
        // {
        //     title: "更新时间",
        //     align: "center",
        //     dataIndex: "updateTime"
        // },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/openServiceCampaignRankDetail/list',
        delete: 'game/openServiceCampaignRankDetail/delete',
        deleteBatch: 'game/openServiceCampaignRankDetail/deleteBatch',
        exportXlsUrl: 'game/openServiceCampaignRankDetail/exportXls',
        importExcelUrl: 'game/openServiceCampaignRankDetail/importExcel',
        importTextUrl: 'game/openServiceCampaignRankDetail/importText'
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
    initDictConfig() {},
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
      getAction(this.url.list, params).then(res => {
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
      this.importText = '';
      this.loadData();
    },
    handleAdd() {
      this.$refs.modalForm.add({ campaignTypeId: this.model.id, campaignId: this.model.campaignId });
      this.$refs.modalForm.title = '新增开服排行配置';
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // typeId、活动id
      param.campaignId = this.model.campaignId;
      param.campaignTypeId = this.model.id;
      return filterObj(param);
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domianURL']}/${text}`;
    },
    importExcelUrl() {
      let domianURL = window._CONFIG['domianURL'];
      return `${domianURL}/${this.url.importExcelUrl}`;
    },
    handleImportText() {
      let params = {
        id: this.model.id,
        text: this.importText
      };
      console.log(params);
      postAction(this.url.importTextUrl, params).then(res => {
        if (res.success) {
          this.$message.success(res.message);
          this.loadData();
        } else {
          this.$message.warning(res.message);
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.image {
  width: 100%;
  height: 100px;
  object-fit: scale-down;
}

.large-text-ontainer {
  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
  max-height: 200px;
}

.large-text {
  white-space: normal;
  word-break: break-word;
}

/** Button按钮间距 */
.ant-btn {
  margin-right: 15px;
}

.import-text {
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
