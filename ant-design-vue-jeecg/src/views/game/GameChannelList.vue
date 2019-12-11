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
                            <a-input placeholder="请输入唯一标识" v-model="queryParam.simpleName"></a-input>
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
                            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                            <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
            <a-button type="primary" icon="download" @click="handleExportXls('游戏渠道配置')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown>
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
        <gameChannel-modal ref="modalForm" @ok="modalFormOk"></gameChannel-modal>
    </a-card>
</template>

<script>
import GameChannelModal from "./modules/GameChannelModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";

export default {
    name: "GameChannelList",
    mixins: [JeecgListMixin],
    components: {
        GameChannelModal,
    },
    data() {
        return {
            description: "游戏渠道配置管理页面",
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
                    },
                },
                {
                    title: "渠道名称",
                    align: "center",
                    dataIndex: "name",
                },
                {
                    title: "唯一标识",
                    align: "center",
                    dataIndex: "simpleName",
                },
                {
                    title: "排序字段",
                    align: "center",
                    dataIndex: "position",
                },
                {
                    title: "公告id",
                    align: "center",
                    dataIndex: "noticeId",
                },
                {
                    title: "大渠道描述",
                    align: "center",
                    dataIndex: "remark",
                },
                {
                    title: "扩展字段",
                    align: "center",
                    dataIndex: "extra",
                },
                {
                    title: "游戏编号",
                    align: "center",
                    dataIndex: "gameId",
                },
                {
                    title: "分组",
                    align: "center",
                    dataIndex: "groupName",
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" },
                },
            ],
            url: {
                list: "/game/gameChannel/list",
                delete: "/game/gameChannel/delete",
                deleteBatch: "/game/gameChannel/deleteBatch",
                exportXlsUrl: "game/gameChannel/exportXls",
                importExcelUrl: "game/gameChannel/importExcel",
            },
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        },
    },
    methods: {},
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
