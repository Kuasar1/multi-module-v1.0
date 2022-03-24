package mn.easybuy.customerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import mn.easybuy.customerservice.entity.Customer;
import mn.easybuy.customerservice.entity.Role;
import mn.easybuy.customerservice.repository.CustomerRepository;
import mn.easybuy.customerservice.repository.RoleRepository;
import mn.easybuy.customerservice.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OnboardService {

	private static final Logger log = LoggerFactory.getLogger(OnboardService.class);
	private final CustomerRepository customerRepository;
	private final CustomerService customerService;
	private final RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@SneakyThrows
	public ResponseEntity<String> fullOnboard(Customer customer) {
		try {
			log.info("Start fullOnboard...");
			log.debug("fullOnboard REQ:{}", customer);

			boolean exists = customerRepository.findById(customer.getId()).isPresent();
			log.debug("exists:{}",exists);
			if (!exists) {
				return new ResponseEntity<>("Temporary customer does not exist!", HttpStatus.BAD_REQUEST);
			} else {
				Customer existingUser = new Customer(); //customerRepository.getById(customer.getId());

				if (!existingUser.getStatus().equalsIgnoreCase(Status.ACTIVE.name())) {
					return new ResponseEntity<>("Customer is not active!", HttpStatus.BAD_REQUEST);
				} else {
					existingUser.setState("F");
					existingUser.setPhonePrefix(customer.getPhonePrefix());
					existingUser.setPhoneType(customer.getPhoneType());
					existingUser.setPhoneStatus(Status.ACTIVE.name());
					existingUser.setEmailType(customer.getEmailType());
					existingUser.setEmailStatus(Status.ACTIVE.name());
					existingUser.setCountry(customer.getCountry());
					existingUser.setCity(customer.getCity());
					existingUser.setDistrict(customer.getDistrict());
					existingUser.setStreet(customer.getStreet());
					existingUser.setBuilding(customer.getBuilding());
					existingUser.setFloor(customer.getFloor());
					existingUser.setDoorNumber(customer.getDoorNumber());
					existingUser.setAddressType(customer.getAddressType());
					existingUser.setAddressStatus(Status.ACTIVE.name());
					log.debug("existing user:{}",existingUser);
					customerRepository.save(existingUser);
					return new ResponseEntity<>("Successful full onboard!", HttpStatus.CREATED);
				}
			}
		} catch (Exception e) {
			log.error("Error occurred in fullOnboard: ", e);
			throw new Exception(e);
		} finally {
			log.info("End fullOnboard...");
		}
	}

	@SneakyThrows
	public ResponseEntity<String> tempoOnboard(Customer customer) {
		try {
			log.info("Start tempoOnboard...");
			log.debug("tempoOnboard REQ:{}", customer);

			ResponseEntity<Boolean> checkUserNameRes = customerService.checkCustomerByUserName(customer.getUserName());
			log.debug("checkUserNameRes:{}",checkUserNameRes);
			if (checkUserNameRes.getStatusCode() != HttpStatus.OK || ObjectUtils.isEmpty(checkUserNameRes.getBody()))
				return new ResponseEntity<>("Could not check customer by username!", HttpStatus.BAD_REQUEST);
			if (checkUserNameRes.getBody())
				return new ResponseEntity<>("Username " + customer.getUserName() + " already exists!", HttpStatus.BAD_REQUEST);

			ResponseEntity<Boolean> checkEmailRes = customerService.checkCustomerByEmail(customer.getEmail());
			log.debug("checkEmailRes:{}",checkEmailRes);
			if (checkEmailRes.getStatusCode() != HttpStatus.OK || ObjectUtils.isEmpty(checkEmailRes.getBody()))
				return new ResponseEntity<>("Could not check customer by email!", HttpStatus.BAD_REQUEST);
			if (checkEmailRes.getBody())
				return new ResponseEntity<>("Email " + customer.getEmail() + " is already used!", HttpStatus.BAD_REQUEST);

			Customer newUser = new Customer();
			newUser.setUserName(customer.getUserName());
			newUser.setFirstName(customer.getFirstName());
			newUser.setLastName(customer.getLastName());
			newUser.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
			newUser.setState("T");
			newUser.setStatus(Status.ACTIVE.name());
			newUser.setGender(customer.getGender());
			newUser.setBirthDate(customer.getBirthDate());
			newUser.setPhoneNumber(customer.getPhoneNumber());
			newUser.setEmail(customer.getEmail());

			List<Role> roles = new ArrayList<>();
			roles.add(roleRepository.findById(2).get());
			newUser.setRoles(roles);
			log.debug("new user:{}",newUser);
			customerRepository.save(newUser);
			return new ResponseEntity<>("Successful tempo onboard!", HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error occurred in tempoOnboard: ", e);
			throw new Exception(e);
		} finally {
			log.info("End tempoOnboard...");
		}

	}

}
