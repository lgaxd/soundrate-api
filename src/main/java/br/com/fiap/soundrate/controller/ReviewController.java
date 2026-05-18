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
import br.com.fiap.soundrate.dto.ReviewDTO;
import br.com.fiap.soundrate.entity.Review;
import br.com.fiap.soundrate.service.ReviewService;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    
    @GetMapping
    public ResponseEntity<Page<Review>> findAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Review>> findById(@PathVariable Long id) {
        Review review = reviewService.findById(id);
        
        EntityModel<Review> resource = EntityModel.of(review);
        Link selfLink = linkTo(methodOn(ReviewController.class).findById(id)).withSelfRel();
        Link allLink = linkTo(methodOn(ReviewController.class).findAll(null)).withRel("all-reviews");
        resource.add(selfLink, allLink);
        
        return ResponseEntity.ok(resource);
    }
    
    @PostMapping
    public ResponseEntity<Review> create(@Valid @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.create(reviewDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.update(id, reviewDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<Review>> findByAlbumId(@PathVariable Long albumId) {
        return ResponseEntity.ok(reviewService.findByAlbumId(albumId));
    }
}