package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ComponentScan
public class HellobootApplication_old2 {

//	@Bean
//	public ServletWebServerFactory servletWebServerFactory() {
//		return new TomcatServletWebServerFactory();
//	}
//
//	@Bean
//	public DispatcherServlet dispatcherServlet() {
//		return new DispatcherServlet();
//	}

	public static void main(String[] args) {
		System.out.println("Hello Containerless Standalong Application");

		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
			@Override
			protected void onRefresh() {
				super.onRefresh();

				TomcatServletWebServerFactory serverFactory = (TomcatServletWebServerFactory) this.getBean(ServletWebServerFactory.class);
				DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
				dispatcherServlet.setApplicationContext(this);

				serverFactory.addConnectorCustomizers(connector -> {
					connector.setPort(9090);
				});

				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet", dispatcherServlet).addMapping("/*");
				});
				webServer.start(); // Tomcat Servlet Container가 동작한다.
			}
		};
		applicationContext.register(HellobootApplication_old2.class);
		applicationContext.refresh();
	}

}
