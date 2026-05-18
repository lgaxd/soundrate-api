package br.com.fiap.soundrate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private Long id;
    
    @NotBlank(message = "O título é obrigatório")
    private String title;
    
    @NotBlank(message = "O artista é obrigatório")
    private String artist;
    
    private String genre;
    
    @Positive(message = "O ano de lançamento deve ser positivo")
    private Integer releaseYear;
}