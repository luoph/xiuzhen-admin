<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24"> </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('开服夺宝详情')">导出</a-button>
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

        <openServiceCampaignLotteryDetail-modal ref="modalForm" @ok="modalFormOk"></openServiceCampaignLotteryDetail-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import OpenServiceCampaignLotteryDetailModal from "./modules/OpenServiceCampaignLotteryDetailModal";

export default {
    name: "OpenServiceCampaignLotteryDetailList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignLotteryDetailModal
    },
    data() {
        return {
            description: "开服夺宝详情管理页面",
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
                //     title: "活动TypeId",
                //     align: "center",
                //     dataIndex: "campaignTypeId"
                // },
                {
                    title: "活动页签名称",
                    align: "center",
                    dataIndex: "tabName"
                },
                {
                    title: "活动名称",
                    align: "center",
                    dataIndex: "name"
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
                    title: "活动宣传背景图",
                    align: "center",
                    dataIndex: "banner"
                },
                {
                    title: "骨骼动画资源图",
                    align: "center",
                    dataIndex: "skeleton"
                },
                {
                    title: "获奖记录显示数量",
                    align: "center",
                    dataIndex: "rewardRecordNum"
                },
                {
                    title: "获奖记录内容",
                    align: "center",
                    dataIndex: "rewardRecordMsg"
                },
                {
                    title: "获奖传闻内容",
                    align: "center",
                    dataIndex: "rewardMsg"
                },
                {
                    title: "概率公示",
                    align: "center",
                    dataIndex: "probabilityMsg"
                },
                {
                    title: "抽奖设置(单抽/多抽)",
                    align: "center",
                    dataIndex: "lotteryType"
                },
                {
                    title: "展示特奖",
                    align: "center",
                    dataIndex: "ssrShowReward"
                },
                {
                    title: "展示大奖",
                    align: "center",
                    dataIndex: "srShowReward"
                },
                {
                    title: "展示奖励",
                    align: "center",
                    dataIndex: "showReward"
                },
                {
                    title: "重置大奖",
                    align: "center",
                    dataIndex: "resetReward"
                },
                {
                    title: "奖池",
                    align: "center",
                    dataIndex: "rewardPool"
                },
                {
                    title: "帮助信息",
                    align: "center",
                    dataIndex: "helpMsg"
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
                list: "game/openServiceCampaignLotteryDetail/list",
                delete: "game/openServiceCampaignLotteryDetail/delete",
                deleteBatch: "game/openServiceCampaignLotteryDetail/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignLotteryDetail/exportXls",
                importExcelUrl: "game/openServiceCampaignLotteryDetail/importExcel"
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
        initDictConfig() {}
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
