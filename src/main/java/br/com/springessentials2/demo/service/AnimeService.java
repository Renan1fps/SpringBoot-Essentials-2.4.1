package br.com.springessentials2.demo.service;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.repository.AnimeRepository;
import br.com.springessentials2.demo.requests.AnimePostRequestBody;
import br.com.springessentials2.demo.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public Anime findByIOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(Anime.builder().name(animePostRequestBody.getName()).build());
    }

    public void delete(long id) {
        animeRepository.delete(findByIOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime animeSaved = findByIOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = Anime.builder()
                .name(animePutRequestBody.getName())
                .id(animeSaved.getId()).build();
        animeRepository.save(anime);

    }
}
