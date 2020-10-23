<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- <a-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"> -->
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="生命" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['hp', validatorRules.hp]" placeholder="生命"></a-input>
                </a-form-item>
                <a-form-item label="防御" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['def', validatorRules.def]" placeholder="请输入角色昵称"></a-input>
                </a-form-item>
                <a-form-item label="闪避" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-decorator="['dodge', validatorRules.dodge]" placeholder="请输入角色头像"></a-input>
                </a-form-item>
                <a-form-item label="命中" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['hit', validatorRules.hit]" placeholder="请输入性别" style="width: 100%"/>
                </a-form-item>
                <a-form-item label="速度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['speed', validatorRules.speed]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="暴击" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['crit', validatorRules.crit]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="暴抗" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['critDef', validatorRules.critDef]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="暴击率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['critPct', validatorRules.critPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="命中率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['hitPct', validatorRules.hitPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="闪避率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['dodgePct', validatorRules.dodgePct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="暴抗率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['critDefPct', validatorRules.critDefPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="破军率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['breakAttPct', validatorRules.breakAttPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="破军免伤率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['breakDrPct', validatorRules.breakDrPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>

                <a-form-item label="卓越率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['excelAttPct', validatorRules.excelAttPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="卓越抵抗" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['excelDelPct', validatorRules.excelDelPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="卓越伤害" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['excelDmgPct', validatorRules.excelDmgPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="卓越免伤" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['excelDrPct', validatorRules.excelDrPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>

                <a-form-item label="会心率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['insightAttPct', validatorRules.insightAttPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="会心抵抗" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['insightDefPct', validatorRules.insightDefPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="会心伤害" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['insightDmgPct', validatorRules.insightDmgPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>
                <a-form-item label="会心免伤" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number v-decorator="['insightDrPct', validatorRules.insightDrPct]" placeholder="请输入性别"
                                    style="width: 100%"/>
                </a-form-item>

            </a-form>
        </a-spin>
        <!-- </a-modal> -->
        <!--<a-button type="primary" @click="handleOk">确定</a-button>-->
        <a-button type="primary" @click="handleCancel">取消</a-button>
    </a-drawer>
</template>

<script>
    import {httpAction} from "@/api/manage";
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
                    xs: {span: 24},
                    sm: {span: 5}
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16}
                },
                confirmLoading: false,
                validatorRules: { //dodge hit speed crit critDef
                    hp: {},
                    def: {},
                    dodge: {},
                    hit: {},
                    speed: {},
                    crit: {},//critPct hitPct dodgePct critDefPct breakAttPct breakDrPct
                    critDef: {},
                    critPct: {},
                    hitPct: {},
                    dodgePct: {},
                    critDefPct: {},
                    breakAttPct: {},
                    breakDrPct: {},//excelAttPct excelDelPct excelDmgPct excelDrPct

                    excelAttPct: {},
                    excelDelPct: {},
                    excelDmgPct: {},
                    excelDrPct: {},

                    insightAttPct: {},//insightAttPct insightDefPct insightDmgPct insightDrPct
                    insightDefPct: {},
                    insightDmgPct: {},
                    insightDrPct: {}
                },
                url: {
                    detail: "game/player/detail"
                }
            };
        },
        created() {

        },
        methods: {
            /*add() {
                this.edit({});
            },
            edit(record) {
                this.loadData(record.playerId)
            },*/
            close() {
                this.$emit("close");
                this.visible = false;
            },
            /*handleOk() {
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
            },*/
            handleCancel() {
                this.close();
            },
            popupCallback(row) {
                this.form.setFieldsValue(pick(row, "hp", "def", "dodge", "hit", "speed", "crit", "critDef", "critPct", "hitPct", "dodgePct", "critDefPct", "breakAttPct", "breakDrPct",
                    "excelAttPct", "excelDelPct", "excelDmgPct", "excelDrPct", "insightAttPct", "insightDefPct", "insightDmgPct", "insightDrPct"));
            },
            loadData: function (playerId) {
                getAction()
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
