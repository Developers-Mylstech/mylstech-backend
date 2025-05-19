package com.mylstech.product.service;

import com.mylstech.product.dto.request.ClientRequest;
import com.mylstech.product.dto.response.ClientResponse;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientResponse addClient(ClientRequest request);
    List<ClientResponse> getAllClients();
    Optional<ClientResponse> getClientById(Long clientId);
    ClientResponse updateClient(Long clientId, ClientRequest request);
    void deleteClient(Long clientId);
    ClientResponse deleteClientImage(Long clientId);
}