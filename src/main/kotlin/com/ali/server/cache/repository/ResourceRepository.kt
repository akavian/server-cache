package com.ali.server.cache.repository

import com.ali.server.cache.model.Resource
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository: MongoRepository<Resource, String>