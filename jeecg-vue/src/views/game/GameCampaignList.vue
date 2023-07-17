<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="活动展示名称">
              <j-input placeholder="活动展示名称" v-model="queryParam.showName" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="时间类型">
              <a-select placeholder="请选择时间类型" v-model="queryParam.timeType" initialValue="1">
                <a-select-option :value="1">1-时间范围</a-select-option>
                <a-select-option :value="2">2-开服第N天</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="自动开启">
              <a-select placeholder="请选择自动开启" v-model="queryParam.autoOpen" initialValue="1">
                <a-select-option :value="1">开启</a-select-option>
                <a-select-option :value="0">关闭</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="活动状态">
              <a-select placeholder="请选择状态" v-model="queryParam.status" initialValue="1">
                <a-select-option :value="1">有效</a-select-option>
                <a-select-option :value="0">无效</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="8" :sm="8">
              <a-form-item label="活动开始时间">
                <a-range-picker v-model="queryParam.startTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onStartTimeChange" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="活动结束时间">
                <a-range-picker v-model="queryParam.endTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onEndTimeChange" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
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
      <a-button type="primary" icon="download" @click="handleExportXls('活动配置')">导出</a-button>
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
          <img v-else :src="getImgView(text)" alt="图片不存在" class="image" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <span slot="statusSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
        </span>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <span slot="largeTextSlot" slot-scope="text" @click="copyText(text)" class="large-text-container">
          {{ text || '--' }}
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="channelSlot" slot-scope="text" class="channel-container">
          <a-tag v-if="!text">未设置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
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
          <a @click="handleEdit(record)">活动信息</a><br />
          <a @click="handleServerList(record)">活动状态</a><br />
          <a @click="handleDuplicate(record)">复制</a><br />
          <a @click="handleSyncCampaign(record)">同步到区服</a><br />
          <a @click="removeCompletedServer(record)">移除已结束区服</a>
          <!-- <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown> -->
        </span>
      </a-table>
    </div>

    <game-campaign-modal ref="modalForm" @ok="modalFormOk"></game-campaign-modal>
    <game-campaign-server-list ref="serverListModal"></game-campaign-server-list>
  </a-card>
</template>

<script>
import JInput from '@/components/jeecg/JInput';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction } from '@/api/manage';
import { filterObj } from '@/utils/util';
import GameCampaignModal from './modules/GameCampaignModal';
import GameCampaignServerList from './modules/GameCampaignServerList';
import GameCampaignTabList from './GameCampaignTabList';
import JDate from '@/components/jeecg/JDate.vue';

export default {
  name: 'GameCampaignList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    JInput,
    GameCampaignModal,
    GameCampaignServerList,
    GameCampaignTabList
  },
  data() {
    return {
      description: '活动信息',
      isorter: {
        column: 'id',
        order: 'desc'
      },
      // 表头
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key: 'rowIndex',
        //   width: 60,
        //   align: 'center',
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1;
        //   }
        // },
        {
          title: '主活动id',
          align: 'center',
          dataIndex: 'id'
        },
        {
          title: '活动名称',
          align: 'center',
          dataIndex: 'name',
          scopedSlots: { customRender: 'largeTextSlot' }
        },
        {
          title: '活动标语（描述）',
          align: 'left',
          dataIndex: 'description',
          scopedSlots: { customRender: 'largeTextSlot' }
        },
        {
          title: '活动图标',
          width: 80,
          align: 'center',
          dataIndex: 'icon',
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '活动宣传图',
          width: 240,
          align: 'center',
          dataIndex: 'banner',
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '活动状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'statusSlot' }
        },
        {
          title: '优先级',
          align: 'center',
          dataIndex: 'priority'
        },
        {
          title: '区服ID',
          align: 'left',
          dataIndex: 'serverIds',
          scopedSlots: { customRender: 'serverIdsSlot' }
        },
        {
          title: '自动添加新服渠道',
          align: 'center',
          dataIndex: 'autoAddServerChannels',
          scopedSlots: { customRender: 'channelSlot' }
        },
        {
          title: 'Sdk渠道',
          align: 'center',
          width: 100,
          dataIndex: 'sdkChannels',
          scopedSlots: { customRender: 'channelSlot' }
        },
        // {
        //     title: "自动开启",
        //     align: "center",
        //     dataIndex: "autoOpen",
        //     width: 80,
        //     customRender: value => {
        //         let re = "--";
        //         if (value === 0) {
        //             re = "关闭";
        //         } else if (value === 1) {
        //             re = "开启";
        //         }
        //         return re;
        //     }
        // },
        {
          title: '时间类型',
          align: 'center',
          dataIndex: 'timeType',
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '时间范围[1]';
            } else if (value === 2) {
              text = '开服第N天[2]';
            }
            return text;
          }
        },
        {
          title: '活动时间',
          align: 'center',
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
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 130,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameCampaign/list',
        delete: 'game/gameCampaign/delete',
        sync: 'game/gameCampaign/sync',
        duplicate: 'game/gameCampaign/duplicate',
        deleteBatch: 'game/gameCampaign/deleteBatch',
        exportXlsUrl: 'game/gameCampaign/exportXls',
        importExcelUrl: 'game/gameCampaign/importExcel',
        removeCompletedServerUrl: 'game/gameCampaign/removeCompletedServer'
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
    },
    handleEdit: function (record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = '活动信息';
      this.$refs.modalForm.disableSubmit = false;
    },
    handleServerList: function (record) {
      this.$refs.serverListModal.edit(record);
      this.$refs.serverListModal.title = '活动信息';
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    },
    handleSyncCampaign: function (record) {
      const that = this;
      that.loading = true;
      getAction(that.url.sync, { id: record.id })
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
          } else {
            that.$message.error(res.message);
          }
        })
        .finally(() => {
          that.loading = false;
        });
    },
    handleDuplicate: function (record) {
      const that = this;
      that.loading = true;
      getAction(that.url.duplicate, { id: record.id })
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
          } else {
            that.$message.error(res.message);
          }
        })
        .finally(() => {
          that.loading = false;
          that.loadData();
        });
    },
    removeCompletedServer: function (record) {
      const that = this;
      that.loading = true;
      getAction(that.url.removeCompletedServerUrl, { id: record.id })
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
          } else {
            that.$message.error(res.message);
          }
        })
        .finally(() => {
          that.loading = false;
          that.searchQuery();
        });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.channel-container {
  width: auto;
  height: auto;
  display: block;
  min-width: 80px;
  max-width: 200px;
}

.large-text-container {
  display: block;
  min-width: 80px;
  max-width: 280px;
  overflow-y: auto;
  white-space: normal;
  word-break: break-word;
}
</style>
