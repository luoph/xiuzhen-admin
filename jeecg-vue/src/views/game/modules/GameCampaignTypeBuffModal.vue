<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]" placeholder="请输入主活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="加成类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="选择加成类型" v-decorator="['type', validatorRules.type]" initialValue="5">
            <a-select-option :value="5">5-修为加成</a-select-option>
            <a-select-option :value="6">6-灵气加成</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" style="width: 100%" />
        </a-form-item>
        <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" style="width: 100%" />
        </a-form-item>
        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['description', validatorRules.description]" placeholder="请输入描述" />
        </a-form-item>
        <a-form-item label="加成" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['addition', validatorRules.addition]" placeholder="请输入加成" style="width: 100%" />
        </a-form-item>
        <a-form-item label="最小世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['minLevel', validatorRules.minLevel]" placeholder="请输入最小世界等级" style="width: 100%" />
        </a-form-item>
        <a-form-item label="最大世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['maxLevel', validatorRules.maxLevel]" placeholder="请输入最大世界等级" style="width: 100%" />
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
import pick from 'lodash.pick';
import moment from 'moment';
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'GameCampaignTypeBuffModal',
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
        campaignId: { rules: [{ required: true, message: '请输入主活动id!' }] },
        typeId: { rules: [{ required: true, message: '请输入子活动id!' }] },
        type: { rules: [{ required: true, message: '请输入活动类型!' }] },
        startTime: { rules: [{ required: true, message: '请输入开始时间!' }] },
        endTime: { rules: [{ required: true, message: '请输入结束时间!' }] },
        description: { rules: [{ required: true, message: '请输入描述!' }] },
        addition: { rules: [{ required: true, message: '请输入加成!' }] },
        minLevel: { rules: [{ required: true, message: '请输入最小世界等级!' }] },
        maxLevel: { rules: [{ required: true, message: '请输入最大世界等级!' }] }
      },
      url: {
        add: 'game/gameCampaignTypeBuff/add',
        edit: 'game/gameCampaignTypeBuff/edit'
      }
    };
  },
  created() {},
  methods: {
    add(record) {
      this.edit(record);
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.isEdit = this.model.id != null;
      this.visible = true;
      console.log('GameCampaignTypeBuffModal, model:', JSON.stringify(this.model));

      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'campaignId', 'typeId', 'type', 'startTime', 'endTime', 'description', 'addition', 'minLevel', 'maxLevel'));
        this.form.setFieldsValue({ startTime: this.model.startTime ? moment(this.model.startTime) : null });
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
      this.form.setFieldsValue(pick(row, 'campaignId', 'typeId', 'type', 'startTime', 'endTime', 'description', 'addition', 'minLevel', 'maxLevel'));
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
