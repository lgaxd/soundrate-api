package br.com.fiap.soundrate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    
    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota deve ser entre 0 e 10")
    @Max(value = 10, message = "A nota deve ser entre 0 e 10")
    private Integer score;
    
    private String comment;
    
    private LocalDateTime createdAt;
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;
    
    @NotNull(message = "ID do álbum é obrigatório")
    private Long albumId;
}