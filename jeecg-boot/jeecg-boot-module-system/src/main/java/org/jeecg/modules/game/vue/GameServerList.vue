<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="服务器名字">
              <a-input placeholder="请输入服务器名字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="服务器路径">
              <a-input placeholder="请输入服务器路径" v-model="queryParam.host"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="服务器端口">
              <a-input placeholder="请输入服务器端口" v-model="queryParam.port"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="登陆地址和端口">
              <a-input placeholder="请输入登陆地址和端口" v-model="queryParam.loginUrl"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="服务器状态 0-正常 1-流畅 2-火爆 3-维护">
              <a-input placeholder="请输入服务器状态 0-正常 1-流畅 2-火爆 3-维护" v-model="queryParam.status"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('游戏服配置')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <gameServer-modal ref="modalForm" @ok="modalFormOk"></gameServer-modal>
  </a-card>
</template>

<script>
  import GameServerModal from './modules/GameServerModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "GameServerList",
    mixins:[JeecgListMixin],
    components: {
      GameServerModal
    },
    data () {
      return {
        description: '游戏服配置管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '服务器名字',
            align:"center",
            dataIndex: 'name'
           },
		   {
            title: '服务器路径',
            align:"center",
            dataIndex: 'host'
           },
		   {
            title: '服务器端口',
            align:"center",
            dataIndex: 'port'
           },
		   {
            title: '登陆地址和端口',
            align:"center",
            dataIndex: 'loginUrl'
           },
		   {
            title: '服务器状态 0-正常 1-流畅 2-火爆 3-维护',
            align:"center",
            dataIndex: 'status'
           },
		   {
            title: '推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服',
            align:"center",
            dataIndex: 'recommend'
           },
		   {
            title: '出错提示信息',
            align:"center",
            dataIndex: 'warning'
           },
		   {
            title: '显示版本号 0-不显示 1-显示',
            align:"center",
            dataIndex: 'showVersion'
           },
		   {
            title: '进入游戏客户端版本',
            align:"center",
            dataIndex: 'clientVersionCode'
           },
		   {
            title: '数据库路径',
            align:"center",
            dataIndex: 'dbHost'
           },
		   {
            title: '数据库端口',
            align:"center",
            dataIndex: 'dbPort'
           },
		   {
            title: '数据库用户名',
            align:"center",
            dataIndex: 'dbUser'
           },
		   {
            title: '数据库密码',
            align:"center",
            dataIndex: 'dbPassword'
           },
		   {
            title: '数据库名',
            align:"center",
            dataIndex: 'dbName'
           },
		   {
            title: '后台HTTP端口',
            align:"center",
            dataIndex: 'httpPort'
           },
		   {
            title: '排序字段',
            align:"center",
            dataIndex: 'position'
           },
		   {
            title: '服务器类型 0-混服 1-专服',
            align:"center",
            dataIndex: 'type'
           },
		   {
            title: '合服时母服id',
            align:"center",
            dataIndex: 'pid'
           },
		   {
            title: '合服时间',
            align:"center",
            dataIndex: 'mergeTime'
           },
		   {
            title: '扩展字段',
            align:"center",
            dataIndex: 'extra'
           },
		   {
            title: '服务器开服时间',
            align:"center",
            dataIndex: 'openTime'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/game/gameServer/list",
          delete: "/game/gameServer/delete",
          deleteBatch: "/game/gameServer/deleteBatch",
          exportXlsUrl: "game/gameServer/exportXls",
          importExcelUrl: "game/gameServer/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>