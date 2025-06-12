package com.iliamalafeev.bookstore.bookstore_backend.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;
@Data
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public Integer getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(Integer copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Checkout> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(List<Checkout> checkouts) {
        this.checkouts = checkouts;
    }

    public List<HistoryRecord> getHistoryRecords() {
        return historyRecords;
    }

    public void setHistoryRecords(List<HistoryRecord> historyRecords) {
        this.historyRecords = historyRecords;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Book title must contain at least 1 character")
    @Size(max = 100, message = "Book title length must not exceed 100 characters")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Author name must contain at least 1 character")
    @Size(max = 100, message = "Author name length must not exceed 100 characters")
    @Column(name = "author")
    private String author;

    @NotBlank(message = "Book description must contain at least 1 character")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Copies count must not be null")
    @Min(value = 0, message = "Copies count cannot be below 0")
    @Column(name = "copies")
    private Integer copies;
    @NotNull(message = "Available copies count must not be null")
    @Min(value = 0, message = "Available copies count cannot be below 0")
    @Column(name = "copies_available")
    private Integer copiesAvailable;
    @NotNull(message = "Image must not be null")
    @Column(name = "img")
    private String img;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotEmpty(message = "At least one genre must be selected")
    @ManyToMany(mappedBy = "books")
    private List<Genre> genres;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "checkedOutBook", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Checkout> checkouts;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "historyRecordedBook", fetch = FetchType.LAZY)
    private List<HistoryRecord> historyRecords;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "reviewedBook", fetch = FetchType.LAZY)
    private List<Review> reviews;
    public Book(String title, String author, String description, Integer copies, Integer copiesAvailable, String img) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.copies = copies;
        this.copiesAvailable = copiesAvailable;
        this.img = img;
    }
}

