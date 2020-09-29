<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                <a-form-item label="渠道id">
                <a-input placeholder="请输入渠道id" v-model="queryParam.channel"></a-input>
            </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                <a-form-item label="服务器id">
                <a-input placeholder="请输入服务器id" v-model="queryParam.serverId"></a-input>
            </a-form-item>
                </a-col>
                <template v-if="toggleSearchStatus">
                    <a-col :md="12" :sm="16">
                    <a-form-item label="订单创建时间戳">
                    <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.payTimeBegin"></j-date>
                    <span class="query-group-split-cust"></span>
                    <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.payTimeEnd"></j-date>
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
            <a-button type="primary" icon="download" @click="handleExportXls('消费礼包数据统计')">导出</a-button>
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

        <payOrderGift-modal ref="modalForm" @ok="modalFormOk"></payOrderGift-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import PayOrderGiftModal from "./modules/PayOrderGiftModal";
import JDate from "@/components/jeecg/JDate.vue";

export default {
    name: "PayOrderGiftList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        PayOrderGiftModal
    },
    data() {
        return {
            description: "消费礼包数据统计管理页面",
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
                    title: "消费礼包类型",
                    align: "center",
                    dataIndex: "productId"
                },
                {
                    title: "消费次数",
                    align: "center",
                    dataIndex: "productCount"
                },
                {
                    title: "消费次数比例",
                    align: "center",
                    dataIndex: "productCountRatio"
                },
                {
                    title: "消费金额",
                    align: "center",
                    dataIndex: "payAmountSum"
                },
                {
                    title: "消费金额比例",
                    align: "center",
                    dataIndex: "payAmountRatio"
                },

                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/payOrderGift/list",
                delete: "game/payOrderGift/delete",
                deleteBatch: "game/payOrderGift/deleteBatch",
                exportXlsUrl: "game/payOrderGift/exportXls",
                importExcelUrl: "game/payOrderGift/importExcel"
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
