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
import com.deart.sistema_de_ponto_back.mappers.UserMapper;
import com.deart.sistema_de_ponto_back.models.User;
import com.deart.sistema_de_ponto_back.services.InternService;
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
    
    private final UserService userService;
    private final InternService internService;
    private final UserMapper mapper;

    public UserController(UserService userService, InternService internService, UserMapper mapper){
        this.userService = userService;
        this.internService = internService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> users = userService.findAll()
            .stream().map(mapper::toResponse)
            .toList();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<UserResponse>> findAllActiveUsers() {
        List<UserResponse> users = userService.findAllActives()
            .stream().map(mapper::toResponse)
            .toList();
        return ResponseEntity.ok().body(users);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        User user = userService.login(loginRequest);
        UUID internExternalId = internService.findInternExternalIdByUser(user);
        // String token = tokenService.generateToken(user);
        // Long expiresIn = tokenService.getExpirationTime();
        LoginResponse response = mapper.toLoginResponse(user, internExternalId, null, null);   
        return ResponseEntity.ok().body(response);
    }

    // adicionar endpoints de recuperação de senha
    // POST: /forgot-password
    // POST: /reset-password

    // Verificar ROLE
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest createRequest) {
        User entity = userService.create(createRequest);
        UserResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{externalId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateRequest updateRequest) {
        User entity = userService.update(externalId, updateRequest);
        UserResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok().body(response);
    }
    
    // User logado == User para alterar  || ADMIN ou SUPERV.
    // mudar retorno
    @PatchMapping("/{externalId}/email")
    public void updateEmail(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateEmailRequest request){
        // atualização de email
    }
    
    // deve estar logado e ser o usuário ou o ADMIN
    // mudar retorno
    @PatchMapping("/{externalId}/password")
    public void updatePassword(@PathVariable UUID externalId, @Valid @RequestBody UserUpdatePasswordRequest request){
        // atualização de senha
    }
    
    // Só ADMIN ou SUPERV.
    // mudar retorno
    @PatchMapping("/{externalId}/cpf")
    public void updateCpf(@PathVariable UUID externalId, @Valid @RequestBody UserUpdateCpfRequest request){
        // atualização de cpf
    }

    // só ADMIN ou o SUPERV. do user (se bolsista)
    @DeleteMapping("/{externalId}")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable UUID externalId){
        User entity = userService.deactivate(externalId);
        UserResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok().body(response);
    }

    // só ADMIN ou o SUPERV. do user (se bolsista)
    @PostMapping("/{externalId}/activate")
    public ResponseEntity<UserResponse> activateUser(@PathVariable UUID externalId) {
        User entity = userService.activate(externalId);
        UserResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok().body(response);
    }
    
}
