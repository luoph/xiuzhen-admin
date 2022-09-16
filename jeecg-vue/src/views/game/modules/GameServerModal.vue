<template>
  <!-- <a-drawer :title="title" :width="1000" placement="right" :closable="false" @close="close" :visible="visible"> -->
  <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk"
           @cancel="handleCancel" cancelText="关闭" okText="保存">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服id">
          <a-input-number :disabled="isEdit" placeholder="请输入区服id" v-decorator="['id', validatorRules.id]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服名字">
          <a-input placeholder="请输入区服名字" v-decorator="['name', validatorRules.name]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标签">
          <j-dict-select-tag :triggerChange="true" v-decorator="['tagId', validatorRules.tagId]"
                             placeholder="请选择标签" dictCode="game_server_tag,name,id"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服备注">
          <a-input placeholder="请输入区服备注" v-decorator="['remark', validatorRules.remark]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="游戏编号">
          <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库 -->
          <j-dict-select-tag v-decorator="['gameId', validatorRules.gameId]" placeholder="请选择游戏编号"
                             dictCode="game_info,name,id"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服Host">
          <a-input placeholder="请输入区服Host" v-decorator="['host', validatorRules.host]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Websocket地址">
          <a-input placeholder="请输入Websocket地址" v-decorator="['loginUrl', validatorRules.loginUrl]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="GM地址">
          <a-input v-decorator="['gmUrl', validatorRules.gmUrl]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服状态">
          <a-select placeholder="请选择区服状态" v-decorator="['status', validatorRules.status]">
            <a-select-option :value="0">正常</a-select-option>
            <a-select-option :value="1">流畅</a-select-option>
            <a-select-option :value="2">火爆</a-select-option>
            <a-select-option :value="3">维护</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开启维护">
          <a-select placeholder="开启维护" v-decorator="['isMaintain', validatorRules.isMaintain]">
            <a-select-option :value="0">否</a-select-option>
            <a-select-option :value="1">是</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="推荐标识">
          <a-select placeholder="请选择推荐标识" v-decorator="['recommend', validatorRules.recommend]">
            <a-select-option :value="0">普通</a-select-option>
            <a-select-option :value="1">推荐</a-select-option>
            <a-select-option :value="2">新服</a-select-option>
            <a-select-option :value="3">推荐新服</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="数数统计" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="" v-decorator="['taStatistics', validatorRules.taStatistics]" initialValue="1">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="GM开关" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select placeholder="" v-decorator="['gmStatus', validatorRules.gmStatus]" initialValue="0">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="GM可用IP" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['gmIp', validatorRules.gmIp]"/>
        </a-form-item>
        <a-form-item label="GM可用玩家id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['gmPlayerId', validatorRules.gmPlayerId]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="出错提示信息">
          <a-input placeholder="请输入出错提示信息" v-decorator="['warning', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户端最小版本号">
          <a-input-number v-decorator="['minVersion', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户端最大版本号">
          <a-input-number v-decorator="['maxVersion', {}]"/>
        </a-form-item>
        <!--        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库Host">-->
        <!--          <a-input :disabled="isEdit" placeholder="请输入数据库Host" v-decorator="['dbHost', validatorRules.dbHost]"/>-->
        <!--        </a-form-item>-->
        <!--        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库端口">-->
        <!--          <a-input-number :disabled="isEdit" v-decorator="['dbPort', validatorRules.dbPort]"/>-->
        <!--        </a-form-item>-->
        <!--        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库用户名">-->
        <!--          <a-input :disabled="isEdit" placeholder="请输入数据库用户名" v-decorator="['dbUser', validatorRules.dbUser]"/>-->
        <!--        </a-form-item>-->
        <!--        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库密码">-->
        <!--          <a-input-password :disabled="isEdit" placeholder="请输入数据库密码"-->
        <!--                            v-decorator="['dbPassword', validatorRules.dbPassword]"/>-->
        <!--        </a-form-item>-->
        <!--        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据库名">-->
        <!--          <a-input :disabled="isEdit" placeholder="请输入数据库名" v-decorator="['dbName', validatorRules.dbName]"/>-->
        <!--        </a-form-item>-->
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服类型">
          <a-select v-decorator="['type', {}]" placeholder="请选择区服类型">
            <a-select-option :value="0">混服</a-select-option>
            <a-select-option :value="1">专服</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="合并状态">
          <a-select placeholder="合并状态" v-decorator="['outdated', validatorRules.outdated]" initialValue="0">
            <a-select-option :value="0">未合并</a-select-option>
            <a-select-option :value="1">已合并</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="合服后母服id">
          <a-input-number v-decorator="['pid', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="保留玩家id">
          <a-input-number v-decorator="['reservePlayerId', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="合服时间">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['mergeTime', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单服活动结算时间">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['singleSettleTime', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="在线统计开关">
          <a-select placeholder="在线统计开关" v-decorator="['onlineStat', validatorRules.payCallbackStatus]"
                    initialValue="1">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="支付回调开关">
          <a-select placeholder="支付回调开关" v-decorator="['payCallbackStatus', validatorRules.payCallbackStatus]"
                    initialValue="1">
            <a-select-option :value="0">关闭</a-select-option>
            <a-select-option :value="1">开启</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="扩展字段">
          <a-input placeholder="请输入扩展字段" v-decorator="['extra', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开服时间">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['openTime', {}]"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上线时间">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['onlineTime', {}]"/>
        </a-form-item>
        <a-form-item v-if="isEdit" :labelCol="labelCol" :wrapperCol="wrapperCol" label="创建时间">
          <a-date-picker :disabled="isEdit" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['createTime', {}]"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <!-- <a-button type="primary" @click="handleOk">确定</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer> -->
</template>

<script>
import {httpAction} from "@/api/manage";
import pick from "lodash.pick";
import moment from "moment";

export default {
  name: "GameServerModal",
  data() {
    return {
      title: "操作",
      visible: false,
      isEdit: false,
      model: {},
      gameList: [],
      labelCol: {
        xs: {span: 24},
        sm: {span: 5}
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 16}
      },

      confirmLoading: false,
      form: this.$form.createForm(this),

      validatorRules: {
        id: {rules: [{required: true, message: "请输入区服id"}]},
        gameId: {rules: [{required: true, message: "请选择游戏id!"}]},
        name: {rules: [{required: true, message: "请输入区服名字!"}]},
        remark: {rules: [{required: true, message: "请输入区服备注!"}]},
        tagId: {rules: [{required: false, message: "请选择标签!"}]},
        host: {rules: [{required: true, message: "请输入前端HOST!"}]},
        loginUrl: {rules: [{required: true, message: "请输入登录地址!"}]},
        status: {rules: [{required: true, message: "请输入区服状态!"}]},
        isMaintain: {rules: [{required: true, message: "请选择是否开启维护!"}]},
        type: {rules: [{required: true, message: "请输入区服类型!"}]},
        outdated: {rules: [{required: true, message: "请选择是否废弃!"}]},
        // dbHost: {rules: [{required: true, message: "请输入数据库Host!"}]},
        // dbUser: {rules: [{required: true, message: "请输入数据库帐号!"}]},
        // dbPassword: {rules: [{required: true, message: "请输入数据库密码!"}]},
        // dbName: {rules: [{required: true, message: "请输入数据库名称!"}]},
        gmUrl: {rules: [{required: true, message: "请输入GM地址!"}]},
        gmIp: {rules: [{required: false, message: "请输入GM可用ip!"}]},
        recommend: {rules: [{required: true, message: "请选择推荐标识!"}]},
        gmPlayerId: {rules: [{required: false, message: "请输入GM可用玩家id!"}]},
        gmStatus: {rules: [{required: true, message: "请设置GM开关!"}]},
        taStatistics: {rules: [{required: true, message: "请设置TA开关!"}]},
        onlineStat: {rules: [{required: true, message: "请设置在线统计开关!"}]},
        payCallbackStatus: {rules: [{required: true, message: "请设置支付回调开关!"}]}
      },
      url: {
        add: "game/gameServer/add",
        edit: "game/gameServer/edit"
      }
    };
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.isEdit = this.model.id != null;
      // 转换成 string 格式
      if (this.model.tagId) {
        this.model.tagId = this.model.tagId + "";
      }
      if (this.model.gameId) {
        this.model.gameId = this.model.gameId + "";
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            "id",
            "name",
            "remark",
            "gameId",
            "tagId",
            "host",
            "loginUrl",
            "gmUrl",
            "status",
            "isMaintain",
            "recommend",
            "warning",
            "minVersion",
            "maxVersion",
            "type",
            "outdated",
            "reservePlayerId",
            "singleSettleTime",
            "pid",
            "taStatistics",
            "gmStatus",
            "gmIp",
            "gmPlayerId",
            "payCallbackStatus",
            "onlineStat",
            "extra"
          )
        );

        // 时间格式化
        this.form.setFieldsValue({openTime: this.model.openTime ? moment(this.model.openTime) : null});
        this.form.setFieldsValue({onlineTime: this.model.onlineTime ? moment(this.model.onlineTime) : null});
        this.form.setFieldsValue({mergeTime: this.model.mergeTime ? moment(this.model.mergeTime) : null});
        this.form.setFieldsValue({singleSettleTime: this.model.singleSettleTime ? moment(this.model.singleSettleTime) : null});
        this.form.setFieldsValue({createTime: this.model.createTime ? moment(this.model.createTime) : null});
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          let formData = Object.assign(this.model, values);
          // 时间格式化
          formData.openTime = formData.openTime ? formData.openTime.format("YYYY-MM-DD HH:mm:ss") : null;
          formData.mergeTime = formData.mergeTime ? formData.mergeTime.format("YYYY-MM-DD HH:mm:ss") : null;
          formData.singleSettleTime = formData.singleSettleTime ? formData.singleSettleTime.format("YYYY-MM-DD HH:mm:ss") : null;
          formData.onlineTime = formData.onlineTime ? formData.onlineTime.format("YYYY-MM-DD HH:mm:ss") : null;

          // 创建时间参数不传递后台
          delete formData.createTime;

          console.log(formData);
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
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
          "id",
          "name",
          "remark",
          "gameId",
          "tagId",
          "host",
          "loginUrl",
          "gmUrl",
          "status",
          "isMaintain",
          "recommend",
          "warning",
          "minVersion",
          "maxVersion",
          "type",
          "outdated",
          "reservePlayerId",
          "pid",
          "taStatistics",
          "gmStatus",
          "gmIp",
          "gmPlayerId",
          "payCallbackStatus",
          "onlineStat",
          "extra"
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
