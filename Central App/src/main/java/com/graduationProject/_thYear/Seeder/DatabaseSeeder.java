package com.graduationProject._thYear.Seeder;

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.Advertisements.models.Advertisements;
import com.graduationProject._thYear.Advertisements.repositories.AdvertisementsRepository;
import com.graduationProject._thYear.Branch.models.Branch;
import com.graduationProject._thYear.Branch.repositories.BranchRepository;
import com.graduationProject._thYear.Currency.dtos.requests.CreateCurrencyRequest;
import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.Currency.services.CurrencyService;
import com.graduationProject._thYear.Group.models.Group;
import com.graduationProject._thYear.Group.repositories.GroupRepository;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.models.Type;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;
import com.graduationProject._thYear.Product.models.*;
import com.graduationProject._thYear.Product.repositories.*;
import com.graduationProject._thYear.Unit.models.Unit;
import com.graduationProject._thYear.Unit.models.UnitItem;
import com.graduationProject._thYear.Unit.repositories.UnitItemRepository;
import com.graduationProject._thYear.Unit.repositories.UnitRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.models.WarehouseType;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CurrencyRepository currencyRepo;
    private final AdvertisementsRepository advertisementsRepo;
    private final BranchRepository branchRepo;
    private final GroupRepository groupRepo;
    private final UnitRepository unitRepo;
    private final UnitItemRepository unitItemRepo;
    private final PriceRepository priceRepo;
    private final AccountRepository accountRepo;
    private final WarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;
    private final ProductBarcodeRepository barcodeRepo;
    private final ProductPriceRepository productPriceRepo;
    private final InvoiceTypeRepository invoiceTypeRepo;

    private final CurrencyService currencyService;
    @Override
    @Transactional
    public void run(String... args) {
        //  q Currencies
//        List<Currency> currencies = IntStream.rangeClosed(1, 5)
//                .mapToObj(i -> currencyRepo.save(Currency.builder()
//                        .code("USD" + i)
//                        .name("US Dollar " + i)
//                        .currencyValue(1.0f + i)
//                        .partName("Cent")
//                        .partPrecision(2)
//                        .build()))
//                .toList();

        CreateCurrencyRequest SP = CreateCurrencyRequest.builder()
                .code("CU01")
                .name("syria Bound")
                .currencyValue(1.0f)
                .partName("s.p")
                .partPrecision(1)
                .build();

        CreateCurrencyRequest usd = CreateCurrencyRequest.builder()
                .code("CU02")
                .name("US Dollar")
                .currencyValue(10000.0f)
                .partName("$")
                .partPrecision(1)
                .build();

        currencyService.createCurrency(SP);
        currencyService.createCurrency(usd);

        // Advertisements
        List<String> adImages = List.of(
                "http://localhost:8080/images/rachit-tank-2cFZ_FB08UM-unsplash.jpg",
                "http://localhost:8080/images/pmv-chamara-MEsWk-dZzlI-unsplash.jpg",
                "http://localhost:8080/images/domino-studio-164_6wVEHfI-unsplash.jpg",
                "http://localhost:8080/images/curology-DGH1u80sZik-unsplash.jpg",
                "http://localhost:8080/images/c-d-x-PDX_a_82obo-unsplash.jpg"
        );

        for (int i = 0; i < 5; i++) {
            advertisementsRepo.save(
                    Advertisements.builder()
                            .name("Advertisement " + (i + 1))
                            .mediaUrl(adImages.get(i))
                            .duration(10 + i)
                            .build()
            );
        }


        //  Branches
        List<Branch> branches = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> branchRepo.save(Branch.builder()
                        .name("Branch " + i)
                        .phone("10000" + i)
                        .address("Address " + i)
                        .notes("Notes " + i)
                        .build()))
                .toList();

        //  Groups
        List<Group> groups = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> groupRepo.save(Group.builder()
                        .code("GR" + i)
                        .name("Group " + i)
                        .build()))
                .toList();

        // Units + UnitItems
        List<Unit> units = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> unitRepo.save(Unit.builder()
                        .name("Unit " + i)
                        .build()))
                .toList();

        List<UnitItem> unitItems = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Unit unit = units.get(i - 1);
            UnitItem ui = unitItemRepo.save(UnitItem.builder()
                    .unit(unit)
                    .name("UnitItem " + i)
                    .fact(1f)
                    .build());
            unitItems.add(ui);
        }

        //  Prices
        List<Price> prices = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> priceRepo.save(Price.builder()
                        .name("Price Level " + i)
                        .build()))
                .toList();

        // Accounts
        List<Account> accounts = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> accountRepo.save(Account.builder()
                        .code("ACC" + i)
                        .name("Account " + i)
                        .build()))
                .toList();

        //   Warehouses
        List<Warehouse> warehouses = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> warehouseRepo.save(Warehouse.builder()
                        .name("Warehouse " + i)
                        .code("WH" + i)
                        .branch(branches.get(i - 1))
                        .type(WarehouseType.WAREHOUSE)
                        .isActive(true)
                        .build()))
                .toList();

        //  Products + Barcodes + Prices
        List<String> productImages = List.of(
                "http://localhost:8080/images/rachit-tank-2cFZ_FB08UM-unsplash.jpg",
                "http://localhost:8080/images/pmv-chamara-MEsWk-dZzlI-unsplash.jpg",
                "http://localhost:8080/images/domino-studio-164_6wVEHfI-unsplash.jpg",
                "http://localhost:8080/images/curology-DGH1u80sZik-unsplash.jpg",
                "http://localhost:8080/images/c-d-x-PDX_a_82obo-unsplash.jpg"
        );

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = productRepo.save(Product.builder()
                    .code("PRD" + (i + 1))
                    .name("Product " + (i + 1))
                    .type(Product.TYPE_WAREHOUSE)
                    .groupId(groups.get(i))
                    .defaultUnit(units.get(i))
                    .minQty(1f)
                    .maxQty(100f)
                    .orderQty(10f)
                    .image(productImages.get(i))
                    .build());
            products.add(product);
        }

        for (int i = 0; i < 5; i++) {
            Product product = products.get(i);
            ProductBarcode barcode = ProductBarcode.builder()
                    .barcode("BAR" + (i + 1))
                    .product(product)
                    .unitItem(unitItems.get(i))
                    .build();
            barcodeRepo.save(barcode);

            ProductPrice price = ProductPrice.builder()
                    .productId(product)
                    .priceId(prices.get(i))
                    .priceUnit(unitItems.get(i))
                    .price(10f + i)
                    .build();
            productPriceRepo.save(price);
        }

        //  Invoice Types
        if (invoiceTypeRepo.count() > 0) return;

        // Dummy references (assume you have ID = 1 for all)
        Warehouse defaultWarehouse = warehouseRepo.findById(1).orElseThrow();
        Currency defaultCurrency = currencyRepo.findById(1).orElseThrow();
        Account defaultAccount = accountRepo.findById(1).orElseThrow();

        for (Type type : Type.values()) {
            InvoiceType invoiceType = InvoiceType.builder()
                    .type(type)
                    .name(type.name() + " invoice")
                    .defaultWarehouseId(defaultWarehouse)
                    .defaultCurrencyId(defaultCurrency)

                    .defaultBillAccId(defaultAccount)
                    .defaultCashAccId(defaultAccount)
                    .defaultDiscAccId(defaultAccount)
                    .defaultExtraAccId(defaultAccount)
                    .defaultCostAccId(defaultAccount)
                    .defaultStockAccId(defaultAccount)

                    .isAffectCostPrice(true)
                    .isAffectLastPrice(false)
                    .isAffectCustPrice(false)
                    .isAffectProfit(false)

                    .isDiscAffectCost(false)
                    .isExtraAffectCost(false)

                    .isNoEntry(false)
                    .isAutoEntry(true)
                    .isAutoEntryPost(true)
                    .isNoPost(false)
                    .isAutoPost(true)

                    .isShortEntry(false)
                    .isCashBill(true)
                    .printAfterInsert(false)
                    .isBarcode(false)

                    .build();

            invoiceTypeRepo.save(invoiceType);
        }

        System.out.println("Seeded default invoice types.");
    }


}
