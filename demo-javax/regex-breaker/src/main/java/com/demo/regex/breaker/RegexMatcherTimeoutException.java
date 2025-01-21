package com.demo.regex.breaker;

import lombok.Getter;
import lombok.Setter;

/**
 * 正则匹配超时异常
 *
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-21
 */
@Getter
@Setter
public class RegexMatcherTimeoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 匹配的正则
     */
    private final String regex;
    /**
     * 匹配的原始字符串
     */
    private final CharSequence input;

    public RegexMatcherTimeoutException(String message,
                                        String regex,
                                        CharSequence input) {
        super(message);
        this.regex = regex;
        this.input = input;
    }

}
