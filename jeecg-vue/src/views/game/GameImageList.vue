<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="图片类型">
              <a-select placeholder="请选择图片类型" v-model="queryParam.type" default-value="1">
                <a-select-option :value="1">图标</a-select-option>
                <a-select-option :value="2">宣传图</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="备注">
              <j-input placeholder="请输入备注模糊查询" v-model="queryParam.remark"></j-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-col :md="6" :sm="8">
                <a-form-item label="图片名">
                  <j-input placeholder="请输入图片名" v-model="queryParam.name"></j-input>
                </a-form-item>
              </a-col>
              <a-form-item label="相对路径">
                <j-input placeholder="请输入相对路径" v-model="queryParam.imgUrl"></j-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('游戏图片')">导出</a-button>
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
          <img v-else :src="getImgView(text)" alt="图片不存在" class="list-image" />
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

    <gameImage-modal ref="modalForm" @ok="modalFormOk"></gameImage-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameImageModal from './modules/GameImageModal';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameImageList',
  mixins: [JeecgListMixin],
  components: {
    GameImageModal,
    JInput
  },
  data() {
    return {
      description: '游戏图片管理页面',
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
          title: '图片类型',
          align: 'center',
          width: 100,
          dataIndex: 'type',
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '图标';
            } else if (value === 2) {
              text = '宣传图';
            }
            return text;
          }
        },
        {
          title: '图片',
          align: 'center',
          width: 300,
          dataIndex: 'imgUrl',
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '文件名',
          align: 'center',
          width: 180,
          dataIndex: 'name'
        },
        {
          title: '图片地址',
          align: 'center',
          width: 180,
          customRender: function (t, r) {
            return r.imgUrl;
          }
        },
        {
          title: '图片尺寸',
          align: 'center',
          width: 120,
          customRender: function (t, r) {
            return r.width + 'x' + r.height;
          }
        },
        {
          title: '备注',
          align: 'center',
          width: 160,
          dataIndex: 'remark'
        },
        {
          title: '上传时间',
          align: 'center',
          width: 120,
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
        list: 'game/gameImage/list',
        delete: 'game/gameImage/delete',
        deleteBatch: 'game/gameImage/deleteBatch',
        exportXlsUrl: 'game/gameImage/exportXls',
        importExcelUrl: 'game/gameImage/importExcel'
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
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.list-image {
  width: 100%;
  height: 120px;
  object-fit: scale-down;
}
</style>
