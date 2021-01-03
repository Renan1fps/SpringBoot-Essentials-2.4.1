package br.com.springessentials2.demo.util;

import br.com.springessentials2.demo.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody animePostRequestBody() {
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnime().getName()).build();
    }

}
