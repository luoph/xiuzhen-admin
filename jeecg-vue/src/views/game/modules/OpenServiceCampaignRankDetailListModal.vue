<template>
    <a-modal centered :title="title" :width="width" :visible="visible" @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增页签</a-button>
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
                    <img v-else :src="getImgView(text)" height="100px" alt="图片不存在" style="max-width:180px;" />
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

        <open-service-campaign-rank-detail-modal ref="modalForm" @ok="modalFormOk"></open-service-campaign-rank-detail-modal>
    </a-modal>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { filterObj } from "@/utils/util";
import { getAction } from "../../../api/manage";
import OpenServiceCampaignRankDetailModal from "./OpenServiceCampaignRankDetailModal";

export default {
    name: "OpenServiceCampaignRankDetailList",
    mixins: [JeecgListMixin],
    components: {
        OpenServiceCampaignRankDetailModal
    },
    data() {
        return {
            description: "开服活动-开服排行-活动明细(3级)管理页面",
            visible: false,
            title: "开服排行页签配置",
            width: 1200,
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
                // {
                //     title: "开服活动id",
                //     align: "center",
                //     dataIndex: "campaignId"
                // },
                // {
                //     title: "页签id",
                //     align: "center",
                //     dataIndex: "campaignTypeId"
                // },
                {
                    title: "活动名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "页签名称",
                    align: "center",
                    dataIndex: "tabName"
                },
                {
                    title: "排行类型",
                    align: "center",
                    dataIndex: "rankType",
                    customRender: value => {
                        let text = "--";
                        if (value === 1) {
                            text = "1-境界冲榜";
                        } else if (value === 2) {
                            text = "2-功法冲榜";
                        }
                        return text;
                    }
                },
                {
                    title: "开始时间",
                    align: "center",
                    dataIndex: "startDay"
                },
                {
                    title: "持续时间(天)",
                    align: "center",
                    dataIndex: "duration"
                },
                {
                    title: "宣传图",
                    align: "center",
                    dataIndex: "banner",
                    width: "120",
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "奖励图",
                    align: "center",
                    dataIndex: "rewardImg",
                    width: "80",
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "仙力",
                    align: "center",
                    dataIndex: "combatPower"
                },
                // {
                //     title: "排行玩家数量",
                //     align: "center",
                //     dataIndex: "rankNum"
                // },
                {
                    title: "奖励邮件id",
                    align: "center",
                    dataIndex: "rankRewardEmail"
                },
                {
                    title: "达标邮件id",
                    align: "center",
                    dataIndex: "standardRewardEmail"
                },
                {
                    title: "帮助信息",
                    align: "center",
                    width: 180,
                    dataIndex: "helpMsg",
                    scopedSlots: { customRender: "largeText" }
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
                list: "game/openServiceCampaignRankDetail/list",
                delete: "game/openServiceCampaignRankDetail/delete",
                deleteBatch: "game/openServiceCampaignRankDetail/deleteBatch",
                exportXlsUrl: "game/openServiceCampaignRankDetail/exportXls",
                importExcelUrl: "game/openServiceCampaignRankDetail/importExcel"
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
        close() {
            this.$emit("close");
            this.visible = false;
            this.dataSource = [];
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
            var params = this.getQueryParams();
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
        getQueryParams() {
            var param = Object.assign({}, this.queryParam);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            // 页签id、活动id
            param.campaignTypeId = this.model.id;
            param.campaignId = this.model.campaignId;
            return filterObj(param);
        },
        /** 关闭弹窗 */
        handleCancel() {
            this.close();
        },
        handleOk() {
            this.close();
        },
        handleAdd() {
            this.$refs.modalForm.add({ campaignTypeId: this.model.id, campaignId: this.model.campaignId });
            this.$refs.modalForm.title = "新增页签";
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

/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
}
.largeTextContainer {
    overflow-x: hidden;
    overflow-y: scroll;
    white-space: nowrap;
    max-height: 200px;
}

.largeText {
    white-space: normal;
    word-break: break-word;
}
</style>
