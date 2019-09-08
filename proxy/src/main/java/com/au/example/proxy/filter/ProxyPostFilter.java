package com.au.example.proxy.filter;

import com.au.example.proxy.model.Status;
import com.au.example.proxy.service.ProxyLogService;
import com.au.example.proxy.service.dto.HeaderDto;
import com.au.example.proxy.service.dto.ProxyLogDto;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.pre.Servlet30WrapperFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProxyPostFilter extends ZuulFilter {

    @NonNull
    ProxyLogService proxyLogService;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();

        try {
            InputStream in = (InputStream) context.get("requestEntity");
            context.getRequestQueryParams();


            Set<HeaderDto> headerDtos = new HashSet<>();

            HttpServletRequest httpServletRequest = context.getRequest();



            httpServletRequest.getHeaderNames().asIterator().
                    forEachRemaining(s -> headerDtos.add(HeaderDto.builder().key(s).value(httpServletRequest.getHeader(s)).build()));


            if (in == null) {
                in = context.getRequest().getInputStream();
            }
            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            body = body.toUpperCase();
            context.set("requestEntity", new ByteArrayInputStream(body.getBytes("UTF-8")));

            ProxyLogDto proxyLogDto = ProxyLogDto.builder().
                    body(body).headers(headerDtos).
                    queryString(httpServletRequest.getQueryString()).
                    requestUrl(((HttpServletRequestWrapper)context.getRequest()).getRequest().getRequestURL().toString()).build();
            proxyLogDto.setStatus(HttpStatus.valueOf(context.getResponse().getStatus()));
            proxyLogDto.setHttpMethod(HttpMethod.valueOf(httpServletRequest.getMethod()));
            proxyLogService.save(proxyLogDto);

        } catch (Exception e) {
            log.error(String.format("Unexpected error %s", e.getMessage()));
            throw new ZuulException(e.getMessage(), 404, e.getCause().getMessage());
        }
        return null;
    }

    public static String getURL(HttpServletRequest req) {

        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet
        String pathInfo = req.getPathInfo();         // /a/b;c=123
        String queryString = req.getQueryString();          // d=789

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append(servletPath);

        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }
}
