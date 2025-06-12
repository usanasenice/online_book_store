package com.iliamalafeev.bookstore.bookstore_backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenreDTO {
    private Long id;

    @NotBlank(message = "Genre description must contain at least 1 character")
    @Size(max = 50, message = "Genre description length must not exceed 50 characters")
    private String description;

    @JsonCreator
    public GenreDTO(@JsonProperty("id") Long id, @JsonProperty("description") String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static GenreDTO fromString(String value) {
        GenreDTO genre = new GenreDTO();
        try {
            genre.id = Long.parseLong(value);
        } catch (NumberFormatException e) {
            genre.description = value;
        }
        return genre;
    }
}
