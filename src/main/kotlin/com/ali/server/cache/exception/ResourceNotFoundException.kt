package com.ali.server.cache.exception

import java.lang.RuntimeException

class ResourceNotFoundException(key: String) :
    RuntimeException("Resource $key not found.")