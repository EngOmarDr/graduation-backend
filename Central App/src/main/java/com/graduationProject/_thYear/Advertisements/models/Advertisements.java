package com.graduationProject._thYear.Advertisements.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mediaUrl;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private MediaType mediaType;

    @Column(name = "duration_seconds", nullable = false)
    private Integer duration;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

//    public enum MediaType {
//        IMAGE,
//        VIDEO
//    }
}
