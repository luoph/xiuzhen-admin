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
          <a-col :md="8" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="日期范围">
              <a-radio-group v-model="dayRange" @change="onDayRangeChange">
                <a-radio :value="0">自定义</a-radio>
                <a-radio :value="6">近7天</a-radio>
                <a-radio :value="14">近15天</a-radio>
                <a-radio :value="29">近1月</a-radio>
                <a-radio :value="59">近2月</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!--查询区域结束-->
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
      />
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import { getAction } from '@/api/manage';
import { filterObj } from '@/utils/util';
import moment from 'moment';
import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';

export default {
  description: '新增转化数据',
  name: 'GameStatConversionList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    ChannelServerSelector,
    getAction
  },
  data() {
    return {
      /* 排序参数 */
      isorter: {
        column: 'countDate',
        order: 'desc'
      },
      dayRange: 6,
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: 80,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          dataIndex: 'countDate',
          width: 100,
          align: 'center',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          title: '渠道',
          dataIndex: 'channel',
          width: 80,
          align: 'center'
        },
        {
          title: '区服',
          dataIndex: 'serverId',
          width: 80,
          align: 'center',
          customRender: function (text) {
            return text === 0 ? '全部' : text;
          }
        },
        {
          title: '新增账号',
          dataIndex: 'newAccountNum',
          align: 'center',
          width: 80
        },
        {
          title: '新增角色',
          dataIndex: 'newPlayerNum',
          width: 80,
          align: 'center'
        },
        {
          title: '新增付费角色数',
          dataIndex: 'newPlayerPayNum',
          width: 80,
          align: 'center'
        },
        {
          title: '账号角色转化率',
          dataIndex: 'newConversionRate',
          align: 'center',
          width: 80,
          customRender: function (text) {
            return text + '%';
          }
        },
        {
          title: '新增付费率',
          dataIndex: 'newPlayerPayRate',
          align: 'center',
          width: 80,
          customRender: function (text) {
            return text + '%';
          }
        }
      ],
      url: {
        list: 'game/stat/conversion/list'
      },
      dictOptions: {}
    };
  },
  computed: {},
  created() {},
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
      if (this.dayRange >= 0) {
        this.selectDayRange(this.dayRange);
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
      this.dayRange = 6;
    },
    onDateChange(date, dateString) {
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
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
        this.queryParam.countDateRange = [start, end];
        this.queryParam.countDate_begin = start;
        this.queryParam.countDate_end = end;
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
</style>
