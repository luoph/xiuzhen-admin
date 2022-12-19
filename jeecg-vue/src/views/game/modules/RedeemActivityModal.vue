<template>
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="激活码名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入激活码名称"></a-input>
        </a-form-item>
        <a-form-item label="礼包说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['summary', validatorRules.summary]" placeholder="请输入礼包说明"></a-input>
        </a-form-item>
        <a-form-item label="限制类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="请选择限制类型" v-decorator="['limitType', validatorRules.limitType]" initialValue="4">
            <a-select-option :value="0">0 - 通用</a-select-option>
            <a-select-option :value="1">1 - 指定渠道</a-select-option>
            <a-select-option :value="2">2 - SERVER</a-select-option>
            <a-select-option :value="4">4 - 同一分组只能兑换一次</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="分组id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isIncludeGroupModel" v-decorator="['groupId', validatorRules.groupId]" placeholder="请输入分组id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="限制渠道id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['channelIds', validatorRules.channelIds]" placeholder="请输入限制渠道id, 使用半角','分割多个id"></a-textarea>
        </a-form-item>
        <a-form-item label="限制区服id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['serverIds', validatorRules.serverIds]" placeholder="请输入限制区服id, 使用半角','分割多个id"></a-textarea>
        </a-form-item>
        <a-form-item label="活动状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="请选择活动状态" v-decorator="['status', validatorRules.status]" initialValue="1">
            <a-select-option :value="0">无效</a-select-option>
            <a-select-option :value="1">有效</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="奖励" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-button type="danger" icon="plus" @click="handleAddItem">奖励选择</a-button>
          <a-textarea v-decorator="['reward', { initialValue: itemTree }, validatorRules.reward]" placeholder="请输入奖励" :autoSize="{ minRows: 2, maxRows: 6 }" />
          <game-email-item-tree-modal ref="gameEmailItemTreeModal" @func="getItemTreeJson"></game-email-item-tree-modal>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注"></a-textarea>
        </a-form-item>
        <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择开始时间" v-decorator="['startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
        <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择结束时间" v-decorator="['endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
      </a-form>

      <a-tabs v-if="isEdit" defaultActiveKey="1">
        <a-tab-pane tab="激活码配置" key="1">
          <redeem-code-list ref="redeemCodeList" :disableMixinCreated="true"></redeem-code-list>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import RedeemCodeList from '../RedeemCodeList';
import GameEmailItemTreeModal from './GameEmailItemTreeModal';

export default {
  name: 'RedeemActivityModal',
  components: {
    JDate,
    RedeemCodeList,
    GameEmailItemTreeModal
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 1024,
      visible: false,
      isEdit: false,
      isIncludeGroupModel: false,
      itemTree: null,
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
        name: { rules: [{ required: true, message: '请输入激活码名称!' }] },
        summary: { rules: [{ required: true, message: '请输入礼包说明!' }] },
        limitType: { rules: [{ required: true, message: '请选择限制类型!' }] },
        groupId: {},
        channelIds: {},
        serverIds: {},
        status: { rules: [{ required: true, message: '请选择活动状态!' }] },
        reward: { rules: [{ required: true, message: '请输入奖励!' }] },
        remark: { rules: [{ required: true, message: '请输入备注!' }] },
        startTime: { rules: [{ required: true, message: '请选择开始时间!' }] },
        endTime: {}
      },
      url: {
        add: 'game/redeemActivity/add',
        edit: 'game/redeemActivity/edit'
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
      this.isIncludeGroupModel = this.model.isIncludeGroupModel;
      this.visible = true;
      this.$nextTick(() => {
        if (this.isEdit) {
          // 手动渲染数据
          this.$refs.redeemCodeList.reset();
          this.$refs.redeemCodeList.loadDateById(record);
        }
        this.form.setFieldsValue(pick(this.model, 'name', 'summary', 'limitType', 'groupId', 'channelIds', 'serverIds', 'status', 'reward', 'remark', 'startTime', 'endTime'));
      });
    },
    close() {
      this.$emit('close');
      this.itemTree = null;
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
      this.form.setFieldsValue(pick(row, 'name', 'summary', 'limitType', 'groupId', 'channelIds', 'serverIds', 'status', 'reward', 'remark', 'startTime', 'endTime'));
    },
    handleAddItem() {
      this.$refs.gameEmailItemTreeModal.$emit('getItemTree');
      this.reward = null;
    },
    getItemTreeJson(item) {
      this.itemTree = null;
      this.itemTree = item;
      this.form.setFieldsValue({
        reward: item
      });
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
