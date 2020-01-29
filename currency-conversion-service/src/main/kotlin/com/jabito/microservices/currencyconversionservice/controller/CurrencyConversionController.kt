package com.jabito.microservices.currencyconversionservice.controller

import com.jabito.microservices.currencyconversionservice.`interface`.CurrencyExchangeServiceProxy
import com.jabito.microservices.currencyconversionservice.bean.CurrencyConversionBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@RestController
class CurrencyConversionController(@Autowired private val proxy: CurrencyExchangeServiceProxy) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    fun convertCurrency(@PathVariable from: String,
                        @PathVariable to: String,
                        @PathVariable quantity: BigDecimal): CurrencyConversionBean {

        val uriVariables = hashMapOf<String, String>()
        uriVariables["from"] = from
        uriVariables["to"] = to
        print(uriVariables)
        val responseEntity: ResponseEntity<CurrencyConversionBean> =
                RestTemplate().getForEntity("http://localhost:8001/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversionBean::class.java, uriVariables)

        val response = responseEntity.body!!
        logger.info("$response")
        return CurrencyConversionBean(response.id,
                from,
                to,
                response.conversionMultiple,
                quantity,
                quantity.multiply(response.conversionMultiple),
                response.port)
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    fun convertCurrencyFeign(@PathVariable from: String,
                        @PathVariable to: String,
                        @PathVariable quantity: BigDecimal): CurrencyConversionBean {
        val response = proxy.retrieveExchangeValue(from, to)

        return CurrencyConversionBean(response.id,
                from,
                to,
                response.conversionMultiple,
                quantity,
                quantity.multiply(response.conversionMultiple),
                response.port)
    }
}
