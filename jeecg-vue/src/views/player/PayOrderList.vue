<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="己方单号">
                            <a-input placeholder="请输入己方单号" v-model="queryParam.queryId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="平台方订单号">
                            <a-input placeholder="请输入平台方订单号" v-model="queryParam.orderId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="创建时间">
                            <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="渠道key">
                                <a-input placeholder="请输入渠道key" v-model="queryParam.channelKey"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="渠道id">
                                <a-input placeholder="请输入渠道Id" v-model="queryParam.channelId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="服务器id">
                                <a-input placeholder="请输入服务器Id" v-model="queryParam.serverId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="玩家Id">
                                <a-input placeholder="请输入玩家Id" v-model="queryParam.playerId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="商品id">
                                <a-input placeholder="请输入商品id" v-model="queryParam.goodsId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="订单状态">
                                <a-input placeholder="请输入订单状态" v-model="queryParam.orderStatus"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="12" :sm="16">
                            <a-form-item label="金额">
                                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.realAmount_begin"></a-input>
                                <span class="query-group-split-cust"></span>
                                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.realAmount_end"></a-input>
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
            <!-- <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button> -->
            <a-button type="primary" icon="download" @click="handleExportXls('充值订单')">导出</a-button>
            <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
            <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown> -->
        </div>

        <!-- table区域-begin -->
        <div>
            <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div> -->

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
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">详情</a>
                    <!-- <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown> -->
                </span>
            </a-table>
        </div>

        <payOrder-modal ref="modalForm" @ok="modalFormOk"></payOrder-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import PayOrderModal from "./modules/PayOrderModal";
import { filterObj } from "@/utils/util";

export default {
    name: "PayOrderList",
    mixins: [JeecgListMixin],
    components: {
        PayOrderModal
    },
    data() {
        return {
            description: "充值订单管理页面",
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
                    title: "玩家Id",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "己方单号",
                    align: "center",
                    dataIndex: "queryId"
                },
                {
                    title: "平台方订单号",
                    align: "center",
                    dataIndex: "orderId"
                },
                {
                    title: "渠道id",
                    align: "center",
                    dataIndex: "channelId"
                },
                {
                    title: "服务器id",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "商品id",
                    align: "center",
                    dataIndex: "goodsId"
                },
                {
                    title: "ip地址",
                    align: "center",
                    dataIndex: "remoteIp"
                },
                {
                    title: "订单状态",
                    align: "center",
                    dataIndex: "orderStatus_dictText"
                },
                {
                    title: "订单金额",
                    align: "center",
                    dataIndex: "realAmount"
                },
                // {
                //     title: "充值货币",
                //     align: "center",
                //     dataIndex: "currency"
                // },
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
                list: "player/payOrder/list",
                exportXlsUrl: "player/payOrder/exportXls"
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
        getQueryParams() {
            console.log(this.queryParam.createTimeRange);
            var param = Object.assign({}, this.queryParam, this.isorter);
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // 范围参数不传递后台
            delete param.createTimeRange;
            return filterObj(param);
        },
        onDateChange: function(value, dateString) {
            console.log(dateString[0], dateString[1]);
            this.queryParam.createTime_begin = dateString[0];
            this.queryParam.createTime_end = dateString[1];
        },
        onDateOk(value) {
            console.log(value);
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
