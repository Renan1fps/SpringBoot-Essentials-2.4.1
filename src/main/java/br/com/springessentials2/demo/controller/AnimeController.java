package br.com.springessentials2.demo.controller;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "animes")
//@Log4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    public List<Anime> list() {
        return animeService.listAll();
    }
}
