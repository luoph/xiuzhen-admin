<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="20">
          <a-col :md="6" :sm="10">
            <a-form-item label="标题">
              <a-input placeholder="请输入标题" class="query-group-cust" v-model="queryParam.title" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="10">
            <a-form-item label="状态">
              <a-select placeholder="请选择状态" v-model="queryParam.status" initialValue="0">
                <a-select-option :value="0">关闭</a-select-option>
                <a-select-option :value="1">开启</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="12" :sm="8">
              <a-form-item label="开始时间">
                <a-range-picker v-model="queryParam.startTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onStartTimeChange" />
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
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
      <a-button type="primary" icon="download" @click="handleExportXls('更新公告')">导出</a-button>
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
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <span slot="tagSlot" slot-scope="text" class="tag-container">
          <a-tag v-if="!text">未设置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleCopy(record)">复制</a>
          <a-divider type="vertical" />
          <a @click="resumeJob(record)" v-if="record.status === 1">已启动</a>
          <a @click="resumeJob(record)" v-else>已关闭</a>
          <a-divider type="vertical" />
          <!--                    <a-dropdown>-->
          <!--                        <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
          <!--                        <a-menu slot="overlay">-->
          <!--                            <a-menu-item>-->
          <!--                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                                    <a>删除</a>-->
          <!--                                </a-popconfirm>-->
          <!--                            </a-menu-item>-->
          <!--                        </a-menu>-->
          <!--                    </a-dropdown>-->
          <a @click="handleSyncCampaign(record)">同步到区服</a>
        </span>
      </a-table>
    </div>

    <gameUpgradeNotice-modal ref="modalForm" @ok="modalFormOk"></gameUpgradeNotice-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameUpgradeNoticeModal from './modules/GameUpgradeNoticeModal';
import JDate from '@/components/jeecg/JDate.vue';
import { getAction } from '@api/manage';

export default {
  name: 'GameUpgradeNoticeList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameUpgradeNoticeModal
  },
  data() {
    return {
      description: '更新公告管理页面',
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
          title: '标题',
          align: 'left',
          dataIndex: 'title'
        },
        {
          title: '奖励',
          align: 'left',
          dataIndex: 'reward'
        },
        {
          title: '服务器',
          align: 'center',
          dataIndex: 'serverIds',
          scopedSlots: { customRender: 'tagSlot' }
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
          title: '更新时间',
          align: 'center',
          dataIndex: 'updateTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameUpgradeNotice/list',
        delete: 'game/gameUpgradeNotice/delete',
        deleteBatch: 'game/gameUpgradeNotice/deleteBatch',
        exportXlsUrl: 'game/gameUpgradeNotice/exportXls',
        importExcelUrl: 'game/gameUpgradeNotice/importExcel',
        openOrClose: 'game/gameUpgradeNotice/openOrClose',
        severSync: 'game/gameUpgradeNotice/serverSync'
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

    handleSyncCampaign: function (record) {
      const that = this;
      that.confirmLoading = true;
      getAction(that.url.severSync, { id: record.id })
        .then((res) => {
          if (res.success) {
            that.$message.success('同步成功');
          } else {
            that.$message.error(res.message);
          }
        })
        .finally(() => {
          that.confirmLoading = false;
        });
    },

    resumeJob: function (record) {
      const that = this;
      let res = {
        title: '确认启动',
        content: '是否启动选中消息?'
      };
      if (record.status === 1) {
        res = {
          title: '确认关闭',
          content: '是否关闭选中消息?'
        };
      }
      this.$confirm({
        title: res.title,
        content: res.content,
        onOk: function () {
          getAction(that.url.openOrClose, { id: record.id }).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.loadData();
              that.onClearSelected();
            } else {
              that.$message.warning(res.message);
            }
          });
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
