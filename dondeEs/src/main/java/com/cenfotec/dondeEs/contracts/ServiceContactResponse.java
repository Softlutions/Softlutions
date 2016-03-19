package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;

public class ServiceContactResponse extends BaseResponse {
	
	List<ServiceContactPOJO> listContracts;

	public List<ServiceContactPOJO> getListContracts() {
		return listContracts;
	}

	public void setListContracts(List<ServiceContactPOJO> listContracts) {
		this.listContracts = listContracts;
	}
	
	

}
