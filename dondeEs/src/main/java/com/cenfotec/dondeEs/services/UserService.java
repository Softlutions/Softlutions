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
			// if(ta.getUser()!=null){
			// UserPOJO userPOJO = new UserPOJO();
			// BeanUtils.copyProperties(ta.getUser(), userPOJO);
			// servicePOJO.setUser(userPOJO);
			// }
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

	public Boolean saveUser(UserRequest ur) {
		User user = new User();
		BeanUtils.copyProperties(ur.getUser(), user);
		user.setRole(roleRepository.findOne(ur.getUser().getRole().getRoleId()));
		User nuser = userRepository.save(user);
		return (nuser == null) ? false : true;
	}
	
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
			String text = "Contraseña restablecida correctamente, tu nueva contraseña es: " + password
					+ ".";
			user.setPassword(encryptPassword);
			mailMessage.setTo(email);
			mailMessage.setText(text);
			mailMessage.setSubject(subject);
			mailSender.send(mailMessage);
			User nuser = userRepository.save(user);
			if(nuser!=null) return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@Transactional
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);

		return user;
	}

	@Override
	@Transactional
	public List<UserPOJO> getAllServicesProviderAuction(int idEvent) {
		List<UserPOJO> usersPOJO = new ArrayList<UserPOJO>();

		System.out.println(auctionRepository); // prueba

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
	
	@Override
	public User findById(int id) {	
		return userRepository.findByUserId(id);
	}
}
