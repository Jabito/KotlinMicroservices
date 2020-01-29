package com.jabito.microservices.serviceone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix

@EnableConfigurationProperties
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
class ServiceOneApplication

fun main(args: Array<String>) {
	runApplication<ServiceOneApplication>(*args)
}
