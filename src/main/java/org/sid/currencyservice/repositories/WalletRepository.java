package org.sid.currencyservice.repositories;

import org.sid.currencyservice.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet,String> {
    List<Wallet> findByUserId(String userId);
}
