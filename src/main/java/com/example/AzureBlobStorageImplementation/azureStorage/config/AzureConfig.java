package com.example.AzureBlobStorageImplementation.azureStorage.config;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class AzureConfig {

    @Value("${azure.azureStorage.ConnectionString}")
    private String connectionString;

    @Value("${azure.azureStorage.container.name}")
    private String containerName;

    @Value("${azure.file.inbox}")
    private String localPath;

		@Value("${azure.file.inbox.archive}")
		private String archivePath;

		@Value("${azure.file.inbox.error}")
		private String errorPath;

		@Value("${azure.storage.base.uri}")
		private String baseUri;


    @Bean
    public CloudBlobClient cloudBlobClient() throws URISyntaxException, StorageException, InvalidKeyException {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        return storageAccount.createCloudBlobClient();
    }

    @Bean
    public CloudBlobContainer cloudBlobContainer() throws URISyntaxException, StorageException, InvalidKeyException {
	    CloudBlobContainer container = cloudBlobClient().getContainerReference(containerName);
	    if(!container.exists()) {
		    container.createIfNotExists(
						    BlobContainerPublicAccessType.CONTAINER,
						    new BlobRequestOptions(),
						    new OperationContext()
		    );
	    }
        return container;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getLocalPath() {
        return localPath;
    }

		public String getArchivePath() {
			return archivePath;
		}

		public String getErrorPath() {
			return errorPath;
		}

		public String getBaseUri() {
			return baseUri;
		}
}
