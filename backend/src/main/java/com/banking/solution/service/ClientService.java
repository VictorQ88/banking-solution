package com.banking.solution.service;

import java.util.List;

import com.banking.solution.dto.ClientDTO;

public interface ClientService {

    ClientDTO create(ClientDTO dto);

    List<ClientDTO> findAll();

    ClientDTO findById(Long id);

    ClientDTO update(Long id, ClientDTO dto);

    void delete(Long id);
}
