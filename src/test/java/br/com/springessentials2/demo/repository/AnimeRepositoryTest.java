package br.com.springessentials2.demo.repository;

import br.com.springessentials2.demo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("Save update anime when successful")
    void save_UpdatedAnime_WhenSuccessful() {

        Anime animeToBeSaved = createdAnime();

        Anime save = animeRepository.save(animeToBeSaved);

        animeToBeSaved.setName("Ben 10");

        Anime update = animeRepository.save(animeToBeSaved);

        Assertions.assertThat(update).isNotNull();

        Assertions.assertThat(update.getId()).isNotNull();

        Assertions.assertThat(update.getName()).isEqualTo(save.getName());

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {

        Anime animeToBeSaved = createdAnime();

        Anime save = animeRepository.save(animeToBeSaved);

        animeRepository.delete(save);

        Optional<Anime> animeOptional = animeRepository.findById(animeToBeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name return list of anime when successful")
    void findByName_ReturnAnime_WhenSuccessful() {

        Anime animeToBeSaved = createdAnime();

        Anime save = animeRepository.save(animeToBeSaved);

        String name = animeToBeSaved.getName();

        List<Anime> byName = animeRepository.findByName(name);

        Assertions.assertThat(byName).isNotEmpty();

        Assertions.assertThat(byName).contains(animeToBeSaved);


    }

    @Test
    @DisplayName("Find by name returns empty list when anime is not found ")
    void findByName_ReturnsEmptyList_WhenAnimeISNotFound() {

        List<Anime> byName = animeRepository.findByName("name");

        Assertions.assertThat(byName).isEmpty();

    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty ")
    void save_ThrowsConstraintViolationException_WhenNameISEmpty() {
        Anime anime = new Anime();

        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime)).isInstanceOf(
                ConstraintViolationException.class
        );
        
    }

    private Anime createdAnime() {
        return Anime.builder().name("Bob esponja").build();
    }

}