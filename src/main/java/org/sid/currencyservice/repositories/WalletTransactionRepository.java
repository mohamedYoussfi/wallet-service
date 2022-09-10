package org.sid.currencyservice.repositories;

import org.sid.currencyservice.entities.Wallet;
import org.sid.currencyservice.entities.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,String> {
 List<WalletTransaction> findByWalletId(String walletId);
}
