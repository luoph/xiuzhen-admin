<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col v-if="!isIncludeActivityModel" :md="6" :sm="8">
            <a-form-item label="激活码活动id">
              <a-input placeholder="请输入激活码活动id" v-model="queryParam.activityId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="激活码">
              <a-input placeholder="请输入激活码" v-model="queryParam.code" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="12" :sm="16">
              <a-form-item label="创建时间">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                <span class="query-group-split-cust" />
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="状态">
                <a-select placeholder="请选择活动状态" v-model="queryParam.status" initialValue="1">
                  <a-select-option :value="0">无效</a-select-option>
                  <a-select-option :value="1">有效</a-select-option>
                </a-select>
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
      <a-button type="primary" icon="plus" @click="handleAdd(false)">新增</a-button>
      <a-button type="primary" icon="plus" @click="handleAdd(true)">批量新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('激活码')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
        <span slot="statusSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
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
        <span slot="codeTitle">激活码 <a-icon type="copy" /></span>
      </a-table>
    </div>

    <redeemCode-modal ref="modalForm" @ok="modalFormOk"></redeemCode-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import RedeemCodeModal from './modules/RedeemCodeModal';
import JDate from '@/components/jeecg/JDate.vue';

export default {
  name: 'RedeemCodeList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    RedeemCodeModal
  },
  props: {
    // 作为子组件，禁止初始化table数据
    disableMixinCreated: Boolean
  },
  data() {
    return {
      description: '激活码管理页面',
      queryParam: {
        activityId: undefined
      },
      isIncludeActivityModel: false,
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
          title: '激活码活动id',
          align: 'center',
          width: 120,
          dataIndex: 'activityId'
        },
        {
          // title: '激活码',
          align: 'center',
          dataIndex: 'code',
          slots: { title: 'codeTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: '可使用总数',
          align: 'center',
          dataIndex: 'totalNum'
        },
        {
          title: '已使用数量',
          align: 'center',
          dataIndex: 'usedNum'
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'statusSlot' }
        },
        {
          title: '创建时间',
          align: 'center',
          width: 240,
          dataIndex: 'createTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/redeemCode/list',
        delete: 'game/redeemCode/delete',
        deleteBatch: 'game/redeemCode/deleteBatch',
        exportXlsUrl: 'game/redeemCode/exportXls',
        importExcelUrl: 'game/redeemCode/importExcel'
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
    reset() {
      this.queryParam = {};
    },
    searchReset() {
      let activityId = this.queryParam.activityId;
      this.queryParam = {};
      if (this.isIncludeActivityModel) {
        this.queryParam.activityId = activityId;
      }
      this.loadData(1);
    },
    handleAdd(isBatchAdd) {
      let editParam = {
        activityId: this.isIncludeActivityModel ? this.model.id : undefined,
        isIncludeActivityModel: this.isIncludeActivityModel,
        isBatchAdd: false
      };
      if (isBatchAdd) {
        editParam.isBatchAdd = true;
      }
      this.$refs.modalForm.edit(editParam);
      this.$refs.modalForm.title = '新增激活码配置';
    },
    loadDateById(record) {
      this.model = record;
      this.isIncludeActivityModel = this.model.id != null ? true : false;
      this.queryParam.activityId = this.isIncludeActivityModel ? this.model.id : undefined;
      this.loadData(1);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
