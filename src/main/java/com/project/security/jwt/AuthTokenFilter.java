package com.project.security.jwt;

import com.cossinest.homes.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
Spring Security'de her istekten önce çalışacak ve gelen isteğin kimliğini doğrulamak için bir JWT (JSON Web Token) kullanacak bir filtre oluşturur.
OncePerRequestFilter sınıfını genişleterek her HTTP isteği için sadece bir kez çalıştırılacak bir filtre tanımlamış olursunuz.
 */

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER= LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;






    //Bu metod, her HTTP isteği için çağrılır.
    //İstek başlıklarını (headers) kontrol eder ve bir JWT olup olmadığını belirler.
    //JWT varsa, bu token'ı doğrular ve kullanıcı kimliğini doğrulamak için kullanır.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            //her istekte JWT tokeni al:
            String jwt= parseJwt(request);
            //validate Jwt token:
            if (jwt!=null && jwtUtils.validateJwtToken(jwt)){
                // tokenden emaili çek:
              String email=  jwtUtils.getEmailFromJwtToken(jwt);
              //aldığımız emaili kullanarak userı UserDetails e çeviriyoruz:
              UserDetails userDetails= userDetailsService.loadUserByUsername(email);
              // servis katmanında usera email ile ulaşabilmek için yazdık:
              request.setAttribute("email" , email);
              //elimizdeki UserDetails i Security contexte gönderiyoruz, oluşturduğumuz Bu nesne, Spring Security'de kimlik doğrulama ve yetkilendirme işlemlerinde kullanılır.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities() );

                // WebAuthenticationDetailsSource kullanarak, isteğin ayrıntılarını (örneğin, IP adresi,
                // kullanılan tarayıcı vb.) bu nesneye ekler. Bu bilgiler, kullanıcının oturum açma
                // isteğinin nereden geldiği ve hangi cihaz üzerinden yapıldığı gibi ayrıntıları içerir.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                /*
                Bu kod satırı, Spring Security içindeki güvenlik bağlamına bir kimlik doğrulama nesnesi ekleyerek veya
                mevcut kimlik doğrulama nesnesini güncelleyerek, kullanıcının kimlik doğrulama ve yetkilendirme işlemlerini yönetir.
                Bu işlem, genellikle kimlik doğrulama başarılı olduğunda veya kullanıcı oturum açtığında gerçekleştirilir
                 ve ardından kullanıcı, uygulamanın yetkilendirilmiş bölgelerine erişim sağlayabilir.
                 */
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (UsernameNotFoundException e) {
            LOGGER.error("Cannot set user authentication" , e);
        }
        filterChain.doFilter(request,response);

    }

    //yardımcı metod: parseJwt()
    //Bu metod, bir HTTP isteğinin başlıklarından (headers) JWT token'ını çıkarır.
    // JWT token'ı genellikle Authorization başlığında Bearer ön eki ile gönderilir.
    // Metod, bu başlığı kontrol eder ve JWT token'ını çıkarır.
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");  // Authorization başlığını al
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {  // Başlık mevcut mu ve "Bearer " ile mi başlıyor?
            return headerAuth.substring(7);  // "Bearer " kısmını çıkararak JWT token'ını döndür
        }
        return null;  // Başlık yoksa veya doğru formatta değilse null döndür
    }
}
