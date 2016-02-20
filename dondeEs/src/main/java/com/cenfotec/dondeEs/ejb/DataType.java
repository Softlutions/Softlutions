package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the data_type database table.
 * 
 */
@Entity
@Table(name="data_type")
@NamedQuery(name="DataType.findAll", query="SELECT d FROM DataType d")
public class DataType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="data_type_id")
	private int dataTypeId;

	private String type;

	//bi-directional many-to-one association to EventAttribute
	@OneToMany(mappedBy="dataType")
	private List<EventAttribute> eventAttributes;

	public DataType() {
	}

	public int getDataTypeId() {
		return this.dataTypeId;
	}

	public void setDataTypeId(int dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<EventAttribute> getEventAttributes() {
		return this.eventAttributes;
	}

	public void setEventAttributes(List<EventAttribute> eventAttributes) {
		this.eventAttributes = eventAttributes;
	}

	public EventAttribute addEventAttribute(EventAttribute eventAttribute) {
		getEventAttributes().add(eventAttribute);
		eventAttribute.setDataType(this);

		return eventAttribute;
	}

	public EventAttribute removeEventAttribute(EventAttribute eventAttribute) {
		getEventAttributes().remove(eventAttribute);
		eventAttribute.setDataType(null);

		return eventAttribute;
	}

}