package com.jabito.microservices.currencyconversionservice.bean

import java.math.BigDecimal

data class CurrencyConversionBean(val id: Long,
                                  val from: String,
                                  val to: String,
                                  val conversionMultiple: BigDecimal?,
                                  val quantity: BigDecimal?,
                                  val totalAmount: BigDecimal?,
                                  val port: Int = 0)
