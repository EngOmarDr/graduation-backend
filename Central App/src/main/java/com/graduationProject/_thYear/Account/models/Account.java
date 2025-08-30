package com.graduationProject._thYear.Account.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
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
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_account", nullable = true)
    private Account finalAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Account parent;

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
    private List<Account> children;

    public void addChild(Account child) {
        children.add(child);
        child.setParent(this);
    }

    public List<Integer> getAllChildren(List<Integer> result){
        result.add(this.getId());
        for(Account account : this.getChildren()){
            account.getAllChildren(result);
        }
        return result;
    }

    public void removeChild(Account child) {
        children.remove(child);
        child.setParent(null);
    }

}
