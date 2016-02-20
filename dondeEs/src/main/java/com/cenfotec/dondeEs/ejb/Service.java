package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the service database table.
 * 
 */
@Entity
@Table(name="service")
@NamedQuery(name="Service.findAll", query="SELECT s FROM Service s")
public class Service implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="service_id")
	private int serviceId;

	private String description;

	private String name;

	private byte state;

	//bi-directional many-to-one association to ServiceType
	@ManyToOne
	@JoinColumn(name="type")
	private ServiceType serviceType;

	//bi-directional many-to-one association to ServiceContract
	@OneToMany(mappedBy="service")
	private List<ServiceContract> serviceContracts;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="services")
	private List<User> users;

	public Service() {
	}

	public int getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getState() {
		return this.state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public ServiceType getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public List<ServiceContract> getServiceContracts() {
		return this.serviceContracts;
	}

	public void setServiceContracts(List<ServiceContract> serviceContracts) {
		this.serviceContracts = serviceContracts;
	}

	public ServiceContract addServiceContract(ServiceContract serviceContract) {
		getServiceContracts().add(serviceContract);
		serviceContract.setService(this);

		return serviceContract;
	}

	public ServiceContract removeServiceContract(ServiceContract serviceContract) {
		getServiceContracts().remove(serviceContract);
		serviceContract.setService(null);

		return serviceContract;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}