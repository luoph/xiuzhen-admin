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
            <a-form-model-item label="阶段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stage">
              <a-input-number v-model="model.stage" placeholder="请输入阶段" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taskId">
              <a-input-number v-model="model.taskId" placeholder="请输入任务id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description">
              <a-input v-model="model.description" placeholder="请输入描述" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模块id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="moduleId">
              <a-input-number v-model="model.moduleId" placeholder="请输入模块id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务完成条件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="target">
              <a-input-number v-model="model.target" placeholder="请输入任务完成条件" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务参数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="args">
              <a-input-number v-model="model.args" placeholder="请输入任务参数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reward">
              <a-input v-model="model.reward" placeholder="请输入奖励" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="跳转id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jumpId">
              <a-input-number v-model="model.jumpId" placeholder="请输入跳转id" style="width: 100%" />
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
  name: 'GameCampaignTypeStageTaskItemForm',
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
      validatorRules: {},
      url: {
        add: '/game/gameCampaignTypeStageTaskItem/add',
        edit: '/game/gameCampaignTypeStageTaskItem/edit',
        queryById: '/game/gameCampaignTypeStageTaskItem/queryById'
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