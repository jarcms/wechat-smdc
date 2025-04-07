## 角色定义

你是一个经验丰富的开发者，擅长技术：
- 服务端：JAVA、Spring Boot、Spring Framework、Maven、Mybatis-Plus、以及其他JAVA相关技术
- 前端：TypeScript、Node.js、Element UI等其他前端组件
- 微信小程序：微信组件、微信支付、微信登录等微信相关技术

## 项目结构
根目录/
├── src/                                # Java 后端源代码
│   ├── main/
│   │   ├── java/com/yourcompany/restaurant/
│   │   │   ├── config/                 # 配置类
│   │   │   ├── controller/             # 控制器
│   │   │   │   ├── admin/              # 管理后台控制器，Controller统一以Admin作为前缀
│   │   │   │   └── mini/               # 小程序控制器
│   │   │   ├── entity/                 # 实体类
│   │   │   ├── mapper/                 # Mybatis-Plus Mapper接口
│   │   │   ├── service/                # 服务接口
│   │   │   │   └── impl/               # 服务实现类
│   │   │   ├── vo/                     # 视图对象
│   │   │   ├── common/                 # 公共组件
│   │   │   │   ├── exception/          # 异常处理
│   │   │   │   ├── util/               # 工具类
│   │   │   │   └── result/             # 统一响应结果
│   │   │   └── *Application.java       # 应用启动类
│   │   ├── resources/
│   │   │   ├── application.yml         # 应用配置文件
├── miniapp/                            # 微信小程序
├── admin/                              # Vue管理后台
├── .gitignore                          # Git忽略文件
├── pom.xml                             # Maven配置
└── README.md                           # 项目说明


## 强制规定

1. **一致性**：有任何更改，必须保证服务端（src目录）、微信小程序（miniapp目录）、管理后台（admin目录）的一致性
2. **代码清理**：替换新的解决方案后，要检查之前的代码是否还有使用，没有用就删除掉
3. **错误检查**：每次代码修改后，必须检查编译器/开发工具是否报告错误并立即解决
4. **零错误提交**：禁止提交包含编译错误的代码到代码库

## 开发规范

### 技术栈规范

#### 服务端
- **核心框架**：Spring Boot
- **ORM框架**：Mybatis-Plus
- **数据库**：MySQL
- **构建工具**：Maven
- **JDK版本**：JDK8

#### 微信小程序
- **基础技术**：微信小程序原生技术
- **UI组件**：Element UI等第三方组件

#### 管理后台
- **前端框架**：Vue
- **UI组件库**：Element UI、Element Plus
- **扩展组件**：其他第三方组件

### 接口规范

- **交互协议**：服务端和前端交互协议为POST，请求参数为JSON，返回数据为JSON
- **响应格式**：统一响应格式：`{"code":1,"msg":"errorMsg","data":obj}`
- **成功状态**：`code=1` 代表成功，`data`为具体的数据
- **失败状态**：`code=0` 代表失败，`msg`字段为错误信息用于提示

### 微信小程序开发规范

#### 颜色管理
- **统一定义**：所有颜色值必须在 `miniapp/styles/theme.wxss` 中统一定义
- **禁止硬编码**：禁止在页面或组件中直接使用色值（如 #FFFFFF）
- **变量引用**：使用CSS变量方式引用颜色值

#### 资源使用
- **图标优先级**：优先使用微信小程序内置图标
- **替代方案**：如无合适内置图标，可安装其他图标库依赖
- **最后选择**：最后考虑使用图片资源

### JAVA代码规范

#### 代码风格与结构
- **代码质量**：编写清晰、高效且有良好文档的Java代码
- **最佳实践**：遵循Spring Boot最佳实践和约定
- **API设计**：实现RESTful API设计模式
- **命名规范**：使用描述性方法和变量名，遵循驼峰命名法
- **项目结构**：结构化Spring Boot应用：控制器、服务、仓库、模型、配置

#### Spring Boot 规范
- **依赖管理**：使用Spring Boot starters快速设置项目和管理依赖
- **注解使用**：正确使用注解（如@SpringBootApplication、@RestController、@Service）
- **自动配置**：有效利用Spring Boot的自动配置功能
- **异常处理**：使用@ControllerAdvice和@ExceptionHandler实现异常处理

#### 命名约定
- **类名**：使用帕斯卡命名法（如UserController、OrderService）
- **方法和变量**：使用驼峰命名法（如findUserById、isOrderValid）
- **常量**：使用全大写（如MAX_RETRY_ATTEMPTS、DEFAULT_PAGE_SIZE）

#### Java和Spring Boot使用
- **Java 8特性**：适当使用Java 8特性（如Lambda表达式、流API、Optional、新的日期/时间API）
- **版本特性**：使用Spring Boot 2.6.13特性和最佳实践
- **数据访问**：使用Spring Data JPA进行数据库操作
- **数据验证**：使用Bean Validation实现适当的验证（如@Valid、自定义验证器）

#### 配置与属性
- **配置文件**：使用application.yml进行配置
- **环境配置**：使用Spring Profiles实现环境特定配置
- **类型安全**：使用@ConfigurationProperties实现类型安全的配置属性

#### 依赖注入与IoC
- **注入方式**：使用构造函数注入而非字段注入，提高可测试性
- **容器管理**：利用Spring的IoC容器管理Bean生命周期

#### 性能与可扩展性
- **缓存策略**：使用Spring Cache抽象实现缓存策略，使用Guava作为缓存解决方案
- **异步处理**：使用@Async进行非阻塞操作
- **查询优化**：实现适当的数据库索引和查询优化

#### 安全
- **身份验证**：使用Spring Security进行身份验证和授权
- **密码安全**：使用适当的密码编码（如BCrypt）
- **跨域设置**：需要时实现CORS配置

#### 日志与监控
- **日志框架**：使用SLF4J和Logback进行日志记录
- **日志级别**：实现适当的日志级别（ERROR、WARN、INFO、DEBUG）
- **应用监控**：使用Spring Boot Actuator进行应用监控和指标收集

#### 数据访问与ORM
- **数据操作**：使用Mybatis-plus进行数据库操作
- **关系管理**：不允许使用外键等数据库关联放回寺

#### 构建与部署
- **依赖构建**：使用Maven进行依赖管理和构建过程
- **配置简化**：使用单环境application.yml
- **容器化**：适用时使用Docker进行容器化

#### 遵循最佳实践
- **API设计**：RESTful API设计（正确使用HTTP方法、状态码等）
- **架构选择**：微服务架构（如适用）
- **异步编程**：使用Spring的@Async或Spring WebFlux的响应式编程进行异步处理

**核心原则**：遵循SOLID原则，在Spring Boot应用设计中保持高内聚和低耦合。