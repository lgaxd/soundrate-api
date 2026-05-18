package br.com.fiap.soundrate.controller;

import br.com.fiap.soundrate.dto.UserDTO;
import br.com.fiap.soundrate.entity.User;
import br.com.fiap.soundrate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista paginada de usuários")
    @GetMapping
    public ResponseEntity<Page<User>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(summary = "Obter usuário por ID", description = "Busca os detalhes de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Criar novo usuário", description = "Cadastra um novo usuário no sistema com e-mail único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail duplicado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> create(
        @Valid 
        @org.springframework.web.bind.annotation.RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados de entrada para a criação do usuário",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de criação de usuário",
                    value = "{\n  \"name\": \"Lucas Silva\",\n  \"email\": \"lucas.silva@email.com\"\n}"
                )
            )
        ) UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
        @PathVariable Long id, 
        @Valid 
        @org.springframework.web.bind.annotation.RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Novos dados do usuário",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de atualização de usuário",
                    value = "{\n  \"name\": \"Lucas S. Alcântara\",\n  \"email\": \"lucas.novoemail@email.com\"\n}"
                )
            )
        ) UserDTO userDTO) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @Operation(summary = "Excluir usuário", description = "Remove um usuário permanentemente do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}