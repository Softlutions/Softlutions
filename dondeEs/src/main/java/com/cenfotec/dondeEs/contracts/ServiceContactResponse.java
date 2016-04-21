package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.ServiceContactPOJO;

public class ServiceContactResponse extends BaseResponse {
	
	ServiceContactPOJO contract;
	List<ServiceContactPOJO> listContracts;

	public ServiceContactPOJO getContract() {
		return contract;
	}

	public void setContract(ServiceContactPOJO contract) {
		this.contract = contract;
	}

	public List<ServiceContactPOJO> getListContracts() {
		return listContracts;
	}

	public void setListContracts(List<ServiceContactPOJO> listContracts) {
		this.listContracts = listContracts;
	}
}
