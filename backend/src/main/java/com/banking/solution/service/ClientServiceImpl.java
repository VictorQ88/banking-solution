package com.banking.solution.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.banking.solution.domain.Client;
import com.banking.solution.dto.ClientDTO;
import com.banking.solution.repository.ClientRepository;
import com.banking.solution.utils.ClientMapper;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientDTO create(ClientDTO dto) {
        Client entity = ClientMapper.toEntity(dto);
        Client saved = repository.save(entity);
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
        Client entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        ClientMapper.updateEntity(entity, dto);

        Client saved = repository.save(entity);
        return ClientMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
