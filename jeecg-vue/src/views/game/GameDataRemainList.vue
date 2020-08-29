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
                    width: 120,
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
                    width: 120
                },
                {
                    title: '首日免费角色',
                    dataIndex: 'freeNum',
                    key: 'freeNum',
                    align: 'center',
                    width: 120
                },
                {
                    title: '首日付费角色',
                    dataIndex: 'payNum',
                    key: 'payNum',
                    align: 'center',
                    width: 120
                },
                {
                    title: '首日付费率',
                    dataIndex: 'payRate',
                    key: 'payRate',
                    align: 'center',
                    width: 120,
                    customRender: (text, record) => {
                        return this.countRate(record.payNum, record.registerNum);
                    }
                },
                {
                    title: '免费角色次留率',
                    dataIndex: 'freeRemainRate',
                    key: 'freeRemainRate',
                    align: 'center',
                    width: 120,
                    customRender: (text, record) => {
                        return this.countRate(record.freeRemain, record.freeNum);
                    }
                },
                {
                    title: '付费角色次留率',
                    dataIndex: 'payRemainRate',
                    key: 'payRemainRate',
                    align: 'center',
                    width: 120,
                    customRender: (text, record) => {
                        return this.countRate(record.payRemain, record.payNum);
                    }
                },
                {
                    title: '次留率',
                    dataIndex: 'd2RemainRate',
                    key: 'd2RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d2Remain, record.registerNum);
                    }
                },
                {
                    title: '3留率',
                    dataIndex: 'd3RemainRate',
                    key: 'd3RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d3Remain, record.registerNum);
                    }
                },
                {
                    title: '4留率',
                    dataIndex: 'd4RemainRate',
                    key: 'd4RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d4Remain, record.registerNum);
                    }
                },
                {
                    title: '5留率',
                    dataIndex: 'd5RemainRate',
                    key: 'd5RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d5Remain, record.registerNum);
                    }
                },
                {
                    title: '6留率',
                    dataIndex: 'd6RemainRate',
                    key: 'd6RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d6Remain, record.registerNum);
                    }
                },
                {
                    title: '7留率',
                    dataIndex: 'd7RemainRate',
                    key: 'd7RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d7Remain, record.registerNum);
                    }
                },
                {
                    title: '15留率',
                    dataIndex: 'd15RemainRate',
                    key: 'd15RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d15Remain, record.registerNum);
                    }
                },
                {
                    title: '30留率',
                    dataIndex: 'd30RemainRate',
                    key: 'd30RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d30Remain, record.registerNum);
                    }
                },
                {
                    title: '60留率',
                    dataIndex: 'd60RemainRate',
                    key: 'd60RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d60Remain, record.registerNum);
                    }
                },
                {
                    title: '90留率',
                    dataIndex: 'd90RemainRate',
                    key: 'd90RemainRate',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d90Remain, record.registerNum);
                    }
                },
                {
                    title: '120留率',
                    dataIndex: 'd120Remain',
                    key: 'd120Remain',
                    align: 'center',
                    width: 100,
                    customRender: (text, record) => {
                        return this.countRate(record.d120Remain, record.registerNum);
                    }
                }
            ],
            url: {
                list: 'game/gameDataCountController/remainRate'
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
        },
        countRate: function(n, r) {
            return r > 0 ? parseFloat((n / r) * 100).toFixed(2) : parseFloat(0).toFixed(2);
        }
    }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
