package ru.itfbgroup.telecom.services.notificationservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@SequenceGenerator(name = "MY_SEQ", sequenceName = "bookBinary_SEQ")
public class BookBinary extends IDIdentity{

    @Column
    private byte [] binaryContent;

    @Column(length = 64)
    private String mimeType;

    @Column
    private  String fileName;
}
