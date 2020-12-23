<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper"></div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
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
                    <img v-else :src="getImgView(text)" height="100px" alt="图片不存在" style="max-width:280px;font-size: 12px;font-style: italic;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <!-- <a @click="handleEditDetail(record)">活动配置</a> -->
                    <!-- <a-divider type="vertical" /> -->
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

        <open-service-campaign-type-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-type-modal>

        <!-- 不同的类型开服活动对应不同的弹窗 -->
        <open-service-campaign-gift-detail-list-modal ref="giftDetailListModal"></open-service-campaign-gift-detail-list-modal>
        <open-service-campaign-rank-detail-list-modal ref="rankDetailListModal"></open-service-campaign-rank-detail-list-modal>
        <open-service-campaign-single-gift-detail-list-modal ref="singleGiftDetailListModal"></open-service-campaign-single-gift-detail-list-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction, httpAction } from "../../api/manage";
import { filterObj } from "@/utils/util";
import OpenServiceCampaignTypeModal from "./modules/OpenServiceCampaignTypeModal";
import OpenServiceCampaignRankDetailListModal from "./modules/OpenServiceCampaignRankDetailListModal";
import OpenServiceCampaignGiftDetailListModal from "./modules/OpenServiceCampaignGiftDetailListModal";
import OpenServiceCampaignSingleGiftDetailListModal from "./modules/OpenServiceCampaignSingleGiftDetailListModal";

export default {
    name: "OpenServiceCampaignTypeList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignTypeModal,
        OpenServiceCampaignGiftDetailListModal,
        OpenServiceCampaignRankDetailListModal,
        OpenServiceCampaignSingleGiftDetailListModal
    },
    data() {
        return {
            description: "开服活动-类型(2级)管理页面",
            model: {},
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
                    title: "开服活动id",
                    align: "center",
                    dataIndex: "campaignId"
                },
                {
                    title: "开服活动类型",
                    align: "center",
                    dataIndex: "type",
                    // <!-- 1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗 -->
                    customRender: value => {
                        let text = "--";
                        if (value === 1) {
                            text = "1-开服排行";
                        } else if (value === 2) {
                            text = "2-开服礼包";
                        } else if (value === 3) {
                            text = "3-单笔充值";
                        } else if (value === 4) {
                            text = "4-寻宝";
                        } else if (value === 5) {
                            text = "5-道具消耗";
                        }
                        return text;
                    }
                },
                {
                    title: "排序",
                    align: "center",
                    dataIndex: "sort"
                },
                {
                    title: "活动备注",
                    align: "center",
                    dataIndex: "remark"
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime"
                },
                // {
                //     title: "更新时间",
                //     align: "center",
                //     dataIndex: "updateTime"
                // },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/openServiceCampaignType/list",
                delete: "game/openServiceCampaignType/delete",
                deleteBatch: "game/openServiceCampaignType/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignType/exportXls",
                importExcelUrl: "game/openServiceCampaignType/importExcel"
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
                campaignId: this.model.id
            });
            this.$refs.modalForm.title = "新增礼包配置";
        },
        handleEditDetail(record) {
            if (record.type === 1) {
                // 1-开服排行
                this.$refs.rankDetailListModal.title = "开服排行配置";
                this.$refs.rankDetailListModal.visible = true;
                this.$refs.rankDetailListModal.edit(record);
            } else if (record.type === 2) {
                // 2-开服礼包
                this.$refs.giftDetailListModal.title = "开服礼包配置";
                this.$refs.giftDetailListModal.visible = true;
                this.$refs.giftDetailListModal.edit(record);
            } else if (record.type === 3) {
                // 3-单笔充值
                this.$refs.singleGiftDetailListModal.title = "单笔充值配置";
                this.$refs.singleGiftDetailListModal.visible = true;
                this.$refs.singleGiftDetailListModal.edit(record);
            } else if (record.type === 4) {
                // 4-寻宝
            } else if (record.type === 5) {
                // 5-道具消耗
            }
        },
        getQueryParams() {
            var param = Object.assign({}, this.queryParam);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // typeId、活动id、页签详情id
            param.campaignId = this.model.id;
            return filterObj(param);
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
