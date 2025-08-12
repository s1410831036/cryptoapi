package com.example.cryptoapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(CurrencyId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    private String chartName;

    private String chineseName;

    @Id
    private String code;

    private String rate;
}
