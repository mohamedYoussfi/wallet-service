package org.sid.currencyservice.web;

import org.sid.currencyservice.entities.*;
import org.sid.currencyservice.repositories.*;
import org.sid.currencyservice.services.WalletServiceImpl;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CurrencyGraphQLController {
  private CurrencyRepository currencyRepository;
  private CountryRepository  countryRepository;
  private ContinentRepository continentRepository;
  private WalletServiceImpl walletService;
  private WalletRepository walletRepository;
  private WalletTransactionRepository walletTransactionRepository;


    public CurrencyGraphQLController(CurrencyRepository currencyRepository, CountryRepository countryRepository, ContinentRepository continentRepository, WalletServiceImpl walletService, WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.currencyRepository = currencyRepository;
        this.countryRepository = countryRepository;
        this.continentRepository = continentRepository;
        this.walletService = walletService;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }
    @QueryMapping
    public List<Currency> currencies(){
        return  currencyRepository.findAll();
    }
    @QueryMapping
    public List<Currency> currenciesByNameContains(@Argument String name){
        return  currencyRepository.findByNameContains(name);
    }
    @QueryMapping
    public List<Country> countries(){
        return  countryRepository.findAll();
    }
    @QueryMapping
    public List<Country> countriesByContinent(@Argument Long id){
        return  countryRepository.findByContinentId(id);
    }
    @QueryMapping
    public List<Continent> continents(){
        return  continentRepository.findAll();
    }
    @QueryMapping
    public Continent continentById(@Argument String continentName){
        Continent continent=continentRepository.findByContinentName(continentName);
        return continent;
    }
    @QueryMapping
    public Country countryByIsoCode(@Argument String isoCode){
        return countryRepository.findByIsoCode(isoCode);
    }
    @QueryMapping
    public Currency currencyByCode(@Argument String code){
        return currencyRepository.findByCode(code);
    }
    @MutationMapping
    public Currency updateCurrencyPrice(@Argument CurrencyPriceRequest currencyPriceRequest){
        Currency currency=currencyRepository.findByCode(currencyPriceRequest.code());
        currency.setPrice(currencyPriceRequest.price());
        return currencyRepository.save(currency);
    }
    @MutationMapping
    public Wallet addWallet(@Argument String currencyCode, @Argument double initialBalance){
        return walletService.newWallet(currencyCode,initialBalance,"@you");
    }
    @MutationMapping
    public List<Wallet> walletTransfer(@Argument String sourceWalletId,@Argument String destinationWalletId, @Argument double amount){
         return walletService.walletTransfer(sourceWalletId,destinationWalletId, amount);
    }
    @QueryMapping
    public List<Wallet> userWallets(){
        return walletRepository.findByUserId("@you");
    }
    @QueryMapping
    public Wallet walletById(@Argument String id){
        return walletRepository.findById(id).orElseThrow();
    }
    @QueryMapping
    public List<WalletTransaction> walletTransactions(@Argument String walletId){
        return walletTransactionRepository.findByWalletId(walletId);
    }

}
record CurrencyPriceRequest(String code, double price){}
