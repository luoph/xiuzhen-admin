<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper"></div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入活动配置</a-button>
      </a-upload>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('节日活动页签配置')">导出</a-button> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
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

    <game-campaign-type-modal ref="modalForm" @ok="modalFormOk"></game-campaign-type-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import { getAction } from '../../api/manage';
import { filterObj } from '@/utils/util';
import GameCampaignTypeModal from './modules/GameCampaignTypeModal';
import JDate from '@/components/jeecg/JDate.vue';

export default {
  name: 'GameCampaignTypeList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameCampaignTypeModal
  },
  data() {
    return {
      description: '节日活动页签配置管理页面',
      model: {},
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
          title: '活动id',
          align: 'center',
          width: 80,
          dataIndex: 'campaignId'
        },
        {
          title: '页签id',
          align: 'center',
          width: 80,
          dataIndex: 'id'
        },
        {
          title: '页签名',
          align: 'center',
          width: 80,
          dataIndex: 'name'
        },
        {
          title: '活动类型',
          align: 'center',
          dataIndex: 'type',
          width: 120,
          customRender: value => {
            let text = '--';
            if (value === 1) {
              text = '1-登录礼包';
            } else if (value === 2) {
              text = '2-累计充值';
            } else if (value === 3) {
              text = '3-节日兑换';
            } else if (value === 4) {
              text = '4-节日任务';
            } else if (value === 5) {
              text = '5-修为加成';
            } else if (value === 6) {
              text = '6-灵气加成';
            } else if (value === 7) {
              text = '7-节日掉落';
            } else if (value === 8) {
              text = '8-节日烟花';
            } else if (value === 9) {
              text = '9-消费排行';
            } else if (value === 10) {
              text = '10-限时仙剑';
            } else if (value === 11) {
              text = '11-砸蛋';
            } else if (value === 12) {
              text = '12-砸蛋榜单';
            } else if (value === 13) {
              text = '13-砸蛋礼包';
            } else if (value === 14) {
              text = '14-节日派对';
            } else if (value === 15) {
              text = '15-直购礼包';
            } else if (value === 16) {
              text = '16-返利狂欢';
            } else if (value === 17) {
              text = '17-赠酒排行榜';
            } else if (value === 18) {
              text = '18-魅力值排行榜';
            } else if (value === 20) {
              text = '20-自选特惠';
            }
            return text;
          }
        },
        {
          title: '活动宣传图',
          align: 'center',
          width: 400,
          dataIndex: 'typeImage',
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '排序',
          align: 'center',
          width: 80,
          dataIndex: 'sort'
        },
        {
          title: '开始时间',
          align: 'center',
          width: 120,
          dataIndex: 'startTime'
        },
        {
          title: '结束时间',
          align: 'center',
          width: 120,
          dataIndex: 'endTime'
        },
        {
          title: '创建时间',
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
        list: 'game/gameCampaignType/list',
        delete: 'game/gameCampaignType/delete',
        deleteBatch: 'game/gameCampaignType/deleteBatch',
        exportXlsUrl: 'game/gameCampaignType/exportXls',
        importExcelUrl: 'game/gameCampaignType/importExcel'
      },
      dictOptions: {}
    };
  },
  computed: {},
  methods: {
    initDictConfig() {},
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
      getAction(this.url.list, params).then(res => {
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
      this.$refs.modalForm.add({ campaignId: this.model.id });
      this.$refs.modalForm.title = '新增节日页签配置';
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      // 活动id
      param.campaignId = this.model.id;
      return filterObj(param);
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domianURL']}/${text}`;
    },
    importExcelUrl() {
      let domianURL = window._CONFIG['domianURL'];
      return `${domianURL}/${this.url.importExcelUrl}?campaignId=${this.model.id}`;
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.list-image {
  width: 100%;
  height: 100px;
  object-fit: scale-down;
}
</style>
