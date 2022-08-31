<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="游戏编号">
              <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库 -->
              <j-dict-select-tag v-model="queryParam.gameId" placeholder="请选择游戏编号" dictCode="game_info,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="16">
            <a-form-item label="区服id">
              <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.id_begin"></a-input>
              <span class="query-group-split-cust"></span>
              <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.id_end"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="名字">
              <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库 -->
              <j-dict-select-tag v-model="queryParam.id" placeholder="请选择名字" dictCode="game_server,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="标签">
              <j-dict-select-tag v-model="queryParam.tagId" placeholder="请选择标签"
                                 dictCode="game_server_tag,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag v-model="queryParam.status" placeholder="请选择状态" dictCode="server_status"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="类型">
              <j-dict-select-tag v-model="queryParam.type" placeholder="请选择类型" dictCode="server_type"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="开服时间">
              <a-range-picker v-model="queryParam.openTimeRange" format="YYYY-MM-DD"
                              :placeholder="['开始时间', '结束时间']" @change="onOpenDateChange"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD"
                                :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange"/>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="地址">
                <j-input placeholder="请输入地址" v-model="queryParam.host"></j-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-form-item label="数据库Host">
                <j-input placeholder="请输入数据库Host" v-model="queryParam.dbHost"></j-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
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
      <a-button :disabled="selectedRowKeys.length <= 0" @click="updateActivity" type="primary" icon="sync">
        刷新活动配置
      </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="updateSetting" type="primary" icon="sync">刷新游戏配置
      </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="startMaintain" v-has="'game:server:admin'" type="danger"
                icon="alert">开启维护!!!
      </a-button>
      <a-button :disabled="selectedRowKeys.length <= 0" @click="stopMaintain" v-has="'game:server:admin'" type="danger"
                icon="alert">结束维护!!!
      </a-button>

      <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏服配置')">导出</a-button> -->
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div> -->

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link"> 更多 <a-icon type="down"/> </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <span slot="tagSlot" slot-scope="text, record">
          <a-tag color="orange">{{ text }}</a-tag>
        </span>
        <span slot="maintainSlot" slot-scope="text, record">
          <a-tag v-if="record.isMaintain == 1" color="red">维护中</a-tag>
          <a-tag v-else color="green">运行中</a-tag>
        </span>
        <span slot="statSlot" slot-scope="text, record">
          <a-tag v-if="record.status == 0" color="blue">正常</a-tag>
          <a-tag v-else-if="record.status == 1" color="green">流畅</a-tag>
          <a-tag v-else-if="record.status == 2" color="red">火爆</a-tag>
          <a-tag v-else-if="record.status == 3" color="gray">维护</a-tag>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <game-server-modal ref="modalForm" @ok="modalFormOk"></game-server-modal>
  </a-card>
</template>

<script>
import GameServerModal from './modules/GameServerModal';
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {filterObj} from '@/utils/util';
import {getAction} from '@/api/manage';
import JInput from '@/components/jeecg/JInput';

function filterGameIdText(options, text) {
  if (options instanceof Array) {
    for (let game of options) {
      if (text === game.id) {
        return game.name + '(' + game.id + ')';
      }
    }
  }
  return text;
}

