package tr.gov.gib.fpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.gov.gib.fpos.entity.FizikselPos;

@Repository
public interface FPosRepository extends JpaRepository<FizikselPos,String> {
}
