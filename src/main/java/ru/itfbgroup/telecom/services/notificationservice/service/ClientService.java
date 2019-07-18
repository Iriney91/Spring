package ru.itfbgroup.telecom.services.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itfbgroup.telecom.services.notificationservice.model.Book;
import ru.itfbgroup.telecom.services.notificationservice.model.Client;
import ru.itfbgroup.telecom.services.notificationservice.repository.BookRepository;
import ru.itfbgroup.telecom.services.notificationservice.repository.ClientRepository;
import ru.itfbgroup.telecom.services.notificationservice.web.dto.ClientPaginalRequestDTO;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ApplicationContext context;

    private final ClientRepository clientRepository;
    private final BookRepository bookRepository;

    public Page<Client> getPaginatedBySearchRequest(ClientPaginalRequestDTO clientPaginalRequestDTO) {
        if (clientPaginalRequestDTO.getSortColumn() == null)
            clientPaginalRequestDTO.setSortColumn("fullName");
        if (clientPaginalRequestDTO.getFullName() == null)

            return clientRepository.findAll(clientPaginalRequestDTO.getPageRequest());

        return clientRepository.findAllBySearchParams(clientPaginalRequestDTO.getFullName(), clientPaginalRequestDTO.getLogin(), clientPaginalRequestDTO.getPageRequest());
    }

    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(IllegalAccessError::new);
    }

    public Client update(Client client) {
        if (clientRepository.existsById(client.getId())) {
            clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("No such client found");
        }
        return client;
    }

    public void delete(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No such client found");
        }
    }

    public Client create(Client client) {
        if (client == null) return null;
        return create(client.getLogin(), client.getPassword(), client.getFullName(), client.getUserRole());
    }

    public Client create(String login, String password, String name, String role) {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

        CharSequence pass = encoder.encode(password);
        Client client = null;
        if (!clientRepository.existsByLogin(login)) {
            client = new Client();
            client.setLogin(login);
            client.setFullName(name);
            client.setPassword(pass.toString());
            client.setUserRole(role);
            clientRepository.save(client);
            log.info("Customer saved");
            log.info(String.format("Customer password is: %s", client.getPassword()));
        } else {
            log.info(String.format("Customer with %s login already exist", login));
        }
        return client;
    }

    public Client addBook(Long clientId, Long bookId) {
        Client client = clientRepository.getOne(clientId);
        Book book = bookRepository.getOne(bookId);

        client.addBook(book);

        return client;
    }

    public Client removeBook(Long clientId, Long bookId) {
        Client client = clientRepository.getOne(clientId);
        Book book = bookRepository.getOne(bookId);

        client.removeBook(book);

        return client;
    }
}
