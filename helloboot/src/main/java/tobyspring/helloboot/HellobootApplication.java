package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HellobootApplication {

	public static void main(String[] args) {
		System.out.println("Hello Containerless Standalong Application");

//		new Tomcat().start();
		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		serverFactory.addConnectorCustomizers(connector -> {
			connector.setPort(9090);
		});
		WebServer webServer = serverFactory.getWebServer(new ServletContextInitializer() {
			HelloController helloController = new HelloController();

			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.addServlet("frontcontroller", new HttpServlet() {
					@Override
					protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

						if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
							String name = req.getParameter("name");

							String ret = helloController.hello(name);

							resp.setStatus(HttpStatus.OK.value());
							resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
							PrintWriter writer = resp.getWriter();
							writer.println(ret);
						} else if (req.getRequestURI().equals("/user")) {

						} else {
							resp.setStatus(HttpStatus.NOT_FOUND.value());
						}
					}
				}).addMapping("/*");
			}
		});
		webServer.start(); // Tomcat Servlet Container가 동작한다.
	}

}
