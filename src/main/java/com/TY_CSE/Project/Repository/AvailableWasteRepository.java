package com.TY_CSE.Project.Repository;

import com.TY_CSE.Project.Model.AvailableWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableWasteRepository extends JpaRepository<AvailableWaste, Long>{
}
