package com.boot.admin.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.boot.admin.exception.BadRequestException;
import com.boot.admin.exception.enums.UserErrorCode;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import static cn.hutool.core.io.FileUtil.getName;
import static com.boot.admin.util.FileUtils.getExtensionName;

/**
 * 图片 工具类
 *
 * @author Li Yanfeng
 */
public class ImageUtils extends ImgUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static final String COMPRESS_DIR = "compress";

    /**
     * 压缩图片
     * <p>
     * 1.在文件同级新建 compress 目录,压缩的文件存放在 compress 目录
     * 2.压缩后的文件与原文件名称保持一致
     * </p>
     *
     * @param sourceFile 源文件
     * @return 目标文件
     */
    public static File compress(File sourceFile) {
        String filename = getName(sourceFile);
        // 判断文件是否为图片
        String suffix = getExtensionName(filename);
        fileIsPicture(suffix);
        // 目录路径
        String compressPath = sourceFile.getParent() + File.separator + COMPRESS_DIR;
        try {
            // 目标文件
            File dest = FileUtil.touch(compressPath, filename);
            Thumbnails.of(sourceFile).scale(0.4F).outputQuality(0.4F).toFile(dest);
            return dest;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 压缩图片
     * <p>
     * 注意：需要自己关闭输入流
     * </p>
     *
     * @param inputStream 文件输入流
     * @param destPath    目标文件
     * @return 目标文件
     */
    public static File compress(InputStream inputStream, String destPath) {
        try {
            // 目标文件
            File dest = FileUtil.touch(destPath);
            Thumbnails.of(inputStream).scale(0.4F).outputQuality(0.4F).toFile(dest);
            return dest;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 压缩图片
     *
     * @param sourceFile 源文件
     * @param destPath   目标文件
     * @return 目标文件
     */
    public static File compress(File sourceFile, String destPath) {
        // 判断文件是否为图片
        String suffix = getExtensionName(destPath);
        fileIsPicture(suffix);

        BufferedInputStream bis = null;
        try {
            bis = FileUtils.getInputStream(sourceFile);
            return compress(bis, destPath);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IoUtil.close(bis);
        }
        return null;
    }

    /**
     * 压缩图片
     *
     * @param file     源文件
     * @param destPath 目标文件
     * @return 目标文件
     */
    public static File compress(MultipartFile file, String destPath) {
        // 判断文件是否为图片
        String suffix = getExtensionName(destPath);
        fileIsPicture(suffix);

        InputStream is = null;
        try {
            // 目标文件
            File dest = FileUtil.touch(destPath);
            is = file.getInputStream();
            Thumbnails.of(is).scale(0.4F).outputQuality(0.4F).toFile(dest);
            return dest;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IoUtil.close(is);
        }
        return null;
    }

    /**
     * 文件是否为图片
     *
     * @param suffix 文件后缀
     */
    private static void fileIsPicture(String suffix) {
        if (ObjectUtil.notEqual(FileUtils.IMAGE, FileUtils.getFileType(suffix))) {
            throw new BadRequestException(UserErrorCode.USER_UPLOAD_FILE_TYPE_DOES_NOT_MATCH);
        }
    }
}
