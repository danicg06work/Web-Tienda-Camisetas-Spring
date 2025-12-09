package com.danielcazalilla.tiendaCamisetas.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Autowired
        DataSource dataSource;

        @Autowired
        public void configure(AuthenticationManagerBuilder amb) throws Exception {
                amb.jdbcAuthentication()
                                .dataSource(dataSource)
                                .usersByUsernameQuery(
                                                "select username, password, enabled from usuario where username = ?")
                                .authoritiesByUsernameQuery(
                                                "select u.username, r.rol as 'authority' " +
                                                                "from usuario u, rol_usuario r " +
                                                                "where u.id=r.usuario_id and username = ?");
        }

        @Bean
        BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filter(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests((requests) -> requests
                                                // Recursos estáticos
                                                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**")
                                                .permitAll()

                                                // Páginas públicas
                                                .requestMatchers("/", "/index", "/productos/**", "/ayuda", "/acerca",
                                                                "/login", "/register", "/error", "/denegado")
                                                .permitAll()

                                                // Rutas que requieren estar logueado
                                                .requestMatchers("/carro/**", "/mis-pedidos/**", "/mis-datos/**",
                                                                "/pedidos/**", "/admin/**")
                                                .authenticated())
                                .exceptionHandling((exception) -> exception.accessDeniedPage("/denegado"))
                                .formLogin((formLogin) -> formLogin
                                                .loginPage("/login")
                                                .permitAll())
                                .logout((logout) -> logout
                                                .invalidateHttpSession(true)
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .csrf((csrf) -> csrf.disable())
                                .build();
        }
}
