<template>
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="true" v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="封禁类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select :disabled="true" placeholder="请选择封禁类型" v-decorator="['type', validatorRules.type]">
            <a-select-option :value="1">登录</a-select-option>
            <a-select-option :value="2">聊天</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['banValue', validatorRules.banValue]" placeholder="请输入玩家id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="封禁时长（秒）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['duration', validatorRules.duration]" placeholder="请输入封禁时长（秒）" style="width: 100%" />
        </a-form-item>
        <a-form-item label="选择时长" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-radio-group v-model="durationType" @change="onDurationChange" :defaultValue="7">
            <a-radio :value="0">自定义</a-radio>
            <a-radio :value="1">1天</a-radio>
            <a-radio :value="3">3天</a-radio>
            <a-radio :value="7">7天</a-radio>
            <a-radio :value="30">30天</a-radio>
            <a-radio :value="365">365天</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="封禁原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['reason', validatorRules.reason]" placeholder="请输入封禁原因" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import moment from 'moment';

export default {
  name: 'GameForbidTalkModal',
  components: {
    JDate
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
      visible: false,
      isEdit: false,
      model: {},
      durationType: 7,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {
        serverId: { rules: [{ required: true, message: '请输入服务器id!' }] },
        type: { rules: [{ required: true, message: '请输入封禁功能' }] },
        banValue: { rules: [{ required: true, message: '请输入对应封禁值!' }] },
        duration: { rules: [{ required: true, message: '请输入封禁时长（秒）!' }] },
        reason: { rules: [{ required: true, message: '请输入封禁原因!' }] }
      },
      url: {
        add: 'game/forbidden/add',
        edit: 'game/forbidden/edit'
      }
    };
  },
  created() {},
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      if (this.durationType > 0) {
        this.selectDuration(this.durationType);
      }

      this.visible = true;

      this.$nextTick(() => {
        setTimeout(() => {
          this.form.setFieldsValue(pick(this.model, 'serverId', 'type', 'banKey', 'banValue', 'reason', 'duration'));

          // 时间格式化
          this.form.setFieldsValue({ startTime: this.model.startTime ? moment(this.model.startTime) : null });
          this.form.setFieldsValue({ endTime: this.model.endTime ? moment(this.model.endTime) : null });
        });
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
          console.log(this.model);
          console.log(values);
          let formData = Object.assign(this.model, values);
          // 时间格式化
          formData.startTime = formData.startTime ? formData.startTime.format('YYYY-MM-DD HH:mm:ss') : null;
          formData.endTime = formData.endTime ? formData.endTime.format('YYYY-MM-DD HH:mm:ss') : null;
          console.log('表单提交数据', formData);
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
      this.form.setFieldsValue(pick(row, 'serverId', 'type', 'banKey', 'banValue', 'reason', 'duration'));
    },
    onDurationChange(e) {
      this.selectDuration(e.target.value);
    },
    selectDuration(value) {
      if (value > 0) {
        this.model.duration = value * 24 * 60 * 60;
        this.popupCallback(this.model);
      }
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
