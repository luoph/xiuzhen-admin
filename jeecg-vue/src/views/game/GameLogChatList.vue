<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="3" :sm="8">
            <a-form-item label="区服Id">
              <a-input placeholder="请输入区服Id" v-model="queryParam.serverId" />
            </a-form-item>
          </a-col>
          <a-col :md="3" :sm="8">
            <a-form-item label="聊天类型">
              <a-select placeholder="请选择聊天类型" v-model="queryParam.chatType" initialValue="1">
                <!-- <a-select-option :value="0">传闻</a-select-option> -->
                <a-select-option :value="1">世界聊天</a-select-option>
                <a-select-option :value="2">私聊</a-select-option>
                <a-select-option :value="3">仙盟聊天</a-select-option>
                <a-select-option :value="4">跨服聊天</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="3" :sm="8">
            <a-form-item label="发送者id">
              <a-input placeholder="请输入发送者id" v-model="queryParam.senderId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="发送者名称">
              <a-input placeholder="请输入发送者名称" v-model="queryParam.senderName" />
            </a-form-item>
          </a-col>
          <a-col :md="3" :sm="8">
            <a-form-item label="接收者id">
              <a-input placeholder="请输入接收者id" v-model="queryParam.receiverId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="接收者名称">
              <a-input placeholder="请输入接收者名称" v-model="queryParam.receiverName" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="消息">
                <j-input placeholder="请输入消息内容" v-model="queryParam.msgContent"></j-input>
              </a-form-item>
            </a-col>
            <a-col :md="3" :sm="8">
              <a-form-item label="消息类型">
                <a-select placeholder="请选择消息类型" v-model="queryParam.msgType" initialValue="1">
                  <a-select-option :value="1">普通文本</a-select-option>
                  <a-select-option :value="2">修真日志</a-select-option>
                  <a-select-option :value="3">分享</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
              </a-form-item>
            </a-col>
          </template>
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
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="status">
          <div v-html="status"></div>
        </template>
        <template slot="imgSlot" slot-scope="text, record">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)"> 下载 </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">详情</a>
        </span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less';
import { mixinDevice } from '@/utils/mixin';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'LogChatList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JInput
  },
  data() {
    return {
      description: '聊天日志管理页面',
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
          title: '区服Id',
          align: 'center',
          width: 80,
          dataIndex: 'serverId'
        },
        {
          title: '发送者id',
          align: 'center',
          width: 80,
          dataIndex: 'senderId'
        },
        {
          title: '发送者名称',
          align: 'center',
          width: 120,
          dataIndex: 'senderName'
        },
        {
          title: '接收者id',
          align: 'center',
          width: 80,
          dataIndex: 'receiverId'
        },
        {
          title: '接收者名称',
          align: 'center',
          width: 120,
          dataIndex: 'receiverName'
        },
        {
          title: '聊天类型',
          align: 'center',
          width: 80,
          dataIndex: 'chatType',
          customRender: (value) => {
            let re = '未知';
            if (value === 0) {
              re = '传闻';
            } else if (value === 1) {
              re = '世界聊天';
            } else if (value === 2) {
              re = '私聊';
            } else if (value === 3) {
              re = '仙盟聊天';
            } else if (value === 4) {
              re = '跨服聊天';
            }
            return re;
          }
        },
        {
          title: '消息类型',
          align: 'center',
          width: 80,
          dataIndex: 'msgType',
          customRender: (value) => {
            let re = '未知';
            if (value === 1) {
              re = '普通文本';
            } else if (value === 2) {
              re = '修真日志';
            } else if (value === 3) {
              re = '分享';
            }
            return re;
          }
        },
        {
          title: '消息内容',
          align: 'center',
          width: 320,
          dataIndex: 'msgContent'
        },
        {
          title: '发送时间',
          align: 'center',
          width: 120,
          dataIndex: 'createTime'
        }
        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align: 'center',
        //   fixed: 'right',
        //   width: 147,
        //   scopedSlots: { customRender: 'action' }
        // }
      ],
      url: {
        list: '/game/stat/logChat/list',
        exportXlsUrl: '/game/stat/logChat/exportXls'
      },
      dictOptions: {},
      superFieldList: []
    };
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {},
    getQueryParams() {
      console.log(this.queryParam.createTimeRange);
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createTimeRange;
      return filterObj(param);
    },
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>