<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="campaignId">
              <a-input-number v-model="model.campaignId" placeholder="请输入主活动id" style="width: 100%" :disabled="true"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="typeId">
              <a-input-number v-model="model.typeId" placeholder="请输入子活动id" style="width: 100%" :disabled="true"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="获取道具id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemId">
              <a-input-number v-model="model.itemId" placeholder="请输入获取道具id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="传闻内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-textarea v-model="model.content" rows="4" placeholder="请输入传闻内容" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'GameCampaignTypeSummonMessageForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
        model:{},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          itemId: [
              { required: true, message: '请输入获取道具id!'},
           ],
           content: [
              { required: true, message: '请输入传闻内容!'},
           ]
        },
        url: {
          add: "/game/gameCampaignTypeSummonMessage/add",
          edit: "/game/gameCampaignTypeSummonMessage/edit",
          queryById: "/game/gameCampaignTypeSummonMessage/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.isEdit = this.model.id != null;
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>