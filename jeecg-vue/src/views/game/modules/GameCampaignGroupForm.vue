<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="分组名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input placeholder="请输入分组名称" v-model="model.name" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="分组备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input placeholder="请输入分组备注" v-model="model.remark" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction } from '@/api/manage';
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'GameCampaignGroupForm',
  components: {
    JDate
  },
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
        name: [{ required: true, message: '请输入活动分组名称!' }],
        remark: [{ required: false, message: '请输入备注!' }]
      },
      url: {
        add: '/game/gameCampaignGroup/add',
        edit: '/game/gameCampaignGroup/edit',
        queryById: '/game/gameCampaignGroup/queryById'
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
    add(record) {
      this.edit(record);
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