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
                        <a-form-item label="玩家id">
                            <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="4">
                        <a-form-item label="角色昵称">
                            <a-input placeholder="请输入角色昵称" v-model="queryParam.nickName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="8" :sm="8">
                        <a-form-item label="注册时间">
                            <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onCreateDateChange" />
                        </a-form-item>
                        <a-form-item label="最后登录时间">
                            <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onLoginDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="4">
                        <a-form-item label="等级范围">
                            <a-select placeholder="等级" v-model="queryParam.level">
                                <a-select-option value="">不选择等级范围</a-select-option>
                                <a-select-option value="0-30">0-30</a-select-option>
                                <a-select-option value="30-60">30-60</a-select-option>
                                <a-select-option value="60-100">60-100</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="4">
                        <a-form-item label="充值范围">
                            <a-select placeholder="充值" v-model="queryParam.recharge">
                                <a-select-option value="">不选择充值档位</a-select-option>
                                <a-select-option value="0-6">0-6</a-select-option>
                                <a-select-option value="6-30">6-30</a-select-option>
                                <a-select-option value="30-100">30-100</a-select-option>
                                <a-select-option value="100-9999">100-9999</a-select-option>
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
                <a-button type="primary" icon="download" @click="handleExportXls('玩家查询')">导出</a-button>
            </div>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
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
                <span slot="action" slot-scope="text, record">
                       <a @click="editChannelServer(record)"><a-icon type="setting" /> 游戏服</a>
                </span>
            </a-table>
        </div>

        <!-- 字典类型 -->
        <player-modal ref="playerModal"></player-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import PlayerModal from "./modules/PlayerModal";
import { getAction } from "@/api/manage";

export default {
    name: "PlayerList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        PlayerModal,
        getAction
    },
    data() {
        return {
            description: "玩家管理页面",
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
                    title: "玩家ID",
                    align: "center",
                    dataIndex: "id"
                    //scopedSlots: { customRender: "roleAttr" }
                },
                {
                    title: "玩家账号",
                    align: "center",
                    dataIndex: "account"
                },
                {
                    title: "玩家名",
                    align: "center",
                    dataIndex: "nickname"
                },
                {
                    title: "境界等级",
                    align: "center",
                    dataIndex: "level"
                },
                {
                    title: "背包大小",
                    align: "center",
                    dataIndex: "backpackSize"
                },
                {
                    title: "修为值",
                    align: "center",
                    dataIndex: "practiceValue"
                },
                {
                    title: "修炼年数",
                    align: "center",
                    dataIndex: "practiceYear"
                },
                {
                    title: "战力",
                    align: "center",
                    dataIndex: "combatPower"
                },
                {
                    title: "充值金额",
                    align: "center",
                    dataIndex: "payAmountSum"
                },
                {
                    title: "注册时间",
                    align: "center",
                    dataIndex: "registerTime"
                },
                {
                    title: "最后登录时间",
                    align: "center",
                    dataIndex: "lastLoginTime"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    width: 200,
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/player/list"
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
        onCreateDateChange: function(value, dateStr) {
            this.queryParam.createBegin = dateStr[0];
            this.queryParam.createEnd = dateStr[1];
        },
        onLoginDateChange: function(value, dateStr) {
            this.queryParam.loginBegin = dateStr[0];
            this.queryParam.loginEnd = dateStr[1];
        },
        searchQuery() {
            let param = {
                playerId: this.queryParam.playerId,
                nickName: this.queryParam.nickName,
                level: this.queryParam.level,
                recharge: this.queryParam.recharge,
                serverId: this.queryParam.serverId,
                createBegin: this.queryParam.createBegin,
                createEnd: this.queryParam.createEnd,
                loginBegin: this.queryParam.loginBegin,
                loginEnd: this.queryParam.loginEnd,
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
        editChannelServer(record) {
            this.$refs.playerModal.edit(record);
        },
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
