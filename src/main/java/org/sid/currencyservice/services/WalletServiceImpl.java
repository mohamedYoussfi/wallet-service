package org.sid.currencyservice.services;

import lombok.extern.slf4j.Slf4j;
import org.sid.currencyservice.entities.Currency;
import org.sid.currencyservice.entities.Wallet;
import org.sid.currencyservice.entities.WalletTransaction;
import org.sid.currencyservice.enums.WalletTransactionType;
import org.sid.currencyservice.repositories.CurrencyRepository;
import org.sid.currencyservice.repositories.WalletRepository;
import org.sid.currencyservice.repositories.WalletTransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class WalletServiceImpl {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final CurrencyRepository currencyRepository;

    public WalletServiceImpl(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository, CurrencyRepository currencyRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<Wallet> walletTransfer(String sourceWalletId, String destinationWalletId, double amount){
        List<Wallet> walletList=new ArrayList<>();
        Wallet sourceWallet=walletRepository.findById(sourceWalletId)
                .orElseThrow(()->new RuntimeException(String.format("Wallet %s not found", sourceWalletId)));
        Wallet destinationWallet=walletRepository.findById(destinationWalletId)
                .orElseThrow(()->new RuntimeException(String.format("Wallet %s not found", sourceWalletId)));
        if(sourceWallet.getBalance()<amount) throw new RuntimeException(String.format("Balance Not sufficient, banlance=%f, amount=%f",sourceWallet.getBalance(),amount));
        WalletTransaction debitWalletTransaction= WalletTransaction.builder()
                .amount(amount)
                .timestamp(System.currentTimeMillis())
                .type(WalletTransactionType.DEBIT)
                .wallet(sourceWallet)
                .build();
        walletTransactionRepository.save(debitWalletTransaction);
        sourceWallet.setBalance(sourceWallet.getBalance()-amount);
        sourceWallet=walletRepository.save(sourceWallet);
        double convertedAmount=amount*(sourceWallet.getCurrency().getPrice()/destinationWallet.getCurrency().getPrice());
        WalletTransaction creditWalletTransaction= WalletTransaction.builder()
                .amount(convertedAmount)
                .timestamp(System.currentTimeMillis())
                .type(WalletTransactionType.CREDIT)
                .wallet(destinationWallet)
                .build();
        walletTransactionRepository.save(creditWalletTransaction);
        destinationWallet.setBalance(destinationWallet.getBalance()+convertedAmount);
        destinationWallet=walletRepository.save(destinationWallet);
        return Arrays.asList(sourceWallet,destinationWallet);
    }
    public Wallet newWallet(String currencyCode, double initialBalance, String userId){
        Currency currency=currencyRepository.findByCode(currencyCode);
        Wallet wallet= Wallet.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .currency(currency)
                .balance(initialBalance)
                .createdAt(System.currentTimeMillis())
                .build();
        return walletRepository.save(wallet);
    }
}
