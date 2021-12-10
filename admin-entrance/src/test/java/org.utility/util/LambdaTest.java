package org.utility.util;

import org.junit.Test;

import java.util.function.Function;

/**
 * @author Li Yanfeng
 */
public class LambdaTest {
    @Test
    public void testLambda() {

        // 调用 action1Test 方法， 因为形参 Action1 是一个函数是接口， 所以可以使用lambda,
        // Action1 类中的 run 方法，没有形参， 所以在调用时候可以不传递实参
        // Action1 类中的 run 方法，没有返回值，所以在调用时候不返回
        action1Test(() -> System.out.println("执行 class: Action1, method: run"));


        // 调用 action2Test 方法， 因为形参 Action2 是一个函数是接口， 所以可以使用lambda,
        // Action2 类中的 run 方法，形参是 String， 所以在调用时候可以传递实参 100
        // Action2 类中的 run 方法，返回值是 Integer，所以在使用时候将字符串转为 数字 + 100 返回
        action2Test("100", (str) -> Integer.parseInt(str) + 100);

        // 调用 action3Test 方法， 因为形参 Action3 是一个函数是接口， 所以可以使用lambda,
        // Action3 类中的 run 方法，形参是 Integer， 所以在调用时候可以传递实参 100
        // Action3 类中的 run 方法，返回值是 String，所以在使用时候输入的数字输出
        action3Test(100, (integer) -> "i = " + integer);
    }

    /**
     * Function 接口
     * <p>
     * 执行顺序 compose --> apply --> andThen
     * </p>
     */
    @Test
    public void testFunction() {
        /*用户注册输入一个名字tom*/
        String name = "Tom";

        /*使用用户的输入的名字创建一个对象*/
        Function<String, Action> f1 = (str) -> new Action(str);

        //注意上面的代码也可以写出这样，引用类中的构造器
        //Function<String, Student> f1 =Student::new;
        Action stu1 = f1.apply(name);
        System.out.println(stu1.getName());

        /*需求改变,使用name创建Action对象之前需要给name加一个前缀*/
        Function<String, String> before = (s) -> "name = " + s;
        //表示f1调用之前先执行before对象的方法,把before对象的方法返回结果作为f1对象方法的参数
        Action stu2 = f1.compose(before).apply("张三");
        System.out.println(stu2.getName());

        /*获得创建好的对象中的名字的长度*/
        Function<Action, Integer> after = (stu) -> stu.getName().length();
        //before先调用方法,结果作为参数传给f1来调用方法,结果再作为参数传给after,结果就是我们接收的数据
        int len = f1.compose(before).andThen(after).apply(name);
        System.out.println(len);
    }

    public void action1Test(Action1 action) {
        action.run();
    }

    public void action2Test(String str, Action2<String, Integer> action) {
        System.out.println(action.run(str));
    }

    public void action3Test(Integer integer, Action3 action) {
        System.out.println(action.run(integer));
    }
}


@FunctionalInterface
interface Action1 {
    void run();
}


@FunctionalInterface
interface Action2<T, R> {
    R run(T t);
}

@FunctionalInterface
interface Action3 {
    String run(Integer integer);
}

class Action {

    private String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
