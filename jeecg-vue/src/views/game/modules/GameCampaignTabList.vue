<template>
    <a-card :bordered="false">
        <a-modal :title="title" :width="1200" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
            <a-tabs :activeKey="tabIndex" @change="handleTabChange">
                <!-- 查询区域 -->
                <a-tab-pane v-for="(row, index) in model.typeList" :key="index" :tab="row.name">
                    <div class="table-page-search-wrapper">
                        <a-form :form="form">
                            <a-form-item label="页签id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input :disabled="isEdit" v-decorator="['id', validatorRules.id]" placeholder="请输入页签id"></a-input>
                            </a-form-item>
                            <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-select :disabled="isEdit" placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                                    <a-select-option :value="1">1-登录礼包</a-select-option>
                                    <a-select-option :value="2">2-累计充值</a-select-option>
                                    <a-select-option :value="3">3-兑换</a-select-option>
                                    <a-select-option :value="4">4-节日任务</a-select-option>
                                    <a-select-option :value="5">5-Buff-修为加成</a-select-option>
                                    <a-select-option :value="6">6-Buff-灵气加成</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item label="页签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入页签名称"></a-input>
                            </a-form-item>
                            <a-form-item label="活动图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <img
                                    v-if="tabModel.typeImage"
                                    :src="getImgView(tabModel.typeImage)"
                                    height="100px"
                                    :alt="getImgView(tabModel.typeImage)"
                                    style="max-width:100%;font-size: 12px;font-style: italic;"
                                />
                                <game-image-selector placeholder="活动图片" v-model="tabModel.typeImage" />
                            </a-form-item>
                            <a-form-item label="活动时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-col :md="7" :sm="8">
                                    <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" />
                                </a-col>
                                <a-col :md="7" :sm="8">
                                    <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" />
                                </a-col>
                            </a-form-item>
                            <a-form-item label="页签顺序" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input-number class="input-number" v-decorator="['sort', validatorRules.sort]" placeholder="请输入页签顺序"></a-input-number>
                            </a-form-item>
                            <a-form-item v-show="tabModel.type == 5 || tabModel.type == 6" label="加成效率" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input-number class="input-number" v-decorator="['addition', validatorRules.addition]" placeholder="请输入加成效率"></a-input-number>
                            </a-form-item>
                            <a-form-item v-show="tabModel.type == 5 || tabModel.type == 6" label="buff描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input v-decorator="['buffDesc', validatorRules.buffDesc]" placeholder="请输入buff描述"></a-input>
                            </a-form-item>
                            <div class="config-card">
                                <!-- 1-登录礼包 -->
                                <div v-if="tabModel.type == 1">
                                    <a-card v-for="(item, index) in detailList" :key="index">
                                        <a-row type="flex" justify="space-around" align="middle">
                                            <a-col span="22">
                                                <a-row class="ant-row">
                                                    {{ "配置：" + (index + 1) }}
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        登录天数
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.loginDay" placeholder="请输入登录天数" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        描述
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input v-model="item.description" placeholder="请输入描述" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        奖励列表
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-textarea v-model="item.reward" placeholder="请输入奖励列表" :auto-size="{ minRows: 2, maxRows: 5 }" />
                                                    </a-col>
                                                </a-row>
                                            </a-col>
                                            <a-col span="2">
                                                <a-button class="del-btn" @click="delRowCustom(index)" icon="minus" type="danger">删除</a-button>
                                            </a-col>
                                        </a-row>
                                    </a-card>
                                </div>
                                <!-- 2-累计充值 -->
                                <div v-else-if="tabModel.type == 2">
                                    <a-card v-for="(item, index) in detailList" :key="index">
                                        <a-row type="flex" justify="space-around" align="middle">
                                            <a-col span="22">
                                                <a-row class="ant-row">
                                                    {{ "配置：" + (index + 1) }}
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        礼包id
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.rechargeId" placeholder="请输入礼包id" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        累计充值额度
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.rechargeAmount" placeholder="请输入累计充值额度" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        奖励列表
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-textarea v-model="item.reward" placeholder="请输入奖励列表" :auto-size="{ minRows: 2, maxRows: 5 }" />
                                                    </a-col>
                                                </a-row>
                                            </a-col>
                                            <a-col span="2">
                                                <a-button class="del-btn" @click="delRowCustom(index)" icon="minus" type="danger">删除</a-button>
                                            </a-col>
                                        </a-row>
                                    </a-card>
                                </div>
                                <!-- 3-兑换 -->
                                <div v-else-if="tabModel.type == 3">
                                    <a-card v-for="(item, index) in detailList" :key="index">
                                        <a-row type="flex" justify="space-around" align="middle">
                                            <a-col span="22">
                                                <a-row class="ant-row">
                                                    {{ "配置：" + (index + 1) }}
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        兑换id
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.exchangeId" placeholder="请输入兑换id" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        道具名称
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input v-model="item.itemName" placeholder="请输入道具名称" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        最大兑换数量
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.maxExchangeNum" placeholder="请输入最大兑换数量" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        奖励列表
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-textarea v-model="item.reward" placeholder="请输入奖励列表" :auto-size="{ minRows: 2, maxRows: 5 }" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        消耗列表
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-textarea v-model="item.consume" placeholder="请输入消耗列表" :auto-size="{ minRows: 2, maxRows: 5 }" />
                                                    </a-col>
                                                </a-row>
                                            </a-col>
                                            <a-col span="2">
                                                <a-button class="del-btn" @click="delRowCustom(index)" icon="minus" type="danger">删除</a-button>
                                            </a-col>
                                        </a-row>
                                    </a-card>
                                </div>
                                <!-- 4-节日任务 -->
                                <div v-else-if="tabModel.type == 4">
                                    <a-card v-for="(item, index) in detailList" :key="index">
                                        <a-row type="flex" justify="space-around" align="middle">
                                            <a-col span="22">
                                                <a-row class="ant-row">
                                                    {{ "配置：" + (index + 1) }}
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        任务id（task_id）
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.taskId" placeholder="请输入任务id" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        任务描述
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input v-model="item.description" placeholder="请输入任务描述" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        模块id （module_id）
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.moduleId" placeholder="请输入模块id(task_module_type.module_id)" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        完成条件（target）
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.target" placeholder="请输入任务完成条件" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        任务参数（args）
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.args" placeholder="请输入任务参数" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        奖励列表
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-textarea v-model="item.reward" placeholder="请输入奖励列表" :auto-size="{ minRows: 2, maxRows: 5 }" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        跳转id（jump_id）
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input-number class="input-number" v-model="item.jumpId" placeholder="请输入跳转id" />
                                                    </a-col>
                                                </a-row>
                                            </a-col>
                                            <a-col span="2">
                                                <a-button class="del-btn" @click="delRowCustom(index)" icon="minus" type="danger">删除</a-button>
                                            </a-col>
                                        </a-row>
                                    </a-card>
                                </div>
                                <!-- 5-Buff-修为加成 、6-Buff-灵气加成-->
                                <div v-else-if="tabModel.type == 5 || tabModel.type == 6">
                                    <a-card v-for="(item, index) in detailList" :key="index">
                                        <a-row type="flex" justify="space-around" align="middle">
                                            <a-col span="22">
                                                <a-row class="ant-row">
                                                    {{ "配置：" + (index + 1) }}
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        加成开始时间
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input v-model="item.startTime" placeholder="请输入加成开始时间" />
                                                    </a-col>
                                                </a-row>
                                                <a-row type="flex" align="middle" class="ant-row">
                                                    <a-col :span="4">
                                                        加成结束时间
                                                    </a-col>
                                                    <a-col :span="18">
                                                        <a-input v-model="item.endTime" placeholder="请输入加成结束时间" />
                                                    </a-col>
                                                </a-row>
                                            </a-col>
                                            <a-col span="2">
                                                <a-button class="del-btn" @click="delRowCustom(index)" icon="minus" type="danger">删除</a-button>
                                            </a-col>
                                        </a-row>
                                    </a-card>
                                </div>
                                <a-button class="add-btn" type="dashed" icon="plus" @click="addRowCustom">添加</a-button>
                                <a-button class="add-btn" type="primary" icon="save" @click="saveTab">保存当前页签</a-button>
                            </div>
                        </a-form>
                    </div>
                </a-tab-pane>
            </a-tabs>
        </a-modal>
    </a-card>
