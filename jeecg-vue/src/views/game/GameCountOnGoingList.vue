<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item label="统计类型">
              <a-select placeholder="统计类型" v-model="queryParam.type">
                <a-select-option :value="1">留存</a-select-option>
                <a-select-option :value="2">LTV</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
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
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
      ></a-table>
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import { getAction } from '@/api/manage';

export default {
  description: '留存率',
  name: 'GameDataRemainList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: '100',
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          dataIndex: 'countDate',
          width: '120',
          align: 'center',
          customRender: function(text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
          }
        },
        {
          title: '新增角色',
          dataIndex: 'registerNum',
          align: 'center',
          width: '120'
        },
        {
          title: '类型',
          dataIndex: 'type',
          align: 'center',
          width: '120',
          customRender: function(text) {
            return text === 1 ? '留存' : 'LTV';
          }
        },
        {
          title: '1日留存/LTV',
          dataIndex: 'c1',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c1, record.registerNum, record.type);
          }
        },
        {
          title: '2日留存/LTV',
          dataIndex: 'c2',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c2, record.registerNum, record.type);
          }
        },
        {
          title: '3日留存/LTV',
          dataIndex: 'c3',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c3, record.registerNum, record.type);
          }
        },
        {
          title: '4日留存/LTV',
          dataIndex: 'c4',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c4, record.registerNum, record.type);
          }
        },
        {
          title: '5日留存/LTV',
          dataIndex: 'c5',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c5, record.registerNum, record.type);
          }
        },
        {
          title: '6日留存/LTV',
          dataIndex: 'c6',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c6, record.registerNum, record.type);
          }
        },
        {
          title: '7日留存/LTV',
          dataIndex: 'c7',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c7, record.registerNum, record.type);
          }
        },
        {
          title: '8日留存/LTV',
          dataIndex: 'c8',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c8, record.registerNum, record.type);
          }
        },
        {
          title: '9日留存/LTV',
          dataIndex: 'c9',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c9, record.registerNum, record.type);
          }
        },
        {
          title: '10日留存/LTV',
          dataIndex: 'c10',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c10, record.registerNum, record.type);
          }
        },
        {
          title: '11日留存/LTV',
          dataIndex: 'c11',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c11, record.registerNum, record.type);
          }
        },
        {
          title: '12日留存/LTV',
          dataIndex: 'c12',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c12, record.registerNum, record.type);
          }
        },
        {
          title: '13日留存/LTV',
          dataIndex: 'c13',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c13, record.registerNum, record.type);
          }
        },
        {
          title: '14日留存/LTV',
          dataIndex: 'c14',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c14, record.registerNum, record.type);
          }
        },
        {
          title: '15日留存/LTV',
          dataIndex: 'c15',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c15, record.registerNum, record.type);
          }
        },
        {
          title: '16日留存/LTV',
          dataIndex: 'c16',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c16, record.registerNum, record.type);
          }
        },
        {
          title: '17日留存/LTV',
          dataIndex: 'c17',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c17, record.registerNum, record.type);
          }
        },
        {
          title: '18日留存/LTV',
          dataIndex: 'c18',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c18, record.registerNum, record.type);
          }
        },
        {
          title: '19日留存/LTV',
          dataIndex: 'c19',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c19, record.registerNum, record.type);
          }
        },
        {
          title: '20日留存/LTV',
          dataIndex: 'c20',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c20, record.registerNum, record.type);
          }
        },
        {
          title: '21日留存/LTV',
          dataIndex: 'c21',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c21, record.registerNum, record.type);
          }
        },
        {
          title: '22日留存/LTV',
          dataIndex: 'c22',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c22, record.registerNum, record.type);
          }
        },
        {
          title: '23日留存/LTV',
          dataIndex: 'c23',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c23, record.registerNum, record.type);
          }
        },
        {
          title: '24日留存/LTV',
          dataIndex: 'c24',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c24, record.registerNum, record.type);
          }
        },
        {
          title: '25日留存/LTV',
          dataIndex: 'c25',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c25, record.registerNum, record.type);
          }
        },
        {
          title: '26日留存/LTV',
          dataIndex: 'c26',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c26, record.registerNum, record.type);
          }
        },
        {
          title: '27日留存/LTV',
          dataIndex: 'c27',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c27, record.registerNum, record.type);
          }
        },
        {
          title: '28日留存/LTV',
          dataIndex: 'c28',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c28, record.registerNum, record.type);
          }
        },
        {
          title: '29日留存/LTV',
          dataIndex: 'c29',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c29, record.registerNum, record.type);
          }
        },
        {
          title: '30日留存/LTV',
          dataIndex: 'c30',
          align: 'center',
          width: '120',
          customRender: (text, record) => {
            return this.countRate(record.c30, record.registerNum, record.type);
          }
        }
      ],
      url: {
        list: 'game/statistics/ongoing'
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
    initDictConfig() {},
    onSelectChannel: function(channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function(serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange: function(value, dateStr) {
      this.queryParam.rangeDateBegin = dateStr[0];
      this.queryParam.rangeDateEnd = dateStr[1];
    },
    searchQuery() {
      let param = {
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
        rangeDateBegin: this.queryParam.rangeDateBegin,
        rangeDateEnd: this.queryParam.rangeDateEnd,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        type: this.queryParam.type
      };
      getAction(this.url.list, param).then(res => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.current = res.result.current;
          this.ipagination.size = res.result.size.toString();
          this.ipagination.total = res.result.total;
          this.ipagination.pages = res.result.pages;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    countRate: function(n, r, t) {
      if (n === null || n === undefined) {
        return '--';
      }

      let rate = r > 0 ? parseFloat(n / r) : 0;
      if (t === 1) {
        return Number(parseFloat(rate * 100).toFixed(2)) + '%';
      } else {
        return Number(parseFloat(rate).toFixed(2));
      }
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
