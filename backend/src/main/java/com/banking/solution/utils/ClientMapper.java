package com.banking.solution.utils;

import com.banking.solution.domain.Client;
import com.banking.solution.dto.ClientDTO;

public final class ClientMapper {

    public static Client toEntity(ClientDTO dto) {
        return new Client(
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
    }

    public static ClientDTO toDto(Client entity) {
        ClientDTO dto = new ClientDTO();
        dto.name = entity.getName();
        dto.gender = entity.getGender();
        dto.age = entity.getAge();
        dto.identification = entity.getIdentification();
        dto.address = entity.getAddress();
        dto.phone = entity.getPhone();
        dto.clientId = entity.getClientId();
        dto.active = entity.getActive();
        return dto;
    }
}
