package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the offline_user database table.
 * 
 */
@Entity
@Table(name="offline_user")
@NamedQuery(name="OfflineUser.findAll", query="SELECT o FROM OfflineUser o")
public class OfflineUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="offline_user_id")
	private int offlineUserId;

	private String email;

	//bi-directional many-to-one association to EventParticipant
	@OneToMany(mappedBy="offlineUser")
	private List<EventParticipant> eventParticipants;

	public OfflineUser() {
	}

	public int getOfflineUserId() {
		return this.offlineUserId;
	}

	public void setOfflineUserId(int offlineUserId) {
		this.offlineUserId = offlineUserId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<EventParticipant> getEventParticipants() {
		return this.eventParticipants;
	}

	public void setEventParticipants(List<EventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public EventParticipant addEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().add(eventParticipant);
		eventParticipant.setOfflineUser(this);

		return eventParticipant;
	}

	public EventParticipant removeEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().remove(eventParticipant);
		eventParticipant.setOfflineUser(null);

		return eventParticipant;
	}

}