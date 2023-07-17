<template>
  <!-- <a-drawer :title="title" :width="1000" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="ID">
          <a-input :disabled="isEdit" placeholder="请输入ID" v-decorator="['id', validatorRules.id]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服ID">
          <a-input :disabled="isEdit" placeholder="请输入区服ID" v-decorator="['serverIds', validatorRules.serverIds]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公网host">
          <j-search-select-tag placeholder="请输入IP" v-decorator="['host', validatorRules.host]" dict="game_vps,hostname,ip" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跨服地址">
          <a-input placeholder="请输入跨服地址" v-decorator="['crossServerUrl', validatorRules.crossServerUrl]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="聊天服地址">
          <a-input placeholder="请输入聊天服地址" v-decorator="['gmUrl', validatorRules.gmUrl]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="GM地址">
          <a-input placeholder="请输入GM地址" v-decorator="['chatServerUrl', validatorRules.chatServerUrl]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
          <a-input placeholder="请输入备注" v-decorator="['remark', validatorRules.remark]" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer> -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';

export default {
  name: 'GameServerGroupModal',
  data() {
    return {
      title: '操作',
      visible: false,
      isEdit: false,
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
      form: this.$form.createForm(this),
      validatorRules: {
        host: { rules: [{ required: true, message: '请输入IP!' }] },
        gmUrl: { rules: [{ required: true, message: '请输入GM地址!' }] },
        crossServerUrl: { rules: [{ required: true, message: '请输入跨服地址!' }] },
        chatServerUrl: { rules: [{ required: true, message: '请输入聊天服地址!' }] },
        serverIds: { rules: [{ required: true, message: '请输入区服ID!' }] },
        remark: { rules: [{ required: true, message: '请输入备注ip!' }] }
      },
      url: {
        add: 'game/group/add',
        edit: 'game/group/edit'
      }
    };
  },
  created() {},
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
        this.form.setFieldsValue(pick(this.model, 'id', 'host', 'gmUrl', 'serverIds', 'crossServerUrl', 'chatServerUrl', `crossSettleTime`, `remark`));
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
          // 时间格式化

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
