package com.example.AzureBlobStorageImplementation.azureStorage.springCloudStream;

import com.example.AzureBlobStorageImplementation.azureStorage.service.AzureService;
import com.microsoft.azure.spring.integration.core.AzureHeaders;
import com.microsoft.azure.spring.integration.core.api.Checkpointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Header;

@EnableBinding(Sink.class)
public class AzureStreamBinderSink {

	@Autowired
	AzureService azureService;

	@StreamListener(Sink.INPUT)
	public void handleMessage(String fileName, @Header(AzureHeaders.CHECKPOINTER) Checkpointer checkpointer) {
		System.out.println(String.format("New message received: '%s'", fileName));
		// download file
		azureService.downloadBlobAndSaveToDB(fileName);
		checkpointer.success().handle((r, ex) -> {
			if (ex == null) {
				System.out.println(String.format("Message '%s' successfully checkpointed", fileName));
			}
			return null;
		});
	}
}
