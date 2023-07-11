<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入名称" v-decorator="['name', validatorRules.name]" />
        </a-form-item>
        <a-form-item label="渠道标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入渠道标识" :disabled="isEdit" v-decorator="['channel', validatorRules.channel]" />
        </a-form-item>
        <a-form-item label="Sdk渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入Sdk渠道" :disabled="isEdit" v-decorator="['sdkChannel', validatorRules.sdkChannel]" />
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入备注" v-decorator="['remark', validatorRules.remark]" />
        </a-form-item>
        <a-form-item label="上线时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['onlineTime', validatorRules.onlineTime]" />
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button> -->
  </a-modal>
  <!-- </a-drawer> -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import moment from 'moment';

export default {
  name: 'GameSdkChannelModal',
  components: {},
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
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
      validatorRules: {
        name: { rules: [{ required: true, message: '请输入渠道名称!' }] },
        channel: { rules: [{ required: true, message: '请输入渠道标识!' }] },
        sdkChannel: { rules: [{ required: true, message: '请输入Sdk渠道!' }] },
        onlineTime: { rules: [{ required: false, message: '请选择上线时间!' }] },
        remark: {}
      },
      url: {
        add: 'game/sdkChannel/add',
        edit: 'game/sdkChannel/edit'
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
      this.isEdit = this.model.id != null;
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'name', 'channel', 'sdkChannel', 'remark'));
        // 时间格式化
        this.form.setFieldsValue({ onlineTime: this.model.onlineTime ? moment(this.model.onlineTime) : null });
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
          formData.onlineTime = formData.onlineTime ? formData.onlineTime.format('YYYY-MM-DD HH:mm:ss') : null;
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
      this.form.setFieldsValue(pick(row, 'name', 'channel', 'sdkChannel', 'remark'));
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
