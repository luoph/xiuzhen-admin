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
          <a-col :md="5" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="8">
            <a-form-item label="创角区服">
              <j-search-select-tag placeholder="请选择创角区服" v-model="queryParam.sid" dict="game_server,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="昵称">
              <j-input placeholder="请输入昵称模糊查询" v-model="queryParam.nickname" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="账号">
              <j-input placeholder="请输入账号模糊查询" v-model="queryParam.account" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="境界">
              <a-select placeholder="请选择境界" v-model="queryParam.realm" showSearch allowClear style="width: 100%">
                <a-select-option :value="1">人界 [1]</a-select-option>
                <a-select-option :value="2">灵界 [2]</a-select-option>
                <a-select-option :value="3">仙界 [3]</a-select-option>
                <a-select-option :value="4">圣界 [4]</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="状态">
                <a-select placeholder="请选择状态" v-model="queryParam.status" showSearch allowClear style="width: 100%">
                  <a-select-option :value="1">有效</a-select-option>
                  <a-select-option :value="0">无效</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="等级">
                <a-input placeholder="最小等级" class="query-group-cust" v-model="queryParam.level_begin" />
                <span class="query-group-split-cust"></span>
                <a-input placeholder="最大等级" class="query-group-cust" v-model="queryParam.level_end" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="16">
              <a-form-item label="战力范围">
                <a-input placeholder="最小战力值" class="query-group-cust" v-model="queryParam.combatPower_begin" />
                <span class="query-group-split-cust"></span>
                <a-input placeholder="最大战力值" class="query-group-cust" v-model="queryParam.combatPower_end" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <a-button type="primary" icon="download" @click="handleExportXls('玩家信息')">导出</a-button>
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
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
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
        <span slot="strikeText" slot-scope="text, record">
          <span v-if="record.status === 0" style="color: red">
            <s>{{ text }}</s>
          </span>
          <span v-else>{{ text }}</span>
        </span>
        <span slot="statusSlot" slot-scope="text, record">
          <a-tag v-if="text === 0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
        </span>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">更多<a-icon type="down" /></a>
            <a-menu slot="overlay" v-has="'game:vip:admin'">
              <a-menu-item :disabled="record.vipId > 0" @click="addVip(record)">添加VIP</a-menu-item>
              <a-menu-item :disabled="record.vipId === 0" @click="deleteVip(record)">删除VIP</a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <GamePlayerModal ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import JInput from '@/components/jeecg/JInput';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import { getAction } from '@/api/manage';

import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';
import GamePlayerModal from './modules/GamePlayerModal';

export default {
  name: 'GamePlayerList',
  mixins: [JeecgListMixin],
  components: {
    GamePlayerModal,
    ChannelServerSelector,
    getAction,
    JInput
  },
  data() {
    return {
      description: '玩家信息管理页面',
      queryParam: {
        serverId: ''
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
          title: '玩家id',
          align: 'center',
          dataIndex: 'playerId',
          scopedSlots: { customRender: 'strikeText' }
        },
        {
          title: '角色昵称',
          align: 'center',
          dataIndex: 'nickname',
          scopedSlots: { customRender: 'strikeText' }
        },
        {
          title: '账号',
          align: 'center',
          dataIndex: 'account'
        },
        {
          title: '区服id',
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
          dataIndex: 'sdkChannel',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '状态',
          align: 'center',
          width: 60,
          dataIndex: 'status',
          scopedSlots: { customRender: 'statusSlot' }
        },
        {
          title: '等级',
          align: 'center',
          width: 60,
          dataIndex: 'level'
        },
        // {
        //   title: '境界',
        //   align: 'center',
        //   dataIndex: 'realm',
        //   customRender: (value) => {
        //     let text = '--';
        //     if (value === 1) {
        //       text = '人界 [1]';
        //     } else if (value === 2) {
        //       text = '灵界 [2]';
        //     } else if (value === 3) {
        //       text = '仙界 [3]';
        //     } else if (value === 4) {
        //       text = '圣界 [4]';
        //     }
        //     return text;
        //   }
        // },
        {
          title: '累计充值',
          align: 'center',
          dataIndex: 'totalPayAmount'
        },
        {
          title: '主线任务id',
          align: 'center',
          dataIndex: 'mainTaskId'
        },
        // {
        //   title: '跳过动画',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'skipCartoon'
        // },
        // {
        //   title: '背包大小',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'backpackSize'
        // },
        // {
        //   title: '背包等级',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'backpackLevel'
        // },
        // {
        //   title: '修为值',
        //   align: 'center',
        //   dataIndex: 'practiceValue'
        // },
        // {
        //   title: '修炼年数',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'practiceYear'
        // },
        {
          title: '战力',
          align: 'center',
          dataIndex: 'combatPower'
        },
        // {
        //   title: '魅力值',
        //   align: 'center',
        //   dataIndex: 'charmValue'
        // },
        // {
        //   title: '本命灵根',
        //   align: 'center',
        //   dataIndex: 'spiritRootCode'
        // },
        // {
        //   title: '修为加持状态',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'practiceState'
        // },
        // {
        //   title: '渡劫增加成功率',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'successRate'
        // },
        {
          title: '创角时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '修为结算时间',
          align: 'center',
          dataIndex: 'settleTime'
        },
        {
          title: '等级升级时间',
          align: 'center',
          dataIndex: 'levelUpdateTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '登录时间',
          align: 'center',
          dataIndex: 'loginTime'
        },
        {
          title: '登录IP',
          align: 'center',
          dataIndex: 'loginIp',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/player/list',
        addVip: 'game/vip/addVip',
        deleteVip: 'game/vip/delete',
        exportXlsUrl: 'game/player/exportXls'
      },
      dictOptions: {}
    };
  },
  created() {},
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    getQueryParams() {
      console.log(this.queryParam.createDateRange);
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createDateRange;
      return filterObj(param);
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
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
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createDate_begin = dateString[0];
      this.queryParam.createDate_end = dateString[1];
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
