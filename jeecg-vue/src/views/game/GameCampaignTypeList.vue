<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="活动id">
              <a-input placeholder="请输入活动id" v-model="queryParam.campaignId" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="活动名称">
              <j-input placeholder="请输入活动名称" v-model="queryParam.name" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="时间类型">
              <a-select placeholder="请选择时间类型" v-model="queryParam.timeType" initialValue="1">
                <a-select-option :value="1">1-时间范围</a-select-option>
                <a-select-option :value="2">2-开服第N天</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="活动类型">
              <a-select placeholder="选择活动类型" v-model="queryParam.type" default-value="1" showSearch allowClear style="width: 100%">
                <a-select-option :value="1">1-登录礼包</a-select-option>
                <a-select-option :value="2">2-累计充值</a-select-option>
                <a-select-option :value="3">3-节日兑换</a-select-option>
                <a-select-option :value="4">4-节日任务</a-select-option>
                <a-select-option :value="5">5-修为加成</a-select-option>
                <a-select-option :value="6">6-灵气加成</a-select-option>
                <a-select-option :value="7">7-节日掉落</a-select-option>
                <a-select-option :value="8">8-节日烟花</a-select-option>
                <a-select-option :value="9">9-消耗排行</a-select-option>
                <a-select-option :value="10">10-限时仙剑</a-select-option>
                <a-select-option :value="11">11-砸蛋</a-select-option>
                <a-select-option :value="12">12-砸蛋榜</a-select-option>
                <a-select-option :value="13">13-砸蛋礼包</a-select-option>
                <a-select-option :value="14">14-节日派对</a-select-option>
                <a-select-option :value="15">15-直购礼包</a-select-option>
                <a-select-option :value="16">16-返利狂欢</a-select-option>
                <a-select-option :value="17">17-赠酒排行榜</a-select-option>
                <a-select-option :value="18">18-魅力值排行榜</a-select-option>
                <a-select-option :value="20">20-自选特惠</a-select-option>
                <a-select-option :value="21">21-累充排行</a-select-option>
                <a-select-option :value="22">22-邮件活动</a-select-option>
                <a-select-option :value="23">23-遗迹夺宝</a-select-option>
                <a-select-option :value="24">24-阶段任务</a-select-option>
                <a-select-option :value="25">25-夺宝战令</a-select-option>
                <a-select-option :value="26">26-召唤活动</a-select-option>
                <a-select-option :value="27">27-召唤排行</a-select-option>
                <a-select-option :value="28">28-超值礼包</a-select-option>
                <a-select-option :value="29">29-单日仙玉返利</a-select-option>
                <a-select-option :value="30">30-单日道具返利</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <!-- <a-col :md="12" :sm="8">
                            <a-form-item label="排序">
                                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.sort_begin"/>
                                <span class="query-group-split-cust" />
                                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.sort_end"/>
                            </a-form-item>
                        </a-col> -->
            <a-col :md="8" :sm="8">
              <a-form-item label="活动开始时间">
                <a-range-picker v-model="queryParam.startTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onStartTimeChange" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="活动结束时间">
                <a-range-picker v-model="queryParam.endTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onEndTimeChange" />
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
      <a-button type="primary" icon="download" @click="handleExportXls('活动类型配置')">导出</a-button>
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
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" alt="图片不存在" class="list-image" />
        </template>
        <span slot="crossSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="green">本服</a-tag>
          <a-tag v-else-if="text === 1" color="blue">跨服</a-tag>
          <a-tag v-else>未设置</a-tag>
        </span>
        <span slot="timeSlot" slot-scope="text, record">
          <div v-if="record.timeType == 1">
            <a-tag color="blue">{{ record.startTime }}</a-tag>
            <a-tag color="blue">{{ record.endTime }}</a-tag>
          </div>
          <div v-if="record.timeType == 2">
            <a-tag color="green">开服第{{ record.startDay }}天</a-tag>
            <a-tag color="green">持续{{ record.duration }}天</a-tag>
          </div>
        </span>
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

    <game-campaign-type-modal ref="modalForm" @ok="modalFormOk"></game-campaign-type-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameCampaignTypeModal from './modules/GameCampaignTypeModal';
