<template>
  <!-- <a-drawer :title="title" :width="1000" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item v-if="isEdit" :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告id">
          <a-input :disabled="isEdit" placeholder="公告id" v-model="model.id" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告类型">
          <a-select :disabled="isEdit" placeholder="请选择公告类型" v-decorator="['noticeType', validatorRules.noticeType]">
            <a-select-option :value="1">渠道公告</a-select-option>
            <a-select-option :value="2">滚动公告</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告标题">
          <a-input placeholder="请输入标题" v-decorator="['title', validatorRules.title]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告内容">
          <a-textarea :rows="5" placeholder="请输入公告内容" v-decorator="['content', validatorRules.content]" size="large" allowClear showCount :auto-size="{ minRows: 5, maxRows: 30 }" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始时间">
          <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['beginTime', validatorRules.beginTime]" style="width: 100%;"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束时间">
          <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" style="width: 100%;" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
          <a-select placeholder="请选择状态" v-decorator="['status', validatorRules.status]" initialValue="1">
            <a-select-option :value="1">启用</a-select-option>
            <a-select-option :value="0">禁用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="滚动公告间隔(秒)">
          <a-input-number v-decorator="['intervalSeconds', {}]" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer> -->
</template>

<script>
import { httpAction } from '@/api/manage';
import JEditor from '@/components/jeecg/JEditor';
import pick from 'lodash.pick';
import moment from 'moment';

export default {
  name: 'GameNoticeModal',
  components: {
    JEditor
  },
  data() {
    return {
      title: '操作',
      visible: false,
      isEdit: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      form: this.$form.createForm(this),
      validatorRules: {
        noticeType: { rules: [{ required: true, message: '请选择公告类型!' }] },
        title: { rules: [{ required: false, message: '请输入公告标题!' }] },
        content: { rules: [{ required: true, message: '请输入公告内容!' }] },
        beginTime: { rules: [{ required: true, message: '请输入开始时间!' }] },
        endTime: { rules: [{ required: true, message: '请输入结束时间!' }] },
        status: { rules: [{ required: true, message: '请选择状态!' }] }
      },
      url: {
        add: 'game/gameNotice/add',
        edit: 'game/gameNotice/edit'
      }
    };
  },
  created() {},
  methods: {
    add() {
      // 新增默认值
      this.edit({ noticeType: 1, status: 1, intervalSeconds: 0 });
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.isEdit = this.model.id != null;
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'noticeType', 'title', 'content', 'status', 'intervalSeconds'));
        // 时间格式化
        this.form.setFieldsValue({ beginTime: this.model.beginTime ? moment(this.model.beginTime) : null });
        this.form.setFieldsValue({ endTime: this.model.endTime ? moment(this.model.endTime) : null });
      });
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpUrl = '';
          let method = '';
          if (!this.model.id) {
            httpUrl += this.url.add;
            method = 'post';
          } else {
            httpUrl += this.url.edit;
            method = 'put';
          }
          let formData = Object.assign(this.model, values);

          // 时间格式化
          formData.beginTime = formData.beginTime ? formData.beginTime.format('YYYY-MM-DD HH:mm:ss') : null;
          formData.endTime = formData.endTime ? formData.endTime.format('YYYY-MM-DD HH:mm:ss') : null;

          console.log(formData);
          httpAction(httpUrl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
              that.close();
            });
        }
      });
    },
    handleCancel() {
      this.close();
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'noticeType', 'title', 'content', 'status', 'intervalSeconds'));
    }
  }
};
</script>

//
<style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
  float: right;
}
</style>
