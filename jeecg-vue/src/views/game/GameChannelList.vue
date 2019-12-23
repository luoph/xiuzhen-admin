<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="渠道名称">
                            <a-input placeholder="请输入渠道名称" v-model="queryParam.name"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="唯一标识">
                            <j-dict-select-tag v-model="queryParam.simpleName" placeholder="请选择唯一标识" dictCode="game_channel,simple_name,simple_name" />
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="排序字段">
                                <a-input placeholder="请输入排序字段" v-model="queryParam.position"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="公告id">
                                <a-input placeholder="请输入公告id" v-model="queryParam.noticeId"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="大渠道描述">
                                <a-input placeholder="请输入大渠道描述" v-model="queryParam.remark"></a-input>
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
            <a-popconfirm title="刷新服务器列表" @confirm="updateServerConfig()">
                <a-button type="primary" icon="update">刷新服务器列表</a-button>
            </a-popconfirm>
            <a-popconfirm title="刷新IP白名单配置" @confirm="updateIpWhitelistConfig()">
                <a-button type="primary" icon="update">刷新IP白名单配置</a-button>
            </a-popconfirm>
            <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏渠道')">导出</a-button> -->
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
            <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
                >项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>
            :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" -->

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
                    <a @click="editChannelServer(record)"><a-icon type="setting" /> 游戏服</a>
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
        <gameChannel-modal ref="modalForm" @ok="modalFormOk"></gameChannel-modal>
        <!-- 字典类型 -->
        <gameChannel-serverList ref="channelServerList"></gameChannel-serverList>
    </a-card>
</template>

<script>
import { filterObj } from "@/utils/util";
import GameChannelModal from "./modules/GameChannelModal";
import GameChannelServerList from "./GameChannelServerList";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction, putAction, httpAction } from "@/api/manage";
import Vue from "vue";

function filterGameIdText(options, text) {
    if (options instanceof Array) {
        for (let game of options) {
            if (text === game.id) {
                return game.name + "(" + game.id + ")";
            }
        }
    }
    return text;
}

export default {
    name: "GameChannelList",
    mixins: [JeecgListMixin],
    components: { GameChannelModal, GameChannelServerList },
    data() {
        return {
            description: "游戏渠道管理页面",
            gameList: [],
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
                    title: "渠道id",
                    align: "center",
                    dataIndex: "id"
                },
                {
                    title: "渠道名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "唯一标识",
                    align: "center",
                    dataIndex: "simpleName"
                },
                {
                    title: "游戏编号",
                    align: "center",
                    dataIndex: "gameId",
                    customRender: text => {
                        return filterGameIdText(this.gameList, text);
                    }
                },
                {
                    title: "排序字段",
                    align: "center",
                    dataIndex: "position"
                },
                {
                    title: "公告id",
                    align: "center",
                    dataIndex: "noticeId"
                },
                {
                    title: "版本号",
                    align: "center",
                    dataIndex: "versionCode"
                },
                {
                    title: "版本名",
                    align: "center",
                    dataIndex: "versionName"
                },
                {
                    title: "版本更新时间",
                    align: "center",
                    dataIndex: "versionUpdateTime"
                },
                {
                    title: "IP白名单",
                    align: "center",
                    dataIndex: "ipWhitelist"
                },
                {
                    title: "大渠道描述",
                    align: "center",
                    dataIndex: "remark"
                },
                {
                    title: "扩展字段",
                    align: "center",
                    dataIndex: "extra"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameChannel/list",
                delete: "game/gameChannel/delete",
                deleteBatch: "game/gameChannel/deleteBatch",
                updateServerConfigUrl: "game/gameChannel/updateServerConfig",
                updateIpWhitelistConfigUrl: "game/gameChannel/updateIpWhitelist",
                // exportXlsUrl: "game/gameChannel/exportXls",
                // importExcelUrl: "game/gameChannel/importExcel",
                // 游戏列表
                gameInfoListUrl: "game/gameInfo/list"
            }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    created() {
        this.queryGameInfoList();
    },
    methods: {
        getQueryParams() {
            var param = Object.assign({}, this.queryParam, this.isorter);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            return filterObj(param);
        },
        queryGameInfoList() {
            let that = this;
            getAction(that.url.gameInfoListUrl).then(res => {
                if (res.success) {
                    if (res.result instanceof Array) {
                        this.gameList = res.result;
                    } else if (res.result.records instanceof Array) {
                        this.gameList = res.result.records;
                    }
                } else {
                    this.gameList = [];
                }
            });
        },
        // 编辑游戏服数据
        editChannelServer(record) {
            this.$refs.channelServerList.edit(record);
        },
        updateServerConfig() {
            // 刷新服务器列表
            console.log("开始刷新服务器列表");
            getAction(this.url.updateServerConfigUrl).then(res => {
                if (res.success) {
                    this.$message.success("服务器列表刷新成功");
                } else {
                    this.$message.error("服务器列表刷新失败");
                }
                console.log("刷新服务器列表完成", res);
            });
        },
        updateIpWhitelistConfig() {
            // 刷新IP白名单配置
            console.log("开始刷新IP白名单配置");
            getAction(this.url.updateIpWhitelistConfigUrl).then(res => {
                if (res.success) {
                    this.$message.success("IP白名单配置刷新成功");
                } else {
                    this.$message.error("IP白名单配置刷新失败");
                }
                console.log("刷新IP白名单配置完成", res);
            });
        }
    }
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
