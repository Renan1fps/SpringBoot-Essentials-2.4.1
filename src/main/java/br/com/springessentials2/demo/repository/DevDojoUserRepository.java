package br.com.springessentials2.demo.repository;

import br.com.springessentials2.demo.domain.Anime;
import br.com.springessentials2.demo.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {

    DevDojoUser findByUserName(String name);


}

