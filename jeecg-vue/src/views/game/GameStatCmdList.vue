<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="16" :sm="8">
            <channel-server-selector
              ref="channelServerSelector"
              :show-sdk-channel="true"
              @onSelectChannel="onSelectChannel"
              @onSelectSdkChannel="onSelectSdkChannel"
              @onSelectServer="onSelectServer"
            />
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="8">
            <a-form-item label="玩家ID">
              <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="8">
            <a-form-item label="消息ID">
              <a-input placeholder="请输入消息ID" v-model="queryParam.msgId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="耗时">
              <a-input placeholder="最短耗时" class="query-group-cust" v-model="queryParam.costTime_begin" />
              <span class="query-group-split-cust" />
              <a-input placeholder="最长耗时" class="query-group-cust" v-model="queryParam.costTime_end" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="次数">
              <a-input placeholder="最小次数" class="query-group-cust" v-model="queryParam.num_begin" />
              <span class="query-group-split-cust" />
              <a-input placeholder="最大次数" class="query-group-cust" v-model="queryParam.num_end" />
            </a-form-item>
          </a-col>
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
          <a-col :md="8" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="danger" icon="sync" style="margin-left: 8px" @click="onClickUpdate">刷新</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!--查询区域结束-->
    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="costTimeSlot" slot-scope="text">
          <a-tag v-if="text >= 1000" color="red" class="ant-tag-no-margin">{{ text }}</a-tag>
          <a-tag v-else-if="text >= 200" color="orange" class="ant-tag-no-margin">{{ text }}</a-tag>
          <a-tag v-else color="green" class="ant-tag-no-margin">{{ text }}</a-tag>
        </span>
        <span slot="msgIdTitle">消息ID <a-icon type="copy" /></span>
        <span slot="msgNameTitle">接口名 <a-icon type="copy" /></span>
        <span slot="serverIdTitle">区服 <a-icon type="copy" /></span>
        <span slot="playerIdTitle">玩家ID <a-icon type="copy" /></span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import { filterObj } from '@/utils/util';
import { getAction } from '@/api/manage';
import ChannelServerSelector from '@comp/gameserver/ChannelServerSelector';
import moment from 'moment';
export default {
  description: '接口耗时统计',
  name: 'GameStatCmdList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      dayRange: 0,
      columns: [
        {
          title: '#',
          dataIndex: '',
          align: 'center',
          width: '60',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          dataIndex: 'createDate',
          align: 'center',
          width: '140',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          // title: '区服',
          align: 'center',
          width: '80',
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '消息ID',
          align: 'center',
          dataIndex: 'msgId',
          slots: { title: 'msgIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '接口名',
          align: 'center',
          dataIndex: 'msgName',
          slots: { title: 'msgNameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '耗时（ms）',
          align: 'center',
          dataIndex: 'costTime',
          scopedSlots: { customRender: 'costTimeSlot' }
        },
        {
          title: '次数',
          align: 'center',
          dataIndex: 'num'
        },
        {
          // title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId',
          slots: { title: 'playerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '时间',
          align: 'center',
          dataIndex: 'createTime'
        }
      ],
      url: {
        list: 'game/stat/cmd/list',
        update: 'game/stat/cmd/update'
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
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
    onResetParams() {
      this.$refs.channelServerSelector.reset();
      this.dayRange = 0;
    },
    onDateChange(date, dateString) {
      this.queryParam.createDate_begin = dateString[0];
      this.queryParam.createDate_end = dateString[1];
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
    onClickUpdate() {
      // 查询条件
      const params = this.getQueryParams();
      this.loading = true;
      getAction(this.url.update, params, this.timeout)
        .then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message);
          }
        })
        .finally(() => {
          this.loading = false;
          this.searchQuery();
        });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.copy-text {
  white-space: nowrap;
  color: rgba(0, 0, 0, 0.65);
}

.ant-tag-no-margin {
  margin-right: auto !important;
}
</style>
