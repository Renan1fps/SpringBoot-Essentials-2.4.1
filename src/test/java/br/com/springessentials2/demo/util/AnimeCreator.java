package br.com.springessentials2.demo.util;

import br.com.springessentials2.demo.domain.Anime;

public class AnimeCreator {


    public static Anime createAnime() {
        return Anime.builder()
                .name("Bob esponja")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .name("Bob esponja")
                .id(1L)
                .build();
    }

    public static Anime createUpdateAnime() {
        return Anime.builder()
                .name("Bob esponja 2")
                .id(1L)
                .build();
    }
}
