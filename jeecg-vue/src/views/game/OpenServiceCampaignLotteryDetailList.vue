<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('开服夺宝详情')">导出</a-button>
      <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
      <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据"
                  :autoSize="{ minRows: 2, maxRows: 20 }"/>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a
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
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" alt="图片不存在" class="image"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载
          </a-button>
        </template>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-ontainer">
            <span class="large-text">{{ text || '--' }}</span>
          </div>
        </template>
        <span slot="timeSlot" slot-scope="text, record">
          <div v-if="record.timeType == 1">
            <a-tag color="blue">{{ record.startTime }}</a-tag>
            &nbsp;
            <a-tag color="blue">{{ record.endTime }}</a-tag>
          </div>
          <div v-if="record.timeType == 2">
            <a-tag color="green">开服第{{ record.startDay }}天</a-tag>
            &nbsp;
            <a-tag color="green">持续{{ record.duration }}天</a-tag>
          </div>
        </span>
        <template slot="action" slot-scope="text, record">
          <div class="action">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical"/>
            <a-dropdown>
              <a class="ant-dropdown-link"
              >更多
                <a-icon type="down"/>
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </div>
        </template>
      </a-table>
    </div>

    <open-service-campaign-lottery-detail-modal ref="modalForm"
                                                @ok="modalFormOk"></open-service-campaign-lottery-detail-modal>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {getAction, postAction} from '../../api/manage';
import {filterObj} from '@/utils/util';
import OpenServiceCampaignLotteryDetailModal from './modules/OpenServiceCampaignLotteryDetailModal';

export default {
  name: 'OpenServiceCampaignLotteryDetailList',
  mixins: [JeecgListMixin],
  components: {
    OpenServiceCampaignLotteryDetailModal
  },
  data() {
    return {
      description: '开服夺宝详情管理页面',
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
        //     title: "活动TypeId",
        //     align: "center",
        //     dataIndex: "campaignTypeId"
        // },
        {
          title: '活动名称',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '页签名称',
          align: 'center',
          dataIndex: 'tabName'
        },
        {
          title: '时间类型',
          align: 'center',
          width: 120,
          dataIndex: 'timeType',
          customRender: (value) => {
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
          scopedSlots: {customRender: 'timeSlot'}
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
          scopedSlots: {customRender: 'imgSlot'}
        },
        {
          title: '骨骼动画资源',
          align: 'center',
          dataIndex: 'skeleton'
        },
        {
          title: '免费抽奖次数',
          align: 'center',
          dataIndex: 'freeNum'
        },
        {
          title: '获奖记录显示数量',
          align: 'center',
          dataIndex: 'rewardRecordNum'
        },
        {
          title: '获奖记录内容',
          align: 'center',
          width: 180,
          dataIndex: 'rewardRecordMsg',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '获奖传闻内容',
          align: 'center',
          width: 180,
          dataIndex: 'rewardMsg',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '概率公示',
          align: 'center',
          width: 180,
          dataIndex: 'probabilityMsg',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '抽奖设置(单抽/多抽)',
          align: 'center',
          width: 180,
          dataIndex: 'lotteryType',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '展示特奖',
          align: 'center',
          width: 180,
          dataIndex: 'ssrShowReward',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '展示大奖',
          align: 'center',
          width: 180,
          dataIndex: 'srShowReward',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '展示奖励',
          align: 'center',
          width: 180,
          dataIndex: 'showReward',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '重置大奖',
          align: 'center',
          width: 120,
          dataIndex: 'resetReward',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '奖池',
          align: 'center',
          width: 180,
          dataIndex: 'rewardPool',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '排名奖励邮件标题',
          align: 'center',
          width: 180,
          dataIndex: 'rankRewardEmailTitle',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '排名奖励邮件内容',
          align: 'center',
          width: 180,
          dataIndex: 'rankRewardEmailContent',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '积分奖励邮件标题',
          align: 'center',
          width: 180,
          dataIndex: 'scoreRewardEmailTitle',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '积分奖励邮件内容',
          align: 'center',
          width: 180,
          dataIndex: 'scoreRewardEmailContent',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '帮助信息',
          align: 'center',
          width: 180,
          dataIndex: 'helpMsg',
          scopedSlots: {customRender: 'largeText'}
        },
        {
          title: '创建时间',
          align: 'center',
          width: 120,
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          align: 'center',
          fixed: 'right',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: 'game/openServiceCampaignLotteryDetail/list',
        delete: 'game/openServiceCampaignLotteryDetail/delete',
        deleteBatch: 'game/openServiceCampaignLotteryDetail/deleteBatch',
        exportXlsUrl: 'game/openServiceCampaignLotteryDetail/exportXls',
        importExcelUrl: 'game/openServiceCampaignLotteryDetail/importExcel',
        importTextUrl: 'game/openServiceCampaignLotteryDetail/importText'
      },
      dictOptions: {}
    };
  },
  computed: {},
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
      this.importText = '';
      this.loadData();
    },
    handleAdd() {
      this.$refs.modalForm.add({campaignTypeId: this.model.id, campaignId: this.model.campaignId});
      this.$refs.modalForm.title = '新增开服夺宝配置';
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
      // return "http://10.21.211.35:8888/jeecg-boot/image/20201222/jierihuodongdi6_105402.jpg";
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    },
    importExcelUrl() {
      let domainURL = window._CONFIG['domainURL'];
      return `${domainURL}/${this.url.importExcelUrl}`;
    },
    handleImportText() {
      let params = {
        id: this.model.id,
        text: this.importText
      };
      console.log(params);
      postAction(this.url.importTextUrl, params).then((res) => {
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

.large-text-ontainer {
  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
  height: 200px;
}

.large-text {
  white-space: normal;
  word-break: break-word;
}

.action {
  height: 200px;
  display: flex;
  align-items: center;
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
