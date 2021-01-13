<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="问卷调查地址">
                            <j-input placeholder="请输入问卷调查地址" v-model="queryParam.url"></j-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="状态">
                            <a-select placeholder="请选择状态" v-model="queryParam.status" initialValue="0">
                                <a-select-option :value="0">关闭</a-select-option>
                                <a-select-option :value="1">开启</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="8" :sm="16">
                            <a-form-item label="活动开始时间">
                                <a-range-picker v-model="queryParam.startTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onStartTimeChange" />
                            </a-form-item>
                        </a-col>
                        <a-col :md="8" :sm="16">
                            <a-form-item label="活动结束时间">
                                <a-range-picker v-model="queryParam.endTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onEndTimeChange" />
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="备注">
                                <a-input placeholder="请输入备注" v-model="queryParam.remark"></a-input>
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
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('问卷调查')">导出</a-button>
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
                :loading="confirmLoading"
                :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
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
                    <a @click="handleSync(record)">同步到区服</a>
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

        <game-questionnaire-modal ref="modalForm" @ok="modalFormOk"></game-questionnaire-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameQuestionnaireModal from "./modules/GameQuestionnaireModal";
import JDate from "@/components/jeecg/JDate.vue";
import { filterObj } from "@/utils/util";
import { getAction } from "@/api/manage";

export default {
    name: "GameQuestionnaireList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameQuestionnaireModal
    },
    data() {
        return {
            description: "问卷调查管理页面",
            confirmLoading: false,
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
                    title: "服务器id",
                    align: "center",
                    dataIndex: "serverIds"
                },
                {
                    title: "问卷调查地址",
                    align: "center",
                    dataIndex: "url"
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "status",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "关闭";
                        } else if (value === 1) {
                            text = "开启";
                        }
                        return text;
                    }
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
                    title: "备注",
                    align: "center",
                    dataIndex: "remark"
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
                list: "game/questionnaire/list",
                sync: "game/questionnaire/sync",
                delete: "game/questionnaire/delete",
                duplicate: "game/questionnaire/duplicate",
                deleteBatch: "game/questionnaire/deleteBatch",
                exportXlsUrl: "game/questionnaire/exportXls",
                importExcelUrl: "game/questionnaire/importExcel"
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
        },
        handleDuplicate: function(record) {
            const that = this;
            that.confirmLoading = true;
            getAction(that.url.duplicate, { id: record.id })
                .then(res => {
                    if (res.success) {
                        that.$message.success("复制成功");
                    } else {
                        that.$message.error("复制失败");
                    }
                })
                .finally(() => {
                    that.confirmLoading = false;
                });
        },
        handleSync: function(record) {
            const that = this;
            that.confirmLoading = true;
            getAction(that.url.sync, { id: record.id })
                .then(res => {
                    if (res.success) {
                        that.$message.success("同步成功");
                    } else {
                        that.$message.error("同步失败");
                    }
                })
                .finally(() => {
                    that.confirmLoading = false;
                });
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
