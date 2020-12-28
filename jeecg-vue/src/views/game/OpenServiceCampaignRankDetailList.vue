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
                    <img v-else :src="getImgView(text)" alt="图片不存在" class="image" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>
                <template slot="largeText" slot-scope="text">
                    <div class="largeTextContainer">
                        <span class="largeText">{{ text }}</span>
                    </div>
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

        <open-service-campaign-rank-detail-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-rank-detail-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "../../api/manage";
import { filterObj } from "@/utils/util";
import OpenServiceCampaignRankDetailModal from "./modules/OpenServiceCampaignRankDetailModal";

export default {
    name: "OpenServiceCampaignRankDetailList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignRankDetailModal
    },
    data() {
        return {
            description: "开服活动-开服排行-活动明细(3级)管理页面",
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
                {
                    title: "活动名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "页签名称",
                    align: "center",
                    dataIndex: "tabName"
                },
                {
                    title: "排序",
                    align: "center",
                    dataIndex: "sort",
                    width: 80
                },
                {
                    title: "排行类型",
                    align: "center",
                    dataIndex: "rankType",
                    customRender: value => {
                        let text = "--";
                        if (value === 1) {
                            text = "1-境界冲榜";
                        } else if (value === 2) {
                            text = "2-功法冲榜";
                        }
                        return text;
                    }
                },
                {
                    title: "开始时间",
                    align: "center",
                    dataIndex: "startDay"
                },
                {
                    title: "持续时间(天)",
                    align: "center",
                    dataIndex: "duration"
                },
                {
                    title: "活动宣传图",
                    align: "center",
                    dataIndex: "banner",
                    width: 160,
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "活动宣传奖励图",
                    align: "center",
                    dataIndex: "rewardImg",
                    width: 50,
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "活动宣传仙力",
                    align: "center",
                    dataIndex: "combatPower"
                },
                // {
                //     title: "排行玩家数量",
                //     align: "center",
                //     dataIndex: "rankNum"
                // },
                {
                    title: "排名奖励邮件id",
                    align: "center",
                    dataIndex: "rankRewardEmail"
                },
                {
                    title: "达标奖励邮件id",
                    align: "center",
                    dataIndex: "standardRewardEmail"
                },
                {
                    title: "帮助信息",
                    align: "center",
                    width: 180,
                    dataIndex: "helpMsg",
                    scopedSlots: { customRender: "largeText" }
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
                list: "game/openServiceCampaignRankDetail/list",
                delete: "game/openServiceCampaignRankDetail/delete",
                deleteBatch: "game/openServiceCampaignRankDetail/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignRankDetail/exportXls",
                importExcelUrl: "game/openServiceCampaignRankDetail/importExcel"
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
            this.$refs.modalForm.add({ campaignTypeId: this.model.id, campaignId: this.model.campaignId });
            this.$refs.modalForm.title = "新增开服排行配置";
        },
        getQueryParams() {
            var param = Object.assign({}, this.queryParam);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // typeId、活动id
            param.campaignId = this.model.campaignId;
            param.campaignTypeId = this.model.id;
            return filterObj(param);
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return `${window._CONFIG["domainURL"]}/${text}`;
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";

.image {
    width: 100%;
    height: 100px;
    object-fit: scale-down;
}

.largeTextContainer {
    display: flex;
    overflow-x: hidden;
    overflow-y: auto;
    max-height: 200px;
}

.largeText {
    white-space: normal;
    word-break: break-word;
}
</style>
