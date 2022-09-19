<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <channel-server-selector @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"/>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="统计日期">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onCountDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
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
      <a-table
        ref="table"
        size="middle"
        bordered
        :rowKey="record => (record.id != null ? record.id : '0')"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :scroll="{ x: 1500, y: 800 }"
        @change="handleTableChange"
      ></a-table>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';
import {getAction} from '@/api/manage';
import {filterObj} from "@/utils/util";

export default {
  description: '日常数据',
  name: 'GameDataCountList',
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
        order: 'desc',
      },
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: '4%',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          dataIndex: 'countDate',
          width: '6%',
          align: 'center',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          title: '渠道',
          dataIndex: 'channel',
          width: '6%',
          align: 'center',
        },
        {
          title: '区服',
          dataIndex: 'serverId',
          width: '6%',
          align: 'center',
          customRender: function (text) {
            return text === 0 ? '全部' : text;
          }
        },
        {
          title: '每天数据',
          children: [
            {
              title: '当天登录角色数',
              dataIndex: 'loginNum',
              align: 'center',
              width: '5%'
            },
            {
              title: '当天付费金额',
              dataIndex: 'payAmount',
              align: 'center',
              width: '5%'
            },
            {
              title: '当天付费角色数',
              dataIndex: 'payPlayerNum',
              align: 'center',
              width: '5%'
            },
            {
              title: '当天付费率',
              dataIndex: 'payRate',
              align: 'center',
              width: '5%',
              customRender: function (text) {
                return text + '%';
              }
            },
            {
              title: 'ARPU',
              dataIndex: 'arpu',
              align: 'center',
              width: '5%'
            },
            {
              title: 'ARPPU',
              dataIndex: 'arppu',
              align: 'center',
              width: '5%'
            }
          ]
        },
        {
          title: '新增数据',
          children: [
            {
              title: '新增角色',
              dataIndex: 'newPlayerNum',
              align: 'center',
              width: '5%'
            },
            {
              title: '新增付费',
              dataIndex: 'newPlayerPayAmount',
              align: 'center',
              width: '5%'
            },
            {
              title: '新增付费角色数',
              dataIndex: 'newPlayerPayNum',
              align: 'center',
              width: '5%'
            },
            {
              title: '新增付费率',
              dataIndex: 'newPlayerPayRate',
              align: 'center',
              width: '5%',
              customRender: function (text) {
                return text + '%';
              }
            },
            {
              title: '新增ARPU',
              dataIndex: 'newPlayerArpu',
              align: 'center',
              width: '5%'
            },
            {
              title: '新增ARPPU',
              dataIndex: 'newPlayerArppu',
              align: 'center',
              width: '5%'
            },
            {
              title: '二次付费角色数',
              dataIndex: 'doublePay',
              align: 'center',
              width: '5%'
            },
            {
              title: '二次付费率',
              dataIndex: 'doublePayRate',
              align: 'center',
              width: '5%',
              customRender: function (text) {
                return text + '%';
              }
            }
          ]
        }
      ],
      url: {
        list: 'game/stat/daily',
        update: 'game/stat/update',
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
    onSelectChannel: function (channel) {
      this.queryParam.channel = channel;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    onCountDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.countDate_begin = dateString[0];
      this.queryParam.countDate_end = dateString[1];
    },
    getQueryParams() {
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;

      // 范围参数不传递后台
      delete param.countDateRange;
      return filterObj(param);
    },
    onClickUpdate() {
      // 查询条件
      const params = this.getQueryParams();
      this.loading = true;
      getAction(this.url.update, params, this.timeout).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
        this.searchQuery();
      })
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