export default {
  name: 'GameServerList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameServerModal
  },
  data() {
    return {
      description: '游戏服配置',
      gameList: [],
      isorter: {
        column: 'id',
        order: 'desc'
      },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          fixed: 'left',
          width: 40,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '区服id',
          align: 'center',
          fixed: 'left',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '名字',
          align: 'left',
          fixed: 'left',
          width: 100,
          dataIndex: 'name'
        },
        {
          title: '标签',
          align: 'center',
          width: 100,
          dataIndex: 'tag',
          scopedSlots: {customRender: 'tagSlot'}
        },
        {
          title: '备注',
          align: 'left',
          width: 100,
          dataIndex: 'remark'
        },
        {
          title: '游戏编号',
          align: 'center',
          width: 100,
          dataIndex: 'gameId',
          customRender: text => {
            return filterGameIdText(this.gameList, text);
          }
        },
        {
          title: '服务器Host',
          align: 'left',
          width: 120,
          dataIndex: 'host'
        },
        {
          title: 'Websocket地址',
          align: 'left',
          width: 120,
          dataIndex: 'loginUrl'
        },
        {
          title: 'GM地址',
          align: 'left',
          width: 120,
          dataIndex: 'gmUrl'
        },
        {
          title: '在线人数',
          align: 'center',
          width: 60,
          dataIndex: 'onlineNum',
          customRender: text => {
            if (text === null || text === '' || text === undefined) {
              return 'N/A';
            }
            return text;
          }
        },
        {
          title: 'GM开关',
          align: 'left',
          width: 60,
          dataIndex: 'gmStatus',
          customRender: value => {
            let text = '--';
            if (value === 1) {
              text = '开启';
            } else if (value === 0) {
              text = '关闭';
            }
            return text;
          }
        },
        {
          title: '数数开关',
          align: 'left',
          width: 60,
          dataIndex: 'taStatistics',
          customRender: value => {
            let text = '--';
            if (value === 1) {
              text = '开启';
            } else if (value === 0) {
              text = '关闭';
            }
            return text;
          }
        },
        {
          title: '状态',
          align: 'center',
          width: 80,
          scopedSlots: {customRender: 'statSlot'}
        },
        {
          title: '维护状态',
          align: 'center',
          width: 80,
          dataIndex: 'isMaintain',
          scopedSlots: {customRender: 'maintainSlot'}
        },
        {
          title: '推荐标识',
          align: 'center',
          width: 80,
          dataIndex: 'recommend_dictText'
        },
        {
          title: '类型',
          align: 'center',
          width: 60,
          dataIndex: 'type_dictText'
        },
        {
          title: '开服时间',
          align: 'center',
          width: 120,
          dataIndex: 'openTime'
        },
        {
          title: '上线时间',
          align: 'center',
          width: 120,
          dataIndex: 'onlineTime'
        },
        {
          title: '操作',
          align: 'center',
          fixed: 'right',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: 'game/gameServer/list',
        delete: 'game/gameServer/delete',
        deleteBatch: 'game/gameServer/deleteBatch',
        updateActivity: 'game/gameServer/updateActivity',
        updateSetting: 'game/gameServer/updateSetting',
        getOnlineNum: 'game/gameServer/getOnlineNum',
        startMaintain: 'game/gameServer/startMaintain',
        stopMaintain: 'game/gameServer/stopMaintain',
        // exportXlsUrl: "game/gameServer/exportXls",
        // importExcelUrl: "game/gameServer/importExcel",
        // 游戏列表
        gameInfoListUrl: 'game/gameInfo/list'
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  created() {
    this.queryGameInfoList();
  },
  methods: {
    queryGameInfoList() {
      let that = this;
      getAction(that.url.gameInfoListUrl).then(res => {
        if (res.success) {
          if (res.result instanceof Array) {
            this.gameList = res.result;
          } else if (res.result.records instanceof Array) {
            this.gameList = res.result.records;
          }
        } else {
          this.gameList = [];
        }
      });
    },
    getQueryParams() {
      console.log(this.queryParam.createTimeRange);
      console.log(this.queryParam.openTimeRange);

      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;

      // 范围参数不传递后台
      delete param.createTimeRange;
      delete param.openTimeRange;
      return filterObj(param);
    },
    onCreateDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
    onOpenDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.openTime_begin = dateString[0];
      this.queryParam.openTime_end = dateString[1];
    },
    onDateOk(value) {
      console.log(value);
    },
    updateActivity: function () {
      this.batchAction(this.url.updateActivity, false);
    },
    updateSetting: function () {
      this.batchAction(this.url.updateSetting, false);
    },
    startMaintain: function () {
      this.batchAction(this.url.startMaintain, true, '确定开启维护状态？', '开启维护状态将导致所有玩家掉线');
    },
    stopMaintain: function () {
      this.batchAction(this.url.stopMaintain, true, '确定关闭维护状态？', '关闭维护状态将允许玩家上线');
    }
  }
};
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
