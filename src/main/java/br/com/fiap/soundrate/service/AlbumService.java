package br.com.fiap.soundrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import br.com.fiap.soundrate.dto.AlbumDTO;
import br.com.fiap.soundrate.dto.AlbumProjection;
import br.com.fiap.soundrate.entity.Album;
import br.com.fiap.soundrate.repository.AlbumRepository;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    
    @Cacheable(value = "albums", key = "#pageable")
    public Page<Album> findAll(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }
    
    @Cacheable(value = "albums", key = "#id")
    public Album findById(Long id) {
        return albumRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Álbum não encontrado com id: " + id));
    }
    
    @Transactional
    @CacheEvict(value = "albums", allEntries = true)
    public Album create(AlbumDTO albumDTO) {
        Album album = new Album();
        album.setTitle(albumDTO.getTitle());
        album.setArtist(albumDTO.getArtist());
        album.setGenre(albumDTO.getGenre());
        album.setReleaseYear(albumDTO.getReleaseYear());
        return albumRepository.save(album);
    }
    
    @Transactional
    @CacheEvict(value = "albums", allEntries = true)
    public Album update(Long id, AlbumDTO albumDTO) {
        Album album = findById(id);
        album.setTitle(albumDTO.getTitle());
        album.setArtist(albumDTO.getArtist());
        album.setGenre(albumDTO.getGenre());
        album.setReleaseYear(albumDTO.getReleaseYear());
        return albumRepository.save(album);
    }
    
    @Transactional
    @CacheEvict(value = "albums", allEntries = true)
    public void delete(Long id) {
        Album album = findById(id);
        albumRepository.delete(album);
    }
    
    public Page<Album> findByGenre(String genre, Pageable pageable) {
        return albumRepository.findByGenre(genre, pageable);
    }
    
    public List<AlbumProjection> getProjectionByGenre(String genre) {
        return albumRepository.findByGenre(genre);
    }
}