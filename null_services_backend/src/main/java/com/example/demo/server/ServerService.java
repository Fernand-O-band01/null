package com.example.demo.server;

import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerResponse createServer(
            ServerRequest request,
            Authentication connectedUser
    ){
        User user = (User) connectedUser.getPrincipal();

        Server newServer = Server.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .owner(user)
                .members(Set.of(user))
                .build();

        Server savedServer = serverRepository.save(newServer);

        return mapToResponse(savedServer);

    }

    public List<ServerResponse> findAll() {
        return serverRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ServerResponse> findByUser(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        return serverRepository.findAllByMembersId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ServerResponse mapToResponse(Server server) {
        return ServerResponse.builder()
                .id(server.getId())
                .name(server.getName())
                .imageUrl(server.getImageUrl())
                .build();
    }
}
