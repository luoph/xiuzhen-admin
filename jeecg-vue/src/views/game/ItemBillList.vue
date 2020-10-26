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
                    <a-col :md="4" :sm="4">
                        <a-form-item label="玩家id">
                            <a-input placeholder="请输入玩家id" v-model="queryParam.playerId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="4" :sm="4">
                        <a-form-item label="物品id">
                            <a-input placeholder="请输入物品id" v-model="queryParam.itemId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="创建日期">
                            <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="3" :sm="3">
                        <a-form-item label="产销类型">
                            <a-select placeholder="产销" v-model="queryParam.type">
                                <a-select-option value="0">全部</a-select-option>
                                <a-select-option value="1">产出</a-select-option>
                                <a-select-option value="2">消耗</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="5" :sm="5">
                        <a-form-item label="产销点">
                            <a-select placeholder="产销点" v-model="queryParam.way">
                                <a-select-option value="0">缺省默认系统</a-select-option>
                                <a-select-option value="1">洞府-灵田-生产</a-select-option>
                                <a-select-option value="2">洞府-灵田-升级</a-select-option>
                                <a-select-option value="3">洞府-灵根-灵根升级</a-select-option>
                                <a-select-option value="4">洞府-灵根-聚灵珠升级</a-select-option>
                                <a-select-option value="5">洞府-功法-学习功法</a-select-option>
                                <a-select-option value="6">洞府-功法-功法升级</a-select-option>
                                <a-select-option value="7">洞府-功法-推演</a-select-option>
                                <a-select-option value="8">洞府-炼丹-学习丹方</a-select-option>
                                <a-select-option value="9">洞府-炼丹-炼丹</a-select-option>
                                <a-select-option value="10">洞府-炼器-学习图纸</a-select-option>
                                <a-select-option value="11">洞府-炼器-炼器</a-select-option>
                                <a-select-option value="12">洞府-合成"</a-select-option>
                                <a-select-option value="13">法宝-激活法宝</a-select-option>
                                <a-select-option value="14">法宝-精炼</a-select-option>
                                <a-select-option value="15">法宝-精粹</a-select-option>
                                <a-select-option value="16">法宝-套装锻造</a-select-option>
                                <a-select-option value="17">法宝-拓星</a-select-option>
                                <a-select-option value="18">法宝-符文-快捷合成</a-select-option>
                                <a-select-option value="19">法宝-符文精炼</a-select-option>
                                <a-select-option value="20">法宝-符文拓星</a-select-option>
                                <a-select-option value="21">法宝-铸灵</a-select-option>
                                <a-select-option value="22">法宝-追加</a-select-option>
                                <a-select-option value="23">仙兽-激活</a-select-option>
                                <a-select-option value="24">仙兽-喂养</a-select-option>
                                <a-select-option value="25">仙兽-觉醒</a-select-option>
                                <a-select-option value="26">仙兽-装备激活</a-select-option>
                                <a-select-option value="27">仙兽-装备升级</a-select-option>
                                <a-select-option value="28">仙兽-仙兽宫</a-select-option>
                                <a-select-option value="29">仙盟-大殿-贡献</a-select-option>
                                <a-select-option value="30">仙盟-仙宴</a-select-option>
                                <a-select-option value="31">仙盟-仓库</a-select-option>
                                <a-select-option value="32">储物袋-道具使用</a-select-option>
                                <a-select-option value="33">储物袋-道具出售</a-select-option>
                                <a-select-option value="34">储物袋-储物袋升级</a-select-option>
                                <a-select-option value="35">储物袋-炼体-聚灵</a-select-option>
                                <a-select-option value="36">储物袋-炼体-天命冲穴</a-select-option>
                                <a-select-option value="37">储物袋-灵宝升阶</a-select-option>
                                <a-select-option value="38">储物袋-仙命-仙术推演</a-select-option>
                                <a-select-option value="39">储物袋-仙命-命盘加点</a-select-option>
                                <a-select-option value="40">储物袋-时装-激活</a-select-option>
                                <a-select-option value="41">储物袋-时装-升级</a-select-option>
                                <a-select-option value="42">储物袋-圣灵-激活</a-select-option>
                                <a-select-option value="43">储物袋-圣灵-升阶</a-select-option>
                                <a-select-option value="44">储物袋-飞剑-打磨</a-select-option>
                                <a-select-option value="45">储物袋-飞剑-重铸</a-select-option>
                                <a-select-option value="46">储物袋-飞剑-精粹</a-select-option>
                                <a-select-option value="47">剧情-快速挂机</a-select-option>
                                <a-select-option value="48">福利-灵石商人</a-select-option>
                                <a-select-option value="49">节日活动-兑换</a-select-option>
                                <a-select-option value="50">仙福-特惠礼包</a-select-option>
                                <a-select-option value="51">开服排行-优惠礼包</a-select-option>
                                <a-select-option value="52">仙榜-称号激活称号</a-select-option>
                                <a-select-option value="53">渡劫-吃渡劫丹</a-select-option>
                                <a-select-option value="54">斗法-福地夺宝-购买次数</a-select-option>
                                <a-select-option value="55">斗法-福地夺宝-刷新对手</a-select-option>
                                <a-select-option value="56">斗法-福地夺宝-清除冷却</a-select-option>
                                <a-select-option value="57">斗法-福地夺宝-兑换</a-select-option>
                                <a-select-option value="58">斗法-上古遗迹-提升品质</a-select-option>
                                <a-select-option value="59">斗法-神魔战场-激励</a-select-option>
                                <a-select-option value="60">斗法-神魔战场-复活</a-select-option>
                                <a-select-option value="61">封魔-法宝器灵-购买次数</a-select-option>
                                <a-select-option value="62">封魔-灵宝幻魔-购买次数</a-select-option>
                                <a-select-option value="63">封魔-魔王入侵-清除冷却</a-select-option>
                                <a-select-option value="64">秘境-仙兽秘境-冒险</a-select-option>
                                <a-select-option value="65">秘境-仙兽秘境-快速通关</a-select-option>
                                <a-select-option value="66">秘境-仙兽秘境-神游</a-select-option>
                                <a-select-option value="67">秘境-仙兽秘境-快速神游</a-select-option>
                                <a-select-option value="68">秘境-丹药秘境-商店</a-select-option>
                                <a-select-option value="69">秘境-丹药秘境-复活</a-select-option>
                                <a-select-option value="70">七日目标-每日福利-购买</a-select-option>
                                <a-select-option value="71">日常-灵石找回</a-select-option>
                                <a-select-option value="72">日常-仙玉找回</a-select-option>
                                <a-select-option value="73">坊市-购买</a-select-option>
                                <a-select-option value="74">飞升仙界-仙脉消耗</a-select-option>
                                <a-select-option value="75">仙尊-购买次数（飞升仙界boss）</a-select-option>
                                <a-select-option value="76">法宝炼灵</a-select-option>
                                <a-select-option value="77">炼灵玄妖（灵界器灵）</a-select-option>
                                <a-select-option value="78">仙盟——仙盟妖兽-挑战（仙盟boss）</a-select-option>
                                <a-select-option value="79">储物袋-圣灵-升级</a-select-option>
                                <a-select-option value="80">储物袋-圣灵-激活化形</a-select-option>
                                <a-select-option value="82">储物袋-圣灵-精粹</a-select-option>
                                <a-select-option value="83">套装</a-select-option>
                                <a-select-option value="84">套装秘境</a-select-option>
                                <a-select-option value="85">鉴宝</a-select-option>
                                <a-select-option value="86">境界每日礼包</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :md="4" :sm="8">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
                        </span>
                    </a-col>
                </a-row>
            </a-form>

            <div class="table-operator">
                <a-button type="primary" icon="download" @click="handleExportXls('物品流水')">导出</a-button>
            </div>
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
                :rowSelection="{ fixed: true, selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                @change="handleTableChange"

            >

            </a-table>
        </div>

    </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate.vue";
