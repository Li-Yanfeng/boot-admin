package com.boot.admin.util;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Li Yanfeng
 */
public class FileUtilTest {

    /**
     * 转换文件
     */
    @Test
    public void testToFile() {
        long retval = FileUtils.toFile(new MockMultipartFile("foo", (byte[]) null)).getTotalSpace();
        assertEquals(500695072768L, retval);
    }

    /**
     * 获取文件扩展名，不带.
     */
    @Test
    public void testGetExtensionName() {
        Assertions.assertEquals("foo", FileUtils.getExtensionName("foo"));
        Assertions.assertEquals("exe", FileUtils.getExtensionName("bar.exe"));
    }

    /**
     * 不带扩展名的文件名
     */
    @Test
    public void testGetFileNameNoEx() {
        Assertions.assertEquals("foo", FileUtils.getFileNameNoEx("foo"));
        Assertions.assertEquals("bar", FileUtils.getFileNameNoEx("bar.txt"));
    }

    /**
     * 文件大小转换
     */
    @Test
    public void testGetSize() {
        Assertions.assertEquals("1000B   ", FileUtils.getSize(1000));
        Assertions.assertEquals("1.00KB   ", FileUtils.getSize(1024));
        Assertions.assertEquals("1.00MB   ", FileUtils.getSize(1048576));
        Assertions.assertEquals("1.00GB   ", FileUtils.getSize(1073741824));
    }
}
