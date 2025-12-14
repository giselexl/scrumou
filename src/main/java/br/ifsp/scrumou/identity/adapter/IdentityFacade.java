package br.ifsp.scrumou.identity.adapter;

import br.ifsp.scrumou.identity.client.IdentityClient;
import br.ifsp.scrumou.identity.dto.UserRequest;
import br.ifsp.scrumou.identity.dto.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class IdentityFacade {
    private final IdentityClient client;

    public IdentityFacade(IdentityClient client) {
        this.client = client;
    }

    public UserResponse createUser(UserRequest request) {
        try {
            return client.createUser(request);
        } catch (Exception ex) {
            throw new RuntimeException("Falha ao criar usuário no Identity Service", ex);
        }
    }

    public UserResponse getUsers() {
        try {
            return client.getUsers();
        } catch (Exception ex) {
            return null;
        }
    }

    public UserResponse getUserById(Long id) {
        try {
            return client.getUserById(id);
        } catch (Exception ex) {
            return null;
        }
    }

    public UserResponse resolveDeveloper(String identifier) {
        UserResponse user;
        try {
            Long id = Long.parseLong(identifier);
            user = client.getUserById(id);
        } catch (NumberFormatException ex) {
            user = client.getUserByEmail(identifier);
        }

        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado no Identity Service: " + identifier);
        }

        String userType = user.userType;
        if (userType == null || !"DEVELOPER".equalsIgnoreCase(userType)) {
            throw new IllegalArgumentException("Usuário não é do tipo DEVELOPER: " + identifier);
        }

        return user;
    }
}