import org.atom.functions.BaseTrait;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ld
 * 自测用工具
 */
public class Tester implements BaseTrait{

    /**
     * 普通测试
     */
    public static <T> long test(Supplier<T> test) {
        long startTime = System.currentTimeMillis();
        T t = test.get();
        long time = System.currentTimeMillis() - startTime;
        System.out.println("运行时间："+ time + "ms");
        System.out.println("测试结果：起始 ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇ begin\n"+ new Tester().obj2PrettyJson(t));
        System.out.println("运行时间："+ time + "ms");
        System.out.println("测试结果：结束 ⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆ end");
        return time;
    }

    /**
     * 对上面代码的柯里化转化
     */
    public static  <T,R> Consumer<T> test(Function<T,R> test) {
        return t -> test(() -> test.apply(t));
    }
}