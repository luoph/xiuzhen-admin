<template>
  <!-- <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-if="isEdit" v-decorator="['serverIds', validatorRules.serverIds]" placeholder="区服id" />
          <game-server-selector v-model="model.serverIds" @onSelectServer="changeSelect"/>
        </a-form-item>
        <a-form-item label="问卷地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['url', validatorRules.url]" placeholder="请输入问卷调查地址"/>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="状态" v-decorator="['status', validatorRules.status]" initialValue="1">
            <a-select-option :value="1">有效</a-select-option>
            <a-select-option :value="0">无效</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-col :md="8" :sm="8">
            <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss"
                           v-decorator="['startTime', validatorRules.startTime]"/>
          </a-col>
          <a-col :md="8" :sm="8">
            <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss"
                           v-decorator="['endTime', validatorRules.endTime]"/>
          </a-col>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['remark', validatorRules.remark]" placeholder="请输入备注" />
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
import {httpAction} from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import moment from 'moment';
import GameServerSelector from '@/components/gameserver/GameServerSelector';

export default {
  name: 'GameQuestionnaireModal',
  components: {
    JDate,
    GameServerSelector
  },
  data() {
    return {
      form: this.$form.createForm(this),
      title: '操作',
      width: 1000,
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
        serverIds: {rules: [{required: true, message: '请选择区服id！'}]},
        url: {rules: [{required: true, message: '请输入问卷地址!'}]},
        status: {rules: [{required: true, message: '请选择状态!'}]},
        startTime: {rules: [{required: true, message: '请输入开始时间!'}]},
        endTime: {rules: [{required: true, message: '请输入结束时间!'}]},
        remark: {rules: [{required: true, message: '请输入备注!'}]}
      },
      url: {
        add: 'game/questionnaire/add',
        edit: 'game/questionnaire/edit'
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
      console.log('GameQuestionnaireModal, model:', JSON.stringify(this.model));

      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'serverIds', 'url', 'status', 'startTime', 'endTime', 'remark'));

        // 时间格式化
        this.form.setFieldsValue({startTime: this.model.startTime ? moment(this.model.startTime) : null});
        this.form.setFieldsValue({endTime: this.model.endTime ? moment(this.model.endTime) : null});
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
          formData.startTime = formData.startTime ? formData.startTime.format('YYYY-MM-DD HH:mm:ss') : null;
          formData.endTime = formData.endTime ? formData.endTime.format('YYYY-MM-DD HH:mm:ss') : null;
          // 创建时间参数不传递后台
          delete formData.createTime;
          delete formData.updateTime;

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
      this.form.setFieldsValue(pick(row, 'serverIds', 'url', 'status', 'startTime', 'endTime', 'remark'));
    },
    changeSelect(value) {
      this.model.serverIds = value.join(',');
      this.form.setFieldsValue({
        serverIds: value.join(',')
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
