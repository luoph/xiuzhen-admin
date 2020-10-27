<template>
    <a-modal :title="title" :width="1400" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-tabs :defaultActiveKey="1" @change="handleTabChange">
                <!-- <a-tab-pane v-for="(row, index) in typeList" :key="index" :tab="row.name">
                    <a-table ref="table" size="middle" bordered :columns="columns" :dataSource="data" rowKey="id" :pagination="false">
                        <span slot="action" slot-scope="text, record">
                            <a @click="handleEdit(record)">编辑</a>
                        </span>
                    </a-table>
                </a-tab-pane> -->

                <a-tab-pane tab="活动信息" key="1">
                    <a-form-item label="活动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-select placeholder="选择活动类型" v-decorator="['type', validatorRules.type]" initialValue="1">
                            <a-select-option :value="1">1-节日活动</a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item label="活动名称（备注）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入活动名称（备注）"></a-input>
                    </a-form-item>
                    <a-form-item label="区服ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <game-server-selector v-decorator="['serverIds', { initialValue: '' }]" @changeSelect="changeSelect" />
                    </a-form-item>
                    <a-form-item label="活动时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-col :md="7" :sm="8">
                            <a-date-picker placeholder="开始时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['startTime', validatorRules.startTime]" />
                        </a-col>
                        <a-col :md="7" :sm="8">
                            <a-date-picker placeholder="结束时间" showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="['endTime', validatorRules.endTime]" />
                        </a-col>
                    </a-form-item>
                    <a-form-item label="自动开启" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onAutoOpenChose" v-model="isAutoOpen" />
                    </a-form-item>
                </a-tab-pane>
            </a-tabs>
        </a-spin>
    </a-modal>
</template>

<script>
import JDate from "@/components/jeecg/JDate";

export default {
    name: "GameCampaignServerListModal",
    description: "活动状态",
    components: {
        JDate
    },
    data() {
        return {
            title: "操作",
            width: 800,
            visible: false,
            // table
            columns: [
                {
                    title: "服务器id",
                    dataIndex: "serverId",
                    key: "serverId",
                    width: "20%"
                },
                {
                    title: "服务器名",
                    dataIndex: "serverName",
                    key: "serverName",
                    width: "20%"
                },
                {
                    title: "开服时间",
                    dataIndex: "openTime",
                    key: "openTime",
                    width: "20%"
                },
                {
                    title: "状态",
                    dataIndex: "serverStatus",
                    key: "serverStatus",
                    width: "20%"
                },
                {
                    title: "活动状态",
                    dataIndex: "campaignStatus",
                    key: "campaignStatus",
                    width: "20%",
                    customRender: value => {
                        let re = "--";
                        if (value === 0) {
                            re = "关闭";
                        } else if (value === 1) {
                            re = "正常";
                        } else if (value === -1) {
                            re = "未开启";
                        }
                        return re;
                    }
                },
                {
                    title: "操作",
                    key: "action",
                    scopedSlots: { customRender: "action" }
                }
            ],
            data: [],
            tabIndex: 0,
            // 类型列表
            typeList: [
                { typeId: 1, name: "登录有礼" },
                { typeId: 2, name: "累计充值" },
                { typeId: 3, name: "兑换" },
                { typeId: 4, name: "节日任务" }
            ],
            // 服务列表
            serverList: [],
            // 样式布局
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
                type: { rules: [{ required: true, message: "请输入活动类型!" }] },
                name: { rules: [{ required: true, message: "请输入活动名称（备注）!" }] },
                description: { rules: [{ required: true, message: "请输入活动标语（描述）!" }] },
                showName: { rules: [{ required: true, message: "请输入活动展示名称!" }] },
                icon: { rules: [{ required: true, message: "请输入活动图标!" }] },
                banner: { rules: [{ required: true, message: "请输入活动宣传图!" }] },
                startTime: { rules: [{ required: true, message: "请输入开始时间!" }] },
                endTime: { rules: [{ required: true, message: "请输入结束时间!" }] }
            },
            url: {
                serverList: "game/gameCampaign/serverList"
            }
        };
    },
    methods: {
        edit(typeList) {
            this.typeList = typeList;
        },
        close() {
            this.$emit("close");
            this.visible = false;
        },
        handleOk() {
            const that = this;
            that.close();
        },
        handleCancel() {
            this.close();
        },
        handleTabChange(tab) {
            console.log(tab);
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
