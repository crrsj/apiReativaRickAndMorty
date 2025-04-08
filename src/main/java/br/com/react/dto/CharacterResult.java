package br.com.react.dto;

import java.util.List;

public record CharacterResult(
		int id,
	    String name,
	    String status,
	    String species,
	    String type,
	    String gender,
	    Origin origin,
	    Location location,
	    String image,
	    List<String> episode,
	    String url,
	    String created) {

}
