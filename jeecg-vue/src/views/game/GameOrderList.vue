<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <channel-server-selector
          ref="channelServerSelector"
          :multi-channel="true"
          :multi-sdk-channel="true"
          :multi-server="true"
          :show-sdk-channel="true"
          @onSelectChannel="onSelectChannel"
          @onSelectSdkChannel="onSelectSdkChannel"
          @onSelectServer="onSelectServer"
        />
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="玩家ID">
              <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId" />
            </a-form-item>
          </a-col>
          <!-- <a-col :md="4" :sm="8">
            <a-form-item label="支付订单号">
              <a-input placeholder="请输入支付订单号" v-model="queryParam.orderId" />
            </a-form-item>
          </a-col> -->
          <a-col :md="6" :sm="8">
            <a-form-item label="平台订单号">
              <a-input placeholder="请输入平台订单号" v-model="queryParam.queryId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="商品ID">
              <a-input placeholder="请输入商品ID" v-model="queryParam.productId" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="订单状态">
              <!-- 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放 -->
              <a-select v-model="queryParam.orderStatus" placeholder="请选择订单状态">
                <a-select-option value="">请选择订单状态</a-select-option>
                <a-select-option value="0">待支付</a-select-option>
                <a-select-option value="1">已支付</a-select-option>
                <a-select-option value="2">已转发</a-select-option>
                <a-select-option value="3">发放中</a-select-option>
                <a-select-option value="4">已发放</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="金额">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.payAmount_begin" />
                <span class="query-group-split-cust" />
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.payAmount_end" />
              </a-form-item>
            </a-col>
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
      <a-button type="primary" icon="download" @click="handleExportXls('充值订单')">导出</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div> -->

      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="idTagSlot" slot-scope="text">
          <a-tag :key="text" :color="tagColor(text)" @click="copyText(text)">{{ text }}</a-tag>
        </span>
        <div slot="serverIdsSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span" @click="copyText(tag)">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else v-for="tag in text.split(',').sort().reverse()" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="orderStatusSlot" slot-scope="text">
          <!-- 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放 -->
          <a-tag v-if="text === 0" color="gray">待支付</a-tag>
          <a-tag v-else-if="text === 1" color="blue">已支付</a-tag>
          <a-tag v-else-if="text === 2" color="orange">已转发</a-tag>
          <a-tag v-else-if="text === 3" color="red">发放中</a-tag>
          <a-tag v-else-if="text === 4" color="green">已发放</a-tag>
          <a-tag v-else>未知</a-tag>
        </span>
        <span slot="accountSlot" slot-scope="text" @click="copyText(text)" style="min-width: 100px">
          {{ text || '--' }}
        </span>
        <span slot="productSlot" slot-scope="text, record" @click="copyText(text)" style="min-width: 100px"> {{ record.productName }}（{{ text }}） </span>
        <span slot="orderIdSlot" slot-scope="text" @click="copyText(text)" style="min-width: 140px">
          {{ text || '--' }}
        </span>
        <div slot="largeTextSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span" @click="copyText(text)">{{ text || '--' }}</span>
        </div>
        <span slot="playerSlot" slot-scope="text, record" style="white-space: nowrap">
          <a-tag>ID</a-tag><a @click="copyText(record.playerId)" class="copy-text"> {{ record.playerId }} <a-icon type="copy" /></a>
          <a-divider />
          <a-tag>昵称</a-tag><a @click="copyText(record.nickname)" class="copy-text"> {{ record.nickname }} <a-icon type="copy" /></a>
        </span>
        <span slot="action" slot-scope="status, record">
          <a @click="handleEdit(record)">详情</a>
          <a-divider type="vertical" v-has="'game:vip:admin'" />
          <a-dropdown v-has="'game:vip:admin'">
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item :disabled="record.vipId > 0" @click="addVip(record)">添加VIP</a-menu-item>
              <a-menu-item :disabled="record.vipId === 0" @click="deleteVip(record)">删除VIP</a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <span slot="serverIdTitle" class="copy-text">区服ID <a-icon type="copy" /></span>
        <span slot="playerIdTitle" class="copy-text">玩家ID <a-icon type="copy" /></span>
        <span slot="nicknameTitle" class="copy-text">角色名 <a-icon type="copy" /></span>
        <span slot="accountTitle" class="copy-text">账号 <a-icon type="copy" /></span>
        <span slot="channelTitle" class="copy-text">渠道 <a-icon type="copy" /></span>
        <span slot="sdkChannelTitle" class="copy-text">Sdk渠道 <a-icon type="copy" /></span>
        <span slot="productIdTitle" class="copy-text">商品ID <a-icon type="copy" /></span>
        <span slot="productNameTitle" class="copy-text">商品 <a-icon type="copy" /></span>
        <span slot="queryIdTitle" class="copy-text">平台订单号 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <GameOrderModal ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { filterObj } from '@/utils/util';
