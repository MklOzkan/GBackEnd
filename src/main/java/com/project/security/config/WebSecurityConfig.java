package com.project.security.config;

import com.cossinest.homes.security.jwt.AuthEntryPointJwt;
import com.cossinest.homes.security.jwt.AuthTokenFilter;
import com.cossinest.homes.security.jwt.JwtUtils;
import com.cossinest.homes.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    /* uygulamanızın nasıl korunacağını, hangi endpointlerin erişime açık olacağını,
    hangi güvenlik kurallarının geçerli olacağını ve nasıl kimlik doğrulama işlemlerinin yapılacağını belirler */

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final JwtUtils jwtUtils;
    private final AuthTokenFilter authTokenFilter;


    // kimlik bilgilerini doğrulamak ve oturum açma işlemlerini gerçekleştirmek için kullanılan merkezi bir bileşendir.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /*Belirli URL desenlerine izin verir (AUTH_WHITE_LIST) ve bu desenlere uygun isteklerin kimlik doğrulaması gerektirmediğini belirtir.
anyRequest().authenticated() ifadesi, diğer tüm isteklerin kimlik doğrulaması gerektirdiğini belirtir.*/
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

                /*Frame Options başlığını "same origin" olarak ayarlar. Bu, yalnızca aynı kaynaktan gelen içeriklerin aynı iframe'de görüntülenmesine izin verir.*/
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        /*authenticationProvider() tarafından tanımlanan kimlik doğrulama sağlayıcısını ekler. Bu sağlayıcı, kullanıcı kimlik doğrulama işlemlerini gerçekleştirir.*/
        http.authenticationProvider(authenticationProvider());

        /*authenticationJwtTokenFilter() tarafından tanımlanan JWT kimlik doğrulama filtresini,
        UsernamePasswordAuthenticationFilter filtresinden önce ekler. Bu filtre, gelen isteklerdeki JWT tokenlarını doğrular.*/
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        /*DaoAuthenticationProvider, kullanıcı kimlik doğrulama işlemlerini yönetir.*/
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        /*UserDetailsService, kullanıcı bilgilerini veritabanından yüklemek için kullanılır.*/
        authenticationProvider.setUserDetailsService(userDetailsService);

        /*PasswordEncoder, kullanıcı şifrelerini güvenli bir şekilde hashlemek ve doğrulamak için kullanılır.*/
        authenticationProvider.setPasswordEncoder(passwordEncoder());


        return authenticationProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }

    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**", // eklenecek
            "/swagger-ui.html", // eklenecek
            "/swagger-ui/**", // eklenecek
            "/*",
            "/*/*",
            "/*/*/*",
            "/index.html",
            "/images/**",
            "/css/**",
            "/js/**",

            "/contactMessages/save",
            "/auth/login",
            "/adverts/cities",
            "/adverts/categories",

            "/contact-messages/contact-messages",
            "/auth/loginUser",
            "/adverts",
            "/cities",
            "/categories",
            "/popular/*",
            "/trySave",
            "/users/register",
            "/users/*",
            "/auth/forgot-password"

    };
}
