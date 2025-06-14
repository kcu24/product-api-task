package com.ingemark.application.command;

import java.math.BigDecimal;

public record ProductCreateCommand(String code, String name, BigDecimal priceInEur, boolean available) {

}
