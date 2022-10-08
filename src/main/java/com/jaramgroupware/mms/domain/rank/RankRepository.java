package com.jaramgroupware.mms.domain.rank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank,Integer> {
    Optional<Rank> findRankById (Integer id);
    Optional<List<Rank>> findAllBy();
}
