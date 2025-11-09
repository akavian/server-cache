package com.ali.server.cache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ServerCacheApplication

fun main(args: Array<String>) {
	runApplication<ServerCacheApplication>(*args)
}
