## SpringBootDemo

以数据字典为例说明SpringBoot的应用。

MongoDB、Redis、MySQL需要自己找服务。配置文件见`application.yml`

PPT文件见`site/presentation`，直接在浏览器中打开`springboot.html`即可。

如果想在这个项目的基础上进行更改，可以Fork此项目。然后自己来修改。

## Big Picture

- 整个系统将UI（PC端、APP端等）与后台服务分离，UI与后台服务之间以及后台服务相互之间均基于
Restful用JSON传输。

- 将整个系统拆分成多个相对独立的服务模块，每个模块可能即是Producer又是Consumer，即：
它在对外提供服务的时，需要依赖其它模块的服务。

- 服务模块分为基础类和业务类，业务类与UI交互，而基础类与业务类交互。

- 鉴于第二点，每个模块都会包含两到三个子模块，分别是：

    * client - 供其它的服务依赖，提供一些Bean和SDK
    * server - 提供服务，client会调用server
    * common - 可选，供client和server依赖，提供client和server共有的类


## SpringBoot

SpringBoot = SpringMVC + SpringData(JPA)

## 开发环境及工具

- JDK1.8

- Maven3

- Git

- Jetbrains IntelliJ IDEA | Spring Tool Suite

- 需要用到的插件：Lombok、CheckStyle、Findbugs、PMD


## 创建项目

File -> New -> Project: Spring Initialzr, Follow the instruction.

