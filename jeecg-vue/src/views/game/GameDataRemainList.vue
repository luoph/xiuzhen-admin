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
                    <a-col :md="4" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!--查询区域结束-->
        <!-- table区域-begin -->
        <div>
            <a-table
                ref="table"
                size="middle"
                bordered
                :rowKey="record => (record.id != null ? record.id : '0')"
                :loading="loading"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :scroll="{ x: 1500, y: 800 }"
                @change="handleTableChange"
            ></a-table>
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
    description: "留存率",
    name: "GameDataRemainList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: "4%",
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "日期",
                    dataIndex: "countDate",
                    align: "center",
                    width: "6%",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "新增角色",
                    dataIndex: "registerNum",
                    width: "5%",
                    align: "center"
                },
                {
                    title: "首日免费角色",
                    dataIndex: "freeNum",
                    width: "5%",
                    align: "center"
                },
                {
                    title: "首日付费角色",
                    dataIndex: "payNum",
                    width: "5%",
                    align: "center"
                },
                {
                    title: "首日付费率",
                    dataIndex: "payRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.payNum, record.registerNum);
                    }
                },
                {
                    title: "免费角色次留率",
                    dataIndex: "freeRemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.freeRemain, record.freeNum);
                    }
                },
                {
                    title: "付费角色次留率",
                    dataIndex: "payRemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.payRemain, record.payNum);
                    }
                },
                {
                    title: "次留率",
                    dataIndex: "d2RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d2Remain, record.registerNum);
                    }
                },
                {
                    title: "3留率",
                    dataIndex: "d3RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d3Remain, record.registerNum);
                    }
                },
                {
                    title: "4留率",
                    dataIndex: "d4RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d4Remain, record.registerNum);
                    }
                },
                {
                    title: "5留率",
                    dataIndex: "d5RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d5Remain, record.registerNum);
                    }
                },
                {
                    title: "6留率",
                    dataIndex: "d6RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d6Remain, record.registerNum);
                    }
                },
                {
                    title: "7留率",
                    dataIndex: "d7RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d7Remain, record.registerNum);
                    }
                },
                {
                    title: "15留率",
                    dataIndex: "d15RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d15Remain, record.registerNum);
                    }
                },
                {
                    title: "30留率",
                    dataIndex: "d30RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d30Remain, record.registerNum);
                    }
                },
                {
                    title: "60留率",
                    dataIndex: "d60RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d60Remain, record.registerNum);
                    }
                },
                {
                    title: "90留率",
                    dataIndex: "d90RemainRate",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d90Remain, record.registerNum);
                    }
                },
                {
                    title: "120留率",
                    dataIndex: "d120Remain",
                    width: "5%",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d120Remain, record.registerNum);
                    }
                }
            ],
            url: {
                list: "game/statistics/remainRate"
            },
            dictOptions: {}
        };
    },
    computed: {},
    methods: {
        initDictConfig() {},
        onSelectChannel: function(channelId) {
            this.queryParam.channelId = channelId;
        },
        onSelectServer: function(serverId) {
            this.queryParam.serverId = serverId;
        },
        onDateChange: function(value, dateStr) {
            this.queryParam.rangeDateBegin = dateStr[0];
            this.queryParam.rangeDateEnd = dateStr[1];
        },
        searchQuery() {
            let param = {
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            };
            getAction(this.url.list, param).then(res => {
                if (res.success) {
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
        countRate: function(n, r) {
            if (n === null || n === undefined) {
                return "--";
            }
            let rate = r > 0 ? parseFloat((n / r) * 100).toFixed(2) : parseFloat(0).toFixed(2);
            return rate + "%";
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
