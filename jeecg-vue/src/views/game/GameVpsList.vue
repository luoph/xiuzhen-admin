<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="主机名">
              <a-input placeholder="请输入主机名" v-model="queryParam.hostname"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="公网ip">
              <a-input placeholder="请输入公网ip" v-model="queryParam.ip"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="内网ip">
              <a-input placeholder="请输入内网ip" v-model="queryParam.lan"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="系统">
                <a-input placeholder="请输入系统" v-model="queryParam.os"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD"
                                :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('虚拟主机信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
        <span slot="blueTags" slot-scope="text, record">
          <a-tag v-if="!text" color="red">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="greenTags" slot-scope="text, record">
          <a-tag v-if="!text" color="red">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="green">{{ tag }}</a-tag>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <!-- 表单区域 -->
    <game-vps-modal ref="modalForm" @ok="modalFormOk"></game-vps-modal>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JInput from '@/components/jeecg/JInput';
import GameVpsModal from "@views/game/modules/GameVpsModal.vue";
import {filterObj} from "@/utils/util";

export default {
  name: 'GameVpsList',
  mixins: [JeecgListMixin],
  components: {
    JInput,
    GameVpsModal
  },
  data() {
    return {
      description: '虚拟主机列表页面',
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
          title: '名称',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '主机名',
          align: 'center',
          dataIndex: 'hostname',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '公网ip',
          align: 'center',
          dataIndex: 'ip',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '内网ip',
          align: 'center',
          dataIndex: 'lan',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '操作系统',
          align: 'center',
          dataIndex: 'os',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '游戏服数量',
          align: 'center',
          dataIndex: 'gameServerNum'
        },
        {
          title: '跨服数量',
          align: 'center',
          dataIndex: 'crossServerNum'
        },
        {
          title: '游戏服id',
          align: 'left',
          dataIndex: 'gameServerIds',
          scopedSlots: {customRender: 'blueTags'}
        },
        {
          title: '跨服id',
          align: 'left',
          dataIndex: 'crossServerIds',
          scopedSlots: {customRender: 'greenTags'}
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
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
        list: 'game/vps/list',
        delete: 'game/vps/delete',
        deleteBatch: 'game/vps/deleteBatch',
      }
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    getQueryParams() {
      const param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;

      // 范围参数不传递后台
      delete param.createTimeRange;
      return filterObj(param);
    },
    onCreateDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createTime_begin = dateString[0];
      this.queryParam.createTime_end = dateString[1];
    },
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
