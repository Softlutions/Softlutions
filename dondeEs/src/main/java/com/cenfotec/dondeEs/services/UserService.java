package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.contracts.LoginRequest;
import com.cenfotec.dondeEs.contracts.UserRequest;
import com.cenfotec.dondeEs.ejb.Auction;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.RolePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.pojo.UserTypePOJO;
import com.cenfotec.dondeEs.repositories.AuctionRepository;
import com.cenfotec.dondeEs.repositories.RoleRepository;
import com.cenfotec.dondeEs.repositories.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private JavaMailSender mailSender;

	public List<UserPOJO> getAll() {
		List<User> usersList = userRepository.findAll();
		List<UserPOJO> usersListPOJO = new ArrayList<>();

		usersList.stream().forEach(u -> {
			UserPOJO userPOJO = new UserPOJO();
			userPOJO.setUserId(u.getUserId());
			userPOJO.setEmail(u.getEmail());
			userPOJO.setLastName1(u.getLastName1());
			userPOJO.setLastName2(u.getLastName2());
			userPOJO.setName(u.getName());
			userPOJO.setPhone(u.getPhone());
			userPOJO.setState((u.getState() == 1 ? true : false));
			if (u.getRole() != null) {
				RolePOJO rolePOJO = new RolePOJO();
				rolePOJO.setName(u.getRole().getName());
				userPOJO.setRole(rolePOJO);
			}

			if (u.getUserType() != null) {
				UserTypePOJO userTypePOJO = new UserTypePOJO();
				userTypePOJO.setName(u.getUserType().getName());
				userPOJO.setUserType(userTypePOJO);
			}
			usersListPOJO.add(userPOJO);
		});
		return usersListPOJO;
	}

	@Override
	@Transactional
	public List<ServicePOJO> getAllService(int idUser) {
		List<com.cenfotec.dondeEs.ejb.Service> listService = userRepository.getServicesByUser(idUser);
		List<ServicePOJO> listPojo = new ArrayList<ServicePOJO>();
		listService.stream().forEach(ta -> {
			ServicePOJO servicePOJO = new ServicePOJO();
			BeanUtils.copyProperties(ta, servicePOJO);

			if (ta.getServiceCatalog() != null) {
				ServiceCatalogPOJO catalogPOJO = new ServiceCatalogPOJO();
				BeanUtils.copyProperties(ta.getServiceCatalog(), catalogPOJO);

				catalogPOJO.setAuctions(null);
				servicePOJO.setServiceCatalog(catalogPOJO);
			}
			servicePOJO.setServiceContacts(null);

			listPojo.add(servicePOJO);

		});

		return listPojo;
	}
	/**
	 * @author Antoni Ramirez Montano
	 * @param u usuario por actualizar
	 * @return si es falso o no
	 */
	public Boolean updateUser(User u){
		if(u.getPassword()==null)
			u.setPassword(userRepository.findOne(u.getUserId()).getPassword());
		User nu = userRepository.save(u);
		return (nu == null) ? false:true;
	}

	public int saveUser(UserRequest ur) {
		User user = new User();
		BeanUtils.copyProperties(ur.getUser(), user);
		user.setRole(roleRepository.findOne(ur.getUser().getRole().getRoleId()));
		User nuser = userRepository.save(user);
		return nuser.getUserId();
	}
	
	/**
	 * @author Alejandro Bermúdez Vargas
	 * @param UserRequest 
	 * @version 1.0
	 */
	public Boolean createUser(UserRequest ur) {
		ur.getUser().setPassword(AES.base64encode(ur.getUser().getPassword()));
		if (userRepository.findByEmail(ur.getUser().getEmail()) == null) return saveUser(ur) > 0;
		return false;
	}

	/**
	 * @author Alejandro Bermúdez Vargas
	 * @exception AddressException no se encuentra la direccion de correo
	 * @exception MessagingException No encuentra el server.
	 * @param LoginRequest, tiene un atributo email del usuario
	 * @version 1.0
	 */
	public Boolean updatePassword(LoginRequest ur) {
		User user = userRepository.findByEmail(ur.getEmail());
		if (user == null)
			return false;
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String subject = "Contraseña restablecida!";
		try {
			String email = user.getEmail();
			String password = UUID.randomUUID().toString().substring(0, 7);
			String encryptPassword = AES.base64encode(password);
			String text = "Contraseña restablecida correctamente, tu nueva contraseña es: " + password;
			user.setPassword(encryptPassword);
			user.setState((byte)2);
			mailMessage.setTo(email);
			mailMessage.setText(text);
			mailMessage.setSubject(subject);
			mailSender.send(mailMessage);
			User nuser = userRepository.save(user);
			if (nuser != null)
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @author Alejandro Bermúdez Vargas
	 * @exception AddressException no se encuentra la direccion de correo
	 * @exception MessagingException No encuentra el server.
	 * @param LoginRequest, tiene un atributo email del usuario
	 * @version 1.0
	 */
	public Boolean updatePasswordRequired(LoginRequest ur) {
		User user = userRepository.findByEmail(ur.getEmail());
		if (user == null) return false;
		user.setPassword(AES.base64encode(ur.getPassword()));
		user.setState((byte)1);
		User nuser = userRepository.save(user);
		return true;
	}

	@Override
	@Transactional
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);

		return user;
	}

	/***
	 * Obtiene el usuario de cada servicio ofertado en todas las subastas de un
	 * determinado evento.
	 * 
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	@Override
	@Transactional
	public List<UserPOJO> getAllServicesProviderAuction(int idEvent) {
		List<UserPOJO> usersPOJO = new ArrayList<UserPOJO>();
		List<Auction> auctions = auctionRepository.findAllByEventEventId(idEvent);

		auctions.stream().forEach(e -> {
			if (e.getAuctionServices() != null) {
				e.getAuctionServices().stream().forEach(as -> {
					UserPOJO userPOJO = new UserPOJO();
					BeanUtils.copyProperties(as.getService().getUser(), userPOJO);
					usersPOJO.add(userPOJO);
				});
			}
		});

		return usersPOJO;
	}

	/***
	 * Obtiene un usuario por su id.
	 * 
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	@Override
	public User findById(int id) {
		return userRepository.findByUserId(id);
	}
	
	/**
	 * Obtiene un usuario por su id.
	 * 
	 * @param userId El id del usuario por consultar
	 * @return El usuario deseado
	 * @author Juan Carlos Sanchez G.
	 * @version 1.0
	 */
	
	public UserPOJO getUserById(int userId) {
		User usersList = userRepository.findOne(userId);	
		UserPOJO userPOJO = new UserPOJO();
		
		userPOJO.setUserId(usersList.getUserId());
		userPOJO.setEmail(usersList.getEmail());
		userPOJO.setLastName1(usersList.getLastName1());
		userPOJO.setLastName2(usersList.getLastName2());
		userPOJO.setName(usersList.getName());
		userPOJO.setPhone(usersList.getPhone());
		userPOJO.setState((usersList.getState() == 1 ? true : false));
		
		if (usersList.getRole() != null) {
			RolePOJO rolePOJO = new RolePOJO();
			rolePOJO.setName(usersList.getRole().getName());
			rolePOJO.setRoleId(usersList.getRole().getRoleId());
			rolePOJO.setState(usersList.getRole().getState());
			userPOJO.setRole(rolePOJO);
		}
		if (usersList.getUserType() != null) {
			UserTypePOJO userTypePOJO = new UserTypePOJO();
			userTypePOJO.setName(usersList.getUserType().getName());
			userPOJO.setUserType(userTypePOJO);
		}
		return userPOJO;
	}
	
	@Override
	@Transactional
	public Boolean changeUserState(int userId, boolean state){
		boolean changed = false;
		User user = userRepository.findOne(userId);
		
		if(user != null){
			user.setState((byte) (state? 1:0));
			changed = true;
		}
		
		return changed;
	}
	
	@Override
	public List<UserPOJO> getAllServiceProviderNames() {
		List<User> usersList = userRepository.findByRoleRoleId(2);
		List<UserPOJO> usersListPOJO = new ArrayList<>();

		usersList.stream().forEach(u -> {
			UserPOJO userPOJO = new UserPOJO();
			userPOJO.setUserId(u.getUserId());
			userPOJO.setName(u.getName());
			userPOJO.setLastName1(u.getLastName1());
			userPOJO.setLastName2(u.getLastName2());
			userPOJO.setEmail(null);
			userPOJO.setPhone(null);
			
			usersListPOJO.add(userPOJO); 
		});
		
		return usersListPOJO;
	}
}
