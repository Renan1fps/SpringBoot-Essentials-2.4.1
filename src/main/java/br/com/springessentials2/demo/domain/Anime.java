package br.com.springessentials2.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data// Vai gerar gets and sets, equals hashcod, tostring;
@AllArgsConstructor// vai gerar construtores com todos atributos;
public class Anime {
    private Long id;

    //@JsonProperty("name") para caso do json nao reconhecer o atributo;
    private String name;


}
