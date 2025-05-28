package com.graduationProject._thYear.Account.models;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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

    @OneToMany(mappedBy = "parent")
    private List<Account> children;

    public void addChild(Account child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Account child) {
        children.remove(child);
        child.setParent(null);
    }

}
