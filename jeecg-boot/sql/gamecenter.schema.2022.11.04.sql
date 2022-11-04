#
************************************************************
# Sequel Ace SQL dump
# 版本号
： 20039
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 10.21.213.217 (MySQL 5.7.38-41-log)
# 数据库: gamecenter
# 生成时间: 2022-11-04 03:47:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


#
转储表 ces_order_customer
# ------------------------------------------------------------

CREATE TABLE `ces_order_customer`
(
    `id`            varchar(36) NOT NULL,
    `create_by`     varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`     varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code`  varchar(64)  DEFAULT NULL COMMENT '所属部门',
    `name`          varchar(32)  DEFAULT NULL COMMENT '客户名字',
    `sex`           varchar(1)   DEFAULT NULL COMMENT '客户性别',
    `birthday`      datetime     DEFAULT NULL COMMENT '客户生日',
    `age`           int(8) DEFAULT NULL COMMENT '年龄',
    `address`       varchar(300) DEFAULT NULL COMMENT '常用地址',
    `order_main_id` varchar(32)  DEFAULT NULL COMMENT '订单ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 ces_order_goods
# ------------------------------------------------------------

CREATE TABLE `ces_order_goods`
(
    `id`            varchar(36) NOT NULL,
    `create_by`     varchar(50) DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime    DEFAULT NULL COMMENT '创建日期',
    `update_by`     varchar(50) DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime    DEFAULT NULL COMMENT '更新日期',
    `sys_org_code`  varchar(64) DEFAULT NULL COMMENT '所属部门',
    `good_name`     varchar(32) DEFAULT NULL COMMENT '商品名字',
    `price`         double      DEFAULT NULL COMMENT '价格',
    `num`           int(8) DEFAULT NULL COMMENT '数量',
    `zong_price`    double      DEFAULT NULL COMMENT '单品总价',
    `order_main_id` varchar(32) DEFAULT NULL COMMENT '订单ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 ces_order_main
# ------------------------------------------------------------

CREATE TABLE `ces_order_main`
(
    `id`           varchar(36) NOT NULL,
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    `order_code`   varchar(32)  DEFAULT NULL COMMENT '订单编码',
    `xd_date`      datetime     DEFAULT NULL COMMENT '下单时间',
    `money`        double       DEFAULT NULL COMMENT '订单总额',
    `remark`       varchar(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 ces_shop_goods
# ------------------------------------------------------------

CREATE TABLE `ces_shop_goods`
(
    `id`           varchar(36) NOT NULL COMMENT '主键',
    `create_by`    varchar(50)    DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime       DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)    DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime       DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)    DEFAULT NULL COMMENT '所属部门',
    `name`         varchar(32)    DEFAULT NULL COMMENT '商品名字',
    `price`        decimal(10, 5) DEFAULT NULL COMMENT '价格',
    `chuc_date`    datetime       DEFAULT NULL COMMENT '出厂时间',
    `contents`     text COMMENT '商品简介',
    `good_type_id` varchar(32)    DEFAULT NULL COMMENT '商品分类',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 ces_shop_type
# ------------------------------------------------------------

CREATE TABLE `ces_shop_type`
(
    `id`           varchar(36) NOT NULL,
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    `name`         varchar(32)  DEFAULT NULL COMMENT '分类名字',
    `content`      varchar(200) DEFAULT NULL COMMENT '描述',
    `pics`         varchar(500) DEFAULT NULL COMMENT '图片',
    `pid`          varchar(32)  DEFAULT NULL COMMENT '父级节点',
    `has_child`    varchar(3)   DEFAULT NULL COMMENT '是否有子节点',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 demo
# ------------------------------------------------------------

CREATE TABLE `demo`
(
    `id`           varchar(50) NOT NULL COMMENT '主键ID',
    `name`         varchar(30)    DEFAULT NULL COMMENT '姓名',
    `key_word`     varchar(255)   DEFAULT NULL COMMENT '关键词',
    `punch_time`   datetime       DEFAULT NULL COMMENT '打卡时间',
    `salary_money` decimal(10, 3) DEFAULT NULL COMMENT '工资',
    `bonus_money`  double(10, 2
) DEFAULT NULL COMMENT '奖金',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别 {男:1,女:2}',
  `age` int(8) DEFAULT NULL COMMENT '年龄',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `content` varchar(1000) DEFAULT NULL COMMENT '个人简介',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门编码',
  `tenant_id` int(8) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 demo_field_def_val_main
# ------------------------------------------------------------

CREATE TABLE `demo_field_def_val_main`
(
    `id`            varchar(36) NOT NULL,
    `code`          varchar(200) DEFAULT NULL COMMENT '编码',
    `name`          varchar(200) DEFAULT NULL COMMENT '姓名',
    `sex`           varchar(200) DEFAULT NULL COMMENT '性别',
    `address`       varchar(200) DEFAULT NULL COMMENT '地址',
    `address_param` varchar(32)  DEFAULT NULL COMMENT '地址（传参）',
    `create_by`     varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`     varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code`  varchar(64)  DEFAULT NULL COMMENT '所属部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 demo_field_def_val_sub
# ------------------------------------------------------------

CREATE TABLE `demo_field_def_val_sub`
(
    `id`           varchar(36) NOT NULL,
    `code`         varchar(200) DEFAULT NULL COMMENT '编码',
    `name`         varchar(200) DEFAULT NULL COMMENT '名称',
    `date`         varchar(200) DEFAULT NULL COMMENT '日期',
    `main_id`      varchar(200) DEFAULT NULL COMMENT '主表ID',
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 game_activity
# ------------------------------------------------------------

CREATE TABLE `game_activity`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
    `activity`      varchar(64)  DEFAULT NULL COMMENT '活动唯一标识, e.g. "buff", "worldboss"',
    `name`          varchar(50)  DEFAULT NULL COMMENT '活动名称',
    `slogan`        varchar(100) DEFAULT NULL COMMENT '活动标语',
    `icon`          varchar(100) DEFAULT NULL COMMENT '活动入口的icon',
    `status`        int(8) DEFAULT NULL COMMENT '活动状态: 0.关闭, 1.开启',
    `start_rumor`   int(8) DEFAULT NULL COMMENT '开始时的传闻id',
    `end_rumor`     int(8) DEFAULT NULL COMMENT '结束时的传闻id',
    `icon_display`  int(8) DEFAULT NULL COMMENT '0：图标常驻, 1：预告时才显示，平时隐藏',
    `notice_time`   int(8) DEFAULT NULL COMMENT '提前预告时间（秒）',
    `notice_period` int(8) DEFAULT NULL COMMENT '跑马灯显示周期(秒)，0表示不显示跑马灯',
    `start_time`    datetime     DEFAULT NULL COMMENT '开始时间',
    `end_time`      datetime     DEFAULT NULL COMMENT '结束时间',
    `custom`        varchar(255) DEFAULT NULL COMMENT '自定义json参数. e.g. {"buff":"1,2"}',
    `remark`        varchar(255) DEFAULT NULL COMMENT '备注',
    `create_time`   datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY             `IDX_ACTIVITY` (`activity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动表';



#
转储表 game_anti_addiction
# ------------------------------------------------------------

CREATE TABLE `game_anti_addiction`
(
    `id`                     bigint(11) NOT NULL AUTO_INCREMENT,
    `kid_age`                int(8) NOT NULL DEFAULT '8' COMMENT '小孩年龄',
    `teen_age`               int(8) NOT NULL DEFAULT '16' COMMENT '少年年龄',
    `adult_age`              int(8) NOT NULL DEFAULT '18' COMMENT '成年年龄',
    `hour_limit_start`       varchar(40)                        NOT NULL DEFAULT '22' COMMENT '开始限制时间 HH:mm',
    `hour_limit_end`         varchar(40)                        NOT NULL DEFAULT '8' COMMENT '结束限制时间 HH:mm',
    `hour_limit_tip`         varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '每日限制提示',
    `visitor_duration`       int(8) NOT NULL DEFAULT '0' COMMENT '游客累计可玩时间（秒）',
    `visitor_limit_interval` int(8) NOT NULL DEFAULT '15' COMMENT '游客限制间隔（天数）',
    `visitor_tip`            varchar(100) CHARACTER SET utf8mb4          DEFAULT '' COMMENT '游客限制提示',
    `duration_holiday`       int(8) NOT NULL DEFAULT '0' COMMENT '节假日累计可玩时间（秒）',
    `duration_workday`       int(8) NOT NULL DEFAULT '0' COMMENT '工作日累计可玩时间（秒）',
    `duration_tip`           varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '时长限制提示',
    `status`                 tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态 0-不可用，1-可用',
    `create_time`            datetime                                    DEFAULT NULL COMMENT '创建时间',
    `update_time`            datetime                                    DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='防沉迷配置';



#
转储表 game_app_update
# ------------------------------------------------------------

CREATE TABLE `game_app_update`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `game_id`        int(8) NOT NULL COMMENT '游戏id',
    `app_name`       varchar(100) DEFAULT NULL COMMENT '应用名称',
    `package_name`   varchar(100) DEFAULT NULL COMMENT '应用包名',
    `version_code`   int(8) DEFAULT NULL COMMENT '版本号',
    `version_name`   varchar(100) DEFAULT NULL COMMENT '版本名',
    `platform`       varchar(64)  DEFAULT NULL COMMENT '平台：ios/android',
    `channel`        varchar(64)  DEFAULT NULL COMMENT '包渠道',
    `download_url`   varchar(512) DEFAULT NULL COMMENT '下载地址',
    `update_title`   varchar(100) DEFAULT NULL COMMENT '更新标题',
    `update_content` varchar(512) DEFAULT NULL COMMENT '更新内容',
    `remark`         varchar(200) DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`    datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `IDX_PKG_CHANNEL_PLATFORM_VERSION` (`package_name`,`channel`,`platform`,`version_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端更新信息';



#
转储表 game_campaign
# ------------------------------------------------------------

CREATE TABLE `game_campaign`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
    `type`        int(8) NOT NULL DEFAULT '1' COMMENT '活动类型: 1.节日活动, 2.限时累充, 3.鉴宝, 4.战令',
    `name`        varchar(50) NOT NULL DEFAULT '' COMMENT '活动名称，后台显示',
    `description` varchar(100)         DEFAULT NULL COMMENT '活动标语（描述）',
    `show_name`   varchar(50)          DEFAULT NULL COMMENT '活动名称，游戏中显示',
    `server_ids`  varchar(1024)        DEFAULT NULL COMMENT '服务器id，使用,分割',
    `icon`        varchar(128)         DEFAULT NULL COMMENT '活动图标',
    `banner`      varchar(128)         DEFAULT NULL COMMENT '活动宣传图',
    `status`      tinyint(2) NOT NULL DEFAULT '1' COMMENT '活动状态: 0.关闭, 1.开启（备用字段，默认全部为开启）',
    `priority`    tinyint(2) NOT NULL DEFAULT '0' COMMENT '优先级',
    `auto_open`   tinyint(2) NOT NULL DEFAULT '0' COMMENT '到达活动时间后自动开启',
    `time_type`   tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`  datetime             DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime             DEFAULT NULL COMMENT '结束时间',
    `start_day`   int(8) DEFAULT '0' COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`    int(8) DEFAULT '0' COMMENT '持续时间(天)',
    `create_time` datetime             DEFAULT NULL,
    `create_by`   varchar(32)          DEFAULT NULL COMMENT '创建人',
    `update_time` datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`   varchar(32)          DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY           `idx_time_type_start_end` (`time_type`,`start_time`,`end_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动管理';



#
转储表 game_campaign_direct_purchase
# ------------------------------------------------------------

CREATE TABLE `game_campaign_direct_purchase`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `campaign_id` bigint(11) NOT NULL COMMENT '主活动id',
    `type_id`     bigint(11) NOT NULL COMMENT '子活动id',
    `limit_num`   int(8) NOT NULL COMMENT '限购数量',
    `goods_id`    int(8) NOT NULL COMMENT '商品id',
    `name`        varchar(256) NOT NULL DEFAULT '' COMMENT '礼包名',
    `type`        int(8) NOT NULL COMMENT '礼包组类型',
    `sort`        int(8) NOT NULL COMMENT '组排序',
    `color`       int(8) NOT NULL DEFAULT '1' COMMENT '图标颜色',
    `discount`    int(8) NOT NULL DEFAULT '0' COMMENT '礼包折扣',
    `reward`      text COMMENT '奖励列表',
    `min_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time` datetime              DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(256)          DEFAULT '' COMMENT '创建者',
    `update_time` datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(256)          DEFAULT '' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `INDEX_DIRECT_PURCHASE` (`campaign_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-直购礼包';



#
转储表 game_campaign_support
# ------------------------------------------------------------

CREATE TABLE `game_campaign_support`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT NULL COMMENT '活动类型id',
    `server_id`   int(8) DEFAULT '0' COMMENT '服务器id',
    `status`      tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否有效',
    `create_time` datetime                            DEFAULT NULL,
    `create_by`   varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                            DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`   varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `IDX_C_T_S` (`campaign_id`,`type_id`,`server_id`),
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE,
    KEY           `idx_server_id` (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动支持-渠道/服务器';



#
转储表 game_campaign_type
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type`
(
    `id`                  bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`         bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type`                int(8) DEFAULT '0' COMMENT '活动项类型: 1.登录礼包, 2.累计充值, 3.兑换, 4.节日任务, 5.buff-修为加成, 6.buff-灵气加成',
    `name`                varchar(50) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '活动名（页签）',
    `cross`               tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否跨服子活动',
    `time_type`           tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`          datetime                             DEFAULT NULL COMMENT '开始时间',
    `end_time`            datetime                             DEFAULT NULL COMMENT '结束时间',
    `start_day`           int(8) DEFAULT '0' COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`            int(8) DEFAULT '0' COMMENT '持续时间(天)',
    `type_image`          varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '活动类型图片',
    `sort`                int(8) DEFAULT '0' COMMENT '排序',
    `animation`           varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '活动动画',
    `eggs_integral_goods` text COLLATE utf8_unicode_ci COMMENT '活动类型是11-砸蛋积分商品',
    `res_type`            tinyint(2) DEFAULT NULL COMMENT '资源格式: 1.骨骼, 2.序列帧, 3.图片',
    `extra`               text COLLATE utf8_unicode_ci COMMENT '额外的配置，根据不同类型下发',
    `create_time`         datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`           varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`         datetime                             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`           varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型';



#
转储表 game_campaign_type_buff
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_buff`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `type`        int(8) DEFAULT '0' COMMENT '活动项类型: 5.修为加成 6.灵气加成',
    `start_time`  datetime                             DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime                             DEFAULT NULL COMMENT '结束时间',
    `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
    `addition`    int(8) DEFAULT '0' COMMENT '加成',
    `create_time` datetime                             DEFAULT NULL,
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-buff';



#
转储表 game_campaign_type_exchange
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_exchange`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`          bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `exchange_id`      int(8) DEFAULT '0' COMMENT '兑换id',
    `item_name`        varchar(50) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '道具名称',
    `max_exchange_num` int(8) DEFAULT '0' COMMENT '最大兑换数量',
    `reward`           varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表',
    `consume`          varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '消耗列表',
    `min_level`        int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`        int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time`      datetime                             DEFAULT NULL,
    `create_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime                             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-兑换';



#
转储表 game_campaign_type_fall
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_fall`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `module`      int(8) DEFAULT '0' COMMENT '1.仙器秘境, 2.仙兽秘境, 3.丹药秘境, 4.修为秘境, 5.灵石秘境, 6.北冥魔海, 7.不死魔巢, 8.蛇陵魔窟, 9.魔王入侵, 10.剧情挂机',
    `reward_type` tinyint(2) DEFAULT '0' COMMENT '1.按比例加成, 2.额外的活动掉落组, 3.剧情挂机奖励',
    `reward`      varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'reward_type对应的奖励值, e.g. 5,  [1, 2],  [{"time":300, "reward":1}]',
    `create_time` datetime                             DEFAULT NULL,
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-掉落';



#
转储表 game_campaign_type_fall_reward
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_fall_reward`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `reward_id`   int(8) DEFAULT '0' COMMENT '奖励组id',
    `reward`      varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表 e.g. [{"itemId":1001, "num":1}]',
    `weight`      int(8) DEFAULT '0' COMMENT '掉落权重',
    `message`     int(8) DEFAULT '0' COMMENT '传闻id',
    `create_time` datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-掉落奖励组';



#
转储表 game_campaign_type_firework
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_firework`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `campaign_id` bigint(11) NOT NULL COMMENT 'game_campaign.id',
    `type_id`     bigint(11) NOT NULL COMMENT 'game_campaign_type.id',
    `gift_id`     int(8) NOT NULL COMMENT '礼包itemId',
    `times`       int(8) NOT NULL DEFAULT '1' COMMENT '购买次数 =1-单次购买 >1-多次购买',
    `price`       varchar(512) DEFAULT NULL COMMENT '价格',
    `discount`    int(8) DEFAULT '0' COMMENT '折扣',
    `num`         int(8) NOT NULL DEFAULT '1' COMMENT '单次购买数量',
    `btn_name`    varchar(256) NOT NULL COMMENT '按钮标题',
    `min_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建者',
    `update_time` datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动-节日烟花';



#
转储表 game_campaign_type_login
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_login`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `login_day`   int(8) DEFAULT '0' COMMENT '登录天数',
    `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
    `reward`      varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表',
    `min_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-登录';



#
转储表 game_campaign_type_marry_rank
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_marry_rank`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`       bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`           bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `big_reward`        varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '大奖展示',
    `big_reward_fight`  bigint(11) DEFAULT '0' COMMENT '大奖战力',
    `rank_num`          int(8) DEFAULT '0' COMMENT '排行玩家数量(上榜人数限制)',
    `rank_reward_email` int(8) DEFAULT '0' COMMENT '排名奖励邮件id',
    `call_on_message`   int(8) DEFAULT '0' COMMENT '号召赠酒传闻id',
    `help_msg`          varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '帮助信息',
    `create_time`       timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`       timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-结义活动';



#
转储表 game_campaign_type_marry_rank_reward
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_marry_rank_reward`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `min_rank`    int(8) DEFAULT '0' COMMENT '排名最小值',
    `max_rank`    int(8) DEFAULT '0' COMMENT '排名最大值',
    `score`       bigint(11) DEFAULT '0' COMMENT '上榜最低积分',
    `reward`      varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表 e.g. [{"itemId":1001,"num":1}]',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-结义活动排名奖励';



#
转储表 game_campaign_type_party_progress
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_party_progress`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) NOT NULL COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) NOT NULL COMMENT 'game_campaign_type.id',
    `target`      int(8) NOT NULL COMMENT '任务规定数量',
    `percent`     int(8) NOT NULL COMMENT '进度百分比',
    `reward`      text     NOT NULL COMMENT '任务奖励',
    `create_time` datetime NOT NULL COMMENT 'createTime',
    `create_by`   varchar(128) DEFAULT NULL COMMENT 'createBy',
    `update_time` datetime     DEFAULT NULL COMMENT 'updateTime',
    `update_by`   varchar(128) DEFAULT NULL COMMENT 'updateBy',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `INDEX_PARTY_PROGRESS` (`campaign_id`,`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日派对进度任务';



#
转储表 game_campaign_type_party_task
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_party_task`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) NOT NULL COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) NOT NULL COMMENT 'game_campaign_type.id',
    `type`        int(8) NOT NULL COMMENT '任务类型',
    `module_id`   int(8) NOT NULL COMMENT '任务模块id',
    `args`        int(8) NOT NULL COMMENT '参数',
    `remark`      varchar(512) NOT NULL DEFAULT '' COMMENT '任务描述',
    `target`      int(8) NOT NULL COMMENT '任务规定数量',
    `cost_num`    int(8) NOT NULL COMMENT '直接消耗数量',
    `jump_id`     int(8) NOT NULL COMMENT '跳转id',
    `reward`      text         NOT NULL COMMENT '任务奖励',
    `min_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time` datetime     NOT NULL COMMENT 'createTime',
    `create_by`   varchar(128)          DEFAULT NULL COMMENT 'createBy',
    `update_time` datetime              DEFAULT NULL COMMENT 'updateTime',
    `update_by`   varchar(128)          DEFAULT NULL COMMENT 'updateBy',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `INDEX_PARTY_TASK` (`campaign_id`,`type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日派对小任务';



#
转储表 game_campaign_type_rebate_recharge
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_rebate_recharge`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `campaign_id`   bigint(11) NOT NULL COMMENT '主活动id',
    `type_id`       bigint(11) NOT NULL COMMENT '子活动id',
    `rid`           int(8) NOT NULL COMMENT '礼包id ',
    `total_day`     int(8) NOT NULL COMMENT '累计充值天数',
    `min`           decimal(10, 3) NOT NULL COMMENT '最低充值金额',
    `progress_desc` varchar(256)   NOT NULL DEFAULT '' COMMENT '进度描述',
    `reward`        text COMMENT '奖励列表',
    `min_level`     int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`     int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time`   timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`     varchar(32)             DEFAULT NULL COMMENT '创建者',
    `update_time`   timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`     varchar(32)             DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `INDEX_REBATE_RECHARGE` (`campaign_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-返利狂欢';



#
转储表 game_campaign_type_recharge
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_recharge`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`     bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`         bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `recharge_id`     int(8) DEFAULT '0' COMMENT '礼包id',
    `recharge_amount` decimal(10, 3)                       DEFAULT '0.000' COMMENT '累计充值额度',
    `reward`          varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表',
    `min_level`       int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`       int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time`     timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `create_by`       varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`     timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`       varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-充值';



#
转储表 game_campaign_type_reduce
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_reduce`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `campaign_id`    bigint(11) NOT NULL COMMENT 'game_campaign.id',
    `type_id`        bigint(11) NOT NULL COMMENT 'game_campaign_type.id',
    `sort`           int(8) NOT NULL COMMENT '排名序列',
    `reduce_item_id` int(8) NOT NULL COMMENT '消耗道具id,item_id',
    `limit_num`      bigint(11) NOT NULL COMMENT '上榜下限数量',
    `reward`         text      NOT NULL COMMENT '奖励内容[{"itemId":1001,"num":100}]',
    `min_level`      int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`      int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time`    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`      varchar(32)        DEFAULT NULL COMMENT '创建者',
    `update_time`    timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`      varchar(32)        DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `INDEX_GAME_CAMPAIGN_TYPE_REWARD` (`campaign_id`,`type_id`,`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-消耗排行';



#
转储表 game_campaign_type_select_discount_item
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_select_discount_item`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `campaign_id`  bigint(11) NOT NULL COMMENT '主活动id',
    `type_id`      bigint(11) NOT NULL COMMENT '子活动id',
    `show_order`   int(8) NOT NULL COMMENT '位置序号',
    `item_desc`    varchar(512) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '商品描述',
    `limit_num`    int(8) NOT NULL DEFAULT '0' COMMENT '限购的话次数多少',
    `goods_id`     int(8) NOT NULL COMMENT '商品id',
    `choose_items` text CHARACTER SET utf8 NOT NULL COMMENT '可选物品',
    `free_items`   text CHARACTER SET utf8 COMMENT '免费物品',
    `free`         tinyint(2) NOT NULL COMMENT '本组商品是否免费领取',
    `create_time`  timestamp                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`    varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '创建者',
    `update_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`    varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY            `INDEX_SELECT_DISCOUNT_ITEMS` (`campaign_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='节日活动-自选特惠-物品部分';



#
转储表 game_campaign_type_select_discount_message
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_select_discount_message`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `campaign_id`   bigint(11) NOT NULL COMMENT '主活动id',
    `type_id`       bigint(11) NOT NULL COMMENT '子活动id',
    `push_time`     varchar(256)       DEFAULT '' COMMENT '推送时间 时分秒',
    `content`       varchar(256)       DEFAULT '' COMMENT '传闻内容',
    `num`           int(8) DEFAULT NULL COMMENT '传闻广播次数',
    `email_title`   text COMMENT '全服广播邮件标题',
    `email_content` text COMMENT '全服广播邮件内容',
    `create_time`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`     varchar(32)        DEFAULT NULL COMMENT '创建者',
    `update_time`   timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`     varchar(32)        DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `INDEX_SELECT_DISCOUNT_MESSAGE` (`campaign_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-自选特惠-传闻部分';



#
转储表 game_campaign_type_sword
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_sword`
(
    `id`                   bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增第',
    `campaign_id`          bigint(11) NOT NULL COMMENT 'game_campaign.id',
    `type_id`              bigint(11) NOT NULL COMMENT 'game_campaign_type.id',
    `checkpoint_id`        int(8) NOT NULL COMMENT '关卡id',
    `monster_id`           int(8) DEFAULT NULL COMMENT '怪物id',
    `combat_power`         bigint(11) DEFAULT NULL COMMENT '推荐战力',
    `unlock_checkpoint_id` int(8) DEFAULT NULL COMMENT '解锁关卡',
    `reward`               text CHARACTER SET utf8 COMMENT '奖励',
    `checkpoint_name`      varchar(512) CHARACTER SET utf8 DEFAULT NULL COMMENT '关卡名',
    `create_time`          datetime                        DEFAULT NULL COMMENT '创建时间',
    `create_by`            varchar(32) CHARACTER SET utf8  DEFAULT NULL COMMENT '创建者',
    `update_time`          datetime                        DEFAULT NULL COMMENT '更新时间',
    `update_by`            varchar(32) CHARACTER SET utf8  DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `INDEX_GAME_CAMPAIGN_SWORD` (`campaign_id`,`type_id`,`checkpoint_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节日活动-限时剑仙';



#
转储表 game_campaign_type_task
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_task`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) DEFAULT '0' COMMENT 'campaign.id, 活动id',
    `type_id`     bigint(11) DEFAULT '0' COMMENT 'game_campaign_type.id',
    `task_id`     int(8) DEFAULT '0' COMMENT '任务id',
    `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
    `module_id`   int(8) DEFAULT '0' COMMENT 'task_module_type.json.module_id',
    `target`      int(8) DEFAULT '0' COMMENT '任务完成条件',
    `args`        int(8) DEFAULT '0' COMMENT '任务参数',
    `reward`      varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表',
    `jump_id`     int(8) DEFAULT NULL COMMENT '跳转id',
    `min_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`   int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time` datetime                             DEFAULT NULL,
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='活动-类型-任务';



#
转储表 game_campaign_type_throwing_eggs
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_throwing_eggs`
(
    `id`                    bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `campaign_id`           bigint(11) NOT NULL COMMENT '活动id',
    `type_id`               bigint(11) NOT NULL COMMENT '子活动id',
    `egg_type`              tinyint(2) NOT NULL COMMENT '砸蛋类型',
    `cost_item_id`          int(8) NOT NULL COMMENT '抽奖所需道具',
    `limit_lucky_value`     int(8) NOT NULL COMMENT '幸运值上限',
    `cost_num`              int(8) NOT NULL COMMENT '抽奖所需道具数量，同时也是每次抽奖掉落的砸蛋值和幸运值',
    `throwing_eggs_value`   int(8) NOT NULL COMMENT '砸蛋值',
    `lottery_integral_min`  int(8) NOT NULL COMMENT '抽奖积分最小值',
    `lottery_integral_max`  int(8) NOT NULL COMMENT '抽奖积分最大值',
    `lucky_probability`     int(8) NOT NULL COMMENT '幸运奖池概率',
    `probability_publicity` text         NOT NULL COMMENT '概率公示',
    `rule`                  text         NOT NULL COMMENT '玩法规则',
    `ordinary_pool`         varchar(512) NOT NULL                                  DEFAULT '' COMMENT '普通奖池',
    `lucky_pool`            varchar(512) NOT NULL                                  DEFAULT '' COMMENT '幸运奖池',
    `ordinary_pool_item`    text         NOT NULL COMMENT '普通奖池掉落',
    `lucky_pool_item`       text         NOT NULL COMMENT '幸运奖池掉落',
    `reward_anim`           varchar(512) NOT NULL COMMENT '大奖动画',
    `show_ordinary_reward`  text         NOT NULL COMMENT '普通奖励',
    `show_lucky_reward`     text         NOT NULL COMMENT '幸运奖励',
    `min_level`             int(8) NOT NULL DEFAULT '0' COMMENT '最小世界等级',
    `max_level`             int(8) NOT NULL DEFAULT '0' COMMENT '最大世界等级',
    `create_time`           datetime                                               DEFAULT NULL COMMENT '创建时间',
    `create_by`             varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
    `update_time`           datetime                                               DEFAULT NULL COMMENT '修改时间',
    `update_by`             varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `INDEX_THROING_EGGS` (`campaign_id`,`type_id`,`egg_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-砸蛋';



#
转储表 game_campaign_type_throwing_eggs_gift
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_throwing_eggs_gift`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `campaign_id`     bigint(11) NOT NULL COMMENT '活动id',
    `type_id`         bigint(11) NOT NULL COMMENT '子活动id',
    `cost_item_id`    int(8) NOT NULL COMMENT '消耗道具',
    `cost_num`        int(8) NOT NULL COMMENT '消耗道具数量',
    `gift_name`       varchar(512)          DEFAULT NULL COMMENT '礼包名',
    `stack`           int(8) NOT NULL COMMENT '库存',
    `amount`          int(8) DEFAULT NULL COMMENT '原价',
    `discount`        int(8) DEFAULT NULL COMMENT '折扣',
    `limit_condition` varchar(512)          DEFAULT NULL COMMENT '限购条件',
    `reward`          varchar(512) NOT NULL DEFAULT '' COMMENT '显示奖励内容',
    `create_time`     datetime     NOT NULL COMMENT '创建时间',
    `create_by`       varchar(32)           DEFAULT NULL COMMENT '创建者',
    `update_time`     datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`       varchar(32)           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    KEY               `INDEX_EGGS_GIFT` (`campaign_id`,`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-砸蛋-礼包';



#
转储表 game_campaign_type_throwing_eggs_rank
# ------------------------------------------------------------

CREATE TABLE `game_campaign_type_throwing_eggs_rank`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `campaign_id` bigint(11) NOT NULL COMMENT '活动id',
    `type_id`     bigint(11) NOT NULL COMMENT '子活动id',
    `sort`        int(8) NOT NULL COMMENT '排名序列',
    `limit_num`   int(8) NOT NULL COMMENT '上榜下限数量',
    `reward`      text NOT NULL COMMENT '奖励内容[{"itemId":1001,"num":100}]',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(32) DEFAULT NULL COMMENT '创建者',
    `update_time` datetime    DEFAULT NULL COMMENT '更新时间',
    `update_by`   varchar(32) DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `INDEX_THROWING_EGGS_RANK` (`campaign_id`,`type_id`,`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节日活动-砸蛋-砸蛋榜';



#
转储表 game_center_customer_answer
# ------------------------------------------------------------

CREATE TABLE `game_center_customer_answer`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT,
    `question_id`   bigint(11) DEFAULT NULL COMMENT '中央服中问题自增主键id',
    `customer_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `content`       text COLLATE utf8mb4_unicode_ci,
    `create_time`   timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='中央客服回复';



#
转储表 game_center_customer_service
# ------------------------------------------------------------

CREATE TABLE `game_center_customer_service`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '问题id',
    `player_id`    bigint(11) DEFAULT NULL COMMENT '玩家id',
    `account`      varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '玩家帐号',
    `question_id`  bigint(11) NOT NULL COMMENT '每个游戏服中问题id',
    `channel_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道标志',
    `server_id`    int(8) NOT NULL COMMENT '玩家注册服务器',
    `type`         int(8) DEFAULT '0' COMMENT '问题类型0bug  1投诉 2建议 3其他',
    `vip`          int(8) DEFAULT '0',
    `content`      text COLLATE utf8mb4_unicode_ci,
    `reply_state`  int(8) DEFAULT '0' COMMENT '0未回复 1已回复',
    `player_name`  varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `network_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `phone_type`   varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `read_time`    timestamp NULL DEFAULT NULL,
    `create_time`  timestamp NULL DEFAULT NULL,
    `update_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `question_id` (`question_id`,`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='中央游戏服客服';



#
转储表 game_channel
# ------------------------------------------------------------

CREATE TABLE `game_channel`
(
    `id`                  int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `game_id`             int(8) DEFAULT NULL COMMENT '游戏编号',
    `name`                varchar(50) NOT NULL COMMENT '渠道名称',
    `simple_name`         varchar(50)  DEFAULT NULL COMMENT '唯一标识',
    `notice_id`           int(8) DEFAULT NULL COMMENT '公告id',
    `version_code`        int(8) DEFAULT NULL COMMENT '版本号',
    `version_name`        varchar(32)  DEFAULT NULL COMMENT '版本名',
    `version_update_time` datetime     DEFAULT NULL COMMENT '版本更新时间',
    `test_login`          tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否允许测试登录 0-关 1-开',
    `ta_statistics`       tinyint(2) NOT NULL DEFAULT '1' COMMENT 'ta统计开关',
    `ip_whitelist`        text COMMENT 'ip白名单',
    `gm_ip`               text COMMENT 'GM ip白名单',
    `auth_status`         tinyint(2) NOT NULL DEFAULT '0' COMMENT '实名认证开关 0-关 1-开',
    `auth_ignore_ip`      text COMMENT '实名认证忽略的ip',
    `auth_ignore_devices` text COMMENT '实名认证忽略的设备id',
    `extra`               varchar(512) DEFAULT NULL COMMENT '扩展字段',
    `group_name`          varchar(16)  DEFAULT NULL COMMENT '分组',
    `remark`              varchar(200) DEFAULT NULL COMMENT '大渠道描述',
    `create_by`           varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`         datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `SIMIPLE_NAME` (`simple_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏渠道';



#
转储表 game_channel_server
# ------------------------------------------------------------

CREATE TABLE `game_channel_server`
(
    `id`            int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `server_id`     int(8) NOT NULL COMMENT '服务器id',
    `channel_id`    varchar(50) NOT NULL COMMENT '渠道id',
    `del_flag`      tinyint(2) DEFAULT NULL COMMENT '删除状态',
    `no_need_count` tinyint(2) NOT NULL DEFAULT '0' COMMENT '参与数据统计0-参与 1-不参与',
    `position`      int(8) NOT NULL DEFAULT '1' COMMENT '排序字段',
    `create_by`     varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(32) DEFAULT NULL COMMENT '修改人',
    `update_time`   datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏渠道服配置';



#
转储表 game_chinese_calendar
# ------------------------------------------------------------

CREATE TABLE `game_chinese_calendar`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `year`        varchar(50) NOT NULL DEFAULT '' COMMENT '年份',
    `holidays`    varchar(1024)        DEFAULT NULL COMMENT '法定节假日',
    `workdays`    varchar(1024)        DEFAULT NULL COMMENT '额外的工作日',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='法定节假日配置';



#
转储表 game_email
# ------------------------------------------------------------

CREATE TABLE `game_email`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主id',
    `title`         text     NOT NULL COMMENT '标题',
    `describe`      text     NOT NULL COMMENT '描述',
    `type`          tinyint(2) NOT NULL COMMENT '类型',
    `content`       text COMMENT '附件',
    `state`         tinyint(2) NOT NULL COMMENT '状态',
    `receiver_type` tinyint(2) NOT NULL COMMENT '目标类型',
    `receiver_ids`  text CHARACTER SET utf8 NOT NULL COMMENT '目标主体 玩家id/服务器id',
    `send_time`     datetime NOT NULL COMMENT '生效时间',
    `start_time`    datetime                       DEFAULT NULL COMMENT '开始时间',
    `end_time`      datetime                       DEFAULT NULL COMMENT '结束时间',
    `create_by`     varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建者',
    `create_time`   datetime                       DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新着',
    `update_time`   datetime                       DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY             `idx_title` (`title`(255)) USING BTREE,
    KEY             `idx_content` (`content`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏邮件';



#
转储表 game_forbidden
# ------------------------------------------------------------

CREATE TABLE `game_forbidden`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `server_id`   int(8) NOT NULL COMMENT '服务器id',
    `type`        tinyint(2) NOT NULL COMMENT '1-登录 2-聊天',
    `ban_key`     varchar(20)                     NOT NULL COMMENT 'playerId/ip/deviceId',
    `ban_value`   varchar(32)                     NOT NULL COMMENT '对应 ban_type 的值',
    `reason`      varchar(500) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '封禁原因',
    `is_forever`  tinyint(2) NOT NULL COMMENT '0-临时 1-永久',
    `start_time`  datetime                                 DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime                                 DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime                                 DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '操作人',
    `update_time` datetime                                 DEFAULT NULL COMMENT '更新时间',
    `update_by`   varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '操作人',
    PRIMARY KEY (`id`),
    KEY           `IDX_SERVER_ID` (`server_id`) USING BTREE,
    KEY           `IDX_TYPE_SERVERID_KEY` (`type`,`server_id`,`ban_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户封禁信息详情表';



#
转储表 game_forbidden_record
# ------------------------------------------------------------

CREATE TABLE `game_forbidden_record`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `operation`    varchar(20)                              DEFAULT NULL COMMENT '执行的增删改操作类型',
    `forbidden_id` bigint(11) NOT NULL COMMENT '封号禁言表id',
    `server_id`    int(8) NOT NULL COMMENT '服务器id',
    `type`         tinyint(2) DEFAULT NULL COMMENT '1-登录 2-聊天',
    `ban_key`      varchar(20)                     NOT NULL COMMENT 'playerId/ip/deviceId',
    `ban_value`    varchar(32)                     NOT NULL COMMENT '对应 ban_type 的值',
    `reason`       varchar(500) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '封禁原因',
    `is_forever`   tinyint(2) NOT NULL COMMENT '0-临时 1-永久',
    `start_time`   datetime                                 DEFAULT NULL COMMENT '开始时间',
    `end_time`     datetime                                 DEFAULT NULL COMMENT '结束时间',
    `create_time`  datetime                                 DEFAULT NULL COMMENT '创建时间',
    `create_by`    varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '操作人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY            `IDX_SERVER_ID` (`server_id`) USING BTREE,
    KEY            `IDX_TYPE_SERVERID_KEY` (`type`,`server_id`,`ban_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户封禁信息详情表';



#
转储表 game_image
# ------------------------------------------------------------

CREATE TABLE `game_image`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
    `type`        int(8) NOT NULL COMMENT '图片类型: 1.icon, 2.banner',
    `name`        varchar(50)  NOT NULL DEFAULT '' COMMENT '图片名',
    `img_url`     varchar(100) NOT NULL DEFAULT '' COMMENT '相对路径',
    `width`       int(8) NOT NULL COMMENT '图片宽px',
    `height`      int(8) NOT NULL COMMENT '图片高px',
    `remark`      varchar(100)          DEFAULT NULL COMMENT '备注',
    `create_time` datetime              DEFAULT NULL COMMENT 'createTime',
    `create_by`   varchar(32)           DEFAULT NULL COMMENT '创建人',
    `update_time` datetime              DEFAULT NULL COMMENT 'updateTime',
    `update_by`   varchar(32)           DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片管理';



#
转储表 game_info
# ------------------------------------------------------------

CREATE TABLE `game_info`
(
    `id`                   int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`                 varchar(50) NOT NULL COMMENT '游戏名称',
    `ya_app_id`            varchar(64)  DEFAULT NULL COMMENT 'YA_APPID',
    `ya_app_key`           varchar(64)  DEFAULT NULL COMMENT 'YA_APPKEY',
    `ya_simple_name`       varchar(64)  DEFAULT NULL COMMENT 'gameSimpleName',
    `weixin_review`        int(8) NOT NULL DEFAULT '0' COMMENT '微信正在审核版本',
    `review_channel`       varchar(64)  DEFAULT NULL COMMENT '审核渠道',
    `ya_game_key`          varchar(64)  DEFAULT NULL COMMENT 'gameAppKey',
    `server_url`           varchar(100) DEFAULT NULL COMMENT '不带host, 公告json地址，客户端自己拼接渠道名+.json，',
    `notice_url`           varchar(100) DEFAULT NULL COMMENT '不带host, 服务器列表json地址，客户端自己拼接渠道名+.json，',
    `login_url`            varchar(100) DEFAULT NULL COMMENT '帐号登录地址',
    `role_url`             varchar(100) DEFAULT NULL COMMENT '角色信息地址',
    `auth_url`             varchar(100) DEFAULT NULL COMMENT '实名认证地址',
    `pay_url`              varchar(100) DEFAULT NULL COMMENT '支付验证地址',
    `oauth_redirect_url`   varchar(100) DEFAULT NULL COMMENT '苹果登录回调',
    `account_register_url` varchar(100) DEFAULT NULL COMMENT '账号注册地址',
    `account_login_url`    varchar(100) DEFAULT NULL COMMENT '账号登录地址',
    `off_register_day`     int(8) NOT NULL DEFAULT '0' COMMENT '关闭注册天数',
    `remark`               varchar(200) DEFAULT NULL COMMENT '描述',
    `create_by`            varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`          datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`            varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`          datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏信息';



#
转储表 game_lamp_notice
# ------------------------------------------------------------

CREATE TABLE `game_lamp_notice`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
    `notice_title`     varchar(128) NOT NULL COMMENT '标题',
    `notice_text`      text         NOT NULL COMMENT '正文',
    `game_server_list` varchar(256) NOT NULL COMMENT '投放服务器',
    `frequency`        int(8) NOT NULL COMMENT '播放频率',
    `cycle_period`     int(8) NOT NULL COMMENT '循环播放周期',
    `begin_time`       datetime     NOT NULL COMMENT '开始时间',
    `end_time`         datetime     NOT NULL COMMENT '结束时间',
    `last_send_time`   datetime    DEFAULT NULL COMMENT 'lastSendTime',
    `status`           tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态 0-关闭 1-开启',
    `create_time`      datetime    DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) DEFAULT NULL COMMENT '创建者',
    `update_time`      datetime    DEFAULT NULL COMMENT '更新时间',
    `update_by`        varchar(32) DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    KEY                `INDEX_LAMP_NOTICE` (`id`,`notice_title`,`game_server_list`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 game_notice
# ------------------------------------------------------------

CREATE TABLE `game_notice`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `notice_type`      int(8) NOT NULL COMMENT '公告类型 1 - 渠道公告 2 - 滚动公告',
    `title`            varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
    `content`          text COMMENT '公告内容',
    `begin_time`       datetime NOT NULL COMMENT '开始时间',
    `end_time`         datetime NOT NULL COMMENT '结束时间',
    `status`           tinyint(2) NOT NULL COMMENT '状态 1 - 可用 0 - 不可用',
    `interval_seconds` int(8) DEFAULT NULL COMMENT '滚动公告间隔(秒)',
    `create_by`        varchar(32)                                             DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime                                                DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(32)                                             DEFAULT NULL COMMENT '修改人',
    `update_time`      datetime                                                DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏公告';



#
转储表 game_online_num
# ------------------------------------------------------------

CREATE TABLE `game_online_num`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `channel`     varchar(20) DEFAULT NULL COMMENT '渠道id',
    `server_id`   int(8) NOT NULL COMMENT '服务器名字',
    `online_num`  bigint(11) DEFAULT NULL COMMENT '在线人数',
    `get_time`    date        DEFAULT NULL COMMENT '获取时间的日期',
    `update_time` datetime    DEFAULT NULL COMMENT '更新时间',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY           `server_id` (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏在线人数统计';



#
转储表 game_open_service_campaign
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
    `name`            varchar(50) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '活动名称',
    `cross`           tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否跨服',
    `server_ids`      text COLLATE utf8_unicode_ci COMMENT '服务器id，使用,分割',
    `last_server_ids` text COLLATE utf8_unicode_ci COMMENT '已刷新的服务器id，使用,分割',
    `icon`            varchar(128) COLLATE utf8_unicode_ci          DEFAULT '' COMMENT '活动图标',
    `status`          tinyint(2) NOT NULL DEFAULT '1' COMMENT '活动状态: 0.关闭, 1.开启',
    `priority`        tinyint(2) NOT NULL DEFAULT '0' COMMENT '优先级',
    `auto_open`       tinyint(2) NOT NULL DEFAULT '0' COMMENT '到达活动时间后自动开启',
    `remark`          varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动备注',
    `create_time`     datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`       varchar(32) COLLATE utf8_unicode_ci           DEFAULT '' COMMENT '创建人',
    `update_time`     datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`       varchar(32) COLLATE utf8_unicode_ci           DEFAULT '' COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动(1级)';



#
转储表 game_open_service_campaign_consume_detail
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_consume_detail`
(
    `id`                           bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`                  bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`             bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `time_type`                    tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`                   datetime                                      DEFAULT NULL COMMENT '开始时间',
    `end_time`                     datetime                                      DEFAULT NULL COMMENT '结束时间',
    `start_day`                    int(8) DEFAULT '0' COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`                     int(8) DEFAULT '0' COMMENT '持续时间(天)',
    `tab_name`                     varchar(50) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '活动页签名称',
    `name`                         varchar(50) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '活动名称',
    `banner`                       varchar(200) COLLATE utf8_unicode_ci          DEFAULT NULL COMMENT '活动宣传背景图',
    `consume_reward_email_title`   varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '消耗奖励邮件标题',
    `consume_reward_email_content` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '消耗奖励邮件内容',
    `help_msg`                     text COLLATE utf8_unicode_ci COMMENT '帮助信息',
    `create_time`                  datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`                    varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '创建者',
    `update_time`                  datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`                    varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                            `IDX_CID_TYPEID` (`campaign_id`,`campaign_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-消耗活动-活动明细(3级)';



#
转储表 game_open_service_campaign_consume_detail_item
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_consume_detail_item`
(
    `id`                   bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`          bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`     bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `consume_detail_id`    bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_consume_detail.id',
    `sort`                 int(8) NOT NULL DEFAULT '0' COMMENT '排序',
    `consume_type`         int(8) NOT NULL DEFAULT '0' COMMENT '统计类型 0.个人, 1.全服',
    `start_day`            int(8) NOT NULL DEFAULT '0' COMMENT '开始时间(子活动开始第n天, e.g. 0表示子活动开始第1天)',
    `statistics_not_start` tinyint(2) NOT NULL DEFAULT '0' COMMENT '开启前是否统计，全服统计默认是',
    `description`          varchar(100) COLLATE utf8_unicode_ci          DEFAULT NULL COMMENT '描述',
    `jump`                 varchar(100) COLLATE utf8_unicode_ci          DEFAULT NULL COMMENT '跳转',
    `num`                  int(8) NOT NULL DEFAULT '0' COMMENT '总数量',
    `consume_items`        varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '消耗道具 e.g. [1001, 1002, 1003]',
    `reward`               varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '奖励列表 e.g. [{"itemId":1001, "num":1}]',
    `create_time`          datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`            varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '创建者',
    `update_time`          datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`            varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                    `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`consume_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-消耗活动-消耗明细';



#
转储表 game_open_service_campaign_consume_detail_message
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_consume_detail_message`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`       bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`  bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `consume_detail_id` bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_consume_detail.id',
    `send_time`         timestamp                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '传闻推送时间',
    `message`           varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '传闻内容',
    `num`               int(8) NOT NULL DEFAULT '0' COMMENT '传闻次数',
    `email`             tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否发送邮件',
    `create_time`       datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`         varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '创建者',
    `update_time`       datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`         varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`consume_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-消耗活动-活动明细-传闻';



#
转储表 game_open_service_campaign_gift_detail
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_gift_detail`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `name`             varchar(50) COLLATE utf8_unicode_ci  NOT NULL COMMENT '活动名称',
    `tab_name`         varchar(50) COLLATE utf8_unicode_ci  NOT NULL COMMENT '活动页签名称',
    `sort`             int(8) NOT NULL DEFAULT '1' COMMENT '排序',
    `banner`           varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动宣传背景图',
    `skeleton`         varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '骨骼动画资源图',
    `res_type`         tinyint(2) DEFAULT '1' COMMENT '资源格式: 1.骨骼, 2.序列帧, 3.图片',
    `time_type`        tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`       datetime                             DEFAULT NULL COMMENT '开始时间',
    `end_time`         datetime                             DEFAULT NULL COMMENT '结束时间',
    `start_day`        int(8) DEFAULT '1' COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`         int(8) DEFAULT NULL COMMENT '持续时间(天)',
    `help_msg`         text COLLATE utf8_unicode_ci COMMENT '帮助信息',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT '' COMMENT '修改人',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT '' COMMENT '创建人',
    `create_time`      datetime                             DEFAULT NULL COMMENT '创建时间',
    `update_time`      datetime                             DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_campaign_id` (`campaign_id`) USING BTREE,
    KEY                `IDX_CID_TYPEID` (`campaign_id`,`campaign_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-活动明细(3级)';



#
转储表 game_open_service_campaign_gift_detail_item
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_gift_detail_item`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `gift_detail_id`   int(8) NOT NULL COMMENT 'open_service_campaign_gift_detail.id',
    `sort`             int(8) NOT NULL COMMENT '排序',
    `gift_type`        int(8) NOT NULL COMMENT '礼包类型 0.普通礼包, 1.大奖礼包',
    `discount`         decimal(10, 2)                       NOT NULL COMMENT '折扣',
    `price`            varchar(500) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '价格 e.g. [{"itemId":1001,"num":"1"}]',
    `buy_num`          int(8) NOT NULL DEFAULT '0' COMMENT '购买数量',
    `reward`           varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '奖励列表 e.g. [{"itemId":1001,"num":"1"}]',
    `create_time`      datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime                             DEFAULT NULL COMMENT '更新时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`gift_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服开服礼包-礼包明细';



#
转储表 game_open_service_campaign_lottery_detail
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_lottery_detail`
(
    `id`                         bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`                bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`           bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `time_type`                  tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`                 datetime                                       DEFAULT NULL COMMENT '开始时间',
    `end_time`                   datetime                                       DEFAULT NULL COMMENT '结束时间',
    `start_day`                  int(8) DEFAULT '0' COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`                   int(8) DEFAULT '0' COMMENT '持续时间(天)',
    `tab_name`                   varchar(50) COLLATE utf8_unicode_ci   NOT NULL DEFAULT '' COMMENT '活动页签名称',
    `name`                       varchar(50) COLLATE utf8_unicode_ci   NOT NULL DEFAULT '' COMMENT '活动名称',
    `banner`                     varchar(200) COLLATE utf8_unicode_ci           DEFAULT '' COMMENT '活动宣传背景图',
    `skeleton`                   varchar(200) COLLATE utf8_unicode_ci           DEFAULT '' COMMENT '骨骼动画资源图',
    `free_num`                   int(8) NOT NULL DEFAULT '0' COMMENT '每日免费抽奖次数',
    `reward_record_num`          int(8) NOT NULL DEFAULT '0' COMMENT '获奖记录显示数量',
    `reward_record_msg`          varchar(200) COLLATE utf8_unicode_ci  NOT NULL COMMENT '获奖记录内容',
    `reward_msg`                 varchar(200) COLLATE utf8_unicode_ci  NOT NULL COMMENT '获奖传闻内容',
    `probability_msg`            text COLLATE utf8_unicode_ci          NOT NULL COMMENT '概率公示',
    `lottery_type`               varchar(1000) COLLATE utf8_unicode_ci NOT NULL COMMENT '抽奖设置(单抽/多抽) e.g. [{"itemId":1001, "num":1, "lotteryNum":1, "score":10}]',
    `ssr_show_reward`            varchar(200) COLLATE utf8_unicode_ci  NOT NULL COMMENT '展示特奖 e.g. [{"itemId":1001, "num":1}]',
    `sr_show_reward`             varchar(200) COLLATE utf8_unicode_ci  NOT NULL COMMENT '展示大奖 e.g. [{"itemId":1001, "num":1}]',
    `show_reward`                varchar(1000) COLLATE utf8_unicode_ci NOT NULL COMMENT '展示奖励 e.g. [{"itemId":1001, "num":1}]',
    `reset_reward`               varchar(1000) COLLATE utf8_unicode_ci NOT NULL COMMENT '重置大奖 e.g. [1002, 1010, 1021, 1031]',
    `reward_pool`                varchar(1000) COLLATE utf8_unicode_ci NOT NULL COMMENT '奖池 [{"timeMin":1, "timeMax":20, "rewardPool":1}, {"timeMin":21, "timeMax":30, "rewardPool":2}]',
    `rank_reward_email_title`    varchar(200) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '排名奖励邮件标题',
    `rank_reward_email_content`  varchar(200) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '排名奖励邮件内容',
    `score_reward_email_title`   varchar(200) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '积分奖励邮件标题',
    `score_reward_email_content` varchar(200) COLLATE utf8_unicode_ci  NOT NULL DEFAULT '' COMMENT '积分奖励邮件内容',
    `help_msg`                   text COLLATE utf8_unicode_ci COMMENT '帮助信息',
    `create_time`                datetime                                       DEFAULT NULL COMMENT '创建时间',
    `create_by`                  varchar(32) COLLATE utf8_unicode_ci            DEFAULT '' COMMENT '创建人',
    `update_time`                datetime                                       DEFAULT NULL COMMENT '更新时间',
    `update_by`                  varchar(32) COLLATE utf8_unicode_ci            DEFAULT '' COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                          `IDX_CID_TYPEID` (`campaign_id`,`campaign_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-排行夺宝-活动明细(3级)';



#
转储表 game_open_service_campaign_lottery_detail_pool
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_lottery_detail_pool`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`       bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`  bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `lottery_detail_id` bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_lottery_detail.id',
    `pool_id`           int(8) NOT NULL DEFAULT '0' COMMENT '奖池id',
    `reward`            varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表 e.g. [{"itemId":1001, "num":1}]',
    `weight`            int(8) NOT NULL DEFAULT '0' COMMENT '掉落权重',
    `record`            tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否记录',
    `message`           tinyint(2) DEFAULT '0' COMMENT '是否传闻',
    `show_reward`       tinyint(2) DEFAULT '0' COMMENT '是否大奖弹窗',
    `create_time`       datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT '' COMMENT '创建人',
    `update_time`       datetime                             DEFAULT NULL COMMENT '更新时间',
    `update_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT '' COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`lottery_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-排行夺宝-奖池';



#
转储表 game_open_service_campaign_lottery_detail_ranking
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_lottery_detail_ranking`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`       bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`  bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `lottery_detail_id` bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_lottery_detail.id',
    `min_rank`          int(8) NOT NULL DEFAULT '0' COMMENT '排名最小值',
    `max_rank`          int(8) NOT NULL DEFAULT '0' COMMENT '排名最大值',
    `score`             bigint(11) NOT NULL DEFAULT '0' COMMENT '上榜最低积分',
    `reward`            varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表 e.g. [{"itemId":1001, "num":1}]',
    `create_time`       datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT '' COMMENT '创建人',
    `update_time`       datetime                             DEFAULT NULL COMMENT '更新时间',
    `update_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT '' COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`lottery_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-排行夺宝-排行榜';



#
转储表 game_open_service_campaign_lottery_detail_score
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_lottery_detail_score`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`       bigint(11) NOT NULL DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`  bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_type.id',
    `lottery_detail_id` bigint(11) NOT NULL DEFAULT '0' COMMENT 'open_service_campaign_lottery_detail.id',
    `score`             bigint(11) NOT NULL DEFAULT '0' COMMENT '积分',
    `reward`            varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '奖励列表 e.g. [{"itemId":1001, "num":1}]',
    `create_time`       datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`       datetime                             DEFAULT NULL COMMENT '修改时间',
    `update_by`         varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`lottery_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-排行夺宝-积分奖励';



#
转储表 game_open_service_campaign_rank_detail
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_rank_detail`
(
    `id`                    bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`           bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id`      bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `name`                  varchar(50) COLLATE utf8_unicode_ci  NOT NULL COMMENT '活动名称',
    `tab_name`              varchar(50) COLLATE utf8_unicode_ci  NOT NULL COMMENT '活动页签名称',
    `sort`                  int(8) NOT NULL DEFAULT '1' COMMENT '排序',
    `rank_type`             int(8) NOT NULL COMMENT '排行类型 open_service_campaign_rank_type.rank_type e.g. 1.境界冲榜, 2.功法冲榜',
    `time_type`             tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`            datetime                             DEFAULT NULL COMMENT '开始时间',
    `end_time`              datetime                             DEFAULT NULL COMMENT '结束时间',
    `start_day`             int(8) DEFAULT NULL COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`              int(8) DEFAULT NULL COMMENT '持续时间(天)',
    `help_msg`              varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '帮助信息',
    `banner`                varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动宣传背景图',
    `reward_img`            varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动宣传奖励图',
    `combat_power`          bigint(11) NOT NULL COMMENT '活动宣传仙力',
    `rank_num`              int(8) NOT NULL DEFAULT '0' COMMENT '排行玩家数量',
    `rank_reward_email`     bigint(11) NOT NULL COMMENT '排名奖励邮件id',
    `standard_reward_email` bigint(11) NOT NULL COMMENT '达标奖励邮件id',
    `jump`                  varchar(60) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '跳转id',
    `create_time`           datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`             varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`           datetime                             DEFAULT NULL COMMENT '更新时间',
    `update_by`             varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                     `IDX_CID_TYPEID` (`campaign_id`,`campaign_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-活动明细(3级)';



#
转储表 game_open_service_campaign_rank_detail_message
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_rank_detail_message`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `rank_detail_id`   bigint(11) NOT NULL COMMENT 'open_service_campaign_rank_detail.id',
    `send_time`        datetime                             NOT NULL COMMENT '传闻推送时间',
    `message`          varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '传闻内容',
    `num`              int(8) NOT NULL COMMENT '传闻次数',
    `email`            tinyint(2) NOT NULL COMMENT '是否发送邮件',
    `create_time`      datetime                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '修改人',
    `update_time`      datetime                                      DEFAULT NULL COMMENT '更新时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`rank_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-活动明细-传闻';



#
转储表 game_open_service_campaign_rank_detail_ranking
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_rank_detail_ranking`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `rank_detail_id`   bigint(11) NOT NULL COMMENT 'open_service_campaign_rank_detail.id',
    `min_rank`         int(8) NOT NULL COMMENT '排名最小值',
    `max_rank`         int(8) NOT NULL COMMENT '排名最大值',
    `score`            int(8) NOT NULL COMMENT '上榜最低积分',
    `reward`           varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '奖励列表 e.g. [{"itemId":1001,"num":"1"}]',
    `rare_reward`      varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '稀有奖励列表 e.g. [{"itemId":1001,"num":"1"}]',
    `message`          varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '广告引导内容',
    `ad_show_time`     int(8) DEFAULT NULL COMMENT '广告引导显示时长(秒)，0表示一直显示直到玩家点击',
    `create_time`      datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`rank_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-活动明细-排行上榜、奖励';



#
转储表 game_open_service_campaign_rank_detail_score
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_rank_detail_score`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `rank_detail_id`   bigint(11) NOT NULL COMMENT 'open_service_campaign_rank_detail.id',
    `item_id`          int(8) NOT NULL COMMENT '排行消耗道具id',
    `num`              int(8) NOT NULL COMMENT '排行消耗道具数量',
    `score`            int(8) NOT NULL COMMENT '消耗对应积分',
    `item_type`        int(8) NOT NULL COMMENT '道具积分分类',
    `item_type_name`   varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '道具积分分类名称',
    `create_time`      datetime                            DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime                            DEFAULT NULL COMMENT '修改时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`rank_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-活动明细-消耗道具分数';



#
转储表 game_open_service_campaign_rank_detail_standard
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_rank_detail_standard`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `rank_detail_id`   bigint(11) NOT NULL COMMENT 'open_service_campaign_rank_detail.id',
    `score`            int(8) NOT NULL COMMENT '达标奖励积分',
    `description`      varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '描述',
    `reward`           varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '奖励列表 e.g. [{"itemId":1001,"num":"1"}]',
    `message`          varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '广告引导内容',
    `ad_show_time`     int(8) NOT NULL DEFAULT '0' COMMENT '广告引导显示时长(秒)，0表示一直显示直到玩家点击',
    `create_time`      datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '创建人',
    `update_time`      datetime                                      DEFAULT NULL COMMENT '修改时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci           DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`rank_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-活动明细-达标奖励';



#
转储表 game_open_service_campaign_rank_type
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_rank_type`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rank_type`      int(8) NOT NULL COMMENT '排行类型 e.g. 1.境界冲榜, 2.功法冲榜',
    `rank_type_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '排行类型名称',
    `create_time`    datetime                            DEFAULT NULL COMMENT '创建时间',
    `create_by`      varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
    `update_time`    datetime                            DEFAULT NULL COMMENT '修改时间',
    `update_by`      varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-开服排行-类型库';



#
转储表 game_open_service_campaign_single_gift_detail
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_single_gift_detail`
(
    `id`               bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `time_type`        tinyint(2) NOT NULL DEFAULT '0' COMMENT '时间类型: 1.具体时间范围, 2.开服第N天',
    `start_time`       datetime                                      DEFAULT NULL COMMENT '开始时间',
    `end_time`         datetime                                      DEFAULT NULL COMMENT '结束时间',
    `start_day`        int(8) DEFAULT NULL COMMENT '开始时间(开服第n天, e.g. 0表示开服第1天)',
    `duration`         int(8) DEFAULT NULL COMMENT '持续时间(天)',
    `help_msg`         varchar(500) COLLATE utf8_unicode_ci          DEFAULT NULL COMMENT '帮助信息',
    `tab_name`         varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动页签名称',
    `sort`             int(8) NOT NULL COMMENT '排序',
    `name`             varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动名称',
    `banner`           varchar(256) COLLATE utf8_unicode_ci NOT NULL COMMENT '活动背景图',
    `email_title`      varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '邮件标题',
    `email_content`    varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '邮件描述',
    `create_time`      datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(512) COLLATE utf8_unicode_ci          DEFAULT NULL COMMENT '创建者',
    `update_time`      datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`        varchar(512) COLLATE utf8_unicode_ci          DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_CID_TYPEID` (`campaign_id`,`campaign_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-单笔好礼活动参数';



#
转储表 game_open_service_campaign_single_gift_item
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_single_gift_item`
(
    `id`               bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `gift_detail_id`   bigint(11) NOT NULL COMMENT 'open_service_single_recharge_gift_detail.id',
    `amount`           decimal(10, 3)                       NOT NULL COMMENT '任务金额',
    `limit_times`      int(8) DEFAULT '1' COMMENT '领取上限次数',
    `remark`           varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '任务描述',
    `reward`           varchar(512) COLLATE utf8_unicode_ci NOT NULL COMMENT '奖励json',
    `create_time`      datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建者',
    `update_time`      datetime                             DEFAULT NULL COMMENT '更新时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`gift_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-单比好礼-任务明细';



#
转储表 game_open_service_campaign_single_gift_notice
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_single_gift_notice`
(
    `id`               bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id`      bigint(11) DEFAULT '0' COMMENT '开服活动id, open_service_campaign.id',
    `campaign_type_id` bigint(11) NOT NULL COMMENT 'open_service_campaign_type.id',
    `gift_detail_id`   bigint(11) NOT NULL COMMENT 'open_service_single_recharge_gift_detail.id',
    `num`              int(8) NOT NULL COMMENT '播放次数',
    `message`          text COLLATE utf8_unicode_ci COMMENT '消息内容',
    `email`            tinyint(2) NOT NULL COMMENT '发送方式 0-非邮件 1-邮件',
    `send_time`        datetime                            DEFAULT NULL COMMENT '发送时间',
    `create_time`      datetime                            DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
    `update_time`      datetime                            DEFAULT NULL COMMENT '更新时间',
    `update_by`        varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改者',
    PRIMARY KEY (`id`),
    KEY                `IDX_CID_TYPEID_DETAILID` (`campaign_id`,`campaign_type_id`,`gift_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-单比好礼-传闻消息';



#
转储表 game_open_service_campaign_type
# ------------------------------------------------------------

CREATE TABLE `game_open_service_campaign_type`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `campaign_id` bigint(11) NOT NULL COMMENT '开服活动id, open_service_campaign.id',
    `type`        int(8) NOT NULL COMMENT '开服活动项类型(1.开服排行，2.开服礼包，3.单笔充值，4.寻宝，5.道具消耗)',
    `sort`        int(8) NOT NULL COMMENT '排序',
    `remark`      varchar(128) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '活动备注',
    `create_time` datetime                             DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                             DEFAULT NULL COMMENT '修改时间',
    `update_by`   varchar(32) COLLATE utf8_unicode_ci  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_campaign_id` (`campaign_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='开服活动-类型(2级)';



#
转储表 game_order
# ------------------------------------------------------------

CREATE TABLE `game_order`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `order_id`        varchar(64)    NOT NULL DEFAULT '' COMMENT '自己方订单号',
    `query_id`        varchar(64)             DEFAULT NULL COMMENT '平台方订单号',
    `channel`         varchar(32)             DEFAULT NULL COMMENT '渠道',
    `server_id`       int(8) NOT NULL COMMENT '服务器id',
    `player_id`       bigint(11) NOT NULL COMMENT '支付玩家id',
    `product_id`      int(8) DEFAULT NULL COMMENT '商品id',
    `source`          varchar(64)             DEFAULT NULL COMMENT '订单来源',
    `remote_ip`       varchar(100)   NOT NULL DEFAULT '' COMMENT 'ip地址',
    `order_status`    tinyint(2) NOT NULL COMMENT '0-已提交,未支付, 1-已支付, 2-已转发,未回复, 3-金币发放中, 4-充值成功,金币已发放',
    `order_amount`    decimal(10, 3) NOT NULL COMMENT '订单金额',
    `pay_amount`      decimal(10, 3) NOT NULL COMMENT '实际支付金额',
    `discount_amount` decimal(10, 3)          DEFAULT NULL COMMENT '折扣金额',
    `custom`          text COMMENT '备注',
    `currency`        varchar(50)             DEFAULT NULL COMMENT '充值货币(CNY:人民币)',
    `pay_time`        datetime                DEFAULT NULL COMMENT '订单创建时间戳',
    `send_time`       datetime                DEFAULT NULL COMMENT '发货时间',
    `update_time`     datetime                DEFAULT NULL COMMENT '更新时间',
    `create_time`     datetime                DEFAULT NULL COMMENT '订单创建时间戳',
    `create_date`     date                    DEFAULT NULL COMMENT '订单创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `IDX_SERVER_ID` (`server_id`) USING BTREE,
    KEY               `IDX_CHANNEL` (`channel`),
    KEY               `IDX_ORDER_STATUS` (`order_status`),
    KEY               `IDX_CREATE_TIME` (`create_time`),
    KEY               `IDX_ORDERID` (`order_id`) USING BTREE,
    KEY               `IDX_QUERYID` (`query_id`) USING BTREE,
    KEY               `IDX_PLAYER_DATE` (`player_id`,`create_date`) USING BTREE,
    KEY               `IDX_PRODUCT_DATE` (`product_id`,`create_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='充值订单表';



#
转储表 game_order_huawei
# ------------------------------------------------------------

CREATE TABLE `game_order_huawei`
(
    `id`                  bigint(11) NOT NULL AUTO_INCREMENT,
    `order_id`            varchar(64)  NOT NULL DEFAULT '' COMMENT '商户的订单号',
    `pay_order_id`        varchar(64)  NOT NULL COMMENT '交易单号，用户支付成功后生成',
    `product_id`          varchar(64)  NOT NULL DEFAULT '' COMMENT '商品ID。每种商品必须有唯一的ID，由应用在PMS中维护，或者应用发起购买时传入',
    `price`               bigint(11) NOT NULL COMMENT '商品实际价格*100以后的值。商品实际价格精确到小数点后2位，例如此参数值为501，则表示商品实际价格为5.01',
    `purchase_state`      tinyint(2) NOT NULL DEFAULT '-1' COMMENT '订单交易状态。 -1：初始化 0：已购买 1：已取消 2：已退款 3：待处理',
    `consumption_state`   tinyint(2) NOT NULL COMMENT '确认状态，取值包括： 0 ：未确认 1：已确认 没有值表示不需要确认',
    `application_id`      bigint(11) NOT NULL COMMENT '应用ID',
    `developer_payload`   text         NOT NULL COMMENT '透传参数',
    `purchase_token`      varchar(128) NOT NULL COMMENT '用于唯一标识商品和用户对应关系的购买令牌，在支付完成时由华为应用内支付服务器生成',
    `auto_renewing`       tinyint(2) NOT NULL COMMENT '消耗型商品或者非消耗型商品：固定为false',
    `kind`                tinyint(2) NOT NULL COMMENT '商品类别，取值包括：0：消耗型商品 1：非消耗型商品 2：订阅型商品',
    `product_name`        varchar(64)  NOT NULL DEFAULT '' COMMENT '商品名称',
    `purchase_time`       bigint(11) DEFAULT NULL COMMENT '商品购买时间，UTC时间戳，以毫秒为单位',
    `developer_challenge` varchar(64)           DEFAULT NULL COMMENT '应用发起消耗请求时自定义的挑战字，可唯一标识此次消耗请求，仅一次性商品存在',
    `purchase_type`       tinyint(2) NOT NULL COMMENT '购买类型。 0：沙盒环境 1：促销，暂不支持',
    `currency`            varchar(16)  NOT NULL COMMENT '定价货币的币种',
    `country`             varchar(16)  NOT NULL COMMENT '国家/地区码',
    `pay_type`            varchar(16)  NOT NULL COMMENT '支付方式',
    `account_flag`        varchar(16)  NOT NULL DEFAULT '' COMMENT '帐号类型。取值如下： 0：华为帐号 1：AppTouch用户帐号',
    `create_time`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date`         date         NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `IDX_ORDER_NO` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='华为支付订单';



#
转储表 game_order_pn
# ------------------------------------------------------------

CREATE TABLE `game_order_pn`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT,
    `uid`            varchar(32)  NOT NULL DEFAULT '' COMMENT '用户名称',
    `product_id`     varchar(64)  NOT NULL DEFAULT '' COMMENT 'sku',
    `server_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '区服id',
    `order_id`       varchar(64)  NOT NULL DEFAULT '' COMMENT '商户的订单号',
    `transaction_id` varchar(64)  NOT NULL DEFAULT '' COMMENT '交易订单号',
    `nonce`          text         NOT NULL COMMENT '透传参数',
    `sign`           varchar(128) NOT NULL COMMENT '签名',
    `pay_type`       varchar(64)  NOT NULL DEFAULT '' COMMENT '支付类型',
    `create_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date`    date         NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `IDX_ORDER_NO` (`order_id`) USING BTREE,
    KEY              `IDX_USER` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='PnSdk订单记录表';



#
转储表 game_order_record
# ------------------------------------------------------------

CREATE TABLE `game_order_record`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT,
    `account`          varchar(100)           DEFAULT '' COMMENT '帐号',
    `server_id`        int(8) NOT NULL COMMENT '服务器id',
    `player_id`        bigint(11) NOT NULL COMMENT '角色 ID',
    `channel`          varchar(64)            DEFAULT NULL COMMENT '渠道',
    `order_id`         varchar(64)   NOT NULL DEFAULT '' COMMENT '渠道方订单 ID',
    `query_id`         varchar(64)   NOT NULL DEFAULT '' COMMENT '平台SDK订单号',
    `uid`              varchar(64)            DEFAULT NULL COMMENT '平台渠道帐号 ID',
    `pay_amount`       decimal(8, 3) NOT NULL COMMENT '支付金额(单位元)',
    `currency`         varchar(32)            DEFAULT NULL COMMENT '货币名称, eg:CNY',
    `goods_id`         varchar(64)            DEFAULT '' COMMENT '商品ID,参数对一些有商品列表 的充值有效',
    `goods_name`       varchar(64)            DEFAULT NULL COMMENT '商品名称',
    `remote_ip`        varchar(64)            DEFAULT NULL COMMENT '客户端充值 IP',
    `custom`           text COMMENT '透传参数 ,长度 512',
    `server_sign`      varchar(64)            DEFAULT NULL COMMENT '发给充值服务器验证',
    `sign`             varchar(64)            DEFAULT NULL COMMENT '升级Md5加密的值',
    `post_time`        datetime               DEFAULT NULL COMMENT '回调给游戏服时间',
    `game_simple_name` varchar(64)            DEFAULT NULL COMMENT '平台SDK后台游戏代号名',
    `sdk_simple_name`  varchar(64)            DEFAULT NULL COMMENT '平台SDK代号',
    `status`           tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否有效，1-有效 0-无效',
    `create_month`     varchar(20)            DEFAULT NULL COMMENT '月份',
    `create_time`      timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date`      date          NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ORDER_QUERY_ID` (`order_id`,`query_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单记录';



#
转储表 game_order_vn
# ------------------------------------------------------------

CREATE TABLE `game_order_vn`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `user_id`     varchar(32) NOT NULL DEFAULT '' COMMENT '用户id',
    `role_id`     bigint(11) NOT NULL COMMENT '角色id',
    `server_id`   int(8) NOT NULL COMMENT '区服id',
    `order_id`    varchar(64) NOT NULL DEFAULT '' COMMENT '商户的订单号',
    `payload`     varchar(512)         DEFAULT NULL COMMENT '透传参数（IAP才有）',
    `item_id`     varchar(32) NOT NULL DEFAULT '' COMMENT '商品id',
    `money`       bigint(11) NOT NULL COMMENT '支付金额',
    `gold`        bigint(11) NOT NULL COMMENT '游戏币数量',
    `time`        bigint(11) NOT NULL COMMENT '时间戳',
    `sign`        varchar(64) NOT NULL DEFAULT '' COMMENT '签名',
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date` date        NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `IDX_ORDER_NO` (`order_id`) USING BTREE,
    KEY           `IDX_USER` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='越南支付订单';



#
转储表 game_order_youai
# ------------------------------------------------------------

CREATE TABLE `game_order_youai`
(
    `id`               bigint(11) NOT NULL AUTO_INCREMENT,
    `player_id`        bigint(11) NOT NULL COMMENT 'playerId',
    `server_id`        bigint(11) NOT NULL COMMENT '服务器 ID',
    `order_id`         varchar(64)  NOT NULL DEFAULT '' COMMENT '渠道方订单 ID',
    `query_id`         varchar(64)  NOT NULL DEFAULT '' COMMENT '平台SDK订单号',
    `pay_amount`       decimal(10, 3)        DEFAULT NULL COMMENT '充值金额(如有商品 ID,此值为商 品的金额, 单位元)',
    `currency`         varchar(16)  NOT NULL DEFAULT '' COMMENT '充值货币(CNY:人民币)',
    `goods_id`         varchar(64)  NOT NULL DEFAULT '' COMMENT '商品ID,参数对一些有商品列表 的充值有效',
    `goods_name`       varchar(64)  NOT NULL DEFAULT '' COMMENT '商品名称',
    `remote_ip`        varchar(32)  NOT NULL DEFAULT '' COMMENT '客户端充值 IP',
    `custom`           text         NOT NULL COMMENT '透传参数 ,长度 512',
    `game_simple_name` varchar(64)  NOT NULL DEFAULT '' COMMENT '平台SDK后台游戏代号名',
    `sdk_simple_name`  varchar(64)  NOT NULL DEFAULT '' COMMENT '平台SDK代号',
    `u_id`             varchar(32)           DEFAULT NULL COMMENT '平台渠道帐号 ID',
    `server_sign`      varchar(128) NOT NULL DEFAULT '' COMMENT '发给充值服务器验证的注意:(改为游戏本身的 gameAppkey 值)',
    `sign`             varchar(128) NOT NULL COMMENT '升级Md5加密的值',
    `post_time`        varchar(64)  NOT NULL DEFAULT '' COMMENT '订单创建时间戳',
    `create_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date`      date         NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `IDX_ORDER_ID` (`order_id`) USING BTREE,
    KEY                `IDX_USER` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='9133/光卡订单记录表';



#
转储表 game_order_youdian
# ------------------------------------------------------------

CREATE TABLE `game_order_youdian`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `app_id`      varchar(64)  NOT NULL DEFAULT '' COMMENT '商户在支付平台申请的应用 ID',
    `pay_no`      varchar(64)  NOT NULL DEFAULT '' COMMENT '支付平台的交易订单号',
    `order_no`    varchar(64)  NOT NULL DEFAULT '' COMMENT '商户的订单号',
    `user_name`   varchar(32)  NOT NULL DEFAULT '' COMMENT '用户名称',
    `result`      tinyint(2) NOT NULL COMMENT '交易结果 1 表示成功，2 表示失败',
    `pay_channel` varchar(64)  NOT NULL DEFAULT '' COMMENT '值由 orderType 决定，orderType=1 时为支 付简码；orderType=2 时为渠道简码',
    `amount`      decimal(10, 3)        DEFAULT NULL COMMENT '支付金额（元）',
    `pay_time`    varchar(64)           DEFAULT NULL COMMENT '支付时间, 格式 yyyy-MM-dd HH:mm:ss',
    `order_type`  tinyint(2) NOT NULL COMMENT '订单类型：1 平台订单；2 联运渠道订单',
    `custom`      text         NOT NULL COMMENT '透传参数',
    `sign`        varchar(128) NOT NULL COMMENT '签名',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date` date         NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `IDX_ORDER_NO` (`order_no`) USING BTREE,
    KEY           `IDX_USER` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='优点订单记录表';



#
转储表 game_order_youdian_iap
# ------------------------------------------------------------

CREATE TABLE `game_order_youdian_iap`
(
    `id`                   bigint(11) NOT NULL AUTO_INCREMENT,
    `app_id`               varchar(64)  NOT NULL DEFAULT '' COMMENT '用户名称',
    `pay_no`               varchar(64)  NOT NULL DEFAULT '' COMMENT '支付平台的交易订单号',
    `order_no`             varchar(64)  NOT NULL DEFAULT '' COMMENT '商户的订单号',
    `app_version`          varchar(64)  NOT NULL DEFAULT '' COMMENT '应用的版本',
    `apple_transaction_id` varchar(64)  NOT NULL DEFAULT '' COMMENT '支付平台的交易订单号',
    `pay_time`             varchar(64)           DEFAULT NULL COMMENT '支付时间, 格式 yyyy-MM-dd HH:mm:ss',
    `result`               tinyint(2) NOT NULL COMMENT '交易结果 1 表示成功，2 表示失败',
    `amount`               decimal(10, 3)        DEFAULT NULL COMMENT '支付金额（元）',
    `user_name`            varchar(64)  NOT NULL DEFAULT '' COMMENT '用户名称',
    `order_type`           tinyint(2) NOT NULL COMMENT '订单类型：1 平台订单；2 联运渠道订单',
    `sandbox`              tinyint(2) NOT NULL COMMENT '是否为沙箱环境1表示沙箱，0 表示生产环境\n\n1 表示沙箱，0 表示生产环境',
    `product_no`           varchar(64)  NOT NULL DEFAULT '' COMMENT '产品编号 对应苹果的内购点的编号',
    `quantity`             int(8) NOT NULL COMMENT '购买产品数量',
    `custom`               text         NOT NULL COMMENT '透传参数',
    `sign`                 varchar(128) NOT NULL COMMENT '签名',
    `create_time`          timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date`          date         NOT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                    `IDX_ORDER_NO` (`product_no`) USING BTREE,
    KEY                    `IDX_USER` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='优点订单记录表';



#
转储表 game_player
# ------------------------------------------------------------

CREATE TABLE `game_player`
(
    `id`                        bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `player_id`                 bigint(11) NOT NULL COMMENT '玩家id',
    `server_id`                 int(8) NOT NULL COMMENT '服务器id',
    `sid`                       int(8) NOT NULL DEFAULT '0' COMMENT '原始服务器id',
    `account`                   varchar(64)                       NOT NULL DEFAULT '' COMMENT '玩家账号',
    `nickname`                  varchar(64) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '角色昵称',
    `distinct_id`               varchar(64)                                DEFAULT NULL COMMENT 'distinctId',
    `channel`                   varchar(20)                                DEFAULT NULL COMMENT '玩家渠道',
    `avatar`                    varchar(50)                                DEFAULT NULL COMMENT '角色头像',
    `sex`                       tinyint(2) DEFAULT NULL COMMENT '性别',
    `level`                     int(8) NOT NULL DEFAULT '1' COMMENT '境界等级',
    `status`                    tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态：1-正常，0-废弃',
    `realm`                     tinyint(2) DEFAULT '1' COMMENT '界: 1.人界, 2.飞升灵界, 3.飞升仙界, 4.飞升圣界',
    `skip_cartoon`              tinyint(2) DEFAULT '0' COMMENT '是否跳过战斗动画 0-否 1-是',
    `backpack_size`             int(8) DEFAULT '30' COMMENT '背包大小',
    `backpack_level`            int(8) DEFAULT '1' COMMENT '背包等级',
    `practice_value`            bigint(11) NOT NULL DEFAULT '0' COMMENT '修为值',
    `practice_year`             int(8) NOT NULL DEFAULT '0' COMMENT '修炼年数',
    `combat_power`              bigint(11) NOT NULL DEFAULT '0' COMMENT '战力',
    `combat_power_compensation` bigint(11) NOT NULL DEFAULT '0' COMMENT '战力补偿',
    `charm_value`               bigint(11) DEFAULT '0' COMMENT '魅力值',
    `vip_level`                 int(8) NOT NULL DEFAULT '0' COMMENT 'vip等级',
    `race`                      tinyint(2) NOT NULL DEFAULT '0' COMMENT '种族id',
    `spirit_root_code`          int(8) NOT NULL DEFAULT '2001' COMMENT '本命灵根',
    `practice_state`            tinyint(2) DEFAULT '0' COMMENT '修为加持状态0-未开启 1-已开启 2-已结束',
    `open_practice_time`        bigint(11) DEFAULT '0' COMMENT '醍醐灌顶开启时间戳',
    `success_rate`              int(8) DEFAULT '0' COMMENT '渡劫增加成功率百分比',
    `settle_time`               datetime                                   DEFAULT NULL COMMENT '修炼值结算时间',
    `login_ip`                  varchar(64)                                DEFAULT NULL COMMENT '登录设备IP',
    `login_time`                datetime                                   DEFAULT NULL COMMENT '登录时间',
    `logout_time`               datetime                                   DEFAULT NULL COMMENT '登出时间',
    `level_update_time`         datetime                                   DEFAULT NULL COMMENT '境界更新时间',
    `create_time`               timestamp                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_date`               date                                       DEFAULT NULL COMMENT '创建日期',
    `update_time`               timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                         `IDX_NAME` (`nickname`) USING BTREE,
    KEY                         `IDX_ACCOUNT` (`account`) USING BTREE,
    KEY                         `IDX_POWER` (`combat_power`) USING BTREE,
    KEY                         `IDX_LEVEL` (`level`) USING BTREE,
    KEY                         `IDX_TIME` (`create_time`) USING BTREE,
    KEY                         `IDX_SERVER` (`server_id`) USING BTREE,
    KEY                         `IDX_CHANNEL` (`channel`) USING BTREE,
    KEY                         `IDX_DISCARD` (`server_id`,`level`,`login_time`) USING BTREE,
    KEY                         `IDX_SID_STATUS` (`sid`,`status`) USING BTREE,
    KEY                         `IDX_PLAYER_DATE` (`player_id`,`create_date`) USING BTREE,
    KEY                         `IDX_PLAYER_SERVER_LEVEL` (`player_id`,`server_id`,`level`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='玩家表';



#
转储表 game_player_ban_info
# ------------------------------------------------------------

CREATE TABLE `game_player_ban_info`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `player_id`      bigint(11) NOT NULL COMMENT '玩家id',
    `nickname`       varchar(20) CHARACTER SET utf8  DEFAULT NULL COMMENT '玩家昵称',
    `server_id`      int(8) DEFAULT NULL COMMENT '服务器id',
    `ip_ban`         varchar(20) CHARACTER SET utf8  DEFAULT NULL COMMENT 'ip标识禁用',
    `player_id_ban`  bigint(11) DEFAULT NULL COMMENT '玩家id标识禁用',
    `identifier_ban` varchar(20) CHARACTER SET utf8  DEFAULT NULL COMMENT '设备唯一标识禁用 : IMEI或者MAC中的一个',
    `type`           tinyint(2) NOT NULL COMMENT '封号类型 1-临时封号, 2-永久封号',
    `ban_reason`     varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '封禁原因',
    `start_time`     timestamp NULL DEFAULT NULL COMMENT '开始时间',
    `end_time`       timestamp NULL DEFAULT NULL COMMENT '结束时间',
    `create_time`    timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp NULL DEFAULT NULL COMMENT '更新时间',
    `create_date`    date NOT NULL COMMENT '创建日期',
    `operator`       varchar(20) CHARACTER SET utf8  DEFAULT NULL COMMENT '操作人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `IDX_PLAYER_ID` (`player_id`) USING BTREE,
    KEY              `IDX_SERVER_ID` (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户封禁信息详情表';



#
转储表 game_player_item_log
# ------------------------------------------------------------

CREATE TABLE `game_player_item_log`
(
    `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `server_id`   bigint(11) NOT NULL COMMENT '服务器id',
    `player_id`   bigint(11) NOT NULL COMMENT '玩家id',
    `item_id`     int(8) NOT NULL COMMENT '道具id',
    `num`         bigint(11) DEFAULT NULL COMMENT '数量',
    `way`         int(8) DEFAULT NULL COMMENT '途径',
    `type`        int(8) DEFAULT NULL COMMENT '方式：1-获取 2-使用',
    `before_num`  bigint(11) DEFAULT '0' COMMENT '更新前数量',
    `after_num`   bigint(11) DEFAULT '0' COMMENT '更新后数量',
    `sync_time`   timestamp NULL DEFAULT NULL COMMENT '同步时间',
    `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY           `INDEX_PLAYER_ITEM_LOG` (`server_id`,`player_id`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 game_questionnaire
# ------------------------------------------------------------

CREATE TABLE `game_questionnaire`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `server_ids`      text COLLATE utf8_unicode_ci COMMENT '服务器id，使用,分割',
    `last_server_ids` text COLLATE utf8_unicode_ci COMMENT '已刷新的服务器id，使用,分割',
    `url`             varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '问卷调查地址',
    `status`          tinyint(2) NOT NULL COMMENT '状态: 0.关闭, 1.开启',
    `start_time`      datetime                             NOT NULL COMMENT '开始时间',
    `end_time`        datetime                             NOT NULL COMMENT '结束时间',
    `remark`          varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '备注',
    `create_time`     datetime                                      DEFAULT NULL COMMENT '创建时间',
    `create_by`       varchar(32) COLLATE utf8_unicode_ci           DEFAULT '' COMMENT '创建人',
    `update_time`     datetime                                      DEFAULT NULL COMMENT '更新时间',
    `update_by`       varchar(32) COLLATE utf8_unicode_ci           DEFAULT '' COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT COMMENT='问卷调查';



#
转储表 game_recharge_goods
# ------------------------------------------------------------

CREATE TABLE `game_recharge_goods`
(
    `id`                bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `goods_id`          int(8) NOT NULL DEFAULT '0' COMMENT '商品Id',
    `sku`               varchar(128)   NOT NULL DEFAULT '' COMMENT 'sku-App内购使用',
    `web_sku`           varchar(128)            DEFAULT '' COMMENT 'web支付使用',
    `goods_type`        tinyint(8) NOT NULL COMMENT '充值分类',
    `goods_group`       tinyint(8) DEFAULT NULL COMMENT '商品类别：1-直充 2-礼包',
    `name`              varchar(256)   NOT NULL DEFAULT '' COMMENT '商品名',
    `price`             decimal(12, 2) NOT NULL COMMENT '价格',
    `local_price`       decimal(12, 2)          DEFAULT NULL COMMENT '当地货币价格',
    `web_local_price`   decimal(12, 2)          DEFAULT NULL COMMENT 'web支付当地货币价格',
    `display_price`     decimal(12, 2)          DEFAULT NULL COMMENT '当地货币显示价格',
    `web_display_price` decimal(12, 2)          DEFAULT NULL COMMENT 'web支付当地货币价格',
    `discount`          decimal(12, 2)          DEFAULT NULL COMMENT '折扣价（打折后的价格）',
    `items`             text COMMENT '奖励列表',
    `amount_stat`       tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否记入累充（0 - 不计入 1 - 记入）',
    `currency`          varchar(10)    NOT NULL DEFAULT 'CNY' COMMENT 'ISO 4217标准货币代码',
    `addition`          text COMMENT '首次额外赠送',
    `exchange`          int(8) NOT NULL COMMENT '游戏币与充值货币(元)的兑换比例',
    `recommend`         tinyint(2) NOT NULL DEFAULT '0' COMMENT '前端特殊标记 0-无 1-推荐 2-礼包',
    `rumor`             varchar(512)            DEFAULT NULL COMMENT '传闻',
    `remark`            varchar(256)            DEFAULT NULL COMMENT '备注',
    `create_time`       datetime                DEFAULT NULL COMMENT '创建时间',
    `create_by`         varchar(256)            DEFAULT '' COMMENT '创建者',
    `update_time`       datetime                DEFAULT NULL COMMENT '更新时间',
    `update_by`         varchar(256)            DEFAULT '' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `INDEX_GOODS_ID` (`goods_id`) USING BTREE,
    KEY                 `IDX_WEB_SKU` (`web_sku`) USING BTREE,
    KEY                 `IDX_TYPE_PRICE` (`goods_type`,`price`) USING BTREE,
    KEY                 `IDX_SKU` (`sku`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏充值商品';



#
转储表 game_recharge_goods_price
# ------------------------------------------------------------

CREATE TABLE `game_recharge_goods_price`
(
    `id`          int(8) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `goods_id`    int(8) NOT NULL COMMENT '充值分类',
    `server_ids`  text COMMENT '服务器id，使用,分割',
    `price`       decimal(8, 2) NOT NULL COMMENT '价格',
    `discount`    decimal(8, 2) DEFAULT NULL COMMENT '折扣',
    `remark`      varchar(256)  DEFAULT NULL COMMENT '备注',
    `status`      tinyint(2) NOT NULL COMMENT '状态：0 - 无效, 1 - 有效',
    `create_time` datetime      DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(256)  DEFAULT '' COMMENT '创建者',
    `update_time` datetime      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(256)  DEFAULT '' COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `IDX_STATUS` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏充值商品特殊价格';



#
转储表 game_recharge_limit
# ------------------------------------------------------------

CREATE TABLE `game_recharge_limit`
(
    `id`            bigint(11) NOT NULL AUTO_INCREMENT,
    `type`          tinyint(8) NOT NULL DEFAULT '0' COMMENT '0-游客，1-8岁以下，2-8到16岁，3-16到18岁，4-18岁以上',
    `single_amount` decimal(10, 2)                     NOT NULL DEFAULT '0.00' COMMENT '单次充值',
    `month_amount`  decimal(10, 2)                     NOT NULL DEFAULT '0.00' COMMENT '月充值',
    `single_tip`    varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '单次充值限制提示',
    `month_tip`     varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '单次充值限制提示',
    `status`        tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态 0-不可用，1-可用',
    `create_time`   timestamp NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值限制';



#
转储表 game_redeem_activity
# ------------------------------------------------------------

CREATE TABLE `game_redeem_activity`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(50)  NOT NULL COMMENT '激活码名称',
    `summary`     varchar(200) NOT NULL COMMENT '礼包说明',
    `limit_type`  tinyint(2) NOT NULL COMMENT '限制类型:0 - 通用 1 - 指定渠道 2 - SERVER 4 - 同一分组只能兑换一次',
    `group_id`    bigint(11) DEFAULT NULL COMMENT '分组id',
    `channel_ids` varchar(200)          DEFAULT NULL COMMENT '限制渠道id',
    `server_ids`  varchar(200)          DEFAULT NULL COMMENT '限制区服id',
    `status`      tinyint(2) NOT NULL COMMENT '活动状态',
    `reward`      varchar(512) NOT NULL COMMENT '奖励',
    `remark`      varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
    `start_time`  datetime              DEFAULT NULL COMMENT '开始时间',
    `end_time`    datetime              DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime              DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='激活码活动';



#
转储表 game_redeem_activity_group
# ------------------------------------------------------------

CREATE TABLE `game_redeem_activity_group`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`        varchar(50)  NOT NULL DEFAULT '' COMMENT '名称',
    `summary`     varchar(200) NOT NULL DEFAULT '' COMMENT '分组说明',
    `limit_count` int(8) NOT NULL COMMENT '限制次数, 0表示不限制',
    `create_time` datetime              DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='激活码活动分组';



#
转储表 game_redeem_code
# ------------------------------------------------------------

CREATE TABLE `game_redeem_code`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `activity_id` bigint(11) NOT NULL COMMENT '激活码活动id',
    `code`        varchar(50) DEFAULT NULL COMMENT '激活码',
    `total_num`   int(8) DEFAULT '1' COMMENT '可使用总数',
    `used_num`    int(8) DEFAULT '0' COMMENT '已使用数量',
    `status`      tinyint(2) NOT NULL COMMENT '状态',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime    DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `IDX_CODE` (`code`),
    KEY           `IDX_ACTIVITY_ID` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='激活码';



#
转储表 game_redeem_code_record
# ------------------------------------------------------------

CREATE TABLE `game_redeem_code_record`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `code`        varchar(50) NOT NULL DEFAULT '' COMMENT '兑换码',
    `channel`     varchar(50) NOT NULL COMMENT '渠道编码',
    `player_id`   bigint(11) unsigned NOT NULL COMMENT '玩家id',
    `group_id`    int(8) DEFAULT NULL COMMENT '分组id',
    `server_id`   int(8) NOT NULL COMMENT '服务器id',
    `remote_ip`   varchar(50) NOT NULL COMMENT '兑换ip',
    `create_time` datetime             DEFAULT NULL COMMENT '创建时间',
    `create_date` datetime             DEFAULT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`),
    KEY           `IDX_PLAYER_ID` (`player_id`),
    KEY           `IDX_CODE` (`code`),
    KEY           `IDX_PLAYERID_GROUPID` (`player_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='激活码记录';



#
转储表 game_register_info
# ------------------------------------------------------------

CREATE TABLE `game_register_info`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `account`        varchar(50) NOT NULL COMMENT '帐号',
    `player_id`      bigint(11) unsigned NOT NULL COMMENT '玩家id',
    `distinct_id`    varchar(64)                       DEFAULT NULL COMMENT 'distinctId',
    `birth_id`       int(8) NOT NULL DEFAULT '0' COMMENT '出身id',
    `server_id`      int(8) NOT NULL DEFAULT '0' COMMENT '服务器id',
    `name`           varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '角色名称',
    `ip`             varchar(50)                       DEFAULT NULL COMMENT '渠道',
    `channel`        varchar(50)                       DEFAULT NULL COMMENT '渠道',
    `source`         varchar(50)                       DEFAULT NULL COMMENT '来源，同一个渠道的不同投放途径',
    `imei`           varchar(50)                       DEFAULT NULL COMMENT 'imei',
    `device_id`      varchar(128)                      DEFAULT NULL COMMENT '设备 id',
    `mac`            varchar(64)                       DEFAULT NULL COMMENT 'mac',
    `idfa`           varchar(64)                       DEFAULT NULL COMMENT 'idfa',
    `vendor`         varchar(50)                       DEFAULT NULL COMMENT '手机品牌',
    `model`          varchar(50)                       DEFAULT NULL COMMENT '手机型号',
    `system`         varchar(50)                       DEFAULT NULL COMMENT '系统名字',
    `system_version` varchar(50)                       DEFAULT NULL COMMENT '系统版本',
    `network`        varchar(50)                       DEFAULT NULL COMMENT '网络类型',
    `version_name`   varchar(50)                       DEFAULT NULL COMMENT 'version_name',
    `version_code`   varchar(50)                       DEFAULT NULL COMMENT 'version_code',
    `platform`       varchar(50)                       DEFAULT NULL COMMENT '平台',
    `create_date`    date                              DEFAULT NULL COMMENT '创建日期',
    `create_time`    datetime                          DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY              `IDX_ACCOUNT` (`account`),
    KEY              `IDX_CHANNEL` (`channel`),
    KEY              `IDX_PLAYER_DATE` (`player_id`,`create_date`) USING BTREE,
    KEY              `IDX_SERVER_DATE` (`server_id`,`create_date`) USING BTREE,
    KEY              `IDX_PLAYER_SERVER` (`player_id`,`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家注册信息';



#
转储表 game_sensitive_word
# ------------------------------------------------------------

CREATE TABLE `game_sensitive_word`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `word`        tinytext NOT NULL COMMENT '敏感词',
    `remark`      varchar(200) DEFAULT NULL COMMENT '备注',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏敏感词库';



#
转储表 game_server
# ------------------------------------------------------------

CREATE TABLE `game_server`
(
    `id`                  int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `game_id`             int(8) DEFAULT NULL COMMENT '游戏id',
    `name`                varchar(30) NOT NULL COMMENT '服务器名字',
    `remark`              varchar(255) DEFAULT NULL COMMENT '备注',
    `tag_id`              int(8) DEFAULT NULL COMMENT '标签id',
    `host`                varchar(50) NOT NULL COMMENT '服务器路径',
    `login_url`           varchar(200) DEFAULT NULL COMMENT '游戏服地址和端口',
    `gm_url`              varchar(200) DEFAULT NULL COMMENT 'GM地址',
    `rpc_url`             varchar(200) DEFAULT NULL COMMENT 'RPC地址',
    `db_name`             varchar(30)  DEFAULT NULL COMMENT '数据库名',
    `open_time`           datetime     DEFAULT NULL COMMENT '服务器开服时间',
    `online_time`         datetime     DEFAULT NULL COMMENT '服务器上线时间',
    `status`              tinyint(2) NOT NULL DEFAULT '0' COMMENT '服务器状态 0-正常 1-流畅 2-火爆 3-维护',
    `outdated`            tinyint(2) NOT NULL DEFAULT '0' COMMENT '服务器状态 0-正常 1-已废弃',
    `reserve_player_id`   bigint(11) NOT NULL DEFAULT '0' COMMENT '保留玩家id',
    `is_maintain`         tinyint(2) NOT NULL DEFAULT '0' COMMENT '维护状态 0-正常 1-开启维护',
    `recommend`           tinyint(2) DEFAULT NULL COMMENT '推荐标识 0-普遍 1-推荐 2-新服 3-推荐新服',
    `ta_statistics`       tinyint(2) NOT NULL DEFAULT '1' COMMENT 'TA统计开关',
    `gm_status`           tinyint(2) NOT NULL DEFAULT '0' COMMENT 'GM开关 0-关 1-开',
    `gm_ip`               varchar(256) DEFAULT NULL COMMENT 'GM可用的ip',
    `gm_player_id`        varchar(256) DEFAULT NULL COMMENT 'GM可用的玩家id',
    `pay_callback_status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '支付回调开关 0-关 1-开',
    `online_stat`         tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否开启在线人数统计',
    `warning`             varchar(50)  DEFAULT '' COMMENT '出错提示信息',
    `min_version`         int(8) DEFAULT NULL COMMENT '客户端最小版本号',
    `max_version`         int(8) DEFAULT NULL COMMENT '客户端最大版本号',
    `db_host`             varchar(25)  DEFAULT NULL COMMENT '数据库路径',
    `db_port`             int(8) DEFAULT NULL COMMENT '数据库端口',
    `db_user`             varchar(30)  DEFAULT NULL COMMENT '数据库用户名',
    `db_password`         varchar(32)  DEFAULT NULL COMMENT '数据库密码',
    `http_allow_ip`       varchar(256) DEFAULT NULL COMMENT '允许调用 http 接口的 ip 地址',
    `type`                tinyint(2) DEFAULT NULL COMMENT '服务器类型 0-混服 1-专服',
    `pid`                 int(8) DEFAULT NULL COMMENT '合服时母服id',
    `merge_time`          datetime     DEFAULT NULL COMMENT '合服时间',
    `single_settle_time`  datetime     DEFAULT NULL COMMENT '单服活动结算时间',
    `extra`               text COMMENT '扩展字段',
    `create_by`           varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`         datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏服务器表';



#
转储表 game_server_group
# ------------------------------------------------------------

CREATE TABLE `game_server_group`
(
    `id`                 bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `gm_url`             varchar(200)          DEFAULT NULL COMMENT 'GM地址',
    `cross_server_url`   varchar(100)          DEFAULT NULL COMMENT '跨服地址',
    `chat_server_url`    varchar(100)          DEFAULT NULL COMMENT '聊天服地址',
    `cross_faction_war`  tinyint(2) NOT NULL DEFAULT '1' COMMENT '跨服仙盟战开关',
    `faction_war_status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否开启仙盟战',
    `cross_settle_time`  datetime              DEFAULT NULL COMMENT '跨服活动结算时间',
    `remark`             varchar(100) NOT NULL DEFAULT '' COMMENT '备注',
    `create_by`          varchar(32)           DEFAULT NULL COMMENT '创建人',
    `create_time`        datetime              DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(32)           DEFAULT NULL COMMENT '修改人',
    `update_time`        datetime              DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏渠道服配置';



#
转储表 game_server_group_item
# ------------------------------------------------------------

CREATE TABLE `game_server_group_item`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `group_id`    bigint(11) NOT NULL COMMENT '分组id',
    `server_id`   int(8) DEFAULT NULL COMMENT '区服id',
    `create_by`   varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_group_id_server_id` (`group_id`,`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏渠道服配置';



#
转储表 game_server_tag
# ------------------------------------------------------------

CREATE TABLE `game_server_tag`
(
    `id`          int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`        varchar(50) NOT NULL COMMENT '标签名',
    `position`    int(8) NOT NULL DEFAULT '0' COMMENT '排序',
    `remark`      varchar(50) DEFAULT NULL COMMENT '备注',
    `create_by`   varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) DEFAULT NULL COMMENT '修改人',
    `update_time` datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `IDX_NAME` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏渠道服配置';



#
转储表 game_setting
# ------------------------------------------------------------

CREATE TABLE `game_setting`
(
    `id`          int(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `dict_key`    varchar(100) NOT NULL COMMENT 'key',
    `dict_value`  varchar(512) NOT NULL DEFAULT '' COMMENT 'value',
    `remark`      varchar(200) NOT NULL COMMENT '描述',
    `create_by`   varchar(32)           DEFAULT NULL COMMENT '创建人',
    `create_time` datetime              DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)           DEFAULT NULL COMMENT '修改人',
    `update_time` datetime              DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `IDX_DICT_KEY` (`dict_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏配置信息';



#
转储表 game_stat_ongoing
# ------------------------------------------------------------

CREATE TABLE `game_stat_ongoing`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `count_date`   date        NOT NULL COMMENT '统计日期',
    `type`         tinyint(2) NOT NULL COMMENT '统计类型 1-留存  2-ltv',
    `register_num` bigint(11) DEFAULT NULL COMMENT '新增玩家',
    `channel`      varchar(50) NOT NULL COMMENT '渠道',
    `server_id`    int(8) NOT NULL COMMENT '服务器id',
    `c1`           decimal(10, 2)       DEFAULT NULL COMMENT '1留存或者ltv',
    `c2`           decimal(10, 2)       DEFAULT NULL COMMENT '2留存或者ltv',
    `c3`           decimal(10, 2)       DEFAULT NULL COMMENT '3留存或者ltv',
    `c4`           decimal(10, 2)       DEFAULT NULL COMMENT '4留存或者ltv',
    `c5`           decimal(10, 2)       DEFAULT NULL COMMENT '5留存或者ltv',
    `c6`           decimal(10, 2)       DEFAULT NULL COMMENT '6留存或者ltv',
    `c7`           decimal(10, 2)       DEFAULT NULL COMMENT '7留存或者ltv',
    `c8`           decimal(10, 2)       DEFAULT NULL COMMENT '8留存或者ltv',
    `c9`           decimal(10, 2)       DEFAULT NULL COMMENT '9留存或者ltv',
    `c10`          decimal(10, 2)       DEFAULT NULL COMMENT '10留存或者ltv',
    `c11`          decimal(10, 2)       DEFAULT NULL COMMENT '11留存或者ltv',
    `c12`          decimal(10, 2)       DEFAULT NULL COMMENT '12留存或者ltv',
    `c13`          decimal(10, 2)       DEFAULT NULL COMMENT '13留存或者ltv',
    `c14`          decimal(10, 2)       DEFAULT NULL COMMENT '14留存或者ltv',
    `c15`          decimal(10, 2)       DEFAULT NULL COMMENT '15留存或者ltv',
    `c16`          decimal(10, 2)       DEFAULT NULL COMMENT '16留存或者ltv',
    `c17`          decimal(10, 2)       DEFAULT NULL COMMENT '17留存或者ltv',
    `c18`          decimal(10, 2)       DEFAULT NULL COMMENT '18留存或者ltv',
    `c19`          decimal(10, 2)       DEFAULT NULL COMMENT '19留存或者ltv',
    `c20`          decimal(10, 2)       DEFAULT NULL COMMENT '20留存或者ltv',
    `c21`          decimal(10, 2)       DEFAULT NULL COMMENT '21留存或者ltv',
    `c22`          decimal(10, 2)       DEFAULT NULL COMMENT '22留存或者ltv',
    `c23`          decimal(10, 2)       DEFAULT NULL COMMENT '23留存或者ltv',
    `c24`          decimal(10, 2)       DEFAULT NULL COMMENT '24留存或者ltv',
    `c25`          decimal(10, 2)       DEFAULT NULL COMMENT '25留存或者ltv',
    `c26`          decimal(10, 2)       DEFAULT NULL COMMENT '26留存或者ltv',
    `c27`          decimal(10, 2)       DEFAULT NULL COMMENT '27留存或者ltv',
    `c28`          decimal(10, 2)       DEFAULT NULL COMMENT '28留存或者ltv',
    `c29`          decimal(10, 2)       DEFAULT NULL COMMENT '29留存或者ltv',
    `c30`          decimal(10, 2)       DEFAULT NULL COMMENT '30留存或者ltv',
    `create_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `COUNT_ONGOING` (`channel`,`server_id`,`type`,`count_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='30天留存和ltv统计';



#
转储表 game_story_analysis
# ------------------------------------------------------------

CREATE TABLE `game_story_analysis`
(
    `id`                   bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增',
    `channel`              varchar(128) NOT NULL COMMENT '渠道',
    `server_id`            int(8) NOT NULL COMMENT '服务器ID',
    `analysis_date`        datetime     NOT NULL COMMENT '分析日期',
    `story_checkpoint`     int(8) NOT NULL COMMENT '剧情小关卡',
    `stay_live_num`        int(8) DEFAULT NULL COMMENT '停留活跃人数',
    `live_rate`            int(8) DEFAULT NULL COMMENT '活跃占比',
    `stay_leave_num`       int(8) DEFAULT NULL COMMENT '停留流失人数',
    `leave_rate`           int(8) DEFAULT NULL COMMENT '流失占比',
    `total_arrive_num`     int(8) DEFAULT NULL COMMENT '总达成人数',
    `total_stay_num`       int(8) DEFAULT NULL COMMENT '总滞留人数',
    `checkpoint_stay_rate` int(8) DEFAULT NULL COMMENT '关卡滞留率',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='剧情玩法分析';



#
转储表 game_upgrade_notice
# ------------------------------------------------------------

CREATE TABLE `game_upgrade_notice`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `title`           varchar(512)                    NOT NULL DEFAULT '' COMMENT '标题',
    `notice_msg`      text                            NOT NULL COMMENT '正文',
    `reward`          varchar(512) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '奖励',
    `server_ids`      varchar(512) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '服务器',
    `last_server_ids` varchar(512) CHARACTER SET utf8          DEFAULT '' COMMENT '更新前的服务器',
    `status`          tinyint(2) NOT NULL COMMENT '状态0-关闭',
    `start_time`      datetime                        NOT NULL COMMENT '开始时间',
    `end_time`        datetime                        NOT NULL COMMENT '结束时间',
    `create_time`     datetime                                 DEFAULT NULL COMMENT '创建时间',
    `create_by`       varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '创建者',
    `update_time`     datetime                                 DEFAULT NULL COMMENT '更新时间',
    `update_by`       varchar(32) CHARACTER SET utf8           DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    KEY               `INDEX_UPDATE_NOTICE` (`title`,`start_time`,`end_time`),
    KEY               `INDEX_UPGRADE_NOTICE` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='更新公告';



#
转储表 game_user_account
# ------------------------------------------------------------

CREATE TABLE `game_user_account`
(
    `id`                 bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增id 也是玩家注册的自动排序号',
    `account`            varchar(64) NOT NULL DEFAULT '' COMMENT '帐号',
    `user_type`          varchar(50) NOT NULL DEFAULT '0' COMMENT '用户类型',
    `open_id`            varchar(64) NOT NULL DEFAULT '' COMMENT 'openid',
    `username`           varchar(50)          DEFAULT NULL COMMENT '角色名称',
    `role_num`           int(8) NOT NULL DEFAULT '0' COMMENT '角色数量',
    `device_id`          varchar(64)          DEFAULT NULL COMMENT '设备id',
    `channel`            varchar(20)          DEFAULT NULL COMMENT '渠道key',
    `source`             varchar(20)          DEFAULT NULL COMMENT '来源',
    `game_name`          varchar(50)          DEFAULT NULL COMMENT '游戏名称',
    `sdk_name`           varchar(50)          DEFAULT NULL COMMENT 'sdk名称',
    `sdk_version`        varchar(50)          DEFAULT NULL COMMENT 'sdk版本',
    `id_card`            varchar(100)         DEFAULT NULL COMMENT '身份证号码',
    `birthday`           date                 DEFAULT NULL COMMENT '生日',
    `real_name`          varchar(100)         DEFAULT NULL COMMENT '真实姓名',
    `account_type`       tinyint(2) DEFAULT NULL COMMENT '防沉迷帐号分类（4类）',
    `is_verified`        tinyint(2) DEFAULT '0' COMMENT '是否已经实名认证',
    `status`             tinyint(2) DEFAULT '1' COMMENT '帐号状态, 0 - 无效 1 - 有效',
    `visitor_limit_time` datetime             DEFAULT NULL COMMENT '游客在线时长统计的重置时间',
    `last_login_time`    datetime             DEFAULT NULL COMMENT '上次登录时间',
    `create_time`        timestamp NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家帐号信息';



#
转储表 game_user_online_record
# ------------------------------------------------------------

CREATE TABLE `game_user_online_record`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `account`     varchar(50) DEFAULT NULL COMMENT '帐号',
    `server_id`   int(8) DEFAULT NULL COMMENT '服务器 id',
    `player_id`   bigint(11) unsigned DEFAULT NULL COMMENT '玩家id',
    `device_id`   varchar(64) DEFAULT NULL COMMENT '设备id',
    `ip`          varchar(32) DEFAULT NULL COMMENT 'ip',
    `channel`     varchar(20) DEFAULT NULL COMMENT '渠道',
    `start_time`  datetime    DEFAULT NULL COMMENT '上线时间',
    `end_time`    datetime    DEFAULT NULL COMMENT '下线时间',
    `duration`    bigint(11) DEFAULT NULL COMMENT '在线时长',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `create_date` date        DEFAULT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`),
    KEY           `IDX_SERVERID_PLAYERID` (`server_id`,`player_id`),
    KEY           `IDX_PLAYERID` (`player_id`),
    KEY           `IDX_S_P_D` (`server_id`,`player_id`,`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家在线时长记录';



#
转储表 game_user_online_time
# ------------------------------------------------------------

CREATE TABLE `game_user_online_time`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `account`     varchar(50) DEFAULT NULL COMMENT '帐号',
    `duration`    bigint(11) DEFAULT NULL COMMENT '在线时长（秒）',
    `player_id`   bigint(11) DEFAULT NULL COMMENT '玩家 id',
    `server_id`   int(8) DEFAULT NULL COMMENT '区服 id',
    `device_id`   varchar(64) DEFAULT NULL COMMENT '设备id',
    `channel`     varchar(20) DEFAULT NULL COMMENT '渠道',
    `ip`          varchar(32) DEFAULT NULL COMMENT 'ip',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `create_date` date        DEFAULT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`),
    KEY           `IDX_ACCOUNT` (`account`),
    KEY           `IDX_PLAYERID` (`player_id`),
    KEY           `IDX_P_D` (`player_id`,`create_date`),
    KEY           `idx_account_date` (`account`,`create_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家在线时长记录';



#
转储表 game_virtual_order
# ------------------------------------------------------------

CREATE TABLE `game_virtual_order`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `server_id`   int(8) NOT NULL COMMENT '区服id',
    `player_id`   bigint(11) NOT NULL COMMENT '玩家id',
    `goods_id`    int(8) NOT NULL COMMENT '商品id',
    `status`      tinyint(2) NOT NULL COMMENT '状态，0-失败 1-成功',
    `remark`      varchar(200) DEFAULT NULL COMMENT '备注',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='虚拟充值记录';



#
转储表 jeecg_monthly_growth_analysis
# ------------------------------------------------------------

CREATE TABLE `jeecg_monthly_growth_analysis`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT,
    `year`         varchar(50)    DEFAULT NULL,
    `month`        varchar(50)    DEFAULT NULL COMMENT '月份',
    `main_income`  decimal(18, 2) DEFAULT '0.00' COMMENT '佣金/主营收入',
    `other_income` decimal(18, 2) DEFAULT '0.00' COMMENT '其他收入',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 jeecg_order_customer
# ------------------------------------------------------------

CREATE TABLE `jeecg_order_customer`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键',
    `name`        varchar(100) NOT NULL COMMENT '客户名',
    `sex`         varchar(4)   DEFAULT NULL COMMENT '性别',
    `idcard`      varchar(18)  DEFAULT NULL COMMENT '身份证号码',
    `idcard_pic`  varchar(500) DEFAULT NULL COMMENT '身份证扫描件',
    `telphone`    varchar(32)  DEFAULT NULL COMMENT '电话1',
    `order_id`    varchar(32)  NOT NULL COMMENT '外键',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 jeecg_order_main
# ------------------------------------------------------------

CREATE TABLE `jeecg_order_main`
(
    `id`          varchar(32) NOT NULL COMMENT '主键',
    `order_code`  varchar(50)  DEFAULT NULL COMMENT '订单号',
    `ctype`       varchar(500) DEFAULT NULL COMMENT '订单类型',
    `order_date`  datetime     DEFAULT NULL COMMENT '订单日期',
    `order_money` double(10, 3
) DEFAULT NULL COMMENT '订单金额',
  `content` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `bpm_status` varchar(3) DEFAULT NULL COMMENT '流程状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 jeecg_order_ticket
# ------------------------------------------------------------

CREATE TABLE `jeecg_order_ticket`
(
    `id`           varchar(32)  NOT NULL COMMENT '主键',
    `ticket_code`  varchar(100) NOT NULL COMMENT '航班号',
    `tickect_date` datetime    DEFAULT NULL COMMENT '航班时间',
    `order_id`     varchar(32)  NOT NULL COMMENT '外键',
    `create_by`    varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(32) DEFAULT NULL COMMENT '修改人',
    `update_time`  datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 jeecg_project_nature_income
# ------------------------------------------------------------

CREATE TABLE `jeecg_project_nature_income`
(
    `id`                       int(8) NOT NULL AUTO_INCREMENT,
    `nature`                   varchar(50) NOT NULL COMMENT '项目性质',
    `insurance_fee`            decimal(18, 2) DEFAULT '0.00' COMMENT '保险经纪佣金费',
    `risk_consulting_fee`      decimal(18, 2) DEFAULT '0.00' COMMENT '风险咨询费',
    `evaluation_fee`           decimal(18, 2) DEFAULT '0.00' COMMENT '承保公估评估费',
    `insurance_evaluation_fee` decimal(18, 2) DEFAULT '0.00' COMMENT '保险公估费',
    `bidding_consulting_fee`   decimal(18, 2) DEFAULT '0.00' COMMENT '投标咨询费',
    `interol_consulting_fee`   decimal(18, 2) DEFAULT '0.00' COMMENT '内控咨询费',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 jimu_dict
# ------------------------------------------------------------

CREATE TABLE `jimu_dict`
(
    `id`          varchar(32)  NOT NULL,
    `dict_name`   varchar(100) NOT NULL COMMENT '字典名称',
    `dict_code`   varchar(100) NOT NULL COMMENT '字典编码',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `del_flag`    tinyint(2) DEFAULT NULL COMMENT '删除状态',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `type`        tinyint(2) unsigned zerofill DEFAULT '00' COMMENT '字典类型0为string,1为number',
    `tenant_id`   varchar(10)  DEFAULT NULL COMMENT '多租户标识',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_sd_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 jimu_dict_item
# ------------------------------------------------------------

CREATE TABLE `jimu_dict_item`
(
    `id`          varchar(32)  NOT NULL,
    `dict_id`     varchar(32)  DEFAULT NULL COMMENT '字典id',
    `item_text`   varchar(100) NOT NULL COMMENT '字典项文本',
    `item_value`  varchar(100) NOT NULL COMMENT '字典项值',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `sort_order`  int(8) DEFAULT NULL COMMENT '排序',
    `status`      int(8) DEFAULT NULL COMMENT '状态（1启用 0不启用）',
    `create_by`   varchar(32)  DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_by`   varchar(32)  DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_sdi_role_dict_id` (`dict_id`) USING BTREE,
    KEY           `idx_sdi_role_sort_order` (`sort_order`) USING BTREE,
    KEY           `idx_sdi_status` (`status`) USING BTREE,
    KEY           `idx_sdi_dict_val` (`dict_id`,`item_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 jimu_report
# ------------------------------------------------------------

CREATE TABLE `jimu_report`
(
    `id`          varchar(32) NOT NULL COMMENT '主键',
    `code`        varchar(50)  DEFAULT NULL COMMENT '编码',
    `name`        varchar(50)  DEFAULT NULL COMMENT '名称',
    `note`        varchar(255) DEFAULT NULL COMMENT '说明',
    `status`      varchar(10)  DEFAULT NULL COMMENT '状态',
    `type`        varchar(10)  DEFAULT NULL COMMENT '类型',
    `json_str`    longtext COMMENT 'json字符串',
    `api_url`     varchar(255) DEFAULT NULL COMMENT '请求地址',
    `thumb`       text COMMENT '缩略图',
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(50)  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`    tinyint(2) DEFAULT NULL COMMENT '删除标识0-正常,1-已删除',
    `api_method`  varchar(255) DEFAULT NULL COMMENT '请求方法0-get,1-post',
    `api_code`    varchar(255) DEFAULT NULL COMMENT '请求编码',
    `template`    tinyint(2) DEFAULT NULL COMMENT '是否是模板 0不是,1是',
    `view_count`  bigint(11) DEFAULT '0' COMMENT '浏览次数',
    `css_str`     text COMMENT 'css增强',
    `js_str`      text COMMENT 'js增强',
    `tenant_id`   varchar(10)  DEFAULT NULL COMMENT '多租户标识',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_jmreport_code` (`code`) USING BTREE,
    KEY           `uniq_jmreport_createby` (`create_by`) USING BTREE,
    KEY           `uniq_jmreport_delflag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='在线excel设计器';



#
转储表 jimu_report_data_source
# ------------------------------------------------------------

CREATE TABLE `jimu_report_data_source`
(
    `id`            varchar(36) NOT NULL,
    `name`          varchar(100)                   DEFAULT NULL COMMENT '数据源名称',
    `report_id`     varchar(100)                   DEFAULT NULL COMMENT '报表_id',
    `code`          varchar(100)                   DEFAULT NULL COMMENT '编码',
    `remark`        varchar(200)                   DEFAULT NULL COMMENT '备注',
    `db_type`       varchar(10)                    DEFAULT NULL COMMENT '数据库类型',
    `db_driver`     varchar(100)                   DEFAULT NULL COMMENT '驱动类',
    `db_url`        varchar(500)                   DEFAULT NULL COMMENT '数据源地址',
    `db_username`   varchar(100)                   DEFAULT NULL COMMENT '用户名',
    `db_password`   varchar(100)                   DEFAULT NULL COMMENT '密码',
    `create_by`     varchar(50)                    DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime                       DEFAULT NULL COMMENT '创建日期',
    `update_by`     varchar(50)                    DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime                       DEFAULT NULL COMMENT '更新日期',
    `connect_times` int(8) unsigned DEFAULT '0' COMMENT '连接失败次数',
    `tenant_id`     varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '多租户标识',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `idx_jmdatasource_report_id` (`report_id`) USING BTREE,
    KEY             `idx_jmdatasource_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 jimu_report_db
# ------------------------------------------------------------

CREATE TABLE `jimu_report_db`
(
    `id`               varchar(36) NOT NULL COMMENT 'id',
    `jimu_report_id`   varchar(32)  DEFAULT NULL COMMENT '主键字段',
    `create_by`        varchar(50)  DEFAULT NULL COMMENT '创建人登录名称',
    `update_by`        varchar(50)  DEFAULT NULL COMMENT '更新人登录名称',
    `create_time`      datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time`      datetime     DEFAULT NULL COMMENT '更新日期',
    `db_code`          varchar(32)  DEFAULT NULL COMMENT '数据集编码',
    `db_ch_name`       varchar(50)  DEFAULT NULL COMMENT '数据集名字',
    `db_type`          varchar(32)  DEFAULT NULL COMMENT '数据源类型',
    `db_table_name`    varchar(32)  DEFAULT NULL COMMENT '数据库表名',
    `db_dyn_sql`       longtext COMMENT '动态查询SQL',
    `db_key`           varchar(32)  DEFAULT NULL COMMENT '数据源KEY',
    `tb_db_key`        varchar(32)  DEFAULT NULL COMMENT '填报数据源',
    `tb_db_table_name` varchar(32)  DEFAULT NULL COMMENT '填报数据表',
    `java_type`        varchar(32)  DEFAULT NULL COMMENT 'java类数据集  类型（spring:springkey,class:java类名）',
    `java_value`       varchar(255) DEFAULT NULL COMMENT 'java类数据源  数值（bean key/java类名）',
    `api_url`          varchar(255) DEFAULT NULL COMMENT '请求地址',
    `api_method`       varchar(255) DEFAULT NULL COMMENT '请求方法0-get,1-post',
    `is_list`          varchar(10)  DEFAULT '0' COMMENT '是否是列表0否1是 默认0',
    `is_page`          varchar(10)  DEFAULT NULL COMMENT '是否作为分页,0:不分页，1:分页',
    `db_source`        varchar(255) DEFAULT NULL COMMENT '数据源',
    `db_source_type`   varchar(50)  DEFAULT NULL COMMENT '数据库类型 MYSQL ORACLE SQLSERVER',
    `json_data`        text COMMENT 'json数据，直接解析json内容',
    `api_convert`      varchar(255) DEFAULT NULL COMMENT 'api转换器',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_jmreportdb_db_key` (`db_key`) USING BTREE,
    KEY                `idx_jimu_report_id` (`jimu_report_id`) USING BTREE,
    KEY                `idx_db_source_id` (`db_source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 jimu_report_db_field
# ------------------------------------------------------------

CREATE TABLE `jimu_report_db_field`
(
    `id`                varchar(36) NOT NULL COMMENT 'id',
    `create_by`         varchar(50)  DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`       datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`         varchar(50)  DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`       datetime     DEFAULT NULL COMMENT '更新日期',
    `jimu_report_db_id` varchar(32)  DEFAULT NULL COMMENT '数据源ID',
    `field_name`        varchar(80)  DEFAULT NULL COMMENT '字段名',
    `field_text`        varchar(50)  DEFAULT NULL COMMENT '字段文本',
    `widget_type`       varchar(50)  DEFAULT NULL COMMENT '控件类型',
    `widget_width`      int(8) DEFAULT NULL COMMENT '控件宽度',
    `order_num`         int(8) DEFAULT NULL COMMENT '排序',
    `search_flag`       tinyint(2) DEFAULT '0' COMMENT '查询标识0否1是 默认0',
    `search_mode`       tinyint(2) DEFAULT NULL COMMENT '查询模式1简单2范围',
    `dict_code`         varchar(255) DEFAULT NULL COMMENT '字典编码支持从表中取数据',
    `search_value`      varchar(100) DEFAULT NULL COMMENT '查询默认值',
    `search_format`     varchar(50)  DEFAULT NULL COMMENT '查询时间格式化表达式',
    `ext_json`          text COMMENT '参数配置',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `idx_jrdf_jimu_report_db_id` (`jimu_report_db_id`) USING BTREE,
    KEY                 `idx_dbfield_order_num` (`order_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 jimu_report_db_param
# ------------------------------------------------------------

CREATE TABLE `jimu_report_db_param`
(
    `id`                  varchar(36) NOT NULL,
    `jimu_report_head_id` varchar(36) NOT NULL COMMENT '动态报表ID',
    `param_name`          varchar(32) NOT NULL COMMENT '参数字段',
    `param_txt`           varchar(32)   DEFAULT NULL COMMENT '参数文本',
    `param_value`         varchar(1000) DEFAULT NULL COMMENT '参数默认值',
    `order_num`           int(8) DEFAULT NULL COMMENT '排序',
    `create_by`           varchar(50)   DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`         datetime      DEFAULT NULL COMMENT '创建日期',
    `update_by`           varchar(50)   DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`         datetime      DEFAULT NULL COMMENT '更新日期',
    `search_flag`         tinyint(2) DEFAULT NULL COMMENT '查询标识0否1是 默认0',
    `search_mode`         tinyint(2) DEFAULT NULL COMMENT '查询模式1简单2范围',
    `widget_type`         varchar(50)   DEFAULT NULL COMMENT '查询控件类型',
    `dict_code`           varchar(255)  DEFAULT NULL COMMENT '字典',
    `search_format`       varchar(50)   DEFAULT NULL COMMENT '查询时间格式化表达式',
    `ext_json`            text COMMENT '参数配置',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `idx_jrdp_jimu_report_head_id` (`jimu_report_head_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 jimu_report_link
# ------------------------------------------------------------

CREATE TABLE `jimu_report_link`
(
    `id`            varchar(32) NOT NULL COMMENT '主键id',
    `report_id`     varchar(32)   DEFAULT NULL COMMENT '积木设计器id',
    `parameter`     text COMMENT '参数',
    `eject_type`    varchar(1)    DEFAULT NULL COMMENT '弹出方式（0 当前页面 1 新窗口）',
    `link_name`     varchar(255)  DEFAULT NULL COMMENT '链接名称',
    `api_method`    varchar(1)    DEFAULT NULL COMMENT '请求方法0-get,1-post',
    `link_type`     varchar(1)    DEFAULT NULL COMMENT '链接方式(0 网络报表 1 网络连接 2 图表联动)',
    `api_url`       varchar(1000) DEFAULT NULL COMMENT '外网api',
    `link_chart_id` varchar(50)   DEFAULT NULL COMMENT '联动图表的ID',
    `requirement`   varchar(255)  DEFAULT NULL COMMENT '条件',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `uniq_link_reportid` (`report_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='超链接配置表';



#
转储表 jimu_report_map
# ------------------------------------------------------------

CREATE TABLE `jimu_report_map`
(
    `id`           varchar(64) NOT NULL COMMENT '主键',
    `label`        varchar(125) DEFAULT NULL COMMENT '地图名称',
    `name`         varchar(125) DEFAULT NULL COMMENT '地图编码',
    `data`         longtext COMMENT '地图数据',
    `create_by`    varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`  datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`     varchar(1)   DEFAULT NULL COMMENT '0表示未删除,1表示删除',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_jmreport_map_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='地图配置表';



#
转储表 jimu_report_share
# ------------------------------------------------------------

CREATE TABLE `jimu_report_share`
(
    `id`                  varchar(32) NOT NULL COMMENT '主键',
    `report_id`           varchar(32)   DEFAULT NULL COMMENT '在线excel设计器id',
    `preview_url`         varchar(1000) DEFAULT NULL COMMENT '预览地址',
    `preview_lock`        varchar(4)    DEFAULT NULL COMMENT '密码锁',
    `last_update_time`    datetime      DEFAULT NULL COMMENT '最后更新时间',
    `term_of_validity`    varchar(1)    DEFAULT NULL COMMENT '有效期(0:永久有效，1:1天，2:7天)',
    `status`              varchar(1)    DEFAULT NULL COMMENT '是否过期(0未过期，1已过期)',
    `preview_lock_status` varchar(1)    DEFAULT NULL COMMENT '是否为密码锁(0 否,1是)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='积木报表预览权限表';



#
转储表 joa_demo
# ------------------------------------------------------------

CREATE TABLE `joa_demo`
(
    `id`          varchar(32)  DEFAULT NULL COMMENT 'ID',
    `name`        varchar(100) DEFAULT NULL COMMENT '请假人',
    `days`        int(8) DEFAULT NULL COMMENT '请假天数',
    `begin_date`  datetime     DEFAULT NULL COMMENT '开始时间',
    `end_date`    datetime     DEFAULT NULL COMMENT '请假结束时间',
    `reason`      varchar(500) DEFAULT NULL COMMENT '请假原因',
    `bpm_status`  varchar(50)  DEFAULT '1' COMMENT '流程状态',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人id',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '修改人id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='流程测试';



#
转储表 onl_auth_data
# ------------------------------------------------------------

CREATE TABLE `onl_auth_data`
(
    `id`            varchar(32) NOT NULL COMMENT '主键',
    `cgform_id`     varchar(32)                    DEFAULT NULL COMMENT 'online表ID',
    `rule_name`     varchar(50)                    DEFAULT NULL COMMENT '规则名',
    `rule_column`   varchar(50)                    DEFAULT NULL COMMENT '规则列',
    `rule_operator` varchar(50)                    DEFAULT NULL COMMENT '规则条件 大于小于like',
    `rule_value`    varchar(255)                   DEFAULT NULL COMMENT '规则值',
    `status`        tinyint(2) DEFAULT NULL COMMENT '1有效 0无效',
    `create_time`   datetime                       DEFAULT NULL COMMENT '创建时间',
    `create_by`     varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(50)                    DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime                       DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 onl_auth_page
# ------------------------------------------------------------

CREATE TABLE `onl_auth_page`
(
    `id`          varchar(32) NOT NULL COMMENT ' 主键',
    `cgform_id`   varchar(32)                    DEFAULT NULL COMMENT 'online表id',
    `code`        varchar(255)                   DEFAULT NULL COMMENT '字段名/按钮编码',
    `type`        tinyint(2) DEFAULT NULL COMMENT '1字段 2按钮',
    `control`     tinyint(2) DEFAULT NULL COMMENT '3可编辑 5可见(仅支持两种状态值3,5)',
    `page`        tinyint(2) DEFAULT NULL COMMENT '3列表 5表单(仅支持两种状态值3,5)',
    `status`      tinyint(2) DEFAULT NULL COMMENT '1有效 0无效',
    `create_time` datetime                       DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
    `update_by`   varchar(50)                    DEFAULT NULL COMMENT '更新人',
    `update_time` datetime                       DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 onl_auth_relation
# ------------------------------------------------------------

CREATE TABLE `onl_auth_relation`
(
    `id`        varchar(32) NOT NULL,
    `role_id`   varchar(32) DEFAULT NULL COMMENT '角色id',
    `auth_id`   varchar(32) DEFAULT NULL COMMENT '权限id',
    `type`      tinyint(2) DEFAULT NULL COMMENT '1字段 2按钮 3数据权限',
    `cgform_id` varchar(32) DEFAULT NULL COMMENT 'online表单ID',
    `auth_mode` varchar(50) DEFAULT NULL COMMENT '授权方式role角色，depart部门，user人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 onl_cgform_button
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_button`
(
    `ID`             varchar(32) NOT NULL COMMENT '主键ID',
    `BUTTON_CODE`    varchar(50)  DEFAULT NULL COMMENT '按钮编码',
    `BUTTON_ICON`    varchar(20)  DEFAULT NULL COMMENT '按钮图标',
    `BUTTON_NAME`    varchar(50)  DEFAULT NULL COMMENT '按钮名称',
    `BUTTON_STATUS`  varchar(2)   DEFAULT NULL COMMENT '按钮状态',
    `BUTTON_STYLE`   varchar(20)  DEFAULT NULL COMMENT '按钮样式',
    `EXP`            varchar(255) DEFAULT NULL COMMENT '表达式',
    `CGFORM_HEAD_ID` varchar(32)  DEFAULT NULL COMMENT '表单ID',
    `OPT_TYPE`       varchar(20)  DEFAULT NULL COMMENT '按钮类型',
    `ORDER_NUM`      int(8) DEFAULT NULL COMMENT '排序',
    `OPT_POSITION`   varchar(3)   DEFAULT NULL COMMENT '按钮位置1侧面 2底部',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY              `index_formid` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `index_button_code` (`BUTTON_CODE`) USING BTREE,
    KEY              `index_button_status` (`BUTTON_STATUS`) USING BTREE,
    KEY              `index_button_order` (`ORDER_NUM`) USING BTREE,
    KEY              `idx_ocb_ORDER_NUM` (`ORDER_NUM`) USING BTREE,
    KEY              `idx_ocb_CGFORM_HEAD_ID` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `idx_ocb_BUTTON_STATUS` (`BUTTON_STATUS`) USING BTREE,
    KEY              `idx_ocb_BUTTON_CODE` (`BUTTON_CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Online表单自定义按钮';



#
转储表 onl_cgform_enhance_java
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_enhance_java`
(
    `ID`             varchar(36)  NOT NULL,
    `BUTTON_CODE`    varchar(32)           DEFAULT NULL COMMENT '按钮编码',
    `CG_JAVA_TYPE`   varchar(32)  NOT NULL COMMENT '类型',
    `CG_JAVA_VALUE`  varchar(200) NOT NULL COMMENT '数值',
    `CGFORM_HEAD_ID` varchar(32)  NOT NULL COMMENT '表单ID',
    `ACTIVE_STATUS`  varchar(2)            DEFAULT '1' COMMENT '生效状态',
    `EVENT`          varchar(10)  NOT NULL DEFAULT 'end' COMMENT '事件状态(end:结束，start:开始)',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY              `index_fmid` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `index_buttoncode` (`BUTTON_CODE`) USING BTREE,
    KEY              `index_status` (`ACTIVE_STATUS`) USING BTREE,
    KEY              `idx_ejava_cgform_head_id` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `idx_ocej_BUTTON_CODE` (`BUTTON_CODE`) USING BTREE,
    KEY              `idx_ocej_ACTIVE_STATUS` (`ACTIVE_STATUS`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 onl_cgform_enhance_js
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_enhance_js`
(
    `ID`             varchar(32) NOT NULL COMMENT '主键ID',
    `CG_JS`          longtext COMMENT 'JS增强内容',
    `CG_JS_TYPE`     varchar(20)   DEFAULT NULL COMMENT '类型',
    `CONTENT`        varchar(1000) DEFAULT NULL COMMENT '备注',
    `CGFORM_HEAD_ID` varchar(32)   DEFAULT NULL COMMENT '表单ID',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY              `index_fmid` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `index_jstype` (`CG_JS_TYPE`) USING BTREE,
    KEY              `idx_ejs_cgform_head_id` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `idx_ejs_cg_js_type` (`CG_JS_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 onl_cgform_enhance_sql
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_enhance_sql`
(
    `ID`             varchar(32) NOT NULL COMMENT '主键ID',
    `BUTTON_CODE`    varchar(50)   DEFAULT NULL COMMENT '按钮编码',
    `CGB_SQL`        longtext COMMENT 'SQL内容',
    `CGB_SQL_NAME`   varchar(50)   DEFAULT NULL COMMENT 'Sql名称',
    `CONTENT`        varchar(1000) DEFAULT NULL COMMENT '备注',
    `CGFORM_HEAD_ID` varchar(32)   DEFAULT NULL COMMENT '表单ID',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY              `index_formid` (`CGFORM_HEAD_ID`) USING BTREE,
    KEY              `idx_oces_CGFORM_HEAD_ID` (`CGFORM_HEAD_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 onl_cgform_field
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_field`
(
    `id`                    varchar(32) NOT NULL COMMENT '主键ID',
    `cgform_head_id`        varchar(32) NOT NULL COMMENT '表ID',
    `db_field_name`         varchar(32) NOT NULL COMMENT '字段名字',
    `db_field_txt`          varchar(200) DEFAULT NULL COMMENT '字段备注',
    `db_field_name_old`     varchar(32)  DEFAULT NULL COMMENT '原字段名',
    `db_is_key`             tinyint(2) DEFAULT NULL COMMENT '是否主键 0否 1是',
    `db_is_null`            tinyint(2) DEFAULT NULL COMMENT '是否允许为空0否 1是',
    `db_type`               varchar(32) NOT NULL COMMENT '数据库字段类型',
    `db_length`             int(8) NOT NULL COMMENT '数据库字段长度',
    `db_point_length`       int(8) DEFAULT NULL COMMENT '小数点',
    `db_default_val`        varchar(20)  DEFAULT NULL COMMENT '表字段默认值',
    `dict_field`            varchar(100) DEFAULT NULL COMMENT '字典code',
    `dict_table`            varchar(255) DEFAULT NULL COMMENT '字典表',
    `dict_text`             varchar(100) DEFAULT NULL COMMENT '字典Text',
    `field_show_type`       varchar(10)  DEFAULT NULL COMMENT '表单控件类型',
    `field_href`            varchar(200) DEFAULT NULL COMMENT '跳转URL',
    `field_length`          int(8) DEFAULT NULL COMMENT '表单控件长度',
    `field_valid_type`      varchar(300) DEFAULT NULL COMMENT '表单字段校验规则',
    `field_must_input`      varchar(2)   DEFAULT NULL COMMENT '字段是否必填',
    `field_extend_json`     varchar(500) DEFAULT NULL COMMENT '扩展参数JSON',
    `field_default_value`   varchar(100) DEFAULT NULL COMMENT '控件默认值，不同的表达式展示不同的结果。\r\n1. 纯字符串直接赋给默认值；\r\n2. #{普通变量}；\r\n3. {{ 动态JS表达式 }}；\r\n4. ${填值规则编码}；\r\n填值规则表达式只允许存在一个，且不能和其他规则混用。',
    `field_value_rule_code` varchar(500) DEFAULT NULL COMMENT '填值规则code',
    `is_query`              tinyint(2) DEFAULT NULL COMMENT '是否查询条件0否 1是',
    `is_show_form`          tinyint(2) DEFAULT NULL COMMENT '表单是否显示0否 1是',
    `is_show_list`          tinyint(2) DEFAULT NULL COMMENT '列表是否显示0否 1是',
    `is_read_only`          tinyint(2) DEFAULT '0' COMMENT '是否是只读（1是 0否）',
    `query_mode`            varchar(10)  DEFAULT NULL COMMENT '查询模式',
    `main_table`            varchar(100) DEFAULT NULL COMMENT '外键主表名',
    `main_field`            varchar(100) DEFAULT NULL COMMENT '外键主键字段',
    `order_num`             int(8) DEFAULT NULL COMMENT '排序',
    `update_by`             varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`           datetime     DEFAULT NULL COMMENT '修改时间',
    `create_time`           datetime     DEFAULT NULL COMMENT '创建时间',
    `create_by`             varchar(255) DEFAULT NULL COMMENT '创建人',
    `converter`             varchar(255) DEFAULT NULL COMMENT '自定义值转换器',
    `query_def_val`         varchar(50)  DEFAULT NULL COMMENT '查询默认值',
    `query_dict_text`       varchar(100) DEFAULT NULL COMMENT '查询配置字典text',
    `query_dict_field`      varchar(100) DEFAULT NULL COMMENT '查询配置字典code',
    `query_dict_table`      varchar(500) DEFAULT NULL COMMENT '查询配置字典table',
    `query_show_type`       varchar(50)  DEFAULT NULL COMMENT '查询显示控件',
    `query_config_flag`     varchar(3)   DEFAULT NULL COMMENT '是否启用查询配置1是0否',
    `query_valid_type`      varchar(50)  DEFAULT NULL COMMENT '查询字段校验类型',
    `query_must_input`      varchar(3)   DEFAULT NULL COMMENT '查询字段是否必填1是0否',
    `sort_flag`             varchar(3)   DEFAULT NULL COMMENT '是否支持排序1是0否',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                     `inex_table_id` (`cgform_head_id`) USING BTREE,
    KEY                     `idx_ocf_cgform_head_id` (`cgform_head_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 onl_cgform_head
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_head`
(
    `id`                   varchar(32)  NOT NULL COMMENT '主键ID',
    `table_name`           varchar(50)  NOT NULL COMMENT '表名',
    `table_type`           tinyint(2) NOT NULL COMMENT '表类型: 0单表、1主表、2附表',
    `table_version`        int(8) DEFAULT '1' COMMENT '表版本',
    `table_txt`            varchar(200) NOT NULL COMMENT '表说明',
    `is_checkbox`          varchar(5)   NOT NULL COMMENT '是否带checkbox',
    `is_db_synch`          varchar(20)  NOT NULL DEFAULT 'N' COMMENT '同步数据库状态',
    `is_page`              varchar(5)   NOT NULL COMMENT '是否分页',
    `is_tree`              varchar(5)   NOT NULL COMMENT '是否是树',
    `id_sequence`          varchar(200)          DEFAULT NULL COMMENT '主键生成序列',
    `id_type`              varchar(100)          DEFAULT NULL COMMENT '主键类型',
    `query_mode`           varchar(10)  NOT NULL COMMENT '查询模式',
    `relation_type`        tinyint(2) DEFAULT NULL COMMENT '映射关系 0一对多  1一对一',
    `sub_table_str`        varchar(1000)         DEFAULT NULL COMMENT '子表',
    `tab_order_num`        int(8) DEFAULT NULL COMMENT '附表排序序号',
    `tree_parent_id_field` varchar(50)           DEFAULT NULL COMMENT '树形表单父id',
    `tree_id_field`        varchar(50)           DEFAULT NULL COMMENT '树表主键字段',
    `tree_fieldname`       varchar(50)           DEFAULT NULL COMMENT '树开表单列字段',
    `form_category`        varchar(50)  NOT NULL DEFAULT 'bdfl_ptbd' COMMENT '表单分类',
    `form_template`        varchar(50)           DEFAULT NULL COMMENT 'PC表单模板',
    `form_template_mobile` varchar(50)           DEFAULT NULL COMMENT '表单模板样式(移动端)',
    `scroll`               tinyint(2) DEFAULT '0' COMMENT '是否有横向滚动条',
    `copy_version`         int(8) DEFAULT NULL COMMENT '复制版本号',
    `copy_type`            tinyint(2) DEFAULT '0' COMMENT '复制表类型1为复制表 0为原始表',
    `physic_id`            varchar(32)           DEFAULT NULL COMMENT '原始表ID',
    `ext_config_json`      varchar(1000)         DEFAULT NULL COMMENT '扩展JSON',
    `update_by`            varchar(32)           DEFAULT NULL COMMENT '修改人',
    `update_time`          datetime              DEFAULT NULL COMMENT '修改时间',
    `create_by`            varchar(32)           DEFAULT NULL COMMENT '创建人',
    `create_time`          datetime              DEFAULT NULL COMMENT '创建时间',
    `theme_template`       varchar(50)           DEFAULT NULL COMMENT '主题模板',
    `is_des_form`          varchar(2)            DEFAULT NULL COMMENT '是否用设计器表单',
    `des_form_code`        varchar(50)           DEFAULT NULL COMMENT '设计器表单编码',
    `low_app_id`           varchar(32)           DEFAULT NULL COMMENT '关联的应用ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_onlineform_table_name` (`table_name`) USING BTREE,
    KEY                    `index_form_templdate` (`form_template`) USING BTREE,
    KEY                    `index_templdate_mobile` (`form_template_mobile`) USING BTREE,
    KEY                    `index_onlineform_table_version` (`table_version`) USING BTREE,
    KEY                    `idx_och_cgform_head_id` (`table_name`) USING BTREE,
    KEY                    `idx_och_table_name` (`form_template`) USING BTREE,
    KEY                    `idx_och_table_version` (`table_version`) USING BTREE,
    KEY                    `idx_och_form_template_mobile` (`form_template_mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 onl_cgform_index
# ------------------------------------------------------------

CREATE TABLE `onl_cgform_index`
(
    `id`             varchar(36) NOT NULL COMMENT '主键',
    `cgform_head_id` varchar(32)  DEFAULT NULL COMMENT '主表id',
    `index_name`     varchar(100) DEFAULT NULL COMMENT '索引名称',
    `index_field`    varchar(500) DEFAULT NULL COMMENT '索引栏位',
    `index_type`     varchar(32)  DEFAULT NULL COMMENT '索引类型',
    `create_by`      varchar(50)  DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`      varchar(50)  DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新日期',
    `is_db_synch`    varchar(2)   DEFAULT 'N' COMMENT '是否同步数据库 N未同步 Y已同步',
    `del_flag`       tinyint(2) DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `index_table_id` (`cgform_head_id`) USING BTREE,
    KEY              `idx_oci_cgform_head_id` (`cgform_head_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 onl_cgreport_head
# ------------------------------------------------------------

CREATE TABLE `onl_cgreport_head`
(
    `id`               varchar(36)   NOT NULL,
    `code`             varchar(100)  NOT NULL COMMENT '报表编码',
    `name`             varchar(100)  NOT NULL COMMENT '报表名字',
    `cgr_sql`          varchar(1000) NOT NULL COMMENT '报表SQL',
    `return_val_field` varchar(100)  DEFAULT NULL COMMENT '返回值字段',
    `return_txt_field` varchar(100)  DEFAULT NULL COMMENT '返回文本字段',
    `return_type`      varchar(2)    DEFAULT '1' COMMENT '返回类型，单选或多选',
    `db_source`        varchar(100)  DEFAULT NULL COMMENT '动态数据源',
    `content`          varchar(1000) DEFAULT NULL COMMENT '描述',
    `low_app_id`       varchar(32)   DEFAULT NULL COMMENT '关联的应用ID',
    `update_time`      datetime      DEFAULT NULL COMMENT '修改时间',
    `update_by`        varchar(32)   DEFAULT NULL COMMENT '修改人id',
    `create_time`      datetime      DEFAULT NULL COMMENT '创建时间',
    `create_by`        varchar(32)   DEFAULT NULL COMMENT '创建人id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_onlinereport_code` (`code`) USING BTREE,
    KEY                `idx_och_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 onl_cgreport_item
# ------------------------------------------------------------

CREATE TABLE `onl_cgreport_item`
(
    `id`          varchar(36) NOT NULL,
    `cgrhead_id`  varchar(36) NOT NULL COMMENT '报表ID',
    `field_name`  varchar(36) NOT NULL COMMENT '字段名字',
    `field_txt`   varchar(300) DEFAULT NULL COMMENT '字段文本',
    `field_width` int(8) DEFAULT NULL,
    `field_type`  varchar(10)  DEFAULT NULL COMMENT '字段类型',
    `search_mode` varchar(10)  DEFAULT NULL COMMENT '查询模式',
    `is_order`    tinyint(2) DEFAULT '0' COMMENT '是否排序  0否,1是',
    `is_search`   tinyint(2) DEFAULT '0' COMMENT '是否查询  0否,1是',
    `dict_code`   varchar(500) DEFAULT NULL COMMENT '字典CODE',
    `field_href`  varchar(120) DEFAULT NULL COMMENT '字段跳转URL',
    `is_show`     tinyint(2) DEFAULT '1' COMMENT '是否显示  0否,1显示',
    `order_num`   int(8) DEFAULT NULL COMMENT '排序',
    `replace_val` varchar(200) DEFAULT NULL COMMENT '取值表达式',
    `is_total`    varchar(2)   DEFAULT NULL COMMENT '是否合计 0否,1是（仅对数值有效）',
    `group_title` varchar(50)  DEFAULT NULL COMMENT '分组标题',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `index_CGRHEAD_ID` (`cgrhead_id`) USING BTREE,
    KEY           `index_isshow` (`is_show`) USING BTREE,
    KEY           `index_order_num` (`order_num`) USING BTREE,
    KEY           `idx_oci_cgrhead_id` (`cgrhead_id`) USING BTREE,
    KEY           `idx_oci_is_show` (`is_show`) USING BTREE,
    KEY           `idx_oci_order_num` (`order_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 onl_cgreport_param
# ------------------------------------------------------------

CREATE TABLE `onl_cgreport_param`
(
    `id`          varchar(36) NOT NULL,
    `cgrhead_id`  varchar(36) NOT NULL COMMENT '动态报表ID',
    `param_name`  varchar(32) NOT NULL COMMENT '参数字段',
    `param_txt`   varchar(32) DEFAULT NULL COMMENT '参数文本',
    `param_value` varchar(32) DEFAULT NULL COMMENT '参数默认值',
    `order_num`   int(8) DEFAULT NULL COMMENT '排序',
    `create_by`   varchar(50) DEFAULT NULL COMMENT '创建人登录名称',
    `create_time` datetime    DEFAULT NULL COMMENT '创建日期',
    `update_by`   varchar(50) DEFAULT NULL COMMENT '更新人登录名称',
    `update_time` datetime    DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `idx_cgrheadid` (`cgrhead_id`) USING BTREE,
    KEY           `idx_ocp_cgrhead_id` (`cgrhead_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 oss_file
# ------------------------------------------------------------

CREATE TABLE `oss_file`
(
    `id`          varchar(32) NOT NULL COMMENT '主键id',
    `file_name`   varchar(255) DEFAULT NULL COMMENT '文件名称',
    `url`         varchar(255) DEFAULT NULL COMMENT '文件地址',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人登录名称',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '更新人登录名称',
    `update_time` datetime     DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Oss File';



#
转储表 qrtz_blob_triggers
# ------------------------------------------------------------

CREATE TABLE `qrtz_blob_triggers`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_NAME`  varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `BLOB_DATA`     blob,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_calendars
# ------------------------------------------------------------

CREATE TABLE `qrtz_calendars`
(
    `SCHED_NAME`    varchar(120) NOT NULL,
    `CALENDAR_NAME` varchar(200) NOT NULL,
    `CALENDAR`      blob         NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_cron_triggers
# ------------------------------------------------------------

CREATE TABLE `qrtz_cron_triggers`
(
    `SCHED_NAME`      varchar(120) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_NAME`    varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_GROUP`   varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `TIME_ZONE_ID`    varchar(80) CHARACTER SET utf8mb4 DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_fired_triggers
# ------------------------------------------------------------

CREATE TABLE `qrtz_fired_triggers`
(
    `SCHED_NAME`        varchar(120) NOT NULL,
    `ENTRY_ID`          varchar(95)  NOT NULL,
    `TRIGGER_NAME`      varchar(200) NOT NULL,
    `TRIGGER_GROUP`     varchar(200) NOT NULL,
    `INSTANCE_NAME`     varchar(200) NOT NULL,
    `FIRED_TIME`        bigint(11) NOT NULL,
    `SCHED_TIME`        bigint(11) NOT NULL,
    `PRIORITY`          int(8) NOT NULL,
    `STATE`             varchar(16)  NOT NULL,
    `JOB_NAME`          varchar(200) DEFAULT NULL,
    `JOB_GROUP`         varchar(200) DEFAULT NULL,
    `IS_NONCONCURRENT`  varchar(1)   DEFAULT NULL,
    `REQUESTS_RECOVERY` varchar(1)   DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_job_details
# ------------------------------------------------------------

CREATE TABLE `qrtz_job_details`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 NOT NULL,
    `JOB_NAME`          varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `JOB_GROUP`         varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `DESCRIPTION`       varchar(250) CHARACTER SET utf8mb4 DEFAULT NULL,
    `JOB_CLASS_NAME`    varchar(250) CHARACTER SET utf8mb4 NOT NULL,
    `IS_DURABLE`        varchar(1) CHARACTER SET utf8mb4   NOT NULL,
    `IS_NONCONCURRENT`  varchar(1) CHARACTER SET utf8mb4   NOT NULL,
    `IS_UPDATE_DATA`    varchar(1) CHARACTER SET utf8mb4   NOT NULL,
    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4   NOT NULL,
    `JOB_DATA`          blob,
    PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_locks
# ------------------------------------------------------------

CREATE TABLE `qrtz_locks`
(
    `SCHED_NAME` varchar(120) NOT NULL,
    `LOCK_NAME`  varchar(40)  NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_paused_trigger_grps
# ------------------------------------------------------------

CREATE TABLE `qrtz_paused_trigger_grps`
(
    `SCHED_NAME`    varchar(120) NOT NULL,
    `TRIGGER_GROUP` varchar(200) NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_scheduler_state
# ------------------------------------------------------------

CREATE TABLE `qrtz_scheduler_state`
(
    `SCHED_NAME`        varchar(120) NOT NULL,
    `INSTANCE_NAME`     varchar(200) NOT NULL,
    `LAST_CHECKIN_TIME` bigint(11) NOT NULL,
    `CHECKIN_INTERVAL`  bigint(11) NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_simple_triggers
# ------------------------------------------------------------

CREATE TABLE `qrtz_simple_triggers`
(
    `SCHED_NAME`      varchar(120) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_NAME`    varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_GROUP`   varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `REPEAT_COUNT`    bigint(11) NOT NULL,
    `REPEAT_INTERVAL` bigint(11) NOT NULL,
    `TIMES_TRIGGERED` bigint(11) NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_simprop_triggers
# ------------------------------------------------------------

CREATE TABLE `qrtz_simprop_triggers`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_NAME`  varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `STR_PROP_1`    varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
    `STR_PROP_2`    varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
    `STR_PROP_3`    varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
    `INT_PROP_1`    int(8) DEFAULT NULL,
    `INT_PROP_2`    int(8) DEFAULT NULL,
    `LONG_PROP_1`   bigint(11) DEFAULT NULL,
    `LONG_PROP_2`   bigint(11) DEFAULT NULL,
    `DEC_PROP_1`    decimal(13, 4)                     DEFAULT NULL,
    `DEC_PROP_2`    decimal(13, 4)                     DEFAULT NULL,
    `BOOL_PROP_1`   varchar(1) CHARACTER SET utf8mb4   DEFAULT NULL,
    `BOOL_PROP_2`   varchar(1) CHARACTER SET utf8mb4   DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 qrtz_triggers
# ------------------------------------------------------------

CREATE TABLE `qrtz_triggers`
(
    `SCHED_NAME`     varchar(120) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_NAME`   varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `TRIGGER_GROUP`  varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `JOB_NAME`       varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `JOB_GROUP`      varchar(200) CHARACTER SET utf8mb4 NOT NULL,
    `DESCRIPTION`    varchar(250) CHARACTER SET utf8mb4 DEFAULT NULL,
    `NEXT_FIRE_TIME` bigint(11) DEFAULT NULL,
    `PREV_FIRE_TIME` bigint(11) DEFAULT NULL,
    `PRIORITY`       int(8) DEFAULT NULL,
    `TRIGGER_STATE`  varchar(16) CHARACTER SET utf8mb4  NOT NULL,
    `TRIGGER_TYPE`   varchar(8) CHARACTER SET utf8mb4   NOT NULL,
    `START_TIME`     bigint(11) NOT NULL,
    `END_TIME`       bigint(11) DEFAULT NULL,
    `CALENDAR_NAME`  varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
    `MISFIRE_INSTR`  smallint(2) DEFAULT NULL,
    `JOB_DATA`       blob,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    KEY              `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 rep_demo_dxtj
# ------------------------------------------------------------

CREATE TABLE `rep_demo_dxtj`
(
    `id`        varchar(32) NOT NULL COMMENT '主键',
    `name`      varchar(50)  DEFAULT NULL COMMENT '姓名',
    `gtime`     datetime     DEFAULT NULL COMMENT '雇佣日期',
    `update_by` varchar(50)  DEFAULT NULL COMMENT '职务',
    `jphone`    varchar(125) DEFAULT NULL COMMENT '家庭电话',
    `birth`     datetime     DEFAULT NULL COMMENT '出生日期',
    `hukou`     varchar(32)  DEFAULT NULL COMMENT '户口所在地',
    `laddress`  varchar(125) DEFAULT NULL COMMENT '联系地址',
    `jperson`   varchar(32)  DEFAULT NULL COMMENT '紧急联系人',
    `sex`       varchar(32)  DEFAULT NULL COMMENT 'xingbie',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 rep_demo_employee
# ------------------------------------------------------------

CREATE TABLE `rep_demo_employee`
(
    `id`                     varchar(10) NOT NULL COMMENT '主键',
    `num`                    varchar(50)  DEFAULT NULL COMMENT '编号',
    `name`                   varchar(100) DEFAULT NULL COMMENT '姓名',
    `sex`                    varchar(10)  DEFAULT NULL COMMENT '性别',
    `birthday`               datetime     DEFAULT NULL COMMENT '出生日期',
    `nation`                 varchar(30)  DEFAULT NULL COMMENT '民族',
    `political`              varchar(30)  DEFAULT NULL COMMENT '政治面貌',
    `native_place`           varchar(30)  DEFAULT NULL COMMENT '籍贯',
    `height`                 varchar(30)  DEFAULT NULL COMMENT '身高',
    `weight`                 varchar(30)  DEFAULT NULL COMMENT '体重',
    `health`                 varchar(30)  DEFAULT NULL COMMENT '健康状况',
    `id_card`                varchar(80)  DEFAULT NULL COMMENT '身份证号',
    `education`              varchar(30)  DEFAULT NULL COMMENT '学历',
    `school`                 varchar(80)  DEFAULT NULL COMMENT '毕业学校',
    `major`                  varchar(80)  DEFAULT NULL COMMENT '专业',
    `address`                varchar(100) DEFAULT NULL COMMENT '联系地址',
    `zip_code`               varchar(30)  DEFAULT NULL COMMENT '邮编',
    `email`                  varchar(30)  DEFAULT NULL COMMENT 'Email',
    `phone`                  varchar(30)  DEFAULT NULL COMMENT '手机号',
    `foreign_language`       varchar(30)  DEFAULT NULL COMMENT '外语语种',
    `foreign_language_level` varchar(30)  DEFAULT NULL COMMENT '外语水平',
    `computer_level`         varchar(30)  DEFAULT NULL COMMENT '计算机水平',
    `graduation_time`        datetime     DEFAULT NULL COMMENT '毕业时间',
    `arrival_time`           datetime     DEFAULT NULL COMMENT '到职时间',
    `positional_titles`      varchar(30)  DEFAULT NULL COMMENT '职称',
    `education_experience`   text COMMENT '教育经历',
    `work_experience`        text COMMENT '工作经历',
    `create_by`              varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`            datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`              varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`            datetime     DEFAULT NULL COMMENT '修改时间',
    `del_flag`               tinyint(2) DEFAULT NULL COMMENT '删除标识0-正常,1-已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 rep_demo_gongsi
# ------------------------------------------------------------

CREATE TABLE `rep_demo_gongsi`
(
    `id`      int(8) NOT NULL AUTO_INCREMENT,
    `gname`   varchar(125) NOT NULL COMMENT '货品名称',
    `gdata`   varchar(255) NOT NULL COMMENT '返利',
    `tdata`   varchar(125) NOT NULL COMMENT '备注',
    `didian`  varchar(255) NOT NULL,
    `zhaiyao` varchar(255) NOT NULL,
    `num`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 rep_demo_jianpiao
# ------------------------------------------------------------

CREATE TABLE `rep_demo_jianpiao`
(
    `id`       int(8) NOT NULL AUTO_INCREMENT,
    `bnum`     varchar(125) NOT NULL,
    `ftime`    varchar(125) NOT NULL,
    `sfkong`   varchar(125) NOT NULL,
    `kaishi`   varchar(125) NOT NULL,
    `jieshu`   varchar(125) NOT NULL,
    `hezairen` varchar(125) NOT NULL,
    `jpnum`    varchar(125) NOT NULL,
    `shihelv`  varchar(125) NOT NULL,
    `s_id`     int(8) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 sys_announcement
# ------------------------------------------------------------

CREATE TABLE `sys_announcement`
(
    `id`           varchar(32) NOT NULL,
    `title`        varchar(100)         DEFAULT NULL COMMENT '标题',
    `titile`       varchar(100)         DEFAULT NULL COMMENT '标题',
    `msg_content`  text COMMENT '内容',
    `start_time`   datetime             DEFAULT NULL COMMENT '开始时间',
    `end_time`     datetime             DEFAULT NULL COMMENT '结束时间',
    `sender`       varchar(100)         DEFAULT NULL COMMENT '发布人',
    `priority`     varchar(255)         DEFAULT NULL COMMENT '优先级（L低，M中，H高）',
    `msg_category` varchar(10) NOT NULL DEFAULT '2' COMMENT '消息类型1:通知公告2:系统消息',
    `msg_type`     varchar(10)          DEFAULT NULL COMMENT '通告对象类型（USER:指定用户，ALL:全体用户）',
    `send_status`  varchar(10)          DEFAULT NULL COMMENT '发布状态（0未发布，1已发布，2已撤销）',
    `send_time`    datetime             DEFAULT NULL COMMENT '发布时间',
    `cancel_time`  datetime             DEFAULT NULL COMMENT '撤销时间',
    `del_flag`     varchar(1)           DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
    `bus_type`     varchar(20)          DEFAULT NULL COMMENT '业务类型(email:邮件 bpm:流程)',
    `bus_id`       varchar(50)          DEFAULT NULL COMMENT '业务id',
    `open_type`    varchar(20)          DEFAULT NULL COMMENT '打开方式(组件：component 路由：url)',
    `open_page`    varchar(255)         DEFAULT NULL COMMENT '组件/路由 地址',
    `create_by`    varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime             DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime             DEFAULT NULL COMMENT '更新时间',
    `user_ids`     text COMMENT '指定用户',
    `msg_abstract` text COMMENT '摘要',
    `dt_task_id`   varchar(100)         DEFAULT NULL COMMENT '钉钉task_id，用于撤回消息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统通告表';



#
转储表 sys_announcement_send
# ------------------------------------------------------------

CREATE TABLE `sys_announcement_send`
(
    `id`          varchar(32) DEFAULT NULL,
    `annt_id`     varchar(32) DEFAULT NULL COMMENT '通告ID',
    `user_id`     varchar(32) DEFAULT NULL COMMENT '用户id',
    `read_flag`   varchar(10) DEFAULT NULL COMMENT '阅读状态（0未读，1已读）',
    `read_time`   datetime    DEFAULT NULL COMMENT '阅读时间',
    `create_by`   varchar(32) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime    DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户通告阅读标记表';



#
转储表 sys_category
# ------------------------------------------------------------

CREATE TABLE `sys_category`
(
    `id`           varchar(36) NOT NULL,
    `pid`          varchar(36)  DEFAULT NULL COMMENT '父级节点',
    `name`         varchar(100) DEFAULT NULL COMMENT '类型名称',
    `code`         varchar(100) DEFAULT NULL COMMENT '类型编码',
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    `has_child`    varchar(3)   DEFAULT NULL COMMENT '是否有子节点',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_code` (`code`) USING BTREE,
    KEY            `idx_sc_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_check_rule
# ------------------------------------------------------------

CREATE TABLE `sys_check_rule`
(
    `id`               varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
    `rule_name`        varchar(100)                   DEFAULT NULL COMMENT '规则名称',
    `rule_code`        varchar(100)                   DEFAULT NULL COMMENT '规则Code',
    `rule_json`        varchar(1024)                  DEFAULT NULL COMMENT '规则JSON',
    `rule_description` varchar(200)                   DEFAULT NULL COMMENT '规则描述',
    `update_by`        varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
    `update_time`      datetime                       DEFAULT NULL COMMENT '更新时间',
    `create_by`        varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime                       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uni_sys_check_rule_code` (`rule_code`) USING BTREE,
    UNIQUE KEY `uk_scr_rule_code` (`rule_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 sys_data_log
# ------------------------------------------------------------

CREATE TABLE `sys_data_log`
(
    `id`           varchar(32) NOT NULL COMMENT 'id',
    `create_by`    varchar(32) DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`  datetime    DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(32) DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`  datetime    DEFAULT NULL COMMENT '更新日期',
    `data_table`   varchar(32) DEFAULT NULL COMMENT '表名',
    `data_id`      varchar(32) DEFAULT NULL COMMENT '数据ID',
    `data_content` text COMMENT '数据内容',
    `data_version` int(8) DEFAULT NULL COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY            `sindex` (`data_table`,`data_id`) USING BTREE,
    KEY            `idx_sdl_data_table_id` (`data_table`,`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_data_source
# ------------------------------------------------------------

CREATE TABLE `sys_data_source`
(
    `id`           varchar(36) NOT NULL,
    `code`         varchar(100) DEFAULT NULL COMMENT '数据源编码',
    `name`         varchar(100) DEFAULT NULL COMMENT '数据源名称',
    `remark`       varchar(200) DEFAULT NULL COMMENT '备注',
    `db_type`      varchar(10)  DEFAULT NULL COMMENT '数据库类型',
    `db_driver`    varchar(100) DEFAULT NULL COMMENT '驱动类',
    `db_url`       varchar(500) DEFAULT NULL COMMENT '数据源地址',
    `db_name`      varchar(100) DEFAULT NULL COMMENT '数据库名称',
    `db_username`  varchar(100) DEFAULT NULL COMMENT '用户名',
    `db_password`  varchar(100) DEFAULT NULL COMMENT '密码',
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `sys_data_source_code_uni` (`code`) USING BTREE,
    UNIQUE KEY `uk_sdc_rule_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 sys_depart
# ------------------------------------------------------------

CREATE TABLE `sys_depart`
(
    `id`               varchar(32)  NOT NULL COMMENT 'ID',
    `parent_id`        varchar(32)           DEFAULT NULL COMMENT '父机构ID',
    `depart_name`      varchar(100) NOT NULL COMMENT '机构/部门名称',
    `depart_name_en`   varchar(500)          DEFAULT NULL COMMENT '英文名',
    `depart_name_abbr` varchar(500)          DEFAULT NULL COMMENT '缩写',
    `depart_order`     int(8) DEFAULT '0' COMMENT '排序',
    `description`      text COMMENT '描述',
    `org_category`     varchar(10)  NOT NULL DEFAULT '1' COMMENT '机构类别 1组织机构，2岗位',
    `org_type`         varchar(10)           DEFAULT NULL COMMENT '机构类型 1一级部门 2子部门',
    `org_code`         varchar(64)  NOT NULL COMMENT '机构编码',
    `mobile`           varchar(32)           DEFAULT NULL COMMENT '手机号',
    `fax`              varchar(32)           DEFAULT NULL COMMENT '传真',
    `address`          varchar(100)          DEFAULT NULL COMMENT '地址',
    `memo`             varchar(500)          DEFAULT NULL COMMENT '备注',
    `status`           varchar(1)            DEFAULT NULL COMMENT '状态（1启用，0不启用）',
    `del_flag`         varchar(1)            DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
    `qywx_identifier`  varchar(100)          DEFAULT NULL COMMENT '对接企业微信的ID',
    `create_by`        varchar(32)           DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime              DEFAULT NULL COMMENT '创建日期',
    `update_by`        varchar(32)           DEFAULT NULL COMMENT '更新人',
    `update_time`      datetime              DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_depart_org_code` (`org_code`) USING BTREE,
    KEY                `index_depart_parent_id` (`parent_id`) USING BTREE,
    KEY                `index_depart_depart_order` (`depart_order`) USING BTREE,
    KEY                `index_depart_org_code` (`org_code`) USING BTREE,
    KEY                `idx_sd_parent_id` (`parent_id`) USING BTREE,
    KEY                `idx_sd_depart_order` (`depart_order`) USING BTREE,
    KEY                `idx_sd_org_code` (`org_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构表';



#
转储表 sys_depart_permission
# ------------------------------------------------------------

CREATE TABLE `sys_depart_permission`
(
    `id`            varchar(32) NOT NULL,
    `depart_id`     varchar(32)   DEFAULT NULL COMMENT '部门id',
    `permission_id` varchar(32)   DEFAULT NULL COMMENT '权限id',
    `data_rule_ids` varchar(1000) DEFAULT NULL COMMENT '数据规则id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门权限表';



#
转储表 sys_depart_role
# ------------------------------------------------------------

CREATE TABLE `sys_depart_role`
(
    `id`          varchar(32) NOT NULL,
    `depart_id`   varchar(32)  DEFAULT NULL COMMENT '部门id',
    `role_name`   varchar(200) DEFAULT NULL COMMENT '部门角色名称',
    `role_code`   varchar(100) DEFAULT NULL COMMENT '部门角色编码',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门角色表';



#
转储表 sys_depart_role_permission
# ------------------------------------------------------------

CREATE TABLE `sys_depart_role_permission`
(
    `id`            varchar(32) NOT NULL,
    `depart_id`     varchar(32)   DEFAULT NULL COMMENT '部门id',
    `role_id`       varchar(32)   DEFAULT NULL COMMENT '角色id',
    `permission_id` varchar(32)   DEFAULT NULL COMMENT '权限id',
    `data_rule_ids` varchar(1000) DEFAULT NULL COMMENT '数据权限ids',
    `operate_date`  datetime      DEFAULT NULL COMMENT '操作时间',
    `operate_ip`    varchar(20)   DEFAULT NULL COMMENT '操作ip',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `index_group_role_per_id` (`role_id`,`permission_id`) USING BTREE,
    KEY             `index_group_role_id` (`role_id`) USING BTREE,
    KEY             `index_group_per_id` (`permission_id`) USING BTREE,
    KEY             `idx_sdrp_role_per_id` (`role_id`,`permission_id`) USING BTREE,
    KEY             `idx_sdrp_role_id` (`role_id`) USING BTREE,
    KEY             `idx_sdrp_per_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部门角色权限表';



#
转储表 sys_depart_role_user
# ------------------------------------------------------------

CREATE TABLE `sys_depart_role_user`
(
    `id`       varchar(32) NOT NULL COMMENT '主键id',
    `user_id`  varchar(32) DEFAULT NULL COMMENT '用户id',
    `drole_id` varchar(32) DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门角色用户表';



#
转储表 sys_dict
# ------------------------------------------------------------

CREATE TABLE `sys_dict`
(
    `id`          varchar(32) NOT NULL,
    `dict_name`   varchar(100) DEFAULT NULL COMMENT '字典名称',
    `dict_code`   varchar(100) DEFAULT NULL COMMENT '字典编码',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `del_flag`    tinyint(2) DEFAULT NULL COMMENT '删除状态',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    `type`        tinyint(2) unsigned zerofill DEFAULT '00' COMMENT '字典类型0为string,1为number',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `indextable_dict_code` (`dict_code`) USING BTREE,
    UNIQUE KEY `uk_sd_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_dict_item
# ------------------------------------------------------------

CREATE TABLE `sys_dict_item`
(
    `id`          varchar(32) NOT NULL,
    `dict_id`     varchar(32)  DEFAULT NULL COMMENT '字典id',
    `item_text`   varchar(100) DEFAULT NULL COMMENT '字典项文本',
    `item_value`  varchar(100) DEFAULT NULL COMMENT '字典项值',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `sort_order`  int(8) DEFAULT NULL COMMENT '排序',
    `status`      tinyint(2) DEFAULT NULL COMMENT '状态（1启用 0不启用）',
    `create_by`   varchar(32)  DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_by`   varchar(32)  DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY           `index_table_dict_id` (`dict_id`) USING BTREE,
    KEY           `index_table_sort_order` (`sort_order`) USING BTREE,
    KEY           `index_table_dict_status` (`status`) USING BTREE,
    KEY           `idx_sdi_role_dict_id` (`dict_id`) USING BTREE,
    KEY           `idx_sdi_role_sort_order` (`sort_order`) USING BTREE,
    KEY           `idx_sdi_status` (`status`) USING BTREE,
    KEY           `idx_sdi_dict_val` (`dict_id`,`item_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_fill_rule
# ------------------------------------------------------------

CREATE TABLE `sys_fill_rule`
(
    `id`          varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '主键ID',
    `rule_name`   varchar(100)                   DEFAULT NULL COMMENT '规则名称',
    `rule_code`   varchar(100)                   DEFAULT NULL COMMENT '规则Code',
    `rule_class`  varchar(100)                   DEFAULT NULL COMMENT '规则实现类',
    `rule_params` varchar(200)                   DEFAULT NULL COMMENT '规则参数',
    `update_by`   varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                       DEFAULT NULL COMMENT '修改时间',
    `create_by`   varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uni_sys_fill_rule_code` (`rule_code`) USING BTREE,
    UNIQUE KEY `uk_sfr_rule_code` (`rule_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



#
转储表 sys_gateway_route
# ------------------------------------------------------------

CREATE TABLE `sys_gateway_route`
(
    `id`           varchar(36) NOT NULL,
    `router_id`    varchar(50) DEFAULT NULL COMMENT '路由ID',
    `name`         varchar(32) DEFAULT NULL COMMENT '服务名',
    `uri`          varchar(32) DEFAULT NULL COMMENT '服务地址',
    `predicates`   text COMMENT '断言',
    `filters`      text COMMENT '过滤器',
    `retryable`    tinyint(2) DEFAULT NULL COMMENT '是否重试:0-否 1-是',
    `strip_prefix` tinyint(2) DEFAULT NULL COMMENT '是否忽略前缀0-否 1-是',
    `persistable`  tinyint(2) DEFAULT NULL COMMENT '是否为保留数据:0-否 1-是',
    `show_api`     tinyint(2) DEFAULT NULL COMMENT '是否在接口文档中展示:0-否 1-是',
    `status`       tinyint(2) DEFAULT NULL COMMENT '状态:0-无效 1-有效',
    `create_by`    varchar(50) DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime    DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50) DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime    DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;



#
转储表 sys_log
# ------------------------------------------------------------

CREATE TABLE `sys_log`
(
    `id`            varchar(32) NOT NULL,
    `log_type`      tinyint(2) DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志）',
    `log_content`   varchar(1000) DEFAULT NULL COMMENT '日志内容',
    `operate_type`  tinyint(2) DEFAULT NULL COMMENT '操作类型',
    `userid`        varchar(32)   DEFAULT NULL COMMENT '操作用户账号',
    `username`      varchar(100)  DEFAULT NULL COMMENT '操作用户名称',
    `ip`            varchar(100)  DEFAULT NULL COMMENT 'IP',
    `method`        varchar(500)  DEFAULT NULL COMMENT '请求java方法',
    `request_url`   varchar(255)  DEFAULT NULL COMMENT '请求路径',
    `request_param` longtext COMMENT '请求参数',
    `request_type`  varchar(10)   DEFAULT NULL COMMENT '请求类型',
    `cost_time`     bigint(11) DEFAULT NULL COMMENT '耗时',
    `create_by`     varchar(32)   DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(32)   DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `index_table_userid` (`userid`) USING BTREE,
    KEY             `index_logt_ype` (`log_type`) USING BTREE,
    KEY             `index_operate_type` (`operate_type`) USING BTREE,
    KEY             `index_log_type` (`log_type`) USING BTREE,
    KEY             `idx_sl_log_type` (`log_type`) USING BTREE,
    KEY             `idx_sl_userid` (`userid`) USING BTREE,
    KEY             `idx_sl_operate_type` (`operate_type`) USING BTREE,
    KEY             `idx_sl_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';



#
转储表 sys_permission
# ------------------------------------------------------------

CREATE TABLE `sys_permission`
(
    `id`             varchar(32) NOT NULL COMMENT '主键id',
    `parent_id`      varchar(32)  DEFAULT NULL COMMENT '父id',
    `name`           varchar(100) DEFAULT NULL COMMENT '菜单标题',
    `url`            varchar(255) DEFAULT NULL COMMENT '路径',
    `component`      varchar(255) DEFAULT NULL COMMENT '组件',
    `component_name` varchar(100) DEFAULT NULL COMMENT '组件名字',
    `redirect`       varchar(255) DEFAULT NULL COMMENT '一级菜单跳转地址',
    `menu_type`      int(8) DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
    `perms`          varchar(255) DEFAULT NULL COMMENT '菜单权限编码',
    `perms_type`     varchar(10)  DEFAULT '0' COMMENT '权限策略1显示2禁用',
    `sort_no`        double(8, 2
) DEFAULT NULL COMMENT '菜单排序',
  `always_show` tinyint(2) DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `is_route` tinyint(2) DEFAULT '1' COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `is_leaf` tinyint(2) DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `keep_alive` tinyint(2) DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `hidden` tinyint(2) DEFAULT '0' COMMENT '是否隐藏路由: 0否,1是',
  `hide_tab` tinyint(2) DEFAULT NULL COMMENT '是否隐藏tab: 0否,1是',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除状态 0正常 1已删除',
  `rule_flag` tinyint(2) DEFAULT '0' COMMENT '是否添加数据权限1是0否',
  `status` varchar(2) DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
  `internal_or_external` tinyint(2) DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_prem_pid` (`parent_id`) USING BTREE,
  KEY `index_prem_is_route` (`is_route`) USING BTREE,
  KEY `index_prem_is_leaf` (`is_leaf`) USING BTREE,
  KEY `index_prem_sort_no` (`sort_no`) USING BTREE,
  KEY `index_prem_del_flag` (`del_flag`) USING BTREE,
  KEY `index_menu_type` (`menu_type`) USING BTREE,
  KEY `index_menu_hidden` (`hidden`) USING BTREE,
  KEY `index_menu_status` (`status`) USING BTREE,
  KEY `idx_sp_is_leaf` (`is_leaf`) USING BTREE,
  KEY `idx_sp_status` (`status`) USING BTREE,
  KEY `idx_sp_sort_no` (`sort_no`) USING BTREE,
  KEY `idx_sp_menu_type` (`menu_type`) USING BTREE,
  KEY `idx_sp_del_flag` (`del_flag`) USING BTREE,
  KEY `idx_sp_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_sp_is_route` (`is_route`) USING BTREE,
  KEY `idx_sp_hidden` (`hidden`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限表';



#
转储表 sys_permission_data_rule
# ------------------------------------------------------------

CREATE TABLE `sys_permission_data_rule`
(
    `id`              varchar(32) NOT NULL COMMENT 'ID',
    `permission_id`   varchar(32)  DEFAULT NULL COMMENT '菜单ID',
    `rule_name`       varchar(50)  DEFAULT NULL COMMENT '规则名称',
    `rule_column`     varchar(50)  DEFAULT NULL COMMENT '字段',
    `rule_conditions` varchar(50)  DEFAULT NULL COMMENT '条件',
    `rule_value`      varchar(300) DEFAULT NULL COMMENT '规则值',
    `status`          varchar(3)   DEFAULT NULL COMMENT '权限有效状态1有0否',
    `create_time`     datetime     DEFAULT NULL COMMENT '创建时间',
    `create_by`       varchar(32)  DEFAULT NULL,
    `update_time`     datetime     DEFAULT NULL COMMENT '修改时间',
    `update_by`       varchar(32)  DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `index_fucntionid` (`permission_id`) USING BTREE,
    KEY               `idx_spdr_permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_permission_v3
# ------------------------------------------------------------

CREATE TABLE `sys_permission_v3`
(
    `id`             varchar(32) NOT NULL COMMENT '主键id',
    `parent_id`      varchar(32)  DEFAULT NULL COMMENT '父id',
    `name`           varchar(255) DEFAULT NULL COMMENT '菜单标题',
    `url`            varchar(255) DEFAULT NULL COMMENT '路径',
    `component`      varchar(255) DEFAULT NULL COMMENT '组件',
    `is_route`       tinyint(2) DEFAULT '1' COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
    `component_name` varchar(255) DEFAULT NULL COMMENT '组件名字',
    `redirect`       varchar(255) DEFAULT NULL COMMENT '一级菜单跳转地址',
    `menu_type`      tinyint(2) DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
    `perms`          varchar(255) DEFAULT NULL COMMENT '菜单权限编码',
    `perms_type`     varchar(10)  DEFAULT '0' COMMENT '权限策略1显示2禁用',
    `sort_no`        double(8, 2
) DEFAULT NULL COMMENT '菜单排序',
  `always_show` tinyint(2) DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `is_leaf` tinyint(2) DEFAULT NULL COMMENT '是否叶子节点:    1是0否',
  `keep_alive` tinyint(2) DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `hidden` tinyint(2) DEFAULT '0' COMMENT '是否隐藏路由: 0否,1是',
  `hide_tab` tinyint(2) DEFAULT NULL COMMENT '是否隐藏tab: 0否,1是',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除状态 0正常 1已删除',
  `rule_flag` tinyint(2) DEFAULT '0' COMMENT '是否添加数据权限1是0否',
  `status` varchar(2) DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
  `internal_or_external` tinyint(2) DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_menu_type` (`menu_type`) USING BTREE,
  KEY `index_menu_hidden` (`hidden`) USING BTREE,
  KEY `index_menu_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';



#
转储表 sys_position
# ------------------------------------------------------------

CREATE TABLE `sys_position`
(
    `id`           varchar(32) NOT NULL,
    `code`         varchar(100) DEFAULT NULL COMMENT '职务编码',
    `name`         varchar(100) DEFAULT NULL COMMENT '职务名称',
    `post_rank`    varchar(2)   DEFAULT NULL COMMENT '职级',
    `company_id`   varchar(255) DEFAULT NULL COMMENT '公司id',
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '修改人',
    `update_time`  datetime     DEFAULT NULL COMMENT '修改时间',
    `sys_org_code` varchar(50)  DEFAULT NULL COMMENT '组织机构编码',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_quartz_job
# ------------------------------------------------------------

CREATE TABLE `sys_quartz_job`
(
    `id`              varchar(32) NOT NULL,
    `create_by`       varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`     datetime     DEFAULT NULL COMMENT '创建时间',
    `del_flag`        tinyint(2) DEFAULT NULL COMMENT '删除状态',
    `update_by`       varchar(32)  DEFAULT NULL COMMENT '修改人',
    `update_time`     datetime     DEFAULT NULL COMMENT '修改时间',
    `job_class_name`  varchar(255) DEFAULT NULL COMMENT '任务类名',
    `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
    `parameter`       varchar(255) DEFAULT NULL COMMENT '参数',
    `description`     varchar(255) DEFAULT NULL COMMENT '描述',
    `status`          tinyint(2) DEFAULT NULL COMMENT '状态 0正常 -1停止',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_role
# ------------------------------------------------------------

CREATE TABLE `sys_role`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键id',
    `role_name`   varchar(200) DEFAULT NULL COMMENT '角色名称',
    `role_code`   varchar(100) NOT NULL COMMENT '角色编码',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `create_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_sys_role_role_code` (`role_code`) USING BTREE,
    KEY           `idx_sr_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';



#
转储表 sys_role_index
# ------------------------------------------------------------

CREATE TABLE `sys_role_index`
(
    `id`           varchar(32) NOT NULL,
    `role_code`    varchar(50)                     DEFAULT NULL COMMENT '角色编码',
    `url`          varchar(100)                    DEFAULT NULL COMMENT '路由地址',
    `component`    varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '组件',
    `is_route`     tinyint(2) DEFAULT '1' COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
    `priority`     int(8) DEFAULT '0' COMMENT '优先级',
    `status`       varchar(2) CHARACTER SET utf8   DEFAULT '0' COMMENT '状态0:无效 1:有效',
    `create_by`    varchar(50) CHARACTER SET utf8  DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`  datetime                        DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50) CHARACTER SET utf8  DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`  datetime                        DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64) CHARACTER SET utf8  DEFAULT NULL COMMENT '所属部门',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色首页表';



#
转储表 sys_role_permission
# ------------------------------------------------------------

CREATE TABLE `sys_role_permission`
(
    `id`            varchar(32) NOT NULL,
    `role_id`       varchar(32)   DEFAULT NULL COMMENT '角色id',
    `permission_id` varchar(32)   DEFAULT NULL COMMENT '权限id',
    `data_rule_ids` varchar(1000) DEFAULT NULL,
    `operate_date`  datetime      DEFAULT NULL COMMENT '操作时间',
    `operate_ip`    varchar(100)  DEFAULT NULL COMMENT '操作ip',
    PRIMARY KEY (`id`) USING BTREE,
    KEY             `index_group_role_per_id` (`role_id`,`permission_id`) USING BTREE,
    KEY             `index_group_role_id` (`role_id`) USING BTREE,
    KEY             `index_group_per_id` (`permission_id`) USING BTREE,
    KEY             `idx_srp_role_per_id` (`role_id`,`permission_id`) USING BTREE,
    KEY             `idx_srp_role_id` (`role_id`) USING BTREE,
    KEY             `idx_srp_permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';



#
转储表 sys_sms
# ------------------------------------------------------------

CREATE TABLE `sys_sms`
(
    `id`             varchar(32) NOT NULL COMMENT 'ID',
    `es_title`       varchar(100)  DEFAULT NULL COMMENT '消息标题',
    `es_type`        varchar(1)    DEFAULT NULL COMMENT '发送方式：1短信 2邮件 3微信',
    `es_receiver`    varchar(50)   DEFAULT NULL COMMENT '接收人',
    `es_param`       varchar(1000) DEFAULT NULL COMMENT '发送所需参数Json格式',
    `es_content`     longtext COMMENT '推送内容',
    `es_send_time`   datetime      DEFAULT NULL COMMENT '推送时间',
    `es_send_status` varchar(1)    DEFAULT NULL COMMENT '推送状态 0未推送 1推送成功 2推送失败 -1失败不再发送',
    `es_send_num`    int(8) DEFAULT NULL COMMENT '发送次数 超过5次不再发送',
    `es_result`      varchar(255)  DEFAULT NULL COMMENT '推送失败原因',
    `remark`         varchar(500)  DEFAULT NULL COMMENT '备注',
    `create_by`      varchar(32)   DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`    datetime      DEFAULT NULL COMMENT '创建日期',
    `update_by`      varchar(32)   DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`    datetime      DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `index_type` (`es_type`) USING BTREE,
    KEY              `index_receiver` (`es_receiver`) USING BTREE,
    KEY              `index_sendtime` (`es_send_time`) USING BTREE,
    KEY              `index_status` (`es_send_status`) USING BTREE,
    KEY              `idx_ss_es_send_status` (`es_send_status`) USING BTREE,
    KEY              `idx_ss_es_receiver` (`es_receiver`) USING BTREE,
    KEY              `idx_ss_es_send_time` (`es_send_time`) USING BTREE,
    KEY              `idx_ss_es_type` (`es_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_sms_template
# ------------------------------------------------------------

CREATE TABLE `sys_sms_template`
(
    `id`                 varchar(32)   NOT NULL COMMENT '主键',
    `template_name`      varchar(50)   DEFAULT NULL COMMENT '模板标题',
    `template_code`      varchar(32)   NOT NULL COMMENT '模板CODE',
    `template_type`      varchar(1)    NOT NULL COMMENT '模板类型：1短信 2邮件 3微信',
    `template_content`   varchar(1000) NOT NULL COMMENT '模板内容',
    `template_test_json` varchar(1000) DEFAULT NULL COMMENT '模板测试json',
    `create_time`        datetime      DEFAULT NULL COMMENT '创建日期',
    `create_by`          varchar(32)   DEFAULT NULL COMMENT '创建人登录名称',
    `update_time`        datetime      DEFAULT NULL COMMENT '更新日期',
    `update_by`          varchar(32)   DEFAULT NULL COMMENT '更新人登录名称',
    `use_status`         varchar(1)    DEFAULT NULL COMMENT '是否使用中 1是0否',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_templatecode` (`template_code`) USING BTREE,
    UNIQUE KEY `uk_sst_template_code` (`template_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_tenant
# ------------------------------------------------------------

CREATE TABLE `sys_tenant`
(
    `id`          int(8) NOT NULL COMMENT '租户编码',
    `name`        varchar(100) DEFAULT NULL COMMENT '租户名称',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(100) DEFAULT NULL COMMENT '创建人',
    `begin_date`  datetime     DEFAULT NULL COMMENT '开始时间',
    `end_date`    datetime     DEFAULT NULL COMMENT '结束时间',
    `status`      tinyint(2) DEFAULT NULL COMMENT '状态 1正常 0冻结',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='多租户信息表';



#
转储表 sys_third_account
# ------------------------------------------------------------

CREATE TABLE `sys_third_account`
(
    `id`              varchar(32) NOT NULL COMMENT '编号',
    `sys_user_id`     varchar(32)                    DEFAULT NULL COMMENT '第三方登录id',
    `avatar`          varchar(255)                   DEFAULT NULL COMMENT '头像',
    `status`          tinyint(2) DEFAULT NULL COMMENT '状态(1-正常,2-冻结)',
    `del_flag`        tinyint(2) DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
    `realname`        varchar(100)                   DEFAULT NULL COMMENT '真实姓名',
    `third_user_uuid` varchar(100)                   DEFAULT NULL COMMENT '第三方账号',
    `third_user_id`   varchar(100)                   DEFAULT NULL COMMENT '第三方app用户账号',
    `create_by`       varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`     datetime                       DEFAULT NULL COMMENT '创建日期',
    `update_by`       varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`     datetime                       DEFAULT NULL COMMENT '更新日期',
    `third_type`      varchar(50)                    DEFAULT NULL COMMENT '登录来源',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_sys_third_account_third_type_third_user_id` (`third_type`,`third_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 sys_user
# ------------------------------------------------------------

CREATE TABLE `sys_user`
(
    `id`             varchar(32) NOT NULL COMMENT '主键id',
    `username`       varchar(100) DEFAULT NULL COMMENT '登录账号',
    `realname`       varchar(100) DEFAULT NULL COMMENT '真实姓名',
    `password`       varchar(255) DEFAULT NULL COMMENT '密码',
    `salt`           varchar(45)  DEFAULT NULL COMMENT 'md5密码盐',
    `avatar`         varchar(255) DEFAULT NULL COMMENT '头像',
    `birthday`       datetime     DEFAULT NULL COMMENT '生日',
    `sex`            tinyint(2) DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
    `email`          varchar(45)  DEFAULT NULL COMMENT '电子邮件',
    `phone`          varchar(45)  DEFAULT NULL COMMENT '电话',
    `org_code`       varchar(64)  DEFAULT NULL COMMENT '机构编码',
    `status`         tinyint(2) DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
    `del_flag`       tinyint(2) DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
    `third_id`       varchar(100) DEFAULT NULL COMMENT '第三方登录的唯一标识',
    `third_type`     varchar(100) DEFAULT NULL COMMENT '第三方类型',
    `activiti_sync`  tinyint(2) DEFAULT NULL COMMENT '同步工作流引擎(1-同步,0-不同步)',
    `work_no`        varchar(100) DEFAULT NULL COMMENT '工号，唯一键',
    `post`           varchar(100) DEFAULT NULL COMMENT '职务，关联职务表',
    `telephone`      varchar(45)  DEFAULT NULL COMMENT '座机号',
    `create_by`      varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
    `user_identity`  tinyint(2) DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
    `identity`       tinyint(2) DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
    `depart_ids`     longtext COMMENT '负责部门',
    `rel_tenant_ids` varchar(100) DEFAULT NULL COMMENT '多租户标识',
    `client_id`      varchar(64)  DEFAULT NULL COMMENT '设备ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `index_user_name` (`username`) USING BTREE,
    UNIQUE KEY `uniq_sys_user_work_no` (`work_no`) USING BTREE,
    UNIQUE KEY `uniq_sys_user_username` (`username`) USING BTREE,
    UNIQUE KEY `uniq_sys_user_phone` (`phone`) USING BTREE,
    UNIQUE KEY `uniq_sys_user_email` (`email`) USING BTREE,
    KEY              `index_user_status` (`status`) USING BTREE,
    KEY              `index_user_del_flag` (`del_flag`) USING BTREE,
    KEY              `idx_su_status` (`status`) USING BTREE,
    KEY              `idx_su_del_flag` (`del_flag`) USING BTREE,
    KEY              `idx_su_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';



#
转储表 sys_user_agent
# ------------------------------------------------------------

CREATE TABLE `sys_user_agent`
(
    `id`               varchar(32) NOT NULL COMMENT '序号',
    `user_name`        varchar(100) DEFAULT NULL COMMENT '用户名',
    `agent_user_name`  varchar(100) DEFAULT NULL COMMENT '代理人用户名',
    `start_time`       datetime     DEFAULT NULL COMMENT '代理开始时间',
    `end_time`         datetime     DEFAULT NULL COMMENT '代理结束时间',
    `status`           varchar(2)   DEFAULT NULL COMMENT '状态0无效1有效',
    `create_name`      varchar(50)  DEFAULT NULL COMMENT '创建人名称',
    `create_by`        varchar(50)  DEFAULT NULL COMMENT '创建人登录名称',
    `create_time`      datetime     DEFAULT NULL COMMENT '创建日期',
    `update_name`      varchar(50)  DEFAULT NULL COMMENT '更新人名称',
    `update_by`        varchar(50)  DEFAULT NULL COMMENT '更新人登录名称',
    `update_time`      datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code`     varchar(50)  DEFAULT NULL COMMENT '所属部门',
    `sys_company_code` varchar(50)  DEFAULT NULL COMMENT '所属公司',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_username` (`user_name`) USING BTREE,
    UNIQUE KEY `uk_sug_user_name` (`user_name`) USING BTREE,
    KEY                `statux_index` (`status`) USING BTREE,
    KEY                `begintime_index` (`start_time`) USING BTREE,
    KEY                `endtime_index` (`end_time`) USING BTREE,
    KEY                `idx_sug_start_time` (`start_time`) USING BTREE,
    KEY                `idx_sug_status` (`status`) USING BTREE,
    KEY                `idx_sug_end_time` (`end_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户代理人设置';



#
转储表 sys_user_depart
# ------------------------------------------------------------

CREATE TABLE `sys_user_depart`
(
    `ID`      varchar(32) NOT NULL COMMENT 'id',
    `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
    `dep_id`  varchar(32) DEFAULT NULL COMMENT '部门id',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY       `index_depart_groupk_userid` (`user_id`) USING BTREE,
    KEY       `index_depart_groupkorgid` (`dep_id`) USING BTREE,
    KEY       `index_depart_groupk_uidanddid` (`user_id`,`dep_id`) USING BTREE,
    KEY       `idx_sud_user_id` (`user_id`) USING BTREE,
    KEY       `idx_sud_dep_id` (`dep_id`) USING BTREE,
    KEY       `idx_sud_user_dep_id` (`user_id`,`dep_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 sys_user_role
# ------------------------------------------------------------

CREATE TABLE `sys_user_role`
(
    `id`      varchar(32) NOT NULL COMMENT '主键id',
    `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
    `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY       `index2_groupuu_user_id` (`user_id`) USING BTREE,
    KEY       `index2_groupuu_ole_id` (`role_id`) USING BTREE,
    KEY       `index2_groupuu_useridandroleid` (`user_id`,`role_id`) USING BTREE,
    KEY       `idx_sur_user_id` (`user_id`) USING BTREE,
    KEY       `idx_sur_role_id` (`role_id`) USING BTREE,
    KEY       `idx_sur_user_role_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';



#
转储表 test_demo
# ------------------------------------------------------------

CREATE TABLE `test_demo`
(
    `id`          varchar(36) NOT NULL COMMENT '主键',
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人登录名称',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`   varchar(50)  DEFAULT NULL COMMENT '更新人登录名称',
    `update_time` datetime     DEFAULT NULL COMMENT '更新日期',
    `name`        varchar(200) DEFAULT NULL COMMENT '用户名',
    `sex`         varchar(32)  DEFAULT NULL COMMENT '性别',
    `age`         int(8) DEFAULT NULL COMMENT '年龄',
    `descc`       varchar(500) DEFAULT NULL COMMENT '描述',
    `birthday`    datetime     DEFAULT NULL COMMENT '生日',
    `user_code`   varchar(32)  DEFAULT NULL COMMENT '用户编码',
    `file_kk`     varchar(500) DEFAULT NULL COMMENT '附件',
    `top_pic`     varchar(500) DEFAULT NULL COMMENT '头像',
    `chegnshi`    varchar(300) DEFAULT NULL COMMENT '城市',
    `ceck`        varchar(32)  DEFAULT NULL COMMENT 'checkbox',
    `xiamuti`     varchar(100) DEFAULT NULL COMMENT '下拉多选',
    `search_sel`  varchar(100) DEFAULT NULL COMMENT '搜索下拉',
    `pop`         varchar(32)  DEFAULT NULL COMMENT '弹窗',
    `sel_table`   varchar(32)  DEFAULT NULL COMMENT '下拉字典表',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 test_enhance_select
# ------------------------------------------------------------

CREATE TABLE `test_enhance_select`
(
    `id`          varchar(36) NOT NULL,
    `province`    varchar(100) DEFAULT NULL COMMENT '省份',
    `city`        varchar(100) DEFAULT NULL COMMENT '市',
    `area`        varchar(100) DEFAULT NULL COMMENT '区',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 test_note
# ------------------------------------------------------------

CREATE TABLE `test_note`
(
    `id`           varchar(36) NOT NULL COMMENT '主键',
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    `name`         varchar(32)  DEFAULT NULL COMMENT '用户名',
    `age`          int(8) DEFAULT NULL COMMENT '年龄',
    `sex`          varchar(32)  DEFAULT NULL COMMENT '性别',
    `birthday`     datetime     DEFAULT NULL COMMENT '生日',
    `contents`     varchar(500) DEFAULT NULL COMMENT '请假原因',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 test_online_link
# ------------------------------------------------------------

CREATE TABLE `test_online_link`
(
    `id`   varchar(32) NOT NULL,
    `pid`  varchar(32)  DEFAULT NULL COMMENT 'pid',
    `name` varchar(255) DEFAULT NULL COMMENT 'name',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 test_order_main
# ------------------------------------------------------------

CREATE TABLE `test_order_main`
(
    `id`          varchar(36) NOT NULL,
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`   varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     DEFAULT NULL COMMENT '更新日期',
    `order_code`  varchar(32)  DEFAULT NULL COMMENT '订单编码',
    `order_date`  datetime     DEFAULT NULL COMMENT '下单时间',
    `descc`       varchar(100) DEFAULT NULL COMMENT '描述',
    `xiala`       varchar(32)  DEFAULT NULL COMMENT '下拉多选',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 test_order_product
# ------------------------------------------------------------

CREATE TABLE `test_order_product`
(
    `id`           varchar(36) NOT NULL COMMENT '主键',
    `create_by`    varchar(50) DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime    DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50) DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime    DEFAULT NULL COMMENT '更新日期',
    `product_name` varchar(32) DEFAULT NULL COMMENT '产品名字',
    `price`        double(32, 0
) DEFAULT NULL COMMENT '价格',
  `num` int(8) DEFAULT NULL COMMENT '数量',
  `descc` varchar(32) DEFAULT NULL COMMENT '描述',
  `order_fk_id` varchar(32) NOT NULL COMMENT '订单外键ID',
  `pro_type` varchar(32) DEFAULT NULL COMMENT '产品类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 test_person
# ------------------------------------------------------------

CREATE TABLE `test_person`
(
    `id`          varchar(36) NOT NULL,
    `create_by`   varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`   varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     DEFAULT NULL COMMENT '更新日期',
    `sex`         varchar(32)  DEFAULT NULL COMMENT '性别',
    `name`        varchar(200) DEFAULT NULL COMMENT '用户名',
    `content`     longtext COMMENT '请假原因',
    `be_date`     datetime     DEFAULT NULL COMMENT '请假时间',
    `qj_days`     int(8) DEFAULT NULL COMMENT '请假天数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#
转储表 test_shoptype_tree
# ------------------------------------------------------------

CREATE TABLE `test_shoptype_tree`
(
    `id`           varchar(36) NOT NULL COMMENT '主键',
    `create_by`    varchar(50)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)  DEFAULT NULL COMMENT '所属部门',
    `type_name`    varchar(32)  DEFAULT NULL COMMENT '商品分类',
    `pic`          varchar(500) DEFAULT NULL COMMENT '分类图片',
    `pid`          varchar(32)  DEFAULT NULL COMMENT '父级节点',
    `has_child`    varchar(3)   DEFAULT NULL COMMENT '是否有子节点',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 tmp_report_data_1
# ------------------------------------------------------------

CREATE TABLE `tmp_report_data_1`
(
    `monty`       varchar(255)   DEFAULT NULL COMMENT '月份',
    `main_income` decimal(10, 2) DEFAULT NULL,
    `total`       decimal(10, 2) DEFAULT NULL,
    `his_lowest`  decimal(10, 2) DEFAULT NULL,
    `his_average` decimal(10, 2) DEFAULT NULL,
    `his_highest` decimal(10, 2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 tmp_report_data_income
# ------------------------------------------------------------

CREATE TABLE `tmp_report_data_income`
(
    `biz_income`        varchar(100)   DEFAULT NULL,
    `bx_jj_yongjin`     decimal(10, 2) DEFAULT NULL,
    `bx_zx_money`       decimal(10, 2) DEFAULT NULL,
    `chengbao_gz_money` decimal(10, 2) DEFAULT NULL,
    `bx_gg_moeny`       decimal(10, 2) DEFAULT NULL,
    `tb_zx_money`       decimal(10, 2) DEFAULT NULL,
    `neikong_zx_money`  decimal(10, 2) DEFAULT NULL,
    `total`             decimal(10, 2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;



#
转储表 v3_demo1
# ------------------------------------------------------------

CREATE TABLE `v3_demo1`
(
    `id`           varchar(36) NOT NULL,
    `create_by`    varchar(50) DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime    DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50) DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime    DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
    `name`         varchar(32) DEFAULT NULL COMMENT '名字',
    `sex`          varchar(32) DEFAULT NULL COMMENT '性别',
    `age`          int(8) DEFAULT NULL COMMENT '年龄',
    `birthday`     date        DEFAULT NULL COMMENT '生日',
    `salary`       double(10, 2
) DEFAULT NULL COMMENT '工资',
  `cccc` varchar(1000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



#
转储表 v3_hello
# ------------------------------------------------------------

CREATE TABLE `v3_hello`
(
    `id`           varchar(36) NOT NULL,
    `create_by`    varchar(50)   DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime      DEFAULT NULL COMMENT '创建日期',
    `update_by`    varchar(50)   DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime      DEFAULT NULL COMMENT '更新日期',
    `sys_org_code` varchar(64)   DEFAULT NULL COMMENT '所属部门',
    `name`         varchar(32)   DEFAULT NULL COMMENT '名字',
    `age`          int(8) DEFAULT NULL COMMENT '年龄',
    `sex`          varchar(32)   DEFAULT NULL COMMENT '性别',
    `birthday`     date          DEFAULT NULL COMMENT '生日',
    `cc`           varchar(1000) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
