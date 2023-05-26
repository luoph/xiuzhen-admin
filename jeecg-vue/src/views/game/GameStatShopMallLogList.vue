<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector ref="channelServerSelector" @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer" />
        <a-row :gutter="45">
          <a-col :md="6" :sm="8">
            <a-form-item label="商店类型">
              <a-select placeholder="商店类型" v-model="queryParam.tabId" showSearch allowClear style="width: 100%">
                <a-select-option :value="0">全部</a-select-option>
                <a-select-option :value="101001">道具（101001）</a-select-option>
                <a-select-option :value="101002">法宝（101002）</a-select-option>
                <a-select-option :value="101004">丹药（101004）</a-select-option>
                <a-select-option :value="101003">荣誉（101003）</a-select-option>
                <a-select-option :value="102301">仙兽（102301）</a-select-option>
                <a-select-option :value="102302">仙兽装备（102302）</a-select-option>
                <a-select-option :value="102303">仙兽材料（102303）</a-select-option>
                <a-select-option :value="105001">元婴（105001）</a-select-option>
                <a-select-option :value="105002">化神（105002）</a-select-option>
                <a-select-option :value="105003">出窍（105003）</a-select-option>
                <a-select-option :value="105004">分神（105004）</a-select-option>
                <a-select-option :value="105005">炼虚（105005）</a-select-option>
                <a-select-option :value="105006">合体（105006）</a-select-option>
                <a-select-option :value="105007">问鼎（105007）</a-select-option>
                <a-select-option :value="105008">大乘（105008）</a-select-option>
                <a-select-option :value="105009">涅槃（105009）</a-select-option>
                <a-select-option :value="105010">生死（105010）</a-select-option>
                <a-select-option :value="105011">轮回（105011）</a-select-option>
                <a-select-option :value="105012">天罡（105012）</a-select-option>
                <a-select-option :value="105013">法相（105013）</a-select-option>
                <a-select-option :value="105014">散仙（105014）</a-select-option>
                <a-select-option :value="105015">真仙（105015）</a-select-option>
                <a-select-option :value="105016">玄仙（105016）</a-select-option>
                <a-select-option :value="105017">天仙（105017）</a-select-option>
                <a-select-option :value="105018">金仙（105018）</a-select-option>
                <a-select-option :value="105019">仙王（105019）</a-select-option>
                <a-select-option :value="105020">仙君（105020）</a-select-option>
                <a-select-option :value="105021">仙尊（105021）</a-select-option>
                <a-select-option :value="105022">仙帝（105022）</a-select-option>
                <a-select-option :value="105023">亚圣（105023）</a-select-option>
                <a-select-option :value="105024">圣人（105024）</a-select-option>
                <a-select-option :value="105025">天尊（105025）</a-select-option>
                <a-select-option :value="105026">天君（105026）</a-select-option>
                <a-select-option :value="105027">天帝（105027）</a-select-option>
                <a-select-option :value="105028">神王（105028）</a-select-option>
                <a-select-option :value="105029">神君（105029）</a-select-option>
                <a-select-option :value="105030">神尊（105030）</a-select-option>
                <a-select-option :value="105031">神帝（105031）</a-select-option>
                <a-select-option :value="105032">神皇（105032）</a-select-option>
                <a-select-option :value="105033">至圣（105033）</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-form-item label="统计时间">
              <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="8">
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
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>

      <div class="table-operator">
        <a-button type="primary" icon="download" @click="handleExportXls('商店销售')">导出</a-button>
      </div>
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
  name: 'GameStatShopMallLogList',
  description: '商城购买日志',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    getAction,
    ChannelServerSelector
  },
  data() {
    return {
      dayRange: 6,
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
          title: '时间',
          align: 'center',
          dataIndex: 'statTime'
        },
        // {
        //   title: '道具',
        //   align: 'center',
        //   dataIndex: 'itemId'
        // },
        {
          title: '商店类型',
          align: 'center',
          dataIndex: 'tabId',
          customRender: (text) => {
            if (text === 101001) {
              return '道具（101001）';
            }
            if (text === 101002) {
              return '法宝（101002）';
            }
            if (text === 101004) {
              return '丹药（101004）';
            }
            if (text === 101003) {
              return '荣誉（101003）';
            }
            if (text === 102301) {
              return '仙兽（102301）';
            }
            if (text === 102302) {
              return '仙兽装备（102302）';
            }
            if (text === 102303) {
              return '仙兽材料（102303）';
            }
            if (text === 105001) {
              return '元婴（105001）';
            }
            if (text === 105002) {
              return '化神（105002）';
            }
            if (text === 105003) {
              return '出窍（105003）';
            }
            if (text === 105004) {
              return '分神（105004）';
            }
            if (text === 105005) {
              return '炼虚（105005）';
            }
            if (text === 105006) {
              return '合体（105006）';
            }
            if (text === 105007) {
              return '问鼎（105007）';
            }
            if (text === 105008) {
              return '大乘（105008）';
            }
            if (text === 105009) {
              return '涅槃（105009）';
            }
            if (text === 105010) {
              return '生死（105010）';
            }
            if (text === 105011) {
              return '轮回（105011）';
            }
            if (text === 105012) {
              return '天罡（105012）';
            }
            if (text === 105013) {
              return '法相（105013）';
            }
            if (text === 105014) {
              return '散仙（105014）';
            }
            if (text === 105015) {
              return '真仙（105015）';
            }
            if (text === 105016) {
              return '玄仙（105016）';
            }
            if (text === 105017) {
              return '天仙（105017）';
            }
            if (text === 105018) {
              return '金仙（105018）';
            }
            if (text === 105019) {
              return '仙王（105019）';
            }
            if (text === 105020) {
              return '仙君（105020）';
            }
            if (text === 105021) {
              return '仙尊（105021）';
            }
            if (text === 105022) {
              return '仙帝（105022）';
            }
            if (text === 105023) {
              return '亚圣（105023）';
            }
            if (text === 105024) {
              return '圣人（105024）';
            }
            if (text === 105025) {
              return '天尊（105025）';
            }
            if (text === 105026) {
              return '天君（105026）';
            }
            if (text === 105027) {
              return '天帝（105027）';
            }
            if (text === 105028) {
              return '神王（105028）';
            }
            if (text === 105029) {
              return '神君（105029）';
            }
            if (text === 105030) {
              return '神尊（105030）';
            }
            if (text === 105031) {
              return '神帝（105031）';
            }
            if (text === 105032) {
              return '神皇（105032）';
            }
            if (text === 105033) {
              return '至圣（105033）';
            }
            return text;
          }
        },
        {
          title: '商品名称',
          align: 'center',
          dataIndex: 'itemName',
          customRender: (text, record) => {
            return record.itemName + '（' + record.itemId + '）';
          }
        },
        {
          title: '商品数量',
          align: 'center',
          dataIndex: 'itemNum'
        },
        {
          title: '货币',
          align: 'center',
          dataIndex: 'costName',
          customRender: (text, record) => {
            return record.costName + '（' + record.costItem + '）';
          }
        },
        {
          title: '货币数量',
          align: 'center',
          dataIndex: 'costNum'
        },
        {
          title: '货币总数量',
          align: 'center',
          dataIndex: 'totalNum'
        },
        {
          title: '购买玩家数',
          align: 'center',
          dataIndex: 'playerNum'
        },
        {
          title: '购买次数',
          align: 'center',
          dataIndex: 'buyNum'
        },
        {
          title: '货币占比',
          align: 'center',
          dataIndex: 'costNumRate',
          customRender: (text, record) => {
            return this.calcRate(record.costNum, record.totalNum);
          }
        },
        {
          title: '次数占比',
          align: 'center',
          dataIndex: 'buyNumRate',
          customRender: (text, record) => {
            return this.calcRate(record.buyNum, record.totalBuyNum);
          }
        }
      ],
      url: {
        list: 'game/stat/shopMallLog/list',
        exportXlsUrl: 'game/stat/shopMallLog/exportXls'
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
    onSelectChannel: function (channel) {
      this.queryParam.channel = channel;
    },
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
    calcRate: function (n, r, t = true) {
      if (n === null || n === undefined) {
        return '--';
      }

      let rate = r > 0 ? parseFloat(n / r) : 0;
      if (t) {
        return Number(parseFloat(rate * 100).toFixed(2)) + '%';
      }
      return Number(parseFloat(rate).toFixed(2));
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
