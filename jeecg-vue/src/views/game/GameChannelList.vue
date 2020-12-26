<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="4" :sm="8">
                        <a-form-item label="渠道名称">
                            <j-input placeholder="请输入渠道名称" v-model="queryParam.name"></j-input>
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
                                <a-input-number placeholder="请输入公告id" v-model="queryParam.noticeId" style="width: 100%"></a-input-number>
                            </a-form-item>
                        </a-col>
                        <a-col :md="4" :sm="8">
                            <a-form-item label="大渠道描述">
                                <j-input placeholder="请输入大渠道描述" v-model="queryParam.remark"></j-input>
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
            <a-button @click="updateAllServer" type="primary" icon="sync">刷新服务器列表</a-button>
            <a-button @click="updateIpWhitelist" type="primary" icon="sync">刷新IP白名单配置</a-button>

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
                    <a @click="editChannelServer(record)">区服列表</a>
                    <a-divider type="vertical" />
                    <a @click="updateChannelServer(record)">刷新区服</a>
                    <a-divider type="vertical" />
                    <a @click="editChannelNotice(record)">编辑公告</a>
                    <a-divider type="vertical" />
                    <a @click="viewChannelNotice(record)">预览公告</a>
                    <a-divider type="vertical" />
                    <a @click="refreshChannelNotice(record)">刷新公告</a>
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
                    <a-tag v-for="tag in text.split(',')" :key="tag" color="blue">{{ tag }}</a-tag>
                </span>
            </a-table>
        </div>
        <!-- table区域-end -->

        <game-channel-modal ref="modalForm" @ok="modalFormOk"></game-channel-modal>
        <!-- 编辑公告 -->
        <game-notice-modal ref="noticeModal" @ok="modalFormOk"></game-notice-modal>
        <!-- html预览 -->
        <game-html-preview-modal ref="htmlModal" @ok="modalFormOk"></game-html-preview-modal>
        <game-channel-server-list ref="channelServerList"></game-channel-server-list>
    </a-card>
</template>

<script>
import { filterObj } from "@/utils/util";
import JInput from "@/components/jeecg/JInput";
import GameChannelModal from "./modules/GameChannelModal";
import GameNoticeModal from "./modules/GameNoticeModal";
import GameHtmlPreviewModal from "./modules/GameHtmlPreviewModal";
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
    components: { JInput, GameChannelModal, GameNoticeModal, GameHtmlPreviewModal, GameChannelServerList },
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
                // 刷新所有渠道区服
                updateAllServerUrl: "game/gameChannel/updateAllServer",
                // 刷新渠道区服
                updateChannelServerUrl: "game/gameChannel/updateChannelServer",
                updateIpWhitelistUrl: "game/gameChannel/updateIpWhitelist",
                // exportXlsUrl: "game/gameChannel/exportXls",
                // importExcelUrl: "game/gameChannel/importExcel",
                // 游戏列表
                gameInfoListUrl: "game/gameInfo/list",
                // 公告id
                noticeUrl: "game/gameNotice/queryById",
                // 刷新渠道公告
                noticeRefresh: "game/gameNotice/refreshById"
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
        // 编辑渠道公告
        editChannelNotice(record) {
            let that = this;
            getAction(that.url.noticeUrl, { id: record.noticeId }).then(res => {
                if (res.success && res.result) {
                    that.$refs.noticeModal.edit(res.result);
                } else {
                    that.$message.error("公告不存在，请检查公告设置");
                }
            });
        },
        // 预览渠道公告
        viewChannelNotice(record) {
            let that = this;
            getAction(that.url.noticeUrl, { id: record.noticeId }).then(res => {
                if (res.success && res.result) {
                    that.$refs.htmlModal.title = "公告预览";
                    that.$refs.htmlModal.edit(res.result.content);
                } else {
                    that.$message.error("公告不存在，请检查公告设置");
                }
            });
        },
        // 刷新渠道公告
        refreshChannelNotice(record) {
            let that = this;
            this.$confirm({
                title: "是否刷新渠道公告？",
                content: "点击刷新渠道公告",
                onOk: function() {
                    getAction(that.url.noticeRefresh, { id: record.noticeId }).then(res => {
                        if (res.success) {
                            that.$message.success("公告刷新成功");
                        } else {
                            that.$message.error("公告刷新失败");
                        }
                    });
                }
            });
        },
        updateChannelServer(record) {
            // 刷新服务器列表
            let that = this;
            this.$confirm({
                title: "是否刷新区服列表？",
                content: "点击确定刷新区服列表",
                onOk: function() {
                    getAction(that.url.updateChannelServerUrl, { id: record.id }).then(res => {
                        if (res.success) {
                            that.$message.success("区服刷新成功");
                        } else {
                            that.$message.error("区服刷新失败");
                        }
                    });
                }
            });
        },
        updateAllServer() {
            // 刷新服务器列表
            let that = this;
            this.$confirm({
                title: "是否刷新所有区服列表？",
                content: "点击确定刷新所有区服列表",
                onOk: function() {
                    getAction(that.url.updateAllServerUrl).then(res => {
                        if (res.success) {
                            that.$message.success("所有区服列表刷新成功");
                        } else {
                            that.$message.error("所有区服列表刷新失败");
                        }
                    });
                }
            });
        },
        updateIpWhitelist() {
            // 刷新IP白名单配置
            let that = this;
            this.$confirm({
                title: "是否IP白名单配置？",
                content: "点击确定IP白名单配置",
                onOk: function() {
                    getAction(that.url.updateIpWhitelistUrl).then(res => {
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
