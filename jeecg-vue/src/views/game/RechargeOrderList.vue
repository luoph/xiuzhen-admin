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
            <a-button type="primary" icon="download" @click="handleExportXls('今日礼包')">导出</a-button>
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

        <rechargeOrder-modal ref="modalForm" @ok="modalFormOk"></rechargeOrder-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import RechargeOrderModal from "./modules/RechargeOrderModal";

export default {
    name: "RechargeOrderList",
    mixins: [JeecgListMixin],
    components: {
        RechargeOrderModal
    },
    data() {
        return {
            description: "今日礼包管理页面",
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
                    title: "支付玩家id",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "己方订单号",
                    align: "center",
                    dataIndex: "orderId"
                },
                {
                    title: "平台方订单号",
                    align: "center",
                    dataIndex: "queryId"
                },
                {
                    title: "商品id",
                    align: "center",
                    dataIndex: "goodsId"
                },
                {
                    title: "下发的商品",
                    align: "center",
                    dataIndex: "items"
                },
                {
                    title: "首次额外赠送",
                    align: "center",
                    dataIndex: "addition"
                },
                {
                    title: "ip地址",
                    align: "center",
                    dataIndex: "remoteIp"
                },
                {
                    title: "0 - 未处理，1 - 已处理",
                    align: "center",
                    dataIndex: "status"
                },
                {
                    title: "1 - 正常充值 2 - 虚拟充值",
                    align: "center",
                    dataIndex: "type"
                },
                {
                    title: "实际支付金额",
                    align: "center",
                    dataIndex: "payAmount"
                },
                {
                    title: "扩展自定义字段",
                    align: "center",
                    dataIndex: "custom"
                },
                {
                    title: "发货时间",
                    align: "center",
                    dataIndex: "sendTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
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
                    title: "创建时间戳",
                    align: "center",
                    dataIndex: "createTime",
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
                list: "game/rechargeOrder/list",
                delete: "game/rechargeOrder/delete",
                deleteBatch: "game/rechargeOrder/deleteBatch",
                exportXlsUrl: "game/rechargeOrder/exportXls",
                importExcelUrl: "game/rechargeOrder/importExcel"
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