<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('开服活动类型配置')">导出</a-button>
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
      <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据"></a-textarea>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
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
        @change="handleTableChange"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="100px" alt="图片不存在" style="max-width: 180px" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <!-- <a-divider type="vertical" /> -->
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

    <open-service-campaign-type-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-type-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction, postAction } from '../../api/manage';
import { filterObj } from '@/utils/util';
import OpenServiceCampaignTypeModal from './modules/OpenServiceCampaignTypeModal';

export default {
  name: 'OpenServiceCampaignTypeList',
  mixins: [JeecgListMixin],
  components: {
    OpenServiceCampaignTypeModal
  },
  data() {
    return {
      description: '开服活动-类型(2级)管理页面',
      model: {},
      importText: '',
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
          title: 'id',
          align: 'center',
          width: 60,
          dataIndex: 'id'
        },
        {
          title: '开服活动id',
          align: 'center',
          dataIndex: 'campaignId'
        },
        {
          title: '开服活动类型',
          align: 'center',
          dataIndex: 'type',
          // <!-- 1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗 -->
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '1-开服排行';
            } else if (value === 2) {
              text = '2-开服礼包';
            } else if (value === 3) {
              text = '3-单笔充值';
            } else if (value === 4) {
              text = '4-寻宝';
            } else if (value === 5) {
              text = '5-道具消耗';
            }
            return text;
          }
        },
        {
          title: '排序',
          align: 'center',
          dataIndex: 'sort'
        },
        {
          title: '活动备注',
          align: 'center',
          dataIndex: 'remark'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        // {
        //     title: "更新时间",
        //     align: "center",
        //     dataIndex: "updateTime"
        // },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/openServiceCampaignType/list',
        delete: 'game/openServiceCampaignType/delete',
        deleteBatch: 'game/openServiceCampaignType/deleteBatch',
        exportXlsUrl: 'game/openServiceCampaignType/exportXls',
        importExcelUrl: 'game/openServiceCampaignType/importExcel',
        importTextUrl: 'game/openServiceCampaignType/importText'
      },
      dictOptions: {}
    };
  },
  computed: {},
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
      this.importText = '';
      this.loadData();
    },
    handleAdd() {
      this.$refs.modalForm.add({
        campaignId: this.model.id
      });
      this.$refs.modalForm.title = '新增礼包配置';
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // typeId、活动id、详情id
      param.campaignId = this.model.id;
      return filterObj(param);
    },
    importExcelUrl() {
      let domainURL = window._CONFIG['domainURL'];
      return `${domainURL}/${this.url.importExcelUrl}`;
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

/** Button按钮间距 */
.ant-btn {
  margin-right: 15px;
}

.import-text {
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
