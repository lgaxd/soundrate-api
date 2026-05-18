package br.com.fiap.soundrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import br.com.fiap.soundrate.dto.ReviewDTO;
import br.com.fiap.soundrate.entity.Review;
import br.com.fiap.soundrate.repository.ReviewRepository;
import br.com.fiap.soundrate.repository.UserRepository;
import br.com.fiap.soundrate.repository.AlbumRepository;
import br.com.fiap.soundrate.entity.User;
import br.com.fiap.soundrate.entity.Album;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    
    @Cacheable(value = "reviews", key = "#pageable")
    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }
    
    @Cacheable(value = "reviews", key = "#id")
    public Review findById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review não encontrada com id: " + id));
    }

    @Cacheable(value = "reviews", key = "'user_' + #userId")
    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
    
    @Transactional
    @CacheEvict(value = {"reviews", "albums"}, allEntries = true)
    public Review create(ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User não encontrado com id: " + reviewDTO.getUserId()));
        Album album = albumRepository.findById(reviewDTO.getAlbumId())
            .orElseThrow(() -> new RuntimeException("Álbum não encontrado com id: " + reviewDTO.getAlbumId()));
        
        Review review = new Review();
        review.setScore(reviewDTO.getScore());
        review.setComment(reviewDTO.getComment());
        review.setUser(user);
        review.setAlbum(album);
        
        return reviewRepository.save(review);
    }
    
    @Transactional
    @CacheEvict(value = {"reviews", "albums"}, allEntries = true)
    public Review update(Long id, ReviewDTO reviewDTO) {
        Review review = findById(id);
        review.setScore(reviewDTO.getScore());
        review.setComment(reviewDTO.getComment());
        return reviewRepository.save(review);
    }
    
    @Transactional
    @CacheEvict(value = {"reviews", "albums"}, allEntries = true)
    public void delete(Long id) {
        Review review = findById(id);
        reviewRepository.delete(review);
    }
    
    public List<Review> findByAlbumId(Long albumId) {
        return reviewRepository.findByAlbumId(albumId);
    }
}