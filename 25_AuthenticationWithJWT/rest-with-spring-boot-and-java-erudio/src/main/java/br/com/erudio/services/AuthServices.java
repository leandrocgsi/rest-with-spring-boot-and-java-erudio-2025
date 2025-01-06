package br.com.erudio.services;

import br.com.erudio.data.dto.security.AccountCredentialsVO;
import br.com.erudio.data.dto.security.TokenVO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.model.User;
import br.com.erudio.repository.UserRepository;
import br.com.erudio.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static br.com.erudio.mapper.ObjectMapper.parseObject;

@Service
public class AuthServices {

	Logger logger = LoggerFactory.getLogger(AuthServices.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository repository;
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity<TokenVO> signin(AccountCredentialsVO credentials) {
		try {
			// Realiza a autenticação com os dados fornecidos
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					credentials.getUsername(),
					credentials.getPassword()
				)
			);

			// Recupera o usuário do banco de dados
			var user = repository.findByUsername(credentials.getUsername());
			if (user == null) {
				throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
			}

			// Gera o token de acesso
			var tokenResponse = tokenProvider.createAccessToken(
					credentials.getUsername(),
					user.getRoles()
			);

			return ResponseEntity.ok(tokenResponse);
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}
	}


	@SuppressWarnings("rawtypes")
	public ResponseEntity refreshToken(String username, String refreshToken) {
		var user = repository.findByUsername(username);
		
		var tokenResponse = new TokenVO();
		if (user != null) {
			tokenResponse = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return ResponseEntity.ok(tokenResponse);
	}


	public AccountCredentialsVO create(AccountCredentialsVO user) {
		if (user == null) throw new RequiredObjectIsNullException();

		logger.info("Creating one new User!");
		var entity = new User();
		entity.setFullName(user.getFullName());
		entity.setPassword(generateHashedPassword(user.getPassword()));
		entity.setUserName(user.getUsername());
		entity.setAccountNonExpired(true);
		entity.setAccountNonLocked(true);
		entity.setCredentialsNonExpired(true);
		entity.setEnabled(true);
		return parseObject(repository.save(entity), AccountCredentialsVO.class);
	}


	private String generateHashedPassword(String password) {
		Map<String, PasswordEncoder> encoders = new HashMap<>();

		Pbkdf2PasswordEncoder pbkdf2Encoder =
				new Pbkdf2PasswordEncoder(
						"", 8, 185000,
						Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

		return passwordEncoder.encode(password);
	}
}
