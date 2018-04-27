package com.execution.service.monitoring_execution_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.execution.service.monitoring_execution_service.model.ConnectionInfo;
import com.execution.service.monitoring_execution_service.model.ConnectionPoolMap;
import com.execution.service.monitoring_execution_service.model.DataSource;
import com.execution.service.monitoring_execution_service.model.RuleInfo;
import com.execution.service.monitoring_execution_service.utility.DatabaseServiceClient;

public class OfflineSchedulerService {
	
	private ConnectionPoolMap connectionPoolMap;
	
	public OfflineSchedulerService (){
		connectionPoolMap = ConnectionPoolMap.getInstance();
	}
	
	
	
	public boolean getTriggeredRulesInDetail(Map<String, List<Integer>> triggeredRulesSchedules){
		boolean res = false;
		try{
			List<Integer> ruleIds = new ArrayList<>();
			if(triggeredRulesSchedules != null && !triggeredRulesSchedules.isEmpty()){
				for(String key : triggeredRulesSchedules.keySet()){
					ruleIds.add(Integer.parseInt(key));
				}
			}
			List<RuleInfo> ruleInfoList = DatabaseServiceClient.getInstance().getRuleInfoListFromDatabaseService(ruleIds);
			//System.out.println(ruleInfoList.size());
			if(ruleInfoList != null){
				//check connection pool list()
				//currently always incremental
				for(int i=0; i<ruleInfoList.size(); i++){
					RuleInfo ruleInfo = ruleInfoList.get(i);
					if(ruleInfo != null){
						ConnectionInfo connInfo = ruleInfo.getConnection();
						if(connInfo != null){
							int connId = connInfo.getId();
							//TODO add PoolInfo
							if(!connectionPoolMap.getMap().containsKey(connId)){
								DataSource dataSource = new DataSource(connInfo);
								connectionPoolMap.getMap().put(connId, dataSource);
							}
							
							
						}
						
					}
					
				}
				
				
				
				
				//TODO INSERT rules for processing
				
				res = true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	
}
