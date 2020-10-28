<template>
    <a-card :bordered="false">
        <a-modal :title="title" :width="1200" :visible="visible" @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
            <a-tabs :defaultActiveKey="tabIndex" @change="handleTabChange">
                <!-- 查询区域 -->
                <a-tab-pane v-for="(row, index) in model.typeList" :key="index" :tab="row.name">
                    <div class="table-page-search-wrapper">
                        <a-form layout="inline" :form="form" @keyup.enter.native="searchQuery">
                            <a-row :gutter="10">
                                <a-col :md="10" :sm="8">
                                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="区服Id">
                                        <a-input placeholder="请输入区服Id" v-model="queryParam.serverId"></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :md="6" :sm="8">
                                    <span style="float: left;" class="table-page-search-submitButtons">
                                        <a-button type="primary" @click="searchQuery">查询</a-button>
                                        <a-button type="primary" @click="searchReset">重置</a-button>
                                    </span>
                                </a-col>
                            </a-row>
                        </a-form>
                    </div>

                    <!-- table区域-begin -->
                    <div>
                        <a-table
                            ref="table"
                            size="middle"
                            bordered
                            rowKey="id"
                            :columns="columns"
                            :dataSource="dataSource"
                            :pagination="ipagination"
                            :loading="loading"
                            @change="handleTableChange"
                        >
                            <span slot="action" slot-scope="text, record">
                                <a @click="handleEdit(record)">编辑</a>
                            </span>
                        </a-table>
                    </div>
                </a-tab-pane>
            </a-tabs>
        </a-modal>
    </a-card>
</template>

<script>
import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { filterObj } from "@/utils/util";
import pick from "lodash.pick";

function filterServerIdText(options, text) {
    if (options instanceof Array) {
        for (let server of options) {
            if (text === server.id) {
                return server.name + "(" + server.id + ")";
            }
        }
    }
    return text;
}

export default {
    name: "GameChannelServerList",
    mixins: [JeecgListMixin],
    components: {},
    data() {
        return {
            description: "活动状态",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "区服Id",
                    align: "center",
                    dataIndex: "serverId",
                    customRender: text => {
                        return filterServerIdText(this.serverList, text);
                    }
                },
                {
                    title: "渠道id",
                    align: "center",
                    dataIndex: "channelId"
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "delFlag_dictText"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            // 查询参数
            queryParam: {
                campaignId: ""
            },
            title: "操作",
            visible: false,
            model: {},

            campaignId: "",
            // 页签信息
            tabIndex: 0,
            serverList: [],
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            form: this.$form.createForm(this),
            url: {
                list: "game/gameChannelServer/list",
                serverListUrl: "game/gameServer/list"
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
            this.tabIndex = 0;
            this.model = Object.assign({}, record);
            this.model.campaignId = this.campaignId;
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "serverId"));
            });

            // 当其它模块调用该模块时,调用此方法加载字典数据
            this.loadData();
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
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            return filterObj(param);
        },
        handleCancel() {
            this.close();
        },
        handleOk() {
            this.close();
        },
        handleTabChange(tab) {
            console.log(tab + "-" + this.typeList[tab].name);
            this.loadData();
        },
        queryServerList() {
            // let that = this;
            // getAction(that.url.serverListUrl).then(res => {
            //     if (res.success) {
            //         if (res.result instanceof Array) {
            //             this.serverList = res.result;
            //         } else if (res.result.records instanceof Array) {
            //             this.serverList = res.result.records;
            //         }
            //     } else {
            //         this.serverList = [];
            //     }
            // });
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
</style>
