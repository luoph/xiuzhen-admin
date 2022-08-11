<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="10" :sm="8">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
          </a-col>
          <a-col :md="10" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="玩家名">
              <a-input placeholder="请输入玩家名" v-model="queryParam.userName"></a-input>
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

    <a-button type="primary" icon="download" @click="downlodaExcel('战力')">导出</a-button>
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
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">查看详情</a>
        </span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameForbiddenModal from './modules/GameForbiddenModal';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import { getAction } from '@/api/manage';
import Vue from 'vue';
import { ACCESS_TOKEN } from '@/store/mutation-types';

export default {
  name: 'MilitaryStrengthList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameForbiddenModal,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      description: '战力日志',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: 'id',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '玩家id',
          align: 'center',
          dataIndex: 'userId'
        },
        {
          title: '玩家名',
          align: 'center',
          dataIndex: 'userName'
        },
        {
          title: '战力变更',
          align: 'center',
          dataIndex: 'militaryStrengthChange'
        },
        {
          title: '原战力',
          align: 'center',
          dataIndex: 'originalMilitary'
        },
        {
          title: '新战力',
          align: 'center',
          dataIndex: 'newMilitary'
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'operation'
        },
        {
          title: '时间',
          align: 'center',
          dataIndex: 'time'
          // customRender: function (text) {
          // return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
          // }
        }
      ],
      url: {
        list: 'game/militaryStrength/list',
        exportXlsUrl: 'game/militaryStrength/download',
        downloadExcel: '/game/militaryStrength/download'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {},
    typeChange: function(type) {
      this.queryParam.type = type;
    },
    isForeverChange: function(isForever) {
      this.queryParam.isForever = isForever;
    },
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
        days: this.queryParam.days,
        channelId: this.queryParam.channelId,
        serverId: this.queryParam.serverId,
        rangeDateBegin: this.queryParam.rangeDateBegin,
        rangeDateEnd: this.queryParam.rangeDateEnd,
        pageNo: this.ipagination.current,
        userName: this.queryParam.userName
      };
      getAction(this.url.list, param).then(res => {
        if (res.success) {
          this.dataSource = res.result.records;
          console.log(res.result.records);
          this.ipagination.current = res.result.current;
          this.ipagination.size = res.result.size.toString();
          this.ipagination.total = res.result.total;
          this.ipagination.pages = res.result.pages;
        } else {
          this.$message.error(res.message);
        }
      });
    },
    downlodaExcel(filename) {
      var xhr = new XMLHttpRequest();
      xhr.open('post', window._CONFIG['domianURL'] + this.url.downloadExcel, true);
      xhr.responseType = 'blob';
      xhr.setRequestHeader('Content-Type', 'application/json');
      const token = Vue.ls.get(ACCESS_TOKEN);
      console.log(token);
      xhr.setRequestHeader('X-Access-Token', token);
      xhr.onload = function() {
        var blob = this.response;
        var reader = new FileReader();
        reader.readAsDataURL(blob);
        reader.onload = function(e) {
          var a = document.createElement('a');
          a.download = filename + '.xlsx';
          a.href = e.target.result;
          a.click();
        };
      };
      var a = this.queryParam;
      var param = JSON.stringify(a);
      console.log(param);
      xhr.send(param);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
