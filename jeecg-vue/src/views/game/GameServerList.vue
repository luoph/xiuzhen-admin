<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="游戏编号">
              <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库 -->
              <j-search-select-tag v-model="queryParam.gameId" placeholder="请选择游戏编号" dict="game_info,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="服务器IP">
              <j-search-select-tag placeholder="请选择服务器IP" v-model="queryParam.host" dict="game_vps,hostname,ip" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="区服ID">
              <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.id_begin" />
              <span class="query-group-split-cust" />
              <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.id_end" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="运行状态">
              <a-select placeholder="请选择运行状态" v-model="queryParam.outdated">
                <a-select-option :value="0">上线中</a-select-option>
                <a-select-option :value="1">已合服</a-select-option>
                <a-select-option :value="2">已下线</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="维护状态">
              <a-select placeholder="请选择维护状态" v-model="queryParam.isMaintain">
                <a-select-option :value="0">正常</a-select-option>
                <a-select-option :value="1">维护</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="标签">
              <j-dict-select-tag v-model="queryParam.tagId" placeholder="请选择标签" dictCode="game_server_tag,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="开服时间">
              <a-range-picker v-model="queryParam.openTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onOpenDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="创建时间">
              <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="状态">
                <j-dict-select-tag v-model="queryParam.status" placeholder="请选择状态" dictCode="server_status" />
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="类型">
                <j-dict-select-tag v-model="queryParam.type" placeholder="请选择类型" dictCode="server_type" />
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="名字">
                <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库 -->
                <j-search-select-tag v-model="queryParam.id" placeholder="请选择名字" dict="game_server,name,id" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
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
      <a-button :disabled="selectedRowKeys.length <= 0" @click="updateActivity" type="primary" icon="sync"> 刷新活动配置 </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="updateSetting" type="primary" icon="sync"> 刷新游戏设置 </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="syncPlayer" v-has="'game:server:admin'" icon="sync">同步角色 </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="startMaintain" v-has="'game:server:admin'" type="danger" icon="alert">开启维护 </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="stopMaintain" v-has="'game:server:admin'" type="danger" icon="alert">结束维护 </a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏服配置')">导出</a-button> -->
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div> -->
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
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleCopy(record)">复制</a>
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link"> 更多 <a-icon type="down"/> </a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </span>
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="tagSlot" slot-scope="text, record">
          <a-tag :key="text" :color="tagColor(record.tagId * 1000)">{{ text }}</a-tag>
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
        <span slot="channelSlot" slot-scope="text" class="tag-container">
          <a-tag v-if="!text">未设置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="versionSlot" slot-scope="text">
          <a-tag v-if="!text">未配置</a-tag>
          <!-- <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag> -->
          <span v-else v-for="tag in text.split(',').sort()" :key="tag">
            <a-tag v-if="tag == 1" color="blue">{{ tag }}-普通服</a-tag>
            <a-tag v-if="tag == 2" color="blue">{{ tag }}-BT服</a-tag>
          </span>
        </span>
        <span slot="maintainSlot" slot-scope="text, record">
          <a-tag v-if="record.isMaintain === 1" color="red">维护中</a-tag>
          <a-tag v-else color="green">运行中</a-tag>
        </span>
        <span slot="switchSlot" slot-scope="text">
          <a-switch checked-children="开" un-checked-children="关" :checked="text === 1" />
        </span>
        <span slot="statusSlot" slot-scope="text, record">
          <a-tag v-if="record.status === 0" color="blue">正常</a-tag>
          <a-tag v-else-if="record.status === 1" color="green">流畅</a-tag>
          <a-tag v-else-if="record.status === 2" color="red">火爆</a-tag>
          <a-tag v-else-if="record.status === 3" color="gray">维护</a-tag>
        </span>
        <span slot="outdatedSlot" slot-scope="text, record">
          <a-tag v-if="record.outdated === 0" color="green">上线中</a-tag>
          <a-tag v-else-if="record.outdated === 1" color="red">已合并</a-tag>
          <a-tag v-else-if="record.outdated === 2" color="red">已下线</a-tag>
        </span>
        <span slot="idTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="hostTitle" class="copy-text">服务器IP <a-icon type="copy" /></span>
        <span slot="loginUrlTitle" class="copy-text">连接地址 <a-icon type="copy" /></span>
        <span slot="gmUrlTitle" class="copy-text">GM地址 <a-icon type="copy" /></span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <game-server-modal ref="modalForm" @ok="modalFormOk"></game-server-modal>
  </a-card>
</template>

