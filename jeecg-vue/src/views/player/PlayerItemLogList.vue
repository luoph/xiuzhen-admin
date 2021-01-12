<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="区服ID">
                            <server-select @select="change"></server-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="玩家ID">
                            <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="道具名">
                            <a-input placeholder="请输入道具名" v-model="queryParam.itemIdName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="3" :sm="3">
                        <a-form-item label="产销类型">
                            <a-select placeholder="产销类型" v-model="queryParam.type" @click="resetWay()">
                                <a-select-option value="1">产出</a-select-option>
                                <a-select-option value="2">消耗</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="8" :sm="5">
                        <a-form-item v-if="queryParam.type === '1'" key="1" label="产出途径">
                            <a-select-read-json-some json-file="item_fall_rule" placeholder="请选择途径" @onSelectOptionSome="selectWay"></a-select-read-json-some>
                        </a-form-item>
                        <a-form-item v-if="queryParam.type === '2'" key="2" label="消耗途径">
                            <a-select-read-json-some json-file="item_expend" placeholder="请选择途径" @onSelectOptionSome="selectWay"></a-select-read-json-some>
                        </a-form-item>
                    </a-col>
                    <a-col :md="8" :sm="8">
                        <a-form-item label="统计日期">
                            <a-range-picker
                                :ranges="{ Today: [moment(), moment()], 'This Month': [moment(), moment().endOf('month')] }"
                                :show-time="{ defaultValue: [moment('00:00:00', 'HH:mm:ss'), moment('00:00:00', 'HH:mm:ss')] }"
                                format="YYYY/MM/DD HH:mm:ss"
                                @change="onDateChange"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="2" :sm="10">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery()">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <div class="table-operator">
            <a-button type="primary" icon="download" @click="downloadExcel('玩家道具日志')">导出</a-button>
        </div>
        <!-- 查询区域-END -->

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
            </a-table>
        </div>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import { filterObj } from "@/utils/util";
import ServerSelect from "@/components/gameserver/ServerSelect";
import ASelectReadJson from "@comp/gameserver/ASelectReadJson";
import ASelectReadJsonSome from "@comp/gameserver/ASelectReadJsonSome";
import Vue from "vue";
import { getAction } from "@/api/manage";
import { ACCESS_TOKEN } from "@/store/mutation-types";
import moment from "moment";
export default {
    name: "PlayerItemLogList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        ServerSelect,
        ASelectReadJson,
        ASelectReadJsonSome
    },
    data() {
        return {
            dateFormat: "YYYY/MM/DD",
            monthFormat: "YYYY/MM",
            description: "玩家道具日志管理页面",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "玩家id",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "道具id",
                    align: "center",
                    dataIndex: "itemId"
                },
                {
                    title: "道具名",
                    align: "center",
                    dataIndex: "itemIdName"
                },
                {
                    title: "数量",
                    align: "center",
                    dataIndex: "num"
                },
                {
                    title: "途径",
                    align: "center",
                    dataIndex: "way"
                },
                {
                    title: "途径名",
                    align: "center",
                    dataIndex: "wayName"
                },
                {
                    title: "更新前数量",
                    align: "center",
                    dataIndex: "beforeNum"
                },
                {
                    title: "更新后数量",
                    align: "center",
                    dataIndex: "afterNum"
                },
                {
                    title: "方式",
                    align: "center",
                    dataIndex: "type",
                    customRender: function (text) {
                        return text === 1 ? "存入" : "支出";
                    }
                },
                {
                    title: "统计日期",
                    align: "center",
                    dataIndex: "createTime"
                }
            ],
            url: {
                list: "player/playerItemLog/list",
                exportXlsUrl: "player/playerItemLog/exportXls",
                downloadExcel: "/player/playerItemLog/download"
            },
            dictOptions: {}
        };
    },
    methods: {
        initDictConfig() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        },
        getQueryParams() {
            let param = Object.assign({}, this.queryParam, this.isorter);
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            return filterObj(param);
        },
        // onDateChange: function (value, dateString) {
        //     this.queryParam.startDate = dateString[0];
        //     this.queryParam.endDate = dateString[1];
        // },
        moment,
        onDateChange(dates, dateStrings) {
            this.queryParam.startDate = dateStrings[0];
            this.queryParam.endDate = dateStrings[1];
        },
        change(serverId) {
            this.queryParam.serverId = serverId;
        },
        selectWay(way) {
            for (const key in way) {
                this.queryParam.wayName = way+",";
            }
        },
        resetWay(){
this.queryParam.wayName = ""
        },
        handleChange(value) {
            console.log(`selected ${value}`);
        },
        searchQuery() {
            let param = this.queryParam;
            console.log(param);
            getAction(this.url.list, param).then((res) => {
                if (res.success) {
                    this.dataSource = res.result.records;
                    this.ipagination.current = res.result.current;
                    this.ipagination.size = res.result.size.toString();
                    this.ipagination.total = res.result.total;
                    this.ipagination.pages = res.result.pages;
                } else {
                    this.$message.error(res.message);
                }
            });
        },
        downloadExcel(filename) {
            var xhr = new XMLHttpRequest();
            xhr.open("post", window._CONFIG["domainURL"] + this.url.downloadExcel, true);
            xhr.responseType = "blob";
            xhr.setRequestHeader("Content-Type", "application/json");
            const token = Vue.ls.get(ACCESS_TOKEN);
            console.log(token);
            xhr.setRequestHeader("X-Access-Token", token);
            xhr.onload = function () {
                var blob = this.response;
                var reader = new FileReader();
                reader.readAsDataURL(blob);
                reader.onload = function (e) {
                    var a = document.createElement("a");
                    a.download = filename + ".xlsx";
                    a.href = e.target.result;
                    a.click();
                };
            };
            var a = this.queryParam;
            var param = JSON.stringify(a);
            console.log(param);
            xhr.send(param);
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
