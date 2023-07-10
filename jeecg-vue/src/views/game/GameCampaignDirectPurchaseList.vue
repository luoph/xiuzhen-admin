<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24"></a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <!-- <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
      <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据" :autoSize="{ minRows: 2, maxRows: 20 }"/> -->
      <a-button type="primary" icon="download" @click="handleExportXls(model.type === 15 ? '节日活动-直购礼包' : model.type === 28 ? '节日活动-超值礼包' : '未知活动类型')"
        >导出</a-button
      >
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
                 <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
                 <a style="margin-left: 24px" @click="onClearSelected">清空</a>
             </div>-->

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
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
          <a @click="handleEdit(record)">编辑</a>
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
      </a-table>
    </div>

    <gameCampaignDirectPurchase-modal ref="modalForm" @ok="modalFormOk"></gameCampaignDirectPurchase-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameCampaignDirectPurchaseModal from './modules/GameCampaignDirectPurchaseModal';
import { getAction, postAction } from '@api/manage';
import { filterObj } from '@/utils/util';

export default {
  name: 'GameCampaignDirectPurchaseList',
  mixins: [JeecgListMixin],
  components: {
    GameCampaignDirectPurchaseModal
  },
  data() {
    return {
      description: '直购礼包管理页面',
      importText: '',
      model: {},
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
          title: '主活动id',
          align: 'center',
          width: 80,
          dataIndex: 'campaignId'
        },
        {
          title: '子活动id',
          align: 'center',
          width: 80,
          dataIndex: 'typeId'
        },
        {
          title: 'id',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '限购数量',
          align: 'center',
          dataIndex: 'limitNum'
        },
        {
          title: '商品id',
          align: 'center',
          dataIndex: 'goodsId'
        },
        {
          title: '礼包名',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '礼包组类型',
          align: 'center',
          dataIndex: 'type'
        },
        {
          title: '组排序',
          align: 'center',
          dataIndex: 'sort'
        },
        {
          title: '奖励列表',
          align: 'center',
          dataIndex: 'reward'
        },
        {
          title: '消耗列表',
          align: 'center',
          dataIndex: 'consume'
        },
        {
          title: '礼包折扣',
          align: 'center',
          dataIndex: 'discount'
        },
        {
          title: '图标颜色',
          align: 'center',
          dataIndex: 'color'
        },
        {
          title: '最小世界等级',
          align: 'center',
          dataIndex: 'minLevel'
        },
        {
          title: '最大世界等级',
          align: 'center',
          dataIndex: 'maxLevel'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameCampaignDirectPurchase/list',
        delete: 'game/gameCampaignDirectPurchase/delete',
        deleteBatch: 'game/gameCampaignDirectPurchase/deleteBatch',
        exportXlsUrl: 'game/gameCampaignDirectPurchase/exportXls',
        importExcelUrl: 'game/gameCampaignType/importExcel/details',
        importTextUrl: 'game/gameCampaignDirectPurchase/importText'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}?campaignId=${this.model.campaignId}&typeId=${this.model.id}`;
    }
  },
  methods: {
    loadData(arg) {
      if (!this.model.id) {
        return;
      }

      if (!this.url.list) {
        this.$message.error('请设置url.list属性!');
        return;
      }

      // 加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }

      // 查询条件
      const params = this.getQueryParams();
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success && res.result && res.result.records) {
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
        }
        if (res.code === 510) {
          this.$message.warning(res.message);
        }
        this.loading = false;
      });
    },
    edit(record) {
      this.model = record;
      this.loadData();
    },
    handleAdd() {
      this.$refs.modalForm.add({ typeId: this.model.id, campaignId: this.model.campaignId });
      this.$refs.modalForm.title = '新增直购礼包活动配置';
    },
    getQueryParams() {
      const param = Object.assign({}, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // typeId、活动id
      param.typeId = this.model.id;
      param.campaignId = this.model.campaignId;
      param.campaignType = this.model.type;
      return filterObj(param);
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    },
    handleImportText() {
      let params = {
        id: this.model.id,
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
</style>
