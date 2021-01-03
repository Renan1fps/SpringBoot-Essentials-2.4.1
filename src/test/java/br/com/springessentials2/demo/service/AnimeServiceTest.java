package br.com.springessentials2.demo.service;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    AnimeService animeService;

    @Mock
    AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));

    }

    @Test
    @DisplayName("Returns list of animes inside page object when successful")
    void listAll_ReturnsListOfAnimesInsidePageObject_whenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Returns list of animes when successful")
    void listAll_ReturnsListOfAnimes_whenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeService.listAllNoPageable();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);


        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsIdOfAnime_whenSuccessful() {

        Long expected = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expected);

    }

    @Test
    @DisplayName("Find by name returns an list of anime when successful")
    void findByName_ReturnsListOfAnimes_whenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeService.findByName("anime");

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().hasSize(1);


        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("Find by name returns an empty list  when anime is not found")
    void findByName_ReturnsListEmpty_whenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeService.findByName("anime");

        Assertions.assertThat(animeList).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("Save returns  anime when successful")
    void save_ReturnsAnime_whenSuccessful() {

        Anime anime = animeService.save(AnimePostRequestBodyCreator.animePostRequestBody());

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_ReturnsAnime_whenSuccessful() {

  Assertions.assertThatCode(()->animeService.replace(AnimePutRequestBodyCreator.animePutRequestBody()))
          .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_whenSuccessful() {

      Assertions.assertThatCode(()->animeService.delete(1))
              .doesNotThrowAnyException();

    }


}