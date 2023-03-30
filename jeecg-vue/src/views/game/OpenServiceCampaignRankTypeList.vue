<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="排行类型">
              <a-select placeholder="请选择排行类型" model="queryParam.rankType" initialValue="1">
                <a-select-option :value="1">1-境界冲榜</a-select-option>
                <a-select-option :value="2">2-喂养冲榜</a-select-option>
                <a-select-option :value="3">3-追加冲榜</a-select-option>
                <a-select-option :value="4">4-祖鲲冲榜</a-select-option>
                <a-select-option :value="5">5-腾蛇冲榜</a-select-option>
                <a-select-option :value="6">6-火凤冲榜</a-select-option>
                <a-select-option :value="7">7-飞剑冲榜</a-select-option>
                <a-select-option :value="8">8-情缘冲榜</a-select-option>
                <a-select-option :value="9">9-功法冲榜</a-select-option>
                <a-select-option :value="10">10-兽魂石冲榜</a-select-option>
                <a-select-option :value="11">11-炼灵冲榜</a-select-option>
                <a-select-option :value="12">12-宝匣冲榜</a-select-option>
                <a-select-option :value="13">13-仙器冲榜</a-select-option>
                <a-select-option :value="14">14-符文冲榜</a-select-option>
                <a-select-option :value="15">15-天命冲榜</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="排行类型名称">
              <a-input placeholder="请输入排行类型名称" v-model="queryParam.rankTypeName"/>
            </a-form-item>
          </a-col>
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
      <a-button type="primary" icon="download" @click="handleExportXls('开服活动-开服排行-类型库')">导出</a-button>
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

    <openServiceCampaignRankType-modal ref="modalForm" @ok="modalFormOk"></openServiceCampaignRankType-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import OpenServiceCampaignRankTypeModal from './modules/OpenServiceCampaignRankTypeModal';

export default {
  name: 'OpenServiceCampaignRankTypeList',
  mixins: [JeecgListMixin],
  components: {
    OpenServiceCampaignRankTypeModal
  },
  data() {
    return {
      description: '开服活动-开服排行-类型库管理页面',
      // 表头
      columns: [
        // {
        //     title: "#",
        //     dataIndex: "",
        //     key: "rowIndex",
        //     width: 60,
        //     align: "center",
        //     customRender: function(t, r, index) {
        //         return parseInt(index) + 1;
        //     }
        // },
        {
          title: 'id',
          align: 'center',
          width: 60,
          dataIndex: 'id'
        },
        {
          title: '排行类型',
          align: 'center',
          dataIndex: 'rankType',
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '1-境界排行';
            } else if (value === 2) {
              text = '2-仙兽排行';
            } else if (value === 3) {
              text = '3-义戒排行';
            } else if (value === 4) {
              text = '4-飞剑排行';
            } else if (value === 5) {
              text = '5-天书排行';
            } else if (value === 6) {
              text = '6-圣灵排行';
            } else if (value === 7) {
              text = '7-法宝排行';
            } else if (value === 8) {
              text = '8-情饰排行';
            } else {
              text = '大于8-过期类型';
            }
            return text;
          }
        },
        {
          title: '排行类型名称',
          align: 'center',
          dataIndex: 'rankTypeName'
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
        list: 'game/openServiceCampaignRankType/list',
        delete: 'game/openServiceCampaignRankType/delete',
        deleteBatch: 'game/openServiceCampaignRankType/deleteBatch',
        exportXlsUrl: 'game/openServiceCampaignRankType/exportXls',
        importExcelUrl: 'game/openServiceCampaignRankType/importExcel'
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
    initDictConfig() {}
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
