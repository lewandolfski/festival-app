package com.capgemini.festivalapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DjDto {
    private String id;

    @NotBlank(message = "DJ name must not be blank")
    private String name;

    @NotBlank(message = "Genre must not be blank")
    private String genre;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}