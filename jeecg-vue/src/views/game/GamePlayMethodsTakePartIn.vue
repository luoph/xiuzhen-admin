<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="10" :sm="8">
                        <!--@ = v-on:数据绑定 不是事件-->
                        <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
                    </a-col>

                    <a-col :md="10" :sm="8">
                        <a-form-item label="时间">
                            <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="5" :sm="5">
                        <a-form-item label="就近天数">
                            <a-select placeholder="天数" v-model="queryParam.days">
                                <a-select-option :value="0">不选择天数</a-select-option>
                                <a-select-option :value="7">近7天</a-select-option>
                                <a-select-option :value="15">近15天</a-select-option>
                                <a-select-option :value="30">近一个月</a-select-option>
                                <a-select-option :value="60">近两个月</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <play-methods-type @onSelectPlayMethodsType="onSelectPlayMethodsType"></play-methods-type>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px" @click="searchQuery">刷新数据</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="download" @click="downloadExcel('玩法参与')">导出</a-button>
        </div>

        <!-- table区域-begin -->
        <div>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="rowIndex"
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
                    <span v-if="!text" style="font-size: 12px; font-style: italic">无此图片</span>
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px; font-style: italic">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">查看详情</a>
                </span>
            </a-table>
        </div>

        <gameForbidden-modal ref="modalForm" @ok="modalFormOk"></gameForbidden-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameForbiddenModal from "./modules/GameForbiddenModal";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import PlayMethodsType from "@/components/game/PlayMethodsType";
import { getAction } from "@/api/manage";
import Vue from "vue";
import { ACCESS_TOKEN } from "@/store/mutation-types"

export default {
    name: "GamePlayMethodsTakePartIn",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameForbiddenModal,
        GameChannelServer,
        getAction,
        PlayMethodsType
    },
    data() {
        return {
            description: "玩法参与",
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
                    title: "日期",
                    align: "center",
                    dataIndex: "date"
                },
                {
                    title: "xx级登录人数",
                    align: "center",
                    dataIndex: "playerNum"
                },
                {
                    title: "参与人数",
                    align: "center",
                    dataIndex: "takePlayInPlayerNum"
                },
                {
                    title: "参与率",
                    align: "center",
                    dataIndex: "takePlayInRate",
                    customRender: function (text) {
                        return text + "%";
                    }
                },
                {
                    title: "满参与人数",
                    align: "center",
                    dataIndex: "allTakePlayInPlayerNum"
                },
                {
                    title: "满参率",
                    align: "center",
                    dataIndex: "allTakePlayInRate",
                    customRender: function (text) {
                        return text + "%";
                    }
                },
                {
                    title: "回头率",
                    align: "center",
                    dataIndex: "secondGlanceRate",
                    customRender: function (text) {
                        return text + "%";
                    }
                }
            ],
            url: {
                list: "game/playMethodsTakePart/list",
                delete: "game/playMethodsTakePart/delete",
                deleteBatch: "game/playMethodsTakePart/deleteBatch",
                exportXlsUrl: "game/playMethodsTakePart/exportXls",
                importExcelUrl: "game/playMethodsTakePart/importExcel",
                downloadExcel: "/game/playMethodsTakePart/download"
            },
            dictOptions: {}
        };
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        initDictConfig() {},
        typeChange: function (type) {
            this.queryParam.type = type;
        },
        isForeverChange: function (isForever) {
            this.queryParam.isForever = isForever;
        },
        onSelectChannel: function (channelId) {
            this.queryParam.channelId = channelId;
        },
        onSelectServer: function (serverId) {
            this.queryParam.serverId = serverId;
        },
        onDateChange: function (value, dateStr) {
            this.queryParam.rangeDateBegin = dateStr[0];
            this.queryParam.rangeDateEnd = dateStr[1];
        },
        replaceByValue: function (arr, grade) {
            console.log(arr);
            for (var j = 0; j < arr.length; j++) {
                console.log(arr[j].dataIndex);
                let a = {
                    title: grade + "级登录人数",
                    align: "center",
                    dataIndex: "playerNum"
                };
                arr.splice(2, 1, a);
            }
        },
        onSelectPlayMethodsType: function (playMethodsType) {
            console.log(playMethodsType);
            this.queryParam.playMethodsType = playMethodsType;
        },
        searchQuery() {
            if(null == this.queryParam.playMethodsType){
                this.$message.error("请选择玩法类型!");
                return;
            }
            this.replaceByValue(this.columns, this.queryParam.playMethodsType.grade);
            let param = {
                days: this.queryParam.days,
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                playMethodsType: this.queryParam.playMethodsType.type,
                grade: this.queryParam.playMethodsType.grade,
                fullTime: this.queryParam.playMethodsType.fullTime
            };
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
            xhr.open("post", window._CONFIG["domainURL"] +this.url.downloadExcel, true);
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
            var a = {
                days: this.queryParam.days,
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                playMethodsType: this.queryParam.playMethodsType.type,
                grade: this.queryParam.playMethodsType.grade,
                fullTime: this.queryParam.playMethodsType.fullTime
            };
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
