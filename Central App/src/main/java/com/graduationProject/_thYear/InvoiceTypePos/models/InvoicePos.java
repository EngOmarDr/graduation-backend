package com.graduationProject._thYear.InvoiceTypePos.models;

import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceTypeId", nullable = false)
    private InvoiceType invoiceTypeId;

}
