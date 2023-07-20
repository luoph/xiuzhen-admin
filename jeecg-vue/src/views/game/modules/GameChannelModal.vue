<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="渠道名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入渠道名称" v-decorator="['name', validatorRules.name]" />
        </a-form-item>
        <a-form-item label="渠道标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入渠道标识" :disabled="isEdit" v-decorator="['simpleName', validatorRules.simpleName]" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏编号">
          <j-search-select-tag placeholder="请选择游戏编号" v-decorator="['gameId', validatorRules.gameId]" dict="game_info,name,id" />
        </a-form-item>
        <a-form-item label="公告Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库 -->
          <j-search-select-tag placeholder="请选择公告Id" v-decorator="['noticeId', validatorRules.noticeId]" dict="game_notice,title,id" />
        </a-form-item>
        <a-form-item label="大渠道描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入大渠道描述" v-decorator="['remark', validatorRules.remark]" />
        </a-form-item>
        <a-form-item label="禁用IP白名单" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="启用后IP白名单失效，所有地址均可访问，谨慎开启！！！" v-decorator="['testLogin', validatorRules.testLogin]" initialValue="0">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="IP白名单" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea placeholder="请输入IP白名单(使用,分割)" :rows="5" v-decorator="['ipWhitelist', validatorRules.ipWhitelist]" />
        </a-form-item>
        <a-form-item label="版本号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number placeholder="请输入版本号" v-decorator="['versionCode', validatorRules.versionCode]" style="width: 100%" />
        </a-form-item>
        <a-form-item label="版本名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入版本名" v-decorator="['versionName', validatorRules.versionName]" />
        </a-form-item>
        <a-form-item label="版本更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker
            placeholder="版本更新时间"
            showTime
            format="YYYY-MM-DD HH:mm:ss"
            v-decorator="['versionUpdateTime', validatorRules.versionUpdateTime]"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="数数统计" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="数数统计" v-decorator="['taStatistics', validatorRules.taStatistics]" :initialValue="0">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="扩展字段" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['extra', validatorRules.extra]" placeholder="请输入扩展字段" />
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button> -->
  </a-modal>
  <!-- </a-drawer> -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import moment from 'moment';

export default {
  name: 'GameChannelModal',
  components: {},
  data() {
    return {
      form: this.$form.createForm(this),
      title: '编辑',
      width: 1000,
      isEdit: false,
      visible: false,
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
        gameId: { rules: [{ required: true, message: '请输入游戏编号!' }] },
        name: { rules: [{ required: true, message: '请输入渠道名称!' }] },
        simpleName: { rules: [{ required: true, message: '请输入唯一标识!' }] },
        noticeId: { rules: [{ required: true, message: '请输入公告Id!' }] },
        versionCode: { rules: [{ required: true, message: '请输入版本号!' }] },
        versionName: { rules: [{ required: true, message: '请输入版本名!' }] },
        versionUpdateTime: { rules: [{ required: true, message: '请选择版本更新时间!' }] },
        testLogin: { rules: [{ required: true, message: '请选择禁用白名单开关!' }] },
        taStatistics: { rules: [{ required: false, message: '请选择数数统计开关!' }] },
        ipWhitelist: {},
        remark: {},
        extra: {}
      },
      url: {
        add: 'game/channel/add',
        edit: 'game/channel/edit'
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
          pick(this.model, 'name', 'simpleName', 'gameId', 'noticeId', 'versionCode', 'versionName', 'taStatistics', 'testLogin', 'ipWhitelist', 'remark', 'extra')
        );
        // 时间格式化
        this.form.setFieldsValue({ versionUpdateTime: this.model.versionUpdateTime ? moment(this.model.versionUpdateTime) : null });
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
          formData.versionUpdateTime = formData.versionUpdateTime ? formData.versionUpdateTime.format('YYYY-MM-DD HH:mm:ss') : null;
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
          'name',
          'simpleName',
          'position',
          'gameId',
          'noticeId',
          'versionCode',
          'versionName',
          'versionUpdateTime',
          'taStatistics',
          'testLogin',
          'ipWhitelist',
          'remark',
          'extra',
          'groupName'
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
