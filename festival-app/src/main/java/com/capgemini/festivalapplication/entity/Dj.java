package com.capgemini.festivalapplication.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "djs")
public class Dj {
    
    @Id
    private String id;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Genre is required")
    @Column(nullable = false)
    private String genre;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "dj", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Performance> performances = new ArrayList<>();
    
    public Dj() {
        this.id = UUID.randomUUID().toString();
    }
    
    public Dj(String name, String genre, String email) {
        this();
        this.name = name;
        this.genre = genre;
        this.email = email;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Performance> getPerformances() {
        return performances;
    }
    
    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }
    
    public void addPerformance(Performance performance) {
        performances.add(performance);
        performance.setDj(this);
    }
    
    public void removePerformance(Performance performance) {
        performances.remove(performance);
        performance.setDj(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dj dj = (Dj) o;
        return Objects.equals(name, dj.name) && 
               Objects.equals(genre, dj.genre) && 
               Objects.equals(email, dj.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, genre, email);
    }
    
    @Override
    public String toString() {
        return "Dj{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
