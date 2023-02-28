<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="玩家id">
              <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="昵称">
              <a-input placeholder="请输入昵称模糊查询" v-model="queryParam.nickname"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="区服id">
              <a-input placeholder="请输入区服id" v-model="queryParam.serverId"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="渠道">
              <a-input placeholder="请输入渠道编码" v-model="queryParam.channel"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="账号">
              <a-input placeholder="请输入账号" v-model="queryParam.account"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="境界">
              <a-input placeholder="请输入境界" v-model="queryParam.realm"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="等级">
              <a-input placeholder="最小等级" class="query-group-cust" v-model="queryParam.level_begin"></a-input>
              <span class="query-group-split-cust"></span>
              <a-input placeholder="最大等级" class="query-group-cust" v-model="queryParam.level_end"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="16">
              <a-form-item label="战力范围">
                <a-input placeholder="最小战力值" class="query-group-cust"
                         v-model="queryParam.combatPower_begin"></a-input>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="最大战力值" class="query-group-cust"
                         v-model="queryParam.combatPower_end"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="创建时间">
                <a-range-picker v-model="queryParam.createDateRange" format="YYYY-MM-DD"
                                :placeholder="['开始时间', '结束时间']" @change="onDateChange"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="6" :sm="8">
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
      <!-- <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button> -->
      <a-button type="primary" icon="download" @click="handleExportXls('玩家信息')">导出</a-button>
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a
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
        :scroll="{ x: 'max-content' }"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
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

        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item :disabled="record.vipId > 0"
                           @click="addVip(record)">添加VIP</a-menu-item>
              <a-menu-item :disabled="record.vipId === 0"
                           @click="deleteVip(record)">删除VIP</a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <GamePlayerModal ref="modalForm" @ok="modalFormOk"></GamePlayerModal>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {deleteAction, getAction, postAction} from '@/api/manage';
import GamePlayerModal from './modules/GamePlayerModal';
import {filterObj} from '@/utils/util';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GamePlayerList',
  mixins: [JeecgListMixin],
  components: {
    GamePlayerModal,
    getAction,
    JInput
  },
  data() {
    return {
      description: '玩家信息管理页面',
      queryParam: {
        serverId: ''
      },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          fixed: 'left',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '玩家id',
          fixed: 'left',
          align: 'center',
          dataIndex: 'playerId'
        },
        {
          title: '角色昵称',
          fixed: 'left',
          align: 'center',
          dataIndex: 'nickname'
        },
        {
          title: '区服id',
          align: 'center',
          fixed: 'left',
          dataIndex: 'serverId'
        },
        {
          title: '账号',
          align: 'center',
          dataIndex: 'account'
        },
        {
          title: '渠道',
          align: 'center',
          dataIndex: 'channel'
        },
        {
          title: 'Sdk渠道',
          align: 'center',
          dataIndex: 'sdkChannel',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '等级',
          align: 'center',
          width: 60,
          dataIndex: 'level'
        },
        {
          title: '境界',
          align: 'center',
          width: 60,
          dataIndex: 'realm'
        },
        {
          title: '累计充值',
          align: 'center',
          width: 60,
          dataIndex: 'totalPayAmount'
        },
        {
          title: '当前主线任务id',
          align: 'center',
          width: 60,
          dataIndex: 'mainTaskId'
        },
        // {
        //   title: '跳过动画',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'skipCartoon'
        // },
        // {
        //   title: '背包大小',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'backpackSize'
        // },
        // {
        //   title: '背包等级',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'backpackLevel'
        // },
        {
          title: '修为值',
          align: 'center',
          dataIndex: 'practiceValue'
        },
        // {
        //   title: '修炼年数',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'practiceYear'
        // },
        {
          title: '战力',
          align: 'center',
          dataIndex: 'combatPower'
        },
        {
          title: '魅力值',
          align: 'center',
          dataIndex: 'charmValue'
        },
        {
          title: '本命灵根',
          align: 'center',
          dataIndex: 'spiritRootCode'
        },
        // {
        //   title: '修为加持状态',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'practiceState'
        // },
        // {
        //   title: '渡劫增加成功率',
        //   align: 'center',
        //   width: 60,
        //   dataIndex: 'successRate'
        // },
        {
          title: '修为结算时间',
          align: 'center',
          width: 120,
          dataIndex: 'settleTime'
        },
        {
          title: '等级升级时间',
          align: 'center',
          width: 120,
          dataIndex: 'levelUpdateTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '登录时间',
          align: 'center',
          width: 120,
          dataIndex: 'loginTime'
        },
        {
          title: '登录IP',
          align: 'center',
          dataIndex: 'loginIp',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '创角时间',
          align: 'center',
          width: 120,
          dataIndex: 'createTime'
        },
        {
          title: "操作",
          width: 100,
          dataIndex: "action",
          align: "center",
          fixed: 'right',
          scopedSlots: {customRender: "action"}
        }
      ],
      url: {
        list: 'player/playerInfo/list',
        addVip: 'game/vip/addVip',
        deleteVip: 'game/vip/delete',
        exportXlsUrl: 'player/playerInfo/exportXls'
      },
      dictOptions: {}
    };
  },
  created() {
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    getQueryParams() {
      console.log(this.queryParam.createDateRange);
      var param = Object.assign({}, this.queryParam, this.isorter);
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 范围参数不传递后台
      delete param.createDateRange;
      return filterObj(param);
    },
    onDateChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.createDate_begin = dateString[0];
      this.queryParam.createDate_end = dateString[1];
    },
    deleteVip(record) {
      this.requestUrlConfirm(this.url.deleteVip,
        {id: record.vipId},
        '是否删除VIP？',
        `删除玩家: ${record.playerId}（${record.nickname}）的VIP特权`,
        'delete');
    },
    addVip(record) {
      this.requestUrlConfirm(this.url.addVip,
        {playerIds: record.playerId},
        '是否添加VIP？',
        `添加玩家: ${record.playerId}（${record.nickname}）为VIP`);
    },
    requestUrlConfirm(url, parameter, title, content, method = 'get') {
      let that = this;
      let requestFunction = getAction;
      if (method === 'post') {
        requestFunction = postAction;
      } else if (method === 'delete') {
        requestFunction = deleteAction;
      }
      this.$confirm({
        title: title,
        content: content,
        onOk: function () {
          that.loading = true;
          requestFunction(url, parameter).then((res) => {
            that.loading = false;
            if (res.success) {
              that.$message.success(res.message);
            } else {
              that.$message.error(res.message);
            }
          }).finally(()=>{
            that.loading = false
            that.searchQuery();
          });
        }
      });
    },
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
