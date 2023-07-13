package cn.youai.xiuzhen.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.youai.basics.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * ip 地址过滤，限制非法访问
 */
@Slf4j
@Order(10011)
@WebFilter(urlPatterns = {"/*"}, filterName = "RequestIPFilter")
public class RequestIPFilter extends OncePerRequestFilter {

    @Value("${app.ip.whitelist:}")
    private Set<String> ipWhitelistSet;

    @Value("${app.ip-filter.path:}")
    private Set<String> ipFilterPath;

    @PostConstruct
    void init() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        boolean isFilterPath = isFilterPath(servletPath);
        if (isFilterPath) {
            String ip = IpUtils.getRealIp(request, HttpServletRequest::getHeader, HttpServletRequest::getRemoteAddr);
            if (CollUtil.isNotEmpty(ipWhitelistSet) && !IpUtils.isMatch(ip, ipWhitelistSet)) {
                log.error("illegal access from ip:" + ip + ", servletPath:" + servletPath, new RuntimeException("IP Address Invalid!"));
                return;
            }
        }
        filterChain.doFilter(request, httpServletResponse);
    }

    private boolean isFilterPath(String path) {
        if (CollUtil.isEmpty(ipFilterPath)) {
            return false;
        }

        for (String it : ipFilterPath) {
            if (StrUtil.startWith(path, it)) {
                return true;
            }
        }
        return false;
    }
}
