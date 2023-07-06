<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="主机名">
              <j-search-select-tag placeholder="请选择主机名" v-model="queryParam.hostname" dict="game_vps,hostname,hostname" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="公网ip">
              <a-input placeholder="请输入公网ip" v-model="queryParam.ip" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="内网ip">
              <a-input placeholder="请输入内网ip" v-model="queryParam.lan" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="系统">
                <a-input placeholder="请输入系统" v-model="queryParam.os" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
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
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a-button type="primary" size="small" @click="handleEdit(record)"> 编辑 </a-button>
          <a-divider />
          <a-button size="small" @click="handleCopy(record)"> 复制 </a-button>
          <a-divider />
          <a-button type="danger" size="small">
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)"><a>删除</a></a-popconfirm>
          </a-button>
        </span>
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <span slot="blueTags" slot-scope="text">
          <a-tag v-if="!text" class="ant-tag-no-margin">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
        </span>
        <span slot="greenTags" slot-scope="text">
          <a-tag v-if="!text" class="ant-tag-no-margin">未配置</a-tag>
          <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="green">{{ tag }}</a-tag>
        </span>
        <span slot="loadSlot" slot-scope="text, record">
          <a-tag color="blue" class="ant-tag-no-margin">{{ record.fiveLoad }} / {{ record.fifteenLoad }}</a-tag>
        </span>
        <span slot="perSlot" slot-scope="text">
          <a-progress type="circle" :width="70" :strokeWidth="8" stroke-linecap="square" :percent="text" :stroke-color="getPercentColor(text)" />
        </span>
        <span slot="ipSlot" slot-scope="text, record">
          <div class="ip-container">
            <a-tag class="ant-tag-no-margin">公网</a-tag><a @click="copyText(record.ip)" class="copy-text"> {{ record.ip }} <a-icon type="copy" /></a>
            <a-divider />
            <a-tag class="ant-tag-no-margin">内网</a-tag><a @click="copyText(record.lan)" class="copy-text"> {{ record.lan }} <a-icon type="copy" /></a>
          </div>
        </span>
        <span slot="serverNumSlot" slot-scope="text, record" class="server-num-container">
          <a-tag class="ant-tag-no-margin">区服</a-tag> {{ record.gameServerNum }}
          <a-divider />
          <a-tag class="ant-tag-no-margin">跨服</a-tag> {{ record.crossServerNum }}
        </span>
        <span slot="diskSlot" slot-scope="text">
          <div class="disk-usage-container">
            <li v-for="(item, index) in text" :key="item.fileSystem">
              <a-tag>{{ item.fileSystem }}</a-tag>
              <a-tag :color="getPercentColor(item.usedPer)">{{ item.avail }}</a-tag>
              <a-tag>{{ item.diskSize }} </a-tag>
              <div class="disk-usage-progress">
                <a-progress size="small" :strokeWidth="8" stroke-linecap="square" :percent="item.usedPer" :stroke-color="getPercentColor(item.usedPer)" />
              </div>
              <a-divider v-if="index !== text.length - 1" />
            </li>
          </div>
        </span>
        <span slot="nameTitle">名称 <a-icon type="copy" /></span>
        <span slot="hostnameTitle">主机名 <a-icon type="copy" /></span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <!-- 表单区域 -->
    <game-vps-modal ref="modalForm" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JInput from '@/components/jeecg/JInput';
import GameVpsModal from '@views/game/modules/GameVpsModal.vue';
import { filterObj } from '@/utils/util';

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
          // title: '名称',
          align: 'center',
          dataIndex: 'name',
          slots: { title: 'nameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          // title: '主机名',
          align: 'center',
          dataIndex: 'hostname',
          slots: { title: 'hostnameTitle' },
          scopedSlots: { customRender: 'copySlot' }
        },
        {
          title: 'IP地址',
          dataIndex: 'ip',
          scopedSlots: { customRender: 'ipSlot' }
        },
        // {
        //   title: '内网ip',
        //   align: 'center',
        //   dataIndex: 'lan',
        //   customRender: (value) => {
        //     return value || '--';
        //   }
        // },
        // {
        //   title: '系统',
        //   align: 'center',
        //   dataIndex: 'platform',
        //   customRender: (value) => {
        //     return value || '--';
        //   }
        // },
        // {
        //   title: '平台',
        //   align: 'center',
        //   dataIndex: 'platform',
        //   customRender: (value) => {
        //     return value || '--';
        //   }
        // },
        {
          title: '区服/跨服数',
          align: 'center',
          width: 100,
          scopedSlots: { customRender: 'serverNumSlot' }
        },
        {
          title: '在线数',
          align: 'center',
          width: 60,
          dataIndex: 'onlineNum'
        },
        {
          title: '区服id',
          align: 'center',
          dataIndex: 'gameServerIds',
          scopedSlots: { customRender: 'blueTags' }
        },
        {
          title: '跨服id',
          align: 'center',
          dataIndex: 'crossServerIds',
          scopedSlots: { customRender: 'greenTags' }
        },
        {
          title: 'CPU核数',
          align: 'center',
          width: 80,
          dataIndex: 'cpuCoreNum'
        },
        {
          title: 'CPU%',
          align: 'center',
          width: 80,
          dataIndex: 'cpuPer',
          scopedSlots: { customRender: 'perSlot' }
        },
        {
          title: '内存%',
          align: 'center',
          width: 80,
          dataIndex: 'memPer',
          scopedSlots: { customRender: 'perSlot' }
        },
        {
          title: '磁盘占用',
          align: 'center',
          dataIndex: 'diskList',
          scopedSlots: { customRender: 'diskSlot' }
        },
        {
          title: '负载(5/15)',
          align: 'center',
          width: 80,
          dataIndex: '',
          scopedSlots: { customRender: 'loadSlot' }
        },
        // {
        //   title: '启动时间',
        //   align: 'center',
        //   width: 100,
        //   dataIndex: 'bootTime'
        // },
        {
          title: '状态更新',
          align: 'center',
          width: 100,
          dataIndex: 'uploadTime'
        },
        {
          title: '操作',
          width: 100,
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: 'game/vps/list',
        delete: 'game/vps/delete',
        deleteBatch: 'game/vps/deleteBatch'
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
    getPercentColor(value) {
      return value >= 80 ? '#FF5252' : value >= 60 ? '#FFAB00' : '#00C853';
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.copy-text {
  white-space: nowrap;
  color: rgba(0, 0, 0, 0.65);
}

.ant-divider-horizontal {
  margin: 6px 0 6px 0;
  padding: 0 10px 0 10px;
}

.ant-tag-no-margin {
  margin-right: auto !important;
}

.ip-container {
  min-width: 170px;
  max-width: 240px;
  white-space: nowrap;
}

.server-num-container {
  min-width: 80px;
  max-width: 140px;
  white-space: nowrap;
}

.disk-usage-container {
  min-width: 210px;
  max-width: 480px;
}

.disk-usage-progress {
  margin: 0px 20px 0px 20px;
}

.circle-progress {
  padding: 20px 20px 20px 20px;
}
</style>
