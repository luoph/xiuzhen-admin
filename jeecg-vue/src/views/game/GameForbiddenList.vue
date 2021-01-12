<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <!-- <a-col :md="6" :sm="8">
                <a-form-item label="服务器id">
                <a-input placeholder="请输入服务器id" v-model="queryParam.serverId"></a-input>
            </a-form-item>
                </a-col> -->
                    <a-col :md="10" :sm="8">
                        <!--@ = v-on:数据绑定 不是事件-->
                        <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
                    </a-col>

                    <a-col :md="6" :sm="8">
                        <a-form-item  label="封禁类型">
                            <a-select placeholder="请选择封禁类型" @change="typeChange">
                                <a-select-option :value="''">不选择</a-select-option>
                                <a-select-option :value="'1'">登录封禁</a-select-option>
                                <a-select-option :value="'2'">聊天封禁</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="封禁值">
                                <a-input placeholder="请输入对应 ban_type 的值" v-model="queryParam.banValue"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="封禁时间">
                            <a-select placeholder="封禁时间" @change="isForeverChange">
                                <a-select-option :value="''">不选择</a-select-option>
                                <a-select-option :value="'0'">临时封禁</a-select-option>
                                <a-select-option :value="'1'">永久封禁</a-select-option>
                            </a-select>
                        </a-form-item>
                        </a-col>
                        <a-col :md="10" :sm="8">
                            <a-form-item label="创建日期">
                                <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="6" :sm="8">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchQuery">刷新数据</a-button>
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
            <a-button type="primary" icon="download" @click="handleExportXls('game_forbidden')">导出</a-button>
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
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
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
                :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"
            >
                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">查看详情</a>
                    <!-- <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a>删除</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown> -->
                </span>
            </a-table>
        </div>

        <gameForbidden-modal ref="modalForm" @ok="modalFormOk"></gameForbidden-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameForbiddenModal from "./modules/GameForbiddenModal";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import { getAction } from "@/api/manage";

export default {
    name: "GameForbiddenList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameForbiddenModal,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            description: "game_forbidden管理页面",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "服务器id",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "封禁类型",
                    align: "center",
                    dataIndex: "type"
                },
                {
                    title: "封禁值类型",
                    align: "center",
                    dataIndex: "banKey"
                },
                {
                    title: "封禁值",
                    align: "center",
                    dataIndex: "banValue"
                },
                {
                    title: "封禁原因",
                    align: "center",
                    dataIndex: "reason"
                },
                {
                    title: "封禁时间",
                    align: "center",
                    dataIndex: "isForever"
                },
                {
                    title: "开始时间",
                    align: "center",
                    dataIndex: "startTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "结束时间",
                    align: "center",
                    dataIndex: "endTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "更新时间",
                    align: "center",
                    dataIndex: "updateTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "操作人",
                    align: "center",
                    dataIndex: "createBy"
                },
                {
                    title: "操作人",
                    align: "center",
                    dataIndex: "updateBy"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameForbidden/list",
                delete: "game/gameForbidden/delete",
                deleteBatch: "game/gameForbidden/deleteBatch",
                exportXlsUrl: "game/gameForbidden/exportXls",
                importExcelUrl: "game/gameForbidden/importExcel"
            },
            dictOptions: {}
        };
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {},
        typeChange: function (type){
            this.queryParam.type = type;
        },
        isForeverChange: function (isForever){
            this.queryParam.isForever = isForever;
        },
        onSelectChannel: function (channelId) {
            this.queryParam.channelId = channelId;
        },
        onSelectServer: function (serverId) {
            this.queryParam.serverId = serverId;
        },
        onDateChange: function (value, dateStr) {
            this.queryParam.rangeDateBegin = dateStr[0];
            this.queryParam.rangeDateEnd = dateStr[1];
        },
        searchQuery() {
            let param = {
                days: this.queryParam.days,
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                type: this.queryParam.type,
                banValue: this.queryParam.banValue,
                isForever: this.queryParam.isForever,
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
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>