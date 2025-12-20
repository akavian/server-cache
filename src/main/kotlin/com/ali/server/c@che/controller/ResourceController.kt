package com.ali.server.`c@che`.controller

import com.ali.server.`c@che`.helper.ETagCalculator
import com.ali.server.`c@che`.model.ResourceRequest
import com.ali.server.`c@che`.service.ResourceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/resource")
class ResourceController(
    private val resourceService: ResourceService,
    private val eTagCalculator: ETagCalculator
) {

    @GetMapping("/{nameSpace}/{id}")
    fun getResource(
        @PathVariable nameSpace: String,
        @PathVariable id: String,
        @RequestHeader(name = "if-none-match", required = false) ifNoneMatch: String?,
    ): ResponseEntity<Any> {
        val resource = resourceService.getResource(nameSpace, id)
        if (ifNoneMatch != null) {
            val clientETags = ifNoneMatch.split(",")
                .map { it.trim().removePrefix("W/").removePrefix("w/") }
            val resourceETag = eTagCalculator.eTagOf(resource)
            if (resourceETag in clientETags) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(resourceETag).build();
            }
        }
        return ResponseEntity.ok(resource)
    }

    @GetMapping("/{nameSpace}")
    fun getResources(
        @PathVariable(name = "nameSpace") nameSpace: String,
        @RequestParam ids: List<String>
    ): ResponseEntity<Any> {
        val resources = resourceService.getManyResourcesInNameSpace(nameSpace, ids)
        return ResponseEntity.ok(resources)
    }

    @PutMapping("/{nameSpace}/{id}")
    fun putResource(
        @PathVariable(name = "nameSpace") nameSpace: String,
        @PathVariable(name = "id") id: String,
        @RequestBody resourceRequest: ResourceRequest
    ): ResponseEntity<Unit> {
        resourceService.putResource(id, nameSpace, resourceRequest)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @DeleteMapping("/{nameSpace}/{id}")
    fun deleteResource(@PathVariable nameSpace: String, @PathVariable id: String): ResponseEntity<Unit> {
        resourceService.deleteResource(nameSpace, id)
        return ResponseEntity.noContent().build()
    }

}