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
            <a-button type="primary" icon="download" @click="handleExportXls('开服活动消耗道具')">导出</a-button>
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

        <openServiceCampaignConsumeDetailItem-modal ref="modalForm" @ok="modalFormOk"></openServiceCampaignConsumeDetailItem-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import OpenServiceCampaignConsumeDetailItemModal from "./modules/OpenServiceCampaignConsumeDetailItemModal";

export default {
    name: "OpenServiceCampaignConsumeDetailItemList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignConsumeDetailItemModal
    },
    data() {
        return {
            description: "开服活动消耗道具管理页面",
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
                    title: "页签详情id",
                    align: "center",
                    dataIndex: "consumeDetailId"
                },
                {
                    title: "排序",
                    align: "center",
                    dataIndex: "sort"
                },
                {
                    title: "统计类型",
                    align: "center",
                    dataIndex: "consumeType",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "个人";
                        } else if (value === 1) {
                            text = "全服";
                        }
                        return text;
                    }
                },
                {
                    title: "开始时间(子活动开始第n天)",
                    align: "center",
                    dataIndex: "startDay"
                },
                {
                    title: "开启前是否统计，全服统计默认是",
                    align: "center",
                    dataIndex: "statisticsUnstart"
                },
                {
                    title: "描述",
                    align: "center",
                    dataIndex: "description"
                },
                {
                    title: "消耗道具",
                    align: "center",
                    dataIndex: "consume"
                },
                {
                    title: "奖励列表",
                    align: "center",
                    dataIndex: "reward"
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
                list: "game/openServiceCampaignConsumeDetailItem/list",
                delete: "game/openServiceCampaignConsumeDetailItem/delete",
                deleteBatch: "game/openServiceCampaignConsumeDetailItem/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignConsumeDetailItem/exportXls",
                importExcelUrl: "game/openServiceCampaignConsumeDetailItem/importExcel"
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
