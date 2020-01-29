package com.jabito.microservices.servicetwo.controller

import com.jabito.microservices.servicetwo.data.ExchangeValue
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@RestController
class CurrencyExchangeController(@Autowired private val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    fun retrieveExchangeValue(@PathVariable from: String,
                              @PathVariable to: String): ExchangeValue{

        val exchangeValue = ExchangeValue(1000L, from, to,
                BigDecimal.valueOf(50.8), Integer.parseInt(env.getProperty("local.server.port")))
        logger.info("$exchangeValue")

        return exchangeValue
    }
}
