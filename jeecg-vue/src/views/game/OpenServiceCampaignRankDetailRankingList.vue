<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="开服活动id">
                            <a-input placeholder="请输入开服活动id" v-model="queryParam.campaignId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="页签id">
                            <a-input placeholder="请输入页签id" v-model="queryParam.campaignTypeId"></a-input>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="open_service_campaign_rank_detail.id">
                                <a-input placeholder="请输入open_service_campaign_rank_detail.id" v-model="queryParam.rankDetailId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="排名最小值">
                                <a-input placeholder="请输入排名最小值" v-model="queryParam.minRank"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="排名最大值">
                                <a-input placeholder="请输入排名最大值" v-model="queryParam.maxRank"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="上榜最低积分">
                                <a-input placeholder="请输入上榜最低积分" v-model="queryParam.score"></a-input>
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="6" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
                            <a style="margin-left: 8px" @click="handleToggleSearch">
                                {{ toggleSearchStatus ? "收起" : "展开" }}
                                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                            </a>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('开服活动-开服排行-活动明细-排行上榜、奖励')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown>
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
                :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"
            >
                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;" />
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

        <openServiceCampaignRankDetailRanking-modal ref="modalForm" @ok="modalFormOk"></openServiceCampaignRankDetailRanking-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import OpenServiceCampaignRankDetailRankingModal from "./modules/OpenServiceCampaignRankDetailRankingModal";

export default {
    name: "OpenServiceCampaignRankDetailRankingList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignRankDetailRankingModal
    },
    data() {
        return {
            description: "开服活动-开服排行-活动明细-排行上榜、奖励管理页面",
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
                    title: "页签id",
                    align: "center",
                    dataIndex: "campaignTypeId"
                },
                {
                    title: "open_service_campaign_rank_detail.id",
                    align: "center",
                    dataIndex: "rankDetailId"
                },
                {
                    title: "排名最小值",
                    align: "center",
                    dataIndex: "minRank"
                },
                {
                    title: "排名最大值",
                    align: "center",
                    dataIndex: "maxRank"
                },
                {
                    title: "上榜最低积分",
                    align: "center",
                    dataIndex: "score"
                },
                {
                    title: "奖励列表",
                    align: "center",
                    dataIndex: "reward"
                },
                {
                    title: "稀有奖励列表",
                    align: "center",
                    dataIndex: "rareReward"
                },
                {
                    title: "传闻内容",
                    align: "center",
                    dataIndex: "message"
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime",
                },
                {
                    title: "更新时间",
                    align: "center",
                    dataIndex: "updateTime",
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/openServiceCampaignRankDetailRanking/list",
                delete: "game/openServiceCampaignRankDetailRanking/delete",
                deleteBatch: "game/openServiceCampaignRankDetailRanking/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignRankDetailRanking/exportXls",
                importExcelUrl: "game/openServiceCampaignRankDetailRanking/importExcel"
            },
            dictOptions: {
            }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
