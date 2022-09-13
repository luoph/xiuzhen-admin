<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="12" :sm="16">
            <!--@ = v-on:数据绑定 不是事件-->
            <game-channel-server @onSelectChannel="onSelectChannel" @onSelectServer="onSelectServer"/>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="角色昵称">
              <a-input placeholder="请输入角色昵称" v-model="queryParam.nickname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="角色id">
              <a-input placeholder="请输入角色id" v-model="queryParam.playerId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="创建日期">
              <a-range-picker format="YYYY-MM-DD" :placeholder="['开始日期', '结束日期']" @change="onDateChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="选择就近天数">
              <a-select placeholder="天数" v-model="queryParam.daysType">
                <a-select-option value="0">不选择天数</a-select-option>
                <a-select-option value="7">近7天</a-select-option>
                <a-select-option value="15">近15天</a-select-option>
                <a-select-option value="30">近一个月</a-select-option>
                <a-select-option value="60">近两个月</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('玩家行为分析')">导出</a-button>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :rowKey="(t, r, index) => (parseInt(index) + 1)"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: true }"
        @change="handleTableChange"
      >
      </a-table>
    </div>
  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin';
import JDate from '@/components/jeecg/JDate.vue';
import GameChannelServer from '@/components/gameserver/GameChannelServer';
import {getAction} from '@/api/manage';

