/*
 Navicat Premium Dump SQL

 Source Server         : Mysql数据库（docker）
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : yunying

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 27/10/2024 19:43:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Contribution
-- ----------------------------
DROP TABLE IF EXISTS `Contribution`;
CREATE TABLE `Contribution`  (
  `con_id` int NOT NULL COMMENT '贡献id',
  `repo_id` int NULL DEFAULT NULL COMMENT '【外键字段】仓库id',
  `dev_id` int NULL DEFAULT NULL COMMENT '【外键字段】开发者id',
  `weight` double NULL DEFAULT NULL COMMENT '【计算字段】贡献权重',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`con_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Developer
-- ----------------------------
DROP TABLE IF EXISTS `Developer`;
CREATE TABLE `Developer`  (
  `dev_id` int NOT NULL COMMENT '开发者id',
  `dev_login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发者登录id',
  `dev_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发者名字',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发者头像',
  `home` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开发者主页',
  `blog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '博客链接',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `bio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自我介绍',
  `followers` int NULL DEFAULT NULL COMMENT '粉丝',
  `following` int NULL DEFAULT NULL COMMENT '关注人数',
  `talent_rank` float NULL DEFAULT NULL COMMENT '【计算字段】技能排序值',
  `nation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '【推测字段】所属国家',
  `nation_conf` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '【推测字段】国家/地区可信度',
  `field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '【推测字段】开发者领域',
  `field_conf` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '【推测字段】领域可信度',
  `profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '【生成字段】开发者简介',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '第一此创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dev_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for Repository
-- ----------------------------
DROP TABLE IF EXISTS `Repository`;
CREATE TABLE `Repository`  (
  `repo_id` int NOT NULL COMMENT '仓库id',
  `repo_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '仓库名字',
  `repo_full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '仓库全名（包含归属者）',
  `repo_home` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '仓库链接',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '仓库描述',
  `owner_id` int NULL DEFAULT NULL COMMENT '仓库作者id',
  `language` json NULL COMMENT '【JSON字段】仓库语言',
  `watchers_count` int NULL DEFAULT NULL COMMENT '观察者数量',
  `forks_count` int NULL DEFAULT NULL COMMENT 'fork数量',
  `stars_count` int NULL DEFAULT NULL COMMENT 'star数量',
  `importance` float NULL DEFAULT NULL COMMENT '【计算字段】仓库重要性评分',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
  PRIMARY KEY (`repo_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
