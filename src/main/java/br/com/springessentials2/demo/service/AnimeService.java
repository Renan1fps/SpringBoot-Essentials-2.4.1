package br.com.springessentials2.demo.service;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.exception.BadRequestException;
import br.com.springessentials2.demo.mapper.AnimeMapper;
import br.com.springessentials2.demo.repository.AnimeRepository;
import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Id not found"));
    }

    @Transactional // anotaçao para transações (roll back);
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));

    }

    public void delete(long id) {
        animeRepository.delete(findByIOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime animeSaved = findByIOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(animeSaved.getId());
        animeRepository.save(anime);

    }
}

