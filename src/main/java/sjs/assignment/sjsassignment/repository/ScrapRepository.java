package sjs.assignment.sjsassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sjs.assignment.sjsassignment.model.ScrapEntity;

@Repository
public interface ScrapRepository extends JpaRepository<ScrapEntity,Long> {
    @Query(value = "SELECT a FROM ScrapEntity a WHERE a.id = ?1")
    ScrapEntity findByScarpData(Long idx);
}
