package org.file.grain.enrichment.api;

import org.file.grain.enrichment.config.AccessLogData;
import org.file.grain.enrichment.config.JsonConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class Data {
	
	
	private   JsonConfig jsonConfig;

	@GetMapping(value = "/get/data")
	public String getData() throws JsonProcessingException {

		
		ObjectMapper objectMapper = jsonConfig.configureJackson();
		objectMapper.setSerializationInclusion(Include.NON_NULL); // Exclure les champs nulls

		// Créer un objet avec des valeurs nulles
		AccessLogData logData = new AccessLogData();
		logData.setUserName("JohnDoe");
		logData.setTimestamp(null); // Timestamp est nul

		// Sérialiser l'objet en JSON
		String jsonString = objectMapper.writeValueAsString(logData);
		
		// Vérifier la sortie JSON
		System.out.println(jsonString);

		return "ok";
	}
	
	public static void main(String[] args) {
		
		  // Le JSON à convertir en objet
        String json = "{ \"userName\": \"JohnDoe\", \"timestamp\": \"2024-10-16T12:45:30\" }";

        // Créer une instance d'ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Désérialiser le JSON en objet AccessLogData
            AccessLogData logData = objectMapper.readValue(json, AccessLogData.class);

            // Afficher l'objet pour vérifier la conversion
            System.out.println("Nom d'utilisateur : " + logData.getUserName());
            System.out.println("Horodatage : " + logData.getTimestamp());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		
		
	}

}
