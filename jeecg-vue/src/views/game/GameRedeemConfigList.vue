<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name" />
            </a-form-item>
          </a-col>
          <!-- <a-col :md="6" :sm="8">
                        <a-form-item label="限制类型">
                            <a-select placeholder="请选择限制类型:" v-model="queryParam.limitType">
                                <a-select-option :value="0">0-通用</a-select-option>
                                <a-select-option :value="1">1-指定渠道</a-select-option>
                                <a-select-option :value="2">2-SERVER</a-select-option>
                                <a-select-option :value="4">4-同一分组只能兑换一次</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col> -->
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="限制次数">
                <a-input placeholder="请输入限制次数" v-model="queryParam.limitCount" />
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
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('game_redeem_config')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
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
          <a @click="handleEdit(record)">分组信息</a>
        </span>
      </a-table>
    </div>

    <gameRedeemConfig-modal ref="modalForm" @ok="modalFormOk"></gameRedeemConfig-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameRedeemConfigModal from './modules/GameRedeemConfigModal';

export default {
  name: 'GameRedeemConfigList',
  mixins: [JeecgListMixin],
  components: {
    GameRedeemConfigModal
  },
  data() {
    return {
      description: 'game_redeem_config管理页面',
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
          title: '分组Id',
          align: 'center',
          dataIndex: 'id'
        },
        {
          title: '名称',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '分组说明',
          align: 'center',
          dataIndex: 'summary'
        },
        {
          title: '限制次数',
          align: 'center',
          dataIndex: 'limitCount'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/redeemActivityGroup/list',
        delete: 'game/redeemActivityGroup/delete',
        deleteBatch: 'game/redeemActivityGroup/deleteBatch',
        exportXlsUrl: 'game/redeemActivityGroup/exportXls',
        importExcelUrl: 'game/redeemActivityGroup/importExcel'
      },
      dictOptions: {},
      tableScroll: { x: 12 * 147 + 50 }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {}
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
