package com.ingemark.infrastructure.exchange;

import com.ingemark.application.exception.ExchangeRateUnavailableException;
import com.ingemark.application.request.SupportedCurrency;
import com.ingemark.infrastructure.exchange.config.HnbExchangeProperties;
import com.ingemark.infrastructure.exchange.model.ExchangeRateDto;
import com.ingemark.infrastructure.exchange.service.HnbExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HnbExchangeRateServiceTest {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    private HnbExchangeProperties properties;
    private HnbExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        properties = mock(HnbExchangeProperties.class);
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        when(properties.getBaseUrl()).thenReturn("https://api.test.com");
        when(properties.getUriPath()).thenReturn("/api/rates/");

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        exchangeRateService = new HnbExchangeRateService(properties, webClientBuilder);
    }

    @Test
    void testGetExchangeRate_serviceIsWorking_successResponseIsReturned() {
        //given
        SupportedCurrency currency = SupportedCurrency.USD;
        ExchangeRateDto[] mockResponse = new ExchangeRateDto[1];
        mockResponse[0] = new ExchangeRateDto();
        mockResponse[0].setMidRate("7.45");

        mockWebBuilderPart("/api/rates/USD", mockResponse);

        //when
        BigDecimal rate = exchangeRateService.getExchangeRate(currency);

        //then
        assertNotNull(rate);
        assertEquals(new BigDecimal("7.45"), rate);
    }

    @Test
    void testGetExchangeRate_emptyResponse_shouldThrowExchangeRateUnavailableException() {
        //given
        SupportedCurrency currency = SupportedCurrency.USD;
        ExchangeRateDto[] mockResponse = new ExchangeRateDto[0];

        mockWebBuilderPart("", mockResponse);

        //when/then
        assertThrows(ExchangeRateUnavailableException.class,
                () -> exchangeRateService.getExchangeRate(currency));
    }

    @Test
    void testGetExchangeRate_errorOccurs_shouldThrowExchangeRateUnavailableException() {
        //Given
        SupportedCurrency currency = SupportedCurrency.USD;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateDto[].class))
                .thenThrow(new RuntimeException("Some error"));

        // when/then
        assertThrows(ExchangeRateUnavailableException.class,
                () -> exchangeRateService.getExchangeRate(currency));
    }

    @Test
    void testGetExchangeRate_clientError_shouldThrowExchangeRateUnavailableException() {
        //Given
        SupportedCurrency currency = SupportedCurrency.USD;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateDto[].class))
                .thenThrow(new WebClientResponseException(444, "StatusText", null, null, null));

        // when/then
        assertThrows(ExchangeRateUnavailableException.class,
                () -> exchangeRateService.getExchangeRate(currency));
    }

    private void mockWebBuilderPart(String uri, ExchangeRateDto[] mockResponse) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ExchangeRateDto[].class)).thenReturn(Mono.just(mockResponse));
    }
}
