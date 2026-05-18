package br.com.fiap.soundrate.controller;

import br.com.fiap.soundrate.dto.ReviewDTO;
import br.com.fiap.soundrate.entity.Review;
import br.com.fiap.soundrate.service.ReviewService;
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
@RequestMapping("/api/reviews")
@Tag(name = "Avaliações", description = "Endpoints para gerenciamento de avaliações de álbuns")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Listar todas as avaliações", description = "Retorna uma lista paginada com todas as avaliações registradas")
    @GetMapping
    public ResponseEntity<Page<Review>> getAll(Pageable pageable) {
        return ResponseEntity.ok(reviewService.findAll(pageable));
    }

    @Operation(summary = "Obter avaliação por ID", description = "Busca uma avaliação específica por meio de seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @Operation(summary = "Criar nova avaliação", description = "Vincula uma nova nota e comentário de um usuário a um álbum")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Avaliação registrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou IDs de usuário/álbum inexistentes", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Review> create(
        @Valid 
        @org.springframework.web.bind.annotation.RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para criação da avaliação",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de avaliação",
                    value = "{\n  \"score\": 9,\n  \"comment\": \"Excelente álbum, produção impecável!\",\n  \"userId\": 1,\n  \"albumId\": 1\n}"
                )
            )
        ) ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.create(reviewDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar avaliação", description = "Edita a nota e o comentário de uma avaliação já existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados informados são inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Review> update(
        @PathVariable Long id, 
        @Valid 
        @org.springframework.web.bind.annotation.RequestBody 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados atualizados da avaliação",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de alteração de avaliação",
                    value = "{\n  \"score\": 10,\n  \"comment\": \"Mudei de ideia, merece nota máxima! Músicas espetaculares.\",\n  \"userId\": 1,\n  \"albumId\": 1\n}"
                )
            )
        ) ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.update(id, reviewDTO));
    }

    @Operation(summary = "Excluir avaliação", description = "Remove permanentemente uma avaliação do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Avaliação removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}