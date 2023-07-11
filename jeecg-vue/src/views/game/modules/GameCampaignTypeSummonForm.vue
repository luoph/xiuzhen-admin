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
              <a-input v-model="model.name" placeholder="请输入活动名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抽奖消耗道具" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="summonConsume">
              <a-textarea v-model="model.summonConsume" rows="4" placeholder="请输入抽奖消耗道具" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="更换心仪大奖消耗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="changeFavoriteRewardConsume">
              <a-textarea v-model="model.changeFavoriteRewardConsume" rows="4" placeholder="请输入更换心仪大奖消耗" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第N次开始抽大奖奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="summonBigRewardNum">
              <a-input-number v-model="model.summonBigRewardNum" placeholder="请输入第N次开始抽大奖奖池" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第N次开始抽心仪奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="summonFavoriteRewardNum">
              <a-input-number v-model="model.summonFavoriteRewardNum" placeholder="请输入第N次开始抽心仪奖池" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="普通奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reward">
              <a-textarea v-model="model.reward" rows="4" placeholder="请输入普通奖池" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大奖奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bigReward">
              <a-textarea v-model="model.bigReward" rows="4" placeholder="请输入大奖奖池" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="心仪奖池" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="favoriteReward">
              <a-textarea v-model="model.favoriteReward" rows="4" placeholder="请输入心仪奖池" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="概率公示" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prShow">
              <a-textarea v-model="model.prShow" rows="4" placeholder="请输入概率公示" />
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
  name: 'GameCampaignTypeSummonForm',
  components: {},
  props: {
    //表单禁用
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
        summonConsume: [{ required: true, message: '请输入抽奖消耗道具!' }],
        changeFavoriteRewardConsume: [{ required: true, message: '请输入更换心仪大奖消耗!' }],
        summonBigRewardNum: [{ required: true, message: '请输入第N次开始抽大奖奖池!' }],
        summonFavoriteRewardNum: [{ required: true, message: '请输入第N次开始抽心仪奖池!' }],
        reward: [{ required: true, message: '请输入普通奖池!' }],
        bigReward: [{ required: true, message: '请输入大奖奖池!' }],
        favoriteReward: [{ required: true, message: '请输入心仪奖池!' }],
        prShow: [{ required: true, message: '请输入概率公示!' }],
        minLevel: [{ required: true, message: '请输入最小世界等级!' }],
        maxLevel: [{ required: true, message: '请输入最大世界等级!' }]
      },
      url: {
        add: '/game/gameCampaignTypeSummon/add',
        edit: '/game/gameCampaignTypeSummon/edit',
        queryById: '/game/gameCampaignTypeSummon/queryById'
      }
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    }
  },
  created() {
    //备份model原始值
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