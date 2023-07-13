<template>
  <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="帐号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['account', validatorRules.account]" placeholder="请输入帐号" />
        </a-form-item>
        <a-form-item label="玩家ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家ID" style="width: 100%" />
        </a-form-item>
        <a-form-item label="区服Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入区服Id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="角色名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="IP" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['ip', validatorRules.ip]" placeholder="请输入IP" />
        </a-form-item>
        <a-form-item label="渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['channel', validatorRules.channel]" placeholder="请输入渠道" />
        </a-form-item>
        <a-form-item label="Sdk渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['sdkChannel', validatorRules.sdkChannel]" placeholder="请输入Sdk渠道" />
        </a-form-item>
        <a-form-item label="imei" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['imei', validatorRules.imei]" placeholder="请输入imei" />
        </a-form-item>
        <a-form-item label="mac" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['mac', validatorRules.mac]" placeholder="请输入mac" />
        </a-form-item>
        <a-form-item label="idfa" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['idfa', validatorRules.idfa]" placeholder="请输入idfa" />
        </a-form-item>
        <a-form-item label="手机品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['vendor', validatorRules.vendor]" placeholder="请输入手机品牌" />
        </a-form-item>
        <a-form-item label="手机型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['model', validatorRules.model]" placeholder="请输入手机型号" />
        </a-form-item>
        <a-form-item label="系统名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['system', validatorRules.system]" placeholder="请输入系统名" />
        </a-form-item>
        <a-form-item label="系统版本" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['systemVersion', validatorRules.systemVersion]" placeholder="请输入系统版本" />
        </a-form-item>
        <a-form-item label="网络类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['network', validatorRules.network]" placeholder="请输入网络类型" />
        </a-form-item>
        <a-form-item label="version_name" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['versionName', validatorRules.versionName]" placeholder="请输入version_name" />
        </a-form-item>
        <a-form-item label="version_code" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['versionCode', validatorRules.versionCode]" placeholder="请输入version_code" />
        </a-form-item>
        <a-form-item label="平台" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['platform', validatorRules.platform]" placeholder="请输入平台" />
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- </a-modal> -->
    <a-button type="primary" @click="handleCancel">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';

export default {
  name: 'PlayerRegisterInfoModal',
  components: {},
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
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
      validatorRules: {
        account: {},
        id: { rules: [{ required: true, message: '请输入玩家ID!' }] },
        serverId: { rules: [{ required: true, message: '请输入区服Id!' }] },
        name: { rules: [{ required: true, message: '请输入角色名称!' }] },
        ip: {},
        channel: {},
        imei: {},
        mac: {},
        idfa: {},
        vendor: {},
        model: {},
        system: {},
        systemVersion: {},
        network: {},
        versionName: {},
        versionCode: {},
        platform: {}
      },
      url: {
        add: 'player/registerInfo/add',
        edit: 'player/registerInfo/edit'
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
      this.visible = true;
      this.isEdit = this.model.id != null;
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'account',
            'playerId',
            'serverId',
            'birthId',
            'name',
            'ip',
            'channel',
            'sdkChannel',
            'imei',
            'mac',
            'idfa',
            'vendor',
            'model',
            'system',
            'systemVersion',
            'network',
            'versionName',
            'versionCode',
            'platform',
            'createDate'
          )
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
          console.log('表单提交数据', formData);
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
        pick(
          row,
          'account',
          'playerId',
          'serverId',
          'birthId',
          'name',
          'ip',
          'channel',
          'sdkChannel',
          'imei',
          'mac',
          'idfa',
          'vendor',
          'model',
          'system',
          'systemVersion',
          'network',
          'versionName',
          'versionCode',
          'platform',
          'createDate'
        )
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
