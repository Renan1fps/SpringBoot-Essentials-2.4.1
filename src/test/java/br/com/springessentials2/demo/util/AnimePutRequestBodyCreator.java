package br.com.springessentials2.demo.util;

import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody animePutRequestBody() {
        return AnimePutRequestBody.builder()
                .id(AnimeCreator.createUpdateAnime().getId())
                .name(AnimeCreator.createUpdateAnime().getName()).build();
    }

}
