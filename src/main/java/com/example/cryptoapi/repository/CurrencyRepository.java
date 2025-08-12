package com.example.cryptoapi.repository;

import com.example.cryptoapi.model.Currency;
import com.example.cryptoapi.model.CurrencyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, CurrencyId> {
}
