package com.ali.server.`c@che`.repository

import com.ali.server.`c@che`.model.Resource
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository: MongoRepository<Resource, String>