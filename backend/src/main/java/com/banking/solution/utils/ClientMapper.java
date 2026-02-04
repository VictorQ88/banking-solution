package com.banking.solution.utils;

import com.banking.solution.domain.Client;
import com.banking.solution.dto.ClientDTO;

public class ClientMapper {

    public static ClientDTO toDto(Client client) {
        if (client == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        dto.name = client.getName();
        dto.gender = client.getGender();
        dto.age = client.getAge();
        dto.identification = client.getIdentification();
        dto.address = client.getAddress();
        dto.phone = client.getPhone();
        dto.clientId = client.getClientId();
        dto.active = client.getActive();
        return dto;
    }

    public static Client toEntity(ClientDTO dto) {
        if (dto == null) {
            return null;
        }

        Client client = new Client(
                dto.name,
                dto.gender,
                dto.age,
                dto.identification,
                dto.address,
                dto.phone,
                dto.clientId,
                dto.password,
                dto.active
        );
        return client;
    }

    public static void updateEntity(Client entity, ClientDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.name);
        entity.setGender(dto.gender);
        entity.setAge(dto.age);
        entity.setIdentification(dto.identification);
        entity.setAddress(dto.address);
        entity.setPhone(dto.phone);
        entity.setPassword(dto.password);
        entity.setActive(dto.active);
    }
}
