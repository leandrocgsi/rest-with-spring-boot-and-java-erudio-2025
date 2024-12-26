package br.com.erudio.repository;

import br.com.erudio.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long>{

    // clearAutomatically = true: Isso limpa o contexto de
    // persistência automaticamente após a execução da query, garantindo
    // que o próximo findById não use dados em cache.
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);

}
