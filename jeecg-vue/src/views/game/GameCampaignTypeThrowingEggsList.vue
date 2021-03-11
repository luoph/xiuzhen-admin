<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
<!--            <a-button type="primary" icon="download" @click="handleExportXls('节日砸蛋')">导出</a-button>-->
<!--            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"-->
<!--                      :action="importExcelUrl" @change="handleImportExcel">-->
<!--                <a-button type="primary" icon="import">导入</a-button>-->
<!--            </a-upload>-->
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
                style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
                         style="max-width:80px;font-size: 12px;font-style: italic;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small"
                              @click="uploadFile(text)"> 下载
                    </a-button>
                </template>
                <span slot="ordinaryPool" slot-scope="text, record">
                    <a-tag v-if="!text" color="red">未设置</a-tag>
                    <a-tag v-else v-for="tag in text.split(',')" :key="tag" color="blue">{{ tag }}</a-tag>
                </span>
                <span slot="luckyPool" slot-scope="text, record">
                    <a-tag v-if="!text" color="red">未设置</a-tag>
                    <a-tag v-else v-for="tag in text.split(',')" :key="tag" color="blue">{{ tag }}</a-tag>
                </span>
                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
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

        <gameCampaignTypeThrowingEggs-modal ref="modalForm" @ok="modalFormOk"></gameCampaignTypeThrowingEggs-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameCampaignTypeThrowingEggsModal from "./modules/GameCampaignTypeThrowingEggsModal";
import { getAction } from "@api/manage";
import { filterObj } from "@/utils/util";

export default {
    name: "GameCampaignTypeThrowingEggsList",
    mixins: [JeecgListMixin],
    components: {
        GameCampaignTypeThrowingEggsModal
    },
    data() {
        return {
            description: "节日砸蛋管理页面",
            model: {},
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
                    title: "砸蛋类型",
                    align: "center",
                    dataIndex: "eggType",
                    customRender:value=>{
                        let text = "--";
                        if(value === 1){
                            text = "1-金蛋"
                        }else if(value === 2){
                            text = "2-铂金单"
                        }else if(value === 3){
                            text = "3-钻石蛋"
                        }
                        return text;
                    }
                },
                {
                    title: "抽奖道具",
                    align: "center",
                    dataIndex: "costItemId"
                },
                {
                    title: "幸运值上限",
                    align: "center",
                    dataIndex: "limitLuckyValue"
                },
                {
                    title: "砸蛋值",
                    align: "center",
                    dataIndex: "throwingEggsValue"
                },
                {
                    title: "消耗道具数量",
                    align: "center",
                    dataIndex: "costNum"
                },
                {
                    title: "抽奖积分最小值",
                    align: "center",
                    dataIndex: "lotteryIntegralMin"
                },
                {
                    title: "抽奖积分最大值",
                    align: "center",
                    dataIndex: "lotteryIntegralMax"
                },
                {
                    title: "幸运奖池概率",
                    align: "center",
                    dataIndex: "luckyProbability"
                },
                {
                    title: "普通奖池",
                    align: "center",
                    dataIndex: "ordinaryPool",
                    scopedSlots: { customRender: "ordinaryPool" }
                },
                {
                    title: "幸运奖池",
                    align: "center",
                    dataIndex: "luckyPool",
                    scopedSlots: { customRender: "luckyPool" }
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime",
                    customRender: function(text) {
                        return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text);
                    }
                },
                {
                    title: "更新时间",
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
                list: "game/gameCampaignTypeThrowingEggs/list",
                delete: "game/gameCampaignTypeThrowingEggs/delete",
                deleteBatch: "game/gameCampaignTypeThrowingEggs/deleteBatch",
                exportXlsUrl: "game/gameCampaignTypeThrowingEggs/exportXls",
                importExcelUrl: "game/gameCampaignTypeThrowingEggs/importExcel"
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
        loadData(arg) {
            if (!this.model.id) {
                return;
            }

            if (!this.url.list) {
                this.$message.error("请设置url.list属性!");
                return;
            }

            // 加载数据 若传入参数1则加载第一页的内容
            if (arg === 1) {
                this.ipagination.current = 1;
            }

            // 查询条件
            const params = this.getQueryParams();
            this.loading = true;
            getAction(this.url.list, params).then(res => {
                if (res.success && res.result && res.result.records) {
                    this.dataSource = res.result.records;
                    this.ipagination.total = res.result.total;
                }
                if (res.code === 510) {
                    this.$message.warning(res.message);
                }
                this.loading = false;
            });
        },
        edit(record) {
            this.model = record;
            this.loadData();
        },
        handleAdd() {
            this.$refs.modalForm.add({ typeId: this.model.id, campaignId: this.model.campaignId });
            this.$refs.modalForm.title = "新增砸蛋活动配置";
        },
        getQueryParams() {
            const param = Object.assign({}, this.queryParam);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // typeId、活动id
            param.typeId = this.model.id;
            param.campaignId = this.model.campaignId;
            return filterObj(param);
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
</style>
