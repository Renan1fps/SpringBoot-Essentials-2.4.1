package br.com.springessentials2.demo.controller;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.requests.AnimePutRequestBody;
import br.com.springessentials2.demo.service.AnimeService;
import br.com.springessentials2.demo.util.AnimeCreator;
import br.com.springessentials2.demo.util.AnimePostRequestBodyCreator;
import br.com.springessentials2.demo.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    AnimeController animeController;

    @Mock
    AnimeService animeServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())).thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNoPageable()).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("Returns list of animes inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_whenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Returns list of animes when successful")
    void listAll_ReturnsListOfAnimes_whenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);


        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Returns id of anime when successful")
    void findById_ReturnsIdOfAnime_whenSuccessful() {

        Long expected = AnimeCreator.createValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expected);

    }

    @Test
    @DisplayName("Find by name returns an list of anime when successful")
    void findByName_ReturnsListOfAnimes_whenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeController.findByName("anime").getBody();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);


        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Find by name returns an empty list  when anime is not found")
    void findByName_ReturnsListEmpty_whenAnimeIsNotFound() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeController.findByName("anime").getBody();

        Assertions.assertThat(animeList).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void save_ReturnsAnime_whenSuccessful() {

        Anime anime = animeController.save(AnimePostRequestBodyCreator.animePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_ReturnsAnime_whenSuccessful() {

        ResponseEntity<Void> replace = animeController.replace(AnimePutRequestBodyCreator.animePutRequestBody());

        Assertions.assertThat(replace).isNotNull();

        Assertions.assertThat(replace.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_whenSuccessful() {

        ResponseEntity<Void> delete = animeController.delete(1);

        Assertions.assertThat(delete).isNotNull();

        Assertions.assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }


}