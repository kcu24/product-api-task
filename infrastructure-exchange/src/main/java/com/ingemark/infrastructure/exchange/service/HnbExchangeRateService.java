package com.ingemark.infrastructure.exchange.service;

import com.ingemark.infrastructure.exchange.config.HnbExchangeProperties;
import com.ingemark.application.exception.ExchangeRateUnavailableException;
import com.ingemark.infrastructure.exchange.model.ExchangeRateDto;
import com.ingemark.application.exchange.ExchangeRateService;
import com.ingemark.application.request.SupportedCurrency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;

@Service
public class HnbExchangeRateService implements ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(HnbExchangeRateService.class);

    private final HnbExchangeProperties properties;
    private final WebClient webClient;

    public HnbExchangeRateService(HnbExchangeProperties properties, WebClient.Builder webClientBuilder) {
        this.properties = properties;
        webClient = webClientBuilder.baseUrl(this.properties.getBaseUrl()).build();
    }

    @Override
    @Cacheable(value = "exchangeRates", key = "#currency.code")
    @Retryable(
            retryFor = {ExchangeRateUnavailableException.class},
            backoff = @Backoff(delay = 2000))
    public BigDecimal getExchangeRate(SupportedCurrency currency) {
        logger.info("Fetching exchange rate for currency: {}", currency.getCode());

        try {
            String uri = properties.getUriPath() + currency.getCode();
            logger.debug("Calling external service at URI: {}", uri);

            ExchangeRateDto[] response = getExchangeRateDtoResponse(uri);

            if (response == null || response.length == 0) {
                logger.warn("No exchange rate data returned for currency: {}", currency.getCode());
                throw new ExchangeRateUnavailableException("No exchange rate data found for currency: " + currency);
            }

            return  response[0].getMidRateAsBigDecimal();

        } catch (WebClientResponseException exception) {
            logger.error("Error from exchange service for currency {}: {}", currency.getCode(), exception.getStatusCode(), exception);
            throw new ExchangeRateUnavailableException("Exchange rate service returned an error: " + exception.getStatusCode());

        } catch (Exception exception) {
            logger.error("Unexpected error fetching exchange rate for currency {}: {}", currency.getCode(), exception.getMessage(), exception);
            throw new ExchangeRateUnavailableException("Failed to fetch exchange rate for currency: " + currency.getCode());
        }
    }

    private ExchangeRateDto[] getExchangeRateDtoResponse(String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ExchangeRateDto[].class)
                .block();
    }
}
