package fr.insee.pogues.webservice.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.pogues.config.auth.UserProvider;
import fr.insee.pogues.config.auth.user.User;
import fr.insee.pogues.persistence.service.QuestionnairesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * WebService class for the Instrument Persistence
 * 
 * See the swagger documentation for this service :
 * http://inseefr.github.io/Pogues/en/remote-apis/swagger.html
 * 
 * @author I6VWID
 * 
 *         schemes: - http
 * 
 *         consumes: - application/json
 * 
 *         produces: - application/json
 *
 */
@RestController
@RequestMapping("/persistence")
@Tag(name = "Pogues Persistence")
public class PoguesPersistence {

    final static Logger logger = LogManager.getLogger(PoguesPersistence.class);

    @Autowired
	private QuestionnairesService questionnaireService;
    
    @Autowired
    private Environment env;

	@Autowired
	private UserProvider userProvider;


	@GET
	@GetMapping("questionnaire/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId  = "getQuestionnaires",
	        summary = "Get questionnaire",
            description = "Gets the questionnaire with id {id}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })	
	public ResponseEntity<Object> getQuestionnaire(
			@Parameter(description = "This is the id of the object we want to retrieve", required = true)
			@PathParam(value = "id") String id
	) throws Exception {
		try {
			JSONObject result = questionnaireService.getQuestionnaireByID(id);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
    
    @GET
    @GetMapping("questionnaire/json-lunatic/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "getJsonLunatic",
	        summary = "Get questionnaire",
            description = "Gets the questionnaire with id JsonLunatic {id}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
	public ResponseEntity<Object> getJsonLunatic(
			@Parameter(description = "This is the id of the object we want to retrieve", required = true)
			@PathParam(value = "id") String id
	) throws Exception {
		try {
			JSONObject result = questionnaireService.getJsonLunaticByID(id);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

	}

    @GET
    @GetMapping("questionnaires/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
    		operationId = "searchQuestionnaires",
            summary = "Search questionnaires",
            description = "Search questionnaires matching query params"
    )
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "400", description = "Bad request")
	})
    public ResponseEntity<Object> searchQuestionnaires(
			@Parameter(description = "A user id matching owner permission on each object of the collection", required = false)
            @QueryParam("owner") String owner
    ) throws Exception {
        try {
			List<JSONObject> questionnaires = new ArrayList<>();
            if(null != owner){
                questionnaires.addAll(questionnaireService.getQuestionnairesByOwner(owner));
            }
            return ResponseEntity.status(HttpStatus.OK).body(questionnaires);
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
            throw e;
        }
    }
    
	@GET
	@GetMapping("questionnaires/search/meta")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "searchQuestionnairesMetadata",
	        summary = "Get questionnaires' metadata",
            description = "Get questionnaires' metadata matching query params"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
	public ResponseEntity<Object> getQuestionnairesMetadata(
			@Parameter(description = "A user id matching owner permission on each object of the collection", required = false)
            @QueryParam("owner") String owner
	) throws Exception {
		try {
			List<JSONObject> questionnairesMetadata = new ArrayList<>();
            if(null != owner){
                questionnairesMetadata.addAll(questionnaireService.getQuestionnairesMetadata(owner));
            }
            return ResponseEntity.status(HttpStatus.OK).body(questionnairesMetadata);
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
            throw e;
        }
	}
	
	@GET
	@GetMapping("questionnaires/stamps")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "searchQuestionnairesStamps",
	        summary = "Get stamps in database",
            description = "Get stamps with at least one questionnaire saved in database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
	public ResponseEntity<Object> getQuestionnaireStamps() throws Exception {
		try {
			List<JSONObject> questionnairesStamps = new ArrayList<>();
			questionnairesStamps.addAll(questionnaireService.getQuestionnairesStamps());
            return ResponseEntity.status(HttpStatus.OK).body(questionnairesStamps);
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
            throw e;
        }
	}
	

	@DELETE
	@DeleteMapping("questionnaire/{id}")
	@Operation(
			operationId = "deleteQuestionnaire",
	        summary = "Delete questionnaire",
            description = "Delete questionnaire with id {id}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
	public ResponseEntity<Object> deleteQuestionnaire(Authentication auth,
			@Parameter(description = "The id of the object that need to be deleted", required = true)
			@PathParam(value = "id") String id
	) throws Exception {
		try {
			questionnaireService.deleteQuestionnaireByID(id);
			User user=userProvider.getUser(auth);
			logger.info("Questionnaire "+ id +" deleted by "+user.getName());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@DELETE
	@DeleteMapping("questionnaire/json-lunatic/{id}")
	@Operation(
			operationId = "deleteJsonLunatic",
	        summary = "Delete Json Lunatic of a questionnaire",
            description = "Delete the Json Lunatic representation of a  questionnaire with id {id}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
//	@OwnerRestricted
	public ResponseEntity<Object> deleteJsonLunatic(
			@Parameter(description = "The id of the object that need to be deleted", required = true)
			@PathParam(value = "id") String id
	) throws Exception {
		try {
			questionnaireService.deleteJsonLunaticByID(id);
			logger.info("Questionnaire {} deleted", id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			throw e;
		}
	}

	@GET
	@GetMapping("questionnaires")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "getQuestionnaireList",
	        summary = "Get questionnaires",
            description = "Gets the `QuestionnaireList` object"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
	public ResponseEntity<Object> getQuestionnaireList() throws Exception {
		try {
			List<JSONObject> questionnaires = questionnaireService.getQuestionnaireList();
			return ResponseEntity.status(HttpStatus.OK).body(questionnaires);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@PUT
	@PutMapping("questionnaire/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "updateQuestionnaire",
	        summary = "Update questionnaire",
            description = "Update a `Questionnaire` object with id {id}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
	public ResponseEntity<Object> updateQuestionnaire(
			@Parameter(description = "The id of the object that need to be updated", required = true)
			@PathParam(value = "id") String id,
			@Parameter(description = "Instrument object to be updated") JSONObject jsonContent
	) throws Exception {
        try {
			questionnaireService.updateQuestionnaire(id, jsonContent);
			logger.info("Questionnaire {} updated", id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
            throw e;
        }
	}
	
	@PUT
	@PutMapping("questionnaire/json-lunatic/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "updateJsonLunatic",
	        summary = "Update Json Lunatic",
            description = "Update Json Lunatic of a `Questionnaire` object with id {id}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
//	@OwnerRestricted
	public ResponseEntity<Object> updateJsonLunatic(
			@Parameter(description = "The id of the questionnaire which json lunatic needs to be updated", required = true)
			@PathParam(value = "id") String id,
			@Parameter(description = "Json Lunatic to be updated") JSONObject jsonLunatic
	) throws Exception {
        try {
			questionnaireService.updateJsonLunatic(id, jsonLunatic);
			logger.info("Json Lunatic of questionnaire {} updated", id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            throw e;
        }
	}

	@POST
	@PostMapping("questionnaires")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "createQuestionnaire",
	        summary = "Create Questionnaire",
            description = "Creates a new `Questionnaire`"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Entity already exists")
    })
	public ResponseEntity<Object> createQuestionnaire(
			@Parameter(description = "New Instrument Object", required = true) JSONObject jsonContent
	) throws Exception {
        try {
        	questionnaireService.createQuestionnaire(jsonContent);
			String id = (String) jsonContent.get("id");
			String dbHost = env.getProperty("fr.insee.pogues.persistence.database.host");
			String apiName = env.getProperty("fr.insee.pogues.api.name");
			String uriQuestionnaire = String.format("http://%s%s/persistence/questionnaire/%s",dbHost,apiName,id);
			logger.debug("New questionnaire created , uri : {}",uriQuestionnaire);
			return ResponseEntity.status(HttpStatus.CREATED).header("Location", uriQuestionnaire).build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@POST
	@PostMapping("questionnaires/json-lunatic")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
			operationId = "createJsonLunatic",
	        summary = "Create Json Lunatic of questionnaire",
            description = "Creates a new Json Lunatic entry"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Entity already exists")
    })
	public ResponseEntity<Object> createJsonLunatic(
			@Parameter(description = "New Instrument Object", required = true) JSONObject jsonContent
	) throws Exception {
        try {
			questionnaireService.createJsonLunatic(jsonContent);
			String id = (String) jsonContent.get("id");
			String dbHost = env.getProperty("fr.insee.pogues.persistence.database.host");
			String apiName = env.getProperty("fr.insee.pogues.api.name");
			String uriJsonLunaticQuestionnaire = String.format("http://%s%s/persistence/questionnaire/json-lunatic/%s",dbHost,apiName,id);
			logger.debug("New Json Lunatic created , uri : {}", uriJsonLunaticQuestionnaire);
			return ResponseEntity.status(HttpStatus.CREATED).header("Location", uriJsonLunaticQuestionnaire).build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

}
