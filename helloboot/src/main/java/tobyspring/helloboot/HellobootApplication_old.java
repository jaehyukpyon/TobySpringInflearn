package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class HellobootApplication_old {

	public static void main(String[] args) {
		System.out.println("Hello Containerless Standalong Application");

		// Spring Container를 대표하는 인터페이스 이름: applicatioonContext
		// application을 구성하고 있는 굉장히 많은 정보 (이 안에 어떤 bean이 들어갈 것인가 부터 시작해서... etc)
		// applicationContext가 결국에는 Spring Container가 된다.
		// applicationContext 중에서 코드에 의해서 손쉽게 만들 수 있도록 만들어 진 게 GenericApplicationContext
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(SimpleHelloService.class);
		applicationContext.refresh();

		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		serverFactory.addConnectorCustomizers(connector -> {
			connector.setPort(9090);
		});

//		WebServer webServer = serverFactory.getWebServer(new ServletContextInitializer() {
//			// HelloController helloController = new HelloController();
//			@Override
//			public void onStartup(ServletContext servletContext) throws ServletException {
//				servletContext.addServlet("frontcontroller", new HttpServlet() {
//					@Override
//					protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//						if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
//							String name = req.getParameter("name");
//
//							HelloController helloController = applicationContext.getBean(HelloController.class);
//							String ret = helloController.hello(name);
//
//
//							resp.setStatus(HttpStatus.OK.value());
//							// resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
//							resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
//							PrintWriter writer = resp.getWriter();
//							writer.println(ret);
//						} else {
//							resp.setStatus(HttpStatus.NOT_FOUND.value());
//						}
//					}
//				}).addMapping("/*"); // localhost:9090/hello, /hello/user, /hello/user/test 도 다 OK 즉 /*로 하위 경로가 몇 개이든 다 받을 수 있다.
//			}
//		});

		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("dispatcherServlet", new DispatcherServlet(applicationContext)).addMapping("/*");
		});
		webServer.start(); // Tomcat Servlet Container가 동작한다.
	}

}
