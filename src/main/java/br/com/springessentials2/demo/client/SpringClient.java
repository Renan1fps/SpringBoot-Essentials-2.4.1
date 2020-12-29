package br.com.springessentials2.demo.client;

import br.com.springessentials2.demo.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> forEntity = new RestTemplate().getForEntity("http://localhost:8080/animes/3", Anime.class);
        log.info(forEntity);

        Anime forObject = new RestTemplate().getForObject("http://localhost:8080/animes/3", Anime.class);
        log.info(forObject);
        //@formatter:off
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        //@formatter:on
        log.info(exchange.getBody());

        Anime kingdom= Anime.builder().name("kingdom").build();
        Anime animePost = new RestTemplate().postForObject("http://localhost:8080/animes/", kingdom, Anime.class);
        log.info("Anime saved {}",animePost );

        //@formatter:off
        Anime cavaleiros= Anime.builder().name("Cavaleiros").build();
        new RestTemplate().exchange("http://localhost:8080/animes/",HttpMethod.POST,
                                         new HttpEntity<>(cavaleiros),Anime.class);
        //@formatter:on
        log.info("saved anime{}",cavaleiros);
    }
}
