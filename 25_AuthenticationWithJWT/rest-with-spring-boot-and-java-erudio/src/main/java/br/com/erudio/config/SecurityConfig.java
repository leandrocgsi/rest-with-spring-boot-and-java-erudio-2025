package br.com.erudio.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.erudio.security.jwt.JwtTokenFilter;
import br.com.erudio.security.jwt.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		// Criamos um mapa para armazenar diferentes implementações de PasswordEncoder
		Map<String, PasswordEncoder> encoders = new HashMap<>();

		// Configuramos um Pbkdf2PasswordEncoder, que usa o algoritmo PBKDF2 com HmacSHA256
		// para gerar senhas seguras. Os parâmetros incluem:
		// - Salt vazio (""), que será gerado automaticamente se não for especificado.
		// - Comprimento da chave (8 bytes).
		// - Iterações (185000), que é o número de vezes que o algoritmo será aplicado.
		// - Algoritmo SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256, que especifica o algoritmo de hash seguro.
		Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000,
				SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

		// Adicionamos o Pbkdf2PasswordEncoder ao mapa de encoders com a chave "pbkdf2"
		encoders.put("pbkdf2", pbkdf2Encoder);

		// Criamos um DelegatingPasswordEncoder, que delega a codificação para um dos encoders no mapa.
		// O primeiro parâmetro ("pbkdf2") define o encoder padrão a ser usado.
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

		// Configuramos o encoder padrão para correspondência, caso nenhuma outra estratégia seja identificada.
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
		return passwordEncoder;
	}
	
    @Bean
    AuthenticationManager authenticationManagerBean(
    		AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        
        //@formatter:off
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
            		session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                    authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
							"/auth/signin",
							"/auth/refresh/**",
							"/auth/createUser",
                    		"/swagger-ui/**",
                    		"/v3/api-docs/**"
                		).permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/users").denyAll()
                )
            .cors(cors -> {})
                .build();
        //@formatter:on
    }
}