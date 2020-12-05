<template>
    <a-modal centered :title="name + '选择'" :width="1200" :visible="visible" @ok="handleOk" @cancel="close" cancelText="关闭">
        <a-row :gutter="18">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="图片类型">
                                <a-select placeholder="请选择图片类型" v-model="queryParam.type" default-value="1">
                                    <a-select-option :value="1">图标</a-select-option>
                                    <a-select-option :value="2">宣传图</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="图片名">
                                <a-input placeholder="请输入图片名" v-model="queryParam.name"></a-input>
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
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :rowSelection="{ selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
                :customRow="customRowFn"
                @change="handleTableChange"
            >
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
                    <img v-else :src="getImgView(text)" height="100px" alt="图片不存在" style="max-width:200px;font-size: 12px;font-style: italic;" />
                </template>
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
        visible: {
            type: Boolean,
            default: false
        },
        valueKey: {
            type: String,
            default: null
        },
        name: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            description: "游戏图片列表",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: "5%",
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "图片类型",
                    align: "center",
                    dataIndex: "type",
                    width: "10%",
                    customRender: value => {
                        let text = "--";
                        if (value === 1) {
                            text = "图标";
                        } else if (value === 2) {
                            text = "宣传图";
                        }
                        return text;
                    }
                },
                {
                    title: "图片",
                    align: "center",
                    dataIndex: "imgUrl",
                    width: "40%",
                    scopedSlots: { customRender: "imgSlot" }
                },
                {
                    title: "文件名",
                    align: "center",
                    width: "20%",
                    dataIndex: "name"
                },
                {
                    title: "图片尺寸",
                    align: "center",
                    width: "10%",
                    customRender: function(t, r) {
                        return r.width + "x" + r.height;
                    }
                },
                {
                    title: "备注",
                    align: "center",
                    width: "15%",
                    dataIndex: "remark"
                }
            ],
            url: {
                list: "game/gameImage/list"
            },
            // 已选择列表
            selectedTable: {
                dataSource: []
            }
        };
    },
    watch: {
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
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return window._CONFIG["gameImgUrl"] + text;
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
            if (value && value[0]) {
                this.$emit("input", value[0]);
            } else {
                this.$emit("input", "");
            }
            this.close();
        },
        customRowFn(record) {
            return {
                on: {
                    click: () => {
                        this.selectedRowKeys = [record.id];
                        this.selectedTable.dataSource = [record];
                    }
                }
            };
        }
    }
};
</script>
<style lang="less" scoped></style>
