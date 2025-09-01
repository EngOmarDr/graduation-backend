package com.graduationProject._thYear.EventSyncronization.Records;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.graduationProject._thYear.Auth.models.Role;
import com.graduationProject._thYear.Auth.models.User;
import com.graduationProject._thYear.Invoice.models.InvoiceDiscount;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.models.InvoiceItem;
import com.graduationProject._thYear.Invoice.models.InvoiceKind;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRecord {
    private UUID globalId;
    private UUID warehouseId;
    private UUID invoiceTypeId;
    private LocalDateTime date;
    private Boolean isSuspended;
    private UUID accountId;
    private UUID currencyId;
    private BigDecimal currencyValue;
    private BigDecimal total;
    private BigDecimal totalDisc;
    private BigDecimal totalExtra;
    private Integer payType;
    private Boolean isPosted;
    private InvoiceKind parentType;
    private Object parent;
    private LocalDateTime postedDate;
    private String notes;
    private UserRecord user;

    @Default
    private List<InvoiceItemRecord> invoiceItems = new ArrayList<>();
    @Default
    private List<InvoiceDiscountRecord> invoiceDiscounts = new ArrayList<>();   

    public static InvoiceRecord fromInvoiceEntity(InvoiceHeader invoiceHeader){
        return InvoiceRecord.builder()
            .globalId(invoiceHeader.getGlobalId())
            .accountId(invoiceHeader.getAccount().getGlobalId())
            .warehouseId(invoiceHeader.getWarehouse().getGlobalId())
            .invoiceTypeId(invoiceHeader.getInvoiceType().getGlobalId())
            .currencyId(invoiceHeader.getCurrency().getGlobalId())
            .currencyValue(invoiceHeader.getCurrencyValue())
            .total(invoiceHeader.getTotal())
            .totalDisc(invoiceHeader.getTotalDisc())
            .totalExtra(invoiceHeader.getTotalExtra())
            .parentType(invoiceHeader.getParentType())
            .payType(invoiceHeader.getPayType())
            .isPosted(invoiceHeader.getIsPosted())
            .isSuspended(invoiceHeader.getIsSuspended())
            .notes(invoiceHeader.getNotes())
            .date(invoiceHeader.getDate())
            .postedDate(invoiceHeader.getPostedDate())
            .user(UserRecord.fromUserEntity(invoiceHeader.getUser()))
            .invoiceItems(invoiceHeader.getInvoiceItems().stream()
                .map(item -> InvoiceItemRecord.fromInvoiceItemEntitgy(item))
                .collect(Collectors.toList())
            )
            .invoiceDiscounts(invoiceHeader.getInvoiceDiscounts().stream()
                .map(item -> InvoiceDiscountRecord.fromInvoiceDiscountEntity(item))
                .collect(Collectors.toList())
            )
            .build();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InvoiceItemRecord{
        private UUID globalId;
        private UUID productId;
        private BigDecimal qty;
        private BigDecimal price;
        private BigDecimal bonusQty;
        private UUID unitItemId;
        private BigDecimal unitFact;
        private String notes;

        public static InvoiceItemRecord fromInvoiceItemEntitgy(InvoiceItem invoiceItem){
            return InvoiceItemRecord.builder()
                .globalId(invoiceItem.getGlobalId())
                .productId(invoiceItem.getProduct().getGlobalId())
                .qty(invoiceItem.getQty())
                .price(invoiceItem.getPrice())
                .bonusQty(invoiceItem.getBonusQty())
                .unitItemId(invoiceItem.getUnitItem().getGlobalId())
                .unitFact(invoiceItem.getUnitFact())
                .notes(invoiceItem.getNotes())
                .build();
        }
        
    }

   
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InvoiceDiscountRecord{
        private UUID globalId;
        private UUID accountId;
        private BigDecimal discount;
        private BigDecimal extra;
        private String notes;

        public static InvoiceDiscountRecord fromInvoiceDiscountEntity(InvoiceDiscount invoiceDiscount){
            return InvoiceDiscountRecord.builder()
                .globalId(invoiceDiscount.getGlobalId())
                .accountId(invoiceDiscount.getAccount().getGlobalId())
                .discount(invoiceDiscount.getDiscount())
                .extra(invoiceDiscount.getExtra())
                .notes(invoiceDiscount.getNotes())
                .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserRecord{
        private UUID globalId;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private Role role;
        private UUID warehouseId;

        public static UserRecord fromUserEntity(User user){
            return UserRecord.builder()
                .globalId(user.getGlobalId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .warehouseId(Optional.ofNullable(user.getWarehouse())
                    .map(warehouse -> warehouse.getGlobalId())
                    .orElse(null))
                .build();
        }
    }
}
