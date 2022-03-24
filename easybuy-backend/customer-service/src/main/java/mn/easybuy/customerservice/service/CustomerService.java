package mn.easybuy.customerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import mn.easybuy.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

	private final CustomerRepository customerRepository;
	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

	@SneakyThrows
	public ResponseEntity<?> getCustomerById(Integer id) {
		try {
			log.info("Start getCustomerById...");
			log.debug("getCustomerById REQ:{}",id);
			boolean exists = customerRepository.findById(id).isPresent();
			if(exists) {
				//return new ResponseEntity<>(customerRepository.getById(id), HttpStatus.OK);
				return new ResponseEntity<>("OK",HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Customer with ID " + id + " does not exist", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Error occurred in getCustomerById: ", e);
			throw new Exception(e);
		} finally {
			log.info("End getCustomerById...");
		}
	}

	@SneakyThrows
	public ResponseEntity<?> getCustomerByUserName(String username) {
		try {
			log.info("Start getCustomerByUserName...");
			log.debug("getCustomerByUserName REQ:{}",username);
			boolean exists = customerRepository.findByUserName(username).isPresent();
			if(exists) {
				return new ResponseEntity<>(customerRepository.findByUserName(username).get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Customer with username " + username + " does not exist", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Error occurred in getCustomerByUserName: ", e);
			throw new Exception(e);
		} finally {
			log.info("End getCustomerByUserName...");
		}
	}

	@SneakyThrows
	public ResponseEntity<Boolean> checkCustomerByUserName(String username) {
		try {
			log.info("Start checkCustomerByUserName...");
			log.debug("checkCustomerByUserName REQ:{}",username);
			boolean exists = customerRepository.findByUserName(username).isPresent();
			return new ResponseEntity<>(exists,HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred in checkCustomerByUserName: ", e);
			throw new Exception(e);
		} finally {
			log.info("End checkCustomerByUserName...");
		}
	}

	@SneakyThrows
	public ResponseEntity<Boolean> checkCustomerByEmail(String email) {
		try {
			log.info("Start checkCustomerByEmail...");
			log.debug("checkCustomerByEmail REQ:{}",email);
			boolean exists = customerRepository.findUserByEmail(email).isPresent();
			return new ResponseEntity<>(exists,HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred in checkCustomerByEmail: ", e);
			throw new Exception(e);
		} finally {
			log.info("End checkCustomerByEmail...");
		}
	}

}
