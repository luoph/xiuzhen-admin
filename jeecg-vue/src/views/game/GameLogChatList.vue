<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="区服">
              <j-search-select-tag placeholder="请选择区服" v-model="queryParam.serverId" dict="game_server,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="聊天类型">
              <a-select placeholder="请选择聊天类型" v-model="queryParam.chatType" initialValue="1">
                <!-- <a-select-option :value="0">传闻</a-select-option> -->
                <a-select-option :value="1">世界</a-select-option>
                <a-select-option :value="2">私聊</a-select-option>
                <a-select-option :value="3">仙盟</a-select-option>
                <a-select-option :value="4">跨服</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <!-- <a-col :md="6" :sm="8">
            <a-form-item label="消息类型">
              <a-select placeholder="请选择消息类型" v-model="queryParam.msgType" initialValue="1">
                <a-select-option :value="1">普通文本</a-select-option>
                <a-select-option :value="2">修真日志</a-select-option>
                <a-select-option :value="3">分享</a-select-option>
              </a-select>
            </a-form-item>
          </a-col> -->
          <a-col :md="6" :sm="8">
            <a-form-item label="发送者id">
              <a-input placeholder="请输入发送者id" v-model="queryParam.senderId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="接收者id">
              <a-input placeholder="请输入接收者id" v-model="queryParam.receiverId" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="日期">
              <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="消息内容">
                <j-input placeholder="请输入消息内容" v-model="queryParam.msgContent" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="发送者名称">
                <a-input placeholder="请输入发送者名称" v-model="queryParam.senderName" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="接收者名称">
                <a-input placeholder="请输入接收者名称" v-model="queryParam.receiverName" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="10" :sm="8">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayRange" @change="onDayRangeChange">
                <a-radio :value="-1">自定义</a-radio>
                <a-radio :value="0">今天</a-radio>
                <a-radio :value="2">近3天</a-radio>
                <a-radio :value="6">近7天</a-radio>
                <a-radio :value="14">近15天</a-radio>
                <a-radio :value="29">近1月</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
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
      <a-button type="primary" icon="download" @click="handleExportXls('聊天日志')">导出</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="action" slot-scope="text, record">
          <a-button type="danger" size="small" @click="forbidTalk(record)"> 禁言 </a-button>
          <a-divider type="vertical" />
          <a-button type="danger" size="small" @click="forbidLogin(record)"> 封号 </a-button>
          <a-divider type="vertical" />
          <a-button size="small" @click="kickOff(record)"> 踢下线 </a-button>
          <a-divider />
          <a-button type="primary" size="small" @click="undoForbidTalk(record)"> 禁言撤回 </a-button>
          <a-divider type="vertical" />
          <a-button type="primary" size="small" @click="undoForbidLogin(record)"> 封号撤回 </a-button>
        </span>
        <span slot="chatTypeSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="green">传闻</a-tag>
          <a-tag v-else-if="text === 1" color="blue">世界</a-tag>
          <a-tag v-else-if="text === 2" color="orange">私聊</a-tag>
          <a-tag v-else-if="text === 3" color="cyan">仙盟</a-tag>
          <a-tag v-else-if="text === 4" color="purple">跨服</a-tag>
          <a-tag v-else>未知</a-tag>
        </span>
        <span slot="msgTypeSlot" slot-scope="text">
          <a-tag v-if="text === 1" color="green">普通文本</a-tag>
          <a-tag v-else-if="text === 2" color="blue">修真日志</a-tag>
          <a-tag v-else-if="text === 3" color="orange">分享</a-tag>
          <a-tag v-else color="red">未知</a-tag>
        </span>
        <span slot="serverIdTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="accountTitle" class="copy-text">账号 <a-icon type="copy" /></span>
        <span slot="senderIdTitle" class="copy-text">发送者id <a-icon type="copy" /></span>
        <span slot="senderNameTitle" class="copy-text">发送者 <a-icon type="copy" /></span>
        <span slot="receiverIdTitle" class="copy-text">接收者id <a-icon type="copy" /></span>
        <span slot="receiverNameTitle" class="copy-text">接收者 <a-icon type="copy" /></span>
        <span slot="sdkChannelTitle" class="copy-text">Sdk渠道 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <GameForbidTalkModal ref="forbiddenTalkForm" @ok="modalFormOk"></GameForbidTalkModal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less';
import { mixinDevice } from '@/utils/mixin';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import JInput from '@/components/jeecg/JInput';
import { getAction } from '@/api/manage';
import moment from 'moment';
import GameForbidTalkModal from './modules/GameForbidTalkModal';

