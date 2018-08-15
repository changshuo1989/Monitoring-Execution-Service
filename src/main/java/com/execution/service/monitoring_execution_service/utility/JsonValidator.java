package com.execution.service.monitoring_execution_service.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonValidator {
	
	private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	
	public static boolean validateJsonSchema(String schemaStr, String instanceStr) throws Exception{
		
        JsonNode mainNode = JsonLoader.fromString(schemaStr);
        JsonNode instanceNode = JsonLoader.fromString(instanceStr);
        JsonSchema schema = factory.getJsonSchema(mainNode);
        ProcessingReport processingReport = schema.validate(instanceNode);
        
        return processingReport.isSuccess();
	}
}
