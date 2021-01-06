package br.com.springessentials2.demo.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {

    @NotEmpty(message = "The anime name cannot  be empty")
    @Schema(description = "This is the anime's name",example = "Boruto: naruto next generation")
    private String name;
}
