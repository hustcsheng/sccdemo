package scc.server.config;

import scc.server.filter.CrossDomainFilter;
import scc.server.filter.LoginFilter;
import scc.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Autowired
    IUserService IUserService;

//    @Bean
//    public FilterRegistrationBean loginFilterRegistration(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new LoginFilter(IUserService));
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setName("loginFilter");
//        registrationBean.setOrder(2);
//        return registrationBean;
//    }

    @Bean
    public FilterRegistrationBean crossDomianFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CrossDomainFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("crossDomianFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