</template>

<script>
import { getAction, putAction } from "@/api/manage";
import GameImageSelector from "../components/GameImageSelector";
import { filterObj } from "@/utils/util";
import pick from "lodash.pick";
import moment from "moment";

export default {
    name: "GameCampaignTabList",
    components: { GameImageSelector },
    data() {
        return {
            description: "页签配置",
            // 查询参数
            queryParam: {},
            title: "操作",
            visible: false,
            isEdit: false,
            confirmLoading: false,
            model: {},
            tabModel: {},
            detailList: [],
            campaignId: "",
            // 页签信息
            tabIndex: 0,
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            form: this.$form.createForm(this),
            validatorRules: {
                campaignId: { rules: [{ required: true, message: "请输入活动id!" }] },
                type: { rules: [{ required: true, message: "请输入活动项类型" }] },
                typeImage: { rules: [{ required: true, message: "请输入活动类型图片!" }] },
                sort: { rules: [{ required: true, message: "请输入排序!" }] },
                startTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] },
                buffDesc: { rules: [{ required: false, message: "请输入buff描述!" }] },
                addition: { rules: [{ required: false, message: "请输入buff加成!" }] }
            },
            url: {
                saveTab: "game/gameCampaignType/edit",
                queryTab: "game/gameCampaignType/queryById"
            }
        };
    },
    computed: {},
    created() {},
    methods: {
        edit(record) {
            if (record.id) {
                this.campaignId = record.id;
            }
            this.queryParam = {};
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.model.campaignId = this.campaignId;
            this.isEdit = this.model.id != null;
            this.tabIndex = 0;
            this.visible = true;
            this.$nextTick(() => {
                this.pickFormValues();
            });
        },
        close() {
            this.$emit("close");
            this.visible = false;
            this.form.resetFields();
            this.dataSource = [];
        },
        getQueryParams() {
            var param = Object.assign({}, this.queryParam);
            param.campaignId = this.campaignId;
            if (this.model && this.model.typeList) {
                this.tabModel = this.model.typeList[this.tabIndex];
                param.typeId = this.tabModel.id;
            }
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            return filterObj(param);
        },
        handleCancel() {
            this.removeEmptyItems();
            this.close();
        },
        handleOk() {
            this.removeEmptyItems();
            // 保存所有页签
            this.close();
        },
        handleTabChange(tab) {
            this.tabIndex = tab;
            this.pickFormValues();
        },
        pickFormValues() {
            this.tabModel = this.model.typeList[this.tabIndex];
            this.detailList = this.tabModel.details;
            this.form.setFieldsValue(pick(this.tabModel, "id", "campaignId", "name", "type", "typeImage", "sort", "startTime", "endTime", "addition", "buffDesc"));
            // 时间格式化
            this.form.setFieldsValue({ startTime: this.tabModel.startTime ? moment(this.tabModel.startTime) : null });
            this.form.setFieldsValue({ endTime: this.tabModel.endTime ? moment(this.tabModel.endTime) : null });
        },
        addRowCustom() {
            this.detailList.push({});
            this.$forceUpdate();
        },
        delRowCustom(index) {
            console.log(index);
            this.detailList.splice(index, 1);
            this.$forceUpdate();
        },
        removeEmptyItems() {
            for (let index = 0; index < this.model.typeList.length; index++) {
                const element = this.model.typeList[index];
                if (element.details == null) {
                    element.details = [];
                } else {
                    var newArray = element.details.filter(value => Object.keys(value).length !== 0);
                    element.details = newArray;
                }
            }
        },
        refreshTabData() {
            const that = this;
            that.confirmLoading = true;
            getAction(this.url.queryTab, { id: that.tabModel.id })
                .then(res => {
                    if (res.success) {
                        that.model.typeList[that.tabIndex] = res.result;
                        that.tabModel = res.result;
                    }
                })
                .finally(() => {
                    that.confirmLoading = false;
                    that.pickFormValues();
                });
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return window._CONFIG["domainURL"] + text;
        },
        saveTab() {
            console.log("saveTab:" + this.tabIndex);
            this.removeEmptyItems();
            const that = this;

            if (that.tabModel.type == 5 || that.tabModel.type == 6) {
                let addition = that.form.getFieldValue("addition");
                let buffDesc = that.form.getFieldValue("buffDesc");

                if (that.tabModel.details != null && that.tabModel.details.length > 0) {
                    if (!addition) {
                        that.$message.warning("请输入Buff加成！");
                        return;
                    }

                    if (!buffDesc) {
                        that.$message.warning("请输入Buff描述！");
                        return;
                    }
                }
            }

            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    that.confirmLoading = true;
                    let formData = Object.assign(that.tabModel, values);
                    // 创建时间参数不传递后台
                    delete formData.createTime;

                    // 使用 json 包装数组，避免 springboot 无法直接映射到对象
                    formData.detailsData = JSON.stringify(formData.details != null ? formData.details : []);
                    delete formData.details;

                    // 时间格式化
                    formData.startTime = formData.startTime ? formData.startTime.format("YYYY-MM-DD HH:mm:ss") : null;
                    formData.endTime = formData.endTime ? formData.endTime.format("YYYY-MM-DD HH:mm:ss") : null;

                    putAction(that.url.saveTab, formData)
                        .then(res => {
                            if (res.success) {
                                that.$message.success(res.message);
                                that.$emit("ok");
                            } else {
                                that.$message.warning(res.message);
                            }
                            that.loading = false;
                        })
                        .finally(() => {
                            that.confirmLoading = false;
                            that.refreshTabData();
                        });
                }
            });
        }
    }
};
</script>

<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
}

.ant-row {
    margin: 10px 0;
}

.del-btn {
    height: 50px;
    margin: 0px 10px 0px -10px;
}

.input-number {
    width: 100%;
}

.config-card {
    margin: auto;
    width: 80%;
    // border: 1px solid green;
    padding: 5px;
}

.add-btn {
    margin: auto;
    width: 100%;
    margin: 20px 0;
}
</style>
