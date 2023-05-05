<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="激活码名称">
              <a-input placeholder="请输入激活码名称" v-model="queryParam.name"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="限制类型">
              <a-select placeholder="请选择限制类型" v-model="queryParam.limitType">
                <a-select-option :value="0">0 - 不限制</a-select-option>
                <a-select-option :value="1">1 - 指定渠道</a-select-option>
                <a-select-option :value="2">2 - 指定区服</a-select-option>
                <a-select-option :value="3">3 - 指定渠道&指定区服</a-select-option>
                <a-select-option :value="4">4 - 同一分组只能兑换一次</a-select-option>
                <a-select-option :value="5">5 - 指定渠道&同一分组只能兑换一次</a-select-option>
                <a-select-option :value="5">6 - 指定区服&同一分组只能兑换一次</a-select-option>
                <a-select-option :value="5">7 - 指定渠道&指定区服&同一分组只能兑换一次</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col v-if="!isIncludeGroupModel" :md="4" :sm="8">
              <a-form-item label="分组id">
                <a-input placeholder="请输入分组id" v-model="queryParam.groupId"/>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="活动状态">
                <a-input placeholder="请输入活动状态" v-model="queryParam.status"/>
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
      <a-table ref="table"
               size="middle"
               bordered
               rowKey="id"
               :columns="columns"
               :dataSource="dataSource"
               :scroll="{ x: 'max-content' }"
               :pagination="ipagination"
               :loading="loading"
               @change="handleTableChange">
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

        <span slot="statusSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="red">无效</a-tag>
          <a-tag v-else color="green">有效</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a @click="handleCopy(record)">复制</a>
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
          align: 'left',
          width: 160,
          dataIndex: 'name'
        },
        {
          title: '礼包说明',
          align: 'left',
          width: 180,
          dataIndex: 'summary',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '限制类型',
          align: 'left',
          width: 140,
          dataIndex: 'limitType',
          customRender: function (text) {
            if (text === 1) {
              return '不限制 [0]';
            } else if (text === 1) {
              return '指定渠道 [1]';
            } else if (text === 2) {
              return '指定区服 [2]';
            } else if (text === 3) {
              return '指定渠道&指定区服 [3]';
            } else if (text === 4) {
              return '同一分组只能兑换一次 [4]';
            } else if (text === 5) {
              return '指定渠道&同一分组只能兑换一次 [5]';
            } else if (text === 6) {
              return '指定区服&同一分组只能兑换一次 [6]';
            } else if (text === 7) {
              return '指定渠道&指定区服&同一分组只能兑换一次 [7]';
            }
            return '--';
          }
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
          dataIndex: 'channelIds',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '限制区服id',
          align: 'center',
          width: 120,
          dataIndex: 'serverIds',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '活动状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: {customRender: 'statusSlot'}
        },
        {
          title: '奖励',
          align: 'left',
          width: 240,
          dataIndex: 'reward',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'startTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 120,
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
