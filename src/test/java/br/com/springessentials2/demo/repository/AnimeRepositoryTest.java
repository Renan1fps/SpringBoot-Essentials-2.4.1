package br.com.springessentials2.demo.repository;

import br.com.springessentials2.demo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {

    @Autowired
    AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when successful")
    void save_PersistAnime_WhenSuccessful() {

        Anime animeToBeSaved = createdAnime();
        Anime save = animeRepository.save(animeToBeSaved);
        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isNotNull();
        Assertions.assertThat(save.getName()).isEqualTo(animeToBeSaved.getName());
    }

    private Anime createdAnime() {
        return Anime.builder().name("Bob esponja").build();
    }

}