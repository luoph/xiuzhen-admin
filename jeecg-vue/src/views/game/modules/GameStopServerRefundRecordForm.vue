<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="停服的服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sourceServerId">
              <a-input-number v-model="model.sourceServerId" placeholder="请输入停服的服务器id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="停服的玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sourcePlayerId">
              <a-input-number v-model="model.sourcePlayerId" placeholder="请输入停服的玩家id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="返还的服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="targetServerId">
              <a-input-number v-model="model.targetServerId" placeholder="请输入返还的服务器id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="返还的玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="targetPlayerId">
              <a-input-number v-model="model.targetPlayerId" placeholder="请输入返还的玩家id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="充值总金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sourceAmount">
              <a-input-number v-model="model.sourceAmount" placeholder="请输入充值总金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="返还总仙玉" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="targetNum">
              <a-input-number v-model="model.targetNum" placeholder="请输入返还总仙玉" style="width: 100%" />
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
  name: 'GameStopServerRefundRecordForm',
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
        sourceServerId: [{ required: true, message: '请输入停服的服务器id!' }],
        sourcePlayerId: [{ required: true, message: '请输入停服的玩家id!' }],
        targetServerId: [{ required: true, message: '请输入返还的服务器id!' }],
        targetPlayerId: [{ required: true, message: '请输入返还的玩家id!' }],
        sourceAmount: [{ required: true, message: '请输入充值总金额!' }],
        targetNum: [{ required: true, message: '请输入返还总仙玉!' }]
      },
      url: {
        add: '/game/gameStopServerRefundRecord/add',
        edit: '/game/gameStopServerRefundRecord/edit',
        queryById: '/game/gameStopServerRefundRecord/queryById'
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
      this.model = Object.assign({}, record);
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