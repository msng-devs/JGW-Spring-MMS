package com.jaramgroupware.mms.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findRoleById (Integer id);
    Optional<List<Role>> findAllBy();
}