import JInput from '@/components/jeecg/JInput';
import GameOrderModal from './modules/GameOrderModal';
import ChannelServerSelector from '@/components/gameserver/ChannelServerSelector';

export default {
  name: 'PayOrderList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameOrderModal,
    ChannelServerSelector
  },
  data() {
    return {
      description: '充值订单管理页面',
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
          title: 'ID',
          align: 'center',
          dataIndex: 'id',
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '区服ID',
          align: 'center',
          dataIndex: 'serverId',
          slots: { title: 'serverIdTitle' },
          scopedSlots: { customRender: 'idTagSlot' }
        },
        // {
        //   title: '玩家',
        //   align: 'center',
        //   dataIndex: 'playerId',
        //   scopedSlots: { customRender: 'playerSlot' }
        // },
        {
          // title: '玩家ID',
          align: 'center',
          dataIndex: 'playerId',
          slots: { title: 'playerIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '玩家名',
          align: 'center',
          dataIndex: 'nickname',
          slots: { title: 'nicknameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '账号',
          // width: 120,
          align: 'center',
          dataIndex: 'account',
          slots: { title: 'accountTitle' },
          scopedSlots: { customRender: 'accountSlot' }
        },
        {
          // title: '渠道',
          align: 'center',
          dataIndex: 'channel',
          slots: { title: 'channelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: 'Sdk渠道',
          align: 'center',
          dataIndex: 'sdkChannel',
          slots: { title: 'sdkChannelTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '商品',
          align: 'center',
          dataIndex: 'productId',
          slots: { title: 'productIdTitle' },
          scopedSlots: { customRender: 'productSlot' }
        },
        // {
        //   // title: '商品名称',
        //   align: 'center',
        //   width: 120,
        //   dataIndex: 'productName',
        //   slots: { title: 'productNameTitle' },
        //   scopedSlots: { customRender: 'copySlot' }
        // },
        // {
        //     title: "支付订单号",
        //     align: "center",
        //     width: 200,
        //     dataIndex: "orderId"
        // },
        {
          // title: '平台订单号',
          dataIndex: 'queryId',
          slots: { title: 'queryIdTitle' },
          scopedSlots: { customRender: 'orderIdSlot' }
        },
        {
          title: '支付金额',
          align: 'center',
          dataIndex: 'payAmount'
        },
        // {
        //   title: '订单金额',
        //   align: 'center',
        //   width: 80,
        //   dataIndex: 'orderAmount'
        // },
        // {
        //   title: '折扣金额',
        //   align: 'center',
        //   width: 80,
        //   dataIndex: 'discountAmount'
        // },
        {
          title: '订单状态',
          align: 'center',
          dataIndex: 'orderStatus',
          scopedSlots: { customRender: 'orderStatusSlot' }
        },
        // {
        //   title: 'ip地址',
        //   align: 'center',
        //   dataIndex: 'remoteIp',
        //   scopedSlots: { customRender: 'copySlot' }
        // },
        // {
        //     title: "透传参数",
        //     align: "center",
        //     dataIndex: "custom"
        // },
        {
          title: '支付时间',
          align: 'center',
          dataIndex: 'payTime'
        },
        {
          title: '发货时间',
          align: 'center',
          dataIndex: 'sendTime'
        },
        // },
        // {
        //     title: "充值货币",
        //     align: "center",
        //     dataIndex: "currency"
        // },
        // {
        //   title: '创建时间',
        //   align: 'center',
        //   width: 120,
        //   dataIndex: 'createTime',
        //   scopedSlots: { customRender: 'textSlot' }
        // },
        {
          title: '操作',
          width: 120,
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/order/list',
        addVip: 'game/vip/addVip',
        deleteVip: 'game/vip/delete',
        exportXlsUrl: 'game/order/exportXls'
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
      console.log(this.queryParam.createDateRange);
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createDateRange;
      return filterObj(param);
    },
    onSelectChannel: function (value) {
      this.queryParam.channel = value;
    },
    onSelectSdkChannel: function (value) {
      this.queryParam.sdkChannel = value;
    },
    onSelectServer: function (value) {
      this.queryParam.serverIds = value;
    },
    onResetParams() {
      this.$refs.channelServerSelector.reset();
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

.ant-divider-horizontal {
  margin: 6px 0 6px 0;
}

.scroll-container {
  min-width: 140px;
  max-width: 320px;
}
</style>
