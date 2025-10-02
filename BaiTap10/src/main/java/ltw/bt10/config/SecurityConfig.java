package ltw.bt10.config;

import ltw.bt10.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean UserDetailsService userDetailsService(UserRepository repo){
        return username -> repo.findByEmail(username).map(u ->
                org.springframework.security.core.userdetails.User
                        .withUsername(u.getEmail())
                        .password(u.getPassword())
                        .roles(u.getRoles().stream().map(r->r.getName().replace("ROLE_",""))
                                .toArray(String[]::new))
                        .build()
        ).orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/graphql"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/static/**", "/graphql").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").permitAll()
                        .successHandler(successHandler()))
                .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler successHandler(){
        return (req,res,auth)->{
            var isAdmin = auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
            res.sendRedirect(isAdmin? "/admin/home" : "/user/home");
        };
    }
}