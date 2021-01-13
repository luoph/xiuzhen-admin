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
                        <a-form-item label="就近天数">
                            <a-select placeholder="天数" v-model="queryParam.days">
                                <a-select-option :value="0">不选择天数</a-select-option>
                                <a-select-option :value="7">近7天</a-select-option>
                                <a-select-option :value="15">近15天</a-select-option>
                                <a-select-option :value="30">近一个月</a-select-option>
                                <a-select-option :value="60">近两个月</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="5" :sm="5">
                        <a-form-item label="产销类型" >
                            <a-select placeholder="产销类型" v-model="queryParam.productAndMarketTyep" initialValue="1" >
                                <a-select-option :value="1002">玉髓</a-select-option>
                                <a-select-option :value="1010">仙石</a-select-option>
                                <a-select-option :value="1001">灵石</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="5" :sm="5">
                        <a-form-item label="货币类型">
                            <a-select placeholder="货币类型" v-model="queryParam.quantityType" initialValue="1">
                                <a-select-option :value="1">产出</a-select-option>
                                <a-select-option :value="2">消耗</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!--查询区域结束-->
        <!-- table区域-begin -->
        <a-row :gutter="45" type="flex">
            <a-col :md="8" :sm="8" v-for="colu in columnsList" :key="colu.id">
                <div>
                    <a-table
                        ref="table"
                        size="middle"
                        bordered
                        :rowKey="(record) => (record.id != null ? record.id : '0')"
                        :loading="loading"
                        :columns="colu.columns"
                        :dataSource="colu.columnsData"
                        :pagination="false"
                        :scroll="{ x: 'max-content' }"
                    >
                        <div slot="title" class="word-v-middle">{{colu.time}}</div></a-table
                    >
                </div>
            </a-col>
        </a-row>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import { getAction } from "@/api/manage";

export default {
    description: "货币分布",
    name: "GameRemainOfNewUserList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            showColumn: "",
            columnsList: [
                
            ],
            url: {
                list: "game/monetaryDistribution/list"
            },
            dictOptions: {}
        };
    },
    computed: {},
    methods: {
        initDictConfig() {},
        onSelectChannel: function (channelId) {
            this.queryParam.channelId = channelId;
        },
        onSelectServer: function (serverId) {
            this.queryParam.serverId = serverId;
        },
        onDateChange: function (value, dateStr) {
            this.queryParam.rangeDateBegin = dateStr[0];
            this.queryParam.rangeDateEnd = dateStr[1];
        },
        removeByValue: function (arr, attr, value) {
            for (var j = 0; j < arr.length; j++) {
                console.log(arr[j].dataIndex);
                console.log(value);
                if (arr[j].dataIndex == value) {
                    // member2 = members[j];
                    arr.splice(j, 1);
                    // this.$delete(arr,j)
                    break;
                }
            }
        },
        pushByValue: function (arr, title, dataIndex) {
            let column = {
                title: "新增玩家",
                dataIndex: "registerNum",
                align: "center",
                width: "120"
            };
            arr.push(
                (column = {
                    title: "新增玩家",
                    dataIndex: "registerNum",
                    align: "center",
                    width: "120"
                })
            );
        },
        searchQuery() {
            let param = {
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                days: this.queryParam.days,
                productAndMarketType: this.queryParam.productAndMarketType,
                quantityType: this.queryParam.quantityType
            };
            getAction(this.url.list, param).then((res) => {
                this.columnsList = [];
                if (res.success) {
                    console.log(res.result.records);
                    for (const aa of res.result.records) {

                    let a = {
                                id: aa.time,
                                time: aa.time,
                                ipagination: {
                                    current:1,
                                    size:1,
                                    total:1,
                                    pages:1
                                },
                                columns: [
                                    {
                                        title: "产销点",
                                        dataIndex: "productAndMarket",
                                        width: "120",
                                        align: "center",
                                        customRender: function (text) {
                                            return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                                        }
                                    },
                                    {
                                        title: "货币数量",
                                        dataIndex: "quantityOfMoney",
                                        align: "center",
                                        width: "120"
                                    },
                                    {
                                        title: "人数",
                                        dataIndex: "numberOfPeople",
                                        align: "center",
                                        width: "120",
                                    },
                                    {
                                        title: "次数",
                                        dataIndex: "times",
                                        align: "center",
                                        width: "120",
                                    },
                                    {
                                        title: "占比",
                                        dataIndex: "proportion",
                                        align: "center",
                                        width: "120",
                                        customRender: (text, record) => {
                                            return this.countRate(record.proportion);
                                        }
                                    }
                                ],
                                columnsData: aa.monetaryDisTributionVOS
                            }
                        this.columnsList.push(a);
                    }

                    
                    this.columnsList.push();
                    this.dataSource = res.result.records;
                    this.ipagination.current = res.result.current;
                    this.ipagination.size = res.result.size.toString();
                    this.ipagination.total = res.result.total;
                    this.ipagination.pages = res.result.pages;
                } else {
                    this.$message.error(res.message);
                }
            });
        },
        countRate: function (n) {
            if (n === null || n === undefined) {
                return "--";
            }
            return Number(parseFloat(n * 100).toFixed(2)) + "%";
        }
    },
    mounted: function () {}
};
</script>

<style scoped>
@import "~@assets/less/common.less";

.word-v-middle {
    margin-bottom: 0;
    font-size: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 21px;
    margin-top: 0px;
    color: #0c0c0c;
    white-space: normal;
}
</style>
