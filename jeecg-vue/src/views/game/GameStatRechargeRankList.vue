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
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="8">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayType" @change="onDayTypeChange">
                <a-radio :value="0">自定义</a-radio>
                <a-radio :value="7">近7天</a-radio>
                <a-radio :value="15">近15天</a-radio>
                <a-radio :value="30">近1月</a-radio>
                <a-radio :value="60">近2月</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="download" @click="handleExportXls('付费排行')">导出</a-button>
              <a-button type="primary" icon="search" style="margin-left: 8px" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div>
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
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <span slot="tagSlot" slot-scope="text, record">
          <a-tag color="orange">{{ text }}</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay" v-has="'game:vip:admin'">
              <a-menu-item :disabled="record.vipId > 0" @click="addVip(record)">添加VIP</a-menu-item>
              <a-menu-item :disabled="record.vipId === 0" @click="deleteVip(record)">删除VIP</a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import { getAction } from '@/api/manage';
import { filterObj } from '@/utils/util';
import moment from 'moment';
import ChannelServerSelector from '@comp/gameserver/ChannelServerSelector';

export default {
  name: 'GameStatRechargeRankList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      description: '充值用户排行',
      timeout: 90000,
      dayType: 7,
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '玩家id',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '区服',
          align: 'center',
          dataIndex: 'serverId'
        },
        {
          title: '创角区服',
          align: 'center',
          dataIndex: 'sid'
        },
        {
          title: '渠道',
          align: 'center',
          dataIndex: 'channel'
        },
        {
          title: 'Sdk渠道',
          align: 'center',
          dataIndex: 'sdkChannel'
        },
        {
          title: '玩家昵称',
          align: 'center',
          dataIndex: 'nickname'
        },
        {
          title: '排名',
          align: 'center',
          dataIndex: 'rank'
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
          dataIndex: 'lastPay'
        },
        {
          title: '创角时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '最后登录时间',
          align: 'center',
          dataIndex: 'lastLoginTime'
        },
        {
          title: '最后充值时间',
          align: 'center',
          dataIndex: 'lastPayTime'
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
          title: '操作',
          width: 100,
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/stat/rechargeRank/list',
        addVip: 'game/vip/addVip',
        deleteVip: 'game/vip/delete',
        exportXlsUrl: 'game/stat/rechargeRank/exportXls'
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
    getQueryParams() {
      if (this.dayType > 0) {
        this.selectDayType(this.dayType);
      }
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.countDateRange;
      return filterObj(param);
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
    },
    onDateChange(date, dateString) {
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
      this.dayType = 0;
    },
    onDayTypeChange(e) {
      if (e.target.value > 0) {
        this.selectDayType(e.target.value);
      }
    },
    selectDayType(dayType) {
      if (dayType > 0) {
        const start = moment().subtract(dayType, 'days').format('YYYY-MM-DD');
        const end = moment().format('YYYY-MM-DD');
        this.queryParam.countDateRange = [start, end];
        this.queryParam.countDate_begin = start;
        this.queryParam.countDate_end = end;
      }
    },
    deleteVip(record) {
      this.handleConfrimRequest(this.url.deleteVip, { id: record.vipId }, '是否删除VIP？', `删除玩家: ${record.playerId}（${record.nickname}）的VIP特权`, 'delete');
    },
    addVip(record) {
      this.handleConfrimRequest(this.url.addVip, { playerIds: record.playerId }, '是否添加VIP？', `添加玩家: ${record.playerId}（${record.nickname}）为VIP`);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
