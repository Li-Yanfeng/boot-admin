package com.boot.admin.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boot.admin.constant.IpConstant;
import com.boot.admin.constant.UrlConstant;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Ip工具类
 *
 * @author Li Yanfeng
 */
public class IpUtils {

    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    public static boolean ipLocal = false;
    private static File file = null;
    private static DbConfig config;
    private static final String UNKNOWN = "unknown";


    static {
        SpringContextHolder.addCallBacks(() -> {
            IpUtils.ipLocal = SpringContextHolder.getProperties("ip.local-parsing", false, Boolean.class);
            if (ipLocal) {
                /*
                 * 此文件为独享 ，不必关闭
                 */
                String path = "ip2region/ip2region.db";
                String name = "ip2region.db";
                try {
                    config = new DbConfig();
                    file = FileUtils.inputStreamToFile(new ClassPathResource(path).getInputStream(), name);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        if (ipLocal) {
            return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(UrlConstant.IP_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getLocalCityInfo(String ip) {
        try {
            DataBlock dataBlock = new DbSearcher(config, file.getPath()).binarySearch(ip);
            String region = dataBlock.getRegion();
            String address = region.replace("0|", "");
            char symbol = '|';
            if (address.charAt(address.length() - 1) == symbol) {
                address = address.substring(0, address.length() - 1);
            }
            return address.equals(IpConstant.REGION) ? "内网IP" : address;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 获取当前机器的IP
     *
     * @return /
     */
    public static String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface anInterface = interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddresses.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return "";
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }
}
