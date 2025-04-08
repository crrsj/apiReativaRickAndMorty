package br.com.react.dto;

import java.util.List;

public record CharacterResponse(
	    Info info,
	    List<CharacterResult> results) {
}
