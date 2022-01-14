<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <!-- <a-col :md="6" :sm="8">
                        <a-form-item label="活动类型">
                            <a-select placeholder="选择活动类型" v-model="queryParam.type" default-value="1">
                                <a-select-option :value="1">1-节日活动</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col> -->
                    <a-col :md="6" :sm="8">
                        <a-form-item label="活动展示名称">
                            <j-input placeholder="活动展示名称" v-model="queryParam.showName"></j-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="自动开启">
                            <a-select placeholder="请选择自动开启" v-model="queryParam.autoOpen" initialValue="1">
                                <a-select-option :value="1">开启</a-select-option>
                                <a-select-option :value="0">关闭</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="活动状态">
                            <a-select placeholder="请选择状态" v-model="queryParam.status" initialValue="1">
                                <a-select-option :value="1">有效</a-select-option>
                                <a-select-option :value="0">无效</a-select-option>
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
            <a-button type="primary" icon="download" @click="handleExportXls('活动配置')">导出</a-button>
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
                    <img v-else :src="getImgView(text)" alt="图片不存在" class="image" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>
                <span slot="serverIdSlot" slot-scope="text, record">
                    <a-tag v-if="!text" color="red">未设置</a-tag>
                    <a-tag v-else v-for="tag in text.split(',').sort()" :key="tag" color="blue">{{ tag }}</a-tag>
                </span>
                <span slot="timeSlot" slot-scope="text, record">
                    <div v-if="record.timeType == 1">
                        <a-tag color="blue">{{ record.startTime }}</a-tag>
                        <a-tag color="blue">{{ record.endTime }}</a-tag>
                    </div>
                    <div v-if="record.timeType == 2">
                        <a-tag color="green">开服第{{ record.startDay }}天</a-tag>
                        <a-tag color="green">持续{{ record.duration }}天</a-tag>
                    </div>
                </span>
                <template slot="largeText" slot-scope="text">
                    <div class="large-text-container">
                        <span class="large-text">{{ text }}</span>
                    </div>
                </template>
                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">活动信息</a>
                    <a-divider type="vertical" />
                    <a @click="handleServerList(record)">活动状态</a>
                    <a-divider type="vertical" />
                    <a @click="handleDuplicate(record)">复制</a>
                    <a-divider type="vertical" />
                    <a @click="handleSyncCampaign(record)">同步到区服</a>
                    <!-- <a-dropdown>
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

        <game-campaign-modal ref="modalForm" @ok="modalFormOk"></game-campaign-modal>
        <game-campaign-server-list ref="serverListModal"></game-campaign-server-list>
    </a-card>
</template>

<script>
import JInput from "@/components/jeecg/JInput";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "@/api/manage";
import { filterObj } from "@/utils/util";
import GameCampaignModal from "./modules/GameCampaignModal";
import GameCampaignServerList from "./modules/GameCampaignServerList";
import GameCampaignTabList from "./GameCampaignTabList";
import JDate from "@/components/jeecg/JDate.vue";

export default {
    name: "GameCampaignList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        JInput,
        GameCampaignModal,
        GameCampaignServerList,
        GameCampaignTabList
    },
    data() {
        return {
            description: "活动信息",
            isorter: {
                column: "id",
                order: "desc"
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
                    title: "活动Id",
                    align: "center",
                    width: 100,
                    dataIndex: "id"
                },
                {
                    title: "活动名称",
                    align: "center",
                    width: 120,
                    dataIndex: "name"
                },
                {
                    title: "活动标语（描述）",
                    align: "left",
                    width: 120,
                    dataIndex: "description"
                },
                {
                    title: "活动图标",
                    align: "center",
                    dataIndex: "icon",
                    width: 80,
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "活动宣传图",
                    align: "center",
                    dataIndex: "banner",
                    width: 300,
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "活动状态",
                    align: "center",
                    dataIndex: "status",
                    width: 80,
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
                    title: "区服id",
                    align: "center",
                    width: 100,
                    dataIndex: "serverIds",
                    scopedSlots: { customRender: "serverIdSlot" }
                },
                {
                    title: "自动开启",
                    align: "center",
                    dataIndex: "autoOpen",
                    width: 80,
                    customRender: value => {
                        let re = "--";
                        if (value === 0) {
                            re = "关闭";
                        } else if (value === 1) {
                            re = "开启";
                        }
                        return re;
                    }
                },
                {
                    title: "时间类型",
                    align: "center",
                    width: 120,
                    dataIndex: "timeType",
                    customRender: value => {
                        let text = "--";
                        if (value === 1) {
                            text = "1-时间范围";
                        } else if (value === 2) {
                            text = "2-开服第N天";
                        }
                        return text;
                    }
                },
                {
                    title: "活动时间",
                    align: "center",
                    width: 80,
                    dataIndex: "startDay",
                    scopedSlots: { customRender: "timeSlot" }
                },
                // {
                //     title: "开始天数",
                //     align: "center",
                //     width: 80,
                //     dataIndex: "startDay"
                // },
                // {
                //     title: "持续天数",
                //     align: "center",
                //     width: 80,
                //     dataIndex: "duration"
                // },
                // {
                //     title: "活动开始时间",
                //     align: "center",
                //     width: 120,
                //     dataIndex: "startTime"
                // },
                // {
                //     title: "活动结束时间",
                //     align: "center",
                //     width: 120,
                //     dataIndex: "endTime"
                // },
                {
                    title: "创建时间",
                    align: "center",
                    width: 120,
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
                list: "game/gameCampaign/list",
                delete: "game/gameCampaign/delete",
                sync: "game/gameCampaign/sync",
                duplicate: "game/gameCampaign/duplicate",
                deleteBatch: "game/gameCampaign/deleteBatch",
                exportXlsUrl: "game/gameCampaign/exportXls",
                importExcelUrl: "game/gameCampaign/importExcel"
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
        handleEdit: function(record) {
            this.$refs.modalForm.edit(record);
            this.$refs.modalForm.title = "活动信息";
            this.$refs.modalForm.disableSubmit = false;
        },
        handleServerList: function(record) {
            this.$refs.serverListModal.edit(record);
            this.$refs.serverListModal.title = "活动信息";
        },
        handleSyncCampaign: function(record) {
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
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return `${window._CONFIG["domainURL"]}/${text}`;
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
                    that.loadData();
                });
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";

.image {
    width: 100%;
    height: 100px;
    object-fit: scale-down;
}

.large-text-container {
    display: flex;
    overflow-x: hidden;
    overflow-y: auto;
    max-height: 200px;
}

.large-text {
    white-space: normal;
    word-break: break-word;
}
</style>
