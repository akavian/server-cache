package com.ali.server.cache.exception

import java.lang.RuntimeException

class ResourceNotFoundException(nameSpace: String, id: String) :
    RuntimeException("Resource $nameSpace:$id not found.")