<template>
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入名称" />
        </a-form-item>
        <a-form-item label="分组说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['summary', validatorRules.summary]" placeholder="请输入分组说明"/>
        </a-form-item>
        <a-form-item label="限制次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['limitCount', validatorRules.limitCount]" placeholder="请输入限制次数"
                          style="width: 100%"/>
        </a-form-item>
      </a-form>

      <a-tabs v-if="isEdit" defaultActiveKey="1">
        <a-tab-pane tab="分组活动配置" key="1">
          <redeem-activity-list ref="redeemActivityList" :disableMixinCreated="true"></redeem-activity-list>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>
import {httpAction} from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";
import RedeemActivityList from "../RedeemActivityList";

export default {
  name: "GameRedeemConfigModal",
  components: {
    JDate,
    RedeemActivityList
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: "操作",
      width: 1444,
      isEdit: false,
      visible: false,
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
        name: {rules: [{required: true, message: "请输入活动名称!"}]},
        summary: {rules: [{required: true, message: "请输入礼包说明!"}]},
        limitCount: {}
      },
      url: {
        add: "game/redeemActivityGroup/add",
        edit: "game/redeemActivityGroup/edit"
      }
    };
  },
  created() {
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.isEdit = this.model.id != null;
      this.visible = true;
      this.$nextTick(() => {
        if (this.isEdit) {
          // 手动渲染数据
          this.$refs.redeemActivityList.reset();
          this.$refs.redeemActivityList.loadDateById(record);
        }
        this.form.setFieldsValue(pick(this.model, "name", "summary", "limitCount"));
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
      this.form.setFieldsValue(pick(row, "name", "summary", "limitCount"));
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
