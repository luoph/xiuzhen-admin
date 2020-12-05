<template>
    <a-modal centered :title="name + '选择'" :width="1000" :visible="visible" @ok="handleOk" @cancel="close" cancelText="关闭">
        <a-row :gutter="18">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline">
                    <a-row :gutter="24">
                        <a-col :span="14">
                            <a-form-item :label="queryParamText || name">
                                <a-input :placeholder="'请输入' + (queryParamText || name)" v-model="queryParam[valueKey]"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :span="8">
                            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                            </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>

            <a-table
                size="small"
                bordered
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :scroll="{ y: 480 }"
                :rowSelection="{ selectedRowKeys, onChange: onSelectChange, type: multiple ? 'checkbox' : 'radio' }"
                :customRow="customRowFn"
                @change="handleTableChange"
            >
            </a-table>
        </a-row>
    </a-modal>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";

export default {
    name: "GameListComponentModal",
    mixins: [JeecgListMixin],
    props: {
        value: {
            type: Array,
            default: () => []
        },
        visible: {
            type: Boolean,
            default: false
        },
        valueKey: {
            type: String,
            required: true
        },
        multiple: {
            type: Boolean,
            default: true
        },

        name: {
            type: String,
            default: ""
        },
        listUrl: {
            type: String,
            required: true,
            default: ""
        },
        displayKey: {
            type: String,
            default: null
        },
        propColumns: {
            type: Array,
            default: () => []
        },
        // 查询条件文字
        queryParamText: {
            type: String,
            default: null
        }
    },
    data() {
        return {
            // 表头
            columns: this.propColumns,
            // 已选择列表
            selectedTable: {
                dataSource: []
            },
            url: { list: this.listUrl }
        };
    },
    watch: {
        value: {
            immediate: true,
            handler(val) {
                this.valueWatchHandler(val);
            }
        },
        dataSource: {
            deep: true,
            handler(val) {
                let options = val.map(data => ({ label: data[this.displayKey || this.valueKey], value: data[this.valueKey] }));
                this.$emit("ok", options);
                this.valueWatchHandler(this.value);
            }
        },
        selectionRows: {
            immediate: true,
            deep: true,
            handler(val) {
                this.selectedTable.dataSource = val;
            }
        }
    },

    methods: {
        /** 关闭弹窗 */
        close() {
            this.$emit("update:visible", false);
        },

        valueWatchHandler(val) {
            let dataSource = [];
            let selectedRowKeys = [];
            val.forEach(item => {
                this.dataSource.forEach(data => {
                    if (data[this.valueKey] === item) {
                        dataSource.push(data);
                        selectedRowKeys.push(data.id);
                    }
                });
            });
            this.selectedTable.dataSource = dataSource;
            this.selectedRowKeys = selectedRowKeys;
        },

        /** 完成选择 */
        handleOk() {
            let value = this.selectedTable.dataSource.map(data => data[this.valueKey]);
            this.$emit("input", value);
            this.close();
        },

        /** 删除已选择的 */
        handleDeleteSelected(record, index) {
            this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1);
            this.selectedTable.dataSource.splice(index, 1);
        },

        customRowFn(record) {
            if (!this.multiple) {
                return {
                    on: {
                        click: () => {
                            this.selectedRowKeys = [record.id];
                            this.selectedTable.dataSource = [record];
                        }
                    }
                };
            }
            return {};
        }
    }
};
</script>
<style lang="less" scoped></style>
