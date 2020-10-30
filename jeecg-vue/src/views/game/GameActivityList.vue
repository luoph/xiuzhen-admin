<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="4" :sm="8">
                        <a-form-item label="唯一标识">
                            <a-input placeholder="请输入唯一标识" v-model="queryParam.activity"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="活动名称">
                            <a-input placeholder="请输入活动名称" v-model="queryParam.name"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="活动状态">
                            <a-select placeholder="活动状态" v-model="queryParam.status">
                                <a-select-option :value="1">有效</a-select-option>
                                <a-select-option :value="0">无效</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="4" :sm="8">
                            <a-form-item label="活动标语">
                                <a-input placeholder="请输入活动标语" v-model="queryParam.slogan"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="16">
                            <a-form-item label="开始时间">
                                <a-range-picker v-model="queryParam.startTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onStartTimeChange" />
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="16">
                            <a-form-item label="结束时间">
                                <a-range-picker v-model="queryParam.endTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onEndTimeChange" />
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="4" :sm="8">
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
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('活动')">导出</a-button>
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

        <game-activity-modal ref="modalForm" @ok="modalFormOk"></game-activity-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameActivityModal from "./modules/GameActivityModal";
import JDate from "@/components/jeecg/JDate.vue";
import { filterObj } from "@/utils/util";

export default {
    name: "GameActivityList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameActivityModal
    },
    data() {
        return {
            description: "活动管理页面",
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
                    title: "活动Id",
                    align: "center",
                    dataIndex: "id"
                },
                {
                    title: "活动名称",
                    align: "left",
                    dataIndex: "name"
                },
                {
                    title: "唯一标识",
                    align: "center",
                    dataIndex: "activity"
                },
                {
                    title: "活动标语",
                    align: "center",
                    dataIndex: "slogan"
                },
                {
                    title: "活动图标",
                    align: "center",
                    dataIndex: "icon"
                },
                {
                    title: "活动状态",
                    align: "center",
                    dataIndex: "status",
                    customRender: value => {
                        let re = "--";
                        if (value === 0) {
                            re = "无效";
                        } else if (value === 1) {
                            re = "有效";
                        }
                        return re;
                    }
                },
                {
                    title: "开始时的传闻id",
                    align: "center",
                    dataIndex: "startRumor"
                },
                {
                    title: "结束时的传闻id",
                    align: "center",
                    dataIndex: "endRumor"
                },
                {
                    title: "图标显示类型",
                    align: "left",
                    dataIndex: "iconDisplay",
                    customRender: value => {
                        let re = "--";
                        if (value === 0) {
                            re = "图标常驻";
                        } else if (value === 1) {
                            re = "预告时才显示，平时隐藏";
                        }
                        return re;
                    }
                },
                {
                    title: "提前预告时间(秒)",
                    align: "center",
                    dataIndex: "noticeTime"
                },
                {
                    title: "跑马灯显示周期(秒)",
                    align: "center",
                    dataIndex: "noticePeriod"
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
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameActivity/list",
                delete: "game/gameActivity/delete",
                deleteBatch: "game/gameActivity/deleteBatch",
                exportXlsUrl: "game/gameActivity/exportXls",
                importExcelUrl: "game/gameActivity/importExcel"
            },
            dictOptions: {}
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {},
        getQueryParams() {
            console.log(this.queryParam.createTimeRange);
            var param = Object.assign({}, this.queryParam, this.isorter);
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // 范围参数不传递后台
            delete param.startTimeRange;
            delete param.endTimeRange;
            return filterObj(param);
        },
        onStartTimeChange: function(value, dateString) {
            console.log(dateString[0], dateString[1]);
            this.queryParam.startTime_begin = dateString[0];
            this.queryParam.startTime_end = dateString[1];
        },
        onEndTimeChange: function(value, dateString) {
            console.log(dateString[0], dateString[1]);
            this.queryParam.endTime_begin = dateString[0];
            this.queryParam.endTime_end = dateString[1];
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
