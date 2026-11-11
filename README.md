# 硅谷甄选后台管理系统 - Java 版后端源码

> 基于 **Spring Boot 3.5.7** 框架重写的硅谷甄选后台 API 应用，与 [Node（Express）版本](https://github.com/chenmiaozhan2024/Vue3_admin_main) 的接口路径、入参、响应格式保持一致，可直接对接前端项目 [Vue3_admin_template](https://github.com/chenmiaozhan2024/Vue3_admin_template)。

## 项目简介

本项目是**尚硅谷**硅谷甄选（前端项目）的配套后端。原项目先后有 Go 语言版本、Node（Express）版本，本仓库是参考上述版本使用 **Spring Boot + MyBatis** 重写的 Java 版本，适合有一定 Java 基础的同学学习使用。

## 技术栈

| 技术 | 说明 |
| ------ | ------ |
| JDK 17 | 运行环境 |
| Spring Boot 3.5.7 | Web 框架 |
| MyBatis 3.0.4 | ORM / 数据访问 |
| MySQL | 数据库（mysql-connector-j） |
| JWT（jjwt 0.12.6） | 身份验证 |
| Lombok | 简化实体类 |
| HikariCP | 数据库连接池（Spring Boot 默认） |

## 项目结构

```
java_admin_backend/
├── pom.xml                        # Maven 依赖清单
├── upload/                        # 上传文件存储目录（upload/img/sph/yyyyMMdd/）
└── src/main/
    ├── resources/
    │   └── application.properties # 应用配置（端口、数据库、JWT、上传限制）
    ├── java/com/itheima/java_admin_backend/
    │   ├── JavaAdminBackendApplication.java  # 启动入口
    │   ├── common/        # 统一响应格式 Result / PageResult
    │   ├── config/        # WebConfig（静态资源映射、CORS、拦截器注册）
    │   ├── controller/    # 控制器层（10 个模块）
    │   ├── service/       # 业务逻辑层（含 impl 实现）
    │   ├── mapper/        # 数据访问层（MyBatis 注解 SQL）
    │   ├── dto/           # 接口入参
    │   ├── vo/            # 接口出参
    │   ├── entity/        # 数据库实体
    │   ├── interceptor/   # JwtInterceptor 登录校验拦截器
    │   ├── exception/     # GlobalExceptionHandler 全局异常处理
    │   └── init-sql/      # init.sql 数据库初始化脚本
    └── ...
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+ / 8.x

### 1. 初始化数据库

执行 `src/main/java/com/itheima/java_admin_backend/init-sql/init.sql`，创建数据库和表并写入初始数据。

### 2. 修改数据库配置

编辑 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/vue3_admin
spring.datasource.username=root
spring.datasource.password=123456   # 替换成你本地数据库密码
```

### 3. 启动后端

```bash
mvn spring-boot:run
```

项目默认运行在 `http://localhost:8685`。

### 4. 启动前端

前端项目（Vue3_admin_template）中修改 `.env.development`：

```
VITE_SERVE = 'http://localhost:8685'
```

重启前端 `npm run dev` 即可联调。

## API 接口文档

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/admin/acl/index/login` | 用户登录 |
| GET | `/admin/acl/index/info` | 获取用户信息（含菜单权限） |
| POST | `/admin/acl/index/logout` | 退出登录 |

### 用户管理（ACL）

| 方法 | 路径 | 说明 |
| ------ | ------ | ------ |
| GET | `/admin/acl/user/{page}/{limit}` | 分页获取用户列表（username 模糊搜索） |
| GET | `/admin/acl/user/toAssign/{userId}` | 获取用户分配的角色及全部角色列表 |
| POST | `/admin/acl/user/save` | 新增用户 |
| PUT | `/admin/acl/user/update` | 更新用户 |
| DELETE | `/admin/acl/user/remove/{id}` | 删除用户 |
| DELETE | `/admin/acl/user/batchRemove` | 批量删除用户（请求体为 id 数组） |
| POST | `/admin/acl/user/doAssignRole` | 为用户分配角色 |

### 角色管理（ACL）

| 方法 | 路径 | 说明 |
| ------ | ------ | ------ |
| GET | `/admin/acl/role/{page}/{limit}` | 分页获取角色列表（roleName 模糊搜索） |
| POST | `/admin/acl/role/save` | 新增角色 |
| PUT | `/admin/acl/role/update` | 更新角色 |
| DELETE | `/admin/acl/role/remove/{roleId}` | 删除角色 |

### 权限/菜单管理（ACL）

| 方法 | 路径 | 说明 |
| ------ | ------ | ------ |
| GET | `/admin/acl/permission` | 获取所有菜单（树形结构） |
| POST | `/admin/acl/permission/save` | 新增菜单/权限 |
| PUT | `/admin/acl/permission/update` | 更新菜单/权限 |
| DELETE | `/admin/acl/permission/remove/{id}` | 删除菜单/权限 |
| GET | `/admin/acl/permission/toAssign/{roleId}` | 获取角色分配的权限（树形+勾选标记） |
| POST | `/admin/acl/permission/doAssign` | 给角色分配权限（roleId + 逗号分隔的 permissionId） |

### 商品管理

| 方法 | 路径 | 说明 |
| ------ | ------ | ------ |
| GET | `/admin/product/getCategory1` | 获取一级分类 |
| GET | `/admin/product/getCategory2/{category1Id}` | 获取二级分类 |
| GET | `/admin/product/getCategory3/{category2Id}` | 获取三级分类 |
| GET | `/admin/product/attrInfoList/{category1}/{category2}/{category3}` | 属性列表 |
| POST | `/admin/product/saveAttrInfo` | 新增/修改属性 |
| DELETE | `/admin/product/deleteAttr/{attrId}` | 删除属性 |
| GET | `/admin/product/baseTrademark/{page}/{pageSize}` | 品牌列表 |
| POST | `/admin/product/baseTrademark/save` | 新增品牌 |
| PUT | `/admin/product/baseTrademark/update` | 更新品牌 |
| DELETE | `/admin/product/baseTrademark/remove/{id}` | 删除品牌 |
| GET | `/admin/product/baseTrademark/getTrademarkList` | 获取全部品牌 |
| POST | `/admin/product/fileUpload` | 图片上传 |
| GET | `/admin/product/{page}/{limit}` | SPU 列表 |
| POST | `/admin/product/saveSpuInfo` | 新增 SPU |
| PUT | `/admin/product/updateSpuInfo` | 更新 SPU |
| DELETE | `/admin/product/deleteSpu/{spuId}` | 删除 SPU |
| GET | `/admin/product/spuImageList/{spuId}` | SPU 图片列表 |
| GET | `/admin/product/spuSaleAttrList/{spuId}` | SPU 销售属性列表 |
| GET | `/admin/product/baseSaleAttrList` | 销售属性列表 |
| POST | `/admin/product/saveSkuInfo` | 保存 SKU |
| GET | `/admin/product/list/{page}/{pageSize}` | SKU 列表 |
| GET | `/admin/product/onSale/{skuId}` | 商品上架 |
| GET | `/admin/product/cancelSale/{skuId}` | 商品下架 |
| GET | `/admin/product/findBySpuId/{spuId}` | 查看 SPU 下已有 SKU |
| GET | `/admin/product/getSkuInfo/{skuId}` | 获取 SKU 详情 |
| GET | `/admin/product/{page}/{limit}` | SKU 分页（SkuController 提供） |

## 统一响应格式

所有接口返回统一的 JSON 格式（`common.Result` / `common.PageResult`）：

### 成功响应

```json
{
  "code": 200,
  "data": {},
  "message": "success",
  "ok": true
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "错误信息",
  "ok": false
}
```

## JWT 认证

除登录接口和静态资源外，`/admin/**` 下的接口都需要在请求头中携带 Token（由 `JwtInterceptor` 校验）：

```
token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Token 有效期、密钥在 `application.properties` 中配置：

```properties
jwt.secret=silicon-valley-admin-jwt-secret-key-please-change-it
jwt.expiration=86400000
```

## 文件上传与静态资源

- 上传接口：`POST /admin/product/fileUpload`，文件保存在 **`{项目运行目录}/upload/img/sph/yyyyMMdd/`**
- 返回可直接访问的图片 URL：`/api/static/img/sph/yyyyMMdd/文件名`
- `WebConfig` 同时映射了 `/api/static/img/**` 和 `/static/img/**` 两个路径（兼容历史数据）
- 上传大小限制：单文件 10MB

## 数据库表结构

### 核心表

- `user` - 用户表
- `role` - 角色表
- `menu` - 菜单/权限表
- `user_role` - 用户角色关联表
- `role_menu` - 角色菜单关联表
- `base_category1/2/3` - 商品三级分类表
- `base_attr / attr_value` - 商品属性表
- `base_trademark` - 品牌表
- `spu / spu_image_list / spu_sale_attr / sale_attr_value` - SPU 相关
- `sku / sku_image / sku_attr_value / sku_sale_attr_value` - SKU 相关

## 与 Node 版本的差异说明

- 运行端口为 **8685**（Node 版为 3000），前端 `.env.development` 中 `VITE_SERVE` 需对应修改
- 图片存储在 Java 项目的 `upload/img/` 目录（Node 版在 `static/img/`），路径已兼容
- 分配角色权限接口为 `POST /admin/acl/permission/doAssign`，`permissionId` 为逗号分隔字符串，与 Node 版一致
- 删除菜单/角色前会先清理关联数据（子菜单、角色菜单关联），保证数据一致性

## License

MIT
