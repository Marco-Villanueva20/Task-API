package dev.marco.taskapi.web.dto;

public record LoginResponse(
		String token,
		String username
		) {
}
