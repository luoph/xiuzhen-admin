<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="4" :sm="8">
                        <a-form-item label="道具ID">
                            <a-input placeholder="请输入道具ID" v-model="queryParam.itemId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <a-form-item label="道具名">
                            <a-input placeholder="请输入道具名" v-model="queryParam.itemName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <span style="float: right;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="getItemTree">查询</a-button>
                        </span>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <span style="float: left;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="reload" style="margin-left: 8px;"
                                      @click="searchReset">重置</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <div style="margin-bottom: 50px;">
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
                style="font-weight: 600;">{{ selectedRowKeys.length }}</a
            >项
                <a style="margin-left: 24px;" @click="onClearSelected">清空</a>
            </div>
            <div v-if="showError">
                <a-alert message="错误选择" description="所选记录没有数量！" type="error" showIcon />
            </div>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="itemId"
                :columns="columns"
                :dataSource="treeData"
                :loading="loading"
                :pagination="{ pageSize: 15 }"
                :scroll="{ y: 650 }"
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"
            >
                <span slot="num" slot-scope="text, record">
                    <a-input-number style="width: 100%" id="inputNumber" v-model="record.num" :min="1" />
                </span>
            </a-table>
        </div>
        <div>
            <a-button style="margin-right: 50px;" type="primary" @click="handleOkGetItem">确定</a-button>
            <a-button type="primary" @click="handleCancel">取消</a-button>
        </div>
    </a-drawer>
</template>
<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getAction } from "@/api/manage";

export default {
    name: "GameEmailItemTreeModal",
    mixins: [JeecgListMixin],
    components: {},
    data() {
        return {
            description: "奖励选择页面",
            title: "奖励选择",
            width: 1050,
            visible: false,
            showError: false,
            treeData: [],
            pageSize: 20,
            queryParam: {
                itemId: null,
                itemName: null
            },
            selectItems: [],
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
                    title: "道具ID",
                    align: "center",
                    dataIndex: "itemId",
                    width: 60
                },
                {
                    title: "道具名",
                    align: "center",
                    dataIndex: "name",
                    width: 100
                },
                {
                    title: "描述",
                    align: "center",
                    dataIndex: "tips"
                },
                {
                    title: "数量",
                    align: "center",
                    width: 75,
                    dataIndex: "num",
                    scopedSlots: { customRender: "num" }
                }
            ],
            url: {
                list: "game/gameEmail/itemTree"
            },
            dictOptions: {}
        };
    },
    created() {
        this.$form.createForm(this);
        this.$nextTick(function() {
            this.$on("getItemTree", function() {
                this.getItemTree();
            });
        });
    },
    methods: {
        initDictConfig() {
        },

        close() {
            this.$emit("close");
            this.visible = false;
            this.showError = false;
            this.selectedRowKeys = [];
            this.selectedRows = [];
            this.selectItems = [];
        },

        handleCancel() {
            this.close();
        },

        onSelectChange(selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
            this.saveSelectItem();
        },

        handleOkGetItem() {
            let itemSize = this.selectItems.length;
            if (itemSize <= 0) {
                this.close();
                return;
            }
            let Items = [];
            for (let i = 0; i < this.selectItems.length; i++) {
                let row = this.selectItems[i];
                if (row.num === undefined || row.num === null || row.num <= 0) {
                    this.$message.error("第" + (i + 1) + "行数量错误！");
                    this.showError = true;
                    return;
                }
                let json = "{\"itemId\":" + row.itemId + ",\"num\":" + row.num + "}";
                Items.push(json);
            }

            let itemTreeResult = "[" + Items + "]";
            console.log(itemTreeResult);
            this.$emit("func", itemTreeResult);
            (this.showError = false);
            (this.showSuccess = true);
            this.handleCancel();
        },

        getItemTree: function() {
            this.visible = true;
            getAction(this.url.list, this.queryParam).then((res) => {
                this.treeData = res.result;
            });
        },

        saveSelectItem: function() {
            let size = this.selectedRowKeys.length;
            if (size > 0) {
                for (let index = 0; index < size; index++) {
                    this.selectItems.push(this.selectedRows[index]);
                }
            } else {
                this.selectItems = [];
            }
            this.selectItems = this.unique(this.selectItems);
        },

        unique(arr) {
            const res = new Map();
            return arr.filter((arr) => !res.has(arr.itemId) && res.set(arr.itemId, arr.num));
        }
    }
};
</script>

//
<style lang="less" scoped></style>
<style lang="less" scoped>
/** Button按钮间距 */
.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
}
</style>
