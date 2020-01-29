package com.jabito.microservices.netflixzuulapigatewayserver

import brave.sampler.Sampler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
class NetflixZuulApiGatewayServerApplication

fun main(args: Array<String>) {
	runApplication<NetflixZuulApiGatewayServerApplication>(*args)
}

@Bean
fun defaultSampler(): Sampler = Sampler.ALWAYS_SAMPLE
