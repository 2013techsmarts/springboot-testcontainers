package co.smarttechie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("content") String content
) {}
