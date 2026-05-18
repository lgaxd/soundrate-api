package br.com.fiap.soundrate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import br.com.fiap.soundrate.dto.UserDTO;
import br.com.fiap.soundrate.entity.User;
import br.com.fiap.soundrate.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints para gerenciamento de usuários")
public class UserController {
    private final UserService userService;
    
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista paginada de usuários. Suporta paginação e ordenação por qualquer campo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<User>> findAll(
            @Parameter(description = "Número da página (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Ordenação (ex: name,asc ou email,desc)", example = "name,asc")
            @RequestParam(required = false) String[] sort,
            
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }
    
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna um usuário específico com links HATEOAS para navegação"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findById(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        User user = userService.findById(id);
        
        EntityModel<User> resource = EntityModel.of(user);
        Link selfLink = linkTo(methodOn(UserController.class).findById(id)).withSelfRel();
        Link allLink = linkTo(methodOn(UserController.class).findAll(0, 10, null, null)).withRel("all-users");
        Link reviewsLink = linkTo(ReviewController.class).slash("user").slash(id).withRel("user-reviews");
        resource.add(selfLink, allLink, reviewsLink);
        
        return ResponseEntity.ok(resource);
    }
    
    @Operation(
        summary = "Criar novo usuário",
        description = "Cadastra um novo usuário no sistema. O email deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos (email duplicado ou formato inválido)", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do usuário a ser criado",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Exemplo de criação de usuário",
                    value = """
                    {
                        "name": "Ana Souza",
                        "email": "ana.souza@email.com"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Exemplo com dados inválidos",
                    value = """
                    {
                        "name": "",
                        "email": "email-invalido"
                    }
                    """
                )
            }
        )
    )
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados de um usuário existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id, 
            @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }
    
    @Operation(
        summary = "Deletar usuário",
        description = "Remove um usuário do sistema. Todas as avaliações associadas também serão removidas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}