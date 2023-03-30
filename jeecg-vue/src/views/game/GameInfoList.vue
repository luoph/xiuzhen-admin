<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="游戏名称">
              <j-input placeholder="请输入游戏名称" v-model="queryParam.name"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="唯一标识">
              <j-search-select-tag v-model="queryParam.yaSimpleName" placeholder="唯一标识"
                                 dictCode="game_info,name,ya_simple_name"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="gameAppKey">
                <a-input placeholder="请输入gameAppKey" v-model="queryParam.yaGameKey"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
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
      <a-popconfirm title="刷新游戏配置" @confirm="refreshConfig()">
        <a-button type="primary" icon="update">刷新游戏配置</a-button>
      </a-popconfirm>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏信息')">导出</a-button> -->
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource"
               :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <!--          <a-divider type="vertical"/>-->
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <game-info-modal ref="modalForm" @ok="modalFormOk"></game-info-modal>
  </a-card>
</template>

<script>
import GameInfoModal from './modules/GameInfoModal';
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {getAction} from '@/api/manage';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameInfoList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameInfoModal
  },
  data() {
    return {
      description: '游戏信息管理页面',
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
          title: '游戏Id',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '游戏名称',
          align: 'center',
          width: 100,
          dataIndex: 'name'
        },
        {
          title: '唯一标识',
          align: 'center',
          width: 100,
          dataIndex: 'yaSimpleName'
        },
        {
          title: '审核渠道',
          align: 'center',
          dataIndex: 'reviewChannel'
        },
        {
          title: '帐号登录地址',
          align: 'center',
          dataIndex: 'loginUrl'
        },
        {
          title: '角色信息地址',
          align: 'center',
          dataIndex: 'roleUrl'
        },
        {
          title: '实名认证地址',
          align: 'center',
          dataIndex: 'authUrl'
        },
        {
          title: '敏感词检测地址',
          align: 'center',
          dataIndex: 'checkTextUrl'
        },
        {
          title: '账号登录地址',
          align: 'center',
          dataIndex: 'accountLoginUrl'
        },
        // {
        //   title: '支付验证地址',
        //   align: 'center',
        //   dataIndex: 'payUrl'
        // },
        // {
        //   title: '账号注册地址',
        //   align: 'center',
        //   dataIndex: 'accountRegisterUrl'
        // },
        // {
        //   title: '苹果登录回调',
        //   align: 'center',
        //   dataIndex: 'oauthRedirectUrl'
        // },
        {
          title: '区服列表地址',
          align: 'center',
          dataIndex: 'serverUrl'
        },
        {
          title: '公告地址',
          align: 'center',
          dataIndex: 'noticeUrl'
        },
        {
          title: '关闭注册天数',
          align: 'center',
          dataIndex: 'offRegisterDay'
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remark'
        },
        {
          title: '操作',
          width: 80,
          dataIndex: 'action',
          align: 'center',
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: 'game/info/list',
        delete: 'game/info/delete',
        deleteBatch: 'game/info/deleteBatch',
        refreshConfig: 'game/info/refreshConfig'
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    refreshConfig() {
      // 开始刷新游戏配置
      getAction(this.url.refreshConfig).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.error(res.message);
        }
      });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
