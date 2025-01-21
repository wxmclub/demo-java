# Regex 匹配超时优化

## 问题与分析

* 环境说明
  * jdk:1.8
* 由于Java正则匹配引擎是 NFA 算法(不确定型有穷自动机)，在某些情况下(`?`,`+`,`*`,`{min,max}`)有可能发生大量回溯，造成CPU飙升，甚至堆栈溢出。

## 解决方法

* 核心：重新实现`CharSequence.charAt()`方法，超时抛出异常。

```java
import com.demo.regex.breaker.RegexMatcherUtils;

public class RegexMatcherTest {

    public static void main(String[] args) {
        System.out.println("----------------");
        testMatch("x*", "xxxxxxx");

        System.out.println("----------------");
        testMatch("(x+x+)+y", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }

    static void testMatch(String regex,
                          String input) {
        long start = SystemClock.currentTimeMillis();
        Matcher matcher = RegexMatcherUtils.matcher(regex, input, 1000L);
        try {
            if (matcher.find()) {
                System.out.printf("匹配成功: cost=%s%n", (SystemClock.currentTimeMillis() - start));
            } else {
                System.out.printf("匹配失败: cost=%s%n", (SystemClock.currentTimeMillis() - start));
            }
        } catch (RegexMatcherTimeoutException e) {
            System.out.printf("匹配超时: cost=%s, message=%s, regex=%s, input=%s%n",
                    (SystemClock.currentTimeMillis() - start), e.getMessage(), e.getRegex(), e.getInput());
        }

    }

}
```

## 参考文档

* [由Java正则表达式的灾难性回溯引发的高CPU异常：java.util.regex.Pattern$Loop.match](https://blog.csdn.net/lewky_liu/article/details/102493459)
* [惊呆了，高并发下System.currentTimeMillis()竟然有这么大的问题？！！！](https://blog.csdn.net/qq_30062181/article/details/108681101)
