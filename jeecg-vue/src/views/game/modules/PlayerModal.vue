<template>
  <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="生命" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['hp']" style="width: 100%"></a-input>
        </a-form-item>
        <a-form-item label="防御" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['def']" style="width: 100%"></a-input>
        </a-form-item>
        <a-form-item label="闪避" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['dodge']" style="width: 100%"></a-input>
        </a-form-item>
        <a-form-item label="命中" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['hit']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="速度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['speed']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="暴击" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['crit']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="暴抗" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['critDef']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="暴击率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['critPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="命中率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['hitPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="闪避率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['dodgePct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="暴抗率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['critDefPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="破军率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['breakAttPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="破军免伤率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['breakDrPct']" style="width: 100%" />
        </a-form-item>

        <a-form-item label="卓越率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['excelAttPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="卓越抵抗" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['excelDelPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="卓越伤害" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['excelDmgPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="卓越免伤" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['excelDrPct']" style="width: 100%" />
        </a-form-item>

        <a-form-item label="会心率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['insightAttPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="会心抵抗" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['insightDefPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="会心伤害" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['insightDmgPct']" style="width: 100%" />
        </a-form-item>
        <a-form-item label="会心免伤" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['insightDrPct']" style="width: 100%" />
        </a-form-item>
      </a-form>
    </a-spin>
    <!-- </a-modal> -->
    <!--<a-button type="primary" @click="handleOk">确定</a-button>-->
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
import { getAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';

export default {
  name: 'PlayerModal',
  components: {
    JDate,
    getAction
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
      url: {
        list: 'game/player/detail'
      }
    };
  },
  created() {
    this.$form.createForm(this);
  },
  methods: {
    close() {
      this.$emit('close');
      this.visible = false;
    },
    edit(record) {
      if (record.id) {
        this.playerId = record.id;
      }
      this.queryServerList();
      this.visible = true;
    },
    handleCancel() {
      this.close();
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          'hp',
          'def',
          'dodge',
          'hit',
          'speed',
          'crit',
          'critDef',
          'critPct',
          'hitPct',
          'dodgePct',
          'critDefPct',
          'breakAttPct',
          'breakDrPct',
          'excelAttPct',
          'excelDelPct',
          'excelDmgPct',
          'excelDrPct',
          'insightAttPct',
          'insightDefPct',
          'insightDmgPct',
          'insightDrPct'
        )
      );
    },
    queryServerList() {
      let that = this;
      getAction(that.url.list + '?playerId=' + this.playerId).then((res) => {
        if (res.success) {
          this.popupCallback(res.result);
        }
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
