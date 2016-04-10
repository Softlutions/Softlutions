package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.ParticipationOnEventsPOJO;

public class ParticipationOnEventsResponse extends BaseResponse{
	private List<ParticipationOnEventsPOJO> resultParticipationOnEvents;

	public List<ParticipationOnEventsPOJO> getResultParticipationOnEvents() {
		return resultParticipationOnEvents;
	}

	public void setResultParticipationOnEvents(
			List<ParticipationOnEventsPOJO> resultParticipationOnEvents) {
		this.resultParticipationOnEvents = resultParticipationOnEvents;
	}
}
