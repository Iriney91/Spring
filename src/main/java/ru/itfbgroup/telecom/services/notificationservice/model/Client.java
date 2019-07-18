package ru.itfbgroup.telecom.services.notificationservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@SequenceGenerator(name = "MY_SEQ", sequenceName = "client_SEQ")
@Table(indexes = {@Index(name = "client_fullname_idx", columnList = "fullname", unique = true)})
public class Client extends IDIdentity{

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(updatable = false)
    private String userRole;

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookClient> books = new ArrayList<>();

    public void addBook(Book book) {
        BookClient bookClient = new BookClient(book, this);
        books.add(bookClient);
        book.getClients().add(bookClient);
    }

    public void removeBook(Book book) {
        for (Iterator<BookClient> iterator = books.iterator(); iterator.hasNext(); ) {
            BookClient bookClient = iterator.next();

            if(bookClient.getClient().equals(this) && bookClient.getBook().equals(book)) {
                iterator.remove();
                bookClient.getBook().getClients().remove(bookClient);
                bookClient.setBook(null);
                bookClient.setClient(null);
            }
        }
    }
}
