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
                    dataIndex: 'd1AmountRate',
                    key: 'd1AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d1Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV2',
                    dataIndex: 'd2AmountRate',
                    key: 'd2AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d2Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV3',
                    dataIndex: 'd3AmountRate',
                    key: 'd3AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d3Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV4',
                    dataIndex: 'd4AmountRate',
                    key: 'd4AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d4Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV5',
                    dataIndex: 'd5AmountRate',
                    key: 'd5AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d5Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV6',
                    dataIndex: 'd6AmountRate',
                    key: 'd6AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d6Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV7',
                    dataIndex: 'd7AmountRate',
                    key: 'd7AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d7Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV14',
                    dataIndex: 'd14AmountRata',
                    key: 'd14AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d14Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV21',
                    dataIndex: 'd21AmountRate',
                    key: 'd21AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d21Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV30',
                    dataIndex: 'd30AmountRate',
                    key: 'd30AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d30Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV60',
                    dataIndex: 'd60AmountRate',
                    key: 'd60AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d60Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV90',
                    dataIndex: 'd90AmountRate',
                    key: 'd90AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d90Amount, record.registerNum);
                    }
                },
                {
                    title: 'LTV120',
                    dataIndex: 'd120AmountRate',
                    key: 'd120AmountRate',
                    align: 'center',
                    width: 80,
                    customRender: (text, record) => {
                        return this.countRate(record.d120Amount, record.registerNum);
                    }
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
