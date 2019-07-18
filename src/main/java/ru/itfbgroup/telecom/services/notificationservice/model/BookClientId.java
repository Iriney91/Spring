package ru.itfbgroup.telecom.services.notificationservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class BookClientId implements Serializable {

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "client_id")
    private Long clientId;


    private BookClientId() {}

    public BookClientId(Long bookId, Long clientId) {
        this.bookId = bookId;
        this.clientId = clientId;
    }
}
