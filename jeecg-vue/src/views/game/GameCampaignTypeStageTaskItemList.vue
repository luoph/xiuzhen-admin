<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('节日活动-阶段任务-任务')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
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
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
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

    <game-campaign-type-stage-task-item-modal ref="modalForm" @ok="modalFormOk"></game-campaign-type-stage-task-item-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '../../api/manage'
  import { filterObj } from '@/utils/util'
  import GameCampaignTypeStageTaskItemModal from './modules/GameCampaignTypeStageTaskItemModal'

  export default {
    name: 'GameCampaignTypeStageTaskItemList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      GameCampaignTypeStageTaskItemModal
    },
    data () {
      return {
        description: '节日活动-阶段任务-任务管理页面',
        model: {},
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
            title:'主活动id',
            align:"center",
            dataIndex: 'campaignId'
          },
          {
            title:'子活动id',
            align:"center",
            dataIndex: 'typeId'
          },
          {
            title:'id',
            align:"center",
            dataIndex: 'id'
          },
          {
            title:'阶段',
            align:"center",
            dataIndex: 'stage'
          },
          {
            title:'任务id',
            align:"center",
            dataIndex: 'taskId'
          },
          {
            title:'描述',
            align:"center",
            dataIndex: 'description'
          },
          {
            title:'模块id',
            align:"center",
            dataIndex: 'moduleId'
          },
          {
            title:'任务完成条件',
            align:"center",
            dataIndex: 'target'
          },
          {
            title:'任务参数',
            align:"center",
            dataIndex: 'args'
          },
          {
            title:'奖励',
            align:"center",
            dataIndex: 'reward'
          },
          {
            title:'跳转id',
            align:"center",
            dataIndex: 'jumpId'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/game/gameCampaignTypeStageTaskItem/list",
          delete: "/game/gameCampaignTypeStageTaskItem/delete",
          deleteBatch: "/game/gameCampaignTypeStageTaskItem/deleteBatch",
          exportXlsUrl: "/game/gameCampaignTypeStageTaskItem/exportXls",
          importExcelUrl: "game/gameCampaignTypeStageTaskItem/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}?campaignId=${this.model.campaignId}&typeId=${this.model.id}`;
      },
    },
    methods: {
      loadData(arg) {
        if (!this.model.id) {
          return;
        }

        if (!this.url.list) {
          this.$message.error('请设置url.list属性!');
          return;
        }

        // 加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }

        // 查询条件
        var params = this.getQueryParams();
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success && res.result && res.result.records) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if (res.code === 510) {
            this.$message.warning(res.message);
          }
          this.loading = false;
        });
      },
      edit(record) {
        this.model = record;
        this.loadData();
      },
      handleAdd() {
        this.$refs.modalForm.add({ typeId: this.model.id, campaignId: this.model.campaignId });
        this.$refs.modalForm.title = '新增阶段任务-任务配置';
      },
      getQueryParams() {
        var param = Object.assign({}, this.queryParam);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        // typeId、活动id
        param.typeId = this.model.id;
        param.campaignId = this.model.campaignId;
        return filterObj(param);
      },
      getImgView(text) {
        if (text && text.indexOf(',') > 0) {
          text = text.substring(0, text.indexOf(','));
        }
        return `${window._CONFIG['domainURL']}/${text}`;
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'campaignId',text:'主活动id'})
        fieldList.push({type:'int',value:'typeId',text:'子活动id'})
        fieldList.push({type:'int',value:'stage',text:'阶段'})
        fieldList.push({type:'int',value:'taskId',text:'任务id'})
        fieldList.push({type:'string',value:'description',text:'描述'})
        fieldList.push({type:'int',value:'moduleId',text:'模块id'})
        fieldList.push({type:'int',value:'target',text:'任务完成条件'})
        fieldList.push({type:'int',value:'args',text:'任务参数'})
        fieldList.push({type:'string',value:'reward',text:'奖励'})
        fieldList.push({type:'int',value:'jumpId',text:'跳转id'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>