package com.viewfunction.infoDiscoverSystemAdminApplication;

import com.viewfunction.infoDiscoverSystemAdminApplication.util.ApplicationLauncherUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InfoDiscoverSystemAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfoDiscoverSystemAdminApplication.class, args);
		ApplicationLauncherUtil.printApplicationConsoleBanner();
	}

	@Bean
	public ServletRegistrationBean testServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new com.vaadin.server.VaadinServlet());
		registration.addInitParameter("UI","com.infoDiscover.adminCenter.ui.AdminCenterApplicationUI");
		registration.addUrlMappings("/*");
		return registration;
	}
}
