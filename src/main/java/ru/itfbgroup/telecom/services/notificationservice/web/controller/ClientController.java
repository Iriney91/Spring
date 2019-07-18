package ru.itfbgroup.telecom.services.notificationservice.web.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itfbgroup.telecom.services.notificationservice.common.web.PaginalResult;
import ru.itfbgroup.telecom.services.notificationservice.common.web.Result;
import ru.itfbgroup.telecom.services.notificationservice.model.Client;
import ru.itfbgroup.telecom.services.notificationservice.service.ClientService;
import ru.itfbgroup.telecom.services.notificationservice.web.dto.ClientInDTO;
import ru.itfbgroup.telecom.services.notificationservice.web.dto.ClientOutDTO;
import ru.itfbgroup.telecom.services.notificationservice.web.dto.ClientPaginalRequestDTO;
import ru.itfbgroup.telecom.services.notificationservice.web.mapper.ClientMapper;

import java.util.List;

@Api(value = "Client management")
@RequiredArgsConstructor
@RequestMapping("/client")
@RestController
@Slf4j
public class ClientController {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    PaginalResult<List<ClientOutDTO>> getClients(ClientPaginalRequestDTO requestDto) {
        Page<Client> paginatedBySearchRequest = clientService.getPaginatedBySearchRequest(requestDto);
        List<ClientOutDTO> clientOutDTOS = clientMapper.clientToDto(paginatedBySearchRequest.getContent());
        return PaginalResult.success(clientOutDTOS, requestDto.getOffset(),
                requestDto.getLimit(), paginatedBySearchRequest.getTotalElements());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<ClientOutDTO> getById(@PathVariable Long id) {
        return Result.success(clientMapper.clientToDto(clientService.getById(id)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<ClientOutDTO> update(@PathVariable Long id, @RequestBody ClientInDTO clientInDTO) {
        Client client = clientMapper.dtoToClient(clientInDTO);
        client.setId(id);
        return Result.success(clientMapper.clientToDto(clientService.update(client)));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result <ClientOutDTO> create(@RequestBody ClientInDTO clientInDTO) {
        return Result.success(clientMapper.clientToDto(clientService.create(clientMapper.dtoToClient(clientInDTO))));
    }

    @DeleteMapping(value = "/{id}")
    Result delete(@PathVariable Long id) {
        clientService.delete(id);
        return Result.success();
    }

    @PostMapping(value = "/{clientId}/book/{bookId}")
    Result addBook(@PathVariable Long clientId, @PathVariable Long bookId) {
        System.out.println(clientId);
        clientService.addBook(clientId, bookId);
        return Result.success();
    }

    @DeleteMapping(value = "/{clientId}/book/{bookId}")
    Result removeBook(@PathVariable Long clientId, @PathVariable Long bookId) {
        clientService.removeBook(clientId, bookId);
        return Result.success();
    }
}
