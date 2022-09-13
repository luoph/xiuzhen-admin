<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"/>
          </a-col>

          <a-col :md="5" :sm="8">
            <a-form-item label="日期">
              <a-date-picker format="YYYY-MM-DD" :placeholder="'年月日'" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="3" :sm="5">
            <a-form-item label="时间" :label-col="{ span: 12 }" :wrapper-col="{ span: 16 }">
              <a-select placeholder="请选择时间" v-model="hour" :initialValue="hour" @change="onSelectHour">
                <a-select-option v-for="index of 24" :key="index" :value="index">
                  {{ index }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="5">
            <a-form-item label="榜单类型" :label-col="{ span: 12 }" :wrapper-col="{ span: 16 }">
              <a-select placeholder="请选择榜单类型" v-model="rankListType" :initialValue="rankListType"
                        @change="onSelectRankListType">
                <a-select-option v-for="rankListTypeShow in this.rankListTypeShowList" :key="rankListTypeShow.name"
                                 :value="rankListTypeShow.type">
                  {{ rankListTypeShow.type + '-' + rankListTypeShow.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchQuery">刷新数据</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
            <a-button type="primary" icon="download" @click="downloadExcel('玩法参与')">导出</a-button>
        </div> -->

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="rowIndex"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
               style="max-width: 80px; font-size: 12px; font-style: italic"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">查看详情</a>
        </span>
      </a-table>
    </div>

    <gameForbidden-modal ref="modalForm" @ok="modalFormOk"></gameForbidden-modal>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import GameForbiddenModal from './modules/GameForbiddenModal';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import {getAction} from '@/api/manage';

export default {
  name: 'GamePlayMethodsTakePartIn',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameForbiddenModal,
    GameChannelServer,
    getAction
  },
  created() {
    this.getRankListTypeShowList();
  },
  data() {
    return {
      description: '排行榜',
      hour: '',
      rankListType: '',
      rankListTypeShowList: [],
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
          title: '排名',
          align: 'center',
          dataIndex: 'ranking'
        },
        {
          title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '玩家名',
          align: 'center',
          dataIndex: 'playerName'
        },
        {
          title: '境界',
          align: 'center',
          dataIndex: 'realm'
        },
        {
          title: '榜单数值',
          align: 'center',
          dataIndex: 'rankValues'
        }
      ],
      url: {
        list: 'game/gameRankList/list',
        rankListTypeShowList: 'game/commonModule/rankListTypeShowList',
        delete: 'game/gameRankList/delete',
        deleteBatch: 'game/gameRankList/deleteBatch',
        exportXlsUrl: 'game/gameRankList/exportXls',
        importExcelUrl: 'game/gameRankList/importExcel',
        downloadExcel: '/game/playMethodsTakePart/download'
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
    getRankListTypeShowList() {
      getAction(this.url.rankListTypeShowList, {}).then(res => {
        console.log(res);
        this.rankListTypeShowList = res;
      });
    },
    initDictConfig() {
    },
    typeChange: function (type) {
      this.queryParam.type = type;
    },
    isForeverChange: function (isForever) {
      this.queryParam.isForever = isForever;
    },
    onSelectChannel: function (channelId) {
      this.queryParam.channelId = channelId;
    },
    onSelectServer: function (serverId) {
      this.queryParam.serverId = serverId;
    },
    onDateChange: function (value, dateStr) {
      this.queryParam.date = dateStr;
    },
    onSelectRankListType: function (rankListType) {
      console.log(rankListType);
      this.rankListType = rankListType;
    },
    onSelectHour: function (hour) {
      this.hour = hour;
    },
    searchQuery() {
      let param = {
        rankListType: this.rankListType,
        hour: this.hour,
        date: this.queryParam.date,
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
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
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
