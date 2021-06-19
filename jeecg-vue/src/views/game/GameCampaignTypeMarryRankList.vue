<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('节日活动-结义排行榜')">导出</a-button>
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
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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

        <gameCampaignTypeMarryRank-modal ref="modalForm" @ok="modalFormOk"></gameCampaignTypeMarryRank-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameCampaignTypeMarryRankModal from "./modules/GameCampaignTypeMarryRankModal";

export default {
    name: "GameCampaignTypeMarryRankList",
    mixins: [JeecgListMixin],
    components: {
        GameCampaignTypeMarryRankModal
    },
    data() {
        return {
            description: "节日活动-结义排行榜管理页面",
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
                    title: "campaign.id, 活动id",
                    align: "center",
                    dataIndex: "campaignId"
                },
                {
                    title: "game_campaign_type.id",
                    align: "center",
                    dataIndex: "typeId"
                },
                {
                    title: "大奖展示",
                    align: "center",
                    dataIndex: "bigReward"
                },
                {
                    title: "大奖战力",
                    align: "center",
                    dataIndex: "bigRewardFight"
                },
                {
                    title: "排行玩家数量(上榜人数限制)",
                    align: "center",
                    dataIndex: "rankNum"
                },
                {
                    title: "排名奖励邮件id",
                    align: "center",
                    dataIndex: "rankRewardEmail"
                },
                {
                    title: "号召赠酒传闻id",
                    align: "center",
                    dataIndex: "callOnMessage"
                },
                {
                    title: "帮助信息",
                    align: "center",
                    dataIndex: "helpMsg"
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "创建人",
                    align: "center",
                    dataIndex: "createBy"
                },
                {
                    title: "更新时间",
                    align: "center",
                    dataIndex: "updateTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "修改人",
                    align: "center",
                    dataIndex: "updateBy"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameCampaignTypeMarryRank/list",
                delete: "game/gameCampaignTypeMarryRank/delete",
                deleteBatch: "game/gameCampaignTypeMarryRank/deleteBatch",
                exportXlsUrl: "game/gameCampaignTypeMarryRank/exportXls",
                importExcelUrl: "game/gameCampaignTypeMarryRank/importExcel"
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