export default {
  name: 'LogChatList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JInput,
    GameForbidTalkModal
  },
  data() {
    return {
      description: '聊天日志管理页面',
      timeout: 90000,
      dayRange: 0,
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
          // title: '区服ID',
          align: 'center',
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        {
          // title: '账号',
          align: 'center',
          width: 80,
          dataIndex: 'account',
          slots: { title: 'accountTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '发送者id',
          align: 'center',
          dataIndex: 'senderId',
          slots: { title: 'senderIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '发送者名称',
          align: 'center',
          dataIndex: 'senderName',
          slots: { title: 'senderNameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '接收者id',
          align: 'center',
          width: 100,
          dataIndex: 'receiverId',
          slots: { title: 'receiverIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '接收者名称',
          align: 'center',
          dataIndex: 'receiverName',
          slots: { title: 'receiverNameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '聊天类型',
          align: 'center',
          dataIndex: 'chatType',
          scopedSlots: { customRender: 'chatTypeSlot' }
        },
        // {
        //   title: '消息类型',
        //   align: 'center',
        //   dataIndex: 'msgType',
        //   scopedSlots: { customRender: 'msgTypeSlot' }
        // },
        {
          title: '角色等级',
          align: 'center',
          dataIndex: 'level'
        },
        {
          // title: 'Sdk渠道',
          align: 'center',
          // width: 100,
          dataIndex: 'sdkChannel',
          slots: { title: 'sdkChannelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '消息内容',
          width: 280,
          dataIndex: 'msgContent'
        },
        {
          title: '发送时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          align: 'center',
          width: 220,
          // fixed: 'right',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/game/stat/logChat/list',
        undo: '/game/forbidden/undo',
        kickOff: '/game/forbidden/kickOff',
        exportXlsUrl: '/game/stat/logChat/exportXls'
      },
      dictOptions: {},
      superFieldList: []
    };
  },
  created() {},
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {},
    getQueryParams() {
      if (this.dayRange >= 0) {
        this.selectDayRange(this.dayRange);
      }
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createDateRange;
      return filterObj(param);
    },
    onDateChange: function (value, date) {
      this.queryParam.createDate_begin = date[0];
      this.queryParam.createDate_end = date[1];
      this.dayRange = -1;
    },
    onDayRangeChange(e) {
      if (e.target.value >= 0) {
        this.selectDayRange(e.target.value);
      }
    },
    selectDayRange(dayRange) {
      if (dayRange >= 0) {
        const start = dayRange >= 9999 ? '' : moment().subtract(dayRange, 'days').format('YYYY-MM-DD');
        const end = moment().format('YYYY-MM-DD');
        this.queryParam.createDateRange = [start, end];
        this.queryParam.createDate_begin = start;
        this.queryParam.createDate_end = end;
      }
    },
    forbidTalk(record) {
      // 禁言
      let that = this;
      // 1 - 登录, 2 - 聊天
      let model = { type: 2, banKey: 'playerId', banValue: record.senderId, serverId: record.serverId, duration: 86400 };
      that.$refs.forbiddenTalkForm.title = '禁言玩家: ' + record.senderId;
      that.$refs.forbiddenTalkForm.edit(model);
    },
    kickOff(record) {
      // 踢下线
      let that = this;
      this.$confirm({
        title: '是否踢玩家下线?',
        content: '玩家ID：' + record.senderId + ' 角色名：' + record.senderName,
        onOk: function () {
          getAction(that.url.kickOff, { playerId: record.senderId, serverId: record.serverId }).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
            } else {
              that.$message.error(res.message);
            }
          });
        }
      });
    },
    forbidLogin(record) {
      // 封号
      let that = this;
      // 1 - 登录, 2 - 聊天
      let model = { type: 1, banKey: 'playerId', banValue: record.senderId, serverId: record.serverId, duration: 86400 };
      that.$refs.forbiddenTalkForm.title = '封号玩家: ' + record.senderId;
      that.$refs.forbiddenTalkForm.edit(model);
    },
    undoForbidLogin(record) {
      // 撤回封号
      this.undoForbid(1, record.serverId, record.senderId);
    },
    undoForbidTalk(record) {
      // 撤回禁言
      this.undoForbid(2, record.serverId, record.senderId);
    },
    undoForbid(type, serverId, playerId) {
      // 1 - 登录, 2 - 聊天
      let that = this;
      getAction(that.url.undo, { type: type, serverId: serverId, playerId: playerId }).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
        } else {
          that.$message.error(res.message);
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.ant-divider-horizontal {
  margin: 6px 0 6px 0;
}
</style>