<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="标题">
                            <a-input placeholder="请输入标题" v-model="queryParam.noticeTitle"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item label="投放服务器">
                            <multiple-server-select v-model="queryParam.gameServerList" @changeSelect="change"></multiple-server-select>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="开始时间">
                                <j-date
                                    :show-time="true"
                                    date-format="YYYY-MM-DD HH:mm:ss"
                                    class="query-group-cust"
                                    placeholder="请选择开始时间"
                                    v-model="queryParam.beginTime_begin"
                                ></j-date>
                                <j-date
                                    :show-time="true"
                                    date-format="YYYY-MM-DD HH:mm:ss"
                                    class="query-group-cust"
                                    placeholder="请选择开始时间"
                                    v-model="queryParam.beginTime_end"
                                ></j-date>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="结束时间">
                                <j-date
                                    :show-time="true"
                                    date-format="YYYY-MM-DD HH:mm:ss"
                                    class="query-group-cust"
                                    placeholder="请选择结束时间"
                                    v-model="queryParam.endTime_begin"
                                ></j-date>
                                <j-date
                                    :show-time="true"
                                    date-format="YYYY-MM-DD HH:mm:ss"
                                    class="query-group-cust"
                                    placeholder="请选择结束时间"
                                    v-model="queryParam.endTime_end"
                                ></j-date>
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :md="6" :sm="8">
                        <span style="float: left; overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                            <a-button type="primary" icon="reload" style="margin-left: 8px;" @click="searchReset">重置</a-button>
                            <a style="margin-left: 8px;" @click="handleToggleSearch">
                                {{ toggleSearchStatus ? "收起" : "展开" }}
                                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                            </a>
                        </span>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
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
                :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"
            >
                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px; font-style: italic;">无此图片</span>
                    <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width: 80px; font-size: 12px; font-style: italic;" />
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span v-if="!text" style="font-size: 12px; font-style: italic;">无此文件</span>
                    <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="uploadFile(text)"> 下载 </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                </span>
            </a-table>
        </div>

        <gameLampNotice-modal ref="modalForm" @ok="modalFormOk"></gameLampNotice-modal>
    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import GameLampNoticeModal from "./modules/GameLampNoticeModal";
import JDate from "@/components/jeecg/JDate.vue";
import MultipleServerSelect from "@/components/gameserver/MultipleServerSelect";

export default {
    name: "GameLampNoticeList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameLampNoticeModal,
        MultipleServerSelect
    },
    data() {
        return {
            description: "跑马灯消息管理页面",
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
                    title: "标题",
                    align: "center",
                    dataIndex: "noticeTitle"
                },
                {
                    title: "正文",
                    align: "center",
                    dataIndex: "noticeText"
                },
                {
                    title: "投放服务器",
                    align: "center",
                    dataIndex: "gameServerList"
                },
                {
                    title: "播放频率",
                    align: "center",
                    dataIndex: "frequency"
                },
                {
                    title: "循环播放周期",
                    align: "center",
                    dataIndex: "cyclePeriod"
                },
                {
                    title: "开始时间",
                    align: "center",
                    dataIndex: "beginTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 20 ? text.substr(0, 20) : text;
                    }
                },
                {
                    title: "结束时间",
                    align: "center",
                    dataIndex: "endTime",
                    customRender: function (text) {
                        return !text ? "" : text.length > 20 ? text.substr(0, 20) : text;
                    }
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    fixed: "right",
                    width: 147,
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "game/gameLampNotice/list"
            }
        };
    },
    computed: {},
    methods: {
        initDictConfig() {},
        change(value) {
            console.log("value" + value);
            this.queryParam.gameServerList = "*" + value.join(",") + "*";
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>