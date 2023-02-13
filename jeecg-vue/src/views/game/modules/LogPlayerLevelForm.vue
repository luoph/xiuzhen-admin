<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="playerId">
              <a-input-number v-model="model.playerId" placeholder="请输入玩家id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serverId">
              <a-input-number v-model="model.serverId" placeholder="请输入服务器id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="境界等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="level">
              <a-input-number v-model="model.level" placeholder="请输入境界等级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="战力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="combatPower">
              <a-input-number v-model="model.combatPower" placeholder="请输入战力" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="战力补偿" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="combatPowerCompensation">
              <a-input-number v-model="model.combatPowerCompensation" placeholder="请输入战力补偿" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
              <j-date placeholder="请选择创建时间" v-model="model.createTime"  style="width: 100%" />
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
    name: 'LogPlayerLevelForm',
    components: {
    },
    props: {
      // 表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
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
           playerId: [
              { required: true, message: '请输入玩家id!'},
           ],
           serverId: [
              { required: true, message: '请输入服务器id!'},
           ],
           level: [
              { required: true, message: '请输入境界等级!'},
           ],
           combatPower: [
              { required: true, message: '请输入战力!'},
           ],
           combatPowerCompensation: [
              { required: true, message: '请输入战力补偿!'},
           ],
           createTime: [
              { required: true, message: '请输入创建时间!'},
           ],
        },
        url: {
          add: "/stat/logPlayerLevel/add",
          edit: "/stat/logPlayerLevel/edit",
          queryById: "/stat/logPlayerLevel/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       // 备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
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