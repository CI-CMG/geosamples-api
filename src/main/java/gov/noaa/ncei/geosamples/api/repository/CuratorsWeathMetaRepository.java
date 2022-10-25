package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsWeathMetaRepository extends JpaRepository<CuratorsWeathMetaEntity, String>,
    JpaSpecificationExecutor<CuratorsWeathMetaEntity>, CustomRepository<CuratorsWeathMetaEntity> {


}
