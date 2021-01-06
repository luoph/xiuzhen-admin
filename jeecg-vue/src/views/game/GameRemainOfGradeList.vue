<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="45">
                    <a-col :md="10" :sm="8">
                        <!--@ = v-on:数据绑定 不是事件-->
                        <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"></game-channel-server>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="创建日期">
                            <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="天数">
                            <a-input v-model="showColumn" placeholder="多个数据用英文，隔开" style="width: 100%" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!--查询区域结束-->
        <!-- table区域-begin -->
        <div>
            <a-table
                ref="table"
                size="middle"
                bordered
                :rowKey="record => (record.id != null ? record.id : '0')"
                :loading="loading"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :scroll="{ x: 'max-content' }"
                @change="handleTableChange"
            ></a-table>
        </div>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import { getAction } from "@/api/manage";

export default {
    description: "分档位留存",
    name: "GameRemainOfGradeList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            showColumn: "",
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    width: "100",
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "档位",
                    dataIndex: "countDate",
                    width: "120",
                    align: "center",
                    customRender: function(text) {
                        return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: "新增玩家",
                    dataIndex: "registerNum",
                    align: "center",
                    width: "120"
                },
                {
                    title: "2日留存",
                    dataIndex: "c2",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c2, record.registerNum);
                    }
                },
                {
                    title: "3日留存",
                    dataIndex: "c3",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c3, record.registerNum);
                    }
                },
                {
                    title: "4日留存",
                    dataIndex: "c4",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c4, record.registerNum);
                    }
                },
                {
                    title: "5日留存",
                    dataIndex: "c5",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c5, record.registerNum);
                    }
                },
                {
                    title: "6日留存",
                    dataIndex: "c6",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c6, record.registerNum);
                    }
                },
                {
                    title: "7日留存",
                    dataIndex: "c7",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c7, record.registerNum);
                    }
                },
                {
                    title: "15日留存",
                    dataIndex: "c15",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c15, record.registerNum);
                    }
                },
                {
                    title: "30日留存",
                    dataIndex: "c30",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c30, record.registerNum);
                    }
                },
                {
                    title: "60日留存",
                    dataIndex: "c60",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c60, record.registerNum);
                    }
                },
                {
                    title: "90日留存",
                    dataIndex: "c90",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c90, record.registerNum);
                    }
                },
                {
                    title: "120日留存",
                    dataIndex: "c120",
                    align: "center",
                    width: "120",
                    customRender: (text, record) => {
                        return this.countRate(record.c120, record.registerNum);
                    }
                }
            ],
            url: {
                list: "game/remainStatistisc/grade"
            },
            dictOptions: {}
        };
    },
    computed: {},
    methods: {
        initDictConfig() {},
        onSelectChannel: function(channelId) {
            this.queryParam.channelId = channelId;
        },
        onSelectServer: function(serverId) {
            this.queryParam.serverId = serverId;
        },
        onDateChange: function(value, dateStr) {
            this.queryParam.rangeDateBegin = dateStr[0];
            this.queryParam.rangeDateEnd = dateStr[1];
        },
        searchQuery() {
// 动态改变表头start---------------------------------------------------------
            let a = this.showColumn.split(",");
            if (a[0] == "") {
                this.columns = [
                    {
                        title: "#",
                        dataIndex: "",
                        width: "100",
                        align: "center",
                        customRender: function (t, r, index) {
                            return parseInt(index) + 1;
                        }
                    },
                    {
                        title: "档位",
                        dataIndex: "countDate",
                        width: "120",
                        align: "center",
                        customRender: function (text) {
                            return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                        }
                    },
                    {
                        title: "新增玩家",
                        dataIndex: "registerNum",
                        align: "center",
                        width: "120"
                    },
                    {
                        title: "2日留存",
                        dataIndex: "c2",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c2, record.registerNum);
                        }
                    },
                    {
                        title: "3日留存",
                        dataIndex: "c3",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c3, record.registerNum);
                        }
                    },
                    {
                        title: "4日留存",
                        dataIndex: "c4",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c4, record.registerNum);
                        }
                    },
                    {
                        title: "5日留存",
                        dataIndex: "c5",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c5, record.registerNum);
                        }
                    },
                    {
                        title: "6日留存",
                        dataIndex: "c6",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c6, record.registerNum);
                        }
                    },
                    {
                        title: "7日留存",
                        dataIndex: "c7",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c7, record.registerNum);
                        }
                    },
                    {
                        title: "15日留存",
                        dataIndex: "c15",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c15, record.registerNum);
                        }
                    },
                    {
                        title: "30日留存",
                        dataIndex: "c30",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c30, record.registerNum);
                        }
                    },
                    {
                        title: "60日留存",
                        dataIndex: "c60",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c60, record.registerNum);
                        }
                    },
                    {
                        title: "90日留存",
                        dataIndex: "c90",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c90, record.registerNum);
                        }
                    },
                    {
                        title: "120日留存",
                        dataIndex: "c120",
                        align: "center",
                        width: "0",
                        customRender: (text, record) => {
                            return this.countRate(record.c120, record.registerNum);
                        }
                    }
                ];
            } else {
                this.columns = [
                    {
                        title: "#",
                        dataIndex: "",
                        width: "100",
                        align: "center",
                        customRender: function (t, r, index) {
                            return parseInt(index) + 1;
                        }
                    },
                    {
                        title: "档位",
                        dataIndex: "countDate",
                        width: "120",
                        align: "center",
                        customRender: function (text) {
                            return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
                        }
                    }
                ];
            }
            for (var j = 0; j < a.length; j++) {
                let b = a[j].split("-");
                if (b.length > 1) {
                    this.columns.push({
                        title: "新增玩家",
                        dataIndex: "registerNum",
                        align: "center",
                        width: "120"
                    });

                    this.columns.push({
                        title: "2日留存",
                        dataIndex: "c2",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c2, record.registerNum);
                        }
                    });

                    this.columns.push({
                        title: "3日留存",
                        dataIndex: "c3",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c3, record.registerNum);
                        }
                    });

                    this.columns.push({
                        title: "4日留存",
                        dataIndex: "c4",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c4, record.registerNum);
                        }
                    });

                    this.columns.push({
                        title: "5日留存",
                        dataIndex: "c5",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c5, record.registerNum);
                        }
                    });

                    this.columns.push({
                        title: "6日留存",
                        dataIndex: "c6",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c6, record.registerNum);
                        }
                    });

                    this.columns.push({
                        title: "7日留存",
                        dataIndex: "c7",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c7, record.registerNum);
                        }
                    });
                } else if (a[j] == 15) {
                    this.columns.push({
                        title: "15日留存",
                        dataIndex: "c15",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c15, record.registerNum);
                        }
                    });
                } else if (a[j] == 30) {
                    this.columns.push({
                        title: "30日留存",
                        dataIndex: "c30",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c30, record.registerNum);
                        }
                    });
                } else if (a[j] == 60) {
                    this.columns.push({
                        title: "60日留存",
                        dataIndex: "c60",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c60, record.registerNum);
                        }
                    });
                } else if (a[j] == 90) {
                    this.columns.push({
                        title: "90日留存",
                        dataIndex: "c90",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c90, record.registerNum);
                        }
                    });
                } else if (a[j] == 120) {
                    this.columns.push({
                        title: "120日留存",
                        dataIndex: "c120",
                        align: "center",
                        width: "120",
                        customRender: (text, record) => {
                            return this.countRate(record.c120, record.registerNum);
                        }
                    });
                }
            }

// 动态改变表头end---------------------------------------------------------
            let param = {
                days: this.queryParam.days,
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                type: this.queryParam.type
            };
            getAction(this.url.list, param).then(res => {
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
        countRate: function(n, r) {
            if (n === null || n === undefined) {
                return "--";
            }

            let rate = r > 0 ? parseFloat(n / r) : 0;
            return Number(parseFloat(rate * 100).toFixed(2)) + "%";
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
