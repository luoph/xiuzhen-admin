<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="应用名称">
              <j-input placeholder="请输入应用名称模糊查询" v-model="queryParam.appName"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="应用包名">
              <j-input placeholder="请输入应用包名" v-model="queryParam.packageName"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="版本号">
              <a-input placeholder="请输入版本号" v-model="queryParam.versionCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="包渠道">
              <a-select placeholder="请选择包渠道" v-model="queryParam.channel">
                <a-select-option value="test">开发(develop)</a-select-option>
                <a-select-option value="test">测试(test)</a-select-option>
                <a-select-option value="plan">策划(plan)</a-select-option>
                <a-select-option value="preview">预览(preview)</a-select-option>
                <a-select-option value="youdian">优点(youdian)</a-select-option>
                <a-select-option value="chenglong">乘龙(chenglong)</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="版本名">
                <j-input placeholder="请输入版本名" v-model="queryParam.versionName"></j-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="平台">
                <a-select placeholder="请选择平台" v-model="queryParam.platform">
                  <a-select-option value="android">Android</a-select-option>
                  <a-select-option value="ios">iOS</a-select-option>
                </a-select>
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
      <a-button @click="updateConfig" type="danger" icon="sync">刷新客户端版本配置</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('game_app_update')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
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
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
        </template>
        <template slot="largeText" slot-scope="text">
          <div class="large-text-container">
            <span class="large-text">{{ text }}</span>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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

    <gameAppUpdate-modal ref="modalForm" @ok="modalFormOk"></gameAppUpdate-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameAppUpdateModal from './modules/GameAppUpdateModal';
import JInput from '@/components/jeecg/JInput';
import { getAction } from '@/api/manage';

export default {
  name: 'GameAppUpdateList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameAppUpdateModal
  },
  data() {
    return {
      description: '客户端更新配置',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '游戏id',
          align: 'center',
          dataIndex: 'gameId'
        },
        {
          title: '应用名称',
          align: 'center',
          dataIndex: 'appName'
        },
        {
          title: '应用包名',
          align: 'center',
          dataIndex: 'packageName'
        },
        {
          title: '版本号',
          align: 'center',
          dataIndex: 'versionCode'
        },
        {
          title: '版本名',
          align: 'center',
          dataIndex: 'versionName'
        },
        {
          title: '平台',
          align: 'center',
          dataIndex: 'platform'
        },
        {
          title: '包渠道',
          align: 'center',
          dataIndex: 'channel'
        },
        {
          title: '更新标题',
          align: 'center',
          dataIndex: 'updateTitle'
        },
        {
          title: '更新内容',
          align: 'center',
          dataIndex: 'updateContent',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '下载地址',
          align: 'center',
          dataIndex: 'downloadUrl'
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '修改时间',
          align: 'center',
          dataIndex: 'updateTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/gameAppUpdate/list',
        delete: 'game/gameAppUpdate/delete',
        deleteBatch: 'game/gameAppUpdate/deleteBatch',
        exportXlsUrl: 'game/gameAppUpdate/exportXls',
        importExcelUrl: 'game/gameAppUpdate/importExcel',
        updateConfigUrl: 'game/gameAppUpdate/updateConfig'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {},
    updateConfig() {
      let that = this;
      this.$confirm({
        title: '是否刷新客户端版本配置？',
        content: '点击确定刷新客户端版本配置',
        onOk: function() {
          getAction(that.url.updateConfigUrl).then(res => {
            if (res.success) {
              that.$message.success('客户端版本配置刷新成功');
            } else {
              that.$message.error('客户端版本配置刷新失败');
            }
          });
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

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
