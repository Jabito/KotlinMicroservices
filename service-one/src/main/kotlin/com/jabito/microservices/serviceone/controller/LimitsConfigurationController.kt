package com.jabito.microservices.serviceone.controller

import com.jabito.microservices.serviceone.bean.Configuration
import com.jabito.microservices.serviceone.bean.LimitConfiguration
import com.netflix.ribbon.proxy.annotation.Hystrix
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LimitsConfigurationController(@Autowired private val configuration: Configuration){

    @GetMapping("/limits")
    fun retrieveLimitsFromConfigurations(): LimitConfiguration {
        return LimitConfiguration(configuration.minimum,configuration.maximum)
    }

    @GetMapping("/fault-tolerance-example")
    fun retrieveConfiguration(): LimitConfiguration = throw RuntimeException("Not Available")
}
