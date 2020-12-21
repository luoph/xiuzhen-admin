<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="4" :sm="6">
                        <a-form-item label="标题"><a-input placeholder="请输入标题" v-model="queryParam.title"></a-input></a-form-item>
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
                                <a-select placeholder="目标类型" ref="targetSelector" v-model="queryParam.targetType" @change="selectTarget">
                                    <a-select-option :value="1">玩家</a-select-option>
                                    <a-select-option :value="2">全服</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col v-if="serverType" :md="6" :sm="8">
                            <a-form-item label="服务器">
                                <multiple-server-select v-model="queryParam.targetIds" @changeSelect="change"></multiple-server-select>
                            </a-form-item>
                        </a-col>
                        <a-col v-if="playerType" :md="6" :sm="8">
                            <a-form-item label="玩家账号">
                                <a-input placeholder="请以英文“[,]”分割输入多个玩家ID" v-model="queryParam.targetIds" @input="inputChange($event)"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="生效时间"><j-date placeholder="请选择生效时间" v-model="queryParam.sendTime"></j-date></a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="时间">
                                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.startTime_begin"></j-date>
                                <span class="query-group-cust"></span>
                                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.startTime_end"></j-date>
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
        <div class="table-operator"><a-button type="primary" icon="plus" @click="handleAdd">新增</a-button></div>

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
                <template slot="largeText" slot-scope="text">
                    <div class="largeTextContainer">
                        <span class="largeText">{{ text }}</span>
                    </div>
                </template>
            </a-table>
        </div>

        <game-email-modal ref="modalForm" @ok="modalFormOk"></game-email-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameEmailModal from "./modules/GameEmailModal";
import JDate from "@/components/jeecg/JDate.vue";
import ServerSelect from "@/components/gameserver/ServerSelect";
import MultipleServerSelect from "@/components/gameserver/MultipleServerSelect";

export default {
    name: "GameEmailList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameEmailModal,
        ServerSelect,
        MultipleServerSelect
    },
    data() {
        return {
            description: "游戏下发邮件管理页面",
            queryParam: {
                validState: undefined,
                targetType: undefined
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
                    align: "left",
                    width: 120,
                    dataIndex: "title"
                },
                {
                    title: "描述",
                    align: "left",
                    width: 240,
                    dataIndex: "remark",
                    scopedSlots: { customRender: "largeText" }
                },
                {
                    title: "类型",
                    align: "center",
                    width: 80,
                    dataIndex: "emailType",
                    customRender: function(text) {
                        return text == 1 ? "无附件" : "有附件";
                    }
                },
                {
                    title: "附件",
                    align: "center",
                    width: 240,
                    dataIndex: "content",
                    scopedSlots: { customRender: "largeText" }
                },
                {
                    title: "状态",
                    align: "center",
                    width: 80,
                    dataIndex: "validState",
                    customRender: function(text) {
                        return text == 1 ? "有效" : "无效";
                    }
                },
                {
                    title: "目标类型",
                    align: "center",
                    width: 80,
                    dataIndex: "targetType",
                    customRender: function(text) {
                        return text == 1 ? "玩家" : "全服";
                    }
                },
                {
                    title: "目标主体",
                    align: "left",
                    width: 210,
                    dataIndex: "targetIds",
                    scopedSlots: { customRender: "largeText" }
                },
                {
                    title: "生效时间",
                    align: "center",
                    dataIndex: "sendTime"
                },
                {
                    title: "开始时间",
                    align: "center",
                    dataIndex: "startTime"
                },
                {
                    title: "结束时间",
                    align: "center",
                    dataIndex: "endTime"
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime"
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
            } else if (`${target}` == 2) {
                this.serverType = true;
                this.playerType = false;
            }
            this.queryParam.targetIds = "";
        },
        change(value) {
            this.queryParam.targetIds = value.join(",").toString();
        },
        inputChange(e) {
            this.$forceUpdate();
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";

.largeTextContainer {
    overflow-x: hidden;
    overflow-y: scroll;
    white-space: nowrap;
    max-height: 200px;
}

.largeText {
    white-space: normal;
    word-break: break-word;
}
</style>
