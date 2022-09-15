<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="45">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"/>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item label="选择就近天数">
              <a-select placeholder="天数" v-model="queryParam.days">
                <a-select-option value="0">不选择天数</a-select-option>
                <a-select-option value="7">近7天</a-select-option>
                <a-select-option value="15">近15天</a-select-option>
                <a-select-option value="30">近一个月</a-select-option>
                <a-select-option value="60">近两个月</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item label="商店类型">
              <a-select placeholder="商店类型" v-model="queryParam.type">
                <a-select-option value="1001">优惠坊市</a-select-option>
                <a-select-option value="100201">时装坊市--头像</a-select-option>
                <a-select-option value="100202">时装坊市--聊天</a-select-option>
                <a-select-option value="100203">时装坊市--名帖</a-select-option>
                <a-select-option value="1003">荣誉坊市</a-select-option>
                <a-select-option value="3001">成仙路</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
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
    <a-row :gutter="45" type="flex">
      <a-col :md="8" :sm="8" v-for="colu in columnsList" :key="colu.id">
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            :rowKey="record => (record.id != null ? record.id : '0')"
            :loading="loading"
            :columns="colu.columns"
            :dataSource="colu.columnsData"
            :pagination="false"
            :scroll="{ x: 'max-content' }"
          >
            <div slot="title" class="word-v-middle">{{ colu.time }}</div>
          </a-table
          >
        </div>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import {getAction} from '@/api/manage';

export default {
  name: 'ShopMallLogList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      columnsList: [],
      description: '商店销售管理页面',
      url: {
        list: 'game/shopMallLog/list'
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
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange: function (value, dateStr) {
      this.queryParam.rangeDateBegin = dateStr[0];
      this.queryParam.rangeDateEnd = dateStr[1];
    },
    searchQuery() {
      let param = {
        days: this.queryParam.days,
        type: this.queryParam.type,
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
        rangeDateBegin: this.queryParam.rangeDateBegin,
        rangeDateEnd: this.queryParam.rangeDateEnd,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      };
      getAction(this.url.list, param).then(res => {
        if (res.success) {
          this.columnsList = [];
          this.dataSource = res.result.records;
          res.result.records.forEach(element => {
            console.log(element.time);
          });

          for (const aa of res.result.records) {
            let a = {
              id: aa.time,
              time: aa.time,
              ipagination: {
                current: 1,
                size: 1,
                total: 1,
                pages: 1
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
                  title: '道具',
                  align: 'center',
                  dataIndex: 'wayName'
                },
                {
                  title: '货币数量',
                  align: 'center',
                  dataIndex: 'itemNum'
                },
                {
                  title: '人数',
                  align: 'center',
                  dataIndex: 'playerNum'
                },
                {
                  title: '次数',
                  align: 'center',
                  dataIndex: 'itemCount'
                },
                {
                  title: '占比',
                  align: 'center',
                  dataIndex: 'itemNumRate',
                  customRender: function (text) {
                    return text * 100 + '%';
                  }
                }
              ],
              columnsData: aa.shopMallLogList
            };
            this.columnsList.push(a);
          }

          this.ipagination.current = res.result.current;
          this.ipagination.size = res.result.size.toString();
          this.ipagination.total = res.result.total;
          this.ipagination.pages = res.result.pages;
        } else {
          this.$message.error(res.message);
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.word-v-middle {
  margin-bottom: 0;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 21px;
  margin-top: 0px;
  color: #0c0c0c;
  white-space: normal;
}
</style>
