package ru.iteco.core.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategory, Integer> {
    Collection<GameCategory> findAllByOrderByPriorityDesc();
}
