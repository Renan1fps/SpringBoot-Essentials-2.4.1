package br.com.springessentials2.demo.mapper;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {

    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);

}
