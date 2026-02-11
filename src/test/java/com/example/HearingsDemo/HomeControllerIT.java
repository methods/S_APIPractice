package com.example.HearingsDemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

//Simulates a real server environment: entire app context + runs on random port to avoid port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void homePage_ShouldReturnWelcomeMessage() {
        String body = this.restTemplate.getForObject("/", String.class);
        assertThat(body).contains("Hearings API is ONLINE");
    }
}
