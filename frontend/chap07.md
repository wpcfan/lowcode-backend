# 数据库

<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

- [数据库](#数据库)
  - [7.1 Repository](#71-repository)
    - [7.1.1 Spring Data JPA 的命名式查询](#711-spring-data-jpa-的命名式查询)
    - [7.1.2 Spring Data JPA 测试命名式查询](#712-spring-data-jpa-测试命名式查询)
    - [7.1.3 返回结果类型的选择](#713-返回结果类型的选择)
    - [7.1.4 使用 @Query 注解进行查询](#714-使用-query-注解进行查询)
      - [关联查询](#关联查询)
      - [作业：类目的关联查询](#作业类目的关联查询)
    - [7.1.5 Example 查询](#715-example-查询)
    - [7.1.6 Specification 查询](#716-specification-查询)
    - [7.1.7 测试 Specification](#717-测试-specification)
    - [7.1.8 Spring Data JPA 的分页支持](#718-spring-data-jpa-的分页支持)

<!-- /code_chunk_output -->

## 7.1 Repository

Spring Data JPA 的 `Repository` 是一个核心概念，它提供了一种简化数据库访问和操作的方法。`Repository` 是一个接口，它定义了用于执行基本的 CRUD（创建、读取、更新和删除）操作的方法。通过继承 `Repository` 接口，我们可以为实体类创建自定义的 `Repository，从而避免编写大量的数据访问代码。和` `EntityManager` 相比，`Repository` 更加简单易用，而且 `Repository` 也是基于 `EntityManager` 实现的。

Spring Data JPA 提供了几种预定义的 Repository 接口，它们分别提供了不同级别的功能：

- `CrudRepository`：这个接口继承自 Repository 接口，并提供了一组用于执行 CRUD 操作的方法。当你需要基本的 CRUD 功能时，可以让你的 Repository 接口继承 CrudRepository。

- `PagingAndSortingRepository`：这个接口继承自 CrudRepository 接口，并提供了用于分页和排序的方法。当你需要分页和排序功能时，可以让你的 Repository 接口继承 PagingAndSortingRepository。

- `JpaRepository`：这个接口继承自 PagingAndSortingRepository 接口，并提供了一些额外的 JPA 相关功能，如实体管理和查询。当你使用 JPA 作为持久化技术时，可以让你的 Repository 接口继承 JpaRepository。

要创建一个自定义的 Repository，你只需定义一个接口并继承相应的预定义 `Repository` 接口。例如，以下代码创建了一个用于操作 `PageLayout` 实体类的 Repository：

```java
public interface PageLayoutRepository extends JpaRepository<PageLayout, Long> {
}
```

在这个例子中，我们继承了 `JpaRepository` 接口，我们指定了实体类（PageLayout）和主键类型（Long）作为泛型参数。它提供了一组用于执行 CRUD 操作的方法，如 `save()`、`findAll()`、`findById()`、`deleteById()` 等。这些方法都是基于 `EntityManager` 实现的，所以我们不需要再编写这些方法了。

### 7.1.1 Spring Data JPA 的命名式查询

Spring Data JPA 为我们提供了一种使用命名来查询数据的方式。这种方式有点像黑魔法，直接写出方法名字，不用具体实现就可以使用了。这些方法的命名规则如下：

- `findBy`：查询方法以 `findBy` 开头，后面跟着实体类的属性名，属性名的首字母要大写。例如，`findByTitle()` 表示根据 `title` 属性来查询数据。

- `And`：查询方法中可以使用 `And` 连接多个属性，表示这些属性之间是 `AND` 关系。例如，`findByTitleAndPlatform()` 表示根据 `title` 和 `platform` 属性来查询数据。

- `Or`：查询方法中可以使用 `Or` 连接多个属性，表示这些属性之间是 `OR` 关系。例如，`findByTitleOrPlatform()` 表示根据 `title` 或 `platform` 属性来查询数据。

- `Between`：查询方法中可以使用 `Between` 来指定一个范围，表示这个属性的值在这个范围内。例如，`findBySortBetween()` 表示根据 `sort` 属性来查询 `sort` 值在某个范围内的数据。

- `LessThan`：查询方法中可以使用 `LessThan` 来指定一个值，表示这个属性的值小于这个值。例如，`findBySortLessThan()` 表示根据 `sort` 属性来查询 `sort` 值小于某个值的数据。

- `GreaterThan`：查询方法中可以使用 `GreaterThan` 来指定一个值，表示这个属性的值大于这个值。例如，`findBySortGreaterThan()` 表示根据 `sort` 属性来查询 `sort` 值大于某个值的数据。

- `IsNull`：查询方法中可以使用 `IsNull` 来指定一个值，表示这个属性的值为 `null`。例如，`findByTitleIsNull()` 表示根据 `title` 属性来查询 `title` 值为 `null` 的数据。

- `IsNotNull`：查询方法中可以使用 `IsNotNull` 来指定一个值，表示这个属性的值不为 `null`。例如，`findByTitleIsNotNull()` 表示根据 `title` 属性来查询 `title` 值不为 `null` 的数据。

- `Like`：查询方法中可以使用 `Like` 来指定一个值，表示这个属性的值匹配这个值。例如，`findByTitleLike()` 表示根据 `title` 属性来查询 `title` 值匹配某个值的数据。可以使用通配符，例如 `%value%` ，表示查询任何包含给定值的字符串

- `NotLike`：查询方法中可以使用 `NotLike` 来指定一个值，表示这个属性的值不匹配这个值。例如，`findByTitleNotLike()` 表示根据 `title` 属性来查询 `title` 值不匹配某个值的数据。

- `OrderBy`：查询方法中可以使用 `OrderBy` 来指定一个属性，表示按照这个属性来排序。例如，`findByTitleOrderBySort()` 表示根据 `title` 属性来查询数据，并按照 `sort` 属性来排序。

- `Not`：查询方法中可以使用 `Not` 来指定一个属性，表示这个属性的值不等于指定的值。例如，`findByTitleNot()` 表示根据 `title` 属性来查询 `title` 值不等于某个值的数据。

- `In`：查询方法中可以使用 `In` 来指定一个属性，表示这个属性的值在指定的集合中。例如，`findByTitleIn()` 表示根据 `title` 属性来查询 `title` 值在某个集合中的数据。

- `NotIn`：查询方法中可以使用 `NotIn` 来指定一个属性，表示这个属性的值不在指定的集合中。例如，`findByTitleNotIn()` 表示根据 `title` 属性来查询 `title` 值不在某个集合中的数据。

- `IgnoreCase`：查询方法中可以使用 `IgnoreCase` 来指定一个属性，表示这个属性的值忽略大小写。例如，`findByTitleIgnoreCase()` 表示根据 `title` 属性来查询 `title` 值忽略大小写的数据。

命名形式查询是一种非常灵活的方法，可以快速简便地定义查询。通过命名形式查询，可以避免编写大量的查询代码，提高开发效率。

除了 `findBy...` 之外，还有 `countBy...`、`deleteBy...` 等方法，用于统计和删除数据。命名的规则和 `findBy...` 一样，只是方法的返回值不同。 `countBy...` 方法返回的是 `long` 类型的数据，`deleteBy...` 方法返回的是 `void` 类型。

我们来看个具体的例子

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameLikeOrderByIdDesc(String name);
}
```

这个例子中，我们在 `ProductRepository` 中定义了一个 `findByNameLikeOrderByIdDesc()` 方法，表示根据 `name` 属性来查询 `name` 值匹配某个值的数据，并按照 `id` 属性来降序排序。

那么如果有表关联的情况呢？例如，我们要查询 `Product` 表中含有某一类别的所有商品，我们该怎么使用命名式查询呢？

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoriesId(Long id);
}
```

这个例子中，我们在 `ProductRepository` 中定义了一个 `findByCategoriesId()` 方法，前面的部分依然遵循了命名式查询的规则 : `findByCategories` , 后面的部分是 `Id` ，表示根据 `categories` 这个集合属性里面的元素的 `id` 属性来查询数据。

但需要注意的是，这种方式有时候是会有歧义的，比如在 `Product` 中我们有一个属性叫 `CategoriesId` ，那么这个时候，我们就需要使用 `findByCategories_Id()` 来表示根据 `categories` 这个集合属性里面的元素的 `id` 属性来查询数据。这个时候，我们就需要使用 `_` 来分隔实体类和属性。

### 7.1.2 Spring Data JPA 测试命名式查询

我们这里使用 `@DataJpaTest` 注解来测试 Spring Data JPA 的查询方法。`@DataJpaTest` 注解会提供一个 `TestEntityManager` 类型的 `EntityManager` 实例，用于测试数据的插入和删除。

这个 `TestEntityManager` 类型的 `EntityManager` 实例和 `EntityManager` 类型的 `EntityManager` 实例的功能是一样的，只是 `TestEntityManager` 不会对实际数据库产生真实的影响，它只会对内存中数据库进行操作。

```java
@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindByNameLikeOrderByIdDesc() {
        var product = new Product();
        product.setSku("test_sku");
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(10000));
        entityManager.persist(product);

        var product2 = new Product();
        product2.setSku("test_sku_2");
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice(BigDecimal.valueOf(10100));
        entityManager.persist(product2);

        var product3 = new Product();
        product3.setSku("test_sku_3");
        product3.setName("Another Product 3");
        product3.setDescription("Test Description 3");
        product3.setPrice(BigDecimal.valueOf(10200));
        entityManager.persist(product3);

        entityManager.flush();

        var products = productRepository.findByNameLikeOrderByIdDesc("%Test%");

        assertEquals(2, products.size());
        assertEquals("Test Product 2", products.get(0).getName());
        assertEquals("Test Description 2", products.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(10100), products.get(0).getPrice());
        assertEquals("Test Product", products.get(1).getName());
        assertEquals("Test Description", products.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(10000), products.get(1).getPrice());
    }

    @Test
    public void testFindByCategories_Id() {
        var category = new Category();
        category.setCode("cat_one");
        category.setName("Test Category");
        entityManager.persist(category);

        var product1 = new Product();
        product1.setSku("test_sku");
        product1.setName("Test Product");
        product1.setDescription("Test Description");
        product1.setPrice(BigDecimal.valueOf(10000));
        product1.getCategories().add(category);

        var product2 = new Product();
        product2.setSku("test_sku_2");
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice(BigDecimal.valueOf(10100));

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();

        var products = productRepository.findByCategoriesId(category.getId());

        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
        assertEquals("Test Description", products.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(10000), products.get(0).getPrice());
    }

}
```

在 `testFindByNameLikeOrderByIdDesc` 这个测试用例中，我们使用 `TestEntityManager` 插入了 3 条数据，然后使用 `productRepository` 的 `findByNameLikeOrderByIdDesc()` 方法来查询 `name` 属性匹配 `%Test%` 的数据，并按照 `id` 属性来降序排序。

在 `testFindByCategories_Id` 这个测试用例中，我们先插入了一条 `Category` 类型的数据，然后插入了两条 `Product` 类型的数据，其中一条数据的 `categories` 属性包含了刚刚插入的 `Category` 类型的数据，然后使用 `productRepository` 的 `findByCategoriesId()` 方法来查询 `categories` 属性中包含了某个 `Category` 类型的数据的数据。

### 7.1.3 返回结果类型的选择

在 Spring Data JPA 中，我们可以使用 `List`、`Set`、`Optional`、`Page`、`Slice`、 `Stream` 等类型来作为查询方法的返回值类型。

一般对于集合对象，我们可以使用 `List` 或者 `Set` 来作为返回值类型，但是对于分页查询，我们就需要使用 `Page` 或者 `Slice` 来作为返回值类型。

`Stream` 类型的返回值类型，是在 Java 8 中引入的，它可以用来表示一个元素序列，但是它和 `List` 或者 `Set` 不同的是，它并不会将所有的元素都加载到内存中，而是在使用的时候才会加载，这样的话，就可以避免一次性加载大量的数据到内存中，从而导致内存溢出的问题。

对于单体对象，我们可以使用 `Optional` 来作为返回值类型，这样的话，如果查询结果为空的话，就会返回一个空的 `Optional` 对象，否则就会返回一个包含查询结果的 `Optional` 对象。当然你也可以返回这个对象本身的类型，但是这样的话，如果查询结果为空的话，就会返回一个 `null` 值。

我们可以在 `ProductRepository` 中定义如下方法：

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // ...
    Stream<Product> streamByNameLikeIgnoreCaseAndCategoriesCode(String name, String code);
}
```

这个例子中，我们使用 `Stream` 作为返回值类型，然后在方法名中使用 `stream` 关键字来表示这是一个 `Stream` 类型的查询方法。这个方法查询所有商品名称包含指定参数的，且商品分类的 `code` 属性包含指定参数的商品数据。其中商品名称的匹配是忽略大小写的。然后我们在 `ProductRepositoryTests` 中添加如下测试用例：

```java
@DataJpaTest
public class ProductRepositoryTests {
    @Test
    public void testStreamByNameLikeAndCategoriesCode() throws Exception {
        var category = new Category();
        category.setCode("cat_one");
        category.setName("Test Category");
        entityManager.persist(category);

        var product = new Product();
        product.setSku("test_sku");
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(10000));
        product.getCategories().add(category);
        entityManager.persist(product);

        var product2 = new Product();
        product2.setSku("test_sku_2");
        product2.setName("Test Product 2");
        product2.setDescription("Test Description 2");
        product2.setPrice(BigDecimal.valueOf(10100));
        product2.getCategories().add(category);
        entityManager.persist(product2);

        var product3 = new Product();
        product3.setSku("test_sku_3");
        product3.setName("Another Product 3");
        product3.setDescription("Test Description 3");
        product3.setPrice(BigDecimal.valueOf(10200));
        product3.getCategories().add(category);
        entityManager.persist(product3);

        entityManager.flush();

        try (var stream = productRepository.streamByNameLikeIgnoreCaseAndCategoriesCode("%test%", "cat_one")) {
            var products = stream
                .peek(productDTO -> System.out.println(productDTO.getName()))
                .toList();
            assertEquals(2, products.size());
            assertEquals("Test Product", products.get(0).getName());
            assertEquals("Test Description", products.get(0).getDescription());
            assertEquals(BigDecimal.valueOf(10000), products.get(0).getPrice());
            assertEquals("Test Product 2", products.get(1).getName());
            assertEquals("Test Description 2", products.get(1).getDescription());
            assertEquals(BigDecimal.valueOf(10100), products.get(1).getPrice());
        }
    }
}
```

在这个测试用例中，我们使用 `try-with-resources` 语句来创建一个 `Stream` 对象，然后在 `try` 代码块中使用这个 `Stream` 对象来查询数据，最后在 `finally` 代码块中关闭这个 `Stream` 对象。和 `List` 或者 `Set` 不同的是，`Stream` 对象并不会一次性的将所有的数据都加载到内存中，而是在使用的时候才会加载。

### 7.1.4 使用 @Query 注解进行查询

除了使用方法名来定义查询方法之外，我们还可以使用 `@Query` 注解来定义查询方法。`@Query` 注解可以用来定义查询语句，也可以用来定义更新语句。这种方式可以用来定义复杂的查询语句，或者是一些特殊的查询语句，比如使用原生的 SQL 语句来查询数据。

首先来回顾需求：

> 需求 10：app 得到的布局是当前时间在该布局的生效时间段内，且该布局处于发布状态，且目标平台为 `App`，且页面类型为首页。

我们可以在 `PageLayoutRepository` 中定义如下方法，这个方法的目的是为了满足 App 端可以查询到匹配的页面布局：

```java
public interface PageLayoutRepository extends JpaRepository<PageLayout, Long> {
    /**
     * 查询所有满足条件的页面
     * 条件为：
     * 1. 当前时间在开始时间和结束时间之间
     * 2. 平台为指定平台
     * 3. 页面类型为指定页面类型
     * 4. 状态为已发布
     * <p>
     * 使用 Stream 返回，避免一次性查询所有数据，但使用的是延迟加载，
     * 所以在使用时需要使用 try-with-resources 语句，或者手动关闭
     * </p>
     * @param currentTime 当前时间
     * @param platform    平台
     * @param pageType    页面类型
     * @return 页面列表
     */
    @Query("""
    select p from PageLayout p
    where p.status = com.mooc.backend.enumerations.PageStatus.Published
    and p.startTime is not null and p.endTime is not null
    and p.startTime < ?1 and p.endTime > ?1
    and p.platform = ?2
    and p.pageType = ?3
    """)
    Stream<PageLayout> streamPublishedPage(LocalDateTime currentTime, Platform platform, PageType pageType);
}
```

在这个例子中，我们使用 `@Query` 注解来定义查询语句，然后使用 `?1`、`?2`、`?3` 等占位符来表示方法参数，这样的话，我们就可以在方法参数中使用这些占位符来表示查询语句中的参数。这样的话，我们就可以在查询语句中使用一些复杂的查询条件，而不仅仅局限于方法名中的那些查询条件。

`@Query` 注解中的语句叫做 JPQL（Java Persistence Query Language），它是一种面向对象的查询语言，它和 SQL 语言有些类似，但是它并不是直接操作数据库，而是操作实体对象。

也就是说在上面例子中 `select p from PageLayout p` 这个语句中的 `PageLayout` 并不是数据库中的表，而是实体对象。这个语句的意思是从 `PageLayout` 实体对象中查询数据，然后将查询结果封装成 `PageLayout` 实体对象的列表返回。当然转换成 SQL 语句之后确实也是从 `mooc_pages` 表中查询数据，但是这个 SQL 语句是由 JPA 自动生成的，我们并不需要关心。

由于查询的是实体对象，所以在查询条件中我们可以使用实体对象的属性，比如 `p.status`、`p.startTime`、`p.endTime`、`p.platform`、`p.pageType` 等。这些属性都是实体对象的属性，而不是数据库表的字段。这些属性的类型也不是数据库表的字段类型，而是实体对象的属性类型。

好处是我们不需要关心数据库表的字段名和字段类型，只需要关心实体对象的属性名和属性类型就可以了。比如 `p.status = com.mooc.backend.enumerations.PageStatus.Published` 这个语句中的 `com.mooc.backend.enumerations.PageStatus.Published` 是枚举类型，它并不是数据库表的字段，但是 JPA 会自动将枚举类型转换成数据库表的字段。

另外需要注意的是我们使用了多行字符串来定义 JPQL 语句，这个是 Java 13 的新特性，如果你使用的是 Java 11 或者更早的版本，那么你需要将 JPQL 语句写成一行，或者使用 `+` 运算符来连接多行字符串。

如果还是想使用原生 `SQL` 语句来查询数据，那么可以使用 `nativeQuery = true` 参数来指定使用原生 `SQL` 语句来查询数据，比如：

```java
@Query("select * from mooc_pages p where p.id = ?1", nativeQuery = true)
Optional<PageLayout> findByIdWithQuery(Long id);
```

当然一般情况下不建议使用原生 `SQL` 语句来查询数据，因为这样的话就和数据库耦合了，而且也不利于跨数据库的移植。

再来看一个例子，我们首先回顾一个需求

> 需求 4.3：运营人员可以发布布局，在发布时，需要选择布局的生效时间段，后端会校验时间段是否有重叠，如果有重叠，那么会提示运营人员，需要重新选择时间段

这个需求在数据的维度其实就是要查询指定时间段，指定平台，指定页面类型的页面布局的数量，如果数量大于 0，那么就说明有重叠的时间段，否则就没有重叠的时间段。为什么不查询布局呢？因为我们只需要知道数量，而不需要知道具体的布局。这样也会减少数据的传输量，提高性能。

我们可以在 `PageLayoutRepository` 中定义如下方法：

```java
/**
 * 计算指定时间、平台、页面类型的页面数量
 * 用于判断是否存在时间冲突的页面布局
 * @param time
 * @param platform
 * @param pageType
 * @return
 */
@Query("""
select count(p) from PageLayout p
where p.status = com.mooc.backend.enumerations.PageStatus.Published
and p.startTime is not null and p.endTime is not null
and p.startTime < ?1 and p.endTime > ?1
and p.platform = ?2
and p.pageType = ?3
""")
int countPublishedTimeConflict(LocalDateTime time, Platform platform, PageType pageType);
```

#### 关联查询

如果遇到需要关联查询的情况，比如我们需要查询指定商品类目下的所有商品，那么我们可以使用 `@Query` 注解来定义查询语句，比如：

```java
@Query("""
select p from Product p
join p.categories c
where c.id = ?1
""")
List<Product> findByCategoryId(Long categoryId);
```

这个例子中我们查询的是指定商品类目下的所有商品，所以我们首先需要查询指定商品类目，然后再查询指定商品类目下的所有商品。这个查询语句中的 `join p.categories c` 表示我们要查询商品类目，然后将商品类目和商品关联起来，这样的话我们就可以在查询条件中使用商品类目的属性了，比如 `c.id = ?1`。

有的时候我们希望把关联的对象一并查询出来，比如我们希望查询指定商品类目下的所有商品，并且将商品类目也一并查询出来，那么我们可以使用 `left join fetch` 来实现，比如：

```java
@Query("""
select p from Product p
left join fetch p.categories c
where c.id = ?1
""")
List<Product> findByCategoryIdWithCategory(Long categoryId);
```

这个例子中我们使用 `left join fetch` 来将商品类目也一并查询出来，这样的话我们就可以在查询结果中直接使用商品类目了，而不需要再去查询商品类目了。也就是说 `Product` 类中的 `categories` 属性已经被填充了，我们可以直接使用了。

#### 作业：类目的关联查询

需求：

1. 由于类目是树状结构，所以我们需要查询指定类目下的所有子类目，包括子类目的子类目，以此类推。请写出一个可以查询所有类目的方法，查询出的 `Category` 的 `children` 属性也要被填充。
2. 写一个获得根类目列表的方法，查询出的 `Category` 的 `children` 属性也要被填充。判断根类目的方法是：如果一个类目的 `parent` 属性为 `null`，那么就是根类目。

### 7.1.5 Example 查询

Spring Data Jpa 还提供了一个 `Example` 查询的功能，它可以根据实体对象的属性来查询数据，这样的话就不需要自己写 JPQL 语句了。

比如，我们如果要查询所有商品名为 `Test` 开头的所有商品。我们在下面的测试用例中首先构造了一个商品类目，然后构造了三个商品，其中两个商品的名字都是以 `Test` 开头的，另外一个商品的名字不是以 `Test` 开头的。

然后我们构造查询条件，构造的过程非常简单，就是先构造一个商品对象，然后设置商品的名字为 `Test`。最后我们使用 `ExampleMatcher` 来构造一个匹配器，然后调用 `findAll` 方法来查询数据。

```java
@Test
public void testQueryByExample() throws Exception {

    var category = new Category();
    category.setCode("cat_one");
    category.setName("Test Category");
    entityManager.persist(category);

    var product = new Product();
    product.setSku("test_sku");
    product.setName("Test Product");
    product.setDescription("Test Description");
    product.setPrice(BigDecimal.valueOf(10000));
    product.addCategory(category);
    entityManager.persist(product);

    var product2 = new Product();
    product2.setSku("test_sku_2");
    product2.setName("Test Product 2");
    product2.setDescription("Test Description 2");
    product2.setPrice(BigDecimal.valueOf(10100));
    product2.getCategories().add(category);
    entityManager.persist(product2);

    var product3 = new Product();
    product3.setSku("test_sku_3");
    product3.setName("Another Product 3");
    product3.setDescription("Test Description 3");
    product3.setPrice(BigDecimal.valueOf(10200));
    product3.getCategories().add(category);
    entityManager.persist(product3);

    entityManager.flush();

    Product productQuery = new Product();
    productQuery.setName("Test");

    ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase("name")
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith());

    Example<Product> example = Example.of(productQuery, matcher);

    var products = productRepository.findAll(example);

    assertEquals(2, products.size());
}
```

适用于 `Example` 查询的场景是查询条件比较简单的场景，如果查询条件比较复杂，那么就不适合使用 `Example` 查询了。 `Example` 也有一些限制，比如不能使用 `OR` 条件，不能使用 `LIKE` 条件，不能嵌套查询等等。

### 7.1.6 Specification 查询

`Specification` 查询是 Spring Data JPA 提供的一种基于 Criteria API 的查询方式。它允许你根据一组条件来执行查询，而无需编写复杂的查询语句。 `Specification` 查询通过创建一个 `Specification` 对象来描述查询条件，然后将其传递给 Repository 的查询方法中。

`Specification` 是一种基于类的查询方式，它的特点和优势如下

1. 灵活性：可以根据查询条件动态构建 `Specification` ，而不需要写多余的 DAO 层代码。
2. 可读性：使用 `Specification` 的查询条件是定义在单独的类中的，这样比直接写在 DAO 中的 JPQL 或 SQL 语句更加可读性。
3. 可维护性：使用 `Specification` 的查询条件是定义在单独的类中的，这样对于每一种查询条件可以单独维护，而不需要在 DAO 层对多余的代码进行维护。
4. 复用性： `Specification` 可以复用，即一个 `Specification` 可以在多个地方使用，这样可以提高代码的复用率。
5. 可测试性： `Specification` 可以独立测试，不需要依赖数据库。

要使用 `Specification` 查询，你需要执行以下步骤：

- 创建一个 `Specification` 对象，并实现 `toPredicate()` 方法来描述查询条件。
- Repository 继承 `JpaSpecificationExecutor` 接口。
- 调用 Repository 的 `findAll()` 或 `findOne()` 方法，并传入 `Specification` 对象作为参数。

由于 Specfication 是有一些学习门槛的，所以我们先来看一个简单的例子，比如我们查询所有名称中包含某一个字符串的商品类目，当然我们可以非常简单的以命名规则的方式来实现这个查询：

```java
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContaining(String name);
}
```

如果使用 `Specification` 来实现这个查询，那么我们首先需要创建一个 `Specification` 对象，然后实现 `toPredicate()` 方法来描述查询条件

```java
public class CategorySpecification implements Specification<Category> {
    final String nameContaining;

    public CategorySpecification(String nameContaining) {
        this.nameContaining = nameContaining;
    }

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get("name"), "%" + nameContaining + "%");
    }
}
```

注意在 `toPredicate()` 方法中，我们使用 `CriteriaBuilder` 来构造查询条件，这里我们使用 `like()` 方法来构造一个 `LIKE` 条件，然后使用 `root.get("name")` 来获取 `name` 属性，最后我们将这个条件返回。 `root` 对象表示查询的根对象，通过它可以获取到任意属性，这里我们使用 `root.get("name")` 来获取 `name` 属性。我们暂时没有使用 `query` 对象，表示查询对象，可以用于定义查询的结果集、排序、分页等。。

然后 `CategoryRepository` 需要继承 `JpaSpecificationExecutor` 接口，这个接口提供了 `findAll()` 和 `findOne()` 方法，这两个方法都接收一个 `Specification` 对象作为参数。

```java
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    // ...
}
```

在调用的时候，就可以使用 `CategorySpecification` 来构造查询条件了

```java
@Test
public void testSpecification() throws Exception {

    var category = new Category();
    category.setCode("cat_one");
    category.setName("Test Category");
    entityManager.persist(category);

    var category2 = new Category();
    category2.setCode("cat_two");
    category2.setName("Another Category");
    entityManager.persist(category2);

    entityManager.flush();

    var spec = new CategorySpecification("Test");

    var categories = categoryRepository.findAll(spec);

    assertEquals(1, categories.size());
}
```

那么问题来了，为什么我们可以靠一个简单的命名规则实现的查询，却要使用 `Specification` 来实现呢？这是因为 `Specification` 的优势在于它的灵活性，比如我们可以根据不同的条件来构造不同的 `Specification` 对象，然后传入 `findAll()` 方法中，这样就可以实现动态查询了。

还要说明的一点是 `Specification` 接口只有一个方法，所以我们可以使用 Lambda 表达式来简化代码，比如上面的例子可以简化为

```java
@Test
public void testSpecification() throws Exception {

    var category = new Category();
    category.setCode("cat_one");
    category.setName("Test Category");
    entityManager.persist(category);

    var category2 = new Category();
    category2.setCode("cat_two");
    category2.setName("Another Category");
    entityManager.persist(category2);

    entityManager.flush();
    // 就不需要 CategorySpecification 这个类了
    var spec = (Specification<Category>) (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%Test%");

    var categories = categoryRepository.findAll(spec);

    assertEquals(1, categories.size());
}
```

当然这个简单的查询体现不出 `Specification` 的优势，我们来看一个稍微复杂一点的例子，比如这个需求：

- 需求 8：运营人员可以按条件查询布局，查询条件包括
  - 需求 8.1：布局名称含有输入字符
  - 需求 8.2：选择布局状态：草稿、已发布、已下线
  - 需求 8.3：选择布局生效起始时间段，比如查询所有生效起始时间在从 2020-01-01 00:00:00 到 2020-01-02 00:00:00 之间的布局
  - 需求 8.4：选择布局生效结束时间段，比如查询所有生效结束时间在从 2020-01-01 00:00:00 到 2020-01-02 00:00:00 之间的布局
  - 需求 8.5：选择平台 App / Web ，默认是 App，其中 web 做为扩展需求
  - 需求 8.6：选择布局的目标对象，比如 Home / Category / About 等等，这个是扩展需求

这显然是一个动态查询，从结果来说我们希望构建一个查询语句，类似于

```sql
select * from page_layout
where title like '%首页%'
and status = 'PUBLISHED'
and start_date between '2020-01-01 00:00:00' and '2020-01-02 00:00:00'
and end_date between '2020-01-01 00:00:00' and '2020-01-02 00:00:00'
and platform = 'APP';
```

当然根据不同条件，查询语句可能不同，比如如果没有选择生效起始时间段，那么查询语句就不需要包含 `start_date between '2020-01-01 00:00:00' and '2020-01-02 00:00:00'` 这个条件。也就是 `where` 后面的条件是动态的，我们需要根据不同的条件来构建不同的查询语句。

对于这样的动态查询语句，我们使用简单的命名规则是无法实现的，因为我们无法预知用户会选择哪些条件，也就无法预知查询语句的具体内容。如果使用 `@Query` 注解，那么要么我们得利用 SQL 语句拼接的方式来构建查询语句，要么就得写很多的 `@Query` 注解，这样的代码可读性和可维护性都很差。这时候 `Specification` 就可以发挥它的优势了。

我们首先构建一个查询对象，用于接收查询条件

```java
public record PageFilter(
        String title,
        Platform platform,
        PageType pageType,
        PageStatus status,
        LocalDateTime startDateFrom,
        LocalDateTime startDateTo,
        LocalDateTime endDateFrom,
        LocalDateTime endDateTo) {
}
```

然后，以这个 `PageFilter` 对象为参数，构建一个 `Specification` 对象，用于描述查询条件

```java
public class PageSpecs {
    /**
     * 用于构造动态查询条件
     * 1. 通过 PageFilter 对象获取查询条件
     * 2. 通过 builder 构造查询条件
     * 3. 通过 query 构造最终的查询语句
     * 4. 返回查询语句
     * <p>
     * 通过 Function 接口，将 PageFilter 对象转换为 Specification 对象
     * 通过 Specification 对象，可以构造动态查询条件
     */
    public static Function<PageFilter, Specification<PageLayout>> pageSpec = (filter) -> (root, query, builder) -> {
        // root: 代表查询的实体类
        // query: 查询语句
        // builder: 构造查询条件的工具
        List<Predicate> predicates = new ArrayList<>();
        if (filter.title() != null) {
            predicates.add(builder.like(builder.lower(root.get("title")), "%" + filter.title().toLowerCase() + "%"));
        }
        if (filter.platform() != null) {
            predicates.add(builder.equal(root.get("platform"), filter.platform()));
        }
        if (filter.pageType() != null) {
            predicates.add(builder.equal(root.get("pageType"), filter.pageType()));
        }
        if (filter.status() != null) {
            predicates.add(builder.equal(root.get("status"), filter.status()));
        }
        if (filter.startDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("startTime"), filter.startDateFrom()));
        }
        if (filter.startDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("startTime"), filter.startDateTo()));
        }
        if (filter.endDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("endTime"), filter.endDateFrom()));
        }
        if (filter.endDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("endTime"), filter.endDateTo()));
        }
        // 使用 builder.and() 方法，将所有的查询条件组合起来
        // 使用 query.where() 方法，将组合好的查询条件设置到查询语句中
        return query.where(builder.and(predicates.toArray(new Predicate[0])))
                .orderBy(builder.desc(root.get("id")))
                .getRestriction();
    };
}
```

上面的代码中，我们使用了高阶函数的思想，将 `PageFilter` 对象转换为 `Specification` 对象，然后在 `Specification` 对象中构造查询条件。

我们根据 `filter` 对象中的属性，构造了一个 `Predicate` 对象的集合，然后使用 `builder.and()` 方法将这些 `Predicate` 对象组合起来，最后使用 `query.where()` 方法将组合好的查询条件设置到查询语句中。

### 7.1.7 测试 Specification

我们在 `PageLayoutRepositoryTests` 类中，添加一个测试方法，用于测试 `Specification` 对象

```java
@DataJpaTest
public class PageLayoutRepositoryTests {
    @Autowired
    private PageLayoutRepository pageLayoutRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testPageSpecQuery() {
        var now = LocalDateTime.now();
        var pageConfig = PageConfig.builder()
                .baselineScreenWidth(375.0)
                .horizontalPadding(16.0)
                .build();

        var page1 = PageLayout.builder()
                .pageType(PageType.About)
                .platform(Platform.App)
                .status(PageStatus.Published)
                .config(pageConfig)
                .title("Test Page_1")
                .startTime(now.minusDays(1))
                .endTime(now.plusDays(1))
                .build();

        var page2 = PageLayout.builder()
                .pageType(PageType.About)
                .platform(Platform.App)
                .status(PageStatus.Published)
                .config(pageConfig)
                .title("Page 2")
                .startTime(now.minusMinutes(59))
                .endTime(now.plusMinutes(59))
                .build();

        var page3 = PageLayout.builder()
                .pageType(PageType.About)
                .platform(Platform.App)
                .status(PageStatus.Draft)
                .config(pageConfig)
                .title("Page 3")
                .startTime(now.minusMinutes(59))
                .endTime(now.plusMinutes(59))
                .build();

        var page4 = PageLayout.builder()
                .pageType(PageType.About)
                .platform(Platform.Web)
                .status(PageStatus.Published)
                .config(pageConfig)
                .title("Page 4")
                .startTime(now.minusMinutes(59))
                .endTime(now.plusMinutes(59))
                .build();

        var page5 = PageLayout.builder()
                .pageType(PageType.About)
                .platform(Platform.App)
                .status(PageStatus.Published)
                .config(pageConfig)
                .title("Page 5")
                .startTime(now.minusMinutes(59))
                .endTime(now.plusMinutes(59))
                .build();

        entityManager.persist(page1);
        entityManager.persist(page2);
        entityManager.persist(page3);
        entityManager.persist(page4);
        entityManager.persist(page5);

        entityManager.flush();

        var pageFilter = new PageFilter(
                "Test",
                Platform.App,
                PageType.About,
                PageStatus.Published,
                now.minusDays(2),
                now,
                now,
                now.plusDays(2)
        );
        var spec = PageSpecs.pageSpec.apply(pageFilter);

        var pages = pageLayoutRepository.findAll(spec);
        assertEquals(1, pages.size());
        assertEquals(page1.getId(), pages.get(0).getId());

        pageFilter = new PageFilter(
                null,
                Platform.App,
                PageType.About,
                PageStatus.Published,
                null,
                null,
                null,
                null
        );
        spec = PageSpecs.pageSpec.apply(pageFilter);

        pages = pageLayoutRepository.findAll(spec);
        assertEquals(3, pages.size());
    }
}
```

在上面的代码中，我们使用 `PageFilter` 对象构造了一个 `Specification` 对象，然后使用 `findAll()` 方法查询数据。我们分别测试了两种情况：

- 第一种情况，查询条件中包含了 `title`、`platform`、`pageType`、`status`、`startDateFrom`、`startDateTo`、`endDateFrom`、`endDateTo` 这些属性

- 第二种情况，查询条件中只包含了 `platform`、`pageType`、`status` 这三个属性

当然这个测试用例如果想测完整，需要测试更多的情况，这里只是为了演示 `Specification` 的用法。大家可以自行扩展测试用例。

### 7.1.8 Spring Data JPA 的分页支持

Spring Data JPA 提供了一个 `Pageable` 接口，它可以帮助我们进行分页查询。我们可以通过 `PageRequest.of()` 方法来创建一个 `Pageable` 对象，它有两个参数，第一个参数是页码，第二个参数是每页的大小。

```java
@Test
void testPageableQuery() {
    var category = new Category();
    category.setCode("cat_one");
    category.setName("Test Category");
    entityManager.persist(category);

    var product = new Product();
    product.setSku("test_sku");
    product.setName("Test Product");
    product.setDescription("Test Description");
    product.setPrice(BigDecimal.valueOf(10000));
    product.addCategory(category);
    entityManager.persist(product);

    var product2 = new Product();
    product2.setSku("test_sku_2");
    product2.setName("Test Product 2");
    product2.setDescription("Test Description 2");
    product2.setPrice(BigDecimal.valueOf(10100));
    product2.getCategories().add(category);
    entityManager.persist(product2);

    var product3 = new Product();
    product3.setSku("test_sku_3");
    product3.setName("Another Product 3");
    product3.setDescription("Test Description 3");
    product3.setPrice(BigDecimal.valueOf(10200));
    product3.getCategories().add(category);
    entityManager.persist(product3);

    entityManager.flush();

    var pageable = PageRequest.of(0, 2, Sort.by("name").descending());
    var products = productRepository.findAll(pageable);

    assertEquals(2, products.getNumberOfElements());
    assertEquals(3, products.getTotalElements());
    assertEquals(2, products.getTotalPages());
    assertEquals("Test Product 2", products.getContent().get(0).getName());
    assertEquals("Test Product", products.getContent().get(1).getName());
}
```

在上面的代码中，我们使用 `PageRequest.of()` 方法创建了一个 `Pageable` 对象，然后使用 `findAll()` 方法查询数据。

返回的 `Page<T>` 对象提供了一些有用的方法，比如获取总页数、总记录数、当前页的记录数等。

如果返回是 `Page<T>` 的时候，其实是要执行两次查询，第一次查询是查询总记录数，第二次查询是查询当前页的数据。如果我们只需要查询当前页的数据，可以使用 `Slice<T>` 对象，它只会执行一次查询。

`Page<T>` 的常用属性如下：

- `int getTotalPages()`：获取总页数
- `long getTotalElements()`：获取总记录数
- `int getNumber()`：获取当前页码
- `int getNumberOfElements()`：获取当前页的记录数
- `List<T> getContent()`：获取当前页的数据
- `boolean hasContent()`：判断当前页是否有数据
- `boolean isFirst()`：判断当前页是否是第一页
- `boolean isLast()`：判断当前页是否是最后一页
- `boolean hasNext()`：判断是否有下一页
- `boolean hasPrevious()`：判断是否有上一页

`Slice<T>` 的常用属性如下：

- `int getNumber()`：获取当前页码
- `int getNumberOfElements()`：获取当前页的记录数
- `List<T> getContent()`：获取当前页的数据
- `boolean hasContent()`：判断当前页是否有数据
- `boolean isFirst()`：判断当前页是否是第一页
- `boolean isLast()`：判断当前页是否是最后一页
- `boolean hasNext()`：判断是否有下一页
- `boolean hasPrevious()`：判断是否有上一页

除了 `findAll` ，在其他的命名查询、 `@Query` 注解查询以及 `Specification` 查询方法中，我们也可以使用 `Pageable` 对象，比如：

```java
Page<Product> findByCategories(Category category, Pageable pageable);

@Query("select p from Product p where p.name like %:name%")
Page<Product> findByName(@Param("name") String name, Pageable pageable);

Page<Product> findAll(Specification<Product> spec, Pageable pageable);
```