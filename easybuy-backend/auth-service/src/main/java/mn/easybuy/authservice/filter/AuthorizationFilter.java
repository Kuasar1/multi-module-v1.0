package mn.easybuy.authservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static java.util.Arrays.stream;


public class AuthorizationFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);
	private final ObjectMapper mapper = new ObjectMapper();

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.debug("doFilterInternal start...");

		if(request.getServletPath().equalsIgnoreCase("/login") || request.getServletPath().equalsIgnoreCase("/refresh")) {
			log.debug("doFilterInternal without checking credentials...");
			filterChain.doFilter(request,response);
		} else {
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			log.debug("authorizationHeader",authorizationHeader);

			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try	{
					String token = authorizationHeader.substring("Bearer ".length());
					log.debug("token",token);
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					log.debug("decodedJWT",decodedJWT);
					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					log.debug("username:{} | roles:{}", username, roles);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request,response);
				} catch (Exception e) {
					log.error("Error occurred in doFilterInternal: ", e);
					response.setHeader("error",e.getMessage());
					response.setStatus(HttpStatus.FORBIDDEN.value());
					HashMap<String, String> errors = new HashMap<>();
					errors.put("error_message",e.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					mapper.writeValue(response.getOutputStream(), errors);
				} finally {
					log.info("End doFilterInternal...");
				}
			} else {
				log.debug("authorizationHeader is null or token is not Bearer!");
				filterChain.doFilter(request,response);
			}
		}
	}
}
