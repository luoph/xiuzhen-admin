<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="激活码活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number
            :disabled="isEdit || isIncludeActivityModel"
            v-decorator="['activityId', validatorRules.activityId]"
            placeholder="请输入激活码活动id"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item v-if="isBatchAdd" label="批量生成激活码数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['batchNum', validatorRules.batchNum]" placeholder="请输入批量生成激活码数量" style="width: 100%" />
        </a-form-item>
        <a-form-item v-else label="激活码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['code', validatorRules.code]" placeholder="请输入激活码, 使用半角','分割多个激活码" />
        </a-form-item>
        <a-form-item label="可使用总数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['totalNum', validatorRules.totalNum]" placeholder="请输入可使用总数" style="width: 100%" />
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="请选择活动状态" v-decorator="['status', validatorRules.status]" initialValue="1">
            <a-select-option :value="0">无效</a-select-option>
            <a-select-option :value="1">有效</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!-- <a-button type="primary" @click="handleOk">确定</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button> -->
  <!-- </a-drawer> -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'RedeemCodeModal',
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
      isIncludeActivityModel: false,
      isBatchAdd: false,
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
        activityId: { rules: [{ required: true, message: '请输入激活码活动id!' }] },
        batchNum: { rules: [{ required: true, message: '请输入批量生成激活码数量!' }] },
        code: { rules: [{ required: true, message: '请输入激活码!' }] },
        totalNum: { rules: [{ required: true, message: '请输入可使用总数!' }] },
        status: { rules: [{ required: true, message: '请选择状态!' }] },
        createTime: {},
        updateTime: {}
      },
      url: {
        add: 'game/redeemCode/add',
        edit: 'game/redeemCode/edit'
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
      this.isIncludeActivityModel = this.model.isIncludeActivityModel;
      this.isBatchAdd = this.model.isBatchAdd;
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'activityId', 'batchNum', 'code', 'totalNum', 'status'));
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
      this.form.setFieldsValue(pick(row, 'activityId', 'code', 'status', 'createTime', 'totalNum', 'updateTime'));
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
