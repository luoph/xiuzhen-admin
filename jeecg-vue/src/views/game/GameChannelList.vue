<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="渠道名">
              <j-input placeholder="请输入渠道名" v-model="queryParam.name"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="渠道标识">
              <j-search-select-tag placeholder="请选择渠道标识" v-model="queryParam.simpleName"
                                   dict="game_channel,name,simple_name"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="公告id">
                <a-input-number placeholder="请输入公告id" v-model="queryParam.noticeId"
                                style="width: 100%"></a-input-number>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="备注">
                <j-input placeholder="请输入备注" v-model="queryParam.remark"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <a-button @click="updateServerList" type="primary" icon="sync">区服列表</a-button>
      <a-button @click="updateWhitelist" type="primary" icon="sync">IP白名单</a-button>
      <a-button @click="updateServerCache" type="primary" icon="sync">区服缓存</a-button>
      <a-button @click="updateChatServerCache" type="primary" icon="sync">聊天缓存</a-button>

      <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏渠道')">导出</a-button> -->
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
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
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>
            :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" -->

      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource"
               :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a @click="handleCopy(record)">复制</a>
          <a-divider type="vertical"/>
          <a @click="editChannelServer(record)">区服列表</a>
          <a-divider type="vertical"/>
          <a @click="updateChannelServer(record)">刷新区服</a>
          <a-divider type="vertical"/>
          <a @click="editChannelNotice(record)">编辑公告</a>
          <a-divider type="vertical"/>
          <a @click="viewChannelNotice(record)">预览公告</a>
          <a-divider type="vertical"/>
          <a @click="refreshChannelNotice(record)">刷新公告</a>
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
        <span slot="splitTags" slot-scope="text, record">
          <a-tag v-if="!text" color="red">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="switchSlot" slot-scope="text, record">
          <a-switch checked-children="开" un-checked-children="关" :checked="text === 1"/>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <game-channel-modal ref="modalForm" @ok="modalFormOk"/>
    <!-- 编辑公告 -->
    <game-notice-modal ref="noticeModal" @ok="modalFormOk"/>
    <!-- html预览 -->
    <game-html-preview-modal ref="htmlModal" @ok="modalFormOk"/>
    <game-channel-server-list ref="channelServerList"/>
  </a-card>
</template>

<script>
import {filterObj} from '@/utils/util';
import JInput from '@/components/jeecg/JInput';
import GameChannelModal from './modules/GameChannelModal';
import GameNoticeModal from './modules/GameNoticeModal';
import GameHtmlPreviewModal from './modules/GameHtmlPreviewModal';
import GameChannelServerList from './GameChannelServerList';
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {getAction} from '@/api/manage';

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
  name: 'GameChannelList',
  mixins: [JeecgListMixin],
  components: {JInput, GameChannelModal, GameNoticeModal, GameHtmlPreviewModal, GameChannelServerList},
  data() {
    return {
      description: '游戏渠道管理页面',
      gameList: [],
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
          title: '渠道id',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '渠道名称',
          align: 'center',
          width: 120,
          dataIndex: 'name'
        },
        {
          title: '唯一标识',
          align: 'center',
          dataIndex: 'simpleName'
        },
        {
          title: '游戏编号',
          align: 'center',
          width: 140,
          dataIndex: 'gameId',
          customRender: (text) => {
            return filterGameIdText(this.gameList, text);
          }
        },
        {
          title: '公告id',
          align: 'center',
          width: 80,
          dataIndex: 'noticeId'
        },
        {
          title: '版本号',
          align: 'center',
          width: 100,
          dataIndex: 'versionCode'
        },
        {
          title: '版本名',
          align: 'center',
          width: 120,
          dataIndex: 'versionName'
        },
        // {
        //   title: '数数统计',
        //   align: 'center',
        //   width: 80,
        //   dataIndex: 'taStatistics',
        //   customRender: function (text) {
        //     if (text === 0) {
        //       return '关闭';
        //     } else if (text === 1) {
        //       return '开启';
        //     }
        //   }
        // },
        {
          title: '网页登录',
          align: 'center',
          width: 80,
          dataIndex: 'testLogin',
          scopedSlots: {customRender: 'switchSlot'},
        },
        {
          title: 'IP白名单',
          align: 'left',
          dataIndex: 'ipWhitelist',
          scopedSlots: {customRender: 'splitTags'}
        },
        {
          title: '版本更新时间',
          align: 'center',
          width: 180,
          dataIndex: 'versionUpdateTime'
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 220,
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: 'game/channel/list',
        delete: 'game/channel/delete',
        deleteBatch: 'game/channel/deleteBatch',
        // 刷新所有渠道区服
        updateAllServerUrl: 'game/channel/updateAllServer',
        // 刷新渠道区服
        updateChannelServerUrl: 'game/channel/updateChannelServer',
        updateWhitelistUrl: 'game/channel/updateIpWhitelist',
        updateServerCacheUrl: 'game/channel/updateServerCache',
        updateChatServerCacheUrl: 'game/channel/updateChatServerCache',
        // 游戏列表
        gameInfoListUrl: 'game/info/list',
        // 公告id
        noticeUrl: 'game/gameNotice/queryById',
        // 刷新渠道公告
        noticeRefreshUrl: 'game/gameNotice/refreshById'
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
      getAction(that.url.gameInfoListUrl).then((res) => {
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
    // 编辑游戏服数据
    editChannelServer(record) {
      this.$refs.channelServerList.edit(record);
    },
    // 编辑渠道公告
    editChannelNotice(record) {
      let that = this;
      getAction(that.url.noticeUrl, {id: record.noticeId}).then((res) => {
        if (res.success && res.result) {
          that.$refs.noticeModal.edit(res.result);
        } else {
          that.$message.error('公告不存在，请检查公告设置');
        }
      });
    },
    // 预览渠道公告
    viewChannelNotice(record) {
      let that = this;
      getAction(that.url.noticeUrl, {id: record.noticeId}).then((res) => {
        if (res.success && res.result) {
          that.$refs.htmlModal.title = '公告预览';
          that.$refs.htmlModal.edit(res.result.content);
        } else {
          that.$message.error('公告不存在，请检查公告设置');
        }
      });
    },
    refreshChannelNotice(record) {
      // 刷新渠道公告
      this.handleConfrimRequest(this.url.noticeRefreshUrl,
        {id: record.noticeId},
        '是否刷新渠道公告？',
        '点击刷新渠道公告');
    },
    updateChannelServer(record) {
      // 刷新客户端区服列表
      this.handleConfrimRequest(this.url.updateChannelServerUrl,
        {id: record.id},
        '是否刷新区服列表？',
        '点击确定刷新');
    },
    updateServerList() {
      // 刷新客户端区服列表
      this.handleConfrimRequest(this.url.updateAllServerUrl,
        {},
        '是否刷新客户端区服列表？',
        '点击确定刷新');
    },
    updateWhitelist() {
      // 刷新IP白名单配置
      this.handleConfrimRequest(this.url.updateWhitelistUrl,
        {},
        '是否刷新IP白名单？',
        '点击确定刷新');
    },
    updateServerCache() {
      // 刷新区服缓存
      this.handleConfrimRequest(this.url.updateServerCacheUrl,
        {},
        '是否刷新区服缓存？',
        '点击确定刷新');
    },
    updateChatServerCache() {
      // 刷新聊天消息缓存
      this.handleConfrimRequest(this.url.updateChatServerCacheUrl,
        {},
        '是否刷新聊天消息缓存？',
        '点击确定刷新');
    }
  }
};
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
