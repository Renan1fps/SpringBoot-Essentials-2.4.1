package br.com.springessentials2.demo.repository;

import br.com.springessentials2.demo.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}

