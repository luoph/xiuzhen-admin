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
          <a-col :md="4" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="角色名">
              <j-input placeholder="请输入角色名模糊查询" v-model="queryParam.name" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="帐号">
              <j-input placeholder="请输入帐号模糊查询" v-model="queryParam.account" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="注册IP">
              <a-input placeholder="请输入ip" v-model="queryParam.ip" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
              </a-form-item>
            </a-col>
            <!-- <a-col :md="4" :sm="8">
                            <a-form-item label="version_name">
                                <a-input placeholder="请输入version_name" v-model="queryParam.versionName"/>
                            </a-form-item>
                        </a-col> -->
            <!-- <a-col :md="4" :sm="8">
                            <a-form-item label="version_code">
                                <a-input placeholder="请输入version_code" v-model="queryParam.versionCode"/>
                            </a-form-item>
                        </a-col> -->
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a style="margin-left: 8px" @click="handleToggleSearch">
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
      <!-- <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('玩家注册信息')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div> -->

      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
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
          <!-- <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown> -->
        </span>
      </a-table>
    </div>

    <GameRegisterInfoModal ref="modalForm" @ok="modalFormOk"></GameRegisterInfoModal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';
import GameRegisterInfoModal from './modules/GameRegisterInfoModal';
import JDate from '@/components/jeecg/JDate.vue';
import JInput from '@/components/jeecg/JInput';
import { filterObj } from '@/utils/util';

export default {
  name: 'GameRegisterInfoList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    JInput,
    GameRegisterInfoModal,
    ChannelServerSelector
  },
  data() {
    return {
      description: '玩家注册信息管理页面',
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
          title: '区服id',
          align: 'center',
          width: 100,
          dataIndex: 'serverId'
        },
        {
          title: '玩家id',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '角色名',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '帐号',
          align: 'center',
          dataIndex: 'account'
        },
        {
          title: '注册IP',
          align: 'center',
          dataIndex: 'ip'
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
          title: '手机品牌',
          align: 'center',
          dataIndex: 'vendor'
        },
        {
          title: '手机型号',
          align: 'center',
          dataIndex: 'model'
        },
        {
          title: '系统名字',
          align: 'center',
          dataIndex: 'system'
        },
        {
          title: '系统版本',
          align: 'center',
          dataIndex: 'systemVersion'
        },
        {
          title: '网络类型',
          align: 'center',
          dataIndex: 'network'
        },
        {
          title: '版本名称',
          align: 'center',
          dataIndex: 'versionName'
        },
        // {
        //     title: "版本号",
        //     align: "center",
        //     dataIndex: "versionCode"
        // },
        // {
        //     title: "平台",
        //     align: "center",
        //     dataIndex: "platform"
        // },
        {
          title: '创建时间',
          align: 'center',
          width: 200,
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 120,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'player/registerInfo/list',
        exportXlsUrl: 'player/registerInfo/exportXls'
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
    getQueryParams() {
      console.log(this.queryParam.createTimeRange);
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createTimeRange;
      return filterObj(param);
    },
    onSelectChannel: function (value) {
      this.queryParam.channel = value;
    },
    onSelectSdkChannel: function (value) {
      this.queryParam.sdkChannel = value;
    },
    onSelectServer: function (value) {
      this.queryParam.serverId = value;
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
    },
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
    onDateOk(value) {
      console.log(value);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
