<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="商品ID">
              <a-input placeholder="请输入商品ID" v-model="queryParam.goodsId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="商品名称">
              <j-input placeholder="商品名称模糊查询" v-model="queryParam.name" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="SKU">
              <j-input placeholder="请输入SKU模糊查询" v-model="queryParam.sku" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="商品组别">
              <a-select placeholder="请选择商品组别" v-model="queryParam.goodsGroup" initialValue="1">
                <a-select-option :value="1">直充</a-select-option>
                <a-select-option :value="2">礼包</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="商品类型">
              <a-select placeholder="请选择商品类型" v-model="queryParam.goodsType" initialValue="1" showSearch allowClear style="width: 100%">
                <a-select-option :value="0">0-仙玉</a-select-option>
                <a-select-option :value="1">1-仙职</a-select-option>
                <a-select-option :value="2">2-月卡</a-select-option>
                <a-select-option :value="3">3-每日礼包</a-select-option>
                <a-select-option :value="4">4-首充</a-select-option>
                <a-select-option :value="5">5-周卡</a-select-option>
                <a-select-option :value="6">6-六道剑阵</a-select-option>
                <a-select-option :value="7">7-招财进宝/仙力护符</a-select-option>
                <a-select-option :value="8">8-高级天道令</a-select-option>
                <a-select-option :value="9">9-节日派对</a-select-option>
                <a-select-option :value="10">10-直购礼包</a-select-option>
                <a-select-option :value="11">11-精准礼包</a-select-option>
                <a-select-option :value="12">12-结义礼包</a-select-option>
                <a-select-option :value="13">13-自选礼包</a-select-option>
                <a-select-option :value="14">14-灵兽抽奖-礼包</a-select-option>
                <a-select-option :value="15">15-开服目标活动-签到令</a-select-option>
                <a-select-option :value="16">16-开服目标活动-任务礼包</a-select-option>
                <a-select-option :value="17">17-系统直购礼包</a-select-option>
                <a-select-option :value="18">18-成长基金</a-select-option>
                <a-select-option :value="19">19-节日活动-夺宝战令</a-select-option>
                <a-select-option :value="20">20-新战令</a-select-option>
                <a-select-option :value="21">21-超值礼包</a-select-option>
                <a-select-option :value="22">22-神游特权卡</a-select-option>
                <a-select-option :value="23">23-GM特权卡</a-select-option>
                <a-select-option :value="24">24-无限真充</a-select-option>
                <a-select-option :value="25">25-无限神充</a-select-option>
                <a-select-option :value="26">26-GM充值工具-每日礼包</a-select-option>
                <a-select-option :value="27">27-GM充值工具-GM专属资源礼包</a-select-option>
                <a-select-option :value="28">28-仙缘神通-礼包</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <!--          <template v-if="toggleSearchStatus">-->
          <!--            <a-col :md="4" :sm="8">-->
          <!--              <a-form-item label="单价">-->
          <!--                <a-input placeholder="请输入单价" v-model="queryParam.price"/>-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--          </template>-->
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <!--              <a style="margin-left: 8px" @click="handleToggleSearch">-->
              <!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
              <!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
              <!--              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('充值商品')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button type="primary" icon="sync" @click="updateGoods">刷新商品</a-button>
      <a-button type="danger" icon="delete" @click="deleteAll">删除全部</a-button>
      <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down" />
        </a-button>
      </a-dropdown>

      <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据" :autoSize="{ minRows: 2, maxRows: 20 }" />
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
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-container">
            <span class="large-text" @click="copyText(text)">{{ text || '--' }}</span>
          </div>
        </template>
        <span slot="buyTypeSlot" slot-scope="text">
          <a-tag v-if="!text" color="red" class="ant-tag-no-margin">未设置</a-tag>
          <!-- <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag> -->
          <span v-else v-for="tag in text.split(',').sort()" :key="tag">
            <a-tag v-if="tag == 1" color="green" class="ant-tag-no-margin">{{ tag }}-真实充值</a-tag>
            <a-tag v-if="tag == 2" color="yellow" class="ant-tag-no-margin">{{ tag }}-GM额度</a-tag>
            <a-tag v-if="tag == 3" color="orange" class="ant-tag-no-margin">{{ tag }}-代金券</a-tag>
          </span>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleCopy(record)">复制</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <span slot="switchSlot" slot-scope="text">
          <a-switch checked-children="开" un-checked-children="关" :checked="text === 1" />
        </span>
        <span slot="skuSlot" slot-scope="text, record">
          <a-tag class="ant-tag-no-margin">内购</a-tag><a @click="copyText(record.sku)" class="copy-text"> {{ record.sku || '--' }} <a-icon type="copy" /></a>
          <a-divider />
          <a-tag class="ant-tag-no-margin">网页</a-tag><a @click="copyText(record.webSku)" class="copy-text"> {{ record.webSku || '--' }} <a-icon type="copy" /></a>
        </span>
        <span slot="localPriceSlot" slot-scope="text, record">
          <a-tag class="ant-tag-no-margin">内购</a-tag><a class="copy-text"> {{ record.localPrice || '--' }}</a>
          <a-divider />
          <a-tag class="ant-tag-no-margin">网页</a-tag><a class="copy-text"> {{ record.webLocalPrice || '--' }} </a>
        </span>
        <span slot="displayPriceSlot" slot-scope="text, record">
          <a-tag class="ant-tag-no-margin">内购</a-tag><a class="copy-text"> {{ record.displayPrice || '--' }} </a>
          <a-divider />
          <a-tag class="ant-tag-no-margin">网页</a-tag><a class="copy-text"> {{ record.webDisplayPrice || '--' }} </a>
        </span>
        <span slot="recommendSlot" slot-scope="text">
          <a-tag v-if="text === 1" class="ant-tag-no-margin" color="green">推荐</a-tag>
          <a-tag v-else-if="text === 2" class="ant-tag-no-margin" color="orange">礼包</a-tag>
          <a-tag v-else class="ant-tag-no-margin">未配置</a-tag>
        </span>
        <span slot="goodsGroupSlot" slot-scope="text">
          <a-tag v-if="text === 1" class="ant-tag-no-margin" color="green">直充</a-tag>
          <a-tag v-else-if="text === 2" class="ant-tag-no-margin" color="blue">礼包</a-tag>
          <a-tag v-else class="ant-tag-no-margin">未配置</a-tag>
        </span>
        <span slot="tagSlot" slot-scope="text">
          <a-tag color="blue" class="ant-tag-no-margin">{{ text }}</a-tag>
        </span>
        <span slot="goodsIdTitle">商品ID <a-icon type="copy" /></span>
        <span slot="nameTitle">名称 <a-icon type="copy" /></span>
        <span slot="remarkTitle">备注 <a-icon type="copy" /></span>
        <span slot="skuTitle">内购SKU <a-icon type="copy" /></span>
        <span slot="webSkuTitle">网页支付SKU <a-icon type="copy" /></span>
        <span slot="itemsTitle">奖励列表 <a-icon type="copy" /></span>
        <span slot="additionTitle">首次额外赠送 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <gameRechargeGoods-modal ref="modalForm" @ok="modalFormOk"></gameRechargeGoods-modal>
  </a-card>
