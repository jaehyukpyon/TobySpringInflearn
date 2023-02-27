package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class HelloApiTest {

    @Test
    public void helloApi() throws Exception {
        // http localhost:9090/hello?name=Spring
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> res = rest.getForEntity("http://localhost:9090/hello?name={name}", String.class, "Spring");

        // status code 200?
        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        // header(Content-Type) text/plain?
        Assertions.assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);

        // body Hello Spring?
        Assertions.assertThat(res.getBody()).isEqualTo("*Hello Spring*");
    }

}
