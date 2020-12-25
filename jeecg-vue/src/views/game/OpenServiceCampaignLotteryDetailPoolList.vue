<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper"></div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <!-- <a-button type="primary" icon="download" @click="handleExportXls('开服夺宝奖池')">导出</a-button> -->
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

        <open-service-campaign-lottery-detail-pool-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-lottery-detail-pool-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "../../api/manage";
import { filterObj } from "@/utils/util";
import OpenServiceCampaignLotteryDetailPoolModal from "./modules/OpenServiceCampaignLotteryDetailPoolModal";

export default {
    name: "OpenServiceCampaignLotteryDetailPoolList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignLotteryDetailPoolModal
    },
    data() {
        return {
            description: "开服夺宝奖池管理页面",
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
                //     title: "页签详情id",
                //     align: "center",
                //     dataIndex: "lotteryDetailId"
                // },
                {
                    title: "奖池id",
                    align: "center",
                    dataIndex: "poolId"
                },
                {
                    title: "奖励列表",
                    align: "center",
                    dataIndex: "reward"
                },
                {
                    title: "掉落权重",
                    align: "center",
                    dataIndex: "weight"
                },
                {
                    title: "是否记录",
                    align: "center",
                    dataIndex: "record",
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
                    title: "是否传闻",
                    align: "center",
                    dataIndex: "message",
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
                    title: "是否大奖弹窗",
                    align: "center",
                    dataIndex: "showReward",
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
                list: "game/openServiceCampaignLotteryDetailPool/list",
                delete: "game/openServiceCampaignLotteryDetailPool/delete",
                deleteBatch: "game/openServiceCampaignLotteryDetailPool/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignLotteryDetailPool/exportXls",
                importExcelUrl: "game/openServiceCampaignLotteryDetailPool/importExcel"
            },
            dictOptions: {}
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
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
            this.loadData();
        },
        handleAdd() {
            this.$refs.modalForm.add({
                lotteryDetailId: this.model.id,
                campaignTypeId: this.model.campaignTypeId,
                campaignId: this.model.campaignId
            });
            this.$refs.modalForm.title = "新增消耗配置";
        },
        getQueryParams() {
            var param = Object.assign({}, this.queryParam);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // typeId、活动id、页签详情id
            param.campaignId = this.model.campaignId;
            param.campaignTypeId = this.model.campaignTypeId;
            param.lotteryDetailId = this.model.id;
            return filterObj(param);
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
