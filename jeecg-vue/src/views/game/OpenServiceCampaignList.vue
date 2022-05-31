<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="活动名称">
                            <j-input placeholder="请输入活动名称模糊查询" v-model="queryParam.name"></j-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="活动状态">
                            <a-select placeholder="请选择活动状态" v-model="queryParam.status" initialValue="1">
                                <a-select-option :value="0">无效</a-select-option>
                                <a-select-option :value="1">有效</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="自动开启">
                                <a-select placeholder="请选择自动开启状态" v-model="queryParam.autoOpen" initialValue="0">
                                    <a-select-option :value="0">关闭</a-select-option>
                                    <a-select-option :value="1">开启</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="活动备注">
                                <j-input placeholder="请输入活动备注模糊查询" v-model="queryParam.remark"></j-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="12" :sm="16">
                            <a-form-item label="创建时间">
                                <a-range-picker v-model="queryParam.createTimeRange" format="YYYY-MM-DD"
                                                :placeholder="['开始时间', '结束时间']" @change="onCreateTimeChange" />
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
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('开服活动(1级)')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                      :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel">
                        <a-icon type="delete" />
                        删除
                    </a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作
                    <a-icon type="down" />
                </a-button>
            </a-dropdown>
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
                style="font-weight: 600">{{ selectedRowKeys.length }}</a
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
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small"
                              @click="uploadFile(text)"> 下载
                    </a-button>
                </template>
                <span slot="serverIdSlot" slot-scope="text">
                    <a-tag v-if="!text" color="red">未设置</a-tag>
                    <a-tag v-else v-for="tag in text.split(',')" :key="tag" color="blue">{{ tag }}</a-tag>
                </span>
                <span slot="statuSlot" slot-scope="text">
                    <a-tag v-if="text === 0" color="red">无效</a-tag>
                    <a-tag v-else color="green">有效</a-tag>
                </span>
                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">活动信息</a>
                    <a-divider type="vertical" />
                    <a @click="handleDuplicate(record)">复制</a>
                    <a-divider type="vertical" />
                    <!-- <a @click="handleTabList(record)">页签配置</a> -->
                    <a @click="handleSync(record)">同步到区服</a>
                    <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
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

        <open-service-campaign-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-modal>
        <!-- <open-service-campaign-server-list ref="serverListModal" @ok="modalFormOk"></open-service-campaign-server-list> -->
        <!-- <open-service-campaign-tab-list ref="tabListModal"></open-service-campaign-tab-list> -->
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "@/api/manage";
import { filterObj } from "@/utils/util";
import OpenServiceCampaignModal from "./modules/OpenServiceCampaignModal";
import JDate from "@/components/jeecg/JDate.vue";
import JInput from "@/components/jeecg/JInput";

export default {
    name: "OpenServiceCampaignList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        JInput,
        OpenServiceCampaignModal
    },
    data() {
        return {
            description: "开服活动(1级)管理页面",
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
                    title: "id",
                    align: "center",
                    dataIndex: "id"
                },
                {
                    title: "活动名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "是否跨服",
                    align: "center",
                    width: 80,
                    dataIndex: "cross",
                    customRender: value => {
                        let text = "--";
                        if (value === 0) {
                            text = "本服";
                        } else if (value === 1) {
                            text = "跨服";
                        }
                        return text;
                    }
                },
                {
                    title: "服务器id",
                    align: "center",
                    dataIndex: "serverIds",
                    scopedSlots: { customRender: "serverIdSlot" }
                },
                {
                    title: "活动图标",
                    align: "center",
                    dataIndex: "icon",
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "活动状态",
                    align: "center",
                    dataIndex: "status",
                    scopedSlots: { customRender: "statuSlot" }
                },
                {
                    title: "优先级",
                    align: "center",
                    dataIndex: "priority"
                },
                {
                    title: "自动开启",
                    align: "center",
                    dataIndex: "autoOpen",
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
                    title: "活动备注",
                    align: "center",
                    dataIndex: "remark"
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime"
                },
                // {
                //     title: "更新时间",
                //     align: "center",
                //     dataIndex: "updateTime"
                // },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/openServiceCampaign/list",
                sync: "game/openServiceCampaign/sync",
                delete: "game/openServiceCampaign/delete",
                duplicate: "game/openServiceCampaign/duplicate",
                deleteBatch: "game/openServiceCampaign/deleteBatch",
                exportXlsUrl: "game/openServiceCampaign/exportXls",
                importExcelUrl: "game/openServiceCampaign/importExcel"
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
        initDictConfig() {
        },
        getQueryParams() {
            console.log(this.queryParam.createTimeRange);
            var param = Object.assign({}, this.queryParam, this.isorter);
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // 范围参数不传递后台
            delete param.createTimeRange;
            return filterObj(param);
        },
        onCreateTimeChange: function(value, dateString) {
            console.log(dateString[0], dateString[1]);
            this.queryParam.createTime_begin = dateString[0];
            this.queryParam.createTime_end = dateString[1];
        },
        handleEdit: function(record) {
            this.$refs.modalForm.edit(record);
            this.$refs.modalForm.title = "活动信息";
            this.$refs.modalForm.disableSubmit = false;
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
        },
        handleServerList: function(record) {
            this.$refs.serverListModal.edit(record);
            this.$refs.serverListModal.title = "活动信息";
        },
        handleTabList: function(record) {
            this.$refs.tabListModal.edit(record);
            this.$refs.tabListModal.title = "页签配置";
            this.$refs.tabListModal.disableSubmit = false;
        },
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return `${window._CONFIG["domainURL"]}/${text}`;
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
</style>
