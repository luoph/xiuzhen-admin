<template>
  <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="充值订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['orderId', validatorRules.orderId]" placeholder="请输入充值订单号" />
        </a-form-item>
        <a-form-item label="平台订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['queryId', validatorRules.queryId]" placeholder="请输入平台订单号" />
        </a-form-item>
        <a-form-item label="渠道" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['channel', validatorRules.channel]" placeholder="请输入渠道" />
        </a-form-item>
        <a-form-item label="区服Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['serverId', validatorRules.serverId]" placeholder="请输入区服Id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['playerId', validatorRules.playerId]" placeholder="请输入玩家id" style="width: 100%" />
        </a-form-item>
        <a-form-item label="商品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['productId', validatorRules.productId]" placeholder="请输入商品id" />
        </a-form-item>
        <a-form-item label="ip地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['remoteIp', validatorRules.remoteIp]" placeholder="请输入ip地址" />
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="订单状态">
          <a-select :disabled="isEdit" placeholder="请选择订单状态" v-decorator="['orderStatus', validatorRules.orderStatus]">
            <a-select-option value="0">待支付</a-select-option>
            <a-select-option value="1">已支付</a-select-option>
            <a-select-option value="2">已转发</a-select-option>
            <a-select-option value="3">发放中</a-select-option>
            <a-select-option value="4">已发放</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="支付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['payAmount', validatorRules.payAmount]" placeholder="请输入支付金额" style="width: 100%" />
        </a-form-item>
        <a-form-item label="订单金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['orderAmount', validatorRules.orderAmount]" placeholder="请输入订单金额" style="width: 100%" />
        </a-form-item>
        <a-form-item label="折扣金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['discountAmount', validatorRules.discountAmount]" placeholder="请输入折扣金额" style="width: 100%" />
        </a-form-item>
        <a-form-item label="custom" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number :disabled="isEdit" v-decorator="['custom', validatorRules.custom]" placeholder="请输入透传参数" style="width: 100%" />
        </a-form-item>
        <a-form-item label="充值货币" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="['currency', validatorRules.currency]" placeholder="请输入充值货币" />
        </a-form-item>
        <a-form-item label="支付时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            :disabled="isEdit"
            placeholder="请选择支付时间"
            v-decorator="['payTime', validatorRules.payTime]"
            :trigger-change="true"
            :show-time="true"
            date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="发货时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            :disabled="isEdit"
            placeholder="请选择发货时间"
            v-decorator="['sendTime', validatorRules.sendTime]"
            :trigger-change="true"
            :show-time="true"
            date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            :disabled="isEdit"
            placeholder="请选择更新时间"
            v-decorator="['updateTime', validatorRules.updateTime]"
            :trigger-change="true"
            :show-time="true"
            date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date
            :disabled="isEdit"
            placeholder="请选择创建时间"
            v-decorator="['createTime', validatorRules.createTime]"
            :trigger-change="true"
            :show-time="true"
            date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
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
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'GameOrderModal',
  components: {
    JDate
  },
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
        orderId: { rules: [{ required: true, message: '请输入充值订单号!' }] },
        queryId: {},
        channel: { rules: [{ required: true, message: '请输入渠道!' }] },
        serverId: { rules: [{ required: true, message: '请输入区服Id!' }] },
        playerId: { rules: [{ required: true, message: '请输入玩家id!' }] },
        productId: {},
        remoteIp: { rules: [{ required: true, message: '请输入ip地址!' }] },
        orderStatus: { rules: [{ required: true, message: '请选择订单状态!' }] },
        payAmount: { rules: [{ required: true, message: '请输入充值金额!' }] },
        discountAmount: {},
        orderAmount: {},
        currency: {},
        payTime: {},
        sendTime: {},
        updateTime: {},
        createTime: {}
      },
      url: {
        add: 'game/orderadd',
        edit: 'game/orderedit'
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
        this.form.setFieldsValue(
          pick(
            this.model,
            'orderId',
            'queryId',
            'channel',
            'serverId',
            'playerId',
            'productId',
            'remoteIp',
            'orderStatus',
            'payAmount',
            'orderAmount',
            'discountAmount',
            'custom',
            'currency',
            'payTime',
            'sendTime',
            'updateTime',
            'createTime'
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
          'orderId',
          'queryId',
          'channel',
          'serverId',
          'playerId',
          'productId',
          'remoteIp',
          'orderStatus',
          'payAmount',
          'orderAmount',
          'discountAmount',
          'custom',
          'currency',
          'payTime',
          'sendTime',
          'updateTime',
          'createTime'
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
