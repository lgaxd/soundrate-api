package br.com.fiap.soundrate.controller;

import br.com.fiap.soundrate.dto.AlbumDTO;
import br.com.fiap.soundrate.entity.Album;
import br.com.fiap.soundrate.service.AlbumService;
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
@RequestMapping("/api/albums")
@Tag(name = "Álbuns", description = "Endpoints para gerenciamento de álbuns")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Operation(summary = "Listar todos os álbuns", description = "Retorna uma lista paginada de álbuns com a sua nota média")
    @GetMapping
    public ResponseEntity<Page<Album>> getAll(Pageable pageable) {
        return ResponseEntity.ok(albumService.findAll(pageable));
    }

    @Operation(summary = "Obter álbum por ID", description = "Busca detalhes de um álbum específico através do ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Álbum encontrado"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Album> getById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    @Operation(summary = "Criar novo álbum", description = "Cadastra um novo álbum no catálogo do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Álbum criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados informados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Album> create(
        @Valid 
        @org.springframework.web.bind.annotation.RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Informações do novo álbum",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de criação de álbum",
                    value = "{\n  \"title\": \"Random Access Memories\",\n  \"artist\": \"Daft Punk\",\n  \"genre\": \"Electronic\",\n  \"releaseYear\": 2013\n}"
                )
            )
        ) AlbumDTO albumDTO) {
        return new ResponseEntity<>(albumService.create(albumDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar álbum", description = "Modifica os dados estruturais de um álbum existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Álbum atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados fornecidos são inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Album> update(
        @PathVariable Long id, 
        @Valid 
        @org.springframework.web.bind.annotation.RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Novos dados para atualização do álbum",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de atualização de álbum",
                    value = "{\n  \"title\": \"Random Access Memories (10th Anniversary)\",\n  \"artist\": \"Daft Punk\",\n  \"genre\": \"Electronic / Nu-Disco\",\n  \"releaseYear\": 2023\n}"
                )
            )
        ) AlbumDTO albumDTO) {
        return ResponseEntity.ok(albumService.update(id, albumDTO));
    }

    @Operation(summary = "Excluir álbum", description = "Remove um álbum do sistema permanentemente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Álbum excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}