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
                        <a-form-item label="open_service_campaign_type.id">
                            <a-input placeholder="请输入open_service_campaign_type.id" v-model="queryParam.campaignTypeId"></a-input>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="open_service_campaign_gift_detail.id">
                                <a-input placeholder="请输入open_service_campaign_gift_detail.id" v-model="queryParam.giftDetailId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="排序">
                                <a-input placeholder="请输入排序" v-model="queryParam.sort"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="礼包类型">
                                <!-- 0.普通礼包, 1.大奖礼包 -->
                                <a-select placeholder="请选择礼包类型" v-model="queryParam.giftType" initialValue="1">
                                    <a-select-option :value="0">普通礼包</a-select-option>
                                    <a-select-option :value="1">大奖礼包</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="折扣">
                                <a-input placeholder="请输入折扣" v-model="queryParam.discount"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="价格">
                                <a-input placeholder="请输入价格" v-model="queryParam.price"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="奖励列表">
                                <a-input placeholder="请输入奖励列表" v-model="queryParam.reward"></a-input>
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
            <a-button type="primary" icon="download" @click="handleExportXls('开服活动-开服开服礼包-礼包明细')">导出</a-button>
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

        <openServiceCampaignGiftDetailItem-modal ref="modalForm" @ok="modalFormOk"></openServiceCampaignGiftDetailItem-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import OpenServiceCampaignGiftDetailItemModal from "./modules/OpenServiceCampaignGiftDetailItemModal";

export default {
    name: "OpenServiceCampaignGiftDetailItemList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignGiftDetailItemModal
    },
    data() {
        return {
            description: "开服活动-开服开服礼包-礼包明细管理页面",
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
                    title: "open_service_campaign_type.id",
                    align: "center",
                    dataIndex: "campaignTypeId"
                },
                {
                    title: "open_service_campaign_gift_detail.id",
                    align: "center",
                    dataIndex: "giftDetailId"
                },
                {
                    title: "排序",
                    align: "center",
                    dataIndex: "sort"
                },
                {
                    title: "礼包类型",
                    align: "center",
                    dataIndex: "giftType",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "普通礼包";
                        } else if (value === 1) {
                            text = "大奖礼包";
                        }
                        return text;
                    }
                },
                {
                    title: "折扣",
                    align: "center",
                    dataIndex: "discount"
                },
                {
                    title: "价格",
                    align: "center",
                    dataIndex: "price"
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
                    title: "更新时间",
                    align: "center",
                    dataIndex: "updateTime"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/openServiceCampaignGiftDetailItem/list",
                delete: "game/openServiceCampaignGiftDetailItem/delete",
                deleteBatch: "game/openServiceCampaignGiftDetailItem/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignGiftDetailItem/exportXls",
                importExcelUrl: "game/openServiceCampaignGiftDetailItem/importExcel"
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
