package com.ali.server.cache.controller

import com.ali.server.cache.service.ResourceService
import org.springframework.http.ETag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class ResourceController(
    val resourceService: ResourceService
) {

    @GetMapping("/resource/{nameSpace}/{id}")
    fun getResource(
        @PathVariable nameSpace: String,
        @PathVariable id: Int,
        @RequestHeader(name = "if-none-match", required = false) ifNoneMatch: ETag
    ): ResponseEntity<*> {
        TODO()
    }

    @PostMapping("/resource/{nameSpace}/{id}")
    fun createResource(@PathVariable nameSpace: String, @PathVariable id: Int): ResponseEntity<*> {
        TODO()
    }

    @PutMapping("/resource/{nameSpace}/{id}")
    fun updateResource(@PathVariable nameSpace: String, @PathVariable id: Int): ResponseEntity<*> {
        TODO()
    }

    @DeleteMapping("/resource/{nameSpace}/{id}")
    fun deleteMapping(@PathVariable nameSpace: String, @PathVariable id: Int): ResponseEntity<*> {
        TODO()
    }

}