# CLT\_OJ\_System

CLT\_OJ\_System 是一个基于 Spring Boot 的在线判题系统，提供代码评测、题目管理、用户认证等功能，同时集成了 AI 题目生成、虚拟宠物等特色功能。

## 目录

- [项目结构](#项目结构)
- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [快速开始](#快速开始)
- [项目配置](#项目配置)
- [主要 API 端点](#主要-api-端点)
- [项目特色](#项目特色)
- [开发与贡献](#开发与贡献)
- [许可证](#许可证)
- [联系方式](#联系方式)
- [数据库表结构](#数据库表结构)

## 项目结构

本项目采用多模块架构，主要包含以下模块：

```
CLT_OJ_System/
├── clt-oj-system-starter/       # 主模块，核心功能实现
├── ai-qianwen-generator-module/  # AI题目生成模块
├── virtual-pet-module/           # 虚拟宠物模块
├── solution-section-module/      # 题解模块
├── utils/                        # 工具类模块
└── pom.xml                       # 项目依赖管理
```

## 技术栈

- **后端**：Spring Boot 3.5.12, Java 17, MyBatis, MySQL
- **认证**：JWT (JSON Web Token)
- **存储**：阿里云 OSS (用于头像存储)
- **AI 服务**：DashScope API

## 核心功能

### 1. 题目管理

- 题目创建、编辑、删除
- 题目列表查询
- 题目详情查看
- 题目标签管理

### 2. 代码评测

- 支持多种编程语言提交
- 实时评测结果反馈
- 提交历史记录

### 3. 用户系统

- 用户注册、登录、注销
- 个人信息管理
- 用户排名系统

### 4. AI 题目生成

- 基于 DashScope API 自动生成编程题目
- 智能题目推荐

### 5. 虚拟宠物系统

- 宠物状态管理
- 宠物经验值更新
- 每日打卡功能

### 6. 题解系统

- 题解发布、编辑、删除
- 评论功能
- 题解列表查询

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6+ 或更高版本
- MySQL 8.0 或更高版本

### 数据库配置

1. 创建数据库 `clt_oj_database`
2. 配置 `clt-oj-system-starter/src/main/resources/application.yml` 中的数据库连接信息

### 构建与运行

1. 克隆项目

```bash
git clone <项目地址>
cd CLT_OJ_System
```

1. 构建项目

```bash
mvn clean install
```

1. 运行主模块

```bash
cd clt-oj-system-starter
mvn spring-boot:run
```

## 项目配置

### 核心配置文件

- `clt-oj-system-starter/src/main/resources/application.yml`：主模块配置
  - 数据库连接信息（由自己提供）
  - JWT 密钥配置
  - 阿里云 OSS 配置（由自己提供）
  - DashScope API 配置（由自己提供）

### 环境变量
- `DASHSCOPE_API_KEY`：DashScope API 密钥，用于 AI 题目生成功能
- `OSS_ACCESS_KEY_ID`：阿里云 OSS 访问密钥 ID
- `OSS_ACCESS_KEY_SECRET`：阿里云 OSS 访问密钥 Secret

### 配置说明
1. **阿里云 OSS 配置**：
   - 需要创建一个 OSS bucket 容器
   - 在主模块配置文件 `clt-oj-system-starter/src/main/resources/application.yml` 中配置 `aliyun.oss` 相关参数
   - 将 `OSS_ACCESS_KEY_ID` 和 `OSS_ACCESS_KEY_SECRET` 设置为环境变量

2. **DashScope API 配置**：
   - 将 `DASHSCOPE_API_KEY` 设置为环境变量，用于 AI 题目生成功能

## 主要 API 端点

### 认证相关

- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户注销

### 题目相关

- `GET /api/problems` - 获取题目列表
- `GET /api/problems/{id}` - 获取题目详情
- `POST /api/problems` - 创建题目
- `PUT /api/problems/{id}` - 更新题目
- `DELETE /api/problems/{id}` - 删除题目

### 提交相关

- `POST /api/submissions` - 提交代码
- `GET /api/submissions` - 获取提交历史
- `GET /api/submissions/{id}` - 获取提交详情

### 用户相关

- `GET /api/users/{id}` - 获取用户信息
- `PUT /api/users/{id}` - 更新用户信息
- `GET /api/users/rank` - 获取用户排名

### AI 题目生成

- `POST /api/ai/generate` - 生成 AI 题目

### 虚拟宠物

- `GET /api/pets/{userId}` - 获取用户宠物信息
- `POST /api/pets/punch` - 宠物打卡
- `PUT /api/pets/{userId}` - 更新宠物信息

### 题解相关

- `GET /api/solutions` - 获取题解列表
- `POST /api/solutions` - 发布题解
- `GET /api/solutions/{id}` - 获取题解详情
- `POST /api/solutions/{id}/comments` - 发表评论

## 项目特色

1. **模块化设计**：采用多模块架构，各功能模块解耦合，便于维护和扩展
2. **AI 集成**：利用 DashScope API 实现智能题目生成
3. **虚拟宠物**：增加用户粘性的趣味功能
4. **完整的判题系统**：支持多种编程语言的代码评测
5. **响应式前端**：基于 Vue.js 构建的现代化前端界面

## 开发与贡献

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情见 [LICENSE](LICENSE) 文件

## 联系方式

如有问题或建议，请联系项目维护者。

## 数据库表结构

### 表结构详情

#### users 表

**描述**：存储用户信息

**字段说明**：
- `id`：用户ID，自增主键
- `username`：用户名，唯一
- `password`：密码
- `role`：角色，默认值1
- `created_at`：创建时间，默认当前时间
- `nickname`：昵称
- `hobby`：爱好
- `introduction`：简介
- `avatar`：头像URL，默认值为阿里云OSS存储的图片

**创建表SQL**：

```sql
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` tinyint DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `nickname` varchar(50) DEFAULT NULL,
  `hobby` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT 'https://clt-oj-avatar-store.oss-cn-chengdu.aliyuncs.com/2026/04/ca39bd34-5f39-4817-91be-1575ff500c3c.jpg',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### problems 表

**描述**：存储题目信息

**字段说明**：
- `id`：题目ID，自增主键
- `title`：题目标题
- `description`：题目描述
- `input_format`：输入格式
- `output_format`：输出格式
- `time_limit`：时间限制（秒），默认10.000
- `memory_limit`：内存限制（MB），默认256.00
- `difficulty`：难度，默认1
- `hint`：提示

**创建表SQL**：

```sql
CREATE TABLE `problems` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `description` text NOT NULL,
  `input_format` text,
  `output_format` text,
  `time_limit` decimal(8,3) DEFAULT '10.000',
  `memory_limit` decimal(10,2) DEFAULT '256.00',
  `difficulty` tinyint DEFAULT '1',
  `hint` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### tags 表

**描述**：存储题目标签

**字段说明**：
- `id`：标签ID，自增主键
- `problem_id`：题目ID，外键关联problems表
- `name`：标签名称，唯一
- `color`：标签颜色，默认'#ffff'

**表间关系**：
- `problem_id` 外键关联 `problems.id`

**创建表SQL**：

```sql
CREATE TABLE `tags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `problem_id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `color` varchar(20) DEFAULT '#ffff',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `problem_id` (`problem_id`),
  CONSTRAINT `tags_ibfk_1` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### problem_samples 表

**描述**：存储题目样例输入输出

**字段说明**：
- `id`：样例ID，自增主键
- `problem_id`：题目ID，外键关联problems表
- `sample_order`：样例顺序
- `input_content`：输入内容
- `output_content`：输出内容
- `is_example`：是否为示例，默认1

**表间关系**：
- `problem_id` 外键关联 `problems.id`

**创建表SQL**：

```sql
CREATE TABLE `problem_samples` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `problem_id` bigint DEFAULT NULL,
  `sample_order` int DEFAULT NULL,
  `input_content` text,
  `output_content` text,
  `is_example` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `problem_id` (`problem_id`),
  CONSTRAINT `problem_samples_ibfk_1` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### submissions 表

**描述**：存储用户代码提交记录

**字段说明**：
- `id`：提交ID，自增主键
- `user_id`：用户ID，外键关联users表
- `problem_id`：题目ID，外键关联problems表
- `language`：编程语言
- `code`：提交的代码
- `status`：提交状态
- `message`：状态消息
- `stdout`：标准输出
- `stderr`：标准错误
- `compile_output`：编译输出
- `submit_time`：提交时间，默认当前时间
- `runtime`：运行时间（秒）
- `memory`：内存使用（MB）

**表间关系**：
- `user_id` 外键关联 `users.id`
- `problem_id` 外键关联 `problems.id`

**创建表SQL**：

```sql
CREATE TABLE `submissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `problem_id` bigint NOT NULL,
  `language` varchar(20) NOT NULL,
  `code` text NOT NULL,
  `status` varchar(20),
  `message` text,
  `stdout` text,
  `stderr` text,
  `compile_output` text,
  `submit_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `runtime` decimal(8,3) DEFAULT NULL,
  `memory` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `problem_id` (`problem_id`),
  CONSTRAINT `submissions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `submissions_ibfk_2` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### submission_test_cases 表

**描述**：存储提交的测试用例结果

**字段说明**：
- `id`：测试用例ID，自增主键
- `submission_id`：提交ID，外键关联submissions表
- `case_id`：测试用例ID
- `status`：测试状态
- `runtime`：运行时间（秒）
- `memory`：内存使用（MB）
- `create_time`：创建时间，默认当前时间
- `update_time`：更新时间，默认当前时间

**表间关系**：
- `submission_id` 外键关联 `submissions.id`

**创建表SQL**：

```sql
CREATE TABLE `submission_test_cases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `submission_id` bigint NOT NULL,
  `case_id` bigint NOT NULL,
  `status` varchar(20),
  `runtime` decimal(8,3) DEFAULT NULL,
  `memory` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_submission_case` (`submission_id`,`case_id`),
  CONSTRAINT `submission_test_cases_ibfk_1` FOREIGN KEY (`submission_id`) REFERENCES `submissions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### solved_problem_counts 表

**描述**：存储用户解决的题目数量

**字段说明**：
- `id`：记录ID，自增主键
- `user_id`：用户ID，外键关联users表
- `problem_id`：题目ID
- `solved_count`：解决次数，默认0

**表间关系**：
- `user_id` 外键关联 `users.id`

**创建表SQL**：

```sql
CREATE TABLE `solved_problem_counts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `problem_id` bigint NOT NULL,
  `solved_count` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `solved_problem_counts_users_id_fk` (`user_id`),
  CONSTRAINT `solved_problem_counts_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### user_problem_status 表

**描述**：存储用户对题目的状态

**字段说明**：
- `user_id`：用户ID，复合主键
- `problem_id`：题目ID，复合主键
- `status`：状态
- `last_submit_time`：最后提交时间

**创建表SQL**：

```sql
CREATE TABLE `user_problem_status` (
  `user_id` bigint NOT NULL,
  `problem_id` bigint NOT NULL,
  `status` int DEFAULT NULL,
  `last_submit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### pets 表

**描述**：存储用户宠物信息

**字段说明**：
- `id`：宠物ID，自增主键
- `name`：宠物名称
- `experience`：经验值，默认0
- `level`：等级，默认1
- `created_at`：创建时间，默认当前时间
- `punch_date`：打卡日期
- `number_of_punch_outs`：打卡次数，默认0
- `user_id`：用户ID，外键关联users表，唯一

**表间关系**：
- `user_id` 外键关联 `users.id`

**创建表SQL**：

```sql
CREATE TABLE `pets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `experience` bigint DEFAULT '0',
  `level` int DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `punch_date` datetime DEFAULT NULL,
  `number_of_punch_outs` int DEFAULT '0',
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `pets_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### pet_phrases 表

**描述**：存储宠物的不同状态短语

**字段说明**：
- `id`：短语ID，自增主键
- `pet_id`：宠物ID，外键关联pets表
- `accepted`：AC状态短语
- `wrong_answer`：WA状态短语
- `time_limit_exceeded`：TLE状态短语
- `compile_error`：CE状态短语
- `runtime_error`：RE状态短语
- `internal_error`：内部错误状态短语
- `exec_format_error`：执行格式错误状态短语
- `unknown_error`：未知错误状态短语
- `memory_out_of_range`：内存超出范围状态短语

**表间关系**：
- `pet_id` 外键关联 `pets.id`

**创建表SQL**：

```sql
CREATE TABLE `pet_phrases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pet_id` bigint DEFAULT NULL,
  `accepted` varchar(200) DEFAULT NULL,
  `wrong_answer` varchar(200) DEFAULT NULL,
  `time_limit_exceeded` varchar(200) DEFAULT NULL,
  `compile_error` varchar(200) DEFAULT NULL,
  `runtime_error` varchar(200) DEFAULT NULL,
  `internal_error` varchar(200) DEFAULT NULL,
  `exec_format_error` varchar(200) DEFAULT NULL,
  `unknown_error` varchar(200) DEFAULT NULL,
  `memory_out_of_range` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pet_id` (`pet_id`),
  CONSTRAINT `pet_phrases_ibfk_1` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### solutions 表

**描述**：存储题解信息

**字段说明**：
- `id`：题解ID，自增主键
- `problem_id`：题目ID，外键关联problems表
- `user_id`：用户ID，外键关联users表
- `title`：题解标题
- `language`：主要编程语言
- `like_count`：点赞数，默认0
- `comment_count`：评论数，默认0
- `is_official`：是否官方题解，默认0
- `create_time`：创建时间，默认当前时间
- `update_time`：更新时间，默认当前时间并在更新时自动更新

**表间关系**：
- `problem_id` 外键关联 `problems.id`
- `user_id` 外键关联 `users.id`

**创建表SQL**：

```sql
CREATE TABLE `solutions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `problem_id` bigint NOT NULL COMMENT '关联题目ID',
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `title` varchar(200) NOT NULL COMMENT '题解标题',
  `language` varchar(20) NOT NULL COMMENT '主要编程语言',
  `like_count` int unsigned DEFAULT '0' COMMENT '点赞数',
  `comment_count` int unsigned DEFAULT '0' COMMENT '评论数',
  `is_official` tinyint NOT NULL DEFAULT '0' COMMENT '是否官方题解: 1-是, 0-否',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `problem_id` (`problem_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `solutions_ibfk_1` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`id`),
  CONSTRAINT `solutions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### solution_content 表

**描述**：存储题解内容

**字段说明**：
- `id`：内容ID，自增主键
- `solution_id`：题解ID，外键关联solutions表，唯一
- `content_md`：Markdown格式的题解内容

**表间关系**：
- `solution_id` 外键关联 `solutions.id`

**创建表SQL**：

```sql
CREATE TABLE `solution_content` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `solution_id` bigint NOT NULL COMMENT '关联主表ID',
  `content_md` longtext NOT NULL COMMENT 'Markdown源码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_solution_id` (`solution_id`),
  CONSTRAINT `solution_content_ibfk_1` FOREIGN KEY (`solution_id`) REFERENCES `solutions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

#### solution_comment 表

**描述**：存储题解评论

**字段说明**：
- `id`：评论ID，自增主键
- `solution_id`：题解ID，外键关联solutions表
- `user_id`：用户ID，外键关联users表
- `parent_id`：父级评论ID，0表示直接评论题解
- `content`：评论内容
- `like_count`：点赞数，默认0
- `create_time`：创建时间，默认当前时间

**表间关系**：
- `solution_id` 外键关联 `solutions.id`
- `user_id` 外键关联 `users.id`

**创建表SQL**：

```sql
CREATE TABLE `solution_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `solution_id` bigint NOT NULL COMMENT '关联的题解ID，指向 solutions 表的 id',
  `user_id` bigint NOT NULL COMMENT '评论者的用户ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父级评论ID，0表示直接评论题解，非0表示回复某条评论',
  `content` varchar(1000) NOT NULL COMMENT '评论内容',
  `like_count` int unsigned DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `solution_id` (`solution_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `solution_comment_ibfk_1` FOREIGN KEY (`solution_id`) REFERENCES `solutions` (`id`),
  CONSTRAINT `solution_comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

### 表间关系图

```
users ─────┐
           │
           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                                                                   │
▼                                                                   ▼
submissions ──────► submission_test_cases                pets ───────► pet_phrases
│                                                                   │
▼                                                                   │
user_problem_status                                                solutions
│                                                                   │
▼                                                                   ▼
solved_problem_counts                                solution_content
│                                                                   │
▼                                                                   ▼
problems ──────► problem_samples                          solution_comment
│
▼
tags
```

***

*注：本项目为学习和教学目的开发，可根据实际需求进行定制和扩展。*
