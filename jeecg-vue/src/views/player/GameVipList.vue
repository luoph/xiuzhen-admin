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
            <a-form-item label="角色名">
              <a-input placeholder="请输入角色名" v-model="queryParam.nickname" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="sync" @click="refreshCache">刷新缓存</a-button>
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('VIP玩家')">导出</a-button>
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
        :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span" @click="copyText(tag)">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="tagSlot" slot-scope="text">
          <a-tag color="orange">{{ text }}</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
        <span slot="playerIdTitle" class="copy-text">玩家ID <a-icon type="copy" /></span>
        <span slot="nicknameTitle" class="copy-text">角色名 <a-icon type="copy" /></span>
        <span slot="accountTitle" class="copy-text">账号 <a-icon type="copy" /></span>
        <span slot="serverIdTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="sidTitle" class="copy-text">创角区服 <a-icon type="copy" /></span>
        <span slot="channelTitle" class="copy-text">渠道 <a-icon type="copy" /></span>
        <span slot="sdkChannelTitle" class="copy-text">Sdk渠道 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <GameVipModal ref="modalForm" @ok="modalFormOk"></GameVipModal>
  </a-card>
</template>

<script>
import JInput from '@/components/jeecg/JInput';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction } from '@/api/manage';
import { filterObj } from '@/utils/util';
import GameVipModal from './modules/GameVipModal';
import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';

export default {
  name: 'GameVipList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameVipModal,
    ChannelServerSelector
  },
  data() {
    return {
      description: 'VIP管理页面',
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
          // title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId',
          slots: { title: 'playerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '角色名',
          align: 'center',
          dataIndex: 'nickname',
          slots: { title: 'nicknameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '区服',
          align: 'center',
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          // title: '创角区服',
          align: 'center',
          dataIndex: 'sid',
          slots: { title: 'sidTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
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
          title: '玩家等级',
          align: 'center',
          dataIndex: 'level'
        },
        {
          title: '充值总金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        {
          title: '最近充值',
          align: 'center',
          dataIndex: 'lastPay',
          customRender: function (text) {
            return text || '--';
          }
        },
        {
          title: '创角时间',
          align: 'center',
          dataIndex: 'registerTime',
          customRender: function (text) {
            return text || '--';
          }
        },
        {
          title: '最后登录时间',
          align: 'center',
          dataIndex: 'lastLoginTime',
          customRender: function (text) {
            return text || '--';
          }
        },
        {
          title: '最后充值时间',
          align: 'center',
          dataIndex: 'lastPayTime',
          customRender: function (text) {
            return text || '--';
          }
        },
        {
          title: '创角天数',
          align: 'center',
          dataIndex: 'playDays'
        },
        {
          title: '登录预警天数',
          align: 'center',
          dataIndex: 'lastLoginDays'
        },
        {
          title: '充值预警天数',
          align: 'center',
          dataIndex: 'lastPayDays'
        },
        {
          title: '操作人员',
          align: 'center',
          dataIndex: 'createBy'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          width: 100,
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/vip/list',
        delete: 'game/vip/delete',
        refresh: 'game/vip/refresh',
        deleteBatch: 'game/vip/deleteBatch',
        exportXlsUrl: 'game/vip/exportXls',
        importExcelUrl: 'game/vip/importExcel'
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
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
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
    },
    refreshCache() {
      // 刷新VIP缓存
      let that = this;
      getAction(that.url.refresh).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
        } else {
          that.$message.error(res.message);
        }
        that.loadData();
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
