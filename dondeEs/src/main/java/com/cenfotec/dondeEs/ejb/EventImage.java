package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the event_images database table.
 * 
 */
@Entity
@Table(name="event_images")
@NamedQuery(name="EventImage.findAll", query="SELECT e FROM EventImage e")
public class EventImage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="event_images_id")
	private int eventImagesId;

	private String image;

	//bi-directional many-to-one association to EventParticipant
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="event_participant_id")
	private EventParticipant eventParticipant;

	public EventImage() {
	}

	public int getEventImagesId() {
		return this.eventImagesId;
	}

	public void setEventImagesId(int eventImagesId) {
		this.eventImagesId = eventImagesId;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public EventParticipant getEventParticipant() {
		return this.eventParticipant;
	}

	public void setEventParticipant(EventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;
	}

}