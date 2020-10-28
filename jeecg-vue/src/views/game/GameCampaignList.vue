<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="活动类型">
                            <a-select placeholder="选择活动类型" v-model="queryParam.type" default-value="1">
                                <a-select-option :value="1">1-登录礼包</a-select-option>
                                <a-select-option :value="2">2-累计充值</a-select-option>
                                <a-select-option :value="3">3-兑换</a-select-option>
                                <a-select-option :value="4">4-节日任务</a-select-option>
                                <a-select-option :value="5">5-Buff-修为加成</a-select-option>
                                <a-select-option :value="6">6-Buff-灵气加成</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="活动展示名称">
                            <a-input placeholder="活动展示名称" v-model="queryParam.showName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="自动开启">
                            <j-dict-select-tag v-model="queryParam.autoOpen" placeholder="请选择自动开启" dictCode="yn" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="活动状态">
                            <j-dict-select-tag v-model="queryParam.type" placeholder="请选择状态" dictCode="valid_type" />
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
                <span slot="serverIdTags" slot-scope="text, record">
                    <a-tag v-for="tag in text.split(',')" :key="tag" color="blue">{{ tag }}</a-tag>
                </span>
                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">活动信息</a>
                    <a-divider type="vertical" />
                    <a @click="handleEdit(record)">参数配置</a>
                    <a-divider type="vertical" />
                    <a @click="handleServerList(record)">活动状态</a>
                    <a-divider type="vertical" />
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

        <GameCampaignModal ref="modalForm" @ok="modalFormOk"></GameCampaignModal>
        <GameCampaignServerList ref="serverListModal"></GameCampaignServerList>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameCampaignModal from "./modules/GameCampaignModal";
import GameCampaignServerList from "./modules/GameCampaignServerList";
import JDate from "@/components/jeecg/JDate.vue";

export default {
    name: "GameCampaignList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameCampaignModal,
        GameCampaignServerList
    },
    data() {
        return {
            description: "活动信息",
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
                    title: "活动类型",
                    align: "center",
                    dataIndex: "type",
                    // <!-- 1.登录礼包, 2.累计充值, 3.兑换, 4.节日任务, 5.buff-修为加成, 6.buff-灵所加成' -->
                    customRender: value => {
                        let re = "--";
                        if (value === 1) {
                            re = "1-登录礼包";
                        } else if (value === 2) {
                            re = "2-累计充值";
                        } else if (value === 3) {
                            re = "3-兑换";
                        } else if (value === 4) {
                            re = "4-节日任务";
                        } else if (value === 5) {
                            re = "5-Buff-修为加成";
                        } else if (value === 6) {
                            re = "6-Buff-灵气加成";
                        }
                        return re;
                    }
                },
                {
                    title: "活动名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "活动标语（描述）",
                    align: "left",
                    dataIndex: "description"
                },
                {
                    title: "活动图标",
                    align: "center",
                    dataIndex: "icon"
                },
                {
                    title: "活动宣传图",
                    align: "center",
                    dataIndex: "banner"
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
                    title: "区服id",
                    align: "center",
                    dataIndex: "serverIds",
                    scopedSlots: { customRender: "serverIdTags" }
                },
                {
                    title: "自动开启",
                    align: "center",
                    dataIndex: "autoOpen",
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
                    title: "活动开始时间",
                    align: "center",
                    dataIndex: "startTime"
                },
                {
                    title: "活动结束时间",
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
                list: " game/gameCampaign/list",
                delete: " game/gameCampaign/delete",
                deleteBatch: " game/gameCampaign/deleteBatch",
                exportXlsUrl: " game/gameCampaign/exportXls",
                importExcelUrl: " game/gameCampaign/importExcel"
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
            this.$refs.modalForm.title = "编辑";
            this.$refs.modalForm.disableSubmit = false;
        },
        handleServerList: function(record) {
            this.$refs.serverListModal.edit(record);
            this.$refs.serverListModal.title = "活动信息";
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
