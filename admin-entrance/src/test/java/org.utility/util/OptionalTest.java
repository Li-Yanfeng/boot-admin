package org.utility.util;

import org.junit.Test;

import java.util.Optional;

/**
 * <p>
 * Optional.of() 传入的参数不能为null。
 * Optional.empty() 传入的参数为null。
 * Optional.ofNullable()  形参 ！= null ？of() ： empty()。
 * </p>
 *
 * @author Li Yanfeng
 */
public class OptionalTest {

    /**
     * 是否存在 , 如果值存在返回true，否则返回false。
     */
    @Test
    public void testIsPresent() {
        Optional<String> optional1 = Optional.of("Hello World");
        Optional<Object> optional2 = Optional.empty();
        Optional<Object> optional3 = Optional.ofNullable("");

        System.out.println("Optional1 " + (optional1.isPresent() ? optional1.get() : ""));
        System.out.println("Optional2 " + (optional2.isPresent() ? optional2.get() : "null"));
        System.out.println("Optional3 " + (optional3.isPresent() ? optional3.get() : "null"));
    }

    /**
     * 如果Optional实例有值则为其调用consumer，否则不做处理*
     */
    @Test
    public void testIfPresent() {
        Optional<String> optional1 = Optional.of("Hello World");
        Optional<Object> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.ofNullable("");

        optional1.ifPresent((str) -> {
            System.out.println("Optional1 str = " + str);
        });

        optional2.ifPresent((str) -> {
            System.out.println("Optional2 str = " + str);
        });

        optional3.ifPresent((str) -> {
            System.out.println("Optional3 str = " + str);
        });
    }

    /******************************************** orElse 和 orElseGet 区别********************************************/
    /**
     * orElse方法 如果有值则将其返回，否则返回指定的其它值, 每次都会执行
     */
    @Test
    public void testOrElse() {
        Optional<String> optional1 = Optional.of("Hello World");
        Optional<Object> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.ofNullable("");

        System.out.println(optional1.orElse(testOptional1()));
        System.out.println(optional2.orElse(testOptional2()));
        System.out.println(optional3.orElse(testOptional3()));
    }

    private String testOptional1() {
        System.out.println("调用【Optional1】方法");
        return "Optional1";
    }

    private String testOptional2() {
        System.out.println("调用【Optional2】方法");
        return "Optional2";
    }

    private String testOptional3() {
        System.out.println("调用【Optional3】方法");
        return "Optional3";
    }

    /**
     * orElseGet方法 如果有值则将其返回，否则返回指定的其它值， 值不为空时才会执行
     */
    @Test
    public void testOrElseGet() {
        Optional<String> optional1 = Optional.of("Hello World");
        Optional<Object> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.ofNullable(null);

        System.out.println(optional1.orElseGet(() -> {
            System.out.println("执行【Optional1】方法");
            return "Optional1";
        }));
        System.out.println(optional2.orElseGet(() -> {
            System.out.println("执行【Optional2】方法");
            return "Optional2";
        }));
        System.out.println(optional3.orElseGet(() -> {
            System.out.println("执行【Optional3】方法");
            return "Optional3";
        }));
    }
    /******************************************** orElse 和 orElseGet 区别********************************************/

    /**
     * orElseThrow方法 如果有值则将其返回，否则抛出supplier接口创建的异常
     */
    @Test
    public void testOrElseThrow() {
        Optional<String> optional1 = Optional.of("Hello World");
        Optional<Object> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.ofNullable("");

        try {
            String optionalValue = optional1.orElseThrow(() -> new Exception("Optional1 一个不为空的容器"));
            System.out.println(optionalValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Object optionalValue = optional2.orElseThrow(() -> new Exception("Optional2 一个为空的容器"));
            System.out.println(optionalValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Object optionalValue = optional3.orElseThrow(() -> new Exception("Optional3 一个可变的容器"));
            System.out.println(optionalValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 映射方法
     */
    @Test
    public void testMap() {
        Optional<String> optional1 = Optional.of("1");
        Optional<String> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.ofNullable(null);

        Optional<String> optional1Value = optional1.map(str -> "Optional1 " + str);
        System.out.println(optional1Value.get());

        Optional<String> optional2Value = optional2.map(str -> "Optional2 " + str);
        System.out.println(optional2Value.get());

        Optional<String> optional3Value = optional3.map(str -> {
            System.out.println("执行【Optional3】方法");
            return "Optional3 " + str;
        });
        System.out.println(optional3Value.get());
    }

    /**
     * flatMap与map方法类似，区别在于flatMap中的mapper返回值必须是Optional。调用结束时，flatMap不会对结果用Optional封装
     */
    @Test
    public void testFlatMap() {
        Optional<String> optional1 = Optional.of("Hello World");
        Optional<Object> optional2 = Optional.empty();

        Optional<String> optional1Value = optional1.flatMap(str -> Optional.of(str.toUpperCase()));
        System.out.println(optional1Value.get());

        Optional<Object> optional2Value = optional2.flatMap(str -> Optional.of(((String) str).toLowerCase()));
        System.out.println(optional2Value.get());
    }

    /**
     * 过滤方法
     */
    @Test
    public void testFilter() {
        Optional<String> optional1 = Optional.of("Hello World Java");
        Optional<Object> optional2 = Optional.empty();
        Optional<String> optional3 = Optional.of("Hello World Java");

        Optional<String> optional1Value = optional1.filter(str -> true);
        System.out.println(optional1Value.get());

        Optional<Object> optional2Value = optional2.filter(str -> false);
        System.out.println(optional2Value.get());

        Optional<String> optional3Value = optional3.filter(str -> str.startsWith("Hello"));
        System.out.println(optional3Value.get());
    }
}
