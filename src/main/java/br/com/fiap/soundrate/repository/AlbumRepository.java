package br.com.fiap.soundrate.repository;

import br.com.fiap.soundrate.entity.Album;
import br.com.fiap.soundrate.dto.AlbumProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Page<Album> findByGenre(String genre, Pageable pageable);
    List<AlbumProjection> findByGenre(String genre);
}