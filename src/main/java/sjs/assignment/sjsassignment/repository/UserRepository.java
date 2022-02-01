package sjs.assignment.sjsassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sjs.assignment.sjsassignment.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUserId(String id);

}
