package com.jabito.microservices.currencyconversionservice

import brave.sampler.Sampler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableFeignClients("com.jabito.microservices.currencyconversionservice")
@EnableDiscoveryClient
class CurrencyConversionServiceApplication

fun main(args: Array<String>) {
	runApplication<CurrencyConversionServiceApplication>(*args)
}

@Bean
fun defaultSampler(): Sampler = Sampler.ALWAYS_SAMPLE
