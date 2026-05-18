package br.com.fiap.soundrate.controller;

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
public class AlbumController {
    private final AlbumService albumService;
    
    @GetMapping
    public ResponseEntity<Page<Album>> findAll(
            @PageableDefault(size = 5, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(albumService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Album>> findById(@PathVariable Long id) {
        Album album = albumService.findById(id);
        
        EntityModel<Album> resource = EntityModel.of(album);
        Link selfLink = linkTo(methodOn(AlbumController.class).findById(id)).withSelfRel();
        Link allLink = linkTo(methodOn(AlbumController.class).findAll(null)).withRel("all-albums");
        resource.add(selfLink, allLink);
        
        return ResponseEntity.ok(resource);
    }
    
    @PostMapping
    public ResponseEntity<Album> create(@Valid @RequestBody AlbumDTO albumDTO) {
        return new ResponseEntity<>(albumService.create(albumDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Album> update(@PathVariable Long id, @Valid @RequestBody AlbumDTO albumDTO) {
        return ResponseEntity.ok(albumService.update(id, albumDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<Album>> findByGenre(@PathVariable String genre, Pageable pageable) {
        return ResponseEntity.ok(albumService.findByGenre(genre, pageable));
    }
    
    @GetMapping("/projection/{genre}")
    public ResponseEntity<List<AlbumProjection>> getProjectionByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(albumService.getProjectionByGenre(genre));
    }
}