package com.graduationProject._thYear.Advertisements.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advertisements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

        @Column(name = "globalId", nullable = false, updatable = false)
    @Default
    private UUID globalId = UUID.randomUUID();

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mediaUrl;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private MediaType mediaType;

    @Column(name = "duration_seconds", nullable = false)
    private Integer duration;

   
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

//    public enum MediaType {
//        IMAGE,
//        VIDEO
//    }
}
