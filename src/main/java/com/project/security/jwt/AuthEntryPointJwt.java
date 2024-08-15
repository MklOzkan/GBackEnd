package com.project.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// bu sınıf olası kimlik doğrulama hataları meydana geldiğinde çalışacak
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // hata mesajı bir log dosyasına veya konsola kaydedilir.
        LOGGER.error("Unauthorize error: {}" , authException.getMessage());
        // response türünü JSON olarak ayarladık
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //Http Status 401 olacak yani Unauthorized gelecek
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //kimlik doğrulama hatası oluştuğunda oluşturulacak JSON formatındaki hata yanıtını hazırlamak için aşağıdakini yazdık:
        // alınacak unauthorized hatası daha anlaşılır döner:

        final Map<String ,Object> body= new HashMap<>();
        body.put("status" , HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message" , authException.getMessage());
        body.put("path" , request.getServletPath()); // hatanın meydana geldiği pathi gösterir

        //oluşturduğumuz Map yapıyı response olarak dönelim:

        final ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), body);

    }
}
