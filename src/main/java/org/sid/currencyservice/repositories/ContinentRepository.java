package org.sid.currencyservice.repositories;

import org.sid.currencyservice.entities.Continent;
import org.sid.currencyservice.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContinentRepository extends JpaRepository<Continent,Long> {
    Continent findByContinentName(String continentName);
}
