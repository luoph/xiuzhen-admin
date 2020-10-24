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
                    <a-col :md="4" :sm="4">
                        <a-form-item label="角色昵称">
                            <a-input placeholder="请输入角色昵称" v-model="queryParam.nickname"></a-input>
                        </a-form-item>
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
                <a-button type="primary" icon="download" @click="handleExportXls('行为分析')">导出</a-button>
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
    name: "PlayerBehaviorList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            description: "行为分析管理页面",
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
                    title: "日期",
                    align: "center",
                    dataIndex: "createDate"
                },
                {
                    title: "区服ID",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "玩家ID",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "玩家名字",
                    align: "center",
                    dataIndex: "nickname"
                },
                {
                    title: "修炼年数",
                    align: "center",
                    dataIndex: "practiceYear"
                },
                {
                    title: "境界等级",
                    align: "center",
                    dataIndex: "reamLevel"
                },
                {
                    title: "福地夺宝/斗法",
                    align: "center",
                    dataIndex: "arenaBattle"
                },
                {
                    title: "仙兽秘境",
                    align: "center",
                    dataIndex: "mapExplore"
                },
                {
                    title: "剧情关数",
                    align: "center",
                    dataIndex: "mainStoryLevel"
                },
                {
                    title: "灵界器灵",
                    align: "center",
                    dataIndex: "spiritualWorldoss"
                },
                {
                    title: "魔王入侵",
                    align: "center",
                    dataIndex: "worldBoss"
                },
                {
                    title: "仙魔仙宴",
                    align: "center",
                    dataIndex: "factionBanquet"
                },
                {
                    title: "当天充值",
                    align: "center",
                    dataIndex: "recharge"
                },
                {
                    title: "玉髓消耗",
                    align: "center",
                    dataIndex: "consumeMoney"
                },
                {
                    title: "在线时长",
                    align: "center",
                    dataIndex: "onlineTime"
                },

            ],
            url: {
                list: "game/player/behavior",
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
                days: this.queryParam.days,
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                nickname: this.queryParam.nickname,
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
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
