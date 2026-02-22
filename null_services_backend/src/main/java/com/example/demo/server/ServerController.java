package com.example.demo.server;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping("/discover")
    public ResponseEntity<List<ServerResponse>> findAllServer(){
        return ResponseEntity.ok(serverService.findAll());
    }
    @GetMapping("/my-servers")
    public ResponseEntity<List<ServerResponse>> findMyServers(
            Authentication connectedUser
    ){
        return ResponseEntity.ok(serverService.findByUser(connectedUser));
    }
    @PostMapping
    public ResponseEntity<ServerResponse> saveServer(
            @RequestBody @Valid ServerRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(serverService.createServer(request,connectedUser));
    }

}