</template>

<script>
import { postAction } from '@api/manage';
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameRechargeGoodsModal from './modules/GameRechargeGoodsModal';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameRechargeGoodsList',
  mixins: [JeecgListMixin],
  components: {
    GameRechargeGoodsModal,
    JInput
  },
  data() {
    return {
      description: '充值商品管理页面',
      importText: '',
      /* 排序参数 */
      isorter: {
        column: 'id',
        order: 'asc'
      },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          fixed: 'left',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        // {
        //   title: 'id',
        //   align: 'center',
        //   fixed: 'left',
        //   width: 80,
        //   dataIndex: 'id'
        // },
        {
          // title: '商品Id',
          align: 'center',
          fixed: 'left',
          width: 80,
          dataIndex: 'goodsId',
          slots: { title: 'goodsIdTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '商品名称',
          align: 'center',
          fixed: 'left',
          dataIndex: 'name',
          slots: { title: 'nameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '备注',
          align: 'center',
          fixed: 'left',
          dataIndex: 'remark',
          slots: { title: 'remarkTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '商品组别',
          align: 'center',
          dataIndex: 'goodsGroup',
          scopedSlots: { customRender: 'goodsGroupSlot' }
        },
        {
          title: '商品类型',
          align: 'center',
          dataIndex: 'goodsType',
          customRender: (value) => {
            let text = '--';
            if (value === 0) {
              text = '0-仙玉';
            } else if (value === 1) {
              text = '1-仙职';
            } else if (value === 2) {
              text = '2-月卡';
            } else if (value === 3) {
              text = '3-每日礼包';
            } else if (value === 4) {
              text = '4-首充';
            } else if (value === 5) {
              text = '5-周卡';
            } else if (value === 6) {
              text = '6-六道剑阵';
            } else if (value === 7) {
              text = '7-招财进宝/仙力护符';
            } else if (value === 8) {
              text = '8-高级天道令';
            } else if (value === 9) {
              text = '9-节日派对';
            } else if (value === 10) {
              text = '10-直购礼包';
            } else if (value === 11) {
              text = '11-精准礼包';
            } else if (value === 12) {
              text = '12-结义礼包';
            } else if (value === 13) {
              text = '13-自选特惠';
            } else if (value === 14) {
              text = '14-灵兽抽奖礼包';
            } else if (value === 15) {
              text = '15-开服目标活动-签到令';
            } else if (value === 16) {
              text = '16-开服目标活动-任务礼包';
            } else if (value === 17) {
              text = '17-系统直购礼包';
            } else if (value === 18) {
              text = '18-成长基金';
            } else if (value === 19) {
              text = '19-节日活动-夺宝战令';
            } else if (value === 20) {
              text = '20-新战令';
            } else if (value === 21) {
              text = '21-超值礼包';
            } else if (value === 22) {
              text = '22-神游特权卡';
            } else if (value === 23) {
              text = '23-GM特权卡';
            } else if (value === 24) {
              text = '24-无限真充';
            } else if (value === 25) {
              text = '25-无限神充';
            } else if (value === 26) {
              text = '26-GM充值工具-每日礼包';
            } else if (value === 27) {
              text = '27-GM充值工具-GM专属资源礼包';
            } else if (value === 28) {
              text = '28-仙缘神通-礼包';
            }
            return text;
          }
        },
        {
          title: '购买类型',
          align: 'center',
          width: 120,
          dataIndex: 'buyType',
          scopedSlots: { customRender: 'buyTypeSlot' }
        },
        {
          title: 'GM额度',
          align: 'center',
          dataIndex: 'gmCoin'
        },
        {
          title: '代金券',
          align: 'center',
          dataIndex: 'cashCoupon'
        },
        {
          title: '单价',
          align: 'center',
          dataIndex: 'price'
        },
        {
          title: '折扣',
          align: 'center',
          dataIndex: 'discount'
        },
        {
          title: '特殊标签',
          align: 'center',
          dataIndex: 'recommend',
          scopedSlots: { customRender: 'recommendSlot' }
        },
        {
          title: '计入累充',
          align: 'center',
          dataIndex: 'amountStat',
          scopedSlots: { customRender: 'switchSlot' }
        },
        {
          title: 'SKU',
          align: 'center',
          width: 140,
          dataIndex: 'sku',
          scopedSlots: { customRender: 'skuSlot' }
        },
        {
          // title: '奖励列表',
          align: 'center',
          width: 220,
          dataIndex: 'items',
          slots: { title: 'itemsTitle' },
          scopedSlots: { customRender: 'largeText' }
        },
        {
          // title: '首次额外赠送',
          align: 'center',
          width: 220,
          dataIndex: 'addition',
          slots: { title: 'additionTitle' },
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '统计类型',
          align: 'center',
          // width: 80,
          dataIndex: 'amountStatTypes'
        },
        {
          title: '货币',
          align: 'center',
          dataIndex: 'currency',
          scopedSlots: { customRender: 'tagSlot' }
        },
        {
          title: '兑换比例',
          align: 'center',
          width: 80,
          dataIndex: 'exchange'
        },
        {
          title: '当地价格',
          align: 'center',
          width: 180,
          dataIndex: 'localPrice',
          scopedSlots: { customRender: 'localPriceSlot' }
        },
        {
          title: '显示价格',
          align: 'center',
          width: 180,
          dataIndex: 'displayPrice',
          scopedSlots: { customRender: 'displayPriceSlot' }
        },
        {
          title: '网页支付价格',
          align: 'center',
          dataIndex: 'webLocalPrice',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '操作',
          align: 'center',
          width: 180,
          fixed: 'right',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameRechargeGoods/list',
        delete: 'game/gameRechargeGoods/delete',
        deleteBatch: 'game/gameRechargeGoods/deleteBatch',
        deleteAll: 'game/gameRechargeGoods/deleteAll',
        exportXlsUrl: 'game/gameRechargeGoods/exportXls',
        importExcelUrl: 'game/gameRechargeGoods/importExcel',
        updateGoods: 'game/gameRechargeGoods/updateGoods',
        importTextUrl: 'game/gameRechargeGoods/importText'
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
    deleteAll() {
      this.handleConfrimRequest(this.url.deleteAll, {}, '是否删除全部充值商品？', '点击确定删除全部', 'delete');
    },
    updateGoods() {
      this.handleConfrimRequest(this.url.updateGoods, {}, '是否刷新充值商品？', '点击确定刷新');
    },
    handleImportText() {
      let params = {
        id: 0,
        text: this.importText
      };
      console.log(params);
      postAction(this.url.importTextUrl, params).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.loadData();
        } else {
          this.$message.warning(res.message);
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.copy-text {
  white-space: nowrap;
  color: rgba(0, 0, 0, 0.65);
}

.ant-tag-no-margin {
  margin-right: auto !important;
}

.ant-divider-horizontal {
  margin: 6px 0 6px 0;
  padding: 0 10px 0 10px;
}

.import-text {
  margin-top: 8px;
  margin-bottom: -10px;
}

.large-text-container {
  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
  max-height: 200px;
}

.large-text {
  white-space: normal;
  word-break: break-word;
}
</style>