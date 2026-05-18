package br.com.fiap.soundrate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.soundrate.dto.UserDTO;
import br.com.fiap.soundrate.entity.User;
import br.com.fiap.soundrate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    @Cacheable(value = "users", key = "#pageable")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User create(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user);
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User update(Long id, UserDTO userDTO) {
        User user = findById(id);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user);
    }
    
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}