<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="区服">
              <j-search-select-tag placeholder="请选择区服" v-model="queryParam.serverId"
                                   dict="game_server,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="渠道">
              <j-search-select-tag placeholder="请选择渠道" v-model="queryParam.channel"
                                   dict="game_channel,name,simple_name"/>
            </a-form-item>
          </a-col>
          <!-- <a-col :md="4" :sm="8">
            <a-form-item label="支付订单号">
              <a-input placeholder="请输入支付订单号" v-model="queryParam.orderId" />
            </a-form-item>
          </a-col> -->
          <a-col :md="4" :sm="8">
            <a-form-item label="平台订单号">
              <a-input placeholder="请输入平台订单号" v-model="queryParam.queryId"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="商品id">
              <a-input placeholder="请输入商品id" v-model="queryParam.productId"/>
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
          <a-col :md="6" :sm="8">
            <a-form-item label="创建时间">
              <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="金额">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.payAmount_begin"/>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.payAmount_end"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a style="margin-left: 8px" @click="handleToggleSearch">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
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

      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource"
               :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <template slot="htmlSlot" slot-scope="status">
          <div v-html="status"></div>
        </template>
        <template slot="imgSlot" slot-scope="status">
          <span v-if="!status" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(status)" height="25px" alt="图片不存在"
               style="max-width: 80px; font-size: 12px; font-style: italic"/>
        </template>
        <template slot="fileSlot" slot-scope="status">
          <span v-if="!status" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(status)"> 下载
          </a-button>
        </template>

        <span slot="action" slot-scope="status, record">
          <a @click="handleEdit(record)">详情</a>
          <a-divider type="vertical" v-has="'game:vip:admin'"/>
          <a-dropdown v-has="'game:vip:admin'">
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item :disabled="record.vipId > 0"
                           @click="addVip(record)">添加VIP</a-menu-item>
              <a-menu-item :disabled="record.vipId === 0"
                           @click="deleteVip(record)">删除VIP</a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <GameOrderModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {filterObj} from '@/utils/util';
import JInput from '@/components/jeecg/JInput';
import GameOrderModal from './modules/GameOrderModal';

export default {
  name: 'PayOrderList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameOrderModal
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
          title: '区服id',
          align: 'center',
          width: 80,
          dataIndex: 'serverId'
        },
        {
          title: '玩家id',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '玩家名',
          align: 'center',
          width: 80,
          dataIndex: 'nickname',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '账号',
          align: 'center',
          width: 160,
          dataIndex: 'account',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '渠道',
          align: 'center',
          width: 120,
          dataIndex: 'channel',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: 'Sdk渠道',
          align: 'center',
          width: 80,
          dataIndex: 'sdkChannel',
          customRender: (value) => {
            return value || '--';
          }
        },
        // {
        //     title: "支付订单号",
        //     align: "center",
        //     width: 200,
        //     dataIndex: "orderId"
        // },
        {
          title: '商品id',
          align: 'center',
          width: 80,
          dataIndex: 'productId'
        },
        {
          title: '商品名称',
          align: 'center',
          width: 120,
          dataIndex: 'productName'
        },
        {
          title: '平台订单号',
          align: 'left',
          dataIndex: 'queryId'
        },
        {
          title: '支付金额',
          align: 'center',
          width: 80,
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
          width: 80,
          dataIndex: 'orderStatus',
          // <!-- 0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放 -->
          customRender: (status) => {
            let re = '未知';
            if (status === 0) {
              re = '待支付';
            } else if (status === 1) {
              re = '已支付';
            } else if (status === 2) {
              re = '已转发,未回复';
            } else if (status === 3) {
              re = '发放中';
            } else if (status === 4) {
              re = '已发放';
            }
            return re;
          }
        },
        {
          title: "ip地址",
          align: "center",
          dataIndex: "remoteIp",
          customRender: (value) => {
            return value || '--';
          }
        },
        // {
        //     title: "透传参数",
        //     align: "center",
        //     dataIndex: "custom"
        // },
        {
          title: '支付时间',
          align: 'center',
          dataIndex: 'payTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '发货时间',
          align: 'center',
          dataIndex: 'sendTime',
          customRender: (value) => {
            return value || '--';
          }
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
        //   dataIndex: 'createTime'
        // },
        {
          title: '操作',
          width: 120,
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          scopedSlots: {customRender: 'action'}
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
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createDate_begin = dateString[0];
      this.queryParam.createDate_end = dateString[1];
    },
    deleteVip(record) {
      this.handleConfrimRequest(this.url.deleteVip,
        {id: record.vipId},
        '是否删除VIP？',
        `删除玩家: ${record.playerId}（${record.nickname}）的VIP特权`,
        'delete');
    },
    addVip(record) {
      this.handleConfrimRequest(this.url.addVip,
        {playerIds: record.playerId},
        '是否添加VIP？',
        `添加玩家: ${record.playerId}（${record.nickname}）为VIP`);
    },
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
