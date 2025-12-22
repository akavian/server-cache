package com.ali.server.caching.repository

import com.ali.server.caching.model.Resource
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository: MongoRepository<Resource, String>