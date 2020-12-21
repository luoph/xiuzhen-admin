<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                <a-form-item label="开服活动id, open_service_campaign.id">
                <a-input placeholder="请输入开服活动id, open_service_campaign.id" v-model="queryParam.campaignId"></a-input>
            </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                <a-form-item label="open_service_campaign_type.id">
                <a-input placeholder="请输入open_service_campaign_type.id" v-model="queryParam.campaignTypeId"></a-input>
            </a-form-item>
                </a-col>
                <template v-if="toggleSearchStatus">
                    <a-col :md="6" :sm="8">
                    <a-form-item label="活动名称">
                    <a-input placeholder="请输入活动名称" v-model="queryParam.name"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="活动页签名称">
                    <a-input placeholder="请输入活动页签名称" v-model="queryParam.tabName"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="排行类型 open_service_campaign_rank_type.rank_type e.g. 1.境界冲榜, 2.功法冲榜">
                    <a-input placeholder="请输入排行类型 open_service_campaign_rank_type.rank_type e.g. 1.境界冲榜, 2.功法冲榜" v-model="queryParam.rankType"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="开始时间(开服第n天, e.g. 0表示开服第1天)">
                    <a-input placeholder="请输入开始时间(开服第n天, e.g. 0表示开服第1天)" v-model="queryParam.startDay"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="持续时间(天)">
                    <a-input placeholder="请输入持续时间(天)" v-model="queryParam.duration"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="活动宣传背景图">
                    <a-input placeholder="请输入活动宣传背景图" v-model="queryParam.banner"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="活动宣传奖励图">
                    <a-input placeholder="请输入活动宣传奖励图" v-model="queryParam.rewardImg"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="活动宣传仙力">
                    <a-input placeholder="请输入活动宣传仙力" v-model="queryParam.combatPower"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="排行玩家数量">
                    <a-input placeholder="请输入排行玩家数量" v-model="queryParam.rankNum"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="排名奖励邮件id">
                    <a-input placeholder="请输入排名奖励邮件id" v-model="queryParam.rankRewardEmail"></a-input>
                </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                    <a-form-item label="达标奖励邮件id">
                    <a-input placeholder="请输入达标奖励邮件id" v-model="queryParam.standardRewardEmail"></a-input>
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
            <a-button type="primary" icon="download" @click="handleExportXls('开服活动-开服排行-活动明细(3级)')">导出</a-button>
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

        <gameOpenServiceCampaignRankDetail-modal ref="modalForm" @ok="modalFormOk"></gameOpenServiceCampaignRankDetail-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameOpenServiceCampaignRankDetailModal from "./modules/GameOpenServiceCampaignRankDetailModal";

export default {
    name: "GameOpenServiceCampaignRankDetailList",
    mixins: [JeecgListMixin],
    components: {
        GameOpenServiceCampaignRankDetailModal
    },
    data() {
        return {
            description: "开服活动-开服排行-活动明细(3级)管理页面",
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
                    title: "开服活动id, open_service_campaign.id",
                    align: "center",
                    dataIndex: "campaignId"
                },
                {
                    title: "open_service_campaign_type.id",
                    align: "center",
                    dataIndex: "campaignTypeId"
                },
                {
                    title: "活动名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "活动页签名称",
                    align: "center",
                    dataIndex: "tabName"
                },
                {
                    title: "排行类型 open_service_campaign_rank_type.rank_type e.g. 1.境界冲榜, 2.功法冲榜",
                    align: "center",
                    dataIndex: "rankType"
                },
                {
                    title: "开始时间(开服第n天, e.g. 0表示开服第1天)",
                    align: "center",
                    dataIndex: "startDay"
                },
                {
                    title: "持续时间(天)",
                    align: "center",
                    dataIndex: "duration"
                },
                {
                    title: "活动宣传背景图",
                    align: "center",
                    dataIndex: "banner"
                },
                {
                    title: "活动宣传奖励图",
                    align: "center",
                    dataIndex: "rewardImg"
                },
                {
                    title: "活动宣传仙力",
                    align: "center",
                    dataIndex: "combatPower"
                },
                {
                    title: "排行玩家数量",
                    align: "center",
                    dataIndex: "rankNum"
                },
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
                    title: "createTime",
                    align: "center",
                    dataIndex: "createTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "updateTime",
                    align: "center",
                    dataIndex: "updateTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
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