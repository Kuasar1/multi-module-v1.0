package mn.easybuy.customerservice.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	//@NotEmpty
	@Column(nullable = false,unique = true)
	private String userName;

	//@NotEmpty
	@Column(nullable = false)
	private String firstName;

	private String lastName;

	//@NotEmpty
	@Column(nullable = false)
	private String password;

	//@NotEmpty
	@Column(nullable = false)
	private String state;

	//@NotEmpty
	@Column(nullable = false)
	public String status;

	private String gender;
	private String birthDate;

 	//@NotEmpty
	@Column(nullable = false)
	private String phoneNumber;

	//@NotEmpty
	@Column(nullable = false, unique = true)
	//@Email(message = "{errors.invalid_email}")
	private String email;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "customers_roles",
			joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
	private List<Role> roles;

	//Full onboard customer fields
	private String phonePrefix;
	private String phoneType;
	private String phoneStatus;
	private String emailType;
	private String emailStatus;
	private String country;
	private String city;
	private String district;
	private String street;
	private String building;
	private String floor;
	private String doorNumber;
	private String addressType;
	private String addressStatus;

	public Customer(Customer customer) {
		this.userName = customer.getUserName();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.gender = customer.getGender();
		this.birthDate = customer.getBirthDate();
		this.password = customer.getPassword();
		this.state = customer.getState();
		this.status = customer.getStatus();
		this.phoneNumber = customer.getPhoneNumber();
		this.phonePrefix = customer.getPhonePrefix();
		this.phoneType = customer.getPhoneType();
		this.phoneStatus = customer.getPhoneStatus();
		this.email = customer.getEmail();
		this.emailType = customer.getEmailType();
		this.emailStatus = customer.getEmailStatus();
		this.country = customer.getCountry();
		this.city = customer.getCity();
		this.district = customer.getDistrict();
		this.street = customer.getStreet();
		this.building = customer.getBuilding();
		this.floor = customer.getFloor();
		this.doorNumber = customer.getDoorNumber();
		this.addressType = customer.getAddressType();
		this.addressStatus = customer.getAddressStatus();
	}

}
