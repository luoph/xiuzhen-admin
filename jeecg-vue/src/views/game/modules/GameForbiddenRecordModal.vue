<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="执行的增删改操作类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['operation', validatorRules.operation]" placeholder="请输入执行的增删改操作类型" />
        </a-form-item>
        <a-form-item label="封号禁言表id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['forbiddenId', validatorRules.forbiddenId]" placeholder="请输入封号禁言表id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="服务器id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入服务器id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="1-登录 2-聊天" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['type', validatorRules.type]" placeholder="请输入1-登录 2-聊天" style="width: 100%" />
        </a-form-item>
        <a-form-item label="playerId/ip/deviceId" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['banKey', validatorRules.banKey]" placeholder="请输入playerId/ip/deviceId" />
        </a-form-item>
        <a-form-item label="对应 ban_type 的值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['banValue', validatorRules.banValue]" placeholder="请输入对应 ban_type 的值" />
        </a-form-item>
        <a-form-item label="封禁原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reason', validatorRules.reason]" placeholder="请输入封禁原因" />
        </a-form-item>
        <a-form-item label="0-临时 1-永久" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['isForever', validatorRules.isForever]" placeholder="请输入0-临时 1-永久" style="width: 100%" />
        </a-form-item>
        <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择开始时间" v-decorator="['startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
        <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择结束时间" v-decorator="['endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
        <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['createBy', validatorRules.createBy]" placeholder="请输入操作人" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!--
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
        </a-drawer>
     -->
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'GameForbiddenRecordModal',
  components: {
    JDate
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 800,
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
        operation: {},
        forbiddenId: { rules: [{ required: true, message: '请输入封号禁言表id!' }] },
        serverId: { rules: [{ required: true, message: '请输入服务器id!' }] },
        type: {},
        banKey: { rules: [{ required: true, message: '请输入playerId/ip/deviceId!' }] },
        banValue: { rules: [{ required: true, message: '请输入对应 ban_type 的值!' }] },
        reason: { rules: [{ required: true, message: '请输入封禁原因!' }] },
        isForever: { rules: [{ required: true, message: '请输入0-临时 1-永久!' }] },
        startTime: {},
        endTime: {},
        createTime: {},
        createBy: {}
      },
      url: {
        add: 'game/forbiddenRecord/add',
        edit: 'game/forbiddenRecord/edit'
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
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(this.model, 'operation', 'forbiddenId', 'serverId', 'type', 'banKey', 'banValue', 'reason', 'isForever', 'startTime', 'endTime', 'createTime', 'createBy')
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
        pick(row, 'operation', 'forbiddenId', 'serverId', 'type', 'banKey', 'banValue', 'reason', 'isForever', 'startTime', 'endTime', 'createTime', 'createBy')
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