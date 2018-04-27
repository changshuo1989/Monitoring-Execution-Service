package com.execution.service.monitoring_execution_service.resource;



import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.execution.service.monitoring_execution_service.service.OfflineSchedulerService;



@Path("offline-scheduler/triggered")
public class OfflineSchedulerResource {
	
	private OfflineSchedulerService offlineSchedulerService;
	
	public OfflineSchedulerResource(){
		offlineSchedulerService = new OfflineSchedulerService();
	}
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTriggeredRulesAndSchedules(Map<String, List<Integer>> triggeredRulesSchedules){
		System.out.println(triggeredRulesSchedules);
		offlineSchedulerService.getTriggeredRulesInDetail(triggeredRulesSchedules);
		return Response.status(200).entity(true).build();
	}
	
	
	/*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    */
	
	
	
	
}
