package br.com.springessentials2.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data// Vai gerar gets and sets, equals hashcod, tostring;
@AllArgsConstructor// vai gerar construtores com todos atributos;
@NoArgsConstructor
@Entity
@Builder
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@JsonProperty("name") para caso do json nao reconhecer o atributo;
    private String name;


}
