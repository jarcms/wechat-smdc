# 扫码点餐小程序

## 项目介绍
本项目是一款支持单店的扫码点餐微信小程序，为餐厅提供便捷高效的点餐解决方案。用户通过扫描桌面二维码即可进入点餐页面，浏览菜品、添加购物车、提交订单并支付。后台管理系统支持菜品管理、订单管理、桌位管理等功能。

## 项目架构
- **后端**: Spring Boot 2.6.13 + MySQL + MyBatis-Plus
- **前端管理系统**: Vue + Element UI
- **用户端**: 微信小程序

## 技术栈
- **Java版本**: JDK 8
- **后端框架**: Spring Boot, MyBatis-Plus
- **数据库**: MySQL
- **前端技术**: Vue, Element UI, 微信小程序

## 功能清单

### 用户端（微信小程序）
- 微信授权登录
- 扫码点餐
- 浏览菜品分类和商品
- 查看菜品详情
- 添加购物车
- 提交订单
- 微信支付
- 查看订单状态和历史订单

### 商家端（后台管理系统）
- 员工管理
- 分类管理
- 菜品管理
- 订单管理
- 桌位管理
- 统计分析

## 项目运行
### 1. 后端项目
1. 创建数据库并导入SQL文件
```sql
-- 导入初始化SQL
mysql -u root -p < src/main/resources/smdc.sql
```

2. 修改数据库连接配置
```yaml
# 修改 src/main/resources/application.yml 中的数据库连接信息
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smdc?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: 你的用户名
    password: 你的密码
```

3. 编译运行
```bash
# 打包项目
mvn clean package

# 运行项目
java -jar target/smdc-0.0.1-SNAPSHOT.jar
```

### 2. 前端管理系统
```bash
# 进入admin目录
cd admin

# 安装依赖
npm install

# 启动开发服务器
npm run serve

# 打包项目
npm run build
```

### 3. 微信小程序
1. 下载并安装[微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
2. 导入项目/miniapp目录
3. 配置AppID（申请小程序后获得）

## 接口说明
请参考[接口文档](./开发流程.md)中的接口设计部分。

## 项目结构
```
root/
├── src/                                # 后端源代码
│   ├── main/
│   │   ├── java/com/shechubbb/smdc/
│   │   │   ├── config/                 # 配置类
│   │   │   ├── controller/             # 控制器
│   │   │   │   ├── admin/              # 管理后台控制器
│   │   │   │   └── mini/               # 小程序控制器
│   │   │   ├── entity/                 # 实体类
│   │   │   ├── mapper/                 # Mapper接口
│   │   │   ├── service/                # 服务接口
│   │   │   │   └── impl/               # 服务实现类
│   │   │   ├── vo/                     # 视图对象
│   │   │   ├── common/                 # 公共组件
│   │   │   └── SmdcApplication.java    # 应用启动类
│   │   ├── resources/
│   │   │   ├── application.yml         # 应用配置
│   │   │   └── smdc.sql                # 数据库初始化脚本
├── miniapp/                            # 微信小程序代码
├── admin/                              # 管理后台代码
└── pom.xml                             # Maven配置
```

## 开发者
- [您的名字/团队名称]

## 许可证
本项目采用 MIT 许可证，详情请参见 [LICENSE](LICENSE) 文件。 