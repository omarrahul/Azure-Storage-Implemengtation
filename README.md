Utilising azure blob implementation both via Rest API and by using Spring cloud implementation.

This project is an end to end implementation of pushing a blob and pulling it based on write and read in the container.

Need and Usability - This could be leveraged based on business needs where files are getting saved in local storage and business has a tendency to loose these file exchanges during server starts or any other sad events of crash. With this mechanism in place the applications can stay stateless and could be leveraged to be containerised.

Components Used : 
1. Spring boot rest application - This exposes the controller end points and listens to the file created/updated events on azure service bus
2. Azure Storage Account - Using blob storage to read and write files to storage accounts
3. Azure service bus - This is used mainly to generate events as and when files are written to blob storage
4. Logic App - Triggered when a blob is written to storage and generates an event on the service bus

Arch diagram 


Config snapshots