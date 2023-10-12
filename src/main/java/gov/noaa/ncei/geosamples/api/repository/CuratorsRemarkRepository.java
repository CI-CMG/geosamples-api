package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsRemarkRepository extends JpaRepository<CuratorsRemarkEntity, Long>, JpaSpecificationExecutor<CuratorsRemarkEntity> {

  Optional<CuratorsRemarkEntity> findByRemark(String remark);

}
