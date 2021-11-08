<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="商品ID">
                            <a-input placeholder="请输入商品ID" v-model="queryParam.goodsId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="SKU">
                            <j-input placeholder="请输入sku模糊查询" v-model="queryParam.sku"></j-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="商品名称">
                            <j-input placeholder="商品名称模糊查询" v-model="queryParam.name"></j-input>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="4" :sm="8">
                            <a-form-item label="单价">
                                <a-input placeholder="请输入单价" v-model="queryParam.price"></a-input>
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
            <a-button type="primary" icon="download" @click="handleExportXls('game_recharge_goods')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <a-button @click="updateGoods" type="primary" icon="sync">刷新商品配置</a-button>
            <a-button :disabled="!importText" type="primary" icon="import" @click="handleImportText()">导入文本</a-button>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel">
                        <a-icon type="delete" />
                        删除
                    </a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px">
                    批量操作
                    <a-icon type="down" />
                </a-button>
            </a-dropdown>

            <a-textarea class="import-text" v-model="importText" placeholder="输入Excel复制来的文本数据"></a-textarea>
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
                :scroll="{ x: 'max-content' }"
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
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
                <template slot="largeText" slot-scope="text">
                    <div class="large-text-container">
                        <span class="large-text">{{ text }}</span>
                    </div>
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
import { postAction } from "@api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameRechargeGoodsModal from "./modules/GameRechargeGoodsModal";
import JInput from "@/components/jeecg/JInput";
import { getAction } from "../../api/manage";

export default {
    name: "GameRechargeGoodsList",
    mixins: [JeecgListMixin],
    components: {
        GameRechargeGoodsModal,
        JInput
    },
    data() {
        return {
            description: "充值商品管理页面",
            importText: "",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    fixed: "left",
                    width: 40,
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "商品Id",
                    align: "center",
                    fixed: "left",
                    width: 80,
                    dataIndex: "goodsId"
                },
                {
                    title: "商品名称",
                    align: "center",
                    fixed: "left",
                    width: 120,
                    dataIndex: "name"
                },
                {
                    title: "商品分类",
                    align: "center",
                    width: 120,
                    dataIndex: "goodsType",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "0-普通类型";
                        } else if (value === 1) {
                            text = "1-仙职";
                        } else if (value === 2) {
                            text = "2-月卡";
                        } else if (value === 3) {
                            text = "3-每日礼包";
                        } else if (value === 4) {
                            text = "4-首充";
                        } else if (value === 5) {
                            text = "5-周卡";
                        } else if (value === 6) {
                            text = "6-六道剑阵";
                        } else if (value === 7) {
                            text = "7-招财进宝/仙力护符";
                        } else if (value === 8) {
                            text = "8-高级天道令";
                        } else if (value === 9) {
                            text = "9-节日派对";
                        } else if (value === 10) {
                            text = "10-节日直购礼包";
                        } else if (value === 11) {
                            text = "11-精准礼包";
                        } else if (value === 12) {
                            text = "12-结义礼包";
                        } else if (value === 13) {
                            text = "13-自选特惠";
                        }
                        return text;
                    }
                },
                {
                    title: "内购SKU",
                    align: "center",
                    width: 120,
                    dataIndex: "sku"
                },
                {
                    title: "网页支付SKU",
                    align: "center",
                    width: 120,
                    dataIndex: "webSku"
                },
                {
                    title: "单价",
                    align: "center",
                    width: 80,
                    dataIndex: "price"
                },
                {
                    title: "折扣",
                    align: "center",
                    width: 80,
                    dataIndex: "discount"
                },
                {
                    title: "货币",
                    align: "center",
                    width: 80,
                    dataIndex: "currency"
                },
                {
                    title: "特殊标签",
                    align: "center",
                    width: 120,
                    dataIndex: "recommend",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "无(0)";
                        } else if (value === 1) {
                            text = "推荐(1)";
                        } else if (value === 2) {
                            text = "礼包(2)";
                        }
                        return text;
                    }
                },
                {
                    title: "奖励列表",
                    align: "center",
                    width: 220,
                    dataIndex: "items",
                    scopedSlots: { customRender: "largeText" }
                },
                {
                    title: "是否计入累充",
                    align: "center",
                    width: 80,
                    dataIndex: "amountStat",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "否";
                        } else if (value === 1) {
                            text = "是";
                        }
                        return text;
                    }
                },
                {
                    title: "兑换比例",
                    align: "center",
                    width: 80,
                    dataIndex: "exchange"
                },
                {
                    title: "首次额外赠送",
                    align: "center",
                    width: 180,
                    dataIndex: "addition"
                },
                {
                    title: "当地价格",
                    align: "center",
                    width: 120,
                    dataIndex: "localPrice"
                },
                {
                    title: "网页支付价格",
                    align: "center",
                    width: 120,
                    dataIndex: "webLocalPrice"
                },
                {
                    title: "显示价格",
                    align: "center",
                    width: 120,
                    dataIndex: "displayPrice"
                },
                {
                    title: "网页显示价格",
                    align: "center",
                    width: 120,
                    dataIndex: "webDisplayPrice"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    fixed: "right",
                    width: 120,
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameRechargeGoods/list",
                delete: "game/gameRechargeGoods/delete",
                deleteBatch: "game/gameRechargeGoods/deleteBatch",
                exportXlsUrl: "game/gameRechargeGoods/exportXls",
                importExcelUrl: "game/gameRechargeGoods/importExcel",
                updateGoods: "game/gameRechargeGoods/updateGoods",
                importTextUrl: "game/gameRechargeGoods/importText"
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
        updateGoods() {
            // 查询条件
            this.loading = true;
            getAction(this.url.updateGoods).then(res => {
                if (res.code === 510) {
                    this.$message.warning(res.message);
                }
                this.loading = false;
            });
        },
        handleImportText() {
            let params = {
                id: 0,
                text: this.importText
            };
            console.log(params);
            postAction(this.url.importTextUrl, params).then(res => {
                if (res.success) {
                    this.$message.success(res.message);
                    this.loadData();
                } else {
                    this.$message.warning(res.message);
                }
            });
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
.import-text {
    margin-top: 8px;
    margin-bottom: -10px;
}

.large-text-container {
    display: flex;
    overflow-x: hidden;
    overflow-y: auto;
    max-height: 200px;
}

.large-text {
    white-space: normal;
    word-break: break-word;
}
</style>
