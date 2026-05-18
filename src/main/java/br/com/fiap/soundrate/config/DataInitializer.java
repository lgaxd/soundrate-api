package br.com.fiap.soundrate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import br.com.fiap.soundrate.entity.User;
import br.com.fiap.soundrate.entity.Album;
import br.com.fiap.soundrate.entity.Review;
import br.com.fiap.soundrate.repository.UserRepository;
import br.com.fiap.soundrate.repository.AlbumRepository;
import br.com.fiap.soundrate.repository.ReviewRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final ReviewRepository reviewRepository;
    
    @Override
    public void run(String... args) throws Exception {

        User user1 = new User();
        user1.setName("João Silva");
        user1.setEmail("joao@email.com");
        userRepository.save(user1);
        
        User user2 = new User();
        user2.setName("Maria Santos");
        user2.setEmail("maria@email.com");
        userRepository.save(user2);
        
        User user3 = new User();
        user3.setName("Pedro Costa");
        user3.setEmail("pedro@email.com");
        userRepository.save(user3);
        
        Album album1 = new Album();
        album1.setTitle("Abbey Road");
        album1.setArtist("The Beatles");
        album1.setGenre("Rock");
        album1.setReleaseYear(1969);
        albumRepository.save(album1);
        
        Album album2 = new Album();
        album2.setTitle("Thriller");
        album2.setArtist("Michael Jackson");
        album2.setGenre("Pop");
        album2.setReleaseYear(1982);
        albumRepository.save(album2);
        
        Album album3 = new Album();
        album3.setTitle("Kind of Blue");
        album3.setArtist("Miles Davis");
        album3.setGenre("Jazz");
        album3.setReleaseYear(1959);
        albumRepository.save(album3);
        
        Album album4 = new Album();
        album4.setTitle("OK Computer");
        album4.setArtist("Radiohead");
        album4.setGenre("Rock");
        album4.setReleaseYear(1997);
        albumRepository.save(album4);
        
        Album album5 = new Album();
        album5.setTitle("Random Access Memories");
        album5.setArtist("Daft Punk");
        album5.setGenre("Electronic");
        album5.setReleaseYear(2013);
        albumRepository.save(album5);
        
        Review review1 = new Review();
        review1.setScore(10);
        review1.setComment("Masterpiece! One of the best albums ever made.");
        review1.setUser(user1);
        review1.setAlbum(album1);
        reviewRepository.save(review1);
        
        Review review2 = new Review();
        review2.setScore(9);
        review2.setComment("Revolutionary album, incredible production.");
        review2.setUser(user2);
        review2.setAlbum(album2);
        reviewRepository.save(review2);
        
        Review review3 = new Review();
        review3.setScore(10);
        review3.setComment("Pure genius. Miles Davis at his best.");
        review3.setUser(user3);
        review3.setAlbum(album3);
        reviewRepository.save(review3);
        
        Review review4 = new Review();
        review4.setScore(8);
        review4.setComment("Great album, very innovative.");
        review4.setUser(user1);
        review4.setAlbum(album4);
        reviewRepository.save(review4);
        
        Review review5 = new Review();
        review5.setScore(9);
        review5.setComment("A masterpiece of electronic music.");
        review5.setUser(user2);
        review5.setAlbum(album5);
        reviewRepository.save(review5);
    }
}