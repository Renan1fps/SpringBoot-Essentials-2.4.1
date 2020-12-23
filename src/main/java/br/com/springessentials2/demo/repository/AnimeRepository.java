package br.com.springessentials2.demo.repository;

import br.com.springessentials2.demo.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

}

