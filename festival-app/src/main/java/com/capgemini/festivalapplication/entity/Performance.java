package com.capgemini.festivalapplication.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "performances")
public class Performance {
    
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @NotNull(message = "End time is required")
    @Column(nullable = false)
    private LocalDateTime endTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dj_id", referencedColumnName = "id")
    @JsonBackReference
    private Dj dj;
    
    public Performance() {
        this.id = UUID.randomUUID().toString();
    }
    
    public Performance(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Dj dj) {
        this();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dj = dj;
    }
    
    // Business method
    public long getDurationInHours() {
        if (startTime != null && endTime != null) {
            return ChronoUnit.HOURS.between(startTime, endTime);
        }
        return 0;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getDjId() {
        return dj != null ? dj.getId() : null;
    }
    
    public Dj getDj() {
        return dj;
    }
    
    public void setDj(Dj dj) {
        this.dj = dj;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Performance that = (Performance) o;
        return Objects.equals(title, that.title) && 
               Objects.equals(description, that.description) && 
               Objects.equals(startTime, that.startTime) && 
               Objects.equals(endTime, that.endTime) && 
               Objects.equals(dj, that.dj);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, description, startTime, endTime, dj);
    }
    
    @Override
    public String toString() {
        return "Performance{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", djId='" + getDjId() + '\'' +
                '}';
    }
}
