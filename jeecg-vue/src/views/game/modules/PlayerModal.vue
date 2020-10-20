<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
    <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                    <a-form-item label="玩家账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['account', validatorRules.account]" placeholder="请输入玩家账号"></a-input>
                </a-form-item>
                <a-form-item label="角色昵称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['nickname', validatorRules.nickname]" placeholder="请输入角色昵称"></a-input>
                </a-form-item>
                <a-form-item label="角色头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['avatar', validatorRules.avatar]" placeholder="请输入角色头像"></a-input>
                </a-form-item>
                <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['sex', validatorRules.sex]" placeholder="请输入性别" style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否开启音乐 0否 1是" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['openMusic', validatorRules.openMusic]" placeholder="请输入是否开启音乐 0否 1是" style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否开启音效 0否 1是" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['openSound', validatorRules.openSound]" placeholder="请输入是否开启音效 0否 1是" style="width: 100%" />
                </a-form-item>
                <a-form-item label="出身id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['birthId', validatorRules.birthId]" placeholder="请输入出身id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="境界等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['level', validatorRules.level]" placeholder="请输入境界等级" style="width: 100%" />
                </a-form-item>
                <a-form-item label="界: 1.人界, 2.飞升灵界, 3.飞升仙界, 4.飞升圣界" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['realm', validatorRules.realm]" placeholder="请输入界: 1.人界, 2.飞升灵界, 3.飞升仙界, 4.飞升圣界" style="width: 100%" />
                </a-form-item>
                <a-form-item label="是否跳过战斗动画 0-否 1-是" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['skipCartoon', validatorRules.skipCartoon]" placeholder="请输入是否跳过战斗动画 0-否 1-是" style="width: 100%" />
                </a-form-item>
                <a-form-item label="背包大小" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['backpackSize', validatorRules.backpackSize]" placeholder="请输入背包大小" style="width: 100%" />
                </a-form-item>
                <a-form-item label="背包等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['backpackLevel', validatorRules.backpackLevel]" placeholder="请输入背包等级" style="width: 100%" />
                </a-form-item>
                <a-form-item label="修为值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['practiceValue', validatorRules.practiceValue]" placeholder="请输入修为值" style="width: 100%" />
                </a-form-item>
                <a-form-item label="修炼年数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['practiceYear', validatorRules.practiceYear]" placeholder="请输入修炼年数" style="width: 100%" />
                </a-form-item>
                <a-form-item label="战力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['combatPower', validatorRules.combatPower]" placeholder="请输入战力" style="width: 100%" />
                </a-form-item>
                <a-form-item label="vip等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['vipLevel', validatorRules.vipLevel]" placeholder="请输入vip等级" style="width: 100%" />
                </a-form-item>
                <a-form-item label="种族id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['race', validatorRules.race]" placeholder="请输入种族id" style="width: 100%" />
                </a-form-item>
                <a-form-item label="修为加持状态0-未开启 1-已开启 2-已结束" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['practiceState', validatorRules.practiceState]" placeholder="请输入修为加持状态0-未开启 1-已开启 2-已结束" style="width: 100%" />
                </a-form-item>
                <a-form-item label="渡劫增加成功率百分比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['successRate', validatorRules.successRate]" placeholder="请输入渡劫增加成功率百分比" style="width: 100%" />
                </a-form-item>
                <a-form-item label="修炼值结算时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择修炼值结算时间" v-decorator="['settleTime', validatorRules.settleTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="登录设备IP" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['loginIp', validatorRules.loginIp]" placeholder="请输入登录设备IP"></a-input>
                </a-form-item>
                <a-form-item label="登录时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择登录时间" v-decorator="['loginTime', validatorRules.loginTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="登出时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择登出时间" v-decorator="['logoutTime', validatorRules.logoutTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="境界更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择境界更新时间" v-decorator="['levelUpdateTime', validatorRules.levelUpdateTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择创建时间" v-decorator="['createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%" />
                </a-form-item>
                <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-date placeholder="请选择更新时间" v-decorator="['updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%" />
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

export default {
    name: "PlayerModal",
    components: {
        JDate,
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
                account: { rules: [{ required: true, message: "请输入玩家账号!" }] },
                nickname: {},
                avatar: {},
                sex: {},
                openMusic: {},
                openSound: {},
                birthId: { rules: [{ required: true, message: "请输入出身id!" }] },
                level: { rules: [{ required: true, message: "请输入境界等级!" }] },
                realm: {},
                skipCartoon: {},
                backpackSize: {},
                backpackLevel: {},
                practiceValue: { rules: [{ required: true, message: "请输入修为值!" }] },
                practiceYear: { rules: [{ required: true, message: "请输入修炼年数!" }] },
                combatPower: { rules: [{ required: true, message: "请输入战力!" }] },
                vipLevel: { rules: [{ required: true, message: "请输入vip等级!" }] },
                race: { rules: [{ required: true, message: "请输入种族id!" }] },
                practiceState: {},
                successRate: {},
                settleTime: {},
                loginIp: {},
                loginTime: {},
                logoutTime: {},
                levelUpdateTime: {},
                createTime: { rules: [{ required: true, message: "请输入创建时间!" }] },
                updateTime: {},
            },
            url: {
                add: "game/player/add",
                edit: "game/player/edit"
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
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "account", "nickname", "avatar", "sex", "openMusic", "openSound", "birthId", "level", "realm", "skipCartoon", "backpackSize", "backpackLevel", "practiceValue", "practiceYear", "combatPower", "vipLevel", "race", "practiceState", "successRate", "settleTime", "loginIp", "loginTime", "logoutTime", "levelUpdateTime", "createTime", "updateTime"));
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
            this.form.setFieldsValue(pick(row, "account", "nickname", "avatar", "sex", "openMusic", "openSound", "birthId", "level", "realm", "skipCartoon", "backpackSize", "backpackLevel", "practiceValue", "practiceYear", "combatPower", "vipLevel", "race", "practiceState", "successRate", "settleTime", "loginIp", "loginTime", "logoutTime", "levelUpdateTime", "createTime", "updateTime"));
        },
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