<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="12">
                    <a-col :md="4" :sm="4">
                <a-form-item label="玩家id">
                <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"></a-input>
            </a-form-item>
                </a-col>
                <a-col :md="4" :sm="4">
                <a-form-item label="玩家昵称">
                <a-input placeholder="请输入玩家昵称" v-model="queryParam.nickname"></a-input>
            </a-form-item>
                </a-col>
                <a-col :md="4" :sm="4">
                <a-form-item label="服务器id">
                <a-input placeholder="请输入服务器id" v-model="queryParam.serverId"></a-input>
                </a-form-item>
                </a-col>
                    <a-col :md="6" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchReset">重置</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('封禁管理')">导出</a-button>
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
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
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
                    <!--<a @click="handleEdit(record)">编辑</a>-->
                    <a-divider type="vertical" />
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a-popconfirm title="确定解封吗?" @confirm="() => handleDelete(record.id)">
                                    <a>解封</a>
                                </a-popconfirm>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>
                </span>
            </a-table>
        </div>

        <playerBanInfo-modal ref="modalForm" @ok="modalFormOk"></playerBanInfo-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import PlayerBanInfoModal from "./modules/PlayerBanInfoModal";

export default {
    name: "PlayerBanInfoList",
    mixins: [JeecgListMixin],
    components: {
        PlayerBanInfoModal
    },
    data() {
        return {
            description: "封禁管理管理页面",
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
                    title: "服务器ID",
                    align: "center",
                    dataIndex: "serverId"
                },
                {
                    title: "玩家ID",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "玩家昵称",
                    align: "center",
                    dataIndex: "nickname"
                },
                {
                    title: "封禁原因",
                    align: "center",
                    dataIndex: "banReason"
                },
                {
                    title: "全部封禁",
                    align: "center",
                    dataIndex: "allBan",
                    customRender: text => {
                        let re = "";
                        if (text == false) {
                            re = "否";
                        } else if (text == true) {
                            re = "是";
                        }
                        return re;
                    }
                },
                {
                    title: "操作时间",
                    align: "center",
                    dataIndex: "createTime"
                },
                {
                    title: "操作人",
                    align: "center",
                    dataIndex: "operator"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "player/playerBanInfo/list",
                delete: "player/playerBanInfo/delete",
                deleteBatch: "player/playerBanInfo/deleteBatch",
                exportXlsUrl: "player/playerBanInfo/exportXls",
                importExcelUrl: "player/playerBanInfo/importExcel"
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
