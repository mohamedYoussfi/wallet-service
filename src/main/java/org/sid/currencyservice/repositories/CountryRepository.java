package org.sid.currencyservice.repositories;

import org.sid.currencyservice.entities.Country;
import org.sid.currencyservice.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country,Long> {
    Country findByCountryName(String countryName);
    Country findByIsoCode(String isoCode);
    List<Country> findByContinentId(Long id);
}
