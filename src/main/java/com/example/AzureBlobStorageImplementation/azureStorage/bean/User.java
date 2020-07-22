package com.example.AzureBlobStorageImplementation.azureStorage.bean;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document
@ApiModel(description = "Details of the user model which contains id , name and DOB")
public class User implements Serializable {

    @Id
    private Integer id;

    @Field
    private String name;

    @Field
    @ApiModelProperty(notes = "The format is of date type" , example = "11101965")
    private Date birthDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
