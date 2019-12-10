<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="名字">
                            <!-- dictCode:表名,文本字段,取值字段,查询条件, 通过 ajaxGetDictItems 查询数据库，java接口：SysDictController#getDictItems-->
                            <j-dict-select-tag v-model="queryParam.id" placeholder="请选择名字" dictCode="game_server,name,id" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="地址">
                            <a-input placeholder="地址" v-model="queryParam.host"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="创建时间">
                            <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="登录地址和端口">
                                <a-input placeholder="登录地址和端口" v-model="queryParam.loginUrl"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="状态">
                                <j-dict-select-tag v-model="queryParam.status" placeholder="请选择状态" dictCode="server_status" />
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="类型">
                                <j-dict-select-tag v-model="queryParam.type" placeholder="请选择类型" dictCode="server_type" />
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="数据库地址">
                                <a-input placeholder="数据库地址" v-model="queryParam.dbHost"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="数据库名">
                                <a-input placeholder="数据库名" v-model="queryParam.dbName"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="顺序">
                                <a-input placeholder="顺序" v-model="queryParam.position"></a-input>
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
            <!-- <a-button type="primary" icon="download" @click="handleExportXls('游戏服配置')">导出</a-button> -->
            <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>-->
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay"> <a-menu-item key="1" @click="batchDel"> <a-icon type="delete" />删除 </a-menu-item> </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /> </a-button>
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
                        <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
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
        <gameServer-modal ref="modalForm" @ok="modalFormOk"></gameServer-modal>
    </a-card>
</template>

<script>
import GameServerModal from "./modules/GameServerModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { initDictOptions, filterDictText } from "@/components/dict/JDictSelectUtil";
import Vue from "vue";
import { filterObj } from "@/utils/util";
import JInput from "@/components/jeecg/JInput";

export default {
    name: "GameServerList",
    mixins: [JeecgListMixin],
    components: {
        GameServerModal,
        JInput,
    },
    data() {
        return {
            description: "游戏服配置",
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
                    title: "名字",
                    align: "center",
                    dataIndex: "name",
                },
                {
                    title: "地址",
                    align: "center",
                    dataIndex: "host",
                },
                {
                    title: "端口",
                    align: "center",
                    dataIndex: "port",
                },
                {
                    title: "登录地址和端口",
                    align: "center",
                    dataIndex: "loginUrl",
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "status_dictText",
                },
                {
                    title: "推荐标识",
                    align: "center",
                    dataIndex: "recommend",
                    dataIndex: "recommend_dictText",
                },
                {
                    title: "出错提示信息",
                    align: "center",
                    dataIndex: "warning",
                },
                {
                    title: "后台HTTP端口",
                    align: "center",
                    dataIndex: "httpPort",
                },
                {
                    title: "顺序",
                    align: "center",
                    dataIndex: "position",
                },
                {
                    title: "类型",
                    align: "center",
                    dataIndex: "type",
                    dataIndex: "type_dictText",
                },
                {
                    title: "最小版本号",
                    align: "center",
                    dataIndex: "minVersion",
                },
                {
                    title: "最大版本号",
                    align: "center",
                    dataIndex: "maxVersion",
                },
                {
                    title: "开服时间",
                    align: "center",
                    dataIndex: "openTime",
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime",
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" },
                },
            ],
            url: {
                list: "/game/gameServer/list",
                delete: "/game/gameServer/delete",
                deleteBatch: "/game/gameServer/deleteBatch",
                // exportXlsUrl: "game/gameServer/exportXls",
                // importExcelUrl: "game/gameServer/importExcel",
            },
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domainURL"]}/${this.url.importExcelUrl}`;
        },
    },
    methods: {
        getQueryParams() {
            console.log(this.queryParam.createTimeRange);
            var param = Object.assign({}, this.queryParam, this.isorter);
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // 范围参数不传递后台
            delete param.createTimeRange;
            return filterObj(param);
        },
        onDateChange: function(value, dateString) {
            console.log(dateString[0], dateString[1]);
            this.queryParam.createTime_begin = dateString[0];
            this.queryParam.createTime_end = dateString[1];
        },
        onDateOk(value) {
            console.log(value);
        },
    },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
