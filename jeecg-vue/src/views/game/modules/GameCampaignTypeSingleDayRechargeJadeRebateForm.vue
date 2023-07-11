<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="campaignId">
              <a-input-number v-model="model.campaignId" placeholder="请输入主活动id" style="width: 100%" :disabled="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="typeId">
              <a-input-number v-model="model.typeId" placeholder="请输入子活动id" style="width: 100%" :disabled="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入活动名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最小累充金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="minRechargeAmount">
              <a-input-number v-model="model.minRechargeAmount" placeholder="请输入最小累充金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大累充金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maxRechargeAmount">
              <a-input-number v-model="model.maxRechargeAmount" placeholder="请输入最大累充金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="返利比例" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rebatePct">
              <a-input-number v-model="model.rebatePct" placeholder="请输入返利比例" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮件标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-textarea v-model="model.title" rows="4" placeholder="请输入邮件标题" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮件描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="describe">
              <a-textarea v-model="model.describe" rows="4" placeholder="请输入邮件描述" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="下一档邮件描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nextDescribe">
              <a-textarea v-model="model.nextDescribe" rows="4" placeholder="请输入下一档邮件描述" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮件类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <a-select placeholder="请选择邮件类型: 1.有附件, 2.冇附件" @change="handleTypeChange" v-model="model.type" initialValue="1">
                <a-select-option :value="1">1-有附件</a-select-option>
                <a-select-option :value="2">2-冇附件</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="邮件附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-textarea v-model="model.content" rows="4" placeholder="请输入邮件附件" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最小世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="minLevel">
              <a-input-number v-model="model.minLevel" placeholder="请输入最小世界等级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maxLevel">
              <a-input-number v-model="model.maxLevel" placeholder="请输入最大世界等级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage';
import { validateDuplicateValue } from '@/utils/util';

export default {
  name: 'GameCampaignTypeSingleDayRechargeJadeRebateForm',
  components: {},
  props: {
    // 表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      form: this.$form.createForm(this),
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
        name: [{ required: true, message: '请输入活动名称!' }],
        minRechargeAmount: [{ required: true, message: '请输入最小累充金额!' }],
        maxRechargeAmount: [{ required: true, message: '请输入最大累充金额!' }],
        rebatePct: [{ required: true, message: '请输入返利比例!' }],
        title: [{ required: true, message: '请输入邮件标题!' }],
        describe: [{ required: true, message: '请输入邮件描述!' }],
        nextDescribe: [{ required: true, message: '请输入下一档邮件描述!' }],
        type: [{ required: true, message: '请选择邮件类型: 1.有附件, 2.冇附件!' }],
        minLevel: [{ required: true, message: '请输入最小世界等级!' }],
        maxLevel: [{ required: true, message: '请输入最大世界等级!' }]
      },
      url: {
        add: '/game/gameCampaignTypeSingleDayRechargeJadeRebate/add',
        edit: '/game/gameCampaignTypeSingleDayRechargeJadeRebate/edit',
        queryById: '/game/gameCampaignTypeSingleDayRechargeJadeRebate/queryById'
      }
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    }
  },
  created() {
    // 备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.isEdit = this.model.id != null;
      this.visible = true;
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          httpAction(httpurl, this.model, method)
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
            });
        }
      });
    }
  }
};
</script>