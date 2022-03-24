package mn.easybuy.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mn.easybuy.customerservice.entity.Customer;
import mn.easybuy.customerservice.entity.Role;
import mn.easybuy.customerservice.service.CustomerService;
import mn.easybuy.customerservice.service.OnboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private static final Logger log = LoggerFactory.getLogger(OnboardService.class);
	private final CustomerService customerService;

	public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("getRefreshToken start...");
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.debug("authorizationHeader:{}",authorizationHeader);

		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try	{
				String refreshToken = authorizationHeader.substring("Bearer ".length());
				log.debug("refreshToken:{}",refreshToken);
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refreshToken);
				log.debug("decodedJWT:{}",decodedJWT);
				String username = decodedJWT.getSubject();
				Customer customer = (Customer) customerService.getCustomerByUserName(username).getBody();
				log.debug("customer:{}",customer);
				String accessToken = JWT.create()
						.withSubject(customer.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", customer.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				log.debug("new access token created!");
				HashMap<String, String> tokens = new HashMap<>();
				tokens.put("access_token",accessToken);
				tokens.put("refresh_token",refreshToken);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception e) {
				log.error("Error occurred in getRefreshToken: ",e);
				response.setHeader("error",e.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				HashMap<String, String> errors = new HashMap<>();
				errors.put("error_message",e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), errors);
			}
		} else {
			log.debug("Refresh token is missing!");
			throw new RuntimeException("Refresh token is missing!");
		}
	}
}
