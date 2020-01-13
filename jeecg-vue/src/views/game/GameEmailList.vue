<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="4" :sm="6">
                        <a-form-item label="标题">
                            <a-input placeholder="请输入标题" v-model="queryParam.title"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="3" :sm="5">
                        <a-form-item label="状态">
                            <a-select placeholder="邮件状态" v-model="queryParam.validState">
                                <a-select-option :value="1">有效</a-select-option>
                                <a-select-option :value="2">无效</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="3" :sm="5">
                            <a-form-item label="类型">
                                <a-select ref="targetSelector" v-model="queryParam.targetBodyType" @change="selectTarget">
                                    <a-select-option :value="1">玩家</a-select-option>
                                    <a-select-option :value="2">全服</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col v-if="serverType" :md="6" :sm="8">
                            <a-form-item label="服务器">
                                <server-select @select="change"></server-select>
                            </a-form-item>
                        </a-col>
                        <a-col v-if="playerType" :md="6" :sm="8">
                            <a-form-item label="玩家账号">
                                <a-input placeholder="请输入玩家ID" v-model="queryParam.targetBodyId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="生效时间">
                                <j-date placeholder="请选择生效时间" v-model="queryParam.sendTime"></j-date>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="时间">
                                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.validStarTime_begin"></j-date>
                                <span class="query-group-cust"></span>
                                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.validStarTime_end"></j-date>
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
        </div>

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
                @change="handleTableChange"
            >
            </a-table>
        </div>

        <gameEmail-modal ref="modalForm" @ok="modalFormOk"></gameEmail-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameEmailModal from "./modules/GameEmailModal";
import JDate from "@/components/jeecg/JDate.vue";

export default {
    name: "GameEmailList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameEmailModal
    },
    data() {
        return {
            description: "游戏下发邮件管理页面",
            queryParam: {
                targetBodyId: "",
                validState: "",
                targetBodyType: ""
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
                    title: "标题",
                    align: "center",
                    dataIndex: "title"
                },
                {
                    title: "描述",
                    align: "center",
                    dataIndex: "remark"
                },
                {
                    title: "类型",
                    align: "center",
                    dataIndex: "emailType",
                    customRender: function(text) {
                        return text == 1 ? "无附件" : "有附件";
                    }
                },
                {
                    title: "附件",
                    align: "center",
                    dataIndex: "content"
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "validState",
                    customRender: function(text) {
                        return text == 1 ? "有效" : "无效";
                    }
                },
                {
                    title: "目标类型",
                    align: "center",
                    dataIndex: "targetBodyType",
                    customRender: function(text) {
                        return text == 1 ? "玩家" : "全服";
                    }
                },
                {
                    title: "目标主体",
                    align: "center",
                    dataIndex: "targetBodyId"
                },
                {
                    title: "生效时间",
                    align: "center",
                    dataIndex: "sendTime",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "开始时间",
                    align: "center",
                    dataIndex: "validStarTime",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "结束时间",
                    align: "center",
                    dataIndex: "validEndTime",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                }
            ],
            serverType: false,
            playerType: false,
            url: {
                list: "game/gameEmail/list"
            },
            dictOptions: {}
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    mounted() {},
    methods: {
        initDictConfig() {},
        selectTarget(target) {
            if (`${target}` == 1) {
                this.serverType = false;
                this.playerType = true;
                this.queryParam.targetBodyId = "";
            } else if (`${target}` == 2) {
                this.serverType = true;
                this.playerType = false;
                this.queryParam.targetBodyId = "";
            } else {
                this.serverType = false;
                this.playerType = false;
            }
        },
        change(serverId) {
            this.queryParam.targetBodyId = serverId;
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
