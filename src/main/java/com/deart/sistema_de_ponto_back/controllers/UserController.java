package com.deart.sistema_de_ponto_back.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deart.sistema_de_ponto_back.dtos.requests.UserCreateRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserLoginRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateCpfRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateEmailRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdatePasswordRequest;
import com.deart.sistema_de_ponto_back.dtos.requests.UserUpdateRequest;
import com.deart.sistema_de_ponto_back.dtos.responses.LoginResponse;
import com.deart.sistema_de_ponto_back.dtos.responses.UserResponse;
import com.deart.sistema_de_ponto_back.services.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/users")
public class UserController {
    
    private UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> users = service.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<UserResponse>> findAllActiveUsers() {
        List<UserResponse> users = service.findAllActives();
        return ResponseEntity.ok().body(users);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        LoginResponse response = service.login(loginRequest);
        return ResponseEntity.ok().body(response);
    }

    // adicionar endpoints de recuperação de senha
    // POST: /forgot-password
    // POST: /reset-password

    // Verificar ROLE
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest createRequest) {
        UserResponse userCreated = service.create(createRequest);
        return ResponseEntity.ok().body(userCreated);
    }

    @PutMapping("/{externalId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserResponse userUpdated = service.update(externalId, updateRequest);
        return ResponseEntity.ok().body(userUpdated);
    }
    
    // User logado == User para alterar  || ADMIN ou SUPERV.
    @PatchMapping("/{externalId}/email")
    public void updateEmail(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateEmailRequest request){
        // atualização de email
    }
    
    // deve estar logado e ser o usuário ou o ADMIN
    @PatchMapping("/{externalId}/password")
    public void updatePassword(@PathVariable UUID externalId, @Valid @RequestBody UserUpdatePasswordRequest request){
        // atualização de senha
    }
    
    // Só ADMIN ou SUPERV.
    @PatchMapping("/{externalId}/cpf")
    public void updateCpf(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateCpfRequest request){
        // atualização de cpf
    }

    // só ADMIN ou o SUPERV. do user (se bolsista)
    @DeleteMapping("/{externalId}")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable UUID externalId){
        UserResponse desativated = service.deactivate(externalId);
        return ResponseEntity.ok().body(desativated);
    }

    // só ADMIN ou o SUPERV. do user (se bolsista)
    @PostMapping("/{externalId}/activate")
    public ResponseEntity<UserResponse> activateUser(@PathVariable UUID externalId) {
        UserResponse activated = service.activate(externalId);
        return ResponseEntity.ok().body(activated);
    }
    
}
