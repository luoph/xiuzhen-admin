<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="标题">
              <j-input placeholder="请输入标题模糊查询" v-model="queryParam.title"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="描述">
              <j-input placeholder="请输入描述模糊查询" v-model="queryParam.remark"></j-input>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="8">
            <a-form-item label="目标主体">
              <j-input placeholder="请输入玩家id/区服id模糊查询" v-model="queryParam.targetBodyIds"></j-input>
            </a-form-item>
          </a-col>
          <!--                    <a-col :md="4" :sm="8">-->
          <!--                        <a-form-item label="类型">-->
          <!--                            <a-select placeholder="目标类型" ref="targetSelector" v-model="queryParam.targetBodyType" @change="selectTarget">-->
          <!--                                <a-select-option :value="1">玩家</a-select-option>-->
          <!--                                <a-select-option :value="2">全服</a-select-option>-->
          <!--                            </a-select>-->
          <!--                        </a-form-item>-->
          <!--                    </a-col>-->
          <template v-if="toggleSearchStatus">
            <a-col :md="4" :sm="8">
              <a-form-item label="状态">
                <a-select placeholder="邮件状态" v-model="queryParam.validState">
                  <a-select-option :value="0">未审核</a-select-option>
                  <a-select-option :value="1">已审核发送</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="10" :sm="8">
              <a-form-item label="时间">
                <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="4" :sm="8">
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
    </div>

    <div>
      <a-table ref="table" size="middle" bordered rowKey="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" @change="handleTableChange">
        <template slot="largeText" slot-scope="text">
          <div class="large-text-container">
            <span class="large-text">{{ text }}</span>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleCopy(record)">复制</a>
          <a-divider type="vertical" />
          <a v-if="record.validState === 0" @click="handleCheck(record)">审核</a>
          <a v-else>已发送</a>
          <a-divider v-if="record.validState === 0" type="vertical" />
          <a v-if="record.validState === 0" @click="handleEdit(record)">编辑</a>
        </span>
      </a-table>
    </div>

    <game-email-modal ref="modalForm" @ok="modalFormOk"></game-email-modal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import GameEmailModal from './modules/GameEmailModal';
import JDate from '@/components/jeecg/JDate.vue';
import ServerSelect from '@/components/gameserver/ServerSelect';
import MultipleServerSelect from '@/components/gameserver/MultipleServerSelect';
import JInput from '@/components/jeecg/JInput';
import { getAction } from '@api/manage';

export default {
  name: 'GameEmailList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    JInput,
    GameEmailModal,
    ServerSelect,
    MultipleServerSelect
  },
  data() {
    return {
      description: '游戏下发邮件管理页面',
      queryParam: {
        validState: undefined,
        targetBodyType: undefined
      },

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
          title: '标题',
          align: 'left',
          width: 180,
          dataIndex: 'title'
        },
        {
          title: '描述',
          align: 'left',
          width: 240,
          dataIndex: 'remark',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '类型',
          align: 'center',
          width: 80,
          dataIndex: 'emailType',
          customRender: function(text) {
            return text === 1 ? '无附件' : '有附件';
          }
        },
        {
          title: '附件',
          align: 'center',
          width: 240,
          dataIndex: 'content',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '状态',
          align: 'center',
          width: 80,
          dataIndex: 'validState',
          customRender: function(text) {
            return text === 1 ? '已审核发送' : '未审核';
          }
        },
        {
          title: '目标类型',
          align: 'center',
          width: 80,
          dataIndex: 'targetBodyType',
          customRender: function(text) {
            return text === 1 ? '玩家' : '全服';
          }
        },
        {
          title: '目标主体',
          align: 'left',
          width: 220,
          dataIndex: 'targetBodyIds',
          scopedSlots: { customRender: 'largeText' }
        },
        {
          title: '生效时间',
          align: 'center',
          dataIndex: 'sendTime'
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'validStarTime'
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'validEndTime'
        },
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
        list: 'game/gameEmail/list?column=Id&order=desc',
        isCheck: 'game/gameEmail/check'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  mounted() {},
  methods: {
    initDictConfig() {},

    onDateChange: function(value, dateStr) {
      this.queryParam.validStarTime_begin = dateStr[0];
      this.queryParam.validStarTime_end = dateStr[1];
    },

    handleCopy: function(record) {
      this.$refs.modalForm.add(record);
    },

    handleCheck: function(record) {
      const that = this;
      getAction(that.url.isCheck, { id: record.id })
        .then(res => {
          if (res.success) {
            that.$message.success('审核成功！');
          } else {
            that.$message.error('审核发送失败！');
          }
        })
        .finally(() => {
          that.loadData();
        });
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';

.large-text-container {
  display: flex;
  overflow-x: hidden;
  overflow-y: auto;
  max-height: 200px;
}

.large-text {
  white-space: normal;
  word-break: break-word;
}
</style>
