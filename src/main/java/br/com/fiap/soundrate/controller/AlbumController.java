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
import br.com.fiap.soundrate.dto.AlbumDTO;
import br.com.fiap.soundrate.dto.AlbumProjection;
import br.com.fiap.soundrate.entity.Album;
import br.com.fiap.soundrate.service.AlbumService;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
@Tag(name = "Albums", description = "Endpoints para gerenciamento de álbuns musicais")
public class AlbumController {
    private final AlbumService albumService;
    
    @Operation(
        summary = "Listar todos os álbuns",
        description = """
            Retorna uma lista paginada de álbuns. Suporta paginação e ordenação por qualquer campo.
            
            **Exemplos de ordenação:**
            - `sort=title,asc` - Ordena por título A-Z
            - `sort=releaseYear,desc` - Ordena por ano decrescente (mais novos primeiro)
            - `sort=artist,asc&sort=title,asc` - Ordena por artista e depois por título
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<Album>> findAll(
            @Parameter(description = "Número da página (começa em 0)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Quantidade de itens por página (máximo 50)", example = "5") 
            @RequestParam(defaultValue = "5") int size,
            
            @Parameter(description = "Ordenação: campo,direção (asc ou desc)", example = "title,asc") 
            @RequestParam(required = false) String sort,
            
            @PageableDefault(size = 5, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(albumService.findAll(pageable));
    }
    
    @Operation(
        summary = "Buscar álbum por ID",
        description = "Retorna um álbum específico com links HATEOAS e suas avaliações"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Álbum encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Album>> findById(@Parameter(description = "ID do álbum", example = "1") @PathVariable Long id) {
        Album album = albumService.findById(id);
        
        EntityModel<Album> resource = EntityModel.of(album);
        Link selfLink = linkTo(methodOn(AlbumController.class).findById(id)).withSelfRel();
        Link allLink = linkTo(methodOn(AlbumController.class).findAll(0, 5, null, null)).withRel("all-albums");
        Link reviewsLink = linkTo(ReviewController.class).slash("album").slash(id).withRel("album-reviews");
        resource.add(selfLink, allLink, reviewsLink);
        
        return ResponseEntity.ok(resource);
    }
    
    @Operation(
        summary = "Criar novo álbum",
        description = "Cadastra um novo álbum musical no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Álbum criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do álbum a ser criado",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Exemplo de criação de álbum - Rock",
                    value = """
                    {
                        "title": "The Dark Side of the Moon",
                        "artist": "Pink Floyd",
                        "genre": "Rock",
                        "releaseYear": 1973
                    }
                    """
                ),
                @ExampleObject(
                    name = "Exemplo de criação de álbum - Pop",
                    value = """
                    {
                        "title": "25",
                        "artist": "Adele",
                        "genre": "Pop",
                        "releaseYear": 2015
                    }
                    """
                ),
                @ExampleObject(
                    name = "Exemplo de criação de álbum - Jazz",
                    value = """
                    {
                        "title": "A Love Supreme",
                        "artist": "John Coltrane",
                        "genre": "Jazz",
                        "releaseYear": 1965
                    }
                    """
                )
            }
        )
    )
    @PostMapping
    public ResponseEntity<Album> create(@Valid @RequestBody AlbumDTO albumDTO) {
        return new ResponseEntity<>(albumService.create(albumDTO), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Atualizar álbum",
        description = "Atualiza os dados de um álbum existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Álbum atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Album> update(
            @Parameter(description = "ID do álbum", example = "1") @PathVariable Long id, 
            @Valid @RequestBody AlbumDTO albumDTO) {
        return ResponseEntity.ok(albumService.update(id, albumDTO));
    }
    
    @Operation(
        summary = "Deletar álbum",
        description = "Remove um álbum do sistema. Todas as avaliações associadas também serão removidas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Álbum removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do álbum", example = "1") @PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Buscar álbuns por gênero (paginado)",
        description = "Retorna uma lista paginada de álbuns de um determinado gênero musical"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum álbum encontrado para o gênero", content = @Content)
    })
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<Album>> findByGenre(
            @Parameter(description = "Gênero musical (Rock, Pop, Jazz, Electronic, etc)", example = "Rock") 
            @PathVariable String genre, 
            Pageable pageable) {
        return ResponseEntity.ok(albumService.findByGenre(genre, pageable));
    }
    
    @Operation(
        summary = "Projection de álbuns por gênero",
        description = """
            Retorna apenas os campos específicos (título, artista e gênero) de álbuns de um determinado gênero.
            Ideal para consultas que não precisam de todos os dados do álbum.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum álbum encontrado para o gênero", content = @Content)
    })
    @GetMapping("/projection/{genre}")
    public ResponseEntity<List<AlbumProjection>> getProjectionByGenre(
            @Parameter(description = "Gênero musical para filtrar", example = "Rock") 
            @PathVariable String genre) {
        return ResponseEntity.ok(albumService.getProjectionByGenre(genre));
    }
}