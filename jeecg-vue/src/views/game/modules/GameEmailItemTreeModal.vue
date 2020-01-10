<template>
    <a-drawer
        :title="title"
        :width="width"
        placement="right"
        :closable="false"
        @close="close"
        :visible="visible"
    >
        <div style="margin-bottom: 50px;">
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
                <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
            </div>
            <div v-if="showError">
                <a-alert
                    message="错误选择"
                    description="所选记录没有数量！"
                    type="error"
                    showIcon
                />
            </div>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="itemId"
                :columns="columns"
                :dataSource="treeData"
                :loading="loading"
                :pagination="{pageSize:15}"
                :scroll="{ y: 650 }"
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                @change="handleTableChange"
            >
                <span slot="num" slot-scope="text, record">
                    <a-input v-model="record.num"></a-input>
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
            pageSize:20,
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
                    dataIndex: "itemId"
                },
                {
                    title: "道具名",
                    align: "center",
                    dataIndex: "name"
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
        this.$nextTick(function() {
            this.$on("getItemTree", function() {
                console.log("监听成功");
                this.getItemTree();
            });
        });
    },
    methods: {
        initDictConfig() {},
        close() {
            this.$emit("close");
            this.visible = false;
            this.showError = false;
            this.selectedRowKeys = [];
            this.selectedRows = [];
        },
        handleCancel() {
            this.close();
        },
        onSelectChange(selectedRowKeys, selectedRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
        },
        handleOkGetItem() {
            let itemSize = this.selectedRowKeys.length;
            if (itemSize <= 0) {
                this.close();
                return;
            }
            let Items = [];
            for (let i = 0; i < this.selectedRows.length; i++) {
                let row = this.selectedRows[i];
                if (row.num === undefined || row.num === null || row.num <= 0) {
                    this.$message.error("第"+(i+1)+"行数量错误！");
                    this.showError = true;
                    return;
                }
                let json = '{"itemId":' + row.itemId + ',"num":' + row.num + "}";
                Items.push(json);
            }
            let itemTreeResult = '['+Items+']';
            console.log(itemTreeResult);
            this.$emit('func',itemTreeResult);
            (this.showError = false), (this.showSuccess = true), (this.showWarning = false), this.close();
        },
        getItemTree: function() {
            getAction(this.url.list).then(res => {
                this.treeData = res.result;
            });
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>