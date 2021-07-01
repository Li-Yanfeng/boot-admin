package org.utility.util;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.utility.util.FileUtils.*;

/**
 * @author Li Yanfeng
 */
public class FileUtilTest {

    /**
     * 转换文件
     */
    @Test
    public void testToFile() {
        long retval = toFile(new MockMultipartFile("foo", (byte[]) null)).getTotalSpace();
        assertEquals(500695072768L, retval);
    }

    /**
     * 获取文件扩展名，不带.
     */
    @Test
    public void testGetExtensionName() {
        assertEquals("foo", getExtensionName("foo"));
        assertEquals("exe", getExtensionName("bar.exe"));
    }

    /**
     * 不带扩展名的文件名
     */
    @Test
    public void testGetFileNameNoEx() {
        assertEquals("foo", getFileNameNoEx("foo"));
        assertEquals("bar", getFileNameNoEx("bar.txt"));
    }

    /**
     * 文件大小转换
     */
    @Test
    public void testGetSize() {
        assertEquals("1000B   ", getSize(1000));
        assertEquals("1.00KB   ", getSize(1024));
        assertEquals("1.00MB   ", getSize(1048576));
        assertEquals("1.00GB   ", getSize(1073741824));
    }
}
