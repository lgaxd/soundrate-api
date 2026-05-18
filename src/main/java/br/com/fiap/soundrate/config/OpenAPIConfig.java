package br.com.fiap.soundrate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SoundRate API")
                .version("1.0")
                .description("API para avaliação de álbuns musicais - Inspirada no Rate Your Music")
                .contact(new Contact()
                    .name("SoundRate Team")
                    .email("contato@soundrate.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")))
            .addTagsItem(new Tag().name("Users").description("Operações relacionadas a usuários"))
            .addTagsItem(new Tag().name("Albums").description("Operações relacionadas a álbuns"))
            .addTagsItem(new Tag().name("Reviews").description("Operações relacionadas a avaliações"));
    }
}