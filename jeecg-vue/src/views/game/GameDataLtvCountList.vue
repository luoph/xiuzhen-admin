<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="45">
                    <a-col :md="10" :sm="8">
                        <!--@ = v-on:数据绑定 不是事件-->
                        <game-channel-server @SelectChannel="selectChannel" @SelectServer="selectServer"></game-channel-server>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="创建日期"><a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" /></a-form-item>
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
                rowKey="id"
                :loading="loading"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :scroll="{ x: 1500, y: 520 }"
                @change="handleTableChange"
            ></a-table>
        </div>
    </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import { filterObj } from '@/utils/util';
import { getAction } from '@/api/manage';

export default {
    description: '留存率',
    name: 'GameDataRemainList',
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 50,
                    align: 'center',
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '日期',
                    dataIndex: 'countDate',
                    key: 'countDate',
                    width: 100,
                    align: 'center',
                    customRender: function(text) {
                        return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
                    }
                },
                {
                    title: '新增角色',
                    dataIndex: 'registerNum',
                    key: 'registerNum',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV1',
                    dataIndex: 'd1Amount',
                    key: 'd1Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV2',
                    dataIndex: 'd2Amount',
                    key: 'd2Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV3',
                    dataIndex: 'd3Amount',
                    key: 'd3Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV4',
                    dataIndex: 'd4Amount',
                    key: 'd4Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV5',
                    dataIndex: 'd5Amount',
                    key: 'd5Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV6',
                    dataIndex: 'd6Amount',
                    key: 'd6Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV7',
                    dataIndex: 'd7Amount',
                    key: 'd7Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV14',
                    dataIndex: 'd14Amount',
                    key: 'd14Remain',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV21',
                    dataIndex: 'd21Amount',
                    key: 'd21Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV30',
                    dataIndex: 'd30Amount',
                    key: 'd30Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV60',
                    dataIndex: 'd60Amount',
                    key: 'd60Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV90',
                    dataIndex: 'd90Amount',
                    key: 'd90Amount',
                    align: 'center',
                    width: 80
                },
                {
                    title: 'LTV120',
                    dataIndex: 'd120Amount',
                    key: 'd120Amount',
                    align: 'center',
                    width: 80
                }
            ],
            url: {
                list: 'game/gameDataCountController/ltvCount'
            },
            dictOptions: {}
        };
    },
    computed: {},
    methods: {
        initDictConfig() {},
        selectChannel: function(channelId) {
            this.queryParam.channelId = channelId;
        },
        selectServer: function(serverId) {
            this.queryParam.serverId = serverId;
        },
        onDateChange: function(value, dateStr) {
            this.queryParam.rangeDateBegin = dateStr[0];
            this.queryParam.rangeDateEnd = dateStr[1];
        },
        searchQuery() {
            let param = {
                channelId: this.queryParam.channelId,
                serverId: this.queryParam.serverId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            };
            getAction(this.url.list, param).then(res => {
                this.dataSource = res.result.records;
            });
        }
    }
};
</script>

<style>
@import '~@assets/less/common.less';
</style>
