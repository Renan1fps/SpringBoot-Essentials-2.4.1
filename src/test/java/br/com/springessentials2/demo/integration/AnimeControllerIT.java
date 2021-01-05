package br.com.springessentials2.demo.integration;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.domain.DevDojoUser;
import br.com.springessentials2.demo.repository.AnimeRepository;
import br.com.springessentials2.demo.repository.DevDojoUserRepository;
import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.util.AnimeCreator;
import br.com.springessentials2.demo.util.AnimePostRequestBodyCreator;
import br.com.springessentials2.demo.wapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRolesUser")
    TestRestTemplate testRestTemplateRoleUser;


    @Autowired
    @Qualifier(value = "testRestTemplateRolesAdmin")
    TestRestTemplate testRestTemplateRoleAdmin;


    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    DevDojoUserRepository devDojoUserRepository;

    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("renan1fps")
            .userName("renan1fps")
            .password("{bcrypt}$2a$10$ZWjLRBqniB.Gdt/78K97leDxiU8g6cePMeWyZg1QG/R3ALjx7tlku")
            .authorities("ROLE_USER")
            .build();
    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("emerson")
            .userName("emerson")
            .password("{bcrypt}$2a$10$ZWjLRBqniB.Gdt/78K97leDxiU8g6cePMeWyZg1QG/R3ALjx7tlku")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();


    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRolesUser")
        public TestRestTemplate testRestTemplateRolesUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("renan", "20030927");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRolesAdmin")
        public TestRestTemplate testRestTemplateRolesAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("emerson", "20030927");
            return new TestRestTemplate(restTemplateBuilder);
        }

    }

    @Test
    @DisplayName("Returns list of animes inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_whenSuccessful() {

        devDojoUserRepository.save(USER);

        animeRepository.save(AnimeCreator.createAnime());

        String expectedName = AnimeCreator.createValidAnime().getName();

        PageableResponse<Anime> exchange = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(exchange.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Returns list of animes when successful")
    void listAll_ReturnsListOfAnimes_whenSuccessful() {

        devDojoUserRepository.save(USER);

        animeRepository.save(AnimeCreator.createAnime());

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Returns id of anime when successful")
    void findById_ReturnsIdOfAnime_whenSuccessful() {

        devDojoUserRepository.save(USER);

        Anime save = animeRepository.save(AnimeCreator.createAnime());

        Long expected = save.getId();

        Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expected);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expected);

    }

    @Test
    @DisplayName("Find by name returns an list of anime when successful")
    void findByName_ReturnsListOfAnimes_whenSuccessful() {

        devDojoUserRepository.save(USER);

        Anime save = animeRepository.save(AnimeCreator.createAnime());

        String expectedName = save.getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animeList = testRestTemplateRoleUser.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);


        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Find by name returns an empty list  when anime is not found")
    void findByName_ReturnsListEmpty_whenAnimeIsNotFound() {

        devDojoUserRepository.save(USER);

        List<Anime> animeList = testRestTemplateRoleUser.exchange("/animes/find?name=charuto", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animeList).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void save_ReturnsAnime_whenSuccessful() {

        devDojoUserRepository.save(USER);

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.animePostRequestBody();

        ResponseEntity<Anime> anime = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);

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

        devDojoUserRepository.save(USER);

        Anime save = animeRepository.save(AnimeCreator.createAnime());

        save.setName("new name");

        ResponseEntity<Void> exchange = testRestTemplateRoleUser.exchange("/animes", HttpMethod.PUT,
                new HttpEntity<>(save), Void.class);

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(exchange).isNotNull();

    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnime());
        devDojoUserRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("delete returns 403 when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnime());
        devDojoUserRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


}
