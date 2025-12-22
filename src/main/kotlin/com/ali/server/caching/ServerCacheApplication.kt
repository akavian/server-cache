package com.ali.server.caching

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ServerCacheApplication

fun main(args: Array<String>) {
	runApplication<ServerCacheApplication>(*args)
}