import GameChannelServer from "@/components/gameserver/GameChannelServer";
import { filterObj } from "@/utils/util";
import { getAction } from "@/api/manage";

export default {
    name: "ItemBillList",
    mixins: [JeecgListMixin],
    components: {
        JDate,
        GameChannelServer,
        getAction
    },
    data() {
        return {
            description: "物品流水管理页面",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "玩家ID",
                    align: "center",
                    dataIndex: "playerId"
                },
                {
                    title: "玩家名",
                    align: "center",
                    dataIndex: "playerName"
                },
                {
                    title: "物品ID",
                    align: "center",
                    dataIndex: "itemId"
                },
                {
                    title: "物品名称",
                    align: "center",
                    dataIndex: "itemName"
                },
                {
                    title: "产销类型",
                    align: "center",
                    dataIndex: "typeName"
                },
                {
                    title: "产销点",
                    align: "center",
                    dataIndex: "wayName"
                },
                {
                    title: "变更数量",
                    align: "center",
                    dataIndex: "num"
                },
                {
                    title: "原数量",
                    align: "center",
                    dataIndex: "beforeNum"
                },
                {
                    title: "现数量",
                    align: "center",
                    dataIndex: "afterNum"
                },
                {
                    title: "操作时间",
                    align: "center",
                    dataIndex: "syncTime"
                },
            ],
            url: {
                list: "player/playerItemLog/itemBillList",
            },
            dictOptions: {
            }
        };
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
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
            let param = {
                way: this.queryParam.way,
                itemId: this.queryParam.itemId,
                type: this.queryParam.type,
                serverId: this.queryParam.serverId,
                playerId: this.queryParam.playerId,
                rangeDateBegin: this.queryParam.rangeDateBegin,
                rangeDateEnd: this.queryParam.rangeDateEnd,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
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
        }
    }
};
</script>

<style scoped>
@import "~@assets/less/common.less";
</style>
