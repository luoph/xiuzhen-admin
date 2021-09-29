<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper"></div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('开服活动消耗传闻')">导出</a-button>
            <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown>
            <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据"></a-textarea>
            <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

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
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            >
                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
                    <img v-else :src="getImgView(text)" height="100px" alt="图片不存在" style="max-width:180px;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </span>
            </a-table>
        </div>

        <open-service-campaign-consume-detail-message-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-consume-detail-message-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction, postAction } from "../../api/manage";
import { filterObj } from "@/utils/util";
import OpenServiceCampaignConsumeDetailMessageModal from "./modules/OpenServiceCampaignConsumeDetailMessageModal";

export default {
    name: "OpenServiceCampaignConsumeDetailMessageList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignConsumeDetailMessageModal
    },
    data() {
        return {
            description: "开服活动消耗传闻管理页面",
            model: {},
            importText: "",
            // 表头
            columns: [
                // {
                //     title: "#",
                //     dataIndex: "",
                //     key: "rowIndex",
                //     width: 60,
                //     align: "center",
                //     customRender: function(t, r, index) {
                //         return parseInt(index) + 1;
                //     }
                // },
                {
                    title: "id",
                    align: "center",
                    width: 60,
                    dataIndex: "id"
                },
                // {
                //     title: "开服活动id",
                //     align: "center",
                //     dataIndex: "campaignId"
                // },
                // {
                //     title: "页签id",
                //     align: "center",
                //     dataIndex: "campaignTypeId"
                // },
                // {
                //     title: "详情id",
                //     align: "center",
                //     dataIndex: "consumeDetailId"
                // },
                {
                    title: "传闻推送时间",
                    align: "center",
                    dataIndex: "sendTime"
                },
                {
                    title: "传闻内容",
                    align: "center",
                    dataIndex: "message"
                },
                {
                    title: "传闻次数",
                    align: "center",
                    dataIndex: "num"
                },
                {
                    title: "是否发送邮件",
                    align: "center",
                    dataIndex: "email",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "否";
                        } else if (value === 1) {
                            text = "是";
                        }
                        return text;
                    }
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/openServiceCampaignConsumeDetailMessage/list",
                delete: "game/openServiceCampaignConsumeDetailMessage/delete",
                deleteBatch: "game/openServiceCampaignConsumeDetailMessage/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignConsumeDetailMessage/exportXls",
                importExcelUrl: "game/openServiceCampaignConsumeDetailMessage/importExcel",
                importTextUrl: "game/openServiceCampaignConsumeDetailMessage/importText"
            },
            dictOptions: {}
        };
    },
    computed: {},
    methods: {
        initDictConfig() {},
        loadData(arg) {
            if (!this.model.id) {
                return;
            }

            if (!this.url.list) {
                this.$message.error("请设置url.list属性!");
                return;
            }

            // 加载数据 若传入参数1则加载第一页的内容
            if (arg === 1) {
                this.ipagination.current = 1;
            }

            // 查询条件
            var params = this.getQueryParams();
            this.loading = true;
            getAction(this.url.list, params).then(res => {
                if (res.success && res.result && res.result.records) {
                    this.dataSource = res.result.records;
                    this.ipagination.total = res.result.total;
                }
                if (res.code === 510) {
                    this.$message.warning(res.message);
                }
                this.loading = false;
            });
        },
        edit(record) {
            this.model = record;
            this.importText = "";
            this.loadData();
        },
        handleAdd() {
            this.$refs.modalForm.add({
                consumeDetailId: this.model.id,
                campaignTypeId: this.model.campaignTypeId,
                campaignId: this.model.campaignId
            });
            this.$refs.modalForm.title = "新增传闻配置";
        },
        getQueryParams() {
            var param = Object.assign({}, this.queryParam);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // typeId、活动id、详情id
            param.campaignId = this.model.campaignId;
            param.campaignTypeId = this.model.campaignTypeId;
            param.consumeDetailId = this.model.id;
            return filterObj(param);
        },
        importExcelUrl() {
            let domainURL = window._CONFIG["domainURL"];
            return `${domainURL}/${this.url.importExcelUrl}`;
        },
        handleImportText() {
            let params = {
                id: this.model.id,
                text: this.importText
            };
            console.log(params);
            postAction(this.url.importTextUrl, params).then(res => {
                if (res.success) {
                    this.$message.success(res.message);
                    this.loadData();
                } else {
                    this.$message.warning(res.message);
                }
            });
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";

/** Button按钮间距 */
.ant-btn {
    margin-right: 15px;
}

.import-text {
    margin-top: 15px;
    margin-bottom: 15px;
}
</style>
