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
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import br.com.fiap.soundrate.dto.ReviewDTO;
import br.com.fiap.soundrate.entity.Review;
import br.com.fiap.soundrate.service.ReviewService;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Endpoints para gerenciamento de avaliações de álbuns")
public class ReviewController {
    private final ReviewService reviewService;
    
    @Operation(
        summary = "Listar todas as avaliações",
        description = """
            Retorna uma lista paginada de avaliações. Por padrão, ordenado pela data de criação decrescente.
            
            **Exemplos de ordenação:**
            - `sort=score,desc` - Avaliações com notas mais altas primeiro
            - `sort=createdAt,asc` - Avaliações mais antigas primeiro
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<Review>> findAll(
            @Parameter(description = "Número da página (começa em 0)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Quantidade de itens por página", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Ordenação (ex: createdAt,desc ou score,asc)", example = "createdAt,desc") 
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.findAll(pageable));
    }
    
    @Operation(
        summary = "Buscar avaliação por ID",
        description = "Retorna uma avaliação específica com links HATEOAS para o usuário e álbum relacionados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliação encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Review>> findById(@Parameter(description = "ID da avaliação", example = "1") @PathVariable Long id) {
        Review review = reviewService.findById(id);
        
        EntityModel<Review> resource = EntityModel.of(review);
        Link selfLink = linkTo(methodOn(ReviewController.class).findById(id)).withSelfRel();
        Link allLink = linkTo(methodOn(ReviewController.class).findAll(0, 10, null, null)).withRel("all-reviews");
        Link userLink = linkTo(UserController.class).slash(review.getUser().getId()).withRel("user");
        Link albumLink = linkTo(AlbumController.class).slash(review.getAlbum().getId()).withRel("album");
        resource.add(selfLink, allLink, userLink, albumLink);
        
        return ResponseEntity.ok(resource);
    }
    
    @Operation(
        summary = "Criar nova avaliação",
        description = """
            Cria uma nova avaliação para um álbum.
            
            **Regras:**
            - A nota (score) deve ser entre 0 e 10
            - O usuário e o álbum devem existir no sistema
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos (nota fora do intervalo, IDs inválidos)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuário ou álbum não encontrado", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados da avaliação",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Exemplo de avaliação positiva",
                    value = """
                    {
                        "score": 10,
                        "comment": "Obra-prima! Álbum essencial para qualquer amante de música.",
                        "userId": 1,
                        "albumId": 1
                    }
                    """
                ),
                @ExampleObject(
                    name = "Exemplo de avaliação média",
                    value = """
                    {
                        "score": 7,
                        "comment": "Álbum bom, mas não excelente. Algumas músicas são fracas.",
                        "userId": 2,
                        "albumId": 4
                    }
                    """
                ),
                @ExampleObject(
                    name = "Exemplo de avaliação negativa",
                    value = """
                    {
                        "score": 3,
                        "comment": "Decepcionante. Esperava muito mais deste artista.",
                        "userId": 3,
                        "albumId": 5
                    }
                    """
                ),
                @ExampleObject(
                    name = "Exemplo de avaliação sem comentário",
                    value = """
                    {
                        "score": 8,
                        "comment": "",
                        "userId": 1,
                        "albumId": 3
                    }
                    """
                )
            }
        )
    )
    @PostMapping
    public ResponseEntity<Review> create(@Valid @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.create(reviewDTO), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Atualizar avaliação",
        description = "Atualiza a nota e/ou comentário de uma avaliação existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos (nota fora do intervalo)", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Review> update(
            @Parameter(description = "ID da avaliação", example = "1") @PathVariable Long id, 
            @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.update(id, reviewDTO));
    }
    
    @Operation(
        summary = "Deletar avaliação",
        description = "Remove uma avaliação do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Avaliação removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID da avaliação", example = "1") @PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Buscar avaliações por álbum",
        description = "Retorna todas as avaliações de um álbum específico. Útil para calcular a média de notas do álbum."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<Review>> findByAlbumId(@Parameter(description = "ID do álbum", example = "1") @PathVariable Long albumId) {
        return ResponseEntity.ok(reviewService.findByAlbumId(albumId));
    }
    
    @Operation(
        summary = "Buscar avaliações por usuário",
        description = "Retorna todas as avaliações feitas por um usuário específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> findByUserId(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.findByUserId(userId));
    }
}