<template>
  <a-modal :title="title" :width="800" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <!-- <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存"> -->
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="渠道id">
          <a-input :disabled="true" placeholder="请输入渠道id" v-decorator="['channelId', validatorRules.channelId]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服Id" v-show="isEdit">
          <!-- <a-select :disabled="isEdit" placeholder="请选择区服Id" v-decorator="['serverId', {}]">
            <a-select-option v-for="server in serverList" :key="server.name" :value="server.id">
              {{ server.id + ' - ' + server.name }}
            </a-select-option>
          </a-select> -->
            <a-input v-decorator="['serverId', validatorRules.serverId]" placeholder="区服Id" style="width: 100%"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服Id" v-show="!isEdit">
          <a-input v-decorator="['serverIds', validatorRules.serverIds]" 
              placeholder="区服Id（e.g. 1001, 1002, 1003-1006）" style="width: 100%"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="位置权重">
          <a-input-number v-decorator="['position', validatorRules.position]" placeholder="请输入位置权重（值越大越靠前）" style="width: 100%"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
          <a-select v-decorator="['delFlag', {}]" placeholder="请选择状态" :initialValue="0">
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">已删除</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import {getAction, httpAction} from '@/api/manage';
import pick from 'lodash.pick';

export default {
  name: 'GameChannelServerModal',
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
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
        serverId: {rules: [{required: false, message: '请输入区服Id!'}]},
        serverIds: {rules: [{required: false, message: '请输入区服Id!'}]},
        position: {rules: [{required: false, message: '请输入位置权重!'}]}
      },
      url: {
        add: 'game/channelServer/add',
        edit: 'game/channelServer/edit',
        // 游戏服列表
        serverListUrl: 'game/gameServer/all'
      }
    };
  },
  created() {
  },
  methods: {
    add(channelId) {
      // 从上层传递过来的参数，默认选择未删除
      this.edit({channelId: channelId, delFlag: 0});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.isEdit = this.model.id != null;
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'serverId', 'serverIds', 'channelId', 'delFlag', 'position'));
      });
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpUrl = '';
          let method = '';
          if (!this.model.id) {
            httpUrl += this.url.add;
            method = 'post';
          } else {
            httpUrl += this.url.edit;
            method = 'put';
          }
          let formData = Object.assign(this.model, values);

          if (this.isEdit && (this.model.serverId == null || this.model.serverId == '')) {
            that.$message.error("请输入区服id");
            that.confirmLoading = false;
            return;
          }
          if (!this.isEdit && (this.model.serverIds == null || this.model.serverIds == '')) {
            that.$message.error("请输入区服id");
            that.confirmLoading = false;
            return;
          }

          console.log(formData);
          httpAction(httpUrl, formData, method)
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
              that.close();
            });
        }
      });
    },
    handleCancel() {
      this.close();
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
