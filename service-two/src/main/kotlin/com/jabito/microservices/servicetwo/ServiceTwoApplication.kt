package com.jabito.microservices.servicetwo

import brave.sampler.Sampler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableDiscoveryClient
class ServiceTwoApplication

fun main(args: Array<String>) {
	runApplication<ServiceTwoApplication>(*args)
}

@Bean
fun defaultSampler(): Sampler = Sampler.ALWAYS_SAMPLE
