package com.graduationProject._thYear.InvoiceType.seeders;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.models.Type;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceTypeSeeder {

    private final InvoiceTypeRepository invoiceTypeRepo;
    private final WarehouseRepository warehouseRepo;
    private final CurrencyRepository currencyRepo;
    private final AccountRepository accountRepo;
//
//    @PostConstruct
//    public void seed() {
//        if (invoiceTypeRepo.count() > 0) return;
//
//        // Dummy references (assume you have ID = 1 for all)
//        Warehouse defaultWarehouse = warehouseRepo.findById(1).orElseThrow();
//        Currency defaultCurrency = currencyRepo.findById(1).orElseThrow();
//        Account defaultAccount = accountRepo.findById(1).orElseThrow();
//
//        for (Type type : Type.values()) {
//            InvoiceType invoiceType = InvoiceType.builder()
//                    .type(type)
//                    .name(type.name() + " invoice")
//                    .defaultWarehouseId(defaultWarehouse)
//                    .defaultCurrencyId(defaultCurrency)
//
//                    .defaultBillAccId(defaultAccount)
//                    .defaultCashAccId(defaultAccount)
//                    .defaultDiscAccId(defaultAccount)
//                    .defaultExtraAccId(defaultAccount)
//                    .defaultCostAccId(defaultAccount)
//                    .defaultStockAccId(defaultAccount)
//
//                    .isAffectCostPrice(true)
//                    .isAffectLastPrice(false)
//                    .isAffectCustPrice(false)
//                    .isAffectProfit(false)
//
//                    .isDiscAffectCost(false)
//                    .isExtraAffectCost(false)
//
//                    .isNoEntry(false)
//                    .isAutoEntry(true)
//                    .isAutoEntryPost(true)
//                    .isNoPost(false)
//                    .isAutoPost(true)
//
//                    .isShortEntry(false)
//                    .isCashBill(true)
//                    .printAfterInsert(false)
//                    .isBarcode(false)
//
//                    .build();
//
//            invoiceTypeRepo.save(invoiceType);
//        }
//
//        System.out.println("Seeded default invoice types.");
//    }
}
