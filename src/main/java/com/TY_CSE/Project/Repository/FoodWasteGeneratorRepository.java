package com.TY_CSE.Project.Repository;

import com.TY_CSE.Project.Model.FoodWasteGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodWasteGeneratorRepository extends JpaRepository<FoodWasteGenerator, Long> {
}
