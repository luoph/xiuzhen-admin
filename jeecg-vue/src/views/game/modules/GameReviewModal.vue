<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入渠道名称"/>
        </a-form-item>
        <a-form-item label="Sdk渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sdkChannel', validatorRules.sdkChannel]" placeholder="请输入Sdk渠道"/>
        </a-form-item>
        <a-form-item label="游戏编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-search-select-tag v-decorator="['gameId', validatorRules.gameId]" placeholder="请选择游戏编号"
                             dictCode="game_info,name,id"/>
        </a-form-item>
        <a-form-item label="版本号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['version', validatorRules.version]" placeholder="请输入版本号"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="审核区服配置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-search-select-tag
            v-decorator="['profile', validatorRules.profile]" placeholder="请输入审核区服配置"
            dict="game_channel,name,simple_name"
            :async="true">
          </j-search-select-tag>
        </a-form-item>
        <a-form-item label="审核开关" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="审核开关" v-decorator="['status', validatorRules.status]" initialValue="1">
            <a-select-option :value="1">开启</a-select-option>
            <a-select-option :value="0">关闭</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注"/>
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button> -->
  </a-modal>
  <!-- </a-drawer> -->
</template>

<script>
import {httpAction} from '@/api/manage';
import pick from 'lodash.pick';

export default {
  name: 'GameChannelModal',
  components: {},
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
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
        name: {rules: [{required: true, message: '请输入名称!'}]},
        gameId: {rules: [{required: true, message: '请输入游戏编号!'}]},
        sdkChannel: {rules: [{required: true, message: '请输入Sdk渠道标识!'}]},
        version: {rules: [{required: true, message: '请输入版本号!'}]},
        profile: {rules: [{required: false, message: '请输入审核区服配置!'}]},
        status: {rules: [{required: true, message: '请选择审核开关!'}]},
        remark: {},
      },
      url: {
        add: 'game/review/add',
        edit: 'game/review/edit'
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
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(this.model, 'name', 'sdkChannel', 'gameId', 'version', 'profile', 'remark', 'status')
        );
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
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(row, 'name', 'sdkChannel', 'gameId', 'version', 'profile', 'remark', 'status')
      );
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
