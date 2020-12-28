package br.com.springessentials2.demo.client;

import br.com.springessentials2.demo.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> forEntity = new RestTemplate().getForEntity("http://localhost:8080/animes/3", Anime.class);
        log.info(forEntity);

        Anime forObject = new RestTemplate().getForObject("http://localhost:8080/animes/3", Anime.class);
        log.info(forObject);
    }
}
