package com.ali.server.caching.exception

import java.lang.RuntimeException

class ResourceNotFoundException(key: String? = null) :
    RuntimeException(
        key?.let { "Resource $key not found." } ?: "No resource found."
    )