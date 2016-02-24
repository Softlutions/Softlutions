package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.ejb.ServiceContact;

public class ServiceContactResponse extends BaseResponse {
	
	List<ServiceContact> listContracts;

	public List<ServiceContact> getListContracts() {
		return listContracts;
	}

	public void setListContracts(List<ServiceContact> listContracts) {
		this.listContracts = listContracts;
	}
	
	

}
