package com.example.AzureBlobStorageImplementation.azureStorage.repository;

import com.example.AzureBlobStorageImplementation.azureStorage.bean.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

/**
 * Repository to hold all facilities in memory
 */
public interface UserRepository extends CouchbaseRepository<User, Integer> {

}
