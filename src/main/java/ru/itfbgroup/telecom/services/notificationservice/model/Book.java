package ru.itfbgroup.telecom.services.notificationservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"publishingHouse", "binaryContent", "authorIds"})
@Entity
@SequenceGenerator(name = "MY_SEQ", sequenceName = "book_SEQ")
@Table(indexes = {@Index(name = "book_iccid_idx", columnList = "iccid", unique = true), @Index(name = "book_name_idx", columnList = "name", unique = false)})
@NaturalIdCache
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Book extends IDIdentity {

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Date createDate;

    @Column(nullable = false, length = 64)
    private String iccid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @JoinColumn(name = "PUB_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PublishingHouse publishingHouse;

    @Column (name = "PUB_ID", insertable = false, updatable = false, nullable = false, length = 64)
    private Long publishingHouseId;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private BookBinary bookBinary;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "Authors_Books",
            joinColumns = @JoinColumn(name = "Book_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "Author_ID", referencedColumnName = "ID")
    )
    private Set <Author> authors = new HashSet<>();

    @OneToMany(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookClient> clients = new ArrayList<>();
}
