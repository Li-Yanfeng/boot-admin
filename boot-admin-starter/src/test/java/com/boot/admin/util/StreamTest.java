package com.boot.admin.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * java.util.Stream 表示能应用在一组元素上一次执行的操作序列。
 * Stream 操作分为中间操作或者最终操作两种，最终操作返回一特定类型的计算结果，
 * 而中间操作返回Stream本身，这样你就可以将多个操作依次串起来(链式编程)。
 * Stream 的创建需要指定一个数据源，比如 java.util.Collection的子类，List或者Set， Map不支持。
 * Stream的操作可以串行执行或者并行执行。
 * Stream 作为 Java 8 的一大亮点，它与 java.io 包里的 InputStream 和 OutputStream 是完全不同的概念。
 * Java 8 中的 Stream 是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种非常便利、
 * 高效的聚合操作（aggregate operation），或者大批量数据操作 (bulk data operation)。
 * Stream API 借助于同样新出现的Lambda表达式，极大的提高编程效率和程序可读性。
 * 同时它提供串行和并行两种模式进行汇聚操作
 * <p>
 * 特别注意 : 一个 Stream 只可以使用一次，多次运行会抛出异常:
 * java.lang.IllegalStateException: stream has already been operated upon or closed
 * </p>
 * <p>
 * Stream操作
 * 当把一个数据结构包装成Stream后，就要开始对里面的元素进行各类操作了。常见的操作可以归类如下。
 * <p>
 * Intermediate：中间操作
 * map (mapToInt, flatMap 等) 把 Stream中 的每一个元素，映射成另外一个元素
 * <p>
 * filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
 * <p>
 * Terminal： 最终操作
 * forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、
 * findAny、 iterator
 * <p>
 * Short-circuiting： 短路操作
 * anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
 * </p>
 *
 * @author Li Yanfeng
 */
public class StreamTest {

    /**
     * Stream的构建
     */
    @Test
    public void testBuildStream() {
        // 1.使用值构建
        Stream<String> stream = Stream.of("a", "b", "c");

        // 2. 使用数组构建
        String[] strArray = new String[]{"a", "b", "c"};
        Stream<String> stream1 = Stream.of(strArray);
        Stream<String> stream2 = Arrays.stream(strArray);

        // 3. 利用集合构建(不支持Map集合)
        List<String> list = Arrays.asList(strArray);
        Stream<String> stream3 = list.stream();
    }

    /**
     * 数值Stream的构建
     * <p>
     * 对于基本数值型，目前有三种对应的包装类型 Stream：IntStream、LongStream、DoubleStream。
     * 当然我们也可以用 Stream<Integer>、Stream<Long> 、Stream<Double>，但是 自动拆箱装箱会很耗时，所以特别为这三种基本数值型提供了对应的 Stream。
     * Java 8 中还没有提供其它基本类型数值的Stream
     */
    @Test
    public void testBuildIntStream() {
        // [1, 2, 3]
        IntStream intStream1 = IntStream.of(1, 2, 3);
        intStream1.forEach(System.out::println);

        // [1, 2]
        IntStream intStream2 = IntStream.range(1, 3);
        intStream2.forEach(System.out::println);

        // [1, 2, 3]
        IntStream intStream3 = IntStream.rangeClosed(1, 3);
        intStream3.forEach(System.out::println);
    }

    /**
     * Stream转换为其它类型
     */
    @Test
    public void testStreamConverter() {
        Stream<String> stream = Stream.of("hello", "world", "java");
        // 1. 转换为Array
        String[] strings = stream.toArray(String[]::new);

        // 2. 转换为Collection
        List<String> listCollect = stream.collect(Collectors.toList());
        Set<String> setCollect = stream.collect(Collectors.toSet());
        ArrayList<String> arrayListCollect = stream.collect(Collectors.toCollection(ArrayList::new));
        HashSet<String> hashSetCollect = stream.collect(Collectors.toCollection(HashSet::new));


        // 3. 转换为String (hello,world,java)
        String stringCollect = stream.collect(Collectors.joining(","));
    }

