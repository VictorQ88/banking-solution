package com.banking.solution.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.solution.domain.Client;
import com.banking.solution.dto.ClientDTO;
import com.banking.solution.repository.ClientRepository;
import com.banking.solution.utils.ClientMapper;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientDTO create(ClientDTO dto) {

        log.info("Creating client with identification = {}", dto.identification);

        repository.findByIdentification(dto.identification)
                .ifPresent(c -> {
                    log.warn("Client already exists with identification = {}", dto.identification);
                    throw new IllegalArgumentException("Client already exists");
                });

        Client entity = ClientMapper.toEntity(dto);

        String generatedClientId = "CLI-" + UUID.randomUUID();
        entity.setClientId(generatedClientId);

        Client saved = repository.save(entity);

        log.info(
                "Client created successfully id={}, clientId={}, identification={}",
                saved.getId(),
                generatedClientId,
                saved.getIdentification()
        );

        return ClientMapper.toDto(saved);
    }

    @Override
    public List<ClientDTO> findAll() {
        return repository.findAll().stream()
                .map(ClientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDTO findById(Long id) {
        Client entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        return ClientMapper.toDto(entity);
    }

    @Override
    public ClientDTO update(Long id, ClientDTO dto) {

        log.info("Updating client id={}", id);

        Client entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found id={}", id);
                    return new IllegalArgumentException("Client not found");
                });

        log.debug(
                "Current client state id={}, identification={}",
                entity.getId(),
                entity.getIdentification()
        );

        ClientMapper.updateEntity(entity, dto);

        Client saved = repository.save(entity);

        log.info(
                "Client updated successfully id={}, identification={}",
                saved.getId(),
                saved.getIdentification()
        );

        return ClientMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
