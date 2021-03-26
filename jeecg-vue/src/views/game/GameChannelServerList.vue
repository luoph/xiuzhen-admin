<template>
    <a-modal :title="title" :width="1000" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel" cancelText="关闭" okText="保存">
        <a-card :bordered="false">
            <!-- 抽屉 -->
            <!-- <a-drawer :title="title" :width="1000" placement="right" :closable="false" @close="close" :visible="visible"> -->
            <!-- 抽屉内容的border -->
            <div :style="{ padding: '20px', border: '1px solid #e9e9e9', background: '#fff' }">
                <!-- 查询区域 -->
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
                                    <a-button type="primary" @click="handleAdd">新增</a-button>
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
                            <a-divider type="vertical" />
                            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                <a>删除</a>
                            </a-popconfirm>
                        </span>
                        <span slot="statSlot" slot-scope="text, record">
                            <a-tag v-if="record.delFlag == 0" color="green">正常</a-tag>
                            <a-tag v-else-if="record.delFlag == 1" color="red">无效</a-tag>
                        </span>
                    </a-table>
                </div>
            </div>
            <!-- </a-drawer> -->
            <!-- table区域-end -->

            <!-- 表单区域 -->
            <game-channel-server-modal ref="modalForm" @ok="modalFormOk"></game-channel-server-modal>
        </a-card>
    </a-modal>
</template>

<script>
import GameChannelServerModal from "./modules/GameChannelServerModal";
import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { filterObj } from "@/utils/util";
import pick from "lodash.pick";

export default {
    name: "GameChannelServerList",
    mixins: [JeecgListMixin],
    components: {
        GameChannelServerModal
    },
    data() {
        return {
            description: "游戏渠道服配置管理页面",
            confirmLoading: false,
            isorter: {
                column: "position",
                order: "desc"
            },
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
                    title: "渠道id",
                    align: "center",
                    dataIndex: "channelId"
                },
                {
                    title: "位置权重",
                    align: "center",
                    dataIndex: "position"
                },
                {
                    title: "区服Id",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "区服名称",
                    align: "center",
                    dataIndex: "serverName"
                },
                {
                    title: "开服时间",
                    align: "center",
                    dataIndex: "openTime"
                },
                {
                    title: "上线时间",
                    align: "center",
                    dataIndex: "onlineTime"
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "delFlag",
                    scopedSlots: { customRender: "statSlot" }
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            queryParam: {
                channelId: "",
                serverId: "",
                delFlag: "1"
            },
            title: "操作",
            visible: false,
            model: {},
            channelId: "",
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
                delete: "game/gameChannelServer/delete",
                deleteBatch: "game/gameChannelServer/deleteBatch"
            }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    created() {},
    methods: {
        add(channelId) {
            this.channelId = channelId;
            this.edit({});
        },
        edit(record) {
            if (record.id) {
                this.channelId = record.id;
            }

            this.queryParam = {};
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.model.channelId = this.channelId;
            console.log("GameChannelServerList, model:", JSON.stringify(this.model));

            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, "serverId", "delFlag"));
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
            var param = Object.assign({}, this.queryParam, this.isorter, this.filters);
            param.channelId = this.channelId;
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            return filterObj(param);
        },
        handleAdd() {
            var position = 0;
            if (this.dataSource && this.dataSource.length > 0) {
                position = this.dataSource[0].position;
            }
            this.$refs.modalForm.edit({ channelId: this.channelId, delFlag: 0, position: position + 1 });
            this.$refs.modalForm.title = "新增";
        },
        handleCancel() {
            this.close();
        },
        handleOk() {
            this.close();
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
