<template>
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存"> -->
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="主活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['campaignId', validatorRules.campaignId]"
                          placeholder="请输入主活动id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="子活动id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="true" v-decorator="['typeId', validatorRules.typeId]" placeholder="请输入子活动id"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="任务id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['taskId', validatorRules.taskId]" placeholder="请输入任务id"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['description', validatorRules.description]" placeholder="请输入描述"></a-textarea>
        </a-form-item>
        <a-form-item label="moduleId" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['moduleId', validatorRules.moduleId]" placeholder="请输moduleId"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="任务完成条件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['target', validatorRules.target]" placeholder="请输入任务完成条件"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="任务参数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['args', validatorRules.args]" placeholder="请输入任务参数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="奖励列表" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['reward', validatorRules.reward]" placeholder="请输入奖励列表"></a-textarea>
        </a-form-item>
        <a-form-item label="跳转id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['jumpId', validatorRules.jumpId]" placeholder="请输入跳转id"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="最小世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['minLevel', validatorRules.minLevel]" placeholder="请输入最小世界等级"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="最大世界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['maxLevel', validatorRules.maxLevel]" placeholder="请输入最大世界等级"
                          style="width: 100%"/>
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- </a-modal> -->
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-modal>
  <!-- <a-button type="primary" @click="handleOk">确定</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer> -->
</template>

<script>
import {httpAction} from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";

export default {
  name: "GameCampaignTypeTaskModal",
  components: {
    JDate
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: "操作",
      width: 800,
      visible: false,
      isEdit: false,
      model: {},
      labelCol: {
        xs: {span: 24},
        sm: {span: 5}
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16}
      },
      confirmLoading: false,
      validatorRules: {
        campaignId: {rules: [{required: true, message: "请输入主活动id!"}]},
        typeId: {rules: [{required: true, message: "请输入子活动id!"}]},
        taskId: {rules: [{required: true, message: "请输入任务id!"}]},
        description: {rules: [{required: true, message: "请输入描述!"}]},
        moduleId: {rules: [{required: true, message: "请输入moduleId!"}]},
        target: {rules: [{required: true, message: "请输入任务完成条件!"}]},
        args: {rules: [{required: true, message: "请输入任务参数!"}]},
        reward: {rules: [{required: true, message: "请输入奖励列表!"}]},
        jumpId: {rules: [{required: false, message: "请输入跳转id!"}]},
        minLevel: {rules: [{required: true, message: "请输入最小世界等级!"}]},
        maxLevel: {rules: [{required: true, message: "请输入最大世界等级!"}]},
      },
      url: {
        add: "game/gameCampaignTypeTask/add",
        edit: "game/gameCampaignTypeTask/edit"
      }
    };
  },
  created() {
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
      console.log("GameCampaignTypeTaskModal, model:", JSON.stringify(this.model));

      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, "campaignId", "typeId", "taskId", "description", "moduleId", "target", "args", "reward", "jumpId", "minLevel", "maxLevel"));
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpUrl = "";
          let method = "";
          if (!this.model.id) {
            httpUrl += this.url.add;
            method = "post";
          } else {
            httpUrl += this.url.edit;
            method = "put";
          }
          let formData = Object.assign(this.model, values);
          console.log("表单提交数据", formData);
          httpAction(httpUrl, formData, method)
            .then(res => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
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
      this.form.setFieldsValue(pick(row, "campaignId", "typeId", "taskId", "description", "moduleId", "target", "reward", "jumpId", "minLevel", "maxLevel"));
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
