<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="激活码名称">
              <a-input placeholder="请输入激活码名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="限制类型">
              <a-select placeholder="请选择限制类型" v-model="queryParam.limitType">
                <a-select-option :value="0">0 - 通用</a-select-option>
                <a-select-option :value="1">1 - 指定渠道</a-select-option>
                <a-select-option :value="2">2 - SERVER</a-select-option>
                <a-select-option :value="4">4 - 同一分组只能兑换一次</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col v-if="!isIncludeGroupModel" :md="4" :sm="8">
              <a-form-item label="分组id">
                <a-input placeholder="请输入分组id" v-model="queryParam.groupId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="活动状态">
                <a-input placeholder="请输入活动状态" v-model="queryParam.status"></a-input>
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
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('激活码活动')">导出</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource"
               :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
               style="max-width: 80px; font-size: 12px; font-style: italic"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载
          </a-button>
        </template>

        <span slot="statuSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
        </span>
      </a-table>
    </div>

    <redeemActivity-modal ref="modalForm" @ok="modalFormOk"></redeemActivity-modal>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import RedeemActivityModal from './modules/RedeemActivityModal';

export default {
  name: 'RedeemActivityList',
  mixins: [JeecgListMixin],
  components: {
    RedeemActivityModal
  },
  props: {
    // 作为子组件，禁止初始化table数据
    disableMixinCreated: Boolean
  },
  data() {
    return {
      description: '激活码活动管理页面',
      queryParam: {
        groupId: undefined
      },
      isIncludeGroupModel: false,
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
          title: '活动Id',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '激活码名称',
          align: 'center',
          width: 120,
          dataIndex: 'name'
        },
        {
          title: '礼包说明',
          align: 'center',
          width: 180,
          dataIndex: 'summary'
        },
        {
          title: '限制类型',
          align: 'center',
          width: 90,
          dataIndex: 'limitType'
        },
        {
          title: '分组id',
          align: 'center',
          width: 80,
          dataIndex: 'groupId'
        },
        {
          title: '限制渠道id',
          align: 'center',
          width: 120,
          dataIndex: 'channelIds'
        },
        {
          title: '限制区服id',
          align: 'center',
          width: 120,
          dataIndex: 'serverIds'
        },
        {
          title: '活动状态',
          align: 'center',
          width: 90,
          dataIndex: 'status',
          scopedSlots: {customRender: 'statuSlot'}
        },
        {
          title: '奖励',
          align: 'center',
          width: 240,
          dataIndex: 'reward'
        },
        {
          title: '开始时间',
          align: 'center',
          width: 180,
          dataIndex: 'startTime'
        },
        {
          title: '结束时间',
          align: 'center',
          width: 180,
          dataIndex: 'endTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: 'game/redeemActivity/list',
        delete: 'game/redeemActivity/delete',
        deleteBatch: 'game/redeemActivity/deleteBatch',
        exportXlsUrl: 'game/redeemActivity/exportXls',
        importExcelUrl: 'game/redeemActivity/importExcel'
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
    initDictConfig() {
    },
    reset() {
      this.queryParam = {};
    },
    searchReset() {
      let groupId = this.queryParam.groupId;
      this.queryParam = {};
      if (this.isIncludeGroupModel) {
        this.queryParam.groupId = groupId;
      }
      this.loadData(1);
    },
    handleAdd() {
      this.$refs.modalForm.edit({
        groupId: this.isIncludeGroupModel ? this.model.id : undefined,
        isIncludeGroupModel: this.isIncludeGroupModel
      });
      this.$refs.modalForm.title = '新增活动配置';
    },
    loadDateById(record) {
      this.model = record;
      this.isIncludeGroupModel = this.model.id != null ? true : false;
      this.queryParam.groupId = this.isIncludeGroupModel ? this.model.id : undefined;
      this.loadData(1);
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
