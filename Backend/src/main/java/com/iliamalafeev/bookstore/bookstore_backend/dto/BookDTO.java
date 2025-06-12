package com.iliamalafeev.bookstore.bookstore_backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookDTO {

    private Long id;

    @NotBlank(message = "Book title must be present and contain at least 1 character")
    @Size(max = 100, message = "Book title length must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Author name must be present and contain at least 1 character")
    @Size(max = 100, message = "Author name length must not exceed 100 characters")
    private String author;

    @NotBlank(message = "Book description must contain at least 1 character")
    @Size(min = 10, max = 1000, message = "Book description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Copies count must not be null")
    @Min(value = 1, message = "Copies count cannot be below 1")
    @Max(value = 1000, message = "Copies count cannot exceed 1000")
    private Integer copies;

    @NotNull(message = "Available copies count must not be null")
    @Min(value = 0, message = "Available copies count cannot be below 0")
    private Integer copiesAvailable;

    @NotBlank(message = "Cover image must be present")
    @Pattern(regexp = "^https?://.*\\.(png|jpg|jpeg|gif|webp)$", message = "Cover image must be a valid image URL")
    private String img;

    @NotEmpty(message = "At least one genre must be selected")
    private List<GenreDTO> genres;

    @JsonCreator
    public BookDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("author") String author,
            @JsonProperty("description") String description,
            @JsonProperty("copies") Integer copies,
            @JsonProperty("copiesAvailable") Integer copiesAvailable,
            @JsonProperty("img") String img,
            @JsonProperty("genres") List<GenreDTO> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.copies = copies;
        this.copiesAvailable = copiesAvailable;
        this.img = img;
        this.genres = genres;
    }
}
