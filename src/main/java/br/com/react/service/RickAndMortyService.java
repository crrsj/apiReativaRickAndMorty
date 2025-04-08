package br.com.react.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.com.react.dto.CharacterResponse;
import br.com.react.dto.CharacterResult;
import reactor.core.publisher.Mono;

@Service
public class RickAndMortyService {

	  private final WebClient webClient = WebClient.builder()
	            .baseUrl("https://rickandmortyapi.com/api/character")
	            .build();

	    // Cache simples em memória
	    private final List<CharacterResult> cache = new CopyOnWriteArrayList<>();

	    // ✅ Método principal com cache
	    public Mono<List<CharacterResult>> getAllCharactersFromCache() {
	        if (cache.isEmpty()) {
	            return getCharactersByPage(1, new ArrayList<>())
	                    .doOnNext(result -> {
	                        cache.clear();
	                        cache.addAll(result);
	                    });
	        } else {
	            return Mono.just(cache);
	        }
	    }

	    // Método opcional: sempre chama a API, sem cache
	    public Mono<List<CharacterResult>> getAllCharacters() {
	        return getCharactersByPage(1, new ArrayList<>());
	    }

	    //  Método recursivo que percorre todas as páginas
	    private Mono<List<CharacterResult>> getCharactersByPage(int page, List<CharacterResult> allCharacters) {
	        return webClient.get()
	                .uri(uriBuilder -> uriBuilder
	                        .queryParam("page", page)
	                        .build())
	                .retrieve()
	                .bodyToMono(CharacterResponse.class)
	                .flatMap(response -> {
	                    allCharacters.addAll(response.results());
	                    if (page < response.info().pages()) {
	                        return getCharactersByPage(page + 1, allCharacters);
	                    } else {
	                        return Mono.just(allCharacters);
	                    }
	                })
	                .onErrorResume(WebClientResponseException.class, e -> {
	                    return Mono.error(new RuntimeException("Error when searching for characters: " + e.getMessage()));
	                });
	    }
}
