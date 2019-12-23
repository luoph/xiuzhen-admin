<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="游戏名称">
                            <a-input placeholder="请输入游戏名称" v-model="queryParam.name"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="唯一标识">
                            <j-dict-select-tag v-model="queryParam.yaAppId" placeholder="唯一标识" dictCode="game_info,ya_simple_name,ya_app_id" />
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="gameAppKey">
                                <a-input placeholder="请输入gameAppKey" v-model="queryParam.yaGameKey"></a-input>
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="6" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
                            <a @click="handleToggleSearch" style="margin-left: 8px">
                                {{ toggleSearchStatus ? "收起" : "展开" }}
                                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                            </a>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>

        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
            <a-popconfirm title="刷新游戏配置" @confirm="updateGameConfig()">
                <a-button type="primary" icon="update">刷新游戏配置</a-button>
            </a-popconfirm>
            <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏信息')">导出</a-button> -->
            <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload> -->
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown>
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
                @change="handleTableChange"
            >
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
        <!-- table区域-end -->

        <!-- 表单区域 -->
        <gameInfo-modal ref="modalForm" @ok="modalFormOk"></gameInfo-modal>
    </a-card>
</template>

<script>
import GameInfoModal from "./modules/GameInfoModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction, putAction, httpAction } from "@/api/manage";
import Vue from "vue";

export default {
    name: "GameInfoList",
    mixins: [JeecgListMixin],
    components: {
        GameInfoModal
    },
    data() {
        return {
            description: "游戏信息管理页面",
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
                    title: "游戏Id",
                    align: "center",
                    dataIndex: "id"
                },
                {
                    title: "游戏名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "唯一标识",
                    align: "center",
                    dataIndex: "yaSimpleName"
                },
                {
                    title: "YA_APPID",
                    align: "center",
                    dataIndex: "yaAppId"
                },
                {
                    title: "YA_APPKEY",
                    align: "center",
                    dataIndex: "yaAppKey"
                },
                {
                    title: "gameAppKey",
                    align: "center",
                    dataIndex: "yaGameKey"
                },
                {
                    title: "帐号登录地址",
                    align: "center",
                    dataIndex: "loginUrl"
                },
                {
                    title: "游戏列表地址",
                    align: "center",
                    dataIndex: "serverUrl"
                },
                {
                    title: "公告列表地址",
                    align: "center",
                    dataIndex: "noticeUrl"
                },
                {
                    title: "描述",
                    align: "center",
                    dataIndex: "remark"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameInfo/list",
                delete: "game/gameInfo/delete",
                deleteBatch: "game/gameInfo/deleteBatch",
                updateGameConfigUrl: "game/gameInfo/updateGameConfig"
                // exportXlsUrl: "game/gameInfo/exportXls",
                // importExcelUrl: "game/gameInfo/importExcel"
            }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        updateGameConfig() {
            // 开始刷新游戏配置
            console.log("开始刷新游戏配置");
            getAction(this.url.updateGameConfigUrl).then(res => {
                if (res.success) {
                    this.$message.success("游戏配置刷新成功");
                } else {
                    this.$message.error("游戏配置刷新失败");
                }
                console.log("刷新游戏配置完成", res);
            });
        }
    }
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