<script>
import GameServerModal from './modules/GameServerModal';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import { getAction } from '@/api/manage';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameServerList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameServerModal
  },
  data() {
    return {
      description: '游戏服配置',
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
          fixed: 'left',
          width: 40,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          // title: '区服ID',
          fixed: 'left',
          align: 'center',
          dataIndex: 'id',
          // sorter: true,
          slots: { title: 'idTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          title: '名字',
          fixed: 'left',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '标签',
          align: 'center',
          dataIndex: 'tag',
          scopedSlots: { customRender: 'tagSlot' }
        },
        {
          // title: '服务器IP',
          align: 'left',
          dataIndex: 'host',
          slots: { title: 'hostTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '连接地址',
          align: 'left',
          dataIndex: 'loginUrl',
          slots: { title: 'loginUrlTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '在线数',
          align: 'center',
          dataIndex: 'onlineNum',
          customRender: (text) => {
            return text === 0 ? text : text || 'N/A';
          }
        },
        {
          title: '运行状态',
          align: 'center',
          dataIndex: 'outdated',
          scopedSlots: { customRender: 'outdatedSlot' }
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'statusSlot' }
        },
        {
          title: '维护状态',
          align: 'center',
          dataIndex: 'isMaintain',
          scopedSlots: { customRender: 'maintainSlot' }
        },
        {
          title: '开服时间',
          width: 120,
          sorter: true,
          align: 'center',
          dataIndex: 'openTime'
        },
        {
          title: '上线时间',
          sorter: true,
          align: 'center',
          dataIndex: 'onlineTime'
        },
        {
          title: '版本类型',
          align: 'center',
          dataIndex: 'versionType',
          customRender: (text) => {
            return text === 1 ? '普通服' : text === 2 ? 'BT服' : 'N/A';
          }
        },
        {
          title: '删档返还',
          align: 'center',
          dataIndex: 'stopServerRefund',
          scopedSlots: { customRender: 'switchSlot' }
        },
        {
          title: '推荐标识',
          width: 80,
          align: 'center',
          dataIndex: 'recommend_dictText'
        },
        {
          title: '类型',
          width: 60,
          align: 'center',
          dataIndex: 'type_dictText'
        },
        {
          title: '删档返还渠道',
          width: 80,
          align: 'center',
          dataIndex: 'stopServerRefundChannel',
          scopedSlots: { customRender: 'channelSlot' }
        },
        {
          title: '删档返还版本',
          width: 80,
          align: 'center',
          dataIndex: 'stopServerRefundVersionType',
          scopedSlots: { customRender: 'versionSlot' }
        },
        {
          title: 'GM开关',
          width: 80,
          align: 'center',
          dataIndex: 'gmStatus',
          scopedSlots: { customRender: 'switchSlot' }
        },
        {
          // title: 'GM地址',
          align: 'left',
          dataIndex: 'gmUrl',
          slots: { title: 'gmUrlTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        // {
        //   title: '客户端GM',
        //   align: 'left',
        //   width: 120,
        //   dataIndex: 'clientGm',
        //   customRender: (text) => {
        //     return text || '--';
        //   }
        // },
        {
          title: '备注',
          width: 120,
          align: 'center',
          fixed: 'right',
          dataIndex: 'remark'
        },
        {
          title: '操作',
          width: 120,
          align: 'center',
          fixed: 'right',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameServer/list',
        delete: 'game/gameServer/delete',
        deleteBatch: 'game/gameServer/deleteBatch',
        updateActivity: 'game/gameServer/updateActivity',
        updateSetting: 'game/gameServer/updateSetting',
        syncPlayer: 'game/gameServer/syncPlayer',
        getOnlineNum: 'game/gameServer/getOnlineNum',
        startMaintain: 'game/gameServer/startMaintain',
        stopMaintain: 'game/gameServer/stopMaintain',
        // 刷新所有渠道区服
        updateAllServerUrl: 'game/channel/updateAllServer',
        // 刷新渠道区服
        updateChannelServerUrl: 'game/channel/updateChannelServer',
        updateWhitelistUrl: 'game/channel/updateIpWhitelist',
        updateServerCacheUrl: 'game/channel/updateServerCache',
        updateChatServerCacheUrl: 'game/channel/updateChatServerCache',
        gameInfoListUrl: 'game/info/list'
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
    getQueryParams() {
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;

      // 范围参数不传递后台
      delete param.createTimeRange;
      delete param.openTimeRange;
      return filterObj(param);
    },
    onCreateDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
    onOpenDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.openTime_begin = dateString[0];
      this.queryParam.openTime_end = dateString[1];
    },
    onDateOk(value) {
      console.log(value);
    },
    updateActivity: function () {
      this.batchAction(this.url.updateActivity, false);
    },
    updateSetting: function () {
      this.batchAction(this.url.updateSetting, false);
    },
    syncPlayer: function () {
      this.batchAction(this.url.syncPlayer, false);
    },
    startMaintain: function () {
      this.batchAction(this.url.startMaintain, true, '确定开启维护状态？', '开启维护状态将导致所有玩家掉线');
    },
    stopMaintain: function () {
      this.batchAction(this.url.stopMaintain, true, '确定关闭维护状态？', '关闭维护状态将允许玩家上线');
    },
    updateServerList() {
      // 刷新客户端区服列表
      this.handleConfrimRequest(this.url.updateAllServerUrl, {}, '是否刷新客户端区服列表？', '点击确定刷新');
    },
    updateWhitelist() {
      // 刷新IP白名单配置
      this.handleConfrimRequest(this.url.updateWhitelistUrl, {}, '是否刷新IP白名单？', '点击确定刷新');
    },
    updateServerCache() {
      // 刷新区服缓存
      this.handleConfrimRequest(this.url.updateServerCacheUrl, {}, '是否刷新区服缓存？', '点击确定刷新');
    },
    updateChatServerCache() {
      // 刷新聊天消息缓存
      this.handleConfrimRequest(this.url.updateChatServerCacheUrl, {}, '是否刷新聊天消息缓存？', '点击确定刷新');
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
