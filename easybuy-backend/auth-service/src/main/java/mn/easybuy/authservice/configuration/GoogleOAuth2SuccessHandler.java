package mn.easybuy.authservice.configuration;

import mn.easybuy.customerservice.repository.CustomerRepository;
import mn.easybuy.customerservice.entity.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	CustomerRepository customerRepository;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

		String email = token.getPrincipal().getAttributes().get("email").toString();

		if (customerRepository.findUserByEmail(email).isPresent()) {

		} else {
			Customer customer = new Customer();
			Map<String, Object> credentials = token.getPrincipal().getAttributes();
			customer.setFirstName(credentials.get("given_name").toString());
			customer.setLastName(credentials.get("family_name").toString());
			customer.setEmail(email);
			customerRepository.save(customer);
		}

		redirectStrategy.sendRedirect(request, response, "/");

	}

}
