<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                <a-form-item label="单价(创建订单实际价格)">
                <a-input placeholder="请输入单价(创建订单实际价格)" v-model="queryParam.price"></a-input>
            </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                <a-form-item label="商品名">
                <a-input placeholder="请输入商品名" v-model="queryParam.name"></a-input>
            </a-form-item>
                </a-col>
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
            <a-button type="primary" icon="download" @click="handleExportXls('game_recharge_goods')">导出</a-button>
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

        <gameRechargeGoods-modal ref="modalForm" @ok="modalFormOk"></gameRechargeGoods-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameRechargeGoodsModal from "./modules/GameRechargeGoodsModal";

export default {
    name: "GameRechargeGoodsList",
    mixins: [JeecgListMixin],
    components: {
        GameRechargeGoodsModal
    },
    data() {
        return {
            description: "game_recharge_goods管理页面",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 40,
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "单价(创建订单实际价格)",
                    align: "center",
                    width: 170,
                    dataIndex: "price"
                },
                {
                    title: "折扣",
                    align: "center",
                    width: 60,
                    dataIndex: "discount"
                },
                {
                    title: "商品名",
                    align: "center",
                    width: 100,
                    dataIndex: "name"
                },
                {
                    title: "奖励列表",
                    align: "center",
                    dataIndex: "items"
                },
                {
                    title: "充值分类",
                    align: "center",
                    width: 80,
                    dataIndex: "goodsType"
                },
                {
                    title: "是否记入累充（0 - 不计入 1 - 记入）",
                    align: "center",
                    width: 180,
                    dataIndex: "amountStat"
                },
                {
                    title: "首次额外赠送",
                    align: "center",
                    width: 80,
                    dataIndex: "addition"
                },
                {
                    title: "游戏币与人民币(元)的兑换比例",
                    align: "center",
                    dataIndex: "exchange"
                },
                {
                    title: "创建者",
                    align: "center",
                    width: 80,
                    dataIndex: "createBy"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    width: 120,
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameRechargeGoods/list",
                delete: "game/gameRechargeGoods/delete",
                deleteBatch: "game/gameRechargeGoods/deleteBatch",
                exportXlsUrl: "game/gameRechargeGoods/exportXls",
                importExcelUrl: "game/gameRechargeGoods/importExcel"
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
