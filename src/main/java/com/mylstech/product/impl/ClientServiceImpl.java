package com.mylstech.product.impl;

import com.mylstech.product.dto.request.ClientRequest;
import com.mylstech.product.dto.response.ClientResponse;
import com.mylstech.product.exception.ResourceNotFoundException;
import com.mylstech.product.mapper.ClientMapper;
import com.mylstech.product.model.Client;
import com.mylstech.product.repository.ClientRepository;
import com.mylstech.product.repository.ImageRepository;
import com.mylstech.product.service.ClientService;
import com.mylstech.product.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public ClientResponse addClient(ClientRequest request) {
        try {
            Client client = clientMapper.toEntity(request);
            
            if (request.getImageId() != null) {
                client.setImage(imageRepository.findById(request.getImageId())
                        .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId())));
            }
            
            Client savedClient = clientRepository.save(client);
            return clientMapper.toDto(savedClient);
        } catch (DataIntegrityViolationException ex) {
            log.error("Error adding client", ex);
            throw ex;
        }
    }

    @Override
    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clientMapper.toDtoList(clients);
    }

    @Override
    public Optional<ClientResponse> getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .map(clientMapper::toDto);
    }

    @Override
    public ClientResponse updateClient(Long clientId, ClientRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", clientId));
        
        Client updatedClient = clientMapper.updateEntityFromDto(client, request);
        
        if (request.getImageId() != null) {
            updatedClient.setImage(imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId())));
        }
        
        Client savedClient = clientRepository.save(updatedClient);
        return clientMapper.toDto(savedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client", "id", clientId);
        }
        
        // Delete the client's image first
        deleteClientImage(clientId);
        
        // Then delete the client
        clientRepository.deleteById(clientId);
    }

    @Override
    public ClientResponse deleteClientImage(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", clientId));
        
        // Store the image ID before removing the association
        Long imageId = null;
        if (client.getImage() != null) {
            imageId = client.getImage().getImageId();
        }
        
        // Remove the image association
        client.setImage(null);
        Client savedClient = clientRepository.save(client);
        
        // Delete the image if it exists
        if (imageId != null) {
            imageService.deleteImage(imageId);
        }
        
        return clientMapper.toDto(savedClient);
    }
}