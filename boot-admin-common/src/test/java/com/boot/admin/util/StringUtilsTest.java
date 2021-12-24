package com.boot.admin.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertNull;

/**
 * @author Li Yanfeng
 */
public class StringUtilsTest {

    @Test
    public void testToCamelCase() {
        assertNull(StringUtils.toCamelCase(null));
    }

    @Test
    public void testToCapitalizeCamelCase() {
        assertNull(StringUtils.toCapitalizeCamelCase(null));
        Assert.assertEquals("HelloWorld", StringUtils.toCapitalizeCamelCase("hello_world"));
    }

    @Test
    public void testToUnderScoreCase() {
        assertNull(StringUtils.toUnderScoreCase(null));
        Assert.assertEquals("hello_world", StringUtils.toUnderScoreCase("helloWorld"));
        Assert.assertEquals("\u0000\u0000", StringUtils.toUnderScoreCase("\u0000\u0000"));
        Assert.assertEquals("\u0000_a", StringUtils.toUnderScoreCase("\u0000A"));
    }

    @Test
    public void testGetIP() {
        Assert.assertEquals("127.0.0.1", IpUtils.getIp(new MockHttpServletRequest()));
    }
}