export default {
  name: 'PlayerBehaviorList',
  mixins: [JeecgListMixin],
  components: {
    JDate,
    GameChannelServer,
    getAction
  },
  data() {
    return {
      description: '玩家行为分析',
      disableMixinCreated: true, // 禁止初始化加载数据
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: '4%',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '日期',
          align: 'center',
          width: '10%',
          dataIndex: 'createDate'
        },
        {
          title: '区服ID',
          align: 'center',
          width: '5%',
          dataIndex: 'serverId'
        },
        {
          title: '玩家ID',
          align: 'center',
          width: '10%',
          dataIndex: 'playerId'
        },
        {
          title: '玩家名字',
          align: 'center',
          width: '5%',
          dataIndex: 'nickname'
        },
        {
          title: '玩家等级',
          align: 'center',
          width: '5%',
          dataIndex: 'playerLevel'
        },
        {
          title: '战力',
          align: 'center',
          width: '5%',
          dataIndex: 'combatPower'
        },
        {
          title: '修炼年数',
          align: 'center',
          width: '5%',
          dataIndex: 'practiceYear'
        },
        {
          title: '境界等级',
          align: 'center',
          width: '5%',
          dataIndex: 'reamLevel'
        },
        {
          title: '福地夺宝/斗法',
          align: 'center',
          width: '5%',
          dataIndex: 'arenaBattle'
        },
        {
          title: '仙兽秘境',
          align: 'center',
          width: '5%',
          dataIndex: 'mapExplore'
        },
        {
          title: '上古遗迹',
          align: 'center',
          width: '5%',
          dataIndex: 'travelHill'
        },
        {
          title: '剧情关数',
          align: 'center',
          width: '5%',
          dataIndex: 'mainStoryLevel'
        },
        {
          title: '蛇陵魔窟（灵界器灵）',
          align: 'center',
          width: '5%',
          dataIndex: 'spiritualWorldBoss'
        },
        {
          title: '魔王入侵（世界boss）',
          align: 'center',
          width: '5%',
          dataIndex: 'worldBoss'
        },
        {
          title: '北冥魔海',
          align: 'center',
          width: '5%',
          dataIndex: 'mainStoryBoss'
        },
        {
          title: '符文秘境-小灵山',
          align: 'center',
          width: '5%',
          dataIndex: 'lingShanLevel'
        },
        {
          title: '剧情小关卡',
          align: 'center',
          width: '5%',
          dataIndex: 'mainStoryCheck'
        },
        {
          title: '神魔战场',
          align: 'center',
          width: '5%',
          dataIndex: 'ghostWar'
        },
        {
          title: '丹药秘境',
          align: 'center',
          width: '5%',
          dataIndex: 'godRoad'
        },
        {
          title: '仙器秘境',
          align: 'center',
          width: '5%',
          dataIndex: 'tierMapExplore'
        },
        {
          title: '不死魔巢',
          align: 'center',
          width: '5%',
          dataIndex: 'petBoss'
        },
        {
          title: '仙盟仙宴',
          align: 'center',
          width: '5%',
          dataIndex: 'factionBanquet'
        },
        {
          title: '当天充值',
          align: 'center',
          width: '5%',
          dataIndex: 'recharge'
        },
        {
          title: '玉髓消耗',
          align: 'center',
          width: '5%',
          dataIndex: 'consumeMoney'
        },
        {
          title: '阅历值',
          align: 'center',
          width: '5%',
          dataIndex: 'experience'
        },
        {
          title: '仙缘双休',
          align: 'center',
          width: '5%',
          dataIndex: 'matePractice'
        },
        {
          title: '仙缘试炼',
          align: 'center',
          width: '5%',
          dataIndex: 'mateBoss'
        },
        {
          title: '在线时长',
          align: 'center',
          width: '5%',
          dataIndex: 'onlineTime'
        },
        {
          title: '玩家修为',
          align: 'center',
          width: '5%',
          dataIndex: 'practiceValue'
        },
        {
          title: '极品灵石获得',
          align: 'center',
          width: '5%',
          dataIndex: 'gainRareStone'
        },
        {
          title: '极品灵石消耗',
          align: 'center',
          width: '5%',
          dataIndex: 'costRareStone'
        },
        {
          title: '组队BOSS/须弥幻境',
          align: 'center',
          width: '5%',
          dataIndex: 'teamBoss'
        },
        {
          title: '金兰副本',
          align: 'center',
          width: '5%',
          dataIndex: 'marryBoss'
        },
        {
          title: '谪仙台',
          align: 'center',
          width: '5%',
          dataIndex: 'qualifying'
        },
        {
          title: '灵石秘境',
          align: 'center',
          width: '5%',
          dataIndex: 'spiritStoneMapExplore'
        },
        {
          title: '修为秘境',
          align: 'center',
          width: '5%',
          dataIndex: 'practiceMapExplore'
        },
        {
          title: '法宝秘境',
          align: 'center',
          width: '5%',
          dataIndex: 'weaponMapExplore'
        },
        {
          title: '八荒天魔一键挑战',
          align: 'center',
          width: '5%',
          dataIndex: 'immortalHigh'
        },
        {
          title: '九天魔君一键挑战',
          align: 'center',
          width: '5%',
          dataIndex: 'immortalLow'
        },
        {
          title: '至尊月卡一键挑战',
          align: 'center',
          width: '5%',
          dataIndex: 'monthCardHigh'
        },
        {
          title: '特惠月卡一键挑战',
          align: 'center',
          width: '5%',
          dataIndex: 'monthCardLow'
        },
        {
          title: '成功炼丹次数',
          align: 'center',
          width: '5%',
          dataIndex: 'refineDan'
        },
        {
          title: '成功炼器次数',
          align: 'center',
          width: '5%',
          dataIndex: 'refineEquip'
        },
        {
          title: '灵石秘境扫荡',
          align: 'center',
          width: '5%',
          dataIndex: 'spiritStoneMapSweep'
        },
        {
          title: '修为秘境扫荡',
          align: 'center',
          width: '5%',
          dataIndex: 'practiceMapSweep'
        },
        {
          title: '仙器秘境扫荡',
          align: 'center',
          width: '5%',
          dataIndex: 'tierMapSweep'
        },
        {
          title: '法宝秘境扫荡',
          align: 'center',
          width: '5%',
          dataIndex: 'weaponMapSweep'
        }
      ],
      url: {
        list: 'game/player/behavior',
        exportXlsUrl: 'game/player/exportXls'
      },
      dictOptions: {}
    };
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domainURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    initDictConfig() {
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
    }
  }
};
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
