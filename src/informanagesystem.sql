/*
Navicat MySQL Data Transfer

Source Server         : 测试mysql链接
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : informanagesystem

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-11-25 21:57:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `administrator`
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of administrator
-- ----------------------------
INSERT INTO `administrator` VALUES ('1', 'admin1', 'admin1');
INSERT INTO `administrator` VALUES ('2', 'admin2', 'admin2');
INSERT INTO `administrator` VALUES ('3', 'admin3', 'admin3');
INSERT INTO `administrator` VALUES ('4', 'admin4', 'admin4');
INSERT INTO `administrator` VALUES ('5', 'admin5', 'admin5');
INSERT INTO `administrator` VALUES ('6', 'admin6', 'admin6');
INSERT INTO `administrator` VALUES ('7', 'admin7', 'admin7');

-- ----------------------------
-- Table structure for `course`
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `cno` char(10) COLLATE utf8_unicode_ci NOT NULL,
  `cname` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `semester` tinyint(4) DEFAULT NULL,
  `number` int(3) DEFAULT NULL,
  `credit` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`cno`),
  UNIQUE KEY `cname` (`cname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('c01', '数据库', '3', '100', '2');
INSERT INTO `course` VALUES ('c02', '操作系统', '3', '100', '2');
INSERT INTO `course` VALUES ('c03', '网络安全', '3', '50', '3');
INSERT INTO `course` VALUES ('c04', '高等数学', '1', '200', '5');
INSERT INTO `course` VALUES ('c05', '大学英语', '2', '150', '4');
INSERT INTO `course` VALUES ('c06', '大学物理', '4', '100', '2');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `dno` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `dname` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`dno`),
  UNIQUE KEY `dname` (`dname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('d10', '信息工程学院');
INSERT INTO `department` VALUES ('d05', '土木与交通工程学院');
INSERT INTO `department` VALUES ('d08', '外国语学院');
INSERT INTO `department` VALUES ('d02', '机电工程学院');
INSERT INTO `department` VALUES ('d06', '材料与能源学院');
INSERT INTO `department` VALUES ('d09', '物理与光电工程学院');
INSERT INTO `department` VALUES ('d07', '环境科学与工程学院');
INSERT INTO `department` VALUES ('d03', '自动化学院');
INSERT INTO `department` VALUES ('d01', '计算机学院');
INSERT INTO `department` VALUES ('d04', '轻工化工学院');

-- ----------------------------
-- Table structure for `speciality`
-- ----------------------------
DROP TABLE IF EXISTS `speciality`;
CREATE TABLE `speciality` (
  `spno` char(4) COLLATE utf8_unicode_ci NOT NULL,
  `dno` char(3) COLLATE utf8_unicode_ci NOT NULL,
  `spname` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`spno`,`dno`),
  UNIQUE KEY `spname` (`spname`),
  KEY `speciality_ibfk_1` (`dno`),
  KEY `spno` (`spno`),
  CONSTRAINT `speciality_ibfk_1` FOREIGN KEY (`dno`) REFERENCES `department` (`dno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of speciality
-- ----------------------------
INSERT INTO `speciality` VALUES ('s007', 'd05', '交通运输');
INSERT INTO `speciality` VALUES ('s012', 'd10', '信息工程');
INSERT INTO `speciality` VALUES ('s010', 'd08', '商务英语');
INSERT INTO `speciality` VALUES ('s004', 'd02', '工业工程');
INSERT INTO `speciality` VALUES ('s005', 'd03', '物联网工程');
INSERT INTO `speciality` VALUES ('s009', 'd07', '环境工程');
INSERT INTO `speciality` VALUES ('s006', 'd04', '生物工程');
INSERT INTO `speciality` VALUES ('s011', 'd09', '电子科学与技术');
INSERT INTO `speciality` VALUES ('s001', 'd01', '网络工程');
INSERT INTO `speciality` VALUES ('s003', 'd01', '计算机科学与技术');
INSERT INTO `speciality` VALUES ('s002', 'd01', '软件工程');
INSERT INTO `speciality` VALUES ('s008', 'd06', '高分子材料与工程');

-- ----------------------------
-- Table structure for `studentinfo`
-- ----------------------------
DROP TABLE IF EXISTS `studentinfo`;
CREATE TABLE `studentinfo` (
  `sno` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `sname` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `ssex` enum('男','女') COLLATE utf8_unicode_ci NOT NULL DEFAULT '男',
  `dno` char(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spno` char(4) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sbirthday` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sno`),
  KEY `studentinfo_ibfk_1` (`dno`),
  KEY `studentinfo_ibfk_2` (`spno`),
  CONSTRAINT `studentinfo_ibfk_1` FOREIGN KEY (`dno`) REFERENCES `department` (`dno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `studentinfo_ibfk_2` FOREIGN KEY (`spno`) REFERENCES `speciality` (`spno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of studentinfo
-- ----------------------------
INSERT INTO `studentinfo` VALUES ('3214006498', '朱思恩', '男', 'd01', 's001', '1995-05-20 16:01:35');
INSERT INTO `studentinfo` VALUES ('3214006499', '王小明', '男', 'd01', 's001', '1996-11-17 16:04:02');
INSERT INTO `studentinfo` VALUES ('3214006500', '侧小田', '男', 'd03', 's005', '1997-06-18 05:12:18');
INSERT INTO `studentinfo` VALUES ('3214006501', '陈小迅', '男', 'd02', 's004', '1996-01-17 16:18:56');
INSERT INTO `studentinfo` VALUES ('3214006502', '王小菲', '女', 'd04', 's005', '1995-07-19 16:12:12');
INSERT INTO `studentinfo` VALUES ('3214006503', '李小红', '女', 'd01', 's002', '1994-12-21 15:09:09');
INSERT INTO `studentinfo` VALUES ('3214006577', '灌汤包', '男', 'd08', 's010', '1997-01-16 00:00:00');

-- ----------------------------
-- Table structure for `student_course`
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course` (
  `scid` int(11) NOT NULL AUTO_INCREMENT,
  `cno` char(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sno` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `score` int(11) DEFAULT '-1',
  PRIMARY KEY (`scid`),
  KEY `cno` (`cno`),
  KEY `sno` (`sno`),
  CONSTRAINT `student_course_ibfk_1` FOREIGN KEY (`cno`) REFERENCES `course` (`cno`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `student_course_ibfk_2` FOREIGN KEY (`sno`) REFERENCES `studentinfo` (`sno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of student_course
-- ----------------------------
INSERT INTO `student_course` VALUES ('1', 'c01', '3214006498', '99');
INSERT INTO `student_course` VALUES ('2', 'c01', '3214006499', '88');
INSERT INTO `student_course` VALUES ('3', 'c01', '3214006500', '88');
INSERT INTO `student_course` VALUES ('4', 'c01', '3214006501', '88');
INSERT INTO `student_course` VALUES ('5', 'c02', '3214006498', '70');
INSERT INTO `student_course` VALUES ('6', 'c02', '3214006502', '80');
INSERT INTO `student_course` VALUES ('7', 'c03', '3214006503', '60');
INSERT INTO `student_course` VALUES ('8', 'c04', '3214006500', '87');
INSERT INTO `student_course` VALUES ('9', 'c03', '3214006498', '-1');
INSERT INTO `student_course` VALUES ('10', 'c04', '3214006498', '-1');
INSERT INTO `student_course` VALUES ('12', 'c06', '3214006498', '-1');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `sno` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `sno` (`sno`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`sno`) REFERENCES `studentinfo` (`sno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'user1', 'user1', '3214006498');
INSERT INTO `user` VALUES ('2', 'user2', 'user2', '3214006499');
INSERT INTO `user` VALUES ('3', 'user3', 'user3', '3214006500');
INSERT INTO `user` VALUES ('4', 'user4', 'user4', '3214006501');
