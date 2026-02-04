package com.banking.solution.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import com.banking.solution.dto.ClientDTO;
import com.banking.solution.service.ClientService;

@SpringBootTest
class ClientControllerUnitTest {

    private final ClientService service = mock(ClientService.class);
    private final ClientController controller = new ClientController(service);

    @Test
    void create_ok() {
        ClientDTO req = new ClientDTO(null, "Victor", "M", 30, "010203", "Addr", "099", null, "1234", true);
        ClientDTO created = new ClientDTO(1L, "Victor", "M", 30, "010203", "Addr", "099", "CLI-xxx", null, true);

        when(service.create(req)).thenReturn(created);

        ClientDTO res = controller.create(req);

        assertEquals(created.getId(), res.getId());
    }
}
