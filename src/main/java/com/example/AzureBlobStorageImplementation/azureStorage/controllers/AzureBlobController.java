package com.example.AzureBlobStorageImplementation.azureStorage.controllers;

import com.example.AzureBlobStorageImplementation.azureStorage.bean.User;
import com.example.AzureBlobStorageImplementation.azureStorage.service.AzureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class AzureBlobController {

    @Autowired
    AzureService azureService;

    @PutMapping(value="/uploadBlobSpringAzure")
    public String uploadBlobSpringCloudAzure(@RequestBody User user) {
        azureService.uploadBlob(user,"");
        return "Blob successfully uploaded via stream";
    }

    @PutMapping(value="/uploadBlobRest")
    public String uploadBlobRestAzure(@RequestBody User user) {
        azureService.UploadBlobRest(user,"","");
        return "Blob successfully uploaded via REST";
    }
}


