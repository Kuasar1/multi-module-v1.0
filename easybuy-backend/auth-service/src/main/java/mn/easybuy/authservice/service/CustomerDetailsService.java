package mn.easybuy.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import mn.easybuy.authservice.entity.CustomerDetails;
import mn.easybuy.customerservice.entity.Customer;
import mn.easybuy.customerservice.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);
	private final CustomerService customerService;

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try	{
			log.info("Start loadUserByUsername...");
			log.debug("loadUserByUsername REQ:{}",username);
			ResponseEntity<Boolean> checkRes = customerService.checkCustomerByUserName(username);
			log.debug("checkRes:{}",checkRes);
			if(ObjectUtils.isEmpty(checkRes.getBody()) || !checkRes.getBody())
				throw new UsernameNotFoundException("User does not exist");
			ResponseEntity<?> getUserRes = customerService.getCustomerByUserName(username);
			log.debug("getUserRes:{}",checkRes);
			if(getUserRes.getStatusCode() != HttpStatus.OK || ObjectUtils.isEmpty(getUserRes.getBody()))
				throw new UsernameNotFoundException("User not found");
			Customer customer = (Customer) getUserRes.getBody();
			log.debug("Customer:{}",customer);
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			customer.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
			return new CustomerDetails(customer.getUserName(), customer.getPassword(),authorities);
		} catch (Exception e) {
			log.error("Error occurred in loadUserByUsername: ", e);
			throw new Exception(e);
		} finally {
			log.info("End loadUserByUsername...");
		}
	}
}