import JDate from '@/components/jeecg/JDate.vue';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameCampaignTypeList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    JInput,
    GameCampaignTypeModal
  },
  data() {
    return {
      description: '活动类型配置管理页面',
      queryParam: {
        startTimeRange: null,
        endTimeRange: null
      },
      // 表头
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key: 'rowIndex',
        //   width: 60,
        //   align: 'center',
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1;
        //   }
        // },
        {
          title: '主活动id',
          align: 'center',
          dataIndex: 'campaignId',
          width: 100
        },
        {
          title: '子活动id',
          align: 'center',
          dataIndex: 'id',
          width: 100
        },
        {
          title: '活动名称',
          align: 'center',
          dataIndex: 'name',
          width: 180
        },
        {
          title: '活动类型',
          align: 'center',
          dataIndex: 'type',
          width: 180,
          customRender: (value) => {
            let re = '--';
            if (value === 1) {
              re = '1-登录礼包';
            } else if (value === 2) {
              re = '2-累计充值';
            } else if (value === 3) {
              re = '3-节日兑换';
            } else if (value === 4) {
              re = '4-节日任务';
            } else if (value === 5) {
              re = '5-修为加成';
            } else if (value === 6) {
              re = '6-灵气加成';
            } else if (value === 7) {
              re = '7-节日掉落';
            } else if (value === 8) {
              re = '8-节日烟花';
            } else if (value === 9) {
              re = '9-消耗排行';
            } else if (value === 10) {
              re = '10-限时仙剑';
            } else if (value === 11) {
              re = '11-砸蛋';
            } else if (value === 12) {
              re = '12-砸蛋榜单';
            } else if (value === 13) {
              re = '13-砸蛋礼包';
            } else if (value === 14) {
              re = '14-节日派对';
            } else if (value === 15) {
              re = '15-直购礼包';
            } else if (value === 16) {
              re = '16-返利狂欢';
            } else if (value === 17) {
              re = '17-赠酒排行榜';
            } else if (value === 18) {
              re = '18-魅力值排行榜';
            } else if (value === 20) {
              re = '20-自选特惠';
            } else if (value === 21) {
              re = '21-累充排行';
            } else if (value === 22) {
              re = '22-邮件活动';
            } else if (value === 23) {
              re = '23-遗迹夺宝';
            } else if (value === 24) {
              re = '24-阶段任务';
            } else if (value === 25) {
              re = '25-夺宝战令';
            } else if (value === 26) {
              re = '26-召唤活动';
            } else if (value === 27) {
              re = '27-召唤排行';
            } else if (value === 28) {
              re = '28-超值礼包';
            } else if (value === 29) {
              re = '29-单日仙玉返利';
            } else if (value === 30) {
              re = '30-单日道具返利';
            }
            return re;
          }
        },
        {
          title: '活动宣传图',
          align: 'center',
          dataIndex: 'typeImage',
          width: 320,
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '排序',
          align: 'center',
          width: 80,
          dataIndex: 'sort'
        },
        {
          title: '是否跨服',
          align: 'center',
          dataIndex: 'cross',
          scopedSlots: { customRender: 'crossSlot' }
        },
        {
          title: '活动时间',
          align: 'center',
          dataIndex: 'startDay',
          scopedSlots: { customRender: 'timeSlot' }
        },
        // {
        //     title: "开始天数",
        //     align: "center",
        //     width: 80,
        //     dataIndex: "startDay"
        // },
        // {
        //     title: "持续天数",
        //     align: "center",
        //     width: 80,
        //     dataIndex: "duration"
        // },
        // {
        //     title: "活动开始时间",
        //     align: "center",
        //     width: 120,
        //     dataIndex: "startTime"
        // },
        // {
        //     title: "活动结束时间",
        //     align: "center",
        //     width: 120,
        //     dataIndex: "endTime"
        // },
        {
          title: '创建时间',
          align: 'center',
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
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    onStartTimeChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.startTime_begin = dateString[0];
      this.queryParam.startTime_end = dateString[1];
    },
    onEndTimeChange: function (value, dateString) {
      console.log(dateString[0], dateString[1]);
      this.queryParam.endTime_begin = dateString[0];
      this.queryParam.endTime_end = dateString[1];
    },
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
</style>
