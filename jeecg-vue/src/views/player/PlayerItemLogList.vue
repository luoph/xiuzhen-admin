<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="区服ID">
                            <server-select @select="change"></server-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="玩家ID">
                            <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="道具ID">
                            <a-input placeholder="请输入道具ID" v-model="queryParam.itemId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="同步时间">
                            <a-range-picker v-model="queryParam.syncTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="8" :sm="10">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    
                        <span style="float: left;overflow: hidden; margin-left:20px" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="plus" @click="handleAddSync">同步</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->

        <!-- table区域-begin -->
        <div>
            <a-table 
                ref="table" 
                size="middle" 
                bordered rowKey="id" 
                :columns="columns" 
                :dataSource="dataSource" 
                :pagination="ipagination" 
                :loading="loading"
                @change="handleTableChange"> 
            </a-table>
        </div>

        <playerItemLog-modal ref="playerItemLogModal" @ok="modalFormOk"></playerItemLog-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import { filterObj } from "@/utils/util";
import PlayerItemLogModal from "./modules/PlayerItemLogModal";

export default {
    name: "PlayerItemLogList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        PlayerItemLogModal
    },
    data() {
        return {
            description: "玩家道具日志管理页面",
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
                    title: "服务器id",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "玩家id",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "道具id",
                    align: "center",
                    dataIndex: "itemId"
                },
                {
                    title: "数量",
                    align: "center",
                    dataIndex: "num"
                },
                {
                    title: "途径",
                    align: "center",
                    dataIndex: "way"
                },
                {
                    title: "方式",
                    align: "center",
                    dataIndex: "type",
                    customRender: function(text) {
                        return text == 1 ? "获取" : "使用";
                    }
                },
                {
                    title: "同步时间",
                    align: "center",
                    dataIndex: "syncTime",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                }
            ],
            url: {
                list: "player/playerItemLog/list"
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
        getQueryParams() {
            let param = Object.assign({}, this.queryParam, this.isorter);
            // 范围参数不传递后台
            delete param.syncTimeRange;
            return filterObj(param);
        },
        onDateChange: function(value, dateString) {
            this.queryParam.syncTime_begin = dateString[0];
            this.queryParam.syncTime_end = dateString[1];
        },
        change(serverId) {
            this.queryParam.serverId = serverId;
        },
        handleAddSync() {
            this.$refs.playerItemLogModal.visible = true;
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>