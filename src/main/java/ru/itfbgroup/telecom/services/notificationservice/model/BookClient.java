package ru.itfbgroup.telecom.services.notificationservice.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "BookClient")
@Table(name = "book_client")
public class BookClient {

    @EmbeddedId
    private BookClientId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clinetId")
    private Client client;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    private BookClient() {}

    public BookClient(Book book, Client client) {
        this.book = book;
        this.client = client;
        this.id = new BookClientId(book.getId(), client.getId());
    }
}
