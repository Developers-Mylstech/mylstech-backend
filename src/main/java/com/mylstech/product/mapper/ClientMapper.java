package com.mylstech.product.mapper;

import com.mylstech.product.dto.request.ClientRequest;
import com.mylstech.product.dto.response.ClientResponse;
import com.mylstech.product.model.Client;

import java.util.List;

public interface ClientMapper {
    ClientResponse toDto(Client client);
    Client toEntity(ClientRequest clientRequest);
    Client updateEntityFromDto(Client client, ClientRequest clientRequest);
    List<ClientResponse> toDtoList(List<Client> clients);
}