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
                    <a-col :md="8" :sm="8">
                        <a-form-item label="日期">
                            <a-range-picker v-model="queryParam.countDateRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
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
                rowKey="id"
                :loading="loading"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :scroll="{ x: 'max-content' }"
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
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
                    align: "center",
                    width: "60",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "日期",
                    dataIndex: "countDate",
                    align: "center",
                    width: "140",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "区服id",
                    align: "center",
                    width: "80",
                    dataIndex: "serverId"
                },
                {
                    title: "新增角色",
                    align: "center",
                    width: "60",
                    dataIndex: "registerNum"
                },
                {
                    title: "LTV1",
                    align: "center",
                    width: "60",
                    dataIndex: "d1Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d1Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV2",
                    align: "center",
                    width: "60",
                    dataIndex: "d2Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d2Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV3",
                    align: "center",
                    width: "60",
                    dataIndex: "d3Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d3Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV4",
                    align: "center",
                    width: "60",
                    dataIndex: "d4Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d4Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV5",
                    align: "center",
                    width: "60",
                    dataIndex: "d5Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d5Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV6",
                    dataIndex: "d6Amount",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d6Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV7",
                    dataIndex: "d7Amount",
                    align: "center",
                    customRender: (text, record) => {
                        return this.countRate(record.d7Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV14",
                    align: "center",
                    width: "60",
                    dataIndex: "d14AmountRata",
                    customRender: (text, record) => {
                        return this.countRate(record.d14Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV21",
                    align: "center",
                    width: "60",
                    dataIndex: "d21Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d21Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV30",
                    align: "center",
                    width: "60",
                    dataIndex: "d30Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d30Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV60",
                    align: "center",
                    width: "60",
                    dataIndex: "d60Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d60Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV90",
                    align: "center",
                    width: "60",
                    dataIndex: "d90Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d90Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV120",
                    align: "center",
                    width: "60",
                    dataIndex: "d120Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d120Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV180",
                    align: "center",
                    width: "60",
                    dataIndex: "d180Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d180Amount, record.registerNum);
                    }
                },
                {
                    title: "LTV360",
                    align: "center",
                    width: "60",
                    dataIndex: "d360Amount",
                    customRender: (text, record) => {
                        return this.countRate(record.d360Amount, record.registerNum);
                    }
                }
            ],
            url: {
                list: "gameStat/ltv/list"
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
        getQueryParams() {
            console.log(this.queryParam.countDateRange);
            var param = Object.assign({}, this.queryParam, this.isorter);
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // 范围参数不传递后台
            delete param.countDateRange;
            return filterObj(param);
        },
        onDateChange: function(value, dateString) {
            console.log(dateString[0], dateString[1]);
            this.queryParam.countDate_begin = dateString[0];
            this.queryParam.countDate_end = dateString[1];
        },
        countRate: function(n, r) {
            if (n === null || n === undefined) {
                return "--";
            }
            return r > 0 ? parseFloat(n / r).toFixed(2) : parseFloat(0).toFixed(2);
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
