package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.BaseResponse;
import com.cenfotec.dondeEs.contracts.MessageRequest;

@RestController
@RequestMapping(value = "rest/contactMessage")
public class ContactMessageController {

	public static final String APP_DOMAIN = "http://localhost:8080";
	private static String subject;
	private static String text;
	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * Envia al correo del propietario del software un mensaje de contacto ingresado por un usuario.
	 * @author Enmanuel García González
	 * @param message
	 * @version 1.0
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public void sendMessage(@RequestBody MessageRequest message) {
		BaseResponse response = new BaseResponse();
		
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			subject = "Message del usuario";
			text = "Ha recibido un mensaje del usuario: " + message.getUserName() + "\n" +
						"Correo: " + message.getUserEmail() + "\n" +
						"Mensage: " + message.getMessage();
				
			mailMessage.setTo("softlutionscr@gmail.com");
			mailMessage.setText(text);
			mailMessage.setSubject(subject);
			mailSender.send(mailMessage);
			
			response.setCode(200);
		} catch (Exception e) {
			response.setCode(500);
			response.setErrorMessage(e.toString());
			e.printStackTrace();
		}
	}
}
