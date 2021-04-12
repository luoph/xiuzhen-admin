<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="12" :sm="16">
                        <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
                    </a-col>
                    <a-col :md="12" :sm="8">
                        <a-form-item label="分析日期">
                            <j-date placeholder="请选择分析日期" v-model="queryParam.analysisDate"></j-date>
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="10" />
                    <a-col :md="6" :sm="4">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="download" @click="handleExportXls('剧情分析')">导出</a-button>
        </div>

        <!-- table区域-begin -->
        <div>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="level"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                @change="handleTableChange"
                :scroll="tableScroll"
            >
            </a-table>
        </div>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";

export default {
    name: "GameStoryAnalysisList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer
    },
    data() {
        return {
            description: "剧情分析管理页面",
            queryParam: {
                serverId: null,
                analysisDate: null
            },
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
                    title: "剧情小关卡",
                    align: "center",
                    dataIndex: "storyCheckpoint"
                },
                {
                    title: "停留活跃人数",
                    align: "center",
                    dataIndex: "stayLiveNum"
                },
                {
                    title: "活跃占比",
                    align: "center",
                    dataIndex: "liveRate",
                    customRender: text => {
                        return !text ? "" : text + "%";
                    }
                },
                {
                    title: "停留流失人数",
                    align: "center",
                    dataIndex: "stayLeaveNum"
                },
                {
                    title: "流失占比",
                    align: "center",
                    dataIndex: "leaveRate",
                    customRender: text => {
                        return !text ? "" : text + "%";
                    }
                },
                {
                    title: "总达成人数",
                    align: "center",
                    dataIndex: "totalArriveNum"
                },
                {
                    title: "总滞留人数",
                    align: "center",
                    dataIndex: "totalStayNum"
                },
                {
                    title: "关卡滞留率",
                    align: "center",
                    dataIndex: "checkpointStayRate",
                    customRender: text => {
                        return !text ? "" : text + "%";
                    }
                }
            ],
            url: {
                list: "game/gameStoryAnalysis/list",
                exportXlsUrl: "game/gameStoryAnalysis/exportXls",
                importExcelUrl: "game/gameStoryAnalysis/importExcel"
            },
            dictOptions: {},
            tableScroll: { x: 11 * 147 + 50 }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {},
        onSelectServer(serverId) {
            this.queryParam.serverId = serverId;
        },
        onSelectChannel(channelId) {
            this.queryParam.channelId = channelId;
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
