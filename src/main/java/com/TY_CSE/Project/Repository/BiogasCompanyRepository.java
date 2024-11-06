package com.TY_CSE.Project.Repository;

import com.TY_CSE.Project.Model.BiogasCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiogasCompanyRepository extends JpaRepository<BiogasCompany, Long>  {
}
