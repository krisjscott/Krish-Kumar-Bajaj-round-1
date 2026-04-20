package com.bajaj.bajaj_qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class StartupService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"; //ANSWER 1//

    private static final String FINAL_QUERY =
            //ANSWER 3//
            "SELECT p.AMOUNT AS SALARY, " +
            "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
            "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
            "d.DEPARTMENT_NAME " +
            "FROM PAYMENTS p " +
            "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
            "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
            "WHERE DAY(p.PAYMENT_TIME) != 1 " +
            "ORDER BY p.AMOUNT DESC LIMIT 1";

    @EventListener(ApplicationReadyEvent.class) //Used event listener//
    public void onStartup() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "Krish Kumar");
            requestBody.put("regNo", "ADT23SOCB059");
            requestBody.put("email", "krishkumar6566@gmail.com");

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL, request, Map.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> body = response.getBody();
            String webhook = (String) body.get("webhook");
            String accessToken = (String) body.get("accessToken");

            System.out.println("Webhook: " + webhook);
            System.out.println("Token received, submitting answer...");

            HttpHeaders submitHeaders = new HttpHeaders();
            submitHeaders.setContentType(MediaType.APPLICATION_JSON);
            submitHeaders.set("Authorization", accessToken);

            Map<String, String> submitBody = new HashMap<>(); //ANSWER 4//
            submitBody.put("finalQuery", FINAL_QUERY);

            HttpEntity<Map<String, String>> submitRequest = new HttpEntity<>(submitBody, submitHeaders);
            ResponseEntity<String> submitResponse = restTemplate.postForEntity(
                    webhook, submitRequest, String.class);

            System.out.println("Response: " + submitResponse.getStatusCode());
            System.out.println("Body: " + submitResponse.getBody());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
