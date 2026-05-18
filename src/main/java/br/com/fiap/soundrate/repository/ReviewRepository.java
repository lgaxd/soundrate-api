package br.com.fiap.soundrate.repository;

import br.com.fiap.soundrate.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAlbumId(Long albumId);
    List<Review> findByUserId(Long userId);

}