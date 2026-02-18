package dev.marco.taskapi.web.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(
		@NotBlank(message = "Title is required")
		String title,
		@NotBlank(message = "Description is required")
		String description
		) {

}
