package com.graduationProject._thYear.InvoiceTypePos.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoicePos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoicePos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceTypeId", nullable = false)
    private InvoiceType invoiceTypeId;

}
