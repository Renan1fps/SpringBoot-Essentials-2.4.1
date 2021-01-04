package br.com.springessentials2.demo.integration;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.repository.AnimeRepository;
import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.util.AnimeCreator;
import br.com.springessentials2.demo.util.AnimePostRequestBodyCreator;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    @DisplayName("Returns list of animes when successful")
    void listAll_ReturnsListOfAnimes_whenSuccessful() {

        animeRepository.save(AnimeCreator.createAnime());

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Returns id of anime when successful")
    void findById_ReturnsIdOfAnime_whenSuccessful() {

        Anime save = animeRepository.save(AnimeCreator.createAnime());

        Long expected = save.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expected);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expected);

    }

    @Test
    @DisplayName("Find by name returns an list of anime when successful")
    void findByName_ReturnsListOfAnimes_whenSuccessful() {

        Anime save = animeRepository.save(AnimeCreator.createAnime());

        String expectedName = save.getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animeList = testRestTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);


        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Find by name returns an empty list  when anime is not found")
    void findByName_ReturnsListEmpty_whenAnimeIsNotFound() {

        List<Anime> animeList = testRestTemplate.exchange("/animes/find?name=charuto", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void save_ReturnsAnime_whenSuccessful() {

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.animePostRequestBody();

        ResponseEntity<Anime> anime = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Long id = Objects.requireNonNull(anime.getBody()).getId();

        Assertions.assertThat(anime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getBody().getId()).isNotNull();
        Assertions.assertThat(anime.getBody().getId()).isEqualTo(id);
        Assertions.assertThat(anime.getBody()).isNotNull();


    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_ReturnsAnime_whenSuccessful() {

        Anime save = animeRepository.save(AnimeCreator.createAnime());

        save.setName("new name");

        ResponseEntity<Void> exchange = testRestTemplate.exchange("/animes", HttpMethod.PUT,
                new HttpEntity<>(save), Void.class);

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(exchange).isNotNull();

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_whenSuccessful() {
        Anime save = animeRepository.save(AnimeCreator.createAnime());


        ResponseEntity<Void> exchange = testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE,
                null, Void.class, save.getId());

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(exchange).isNotNull();

    }


}
