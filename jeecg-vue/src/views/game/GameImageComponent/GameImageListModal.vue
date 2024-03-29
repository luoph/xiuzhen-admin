<template>
  <a-modal centered :title="name + '选择'" :width="1200" :visible="visible" @ok="handleOk" @cancel="close" cancelText="关闭">
    <a-row :gutter="18">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="图片类型">
                <a-select placeholder="请选择图片类型" v-model="queryParam.type" default-value="1">
                  <a-select-option :value="1">图标</a-select-option>
                  <a-select-option :value="2">宣传图</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="备注">
                <j-input placeholder="请输入备注模糊查询" v-model="queryParam.remark" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="图片名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-input placeholder="请输入图片名" v-model="queryParam.name" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 查询区域-END -->
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
        :customRow="customRowFn"
        @change="handleTableChange"
      >
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
          <img v-else :src="getImgView(text)" alt="图片不存在" class="list-image" />
        </template>
      </a-table>
    </a-row>
  </a-modal>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JInput from '@/components/jeecg/JInput';

export default {
  name: 'GameListComponentModal',
  mixins: [JeecgListMixin],
  components: {
    JInput
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    visible: {
      type: Boolean,
      default: false
    },
    valueKey: {
      type: String,
      default: null
    },
    name: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      description: '游戏图片列表',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
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
          title: '图片类型',
          align: 'center',
          dataIndex: 'type',
          width: 100,
          customRender: (value) => {
            let text = '--';
            if (value === 1) {
              text = '图标';
            } else if (value === 2) {
              text = '宣传图';
            }
            return text;
          }
        },
        {
          title: '图片',
          align: 'center',
          dataIndex: 'imgUrl',
          width: 320,
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '文件名',
          align: 'center',
          width: 200,
          dataIndex: 'name'
        },
        {
          title: '图片尺寸',
          align: 'center',
          width: 120,
          customRender: function (t, r) {
            return r.width + 'x' + r.height;
          }
        },
        {
          title: '备注',
          align: 'center',
          width: 160,
          dataIndex: 'remark'
        },
        {
          title: '上传时间',
          align: 'center',
          width: 120,
          dataIndex: 'createTime'
        }
      ],
      url: {
        list: 'game/gameImage/list'
      },
      // 已选择列表
      selectedTable: {
        dataSource: []
      }
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.valueWatchHandler(val);
      }
    },
    dataSource: {
      deep: true,
      handler(val) {
        let options = val.map((data) => ({ label: data[this.valueKey], value: data[this.valueKey] }));
        this.$emit('ok', options);
        this.valueWatchHandler(this.value);
      }
    },
    selectionRows: {
      immediate: true,
      deep: true,
      handler(val) {
        this.selectedTable.dataSource = val;
      }
    }
  },
  created() {
    this.valueWatchHandler(this.value);
  },
  methods: {
    /** 关闭弹窗 */
    close() {
      this.$emit('update:visible', false);
    },
    getImgView(text) {
      if (text && text.indexOf(',') > 0) {
        text = text.substring(0, text.indexOf(','));
      }
      return `${window._CONFIG['domainURL']}/${text}`;
    },
    valueWatchHandler(val) {
      let dataSource = [];
      let selectedRowKeys = [];
      this.dataSource.forEach((data) => {
        if (data[this.valueKey] === val) {
          dataSource.push(data);
          selectedRowKeys.push(data.id);
          return;
        }
      });

      this.selectedTable.dataSource = dataSource;
      this.selectedRowKeys = selectedRowKeys;
    },
    /** 完成选择 */
    handleOk() {
      let value = this.selectedTable.dataSource.map((data) => data[this.valueKey]);
      if (value && value[0]) {
        this.$emit('input', value[0]);
      } else {
        this.$emit('input', '');
      }
      this.close();
    },
    customRowFn(record) {
      return {
        on: {
          click: () => {
            this.selectedRowKeys = [record.id];
            this.selectedTable.dataSource = [record];
          }
        }
      };
    }
  }
};
</script>
<style lang="less" scoped>
.list-image {
  width: 100%;
  height: 120px;
  object-fit: scale-down;
}
</style>
