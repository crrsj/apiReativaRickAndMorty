package br.com.react.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.react.dto.CharacterResult;
import br.com.react.service.RickAndMortyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/characters")
public class RickAndMortyController {

	private final RickAndMortyService service;
	
	 public RickAndMortyController(RickAndMortyService service) {
		
		this.service = service;
	}
	   
	 
	    @GetMapping("/all")
	    @Operation(summary = "Endpoint responsável por buscar todos os characteres via cache.") 
	    @ApiResponse(responseCode = "200",description = " sucesso",content = {
	   	@Content(mediaType = "application.json",schema = @Schema(implementation = ResponseEntity.class))
	    })   
	    public Mono<List<CharacterResult>> getAllCharacters() {
	        return service.getAllCharactersFromCache();
	    }
	   
	    // ignora o cache e sempre consulte a API:
	    @GetMapping("/fresh")
	    @Operation(summary = "Endpoint responsável por buscar todos os characteres diretamente da API.") 
	    @ApiResponse(responseCode = "200",description = " sucesso",content = {
	   	@Content(mediaType = "application.json",schema = @Schema(implementation = ResponseEntity.class))
	    })   
	    public Mono<List<CharacterResult>> getAllCharactersFresh() {
	        return service.getAllCharacters();
	    }
	
}
