package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsTextureRepository extends JpaRepository<CuratorsTextureEntity, String>,
    JpaSpecificationExecutor<CuratorsTextureEntity>, CustomRepository<CuratorsTextureEntity>, CustomTextureRepository {


}
