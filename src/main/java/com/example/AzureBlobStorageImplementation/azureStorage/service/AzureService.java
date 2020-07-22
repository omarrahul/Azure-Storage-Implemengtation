package com.example.AzureBlobStorageImplementation.azureStorage.service;

import com.couchbase.client.core.CouchbaseException;
import com.example.AzureBlobStorageImplementation.azureStorage.bean.User;
import com.example.AzureBlobStorageImplementation.azureStorage.config.AzureConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.AzureBlobStorageImplementation.azureStorage.repository.UserRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

@Component
@Service
public class AzureService {

	@Autowired
	CloudBlobContainer cloudBlobContainer;

	@Autowired
	AzureConfig azureConfig;

	@Autowired
	UserRepository userRepository;

	RestTemplate restTemplate = new RestTemplate();

	private static String FILENAME = "user.json";

	private static String FILENAME_REST = "user-rest.json";

	User user  = new User();

	public void uploadBlob(User user,String path){
			// Get a blob reference for a file. File is created if does not exists or is updated if already exists
		try {
			CloudBlockBlob blob = cloudBlobContainer.getBlockBlobReference(path+FILENAME);
			// create output stream to write the object
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(user);
			byteArrayOutputStream.close();
			// create input stream for azure storage
			InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			blob.upload(inputStream,-1);
		} catch(StorageException e){
			e.getMessage();
		} catch(IOException e){
			e.getMessage();
		} catch(URISyntaxException e){
			e.getMessage();
		}
	System.out.println("Blob successfully uploaded:"+ path);
	}

	public void UploadBlobRest(User user,String inbox, String flag) {
		UriComponentsBuilder uriCRF = UriComponentsBuilder.fromHttpUrl(azureConfig.getBaseUri())
		                                                  .pathSegment(azureConfig.getContainerName(),inbox,
		                                                               flag,FILENAME_REST)
		                                                  .queryParam("sig", "GzdXzE88TsxoAK%2FQvsS51Yipmxs78lbwkfe6sxDa%2FcE%3D")
		                                                  .queryParam("st", "2020-07-17T15:47:59Z")
		                                                  .queryParam("se", "2020-08-06T23:47:59Z")
		                                                  .queryParam("sv", "2019-10-10")
		                                                  .queryParam("sp", "rwdlacupx")
		                                                  .queryParam("srt", "sco")
		                                                  .queryParam("ss", "bfqt")
		                                                  .queryParam("spr", "https");

			try {
				HttpHeaders headers = new HttpHeaders();
				headers.set("x-ms-blob-type", "BlockBlob");
				HttpEntity<User> entity = new HttpEntity<>(user, headers);
				restTemplate.exchange(uriCRF.build(true).toUri(), HttpMethod.PUT, entity, User.class);
			} catch(RestClientResponseException e){
				e.getMessage();
			}
		System.out.println("Blob successfully uploaded via Rest:"+inbox+"/"+flag);
	}

	public void downloadBlobAndSaveToDB(String fileName) {
		try {
			CloudBlockBlob blob = cloudBlobContainer.getBlockBlobReference(fileName);
			if(!fileName.contains("rest")) {
				user = getUserWithInputStream(blob);
			} else {
				user = getUserWithFileDownload(fileName, blob);
			}
			if(null != userRepository.save(user)) {
				System.out.println("Save Object successfull to couchbase");
				CheckRestAndMoveBlob(fileName, azureConfig.getArchivePath());
			}else{
				System.out.println("Save Object failure to couchbase");
				CheckRestAndMoveBlob(fileName, azureConfig.getErrorPath());
			}
		} catch (URISyntaxException | IOException e) {
			e.getMessage();
		} catch (StorageException e) {
			e.getMessage();
		} catch (CouchbaseException e){
			e.getMessage();
		} catch(ClassNotFoundException e) {
			e.getMessage();
		}
	}

	private void CheckRestAndMoveBlob(String fileName, String path) {
		if (fileName.contains("rest")) {
			String[] splitPath = path.split("/");
			UploadBlobRest(user,splitPath[0],splitPath[1]);
		} else {
			uploadBlob(user, path);
		}
	}

	private User getUserWithFileDownload(String fileName, CloudBlockBlob blob) throws StorageException, IOException {
		// download file and use for DB update
		blob.downloadToFile(azureConfig.getLocalPath() + fileName);
		System.out.println("File Downloaded Successfully at" + azureConfig.getLocalPath());
		//read file and convert to object for saving to couchbase
		ObjectMapper mapper = new ObjectMapper();
		user = mapper.readValue(new File(azureConfig.getLocalPath() + fileName), User.class);
		return user;
	}

	private User getUserWithInputStream(CloudBlockBlob blob)
					throws StorageException, IOException, ClassNotFoundException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// getting blob output stream
		blob.download(byteArrayOutputStream);
		byteArrayOutputStream.close();
		// convert output stream to input stream
		ByteArrayInputStream in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		ObjectInputStream is = new ObjectInputStream(in);
		in.close();
		// read and cast the input stream object to User
		user = (User)is.readObject();
		return user;
	}
}