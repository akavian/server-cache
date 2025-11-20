package com.ali.server.cache.exception

import java.lang.RuntimeException

class ResourceNotFoundException(key: String?) :
    RuntimeException(
        key?.let { "Resource $key not found." } ?: "No resource found."
    )