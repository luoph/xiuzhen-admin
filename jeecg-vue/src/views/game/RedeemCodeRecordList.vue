<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector
          ref="channelServerSelector"
          :show-sdk-channel="true"
          @onSelectChannel="onSelectChannel"
          @onSelectSdkChannel="onSelectSdkChannel"
          @onSelectServer="onSelectServer"
        />
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="玩家ID">
              <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="兑换码">
              <a-input placeholder="请输入兑换码" v-model="queryParam.code" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="兑换IP">
              <a-input placeholder="请输入兑换IP" v-model="queryParam.remoteIp" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="分组ID">
                <a-input placeholder="请输入分组ID" v-model="queryParam.groupId" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="16">
              <a-form-item label="创建时间">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
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
      <!-- <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('激活码领取记录')">导出</a-button>
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
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">详情</a>
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
        <span slot="codeTitle">激活码 <a-icon type="copy" /></span>
        <span slot="channelTitle">渠道 <a-icon type="copy" /></span>
        <span slot="sdkChannelTitle">Sdk渠道 <a-icon type="copy" /></span>
        <span slot="playerIdTitle">玩家ID <a-icon type="copy" /></span>
        <span slot="nicknameTitle">角色名 <a-icon type="copy" /></span>
        <span slot="serverIdTitle">区服id <a-icon type="copy" /></span>
        <span slot="ipTitle">兑换IP <a-icon type="copy" /></span>
      </a-table>
    </div>
    <redeemCodeRecord-modal ref="modalForm" @ok="modalFormOk"></redeemCodeRecord-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import RedeemCodeRecordModal from './modules/RedeemCodeRecordModal';
import JDate from '@/components/jeecg/JDate.vue';
import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';

export default {
  name: 'RedeemCodeRecordList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    RedeemCodeRecordModal,
    ChannelServerSelector
  },
  data() {
    return {
      description: '激活码领取记录管理页面',
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
          title: 'id',
          align: 'center',
          dataIndex: 'id'
        },
        {
          // title: '兑换码',
          align: 'center',
          dataIndex: 'code',
          slots: { title: 'codeTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '渠道',
          align: 'center',
          dataIndex: 'channel',
          slots: { title: 'channelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: 'Sdk渠道',
          align: 'center',
          dataIndex: 'sdkChannel',
          slots: { title: 'sdkChannelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId',
          slots: { title: 'playerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '分组ID',
          align: 'center',
          dataIndex: 'groupId'
        },
        {
          // title: '区服id',
          align: 'center',
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '兑换IP',
          align: 'center',
          dataIndex: 'remoteIp',
          slots: { title: 'ipTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '创建时间',
          align: 'center',
          width: 240,
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 180,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/redeemCodeRecord/list',
        delete: 'game/redeemCodeRecord/delete',
        deleteBatch: 'game/redeemCodeRecord/deleteBatch',
        exportXlsUrl: 'game/redeemCodeRecord/exportXls',
        importExcelUrl: 'game/redeemCodeRecord/importExcel'
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
    onSelectChannel: function (value) {
      this.queryParam.channel = value;
    },
    onSelectSdkChannel: function (value) {
      this.queryParam.sdkChannel = value;
    },
    onSelectServer: function (value) {
      this.queryParam.serverId = value;
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
