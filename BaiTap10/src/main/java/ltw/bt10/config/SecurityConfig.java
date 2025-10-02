package ltw.bt10.config;

import ltw.bt10.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(UserRepository userRepository, AuthSuccessHandler authSuccessHandler) {
        this.userRepository = userRepository;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // supports {noop}, {bcrypt}, ...
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(u -> {
                    var authorities = u.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority(r.getName()))
                            .toList();
                    return new org.springframework.security.core.userdetails.User(
                            u.getUsername(), u.getPassword(), u.getEnabled(), true, true, true, authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder, UserDetailsService uds) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(encoder);
        p.setUserDetailsService(uds);
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/img/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(f -> f.loginPage("/login").successHandler(authSuccessHandler).permitAll())
                .logout(log -> log.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}