    /**
     *
     */
    @Test
    public void testCalcMath() {
        // 计算平方数 1：1映射
        Stream.of(1, 2, 3, 4).map(integer -> integer * integer).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");

        // stream1中的每个元素都是一个List集合对象 1对多映射 （flatMap 把 stream1 中的层级结构扁平化，就是将最底层元素抽出来放到一起，
        // 最终新的 stream2 里面已经没有 List  了，都是直接的数字。）
        Stream.of(
            Arrays.asList(1),
            Arrays.asList(2, 3),
            Arrays.asList(4)).flatMap(Collection::stream).map(integer -> integer * integer).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");


        // 用.切割，输出每个字符串
        Stream<String> stream1 = Stream.of("tom.zhang", "lucy.li", "luck.wang");
        stream1.flatMap(item -> {
            return Arrays.stream(item.split("\\."));
        }).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");

        // peek 对每个元素执行操作并返回一个新的 Stream。注意:调用peek之后,一定要有一个最终操作 peek是一个intermediate 操作
        List<String> list = Arrays.asList("test", "hello", "world", "java", "tom", "C", "javascript");
        long count = list.stream()
            .filter(item -> item.length() > 3)
            .peek(item -> System.out.println("第一次符合条件的值为: " + item))
            .filter(item -> item.length() > 4)
            .peek(item -> System.out.println("第二次符合条件的值为: " + item))
            .count();
        System.out.println("最终还剩 " + count + "个数据");
        System.out.println("----------------------------------------------------------------------------------");


        // findFirst 总是返回 Stream 的第一个元素，或者空，返回值类型：Optional
        String findFirst = list.stream().filter(item -> item.length() > 3).findFirst().orElseGet(() -> "没有找到");
        System.out.println(findFirst);
        System.out.println("----------------------------------------------------------------------------------");

        // sort 排序是一个中间操作，返回的是排序好后的Stream。如果你不指定一个自定义的Comparator则会使用默认排序。
        // 对 Stream 的排序通过 sorted 进行，它比数组的排序更强之处在于你可以首先对 Stream 进行各类 map、filter、limit、skip 甚至 distinct
        // 来减少元素数量后，再排序，这能帮助程序明显缩短执行时间。
        // 需要注意的是，排序只创建了一个排列好后的Stream，而不会影响原有的数据源，排序之后原数据list是不会被修改的：
        list.stream().sorted((o1, o2) -> o1.length() - o2.length()).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");

        // Match 匹配
        //     所有元素匹配成功才返回true 否则返回false
        boolean flag = list.stream().map(String::toUpperCase).allMatch(item -> item.startsWith("H"));
        System.out.println("所有元素都已 H 开头：" + flag);
        //     任意一个匹配成功就返回true 否则返回false
        flag = list.stream().map(String::toUpperCase).anyMatch(item -> item.startsWith("H"));
        System.out.println("有元素已 H 开头：" + flag);
        //     没有一个匹配的就返回true 否则返回false
        flag = list.stream().map(String::toUpperCase).noneMatch(item -> item.startsWith("H"));
        System.out.println("没有元素 H 开头：" + flag);
        System.out.println("----------------------------------------------------------------------------------");

        // Count 计数

        // filter 过滤

        // limit 返回 stream 中的前几个

        // skip 跳过 stream 中的前几个

        // Reduce 规约/合并
        //    这是一个最终操作，允许通过指定的函数来将stream中的多个元素规约合并为一个元素.
        //    它提供一个起始值（种子），然后依照运算规则（BinaryOperator），
        //        和前面 Stream 的第一个、第二个、第 n 个元素组合。Stream.reduce，
        //        常用的方法有average, sum, min, max, and count，返回单个的结果值，
        //        并且reduce操作每处理一个元素总是创建一个新值.
        //        从这个意义上说，字符串拼接、数值的 sum、min、max等都是特殊的 reduce。
        //        例如 Stream 的 sum 就相当于
        int sum = IntStream.range(1, 10).reduce(0, (int left, int right) -> {
            return left + right;
        });
        // 方式2
        sum = IntStream.range(1, 10).reduce(0, Integer::sum);
        System.out.println(sum);
        // 最大方式1
        int optionalInt = IntStream.range(1, 10).reduce((left, right) -> {
            return left > right ? left : right;
        }).getAsInt();
        System.out.println(optionalInt);
        // 最大方式2
        optionalInt = IntStream.range(1, 10).reduce(Math::max).getAsInt();
        System.out.println(optionalInt);
        // 字符拼接1
        System.out.println(list.stream().reduce("", (left, right) -> {
            return left + "-" + right;
        }));
        // 字符拼接2
        System.out.println(list.stream().reduce((left, right) -> left + "-" + right).get());
        // 字符拼接3
        System.out.println(list.stream().collect(Collectors.joining("-", "[", "]")));
        System.out.println("----------------------------------------------------------------------------------");

        // 单词转小写，去掉空字符,去除重复单词并排序
        List<String> list1 = Arrays.asList("Java", "hello", "world", "java", "tom", "C", "javascript");
        list1.stream().map(String::toLowerCase).filter(StrUtil::isNotBlank).distinct().sorted((o1, o2) -> o1.length() - o2.length()).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");

        // Stream.generate 通过Supplier接口，可以自己来控制Stream的生成。这种情形通常用于随机数、常量的 Stream，或者需要前后元素间维持着某种状态信息的 Stream。
        // 把 Supplier 实例传递给 Stream.generate() 生成的 Stream，由于它是无限的，在管道中，必须利用limit之类的操作限制Stream大小。可以使用此方式制造出海量的测试数据
        Stream.generate(() -> (int) (Math.random() * 100)).limit(20).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");

        // Stream.iterate : iterate 跟 reduce 操作很像，接受一个种子值，和一个 UnaryOperator（假设是 f）。
        // 然后种子值成为 Stream 的第一个元素，f(seed) 为第二个，f(f(seed)) 第三个，f(f(f(seed))) 第四个,以此类推。
        Stream.iterate(10, item -> item * 10).limit(3).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");


        // 数据分组:按照字符串的长度分组
        Map<Integer, List<String>> listMap = list.stream().collect(Collectors.groupingBy(String::length));
        listMap.forEach((key, value) -> System.out.println("key: " + key + "\tvalue:" + value));

        // 数据分块 partitioningBy 分解成两个集合
        Map<Boolean, List<String>> collect =
            list.stream().collect(Collectors.partitioningBy(item -> item.length() > 3));
        System.out.println(collect.get(true));
        System.out.println(collect.get(false));
        System.out.println("----------------------------------------------------------------------------------");

        // 转map
        list.stream().collect(Collectors.groupingBy(item -> item.toUpperCase().startsWith("J"))).forEach((key, value) -> System.out.println("key:" + key + "\tvalue:" + value));


        // 数值统计 summaryStatistics()
        DoubleSummaryStatistics doubleSummaryStatistics =
            Stream.generate(() -> (Math.random() * 100)).limit(10).sorted().mapToDouble(x -> x).summaryStatistics();
        System.out.println(doubleSummaryStatistics.toString());

    }

    /**
     * 串行 并行相亲成瘾测试
     */
    @Test
    public void testSpeed() {
        int count = 1000000;
        List list = new ArrayList(10000);
        for (int i = 0; i < count; i++) {
            list.add(IdUtil.simpleUUID());
        }

        long s0 = System.currentTimeMillis();
        long count0 = list.stream().sorted().count();
        long e0 = System.currentTimeMillis();
        System.out.println("串行生成" + count0 + "个 UUID，共耗时：" + (e0 - s0) + "毫秒");

        long s1 = System.currentTimeMillis();
        long count1 = list.parallelStream().sorted().count();
        long e1 = System.currentTimeMillis();
        System.out.println("并行生成" + count1 + "个 UUID，共耗时：" + (e1 - s1) + "毫秒");
    }
}
