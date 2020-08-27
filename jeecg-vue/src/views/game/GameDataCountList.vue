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
        <div><a-table ref="table" size="middle" bordered rowKey="id" :loading="loading" :columns="columns" :dataSource="dataSource" :pagination="ipagination" @change="handleTableChange"></a-table></div>
    </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import { filterObj } from '@/utils/util';
import { getAction } from '@/api/manage';

export default {
    description: '日常数据',
    name: 'GameDataCountList',
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
                    width: 100,
                    align: 'center',
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '渠道',
                    dataIndex: 'channel',
                    key: 'channel',
                    width: 100
                },
                {
                    title: '服务器',
                    dataIndex: 'serverId',
                    key: 'serverId',
                    width: 100
                },
                {
                    title: '每天数据',
                    children: [
                        {
                            title: '每天登陆角色数',
                            dataIndex: 'loginPlayerNum',
                            key: 'loginPlayerNum',
                            width: 180
                        },
                        {
                            title: '每天付费金额',
                            dataIndex: 'payAmount',
                            key: 'payAmount',
                            width: 180
                        },
                        {
                            title: '每天付费角色数',
                            dataIndex: 'payPlayerNum',
                            key: 'payPlayerNum',
                            width: 180
                        },
                        {
                            title: '每天付费率',
                            dataIndex: 'payRate',
                            key: 'payRate',
                            width: 180
                        },
                        {
                            title: 'ARPU',
                            dataIndex: 'arpu',
                            key: 'arpu',
                            width: 180
                        },
                        {
                            title: 'ARPPU',
                            dataIndex: 'arppu',
                            key: 'arppu',
                            width: 180
                        }
                    ]
                },
                {
                    title: '新增数据',
                    children: [
                        {
                            title: '新增角色',
                            dataIndex: 'addPlayerNum',
                            key: 'addPlayerNum',
                            width: 180
                        },
                        {
                            title: '新增付费',
                            dataIndex: 'addPayAmount',
                            key: 'addPayAmount',
                            width: 180
                        },
                        {
                            title: '新增付费角色数',
                            dataIndex: 'addPayPlayerNum',
                            key: 'addPayPlayerNum',
                            width: 180
                        },
                        {
                            title: '新增付费率',
                            dataIndex: 'addPayRate',
                            key: 'addPayRate',
                            width: 180
                        },
                        {
                            title: '新增ARPU',
                            dataIndex: 'addArpu',
                            key: 'addArpu',
                            width: 180
                        },
                        {
                            title: '新增ARPPU',
                            dataIndex: 'addArppu',
                            key: 'addArppu',
                            width: 180
                        },
                        {
                            title: '二次付费角色数',
                            dataIndex: 'doublePayPlayer',
                            key: 'doublePayPlayer',
                            width: 180
                        },
                        {
                            title: '二次付费率',
                            dataIndex: 'doublePayRate',
                            key: 'doublePayRate',
                            width: 180
                        }
                    ]
                },
                {
                    title: '日期',
                    dataIndex: 'countDate',
                    key: 'countDate',
                    width: 220,
                    align: 'center',
                    customRender: function(text) {
                        return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
                    }
                }
            ],
            url: {
                list: 'game/gameDataCountController/dayCount'
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
