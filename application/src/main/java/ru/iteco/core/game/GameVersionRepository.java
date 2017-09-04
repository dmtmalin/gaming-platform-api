package ru.iteco.core.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameVersionRepository extends JpaRepository<GameVersion, Integer> {
    GameVersion findFirstByGameFkOrderByBuildDesc(Integer gameId);
}