以后所有的项目应以[etd-build](http://gitlab.etongdai.org/infra/etd-common-dependency)做为父项目，并加入统一的依赖管理，
在`etd-common-dependency`中已经包括了常用的Jar包的版本，在引入依赖时，仅需要增加Group和Artificat就可以了，无须再指定版本。
防止出现因Jar冲突而导致的各种奇怪问题。

```xml
<parent>
    <groupId>com.etongdai</groupId>
    <artifactId>etd-build</artifactId>
    <version>${etd-build.version}</version>
</parent>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.etongdai</groupId>
            <artifactId>etd-common-dependency</artifactId>
            <version>${etd-common-dependency.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 需要使用到的依赖：

- Web: SpringMVC及Tomcat

- JPA: ORM

- MySQL: 生产数据库

- H2: 开发数据库

- Lombok: 代码简化注解


### 主要文件说明

- pom.xml: Maven配置文件，管理项目依赖，打包、部署

- Application.java: 主程序，运行该文件即可启动Web服务

- application.properties: 配置文件，配置项目中的各个组件

- ApplicationTests.java: 单元测试


### 项目目录结构

- api/rest: SpringMVC Controller，数据校验，并调用service提供服务

- config: 配置类

- entity: JPA的实体Bean

- repository: SpringData Repository，数据库操作

- service: 服务类，处理业务逻辑

- utils: 工具类



## 编码

### Entity

Entity是数据库表中的记录与Java对象的映射。它就是一个普通的JavaBean加上了一些注解。

| 注解 | 位置 | 说明 | 是否必须 |
|-----|------|------|---------|
| @Entity | 类 | 指名该类是一个Entity类 | 是 |
| @Table | 类 | 指定该类对应的数据库表名 | 否 |
| @UniqueConstraint | 类 | 指定唯一约束 | 否 |
| @Id | 属性或get方法 | 标名主键 | 是 |
| @Column/@Basic | 属性或get方法 | 列定义 | 否 |
| @Lob | 属性或get方法 | 大字段 | 否 |
| @Transient | 属性或get方法 | 忽略该字段 | 否 |
| @OneToOne | 属性或get方法 | 映射关系 | 否 |
| @OneToMany | 属性或get方法 | 映射关系 | 否 |
| @ManyToOne | 属性或get方法 | 映射关系 | 否 |

> Entity上增加`@Data`注解会在编译时（需要安装Lombok插件）自动增加get/set/toString/equals/hashCode方法，可以使代码更加简洁。

> 如果`@Data`注解的类有父类，则需要增加`@EqualsAndHashCode(callSupper = true)`

#### 表结构初始化

1. 配置`spring.jpa.hibernate.ddl-auto=update`可以自动创建更新表结构
2. 在resources根目录放置`schema.sql`或`schema-PLATFORM.sql`程序会自动在初始化时创建表（仅适用于内存数据库）

> PLATFORM为`spring.datasource.platform`中指定的值，用于区分不中的环境

#### 数据初始化

在resources根目录放置`data.sql`或`data-PLATFORM.sql`，程序会自动在初始化时执行（仅适用于内存数据库）

> PLATFORM为`spring.datasource.platform`中指定的值，用于区分不中的环境



### Repository

Repository将Entity与数据库表中的数据进行转换，在添加新记录或是更新时，将Entity转换为记录，而在查询时则将记录转换为Entity。

Repository只需要创建接口并继承`JpaRepository`即可，无须编写实现类。如果想使用Specification查询，还可以同时继承`JpaSpecificationExecutor`，后者提供了更灵活的查询。

#### CRUD

- save/batchSave

- findOne/findAll

- delete/deleteAll/deleteAllInBatch

- count

#### 语义化查询方法

| 关键字 | 示例 | JPQL片断 |
|--------|------|---------|
| And | findByLastNameAndFirstName | where x.lastName=?1 and x.firstName = ?2 |
| Or | findByLastNameOrFirstName | where x.lastName=?1 or x.firstName = ?2 |
| Is/Equals | findByFirstName, findByFirstNameIs, findByFirstNameEquals | where x.firstName=?1 |
| Between | findByBirthdayBetween | where x.birthday between ?1 and ?2 |
| LessThan | findByAgeLessThan | where x.age < ?1 |
| LessThanEqual | findByAgeLessThanEqual | where x.age <= ?1 |
| GreaterThan | findByAgeGreaterThan | where x.age > ?1 |
| GreaterThanEqual | findByAgeGreaterThanEqual | where x.age >= ?1 |
| After | findByBirthdayAfter | where x.birthday > ?1 |
| Before | findByBirthdayBefore | where x.birthday < ?1 |
| IsNull | findByAgeIsNull | where x.age is null |
| IsNotNull/NotNull | findByAgeIsNotNull, findByAgeNotNull | where x.age is not null|
| Like | findByFirstNameLike | where x.firstName like ?1 |
| NotLike | findByFirstNameNotLike | where x.firstName not like ?1 |
| StartingWith,EndingWith,Containing | findByFirstNameStartingWith | where x.firstName like ?1 (参数后加%, 参数前加%，两端加%) |
| OrderBy | findByAgeOrderByLastNameDesc | where x.age = ?1 order by x.lastName desc |
| Not | findByLastNameNot | where x.lastName <> ?1 |
| In, NotIn | findByAgeIn(Collection) | where x.age in ?1 |
| True, False | findByActiveTrue | where x.active = true |
| IgnoreCase | findByFirstNameIgnoreCase | where UPPER(x.firstName) = UPPER(?1) |


#### 自定义查询

- @Query
  ```java
  /**
   * 基于{@link Query @Query}的自定义查询
   *
   * @param parentId 上级ID
   * @param pageable 分页信息
   * @return 指定字典的所有字典项
   */
  @Query("select dict from Dict dict where parentId = ?1 order by rank desc")
  Page<Dict> customPageableQuery(Long parentId, Pageable pageable);
  ```

- @NativeQuery：不推荐使用

#### Specification查询

```java
public Page<Dict> findAll(Dict dict, Pageable pageable) {
  return dictRepository.findAll(toSpecification(dict), pageable);
}

private Specification<Dict> toSpecification(Dict dict) {
  return (root, query, builder) -> {
    List<Predicate> predicateList = new ArrayList<>();

    if (StringUtils.hasText(dict.getName())) {
      predicateList.add(builder.like(root.get("name"), "%" + dict.getName() + "%"));
    }

    if (dict.getParentId() != null) {
      predicateList.add(builder.equal(root.get("parentId"), dict.getParentId()));
    }

    return builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
  };
}

```

### Service

服务层不要求编写接口，仅有实现类即可。

#### 事务

- 增加@Transaction注解即可。

- 需要引入Atomikos或Bitronix支持分布式事务


#### 使用RestTemplate调用其它服务的API

```java
@Autowired
private RestTemplate restTemplate;

public Foo getFooFromOtherSystem(String id) {
  return restTemplate.getForObject(host + '/api/v1/foos/{id}', Foo.class, id);
}
```


#### 异步处理

要想支持异步处理，需要在主程序上增加`@EnableAsync`注解。

可以自定义一个Executor, 并在@Async中指定使用这个Executor执行这个异步任务。

```java
/**
 * 没有返回结果的异步方法
 */
@Async
public void doAnotherThingAsync() {
 waiting(3000);
}

/**
 * 有返回结果的异步方法
 *
 * <p>调用Future的get方法时，会等待异步执行结束以后再返回处理结果</p>
 */
@Async
public Future<Long> doSomethingAsyncAndReturn() {
 long waiting = secureRandom.nextInt(1000) + 2000L;

 waiting(waiting);

 return AsyncResult.forValue(waiting);
}
```

### Controller

Controller是API的最外层，负责接收请求并校验数据，最后调用service提供服务。

> 在Controller上增加`@RestController`就不用再每个方法上都加`@ResponseBody`了

> 在方法上的有更简捷的`@XxxMapping`, 如`@GetMapping`就相当于`@RequestMapping(method = HttpMethod.Get)`

#### 数据校验

在Controller上增加`@Validated`就可以支持数据校验了。在需要校验的地方写上相应的规则即可。

```java
@GetMapping("/dummy")
public String demostrateValidation(@RequestParam @Size(min = 3, max = 10, message = "必须是3到10个字符") String name,
    @RequestParam @Min(value = 10, message = "必须大于10") int number) {

  if ("black".equals(name)) {
    throw new BusinessException(ERROR_BLOCK_BY_BLACK_LIST, name);
  }

  return "Hello " + name + ", your input is " + number;
}
```

#### 异常处理

在SpringMVC中已经配置统一的异常处理机制，在程序遇到异常时，会返回相应的错误代码和信息。

在Service层和Controller层，如果有异常，直接抛出即可。

- BusinessException: 业务异常，带有相应的错误代码和描述。状态码：500

- RecordNotFoundException: 未找到请求ID所对应的记录，状态码：404

- 数据校验失败时会自动抛出异常，返回失败字段的描述信息，状态码：415



#### API文档

统一使用[Swagger](https://swagger.io/)作为API展示界面。配置时仅需要在Controller和相应的Bean上配置方法和属性说明即可。

- @Api: 接口名称

- @ApiOperation: 操作

- @ApiParam: 操作参数

- @ApiModel: JavaBean类型的操作参数

- @ApiModelProperty：JavaBean类型的参数属性


#### 跨域

UI的请求发起端如果是Javascript的话，如果被请求的站点与JS所处的站点不在一个domain下，浏览器是不允许访问的。因此需要在业务服务中配置CORS，有两种方式：

1. 在可以跨域的Controller上增加`@CrossOrigin`

2. 配置`WebMvcConfigure`
  ```java
  /**
   * 跨域设置
   *
   * <p>仅与前端(JS)交互时才需要设置</p>
   *
   * @author hotleave
   * @see org.springframework.web.bind.annotation.CrossOrigin
   */
  @Configuration
  public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
      return new CorsWebMvcConfigurerAdapter();
    }

    private static class CorsWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**");
      }
    }
  }
  ```

#### 安全

统一使用SpringSecurity进行管理。

服务间的请求通过KeyCloak的JWT实现。

UI与服务间的请求通过传统的SpringSecurity实现。

1. Filter类型：`regexMatchers("/api/v1/.+").authenticated()`

1. 注解类型: `@PreAuthorize("hasAnyRole('user')")`

### 定时任务

#### 简单定时任务

`@Scheduled`


#### 分布式定时任务

基于Quartz，使用同一个数据库作为JobStore。可以实现集群的定时任务。同时只有一个节点运行。如果正在运行的节点异常退出时，会自动选取另外一个节点运行。


### 日志

SpringBoot默认使用Logback日志系统，通过在application.yml中配置相关的信息可以定义日志的输出：

- logging.level.root: 主日志级别

- logging.level.foo.bar.biz: `foo.bar.biz`包下的日志级别

- logging.path: 日志文件保存目录，文件名为`spring.log`

- logging.file: 日志文件保存位置

- logging.config: 日志配置文件，默认会从classpath的根目录依次查找`logback-spring.xml`, `logback.xml`

> 如果想使用log4j2日志系统，则需要在pom依赖中取消`org.springframework.boot:spring-boot-starter-logging`,
> 并引入`org.springframework.boot:spring-boot-starter-log4j2`. Log4j2的配置文件默认为`log4j2-spring.xml`或`log4j2.xml`

## 单元测试

### Repository测试

```java
/**
 * Repository测试
 *
 * <p>DataJpaTest注解的单元测试会在测试结束时自动回滚， 如果你不想这样，可以使用{@code @Transactional(propagation =
 * Propagation.NOT_SUPPORTED)}禁用事务</p>
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DictRepositoryTest {
  @Autowired
  private DictRepository dictRepository;

  @Test
  public void findByParentIdOrderByRank() throws Exception {
    List<Dict> dictList = dictRepository.findByParentIdOrderByRank(1L);

    assertNotNull(dictList);
    assertEquals(2, dictList.size());
  }

}
```

### Service测试

```java
/**
 * 服务测试
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DictServiceTest {
  @Autowired
  private DictService dictService;

  @Test
  public void getDictItems() throws Exception {
    List<Dict> dictList = dictService.getDictItems(1L);

    assertNotNull(dictList);
    assertEquals(2, dictList.size());
  }
}
```

### Controller测试

```java
/**
 * 启动Web容器测试Controller
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  /**
   * 当前所使用的端口号
   */
  @LocalServerPort
  private int port;

  /**
   * exchange
   */
  @Test
  public void getDictItems() throws Exception {
    ParameterizedTypeReference<List<Dict>> responseType = new ParameterizedTypeReference<List<Dict>>() {
    };

    List<Dict> dictList = restTemplate.exchange("/api/v1/dicts/{id}/items", HttpMethod.GET, null, responseType, 1).getBody();

    Assert.assertNotNull(dictList);
    Assert.assertEquals(2, dictList.size());
  }

  /**
   * getForObject
   */
  @Test
  public void findOne() throws Exception {
    Dict dict = restTemplate.getForObject("/api/v1/dicts/{id}", Dict.class, -1);

    Assert.assertNotNull(dict);
    Assert.assertEquals("gender", dict.getCode());
  }

}
```

### Mockup

```java
/**
 * Mvc Mockup
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DictApi.class)
public class MockControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private DictService dictService;

  @Test
  public void getOne() throws Exception {
    Dict dict = new Dict();
    dict.setCode("gender");
    dict.setId(1L);
    dict.setName("性别");
    dict.setParentId(0L);
    dict.setRank(1);

    // 调用dictService.findOne(1L)时返回dict
    given(dictService.findOne(1L)).willReturn(dict);

    mvc.perform(get("/api/v1/dicts/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"id\":1,\"code\":\"gender\",\"name\":\"性别\",\"rank\":1,\"parentId\":0}"));
  }
}
```

## 打包/部署

### jar

```bash
mvn package
```

### docker

使用Maven插件建构。[官网示例](https://spring.io/guides/gs/spring-boot-docker/)

## 扩展阅读

### CDC (Consumer Driven Contract)

基于[SpringCouldContract](https://cloud.spring.io/spring-cloud-contract/)，客户端与服务端共同维护使用一套契约，服务端利用契约生成测试用例并对自身的服务进行测试。客户端利用契约生成一个MockServer，可以与服务端并行开发。

由于客户端与服务端使用的是同一套契约，因此大大降低了（如果契约定义的够完善，那么应该不再需要边界测试）各自通过Stub Mock的测试全部通过，但是一集成就全是错误的概率。

## 相关链接

- [IDE插件、Maven等配置](http://gitlab.etongdai.org/codex/project-code-guides)

- [基础编码规范](http://wiki.etongdai.org/pages/viewpage.action?pageId=983847)

- [GoobleCodeStyle](https://sealake.net/google-java-codestyle/)
