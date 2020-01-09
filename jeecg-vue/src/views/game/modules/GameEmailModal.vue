<template>
    <a-drawer
        :title="title"
        :width="width"
        placement="right"
        :closable="false"
        @close="close"
        :visible="visible"
    >
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['title', validatorRules.title]" placeholder="请输入标题"></a-input>
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                 <a-textarea
                    v-decorator="['remark', validatorRules.remark]"
      placeholder="请输入描述"
      :autosize="{ minRows: 2, maxRows: 6 }"
    />
                </a-form-item>
                <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group
                        @change="contentType"
                        v-decorator="['emailType',{'initialValue':1}, validatorRules.emailType]"
                        style="width: 100%"
                    >
                    <a-radio-button :value="1">无附件</a-radio-button>
                        <a-radio-button :value="2">有附件</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="contentData" label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea
                     v-decorator="['content', validatorRules.content]"
      placeholder="请输入附件"
      :autosize="{ minRows: 2, maxRows: 6 }"
    />
                </a-form-item>
                <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group
                        v-decorator="['validState',{'initialValue':1}]"
                        dict
                        style="width: 100%"
                    >
                        <a-radio-button :value="1">有效</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item label="目标类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group
                        @change="selectTarget"
                        v-decorator="['targetBodyType',{'initialValue':1}]"
                        dict
                        style="width: 100%"
                    >
                     <a-radio-button :value="1">玩家</a-radio-button>
                        <a-radio-button :value="2">全服</a-radio-button>
                       
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="serverType" label="区服Id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select
                        ref="serverSelector"
                        v-decorator="['targetBodyId',{'initialValue':0} ,validatorRules.targetBodyId]"
                        style="width: 100%"
                    >
                        <a-select-option :value="0">---请选择服务器---</a-select-option>
                        <a-select-option
                            v-for="server in serverList"
                            :key="server.name"
                            :value="server.id"
                        >{{ server.name }}</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item v-if="playerType" label="玩家ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input
                        v-decorator="['targetBodyId',{'initialValue':null}, validatorRules.targetBodyId]"
                        placeholder="请输入玩家ID"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="生效时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        placeholder="请选择生效时间"
                        v-decorator="['sendTime', validatorRules.sendTime]"
                        :trigger-change="true"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        placeholder="请选择开始时间"
                        v-decorator="['validStarTime', validatorRules.validStarTime]"
                        :trigger-change="true"
                        style="width: 100%"
                    />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date
                        placeholder="请选择结束时间"
                        v-decorator="['validEndTime', validatorRules.validEndTime]"
                        :trigger-change="true"
                        style="width: 100%"
                    />
                </a-form-item>
            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from "@/api/manage";
import pick from "lodash.pick";
import JDate from "@/components/jeecg/JDate";
import JSearchSelectTag from "@/components/dict/JSearchSelectTag";
import { getAction } from "@/api/manage";
export default {
    name: "GameEmailModal",
    components: {
        JDate,
        JSearchSelectTag
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
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
                title: { rules: [{ required: true, message: "请输入标题!" }] },
                remark: { rules: [{ required: true, message: "请输入描述!" }] },
                emailType: { rules: [{ required: true, message: "请选择类型!" }] },
                content: {},
                validState: { rules: [{ required: true, message: "请选择状态!" }] },
                targetBodyType: { rules: [{ required: true, message: "请选择目标类型!" }] },
                targetBodyId: { rules: [{ required: true, message: "请输入目标主体ID!" }] },
                sendTime: { rules: [{ required: true, message: "请输入生效时间!" }] },
                validStarTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                validEndTime: {}
            },
            serverType: false,
            playerType: true,
            serverList: [],
            serverIdOption: true,
            contentData:false,
            url: {
                add: "game/gameEmail/add",
                edit: "game/gameEmail/edit",
                serverListUrl: "game/gameServer/list"
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
                    pick(
                        this.model,
                        "title",
                        "remark",
                        "emailType",
                        "content",
                        "validState",
                        "targetBodyType",
                        "targetBodyId",
                        "sendTime",
                        "validStarTime",
                        "validEndTime",
                        "createBy",
                        "createTime",
                        "updateBy",
                        "updateTime"
                    )
                );
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
                    let httpUrl = "";
                    let method = "";
                    if (!this.model.id) {
                        httpUrl += this.url.add;
                        method = "post";
                    } else {
                        httpUrl += this.url.edit;
                        method = "put";
                    }
                    let formData = Object.assign(this.model, values);
                    console.log("表单提交数据", formData);
                    httpAction(httpUrl, formData, method)
                        .then(res => {
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
                    "title",
                    "remark",
                    "emailType",
                    "content",
                    "validState",
                    "targetBodyType",
                    "targetBodyId",
                    "sendTime",
                    "validStarTime",
                    "validEndTime",
                    "createBy",
                    "createTime",
                    "updateBy",
                    "updateTime"
                )
            );
        },
        mounted(){
            this.getServerList();
        },
        selectTarget(e) {
            if (e.target.value == 1) {
                this.serverType = false;
                this.playerType = true;
                this.validatorRules.targetBodyId="";
            } else if (e.target.value == 2) {
                this.serverType = true;
                this.playerType = false;
                this.getServerList();
            }
            
        },
        contentType(e){
            if (e.target.value == 1) {
                this.contentData = false;
                
            } else if (e.target.value == 2) {
                this.contentData = true;
                this.validatorRules.content={ rules: [{ required: true, message: "请添加附件!" }]};
            }
        },
        getServerList:function(){
            getAction(this.url.serverListUrl).then(res=>{
                this.serverList = res.result.records;
            })
        }
    }
};
</script>

// <style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>