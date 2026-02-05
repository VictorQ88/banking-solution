package com.banking.solution.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.banking.solution.domain.Client;
import com.banking.solution.dto.ClientDTO;
import com.banking.solution.exception.NotFoundException;
import com.banking.solution.repository.ClientRepository;
import com.banking.solution.utils.ClientIdGenerator;
import com.banking.solution.utils.ClientMapper;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository repository;
    private final ClientIdGenerator clientIdGenerator;

    public ClientServiceImpl(ClientRepository repository, ClientIdGenerator clientIdGenerator) {
        this.repository = repository;
        this.clientIdGenerator = clientIdGenerator;
    }

    @Override
    public ClientDTO create(ClientDTO dto) {

        log.info("Creating client with identification={}", dto.getIdentification());

        repository.findByIdentification(dto.getIdentification())
                .ifPresent(c -> {
                    log.warn("Client already exists identification={}", dto.getIdentification());
                    throw new IllegalArgumentException("Client already exists");
                });

        Client entity = ClientMapper.toEntity(dto);

        String encoded = Base64.getEncoder()
                .encodeToString(entity.getPassword().getBytes(StandardCharsets.UTF_8));
        entity.setPassword(encoded);

        String generatedClientId = clientIdGenerator.generate();
        entity.setClientId(generatedClientId);

        Client saved = repository.save(entity);

        log.info("Client created id={}, clientId={}, identification={}",
                saved.getId(), saved.getClientId(), saved.getIdentification());

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
                .orElseThrow(() -> new NotFoundException("Client not found"));
        return ClientMapper.toDto(entity);
    }

    @Override
    public ClientDTO update(Long id, ClientDTO dto) {

        log.info("Updating client id={}", id);

        Client entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found id={}", id);
                    return new NotFoundException("Client not found");
                });

        ClientMapper.updateEntity(entity, dto);

        Client saved = repository.save(entity);

        log.info("Client updated id={}, identification={}",
                saved.getId(), saved.getIdentification());

        return ClientMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
