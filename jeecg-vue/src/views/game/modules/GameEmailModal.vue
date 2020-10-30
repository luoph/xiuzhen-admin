<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['title', validatorRules.title]" placeholder="请输入标题"></a-input>
                </a-form-item>
                <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea v-decorator="['remark', validatorRules.remark]" placeholder="请输入描述" :autosize="{ minRows: 2, maxRows: 6 }" />
                </a-form-item>
                <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group @change="contentType" v-decorator="['emailType', { initialValue: 1 }, validatorRules.emailType]" style="width: 100%;">
                        <a-radio-button :value="1">无附件</a-radio-button>
                        <a-radio-button :value="2">有附件</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="contentData" :visible.sync="contentData" label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-button type="danger" icon="plus" @click="handleAddItem">奖励选择</a-button>
                    <a-textarea v-decorator="['content', { initialValue: itemTree }, validatorRules.content]" placeholder="请输入附件" :autoSize="{ minRows: 2, maxRows: 6 }" />
                    <game-email-item-tree-modal ref="gameEmailItemTreeModal" @func="getItemTreeJson"></game-email-item-tree-modal>
                </a-form-item>
                <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group v-decorator="['validState', { initialValue: 1 }]" dict style="width: 100%;"><a-radio-button :value="1">有效</a-radio-button></a-radio-group>
                </a-form-item>
                <a-form-item label="目标类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-radio-group @change="selectTarget" v-decorator="['targetBodyType', { initialValue: 1 }]" dict style="width: 100%;">
                        <a-radio-button :value="1">玩家</a-radio-button>
                        <a-radio-button :value="2">服务器</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item v-if="playerType" label="玩家ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-textarea
                        v-decorator="['targetBody', { initialValue: '' }]"
                        placeholder="请以英文“,”分割输入多个玩家ID"
                        style="width: 100%;"
                        :autoSize="{ minRows: 2, maxRows: 6 }"
                    />
                </a-form-item>
                <a-form-item v-if="serverType" label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <multiple-server-select v-decorator="['targetBodyIds', { initialValue: '' }]" @changeSelect="change"></multiple-server-select>
                </a-form-item>
                <a-form-item label="生效时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择生效时间" v-decorator="['sendTime', validatorRules.sendTime]" :trigger-change="true" style="width: 100%;" />
                </a-form-item>
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择开始时间" v-decorator="['validStarTime', validatorRules.validStarTime]" :trigger-change="true" style="width: 100%;" />
                </a-form-item>
                <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择结束时间" v-decorator="['validEndTime', validatorRules.validEndTime]" :trigger-change="true" style="width: 100%;" />
                </a-form-item>
            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <a-button type="primary" @click="handleOk">确定</a-button>
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
import { httpAction } from '@/api/manage';
import pick from 'lodash.pick';
import JDate from '@/components/jeecg/JDate';
import JSearchSelectTag from '@/components/dict/JSearchSelectTag';
import { Button } from 'ant-design-vue';
import GameEmailItemTreeModal from './GameEmailItemTreeModal';
import ServerSelect from '@/components/gameserver/ServerSelect';
import MultipleServerSelect from '@/components/gameserver/MultipleServerSelect';

export default {
    name: 'GameEmailModal',
    components: {
        JDate,
        JSearchSelectTag,
        Button,
        GameEmailItemTreeModal,
        ServerSelect,
        MultipleServerSelect
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: '操作',
            width: 800,
            visible: false,
            model: {},
            itemTree: null,
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
                title: { rules: [{ required: true, message: '请输入标题!' }] },
                remark: { rules: [{ required: true, message: '请输入描述!' }] },
                emailType: { rules: [{ required: true, message: '请选择类型!' }] },
                content: { rules: [{ required: true, message: '请添加附件!' }] },
                validState: { rules: [{ required: true, message: '请选择状态!' }] },
                targetBodyType: { rules: [{ required: true, message: '请选择目标类型!' }] },
                targetBodyIds: { rules: [{ required: false, message: '请以英文“,”分割输入多个玩家ID！' }] },
                sendTime: { rules: [{ required: true, message: '请输入生效时间!' }] },
                validStarTime: { rules: [{ required: true, message: '请输入开始时间!' }] },
                validEndTime: {}
            },
            serverType: false,
            playerType: true,
            contentData: false,
            url: {
                add: 'game/gameEmail/add',
                edit: 'game/gameEmail/edit'
            }
        };
    },
    created() {
        this.$form.createForm(this);
    },
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
                        'title',
                        'remark',
                        'emailType',
                        'content',
                        'validState',
                        'targetBodyType',
                        'targetBodyIds',
                        'sendTime',
                        'validStarTime',
                        'validEndTime',
                        'createBy',
                        'createTime',
                        'updateBy',
                        'updateTime'
                    )
                );
            });
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.serverType = false;
            this.playerType = true;
            this.validatorRules.content = null;
            this.contentData = false;
            this.itemTree = null;
            this.targetBody = '';
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
                    this.inputTargetBody(formData);
                    console.log('表单提交数据', formData);
                    httpAction(httpUrl, formData, method)
                        .then(res => {
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
            if (this.validatorRules.content !== null) {
                this.form.setFieldsValue({
                    content: null
                });
            }
        },
        handleCancel() {
            this.close();
        },
        popupCallback(row) {
            this.form.setFieldsValue(
                pick(
                    row,
                    'title',
                    'remark',
                    'emailType',
                    'content',
                    'validState',
                    'targetBodyType',
                    'targetBodyIds',
                    'sendTime',
                    'validStarTime',
                    'validEndTime',
                    'createBy',
                    'createTime',
                    'updateBy',
                    'updateTime'
                )
            );
        },
        selectTarget(e) {
            // 1-玩家 2-服务器
            if (e.target.value === 1) {
                this.serverType = false;
                this.playerType = true;
            } else if (e.target.value === 2) {
                this.serverType = true;
                this.playerType = false;
            }
            this.validatorRules.targetBodyIds = '';
            this.targetBody = '';
        },
        contentType(e) {
            if (e.target.value === 1) {
                this.contentData = false;
            } else if (e.target.value === 2) {
                this.contentData = true;
                this.validatorRules.content = { rules: [{ required: true, message: '请添加附件!' }] };
                this.validatorRules.content = '';
            }
        },
        handleAddItem() {
            this.$refs.gameEmailItemTreeModal.visible = true;
            this.$refs.gameEmailItemTreeModal.$emit('getItemTree');
            this.content = null;
        },
        getItemTreeJson(item) {
            console.log(item);
            this.itemTree = null;
            this.itemTree = item;
            this.form.setFieldsValue({
                content: item
            });
        },
        change(value) {
            this.form.setFieldsValue({
                targetBodyIds: value.join(',')
            });
        },
        inputTargetBody(formData) {
            if(this.playerType){
                let a = this.form.getFieldValue('targetBody');
                if (a !== null && a !== '' && a !== undefined) {
                    formData.targetBodyIds = a;
                }
            }
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
