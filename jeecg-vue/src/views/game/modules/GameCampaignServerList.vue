<template>
  <a-modal :title="title" :width="1200" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-tabs :activeKey="tabIndex" @change="handleTabChange">
        <!-- 查询区域 -->
        <a-tab-pane v-for="(row, index) in model.typeList" :key="index" :tab="row.name">
          <div class="table-page-search-wrapper">
            <a-form layout="inline" :form="form" @keyup.enter.native="searchQuery">
              <a-row :gutter="10">
                <a-col :md="10" :sm="8">
                  <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服">
                    <a-input placeholder="搜索区服Id或名称" v-model="queryParam.server"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <span style="float: left" class="table-page-search-submitButtons">
                    <a-button type="primary" @click="searchQuery">查询</a-button>
                    <a-button type="primary" @click="searchReset">重置</a-button>
                  </span>
                </a-col>
              </a-row>
            </a-form>
          </div>

          <!-- 操作按钮区域 -->
          <div class="table-operator">
            <a-button :disabled="selectedRowKeys.length <= 0" @click="batchSwitchServer(1)" type="primary">批量开启
            </a-button>
            <a-button :disabled="selectedRowKeys.length <= 0" @click="batchSwitchServer(0)" type="danger">批量关闭
            </a-button>
          </div>

          <!-- table区域-begin -->
          <div>
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="serverId"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              @change="handleTableChange"
              :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            >
              <span slot="action" slot-scope="text, record">
                <a v-if="record.status === 1" @click="switchServer(record, 0)">关闭</a>
                <a v-else @click="switchServer(record, 1)">开启</a>
              </span>
              <template slot="statusSlot" slot-scope="text">
                <a-tag v-if="text === -1" color="#f1ab52">未开启</a-tag>
                <a-tag v-else-if="text === 0" color="#f50">已关闭</a-tag>
                <a-tag v-else-if="text === 1" color="#aaaaaa">未开始</a-tag>
                <a-tag v-else-if="text === 2" color="#87d068">进行中</a-tag>
                <a-tag v-else-if="text === 3" color="#595959">已结束</a-tag>
                <span v-else>{{ text }}</span>
              </template>
            </a-table>
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>
import {getAction} from '@/api/manage';
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import {filterObj} from '@/utils/util';

export default {
  name: 'GameChannelServerList',
  mixins: [JeecgListMixin],
  components: {},
  data() {
    return {
      description: '活动状态',
      // 查询参数
      title: '操作',
      visible: false,
      confirmLoading: false,
      model: {},
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
          title: '区服Id',
          align: 'center',
          dataIndex: 'serverId'
        },
        {
          title: '区服名',
          align: 'center',
          dataIndex: 'serverName'
        },
        {
          title: '开服时间',
          align: 'center',
          dataIndex: 'openTime'
        },
        {
          title: '活动开关',
          align: 'center',
          dataIndex: 'status',
          customRender: function (text) {
            if (text === -1) {
              return '未设置';
            } else if (text === 0) {
              return '关闭';
            } else if (text === 1) {
              return '开启';
            }
          }
        },
        {
          title: '活动状态',
          align: 'center',
          dataIndex: 'campaignStatus',
          scopedSlots: {customRender: 'statusSlot'}
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: {customRender: 'action'}
        }
      ],
      // 页签信息
      tabIndex: 0,
      labelCol: {
        xs: {span: 24},
        sm: {span: 5}
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16}
      },
      form: this.$form.createForm(this),
      url: {
        list: 'game/gameCampaign/serverList',
        typeList: 'game/gameCampaignType/list',
        switch: 'game/gameCampaign/serverSwitch',
        batch: 'game/gameCampaign/switchBatch'
      }
    };
  },
  computed: {},
  created() {
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
      if (!params.typeId) {
        return;
      }

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
    loadTypeList() {
      if (!this.model.id) {
        return;
      }

      // 查询条件
      let that = this;
      that.loading = true;
      that.confirmLoading = true;
      var params = {campaignId: this.model.id};
      getAction(that.url.typeList, params).then((res) => {
        if (res.success && res.result && res.result.records) {
          that.model.typeList = res.result.records;
        }
        that.confirmLoading = false;

        // 加载对应页签的区服状态
        that.loadData();
      });
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.tabIndex = 0;
      this.visible = true;

      this.form.resetFields();
      this.loadTypeList();
    },
    close() {
      this.$emit('close');
      this.visible = false;
      this.form.resetFields();
      this.dataSource = [];
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.campaignId = this.model.id;
      if (this.model && this.model.typeList) {
        param.typeId = this.model.typeList[this.tabIndex].id;
      }

      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    handleCancel() {
      this.close();
    },
    handleOk() {
      this.close();
    },
    handleTabChange(tab) {
      this.tabIndex = tab;
      this.loadData();
    },
    switchServer(record, status) {
      var params = {
        typeId: record.typeId,
        campaignId: record.campaignId,
        serverId: record.serverId,
        status: status
      };
      let that = this;
      getAction(that.url.switch, params).then(() => {
        that.loadData();
      });
    },
    batchSwitchServer(status) {
      var ids = '';
      for (var a = 0; a < this.selectedRowKeys.length; a++) {
        ids += this.selectedRowKeys[a] + ',';
      }

      var that = this;
      that.loading = true;

      var params = {
        campaignId: this.model.id,
        typeId: this.model.typeList[this.tabIndex].id,
        server: ids,
        status: status
      };

      getAction(that.url.batch, params).then((res) => {
        that.onClearSelected();
        if (res.success) {
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
}
</style>
