package org.sid.currencyservice.repositories;

import org.sid.currencyservice.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Currency findByCode(String code);
    List<Currency> findByNameContains(String name);
}
