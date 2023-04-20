<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="公告类型">
              <j-dict-select-tag v-model="queryParam.noticeType" placeholder="请选择公告类型" dictCode="notice_type"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="公告ID">
              <a-input placeholder="请输入ID" v-model="queryParam.id"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="标题">
              <j-input placeholder="请输入标题" v-model="queryParam.title"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="开始时间">
                <a-input placeholder="请输入开始时间" v-model="queryParam.beginTime"/>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="结束时间">
                <a-input placeholder="请输入结束时间" v-model="queryParam.endTime"/>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="公告内容">
                <a-input placeholder="请输入公告内容" v-model="queryParam.content"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-popconfirm title="刷新公告配置列表" @confirm="updateNoticeConfig()">
        <a-button type="primary" icon="sync">公告配置</a-button>
      </a-popconfirm>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏公告')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text" class="noticeContent"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="100px" alt="图片不存在"
               style="max-width: 280px; font-size: 12px; font-style: italic"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载
          </a-button>
        </template>
        <span slot="statusSlot" slot-scope="text, record">
          <a-tag v-if="0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a @click="handleCopy(record)">复制</a>
          <a-divider type="vertical"/>
          <a @click="handlePreview(record)">公告预览</a>
          <a-divider type="vertical"/>
          <a @click="refreshNotice(record)">刷新公告</a>
          <a-divider type="vertical"/>
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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <game-notice-modal ref="modalForm" @ok="modalFormOk"/>
    <!-- html预览 -->
    <game-html-preview-modal ref="htmlModal" @ok="modalFormOk"/>
  </a-card>
</template>

<script>
import JInput from '@/components/jeecg/JInput';
import {getAction} from '@/api/manage';
import GameHtmlPreviewModal from './modules/GameHtmlPreviewModal';

import GameNoticeModal from './modules/GameNoticeModal';
import {JeecgListMixin} from '@/mixins/JeecgListMixin';

export default {
  name: 'GameNoticeList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameNoticeModal,
    GameHtmlPreviewModal
  },
  data() {
    return {
      description: '游戏公告管理页面',
      isorter: {
        column: 'id',
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
          title: '公告ID',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '公告类型',
          align: 'center',
          width: 80,
          dataIndex: 'noticeType_dictText'
        },
        {
          title: '标题',
          align: 'left',
          width: 180,
          dataIndex: 'title'
        },
        {
          title: '公告内容',
          align: 'left',
          dataIndex: 'content',
          scopedSlots: {customRender: 'htmlSlot'}
        },
        {
          title: '开始时间',
          align: 'center',
          width: 180,
          dataIndex: 'beginTime'
        },
        {
          title: '结束时间',
          align: 'center',
          width: 180,
          dataIndex: 'endTime'
        },
        {
          title: '状态',
          align: 'center',
          width: 80,
          dataIndex: 'status',
          scopedSlots: {customRender: 'statusSlot'}
        },
        {
          title: '滚动公告间隔(秒)',
          align: 'center',
          width: 80,
          dataIndex: 'intervalSeconds'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 140,
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: 'game/gameNotice/list',
        delete: 'game/gameNotice/delete',
        deleteBatch: 'game/gameNotice/deleteBatch',
        updateNoticeConfigUrl: 'game/gameNotice/updateNoticeConfig',
        // 刷新渠道公告
        noticeRefresh: 'game/gameNotice/refreshById'
        // exportXlsUrl: "game/gameNotice/exportXls",
        // importExcelUrl: "game/gameNotice/importExcel"
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    updateNoticeConfig() {
      // 刷新公告配置
      getAction(this.url.updateNoticeConfigUrl).then((res) => {
        if (res.success) {
          this.$message.success('刷新成功');
        } else {
          this.$message.error('刷新失败');
        }
        console.log('刷新公告配置完成', res);
      });
    },
    // 刷新公告
    refreshNotice(record) {
      this.handleConfrimRequest(this.url.noticeRefresh, {id: record.id}, `是否刷新${record.title}？`, '点击确定刷新');
    },
    // 预览渠道公告
    handlePreview(record) {
      let that = this;
      that.$refs.htmlModal.title = '公告预览';
      that.$refs.htmlModal.edit(record.content);
    }
  }
};
</script>
<style scoped>
@import '~@assets/less/common.less';

.noticeContent {
  max-height: 240px;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>
