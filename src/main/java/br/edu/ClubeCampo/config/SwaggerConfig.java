package br.edu.ClubeCampo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Sistema do Clube de Campo")
                        .version("1.0.0")
                        .description("API para gerenciamento de associados, dependentes, reservas e áreas do Clube de Campo.\n\n" +
                                     "Este sistema permite:\n" +
                                     "- Cadastro e validação de associados e dependentes\n" +
                                     "- Reserva de áreas como piscinas, churrasqueiras e quadras\n" +
                                     "- Controle de disponibilidade de espaços com e sem turma\n" +
                                     "- Consulta e administração geral pelo coordenador")
                        .contact(new Contact()
                                .name("Gustavo da Silva Ignacio")
                                .email("gustavoignacio280@gmail.com")
                                .url("https://github.com/GustavoIgn")));
    }
}