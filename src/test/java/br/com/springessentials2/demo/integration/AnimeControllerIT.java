package br.com.springessentials2.demo.integration;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.repository.AnimeRepository;
import br.com.springessentials2.demo.util.AnimeCreator;
import br.com.springessentials2.demo.wapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AnimeControllerIT {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    AnimeRepository animeRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Returns list of animes inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_whenSuccessful() {

        animeRepository.save(AnimeCreator.createAnime());

        String expectedName = AnimeCreator.createValidAnime().getName();

        PageableResponse<Anime> exchange = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(exchange.toList().get(0).getName()).isEqualTo(expectedName);

    }

}
