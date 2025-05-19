package com.mylstech.product.mapper.impl;

import com.mylstech.product.dto.request.ClientRequest;
import com.mylstech.product.dto.response.ClientResponse;
import com.mylstech.product.mapper.ClientMapper;
import com.mylstech.product.model.Client;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientResponse toDto(Client client) {
        if ( client == null ) {
            return null;
        }

        return new ClientResponse ( client );
    }

    @Override
    public Client toEntity(ClientRequest clientRequest) {
        if ( clientRequest == null ) {
            return null;
        }

        Client client = new Client ( );
        client.setName ( clientRequest.getName ( ) );

        return client;
    }

    @Override
    public Client updateEntityFromDto(Client client, ClientRequest clientRequest) {
        if ( clientRequest == null ) {
            return client;
        }

        if ( clientRequest.getName ( ) != null ) {
            client.setName ( clientRequest.getName ( ) );
        }

        return client;
    }

    @Override
    public List<ClientResponse> toDtoList(List<Client> clients) {
        if ( clients == null ) {
            return List.of ( );
        }

        return clients.stream ( )
                .map ( this::toDto )
                .toList ( );
    }
}