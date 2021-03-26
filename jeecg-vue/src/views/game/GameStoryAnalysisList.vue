<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="12" :sm="16">
                        <!-- <a-form-item label="渠道">
                         <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.channel_begin"></a-input>
                         <span class="query-group-split-cust"></span>
                         <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.channel_end"></a-input>
                     </a-form-item>
                     </a-col>
                         <a-col :md="12" :sm="16">
                         <a-form-item label="服务器ID">
                         <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.serverId_begin"></a-input>
                         <span class="query-group-split-cust"></span>
                         <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.serverId_end"></a-input>
                     </a-form-item>-->

                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="分析日期">
                                <j-date placeholder="请选择分析日期" v-model="queryParam.analysisDate"></j-date>
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="6" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px"
                                      @click="searchReset">重置</a-button>
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
            <!--<a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>-->
            <a-button type="primary" icon="download" @click="handleExportXls('剧情分析')">导出</a-button>
            <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                 <a-button type="primary" icon="import">导入</a-button>
             </a-upload>-->
            <!--<a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down"/></a-button>
            </a-dropdown>-->
        </div>

        <!-- table区域-begin -->
        <div>
            <<!--div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>-->

            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="level"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"
                :scroll="tableScroll"
            >
               <!-- <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
                         style="max-width:80px;font-size: 12px;font-style: italic;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small"
                              @click="uploadFile(text)"> 下载
                    </a-button>
                </template>-->

                <!--<span slot="action" slot-scope="text, record">
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
                </span>-->
            </a-table>
        </div>

    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";

export default {
    name: "GameStoryAnalysisList",
    mixins: [JeecgListMixin],
    components: {
        JDate
    },
    data() {
        return {
            description: "剧情分析管理页面",
            queryParam: {
                serverId: null,
                analysisDate: null
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
                // {
                //     title: "渠道",
                //     align: "center",
                //     dataIndex: "channel"
                // },
                // {
                //     title: "服务器ID",
                //     align: "center",
                //     dataIndex: "serverId"
                // },
                {
                    title: "分析日期",
                    align: "center",
                    dataIndex: "analysisDate",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "剧情小关卡",
                    align: "center",
                    dataIndex: "storyCheckpoint"
                },
                {
                    title: "停留活跃人数",
                    align: "center",
                    dataIndex: "stayLiveNum"
                },
                {
                    title: "活跃占比",
                    align: "center",
                    dataIndex: "liveRate"
                },
                {
                    title: "停留流失人数",
                    align: "center",
                    dataIndex: "stayLeaveNum"
                },
                {
                    title: "流失占比",
                    align: "center",
                    dataIndex: "leaveRate"
                },
                {
                    title: "总达成人数",
                    align: "center",
                    dataIndex: "totalArriveNum"
                },
                {
                    title: "总滞留人数",
                    align: "center",
                    dataIndex: "totalStayNum"
                },
                {
                    title: "关卡滞留率",
                    align: "center",
                    dataIndex: "checkpointStayRate"
                }/*,
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    fixed: "right",
                    width: 147,
                    scopedSlots: { customRender: "action" }
                }*/
            ],
            url: {
                list: "game/gameStoryAnalysis/list",
                delete: "game/gameStoryAnalysis/delete",
                deleteBatch: "game/gameStoryAnalysis/deleteBatch",
                exportXlsUrl: "game/gameStoryAnalysis/exportXls",
                importExcelUrl: "game/gameStoryAnalysis/importExcel"
            },
            dictOptions: {}
            ,
            tableScroll: { x: 11 * 147 + 50 }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
