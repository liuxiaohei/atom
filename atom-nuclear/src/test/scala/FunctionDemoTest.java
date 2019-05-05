import org.atom.functions.BaseTrait;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunctionDemoTest implements BaseTrait {

    /**
     * 最初方法只能传入和返回数据
     */
    private Integer sum(Integer x,Integer y) {
        return x + y;
    }

    /**
     * 以函数为参数的例子
     */
    private Integer sum(Integer x,Integer y, BiFunction<Integer,Integer,Integer> function) {
        return function.apply(x,y);
    }

    @Test
    public void demo1() {
        System.out.println(sum(7,2,this::sum));
        System.out.println(sum(7,2,(x,y) -> sum(x,y)));
        System.out.println(sum(7,2,(x,y) -> x + y));
    }

    /**
     * 以函数为返回值的例子
     */
    private Function<Integer,Integer> sum (Integer y) {
        return x -> sum(x,y);
    }

    /**
     * 以此作为例子科里化减少参数个数
     */
    private Function<Integer,Function<Integer,Integer>> sum() {
        return x -> y -> sum(x,y);
    }

    @Test
    public void demo2() {
        System.out.println(sum(1).apply(2));
        System.out.println(sum().apply(2).apply(3));
    }

    @Test
    public void demo3() {
        Integer a = 1;
        Integer b = 2;
        Optional.of(a)
                .map(e -> e + b) //能够使用函数中的变量
        .map(e -> {
            //a = 3;             //lamnda中的变量会被自动final修饰
            return e + 1;
        }).orElse(4);
    }

    private static final List<DemoBean> beans = Arrays.asList(
            new DemoBean("张三",23,233,65),
            new DemoBean("李四",23,180,55),
            new DemoBean("王五",33,150,75),
            new DemoBean("赵六",43,232,63),
            new DemoBean("赵钱",63,232,67),
            new DemoBean("孙李",33,213,75),
            new DemoBean("周吴",22,223,85));

    /**
     * 一个需求 要求获取所有身高大于175的人
     */
    private void demo4(List<DemoBean> list,Function<DemoBean,Integer> func,Integer yuzhi) {
        System.out.println(obj2PrettyJson(
                beans.stream().filter(e -> func.apply(e) > yuzhi).collect(Collectors.toList())
        ));
    }

    @Test
    public void demo5() {
        demo4(beans,e -> e.height,175);
    }

    @Test
    public void demo6() {
        demo4(beans,e -> e.age,25);
    }

    /**
     * 惰性求值
     */
    @Test
    public void demo7() {
        Supplier<String> lazy = () -> {
            System.out.println("执行");
            return  "执行完毕";
        };

        System.out.println("开始");
        System.out.println("第一步");
        System.out.println("第二步");
        System.out.println(lazy.get());
        System.out.println("第三步");
    }

    /**
     * 将函数放入集合
     */
    @Test
    public void demo8() {
        List<Supplier<String>> a = Arrays.asList(() -> "one",() -> "two",() -> "three");
        a.stream().map(Supplier::get).forEach(System.out::println);
        // 其实这个lambda也可以放入枚举之中的 在很多情况都是有奇效的
    }

    /**
     * 十分生硬的介绍一下小工具IntStream
     */
    @Test
    public void demo10() {
        IntStream a = IntStream.rangeClosed(1,15);
        System.out.println(obj2PrettyJson(a));
        System.out.println(a.boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(0,15).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(-1,15).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(15,1).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(0,0).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(-1,-2).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(-2,-1).boxed().collect(Collectors.toList()));
    }

    /**
     * 组合式异步编程
     */
    private Long sleep() {
        return waitFor(1000);
    }

    /**
     * 并发执行15个延时为1秒的操作
     * 方法一 并行流
     */
    @Test
    public void demo11() {
        Tester.test(() -> {
            IntStream.rangeClosed(1,15).boxed().parallel().forEach(e -> sleep());
            return 11;
        });
    }

    /**
     * 方法二 组合式异步
     */
    @Test
    public void demo12() {
        Tester.test(() -> {
            List<CompletableFuture<Long>> future
                    = IntStream.rangeClosed(1,15).boxed()
                    .map(e -> CompletableFuture.supplyAsync(this::sleep))
                    .collect(Collectors.toList());
            future.forEach(CompletableFuture::join);
            return 11;
        });
    }

    /**
     * 方法三 自定义线程池组合异步
     */
    @Test
    public void demo13() {
        Tester.test(() -> {
            List<CompletableFuture<Long>> future
                    = IntStream.rangeClosed(1,15).boxed()
                    .map(e -> CompletableFuture.supplyAsync((this::sleep)))
                    .collect(Collectors.toList());
            future.forEach(CompletableFuture::join);
            return 11;
        });
    }

    /**
     * 组合式异步可以很轻松的处理以前很复杂的问题，比如异步执行两个程序然后还要组合这两个异步程序的结果
     */
    @Test
    public void demo14() {
        Tester.test(() -> {
            CompletableFuture.supplyAsync(this::sleep)
                    .thenCombine(CompletableFuture.supplyAsync(this::sleep),
                    (a,b) -> {System.out.println(a + b);
            return 0;}).join();
            return 14;
        });
    }

    /**
     * 同时执行两个异步程序，谁更快返回谁的结果
     */
    @Test
    public void demo15() {
        Tester.test(() -> {
            CompletableFuture.supplyAsync(() -> this.waitFor(1000)).applyToEither(CompletableFuture.supplyAsync(() -> this.waitFor(2000)),e -> {
               System.out.println("结束" + e);
               return 0;
           }).join();
           return 15;
        });
    }

    /**
     * 查看系统的所有字体
     */
    @Test
    public void demon16() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        for (int i = 0; i < fontName.length; i++) {
            System.out.println(fontName[i]);
        }
    }

    /**
     * 1 组合拳
     */
    @Test
    public void demo18() {
        Optional.ofNullable(beans)
                .orElseGet(ArrayList::new)
                .stream().forEach(System.out::println);

        Optional.ofNullable(beans)
                .ifPresent(list -> list.stream().forEach(System.out::println));
    }

    /**
     * 3 收集器
     */
    @Test
    public void demo19() {
        System.out.println(obj2PrettyJson(beans.stream().collect(Collectors.groupingBy(e -> e.age))));
    }

    /**
     * 4 下游收集器：求出分组后的每个年龄的数量
     */
    @Test
    public void demo20() {
        System.out.println(obj2PrettyJson(beans.stream()
                .collect(Collectors.groupingBy(e -> e.age,Collectors.counting()))));
    }

    /**
     * 4 更下游收集器：求出分组后的每个年龄段的人列表
     */
    @Test
    public void demo21() {
        System.out.println(obj2PrettyJson(beans.stream()
                .collect(Collectors.groupingBy(e -> e.age,
                        Collectors.mapping(e -> e.name,
                                Collectors.toList())))));
    }

    /**
     * 5 比如我要获取每个人的身高
     */
    @Test
    public void demo22() {
        System.out.println(obj2PrettyJson(beans.stream()
                .collect(Collectors.toMap(e -> e.name,e -> e.height))));

        System.out.println(obj2PrettyJson(beans.stream()
                .collect(Collectors.toMap(e -> e.name,e -> e.height,(left,right) -> right))));
    }

    /**
     * 6 闭包特性
     */
    @Test
    public void demo23() {
        String str = "cm";
        System.out.println(obj2PrettyJson(beans.stream()
                .collect(Collectors.toMap(e -> e.name,e -> {
                    //str = str+1;
                    return e.height + str;}))));

        Supplier<Long> b;
        try {
            long a = 10;
            b = () -> 1 + a;
        } finally {
        }
        System.out.println(b.get());
    }

    /**
     * 递归求解斐波那契数列
     *
     * @param n 第n个斐波那数列契数
     * @return 斐波那契数列的第n个数
     */
    private static long fibonacciRecursion(long n) {
        if (n <= 1) return 1;
        return fibonacciRecursion(n - 1) + fibonacciRecursion(n - 2);
    }

    /**
     * 7 不用缓存的递归效率低下
     */
    @Test
    public void demo24 () {
        Tester.test(() -> fibonacciRecursion(47L));
    }

    static Map<Long,Long> cache = new HashMap<>();

    /**
     * 使用computeIfAbsent来优化备忘录模式
     */
    private long fibonacciMemoOpt(long n) {
        if (n <= 1) return 1;
        return cache.computeIfAbsent(n, key -> fibonacciMemoOpt(key - 1) + fibonacciMemoOpt(key - 2));
    }

    /**
     * 8
     */
    @Test
    public void demo25() {
        Tester.test(() -> fibonacciMemoOpt(47L));
        System.out.println(obj2PrettyJson(cache));
    }

    /**
     *
     */
    @Test
    public void demo26() {
        Tester.test(() -> {
            CompletableFuture future = CompletableFuture.supplyAsync(() -> waitFor(3000L));
            return future.join();
        });
    }

    /**
     * 进行转化
     */
    @Test
    public void demo27() {
        Tester.test(() ->
            CompletableFuture.supplyAsync(() -> 1)
                    .thenApply(e -> e + 3).join()
        );
    }

    /**
     * 进行消耗
     */
    @Test
    public void demo28() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> 1)
                        .thenAccept(e -> System.out.println(e + 3)).join()
        );
    }

    /**
     * 不关心上一步结果
     */
    @Test
    public void demo29() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> 1)
                        .thenRun(() -> System.out.println(3)).join()
        );
    }

    /**
     * 组合
     */
    @Test
    public void demo30() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> waitFor(1000))
                        .thenComposeAsync(e -> CompletableFuture.supplyAsync(() -> waitFor(e + 1010))).join()
        );
    }

    @Test
    public void demo31() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> waitFor(1000))
                        .runAfterBothAsync(CompletableFuture.supplyAsync(() -> waitFor(1000))
                                ,() -> System.out.println("finish")).join()
        );
    }

    /**
     * 记录结果
     */
    @Test
    public void demo32() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> {
                    throwParamError("异步");
                    return waitFor(1000);
                }).whenComplete((e,error) -> {
                    //throwParamError(error.getMessage());
                    if (nonNull(error)) {
                        System.out.println(error.getMessage());
                    }
                    if (nonNull(e)) {
                        System.out.println(e);
                    }
                    }).join()
        );
    }

    private void throwParamError(String msg) {
        throw new RuntimeException(msg);
    }

    /**
     * 异常处理 相当于 catch Exception
     */
    @Test
    public void demo33() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> {
            throwParamError("异步");
            return waitFor(1000);
        }).exceptionally(error -> {
            //throwParamError(error.getMessage());
            return 1024L;}).join()
        );
    }

    /**
     * handel处理相当于fianlly
     */
    @Test
    public void demo34() {
        Tester.test(() ->
                CompletableFuture.supplyAsync(() -> {
                    //throwParamError("异步");
                    return waitFor(1000);
                }).exceptionally(error -> {
                    //throwParamError(error.getMessage());
                    return 1024L;})
                        .handle((e,error) -> {
                            System.out.println("finally");
                            return 2 * e;
                        }).join()
        );
    }

    /**
     * orElse和orElseGet的区别
     */
    @Test
    public void demo35() {
        String a = null;
        Optional.ofNullable(null)
                .orElse(a.length());
    }

    @Test
    public void demo36() {
        String a = null;
        Optional.ofNullable(null)
                .orElseGet(() -> a.length());
    }

    /**
     * 等待若干毫秒
     */
    private long waitFor(long ms) {
        try {
            Thread.sleep(ms);
            System.out.println("等待" + ms + "ms");
        } catch (Exception e) {
            System.out.println("时间" + ms + e.getMessage());
            throw error(e.getMessage());
        }
        return ms;
    }
}
