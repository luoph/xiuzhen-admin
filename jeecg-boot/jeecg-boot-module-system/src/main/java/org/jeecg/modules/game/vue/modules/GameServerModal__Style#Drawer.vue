<template>
  <a-drawer
      :title="title"
      :width="800"
      placement="right"
      :closable="false"
      @close="close"
      :visible="visible"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务器名字">
          <a-input placeholder="请输入服务器名字" v-decorator="['name', validatorRules.name ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务器路径">
          <a-input placeholder="请输入服务器路径" v-decorator="['host', validatorRules.host ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务器端口">
          <a-input-number v-decorator="[ 'port', validatorRules.port ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="登陆地址和端口">
          <a-input placeholder="请输入登陆地址和端口" v-decorator="['loginUrl', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务器状态 0-正常 1-流畅 2-火爆 3-维护">
          <a-input placeholder="请输入服务器状态 0-正常 1-流畅 2-火爆 3-维护" v-decorator="['status', validatorRules.status ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服">
          <a-input placeholder="请输入推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服" v-decorator="['recommend', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="出错提示信息">
          <a-input placeholder="请输入出错提示信息" v-decorator="['warning', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="显示版本号 0-不显示 1-显示">
          <a-input placeholder="请输入显示版本号 0-不显示 1-显示" v-decorator="['showVersion', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="进入游戏客户端版本">
          <a-input-number v-decorator="[ 'clientVersionCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库路径">
          <a-input placeholder="请输入数据库路径" v-decorator="['dbHost', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库端口">
          <a-input-number v-decorator="[ 'dbPort', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库用户名">
          <a-input placeholder="请输入数据库用户名" v-decorator="['dbUser', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库密码">
          <a-input placeholder="请输入数据库密码" v-decorator="['dbPassword', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库名">
          <a-input placeholder="请输入数据库名" v-decorator="['dbName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="后台HTTP端口">
          <a-input-number v-decorator="[ 'httpPort', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序字段">
          <a-input-number v-decorator="[ 'position', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务器类型 0-混服 1-专服">
          <a-input placeholder="请输入服务器类型 0-混服 1-专服" v-decorator="['type', validatorRules.type ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="合服时母服id">
          <a-input-number v-decorator="[ 'pid', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="合服时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'mergeTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="扩展字段">
          <a-input placeholder="请输入扩展字段" v-decorator="['extra', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务器开服时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'openTime', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "GameServerModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        name:{rules: [{ required: true, message: '请输入服务器名字!' }]},
        host:{rules: [{ required: true, message: '请输入服务器路径!' }]},
        port:{rules: [{ required: true, message: '请输入服务器端口!' }]},
        status:{rules: [{ required: true, message: '请输入服务器状态 0-正常 1-流畅 2-火爆 3-维护!' }]},
        type:{rules: [{ required: true, message: '请输入服务器类型 0-混服 1-专服!' }]},
        },
        url: {
          add: "/game/gameServer/add",
          edit: "/game/gameServer/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','host','port','loginUrl','status','recommend','warning','showVersion','clientVersionCode','dbHost','dbPort','dbUser','dbPassword','dbName','httpPort','position','type','pid','extra'))
		  //时间格式化
          this.form.setFieldsValue({mergeTime:this.model.mergeTime?moment(this.model.mergeTime):null})
          this.form.setFieldsValue({openTime:this.model.openTime?moment(this.model.openTime):null})
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.mergeTime = formData.mergeTime?formData.mergeTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.openTime = formData.openTime?formData.openTime.format('YYYY-MM-DD HH:mm:ss'):null;
            
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>