package mn.easybuy.customerservice.controller;

import lombok.RequiredArgsConstructor;
import mn.easybuy.customerservice.entity.Customer;
import mn.easybuy.customerservice.service.CustomerService;
import mn.easybuy.customerservice.service.OnboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	private final OnboardService onboardService;

	@GetMapping("/customer/{id}")
	public ResponseEntity<?> findCustomerById(@PathVariable(value = "id") Integer id) {
		return customerService.getCustomerById(id);
	}

	@PostMapping("/customer/full")
	public ResponseEntity<String> fullOnboard(@RequestBody Customer customer) {
		return onboardService.fullOnboard(customer);
	}

	@PostMapping("/customer/tempo")
	public ResponseEntity<String> tempoOnboard(@RequestBody Customer customer) {
		return onboardService.tempoOnboard(customer);
	}

	@GetMapping("/")
	public String home() {
		return "HI";
	}

	@GetMapping("/error")
	public String error() {
		return "ERROR OCCURRED";
	}

//
//	@PostMapping("/login")
//	public String login() {
//		return "<!DOCTYPE html>\n" +
//				"<html>\n" +
//				"<head>\n" +
//				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
//				"    <title> Login Page </title>\n" +
//				"    <style>\n" +
//				"Body {\n" +
//				"  font-family: Calibri, Helvetica, sans-serif;\n" +
//				"  background-color: pink;\n" +
//				"}\n" +
//				"button {\n" +
//				"       background-color: #4CAF50;\n" +
//				"       width: 100%;\n" +
//				"        color: orange;\n" +
//				"        padding: 15px;\n" +
//				"        margin: 10px 0px;\n" +
//				"        border: none;\n" +
//				"        cursor: pointer;\n" +
//				"         }\n" +
//				" form {\n" +
//				"        border: 3px solid #f1f1f1;\n" +
//				"    }\n" +
//				" input[type=text], input[type=password] {\n" +
//				"        width: 100%;\n" +
//				"        margin: 8px 0;\n" +
//				"        padding: 12px 20px;\n" +
//				"        display: inline-block;\n" +
//				"        border: 2px solid green;\n" +
//				"        box-sizing: border-box;\n" +
//				"    }\n" +
//				" button:hover {\n" +
//				"        opacity: 0.7;\n" +
//				"    }\n" +
//				"  .cancelbtn {\n" +
//				"        width: auto;\n" +
//				"        padding: 10px 18px;\n" +
//				"        margin: 10px 5px;\n" +
//				"    }\n" +
//				"\n" +
//				"\n" +
//				" .container {\n" +
//				"        padding: 25px;\n" +
//				"        background-color: lightblue;\n" +
//				"    }\n" +
//				"</style>\n" +
//				"</head>\n" +
//				"<body>\n" +
//				"<center> <h1> Student Login Form </h1> </center>\n" +
//				"<form>\n" +
//				"    <div class=\"container\">\n" +
//				"        <label>Username : </label>\n" +
//				"        <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required>\n" +
//				"        <label>Password : </label>\n" +
//				"        <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required>\n" +
//				"        <button type=\"submit\">Login</button>\n" +
//				"        <input type=\"checkbox\" checked=\"checked\"> Remember me\n" +
//				"        <button type=\"button\" class=\"cancelbtn\"> Cancel</button>\n" +
//				"        Forgot <a href=\"#\"> password? </a>\n" +
//				"    </div>\n" +
//				"</form>\n" +
//				"</body>\n" +
//				"</html>\n" +
//				"\n" +
//				"\n";
//	}

}
