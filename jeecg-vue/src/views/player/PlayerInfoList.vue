<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="区服Id">
                            <server-select @select="change"></server-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="玩家id">
                            <a-input placeholder="请输入玩家id" v-model="queryParam.id"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="角色昵称">
                            <a-input placeholder="请输入角色昵称" v-model="queryParam.nickname"></a-input>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="性别">
                                <a-input placeholder="请输入性别" v-model="queryParam.sex"></a-input>
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
            <!-- <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button> -->
            <a-button type="primary" icon="download" @click="handleExportXls('玩家信息')">导出</a-button>
            <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
            <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown> -->
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>

            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"
            >
                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </span>
            </a-table>
        </div>

        <playerInfo-modal ref="modalForm" @ok="modalFormOk"></playerInfo-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "@/api/manage";
import PlayerInfoModal from "./modules/PlayerInfoModal";
import ServerSelect from "@/components/gameserver/ServerSelect";

export default {
    name: "PlayerInfoList",
    mixins: [JeecgListMixin],
    components: {
        PlayerInfoModal,
        getAction,
        ServerSelect
    },
    data() {
        return {
            description: "玩家信息管理页面",
            queryParam: {
                serverId: ""
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
                    title: "玩家id",
                    align: "center",
                    dataIndex: "id"
                },
                {
                    title: "角色昵称",
                    align: "center",
                    dataIndex: "nickname"
                },
                {
                    title: "等级",
                    align: "center",
                    dataIndex: "level"
                },
                {
                    title: "境界",
                    align: "center",
                    dataIndex: "realm"
                },
                {
                    title: "背包大小",
                    align: "center",
                    dataIndex: "backpackSize"
                },
                {
                    title: "背包等级",
                    align: "center",
                    dataIndex: "backpackLevel"
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
                    title: "修为加持状态",
                    align: "center",
                    dataIndex: "practiceState"
                },
                {
                    title: "渡劫增加成功率",
                    align: "center",
                    dataIndex: "successRate"
                },
                {
                    title: "登录IP",
                    align: "center",
                    dataIndex: "loginIp"
                },
                {
                    title: "登录时间",
                    align: "center",
                    dataIndex: "loginTime"
                },
                {
                    title: "是否跳过战斗动画",
                    align: "center",
                    dataIndex: "skipCartoon"
                },
                // {
                //     title: "操作",
                //     dataIndex: "action",
                //     align: "center",
                //     scopedSlots: { customRender: "action" }
                // }
            ],
            url: {
                list: "player/playerInfo/list",
                delete: "player/playerInfo/delete",
                deleteBatch: "player/playerInfo/deleteBatch",
                exportXlsUrl: "player/playerInfo/exportXls",
                importExcelUrl: "player/playerInfo/importExcel",
                serverListUrl: "game/gameServer/all"
            },
            dictOptions: {}
        };
    },
    created() {},
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {},
        change(serverId) {
            this.queryParam.serverId = serverId;
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
