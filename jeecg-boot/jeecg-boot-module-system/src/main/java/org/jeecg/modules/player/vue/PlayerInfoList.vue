<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="12" :sm="16">
                <a-form-item label="玩家Id">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.playerId_begin"></a-input>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.playerId_end"></a-input>
            </a-form-item>
            </a-col>
                <a-col :md="12" :sm="16">
                <a-form-item label="角色昵称">
                <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.nickname_begin"></a-input>
                <span class="query-group-split-cust"></span>
                <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.nickname_end"></a-input>
            </a-form-item>
            </a-col>
                <template v-if="toggleSearchStatus">
                    <a-col :md="12" :sm="16">
                    <a-form-item label="性别 1男 0女">
                    <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.sex_begin"></a-input>
                    <span class="query-group-split-cust"></span>
                    <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.sex_end"></a-input>
                </a-form-item>
                </a-col>
                    <a-col :md="12" :sm="16">
                    <a-form-item label="出身id">
                    <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.birthId_begin"></a-input>
                    <span class="query-group-split-cust"></span>
                    <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.birthId_end"></a-input>
                </a-form-item>
                </a-col>
                    <a-col :md="12" :sm="16">
                    <a-form-item label="createTime">
                    <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                    <span class="query-group-split-cust"></span>
                    <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
                </a-form-item>
                </a-col>
                    <a-col :md="12" :sm="16">
                    <a-form-item label="updateTime">
                    <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.updateTime_begin"></j-date>
                    <span class="query-group-split-cust"></span>
                    <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.updateTime_end"></j-date>
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
            <a-button type="primary" icon="download" @click="handleExportXls('玩家信息')">导出</a-button>
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
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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

        <playerInfo-modal ref="modalForm" @ok="modalFormOk"></playerInfo-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import PlayerInfoModal from "./modules/PlayerInfoModal";
import JDate from "@/components/jeecg/JDate.vue";

export default {
    name: "PlayerInfoList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        PlayerInfoModal
    },
    data() {
        return {
            description: "玩家信息管理页面",
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
                    title: "全局uuid",
                    align: "center",
                    dataIndex: "uuid"
                },
                {
                    title: "玩家Id",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "角色昵称",
                    align: "center",
                    dataIndex: "nickname"
                },
                {
                    title: "角色头像",
                    align: "center",
                    dataIndex: "avatar"
                },
                {
                    title: "性别 1男 0女",
                    align: "center",
                    dataIndex: "sex"
                },
                {
                    title: "音乐开关",
                    align: "center",
                    dataIndex: "openMusic"
                },
                {
                    title: "音效开关",
                    align: "center",
                    dataIndex: "openSound"
                },
                {
                    title: "出身id",
                    align: "center",
                    dataIndex: "birthId"
                },
                {
                    title: "是否初始化",
                    align: "center",
                    dataIndex: "initialized"
                },
                {
                    title: "createTime",
                    align: "center",
                    dataIndex: "createTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "updateTime",
                    align: "center",
                    dataIndex: "updateTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "/player/playerInfo/list",
                delete: "/player/playerInfo/delete",
                deleteBatch: "/player/playerInfo/deleteBatch",
                exportXlsUrl: "/player/playerInfo/exportXls",
                importExcelUrl: "/player/playerInfo/importExcel"
            },
            dictOptions: {
            }
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