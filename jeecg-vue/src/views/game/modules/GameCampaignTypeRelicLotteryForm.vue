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
            <a-form-model-item label="活动名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入活动名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大区间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="area">
              <a-input-number v-model="model.area" placeholder="请输入大区间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最小层" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="minLayer">
              <a-input-number v-model="model.minLayer" placeholder="请输入最小层" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大层" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maxLayer">
              <a-input-number v-model="model.maxLayer" placeholder="请输入最大层" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="翻牌消耗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="consume">
              <a-input v-model="model.consume" placeholder="请输入翻牌消耗"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="普通奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reward">
              <a-input v-model="model.reward" placeholder="请输入普通奖池"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大奖奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bigReward">
              <a-input v-model="model.bigReward" placeholder="请输入大奖奖池"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="暴击概率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="crit">
              <a-input v-model="model.crit" placeholder="请输入暴击概率"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="概率公示" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prShow">
              <a-input v-model="model.prShow" placeholder="请输入概率公示"></a-input>
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

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'GameCampaignTypeRelicLotteryForm',
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
           name: [
              { required: true, message: '请输入活动名称!'},
           ],
           minLayer: [
              { required: true, message: '请输入最小层!'},
           ],
           maxLayer: [
              { required: true, message: '请输入最大层!'},
           ],
           minLevel: [
              { required: true, message: '请输入最小世界等级!'},
           ],
           maxLevel: [
              { required: true, message: '请输入最大世界等级!'},
           ],
        },
        url: {
          add: "/game/gameCampaignTypeRelicLottery/add",
          edit: "/game/gameCampaignTypeRelicLottery/edit",
          queryById: "/game/gameCampaignTypeRelicLottery/queryById"
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