<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!--<div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    </a-row>
            </a-form>
        </div>-->
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <!-- <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
      <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据" :autoSize="{ minRows: 2, maxRows: 20 }"/> -->
      <a-button type="primary" icon="download" @click="handleExportXls('节日活动-节日派对-进度任务')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!--<a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel">
                        <a-icon type="delete" />
                        删除
                    </a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作
                    <a-icon type="down" />
                </a-button>
            </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
                style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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

    <gameCampaignTypePartyProgress-modal ref="modalForm" @ok="modalFormOk"></gameCampaignTypePartyProgress-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameCampaignTypePartyProgressModal from './modules/GameCampaignTypePartyProgressModal';
import { getAction, postAction } from '@api/manage';
import { filterObj } from '@/utils/util';

export default {
  name: 'GameCampaignTypePartyProgressList',
  mixins: [JeecgListMixin],
  components: {
    GameCampaignTypePartyProgressModal
  },
  data() {
    return {
      description: '节日派对进度任务管理页面',
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
          dataIndex: 'campaignId'
        },
        {
          title: '子活动id',
          align: 'center',
          dataIndex: 'typeId'
        },
        {
          title: 'id',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '任务规定数量',
          align: 'center',
          dataIndex: 'target'
        },
        {
          title: '进度百分比',
          align: 'center',
          dataIndex: 'percent'
        },
        {
          title: '任务奖励',
          align: 'center',
          dataIndex: 'reward'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameCampaignTypePartyProgress/list',
        delete: 'game/gameCampaignTypePartyProgress/delete',
        //deleteBatch: "game/gameCampaignTypePartyProgress/deleteBatch"
        exportXlsUrl: 'game/gameCampaignTypePartyProgress/exportXls',
        importExcelUrl: 'game/gameCampaignTypePartyProgress/importExcel',
        importTextUrl: 'game/gameCampaignTypePartyProgress/importText'
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
      var params = this.getQueryParams();
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
      this.$refs.modalForm.title = '新增节日派对进度任务配置';
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // typeId、活动id
      param.typeId = this.model.id;
      param.campaignId = this.model.campaignId;
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
