package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class HellobootApplication_old1 {

	@Bean
	public HelloController helloController(HelloService helloService) {
		return new HelloController(helloService);
	}

	@Bean
	public HelloService helloService() {
		return new SimpleHelloService();
	}

	public static void main(String[] args) {
		System.out.println("Hello Containerless Standalong Application");

		// Spring Container를 대표하는 인터페이스 이름: applicatioonContext
		// application을 구성하고 있는 굉장히 많은 정보 (이 안에 어떤 bean이 들어갈 것인가 부터 시작해서... etc)
		// applicationContext가 결국에는 Spring Container가 된다.
		// applicationContext 중에서 코드에 의해서 손쉽게 만들 수 있도록 만들어 진 게 GenericApplicationContext
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
			@Override
			protected void onRefresh() {
				super.onRefresh();

				TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				serverFactory.addConnectorCustomizers(connector -> {
					connector.setPort(9090);
				});

				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet", new DispatcherServlet(this)).addMapping("/*");
				});
				webServer.start(); // Tomcat Servlet Container가 동작한다.
			}
		};
		applicationContext.register(HellobootApplication_old1.class);
		applicationContext.refresh();
	}

}
