<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="45">
                    <a-col :md="10" :sm="8">
                        <!--@ = v-on:数据绑定 不是事件-->
                        <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="创建日期">
                            <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="5" :sm="5">
                        <a-form-item label="选择就近天数">
                            <a-select placeholder="天数" v-model="queryParam.days">
                                <a-select-option :value="0">不选择天数</a-select-option>
                                <a-select-option :value="7">近7天</a-select-option>
                                <a-select-option :value="15">近15天</a-select-option>
                                <a-select-option :value="30">近一个月</a-select-option>
                                <a-select-option :value="60">近两个月</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :md="4" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>

            <div class="table-operator">
                <a-button type="primary" icon="download" @click="handleExportXls('每日礼包')">导出</a-button>
            </div>

        </div>
        <!-- 查询区域-END -->
        <!-- table区域-begin -->
        <div>
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
            </a-table>
        </div>

    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import { filterObj } from "@/utils/util";
import { getAction } from "@/api/manage";

export default {
    name: "RechargeOrderList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
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
