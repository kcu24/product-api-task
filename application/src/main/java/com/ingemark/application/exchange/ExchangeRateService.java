package com.ingemark.application.exchange;


import com.ingemark.application.request.SupportedCurrency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface ExchangeRateService {
    BigDecimal getExchangeRate(SupportedCurrency currency);
}
