package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author Łukasz Dumiszewski
 */

public interface CommonCourtRepository extends JpaRepository<CommonCourt, Integer> {

    
    public CommonCourt findOneByCode(String code);
    
}
