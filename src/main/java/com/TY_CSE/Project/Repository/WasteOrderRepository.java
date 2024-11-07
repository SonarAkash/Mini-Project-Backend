package com.TY_CSE.Project.Repository;

import com.TY_CSE.Project.Model.WasteOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WasteOrderRepository extends JpaRepository<WasteOrder, Long> {

    List<WasteOrder> findByAvailableWasteGeneratorId(Long generatorId);

    List<WasteOrder> findByBiogasCompanyId(Long biogasCompanyId);
}
