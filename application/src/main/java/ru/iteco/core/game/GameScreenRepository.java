package ru.iteco.core.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameScreenRepository extends JpaRepository<GameScreen, Integer> {
}
