<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="4" :sm="8">
            <a-form-item label="标题">
              <j-input placeholder="请输入标题模糊查询" v-model="queryParam.title" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="描述">
              <j-input placeholder="请输入描述模糊查询" v-model="queryParam.describe" />
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="8">
            <a-form-item label="目标主体">
              <j-input placeholder="请输入玩家ID/区服ID模糊查询" v-model="queryParam.receiverIds" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="类型">
              <a-select placeholder="目标类型" ref="targetSelector" v-model="queryParam.receiverType">
                <a-select-option :value="1">玩家</a-select-option>
                <a-select-option :value="2">区服</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="状态">
              <a-select placeholder="邮件状态" v-model="queryParam.state">
                <a-select-option :value="0">待审核</a-select-option>
                <a-select-option :value="1">已发送</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="创建人">
              <j-search-select-tag placeholder="请选择创建人" v-model="queryParam.createBy" dict="sys_user,realname,username" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="审核人">
              <j-search-select-tag placeholder="请选择审核人" v-model="queryParam.reviewBy" dict="sys_user,realname,username" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="创建时间">
              <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange" />
            </a-form-item>
          </a-col>
          <!--          <template v-if="toggleSearchStatus">-->
          <!--            <a-col :md="10" :sm="8">-->
          <!--              <a-form-item label="创建时间">-->
          <!--                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD"-->
          <!--                                :placeholder="['开始时间', '结束时间']" @change="onCreateDateChange"/>-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--          </template>-->
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              <!--              <a style="margin-left: 8px" @click="handleToggleSearch">-->
              <!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
              <!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
              <!--              </a>-->
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
      >
        <span slot="action" slot-scope="text, record">
          <a-button type="primary" size="small" @click="handleCopy(record)"> 复制 </a-button>
          <a-divider />
          <a-button size="small" v-if="record.state === 0" @click="handleEdit(record)"> 编辑 </a-button>
          <a-button size="small" v-if="record.state === 1"> 已发送 </a-button>
          <a-divider v-if="record.state === 0" v-has="'game:email:review'" />
          <a-button type="danger" size="small" v-if="record.state === 0" v-has="'game:email:review'">
            <a-popconfirm title="确定发送吗?" @confirm="() => handleReview(record.id)"><a>审核</a></a-popconfirm>
          </a-button>
          <a-divider v-has="'game:email:review'" />
          <a-button type="danger" size="small" v-has="'game:email:review'">
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)"><a>删除</a></a-popconfirm>
          </a-button>
        </span>
        <span slot="copySlot" slot-scope="text">
          <a @click="copyText(text)" class="copy-text">{{ text || '--' }}</a>
        </span>
        <div slot="largeTextSlot" slot-scope="text" class="scroll-container">
          <span class="scroll-span" @click="copyText(text)">{{ text || '--' }}</span>
        </div>
        <div slot="receiverIdsSlot" slot-scope="text, record" @click="copyText(text)" class="scroll-container">
          <span class="scroll-span">
            <a-tag v-if="!text">未设置</a-tag>
            <a-tag v-else-if="record.receiverType === 1" v-for="tag in text.split(',')" :key="tag" :color="playerIdColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
            <a-tag v-else-if="record.receiverType === 2" v-for="tag in text.split(',')" :key="tag" :color="tagColor(tag)" @click="copyText(tag)">{{ tag }}</a-tag>
          </span>
        </div>
        <span slot="stateSlot" slot-scope="text">
          <a-tag v-if="text === 0" color="red">待审核</a-tag>
          <a-tag v-else-if="text === 1" color="green">已发送</a-tag>
        </span>
        <span slot="typeSlot" slot-scope="text">
          <a-tag v-if="text === 1" color="green">有道具</a-tag>
          <a-tag v-else>无道具</a-tag>
        </span>
        <span slot="receiverTypeSlot" slot-scope="text">
          <a-tag v-if="text === 1" color="blue">玩家</a-tag>
          <a-tag v-else-if="text === 2" color="green">区服</a-tag>
        </span>
        <span slot="titleTitle" class="copy-text">标题 <a-icon type="copy" /></span>
        <span slot="describeTitle" class="copy-text">描述 <a-icon type="copy" /></span>
        <span slot="contentTitle" class="copy-text">附件 <a-icon type="copy" /></span>
        <span slot="receiverIdsTitle" class="copy-text">目标主体 <a-icon type="copy" /></span>
      </a-table>
    </div>
    <game-email-modal ref="modalForm" @ok="modalFormOk" />
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
import { filterObj } from '@/utils/util';

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
      description: '邮件列表',
      queryParam: {
        state: undefined,
        receiverType: undefined
      },
      /* 排序参数 */
      isorter: {
        column: 'id',
        order: 'desc'
      },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          width: 60,
          key: 'rowIndex',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '邮件id',
          width: 80,
          align: 'center',
          dataIndex: 'id'
        },
        {
          // title: '标题',
          dataIndex: 'title',
          slots: { title: 'titleTitle' },
          scopedSlots: { customRender: 'largeTextSlot' }
        },
        {
          // title: '描述',
          dataIndex: 'describe',
          slots: { title: 'describeTitle' },
          scopedSlots: { customRender: 'largeTextSlot' }
        },
        {
          // title: '附件',
          dataIndex: 'content',
          slots: { title: 'contentTitle' },
          scopedSlots: { customRender: 'largeTextSlot' }
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'state',
          scopedSlots: { customRender: 'stateSlot' }
        },
        {
          title: '目标类型',
          align: 'center',
          dataIndex: 'receiverType',
          scopedSlots: { customRender: 'receiverTypeSlot' }
        },
        {
          // title: '目标主体',
          dataIndex: 'receiverIds',
          slots: { title: 'receiverIdsTitle' },
          scopedSlots: { customRender: 'receiverIdsSlot' }
        },
        {
          title: '类型',
          align: 'center',
          dataIndex: 'type',
          scopedSlots: { customRender: 'typeSlot' }
        },
        {
          title: '生效时间',
          align: 'center',
          width: 100,
          dataIndex: 'sendTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '开始时间',
          align: 'center',
          width: 100,
          dataIndex: 'startTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '结束时间',
          align: 'center',
          width: 100,
          dataIndex: 'endTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '创建时间',
          align: 'center',
          fixed: 'right',
          width: 100,
          dataIndex: 'createTime',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '创建人',
          align: 'center',
          fixed: 'right',
          dataIndex: 'createBy',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '审核人',
          align: 'center',
          fixed: 'right',
          dataIndex: 'reviewBy',
          customRender: (value) => {
            return value || '--';
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          fixed: 'right',
          align: 'center',
          width: 120,
          scopedSlots: { customRender: 'action' }
        }
      ],

      url: {
        list: 'game/gameEmail/list',
        delete: 'game/gameEmail/delete',
        review: 'game/gameEmail/review'
      },
      dictOptions: {}
    };
  },
  computed: {},
  mounted() {},
  methods: {
    getQueryParams() {
      // console.log(this.queryParam.createTimeRange);
      var param = Object.assign({}, this.queryParam, this.isorter);
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
    handleReview: function (id) {
      const that = this;
      getAction(that.url.review, { id: id })
        .then((res) => {
          if (res.success) {
            that.$message.success(res.message);
          } else {
            that.$message.error(res.message);
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

.ant-divider-horizontal {
  margin: 6px 0 6px 0;
}

.scroll-container {
  min-width: 80px;
  max-width: 240px;
}
</style>
