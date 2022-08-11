<template>
  <a-modal :title="title" :width="800" :visible="visible" v-if="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" okText="执行" cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务类型" hasFeedback>
          <a-select placeholder="任务类型" v-decorator="['jobType', {}]">
            <a-select-option value="1">每日数据统计</a-select-option>
            <a-select-option value="2">留存统计</a-select-option>
            <a-select-option value="3">LTV统计</a-select-option>
            <a-select-option value="4">留存详细统计</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="指定日期" hasFeedback>
          <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { getAction } from '@/api/manage';
import JDate from '@/components/jeecg/JDate.vue';
import pick from 'lodash.pick';
// import moment from "moment"

export default {
  name: 'QuartzJobRunModal',
  components: {
    JDate
  },
  data() {
    return {
      title: '操作',
      visible: false,
      model: {
        startDate: '',
        endDate: ''
      },
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
      url: 'sys/quartzJob/runCoreStatisticManually'
    };
  },
  created() {},
  methods: {
    edit(record) {
      let that = this;
      that.form.resetFields();
      this.model = Object.assign({}, record);
      console.log(this.model);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'quartzJobType', 'startDate', 'endDate'));
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
        console.log('values', values);
        if (!err) {
          that.confirmLoading = true;
          let formData = Object.assign(this.model, values);

          console.log('提交参数', formData);
          getAction(this.url, formData)
            .then(res => {
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
    onDateChange(dates, dateStrings) {
      this.model.startDate = dateStrings[0];
      this.model.endDate = dateStrings[1];
    }
  }
};
</script>

<style scoped></style>
