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
                    <a-col :md="5" :sm="5">
                        <a-form-item label="折线图显示类型">
                            <a-select placeholder="显示类型" v-model="queryParam.lineType"  @change="onChangeLineType">
                                <a-select-option :value="'seconds'">按分</a-select-option>
                                <a-select-option :value="'hours'">按时</a-select-option>
                                <a-select-option :value="'days'">按天</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :md="4" :sm="8">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery2">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <div class="lateral-sliding" v-if="ynShowPicture">
            <div class="" v-for="(item, index) in items" :key="index">
                <lineChartMultid
                    class=""
                    :style="{ width: pictureWidth }"
                    title="折线图(数据量过大只会展示前1200条数据)"
                    :fields="c"
                    :dataSource="dataSourceLineChat"
                    :height="420"
                />
            </div>
        </div>
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

import LineChartMultid from "@/components/chart/LineChartMultid";
import Vue from "vue";

export default {
    name: "GameOnlineNumList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction,
        LineChartMultid
    },
    data() {
        return {
            timer: "",
            ynShowPicture: false,
            ik: 0,
            items: 1,
            pictureWidth: "1300px",
            c: ["pepole"],
            dataSourceLineChat: [],
            description: "在线情况管理页面",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "渠道",
                    align: "center",
                    dataIndex: "channel"
                },
                {
                    title: "服务器ID",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "在线人数",
                    align: "center",
                    dataIndex: "onlineNum"
                },
                {
                    title: "日期",
                    align: "center",
                    dataIndex: "getTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "获取在线人数的时间点",
                    align: "center",
                    dataIndex: "createTime"
                }
            ],
            url: {
                list: "game/gameOnlineNum/list"
            },
            dictOptions: {}
        };
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
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
        onChangeLineType: function(lineType){
            this.queryParam.lineType = lineType;
        },
        searchQuery2() {
            this.ynShowPicture = false;
            this.pictureWidth = "0px";
            this.timer = setTimeout(this.searchQuery, 10);
        },
        searchQuery() {
            let param = {
                days: this.queryParam.days,
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                // pageNo: this.ipagination.current,
                // pageSize: this.ipagination.pageSize
            };
            getAction(this.url.list, param).then((res) => {
                // this.pictureWidth = "0px";
                if (res.success) {
                    this.dataSourceLineChat = [];
                    this.dataSource = res.result.gameOnlineNumListAll;
                    if ("seconds" == this.queryParam.lineType) {
                        res.result.gameOnlineNumListSeconds.forEach((element) => {
                            let lineDate = { type: element.getTime, pepole: element.onlineNum };
                            if (this.dataSourceLineChat.length < 1200) {
                                this.dataSourceLineChat.push(lineDate);
                            }
                        });
                    }else if("days" == this.queryParam.lineType){
                        res.result.gameOnlineNumListDays.forEach((element) => {
                            let lineDate = { type: element.getTime, pepole: element.onlineNum };
                            if (this.dataSourceLineChat.length < 1200) {
                                this.dataSourceLineChat.push(lineDate);
                            }
                        });
                    }else {
                        res.result.gameOnlineNumListHours.forEach((element) => {
                            let lineDate = { type: element.getTime, pepole: element.onlineNum };
                            if (this.dataSourceLineChat.length < 1200) {
                                this.dataSourceLineChat.push(lineDate);
                            }
                        });
                        this.queryParam.lineType = "hours"
                    }

                    // for (let index = 0; index < 40; index++) {
                    //     this.ik ++;
                    //     let lineDate = { type: this.ik, a: 18.4 };
                    //     this.dataSourceLineChat.push(lineDate);
                    // }
                    if (this.dataSourceLineChat.length > 0) {
                        this.ynShowPicture = true;
                    }
                    // this.$forceUpdate();
                    if (this.dataSourceLineChat.length <= 20) {
                        this.pictureWidth = 22 * 50 + "px";
                    } else {
                        this.pictureWidth = this.dataSourceLineChat.length * 50 + "px";
                    }
                    console.log(this.dataSourceLineChat.length);
                    // this.ipagination.current = res.result.current;
                    // this.ipagination.size = res.result.size.toString();
                    // this.ipagination.total = res.result.total;
                    // this.ipagination.pages = res.result.pages;
                } else {
                    this.$message.error(res.message);
                }
            });
        },
        beforeDestroy() {
            clearTimeout(this.timer);
        },
        
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
.lateral-sliding {
    display: flex;
    overflow-y: hidden;
    overflow-x: scroll;
}
</style>
