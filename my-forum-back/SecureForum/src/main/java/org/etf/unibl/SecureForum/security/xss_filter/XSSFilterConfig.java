package org.etf.unibl.SecureForum.security.xss_filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class XSSFilterConfig {

    //@Bean
    public FilterRegistrationBean<XSSFilter> xssFilterRegistration() {
        FilterRegistrationBean<XSSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XSSFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all URL patterns
        registrationBean.setName("XSS Filter");
        //registrationBean.setOrder(1); // Set the order of the filter
        return registrationBean;
    }
}
