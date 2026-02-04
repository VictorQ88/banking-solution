package com.banking.solution.utils;

import com.banking.solution.domain.Client;
import com.banking.solution.dto.ClientDTO;

public class ClientMapper {

    public static ClientDTO toDto(Client client) {
        if (client == null) {
            return null;
        }

        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getGender(),
                client.getAge(),
                client.getIdentification(),
                client.getAddress(),
                client.getPhone(),
                client.getClientId(),
                client.getPassword(),
                client.getActive()
        );
    }

    public static Client toEntity(ClientDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Client(
                dto.getName(),
                dto.getGender(),
                dto.getAge(),
                dto.getIdentification(),
                dto.getAddress(),
                dto.getPhone(),
                null,
                dto.getPassword(),
                dto.getActive()
        );
    }

    public static void updateEntity(Client entity, ClientDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setGender(dto.getGender());
        entity.setAge(dto.getAge());
        entity.setIdentification(dto.getIdentification());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        // entity.setPassword(dto.getPassword());
        entity.setActive(dto.getActive());
    }
}
