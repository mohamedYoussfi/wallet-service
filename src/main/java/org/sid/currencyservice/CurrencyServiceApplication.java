package org.sid.currencyservice;

import org.sid.currencyservice.entities.Currency;
import org.sid.currencyservice.entities.Wallet;
import org.sid.currencyservice.repositories.CurrencyRepository;
import org.sid.currencyservice.repositories.WalletRepository;
import org.sid.currencyservice.repositories.WalletTransactionRepository;
import org.sid.currencyservice.services.CurrencyServiceImpl;
import org.sid.currencyservice.services.WalletServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class CurrencyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CurrencyServiceImpl currencyService,
                            WalletServiceImpl walletService,
                            CurrencyRepository currencyRepository,
                            WalletRepository walletRepository,
                            WalletTransactionRepository walletTransactionRepository){
        return args -> {
            currencyService.loadContinentsAndCountries();
            currencyService.loadCurrencies();
            currencyService.setCurrenciesPrices();
            List<String> currencies= Arrays.asList("MAD","EUR","USD","CAD");
            currencies.forEach(cur->{
                Currency currency=currencyRepository.findByCode(cur);
                Wallet wallet= Wallet.builder()
                        .id(UUID.randomUUID().toString())
                        .currency(currency)
                        .balance(1000)
                        .createdAt(System.currentTimeMillis())
                        .userId("@you")
                        .build();
                wallet=walletRepository.save(wallet);
            });
            var walletList = walletRepository.findAll();
            for (int i = 0; i < walletList.size()-1; i++) {
                var wal1=walletList.get(i);
                var wal2=walletList.get(i+1);
                walletService.walletTransfer(wal1.getId(), wal2.getId(),100);
            }
            walletRepository.findAll().forEach(wallet -> {
                System.out.println("*********************");
                System.out.println(wallet.getId());
                System.out.println(wallet.getBalance());
                System.out.println("*******************");
            });
        };
    }
}
