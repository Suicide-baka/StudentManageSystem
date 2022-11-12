SET FOREIGN_KEY_CHECKS=0;

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
