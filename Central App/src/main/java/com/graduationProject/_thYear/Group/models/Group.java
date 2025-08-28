package com.graduationProject._thYear.Group.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "`group`")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Group {
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
    

    @NotNull
    @Column(name = "code" , unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(name = "name" , unique = true ,nullable = false)
    private String name;


    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Group parent;

    @OneToMany(mappedBy = "parent")
    private List<Group> children ;


    public void addChild(Group child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Group child) {
        children.remove(child);
        child.setParent(null);
    }

}
