<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="4" :sm="8">
                        <a-form-item label="渠道名称">
                            <a-input placeholder="请输入渠道名称" v-model="queryParam.name"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="唯一标识">
                            <j-dict-select-tag v-model="queryParam.simpleName" placeholder="请选择唯一标识" dictCode="game_channel,simple_name,simple_name" />
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="4" :sm="8">
                            <a-form-item label="公告id">
                                <a-input-number placeholder="请输入公告id" v-model="queryParam.noticeId"></a-input-number>
                            </a-form-item>
                        </a-col>
                        <a-col :md="4" :sm="8">
                            <a-form-item label="大渠道描述">
                                <a-input placeholder="请输入大渠道描述" v-model="queryParam.remark"></a-input>
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="4" :sm="8">
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
            <a-button @click="updateServerConfig" type="primary" icon="sync">刷新服务器列表</a-button>
            <a-button @click="updateIpWhitelistConfig" type="primary" icon="sync">刷新IP白名单配置</a-button>

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
                <span slot="ipTags" slot-scope="text, record">
                    <a-tag v-for="tag in text.split(',')" :key="tag" color="green">{{ tag }}</a-tag>
                </span>
            </a-table>
        </div>
        <!-- table区域-end -->

        <GameChannelModal ref="modalForm" @ok="modalFormOk"></GameChannelModal>
        <GameChannelServerList ref="channelServerList"></GameChannelServerList>
    </a-card>
</template>

<script>
import { filterObj } from "@/utils/util";
import GameChannelModal from "./modules/GameChannelModal";
import GameChannelServerList from "./GameChannelServerList";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "@/api/manage";

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
                    width: 80,
                    dataIndex: "id"
                },
                {
                    title: "渠道名称",
                    align: "center",
                    width: 100,
                    dataIndex: "name"
                },
                {
                    title: "唯一标识",
                    align: "center",
                    width: 100,
                    dataIndex: "simpleName"
                },
                {
                    title: "游戏编号",
                    align: "center",
                    width: 120,
                    dataIndex: "gameId",
                    customRender: text => {
                        return filterGameIdText(this.gameList, text);
                    }
                },
                {
                    title: "公告id",
                    align: "center",
                    width: 80,
                    dataIndex: "noticeId"
                },
                {
                    title: "版本号",
                    align: "center",
                    width: 80,
                    dataIndex: "versionCode"
                },
                {
                    title: "版本名",
                    align: "center",
                    width: 120,
                    dataIndex: "versionName"
                },
                {
                    title: "IP白名单",
                    align: "left",
                    width: 280,
                    dataIndex: "ipWhitelist",
                    scopedSlots: { customRender: "ipTags" }
                },
                {
                    title: "大渠道描述",
                    align: "center",
                    width: 200,
                    dataIndex: "remark"
                },
                {
                    title: "版本更新时间",
                    align: "center",
                    width: 120,
                    dataIndex: "versionUpdateTime"
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
            let that = this;
            this.$confirm({
                title: "是否刷新区服列表？",
                content: "点击确定刷新区服列表",
                onOk: function() {
                    getAction(that.url.updateServerConfigUrl).then(res => {
                        if (res.success) {
                            that.$message.success("刷新区服列表刷新成功");
                        } else {
                            that.$message.error("刷新区服列表刷新失败");
                        }
                    });
                }
            });
        },
        updateIpWhitelistConfig() {
            // 刷新IP白名单配置
            let that = this;
            this.$confirm({
                title: "是否IP白名单配置？",
                content: "点击确定IP白名单配置",
                onOk: function() {
                    getAction(that.url.updateIpWhitelistConfigUrl).then(res => {
                        if (res.success) {
                            that.$message.success("IP白名单配置刷新成功");
                        } else {
                            that.$message.error("IP白名单配置刷新失败");
                        }
                    });
                }
            });
        }
    }
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
