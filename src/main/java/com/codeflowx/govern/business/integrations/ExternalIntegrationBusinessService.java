package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.enartframework.nocode.datamodel.dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de acceso a datos para plataformas, modelos y datasets externos.
 */
@Slf4j
@Service
public class ExternalIntegrationBusinessService {

    @Autowired
    private DAO dao;

    @PostConstruct
    void logCounters() {
        try {
            Long platforms = dao.findBySQL(Long.class, "SELECT COUNT(1) FROM EPLEXTERNALPLATFORMS");
            Long models = dao.findBySQL(Long.class, "SELECT COUNT(1) FROM EXMEXTERNALMODELS");
            Long datasets = dao.findBySQL(Long.class, "SELECT COUNT(1) FROM EXDEXTERNALDATASETS");
            log.info("Integraciones externas → platforms={}, models={}, datasets={}", platforms, models, datasets);
        } catch (Exception ex) {
            log.warn("No fue posible contar integraciones externas: {}", ex.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Platforms
    // ---------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ExternalPlatformIntegration> findAllPlatforms() {
        try {
            return Optional.ofNullable(dao.findListBySQL(ExternalPlatformIntegration.class,
                "SELECT * FROM EPLEXTERNALPLATFORMS ORDER BY EPLPLATFORMNAME"))
                .orElse(Collections.emptyList());
        } catch (Exception ex) {
            log.error("Error listando plataformas externas", ex);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<ExternalPlatformIntegration> findPlatformsByType(String type) {
        try {
            return Optional.ofNullable(dao.findListBySQL(ExternalPlatformIntegration.class,
                "SELECT * FROM EPLEXTERNALPLATFORMS WHERE EPLPLATFORMTYPE = ? ORDER BY EPLPLATFORMNAME", type))
                .orElse(Collections.emptyList());
        } catch (Exception ex) {
            log.error("Error listando plataformas externas por tipo {}", type, ex);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public ExternalPlatformIntegration findPlatformById(Long platformId) {
        if (platformId == null) {
            return null;
        }
        try {
            return dao.findById(ExternalPlatformIntegration.class, platformId);
        } catch (Exception ex) {
            log.error("Error recuperando plataforma externa {}", platformId, ex);
            return null;
        }
    }

    @Transactional
    public ExternalPlatformIntegration savePlatform(ExternalPlatformIntegration platform) {
        try {
            if (platform.getIdxexternalplatform() == null) {
                dao.insert(platform);
            } else {
                platform.setEplupdatedat(new Timestamp(System.currentTimeMillis()));
                dao.update(platform);
            }
            return platform;
        } catch (Exception ex) {
            log.error("Error guardando plataforma externa", ex);
            throw new IllegalStateException("No fue posible guardar la plataforma externa", ex);
        }
    }

    @Transactional
    public void updateSyncStatus(Long platformId, String status, String errorMessage) {
        ExternalPlatformIntegration platform = findPlatformById(platformId);
        if (platform == null) {
            return;
        }
        platform.setEplsyncstatus(status);
        platform.setEpllasterror(errorMessage);
        platform.setEpllastsyncat(new Timestamp(System.currentTimeMillis()));
        savePlatform(platform);
    }

    // ---------------------------------------------------------------------
    // External Models
    // ---------------------------------------------------------------------

    @Transactional(readOnly = true)
    public ExternalModel findExternalModel(Long platformId, String externalId) {
        if (platformId == null || externalId == null) {
            return null;
        }
        try {
            List<ExternalModel> models = dao.findListBySQL(ExternalModel.class,
                "SELECT * FROM EXMEXTERNALMODELS WHERE IDXEXTERNALPLATFORM = ? AND EXMEXTERNALID = ?",
                platformId, externalId);
            return models.isEmpty() ? null : models.get(0);
        } catch (Exception ex) {
            log.error("Error localizando modelo externo {}", externalId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public ExternalModel findExternalModelById(Long externalModelId) {
        if (externalModelId == null) {
            return null;
        }
        try {
            ExternalModel model = dao.findById(ExternalModel.class, externalModelId);
            if (model == null) {
                return null;
            }
            if (model.getPlatform() == null) {
                Long platformId = dao.findBySQL(Long.class,
                    "SELECT IDXEXTERNALPLATFORM FROM EXMEXTERNALMODELS WHERE IDXEXTERNALMODEL = ?",
                    externalModelId);
                if (platformId != null) {
                    model.setPlatform(findPlatformById(platformId));
                }
            }
            return model;
        } catch (Exception ex) {
            log.error("Error recuperando modelo externo {}", externalModelId, ex);
            return null;
        }
    }

    @Transactional
    public ExternalModel saveExternalModel(ExternalModel model) {
        try {
            if (model.getIdxexternalmodel() == null) {
                dao.insert(model);
            } else {
                model.setExmupdatedat(new Timestamp(System.currentTimeMillis()));
                dao.update(model);
            }
            return model;
        } catch (Exception ex) {
            log.error("Error guardando modelo externo", ex);
            throw new IllegalStateException("No fue posible guardar el modelo externo", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<ExternalModel> findModelsByPlatform(Long platformId) {
        if (platformId == null) {
            return Collections.emptyList();
        }
        try {
            return Optional.ofNullable(dao.findListBySQL(ExternalModel.class,
                "SELECT * FROM EXMEXTERNALMODELS WHERE IDXEXTERNALPLATFORM = ? ORDER BY EXMEXTERNALNAME",
                platformId)).orElse(Collections.emptyList());
        } catch (Exception ex) {
            log.error("Error listando modelos externos para plataforma {}", platformId, ex);
            return Collections.emptyList();
        }
    }

    // ---------------------------------------------------------------------
    // External Datasets
    // ---------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ExternalDataset> findDatasetsByPlatform(Long platformId) {
        if (platformId == null) {
            return Collections.emptyList();
        }
        try {
            return Optional.ofNullable(dao.findListBySQL(ExternalDataset.class,
                "SELECT * FROM EXDEXTERNALDATASETS WHERE IDXEXTERNALPLATFORM = ? ORDER BY EXDNAME",
                platformId)).orElse(Collections.emptyList());
        } catch (Exception ex) {
            log.error("Error listando datasets externos para plataforma {}", platformId, ex);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public ExternalDataset findDatasetByExternalId(Long platformId, String externalId) {
        if (platformId == null || externalId == null) {
            return null;
        }
        try {
            List<ExternalDataset> datasets = dao.findListBySQL(ExternalDataset.class,
                "SELECT * FROM EXDEXTERNALDATASETS WHERE IDXEXTERNALPLATFORM = ? AND EXDEXTERNALID = ?",
                platformId, externalId);
            return datasets.isEmpty() ? null : datasets.get(0);
        } catch (Exception ex) {
            log.error("Error recuperando dataset externo {}", externalId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public ExternalDataset findDatasetById(Long datasetId) {
        if (datasetId == null) {
            return null;
        }
        try {
            ExternalDataset dataset = dao.findById(ExternalDataset.class, datasetId);
            if (dataset == null) {
                return null;
            }
            if (dataset.getPlatform() == null) {
                Long platformId = dao.findBySQL(Long.class,
                    "SELECT IDXEXTERNALPLATFORM FROM EXDEXTERNALDATASETS WHERE IDXEXTERNALDATASET = ?",
                    datasetId);
                if (platformId != null) {
                    dataset.setPlatform(findPlatformById(platformId));
                }
            }
            return dataset;
        } catch (Exception ex) {
            log.error("Error recuperando dataset externo {}", datasetId, ex);
            return null;
        }
    }

    @Transactional
    public ExternalDataset saveExternalDataset(ExternalDataset dataset) {
        try {
            if (dataset.getIdxexternaldataset() == null) {
                dao.insert(dataset);
            } else {
                dataset.setExdupdatedat(new Timestamp(System.currentTimeMillis()));
                dao.update(dataset);
            }
            return dataset;
        } catch (Exception ex) {
            log.error("Error guardando dataset externo", ex);
            throw new IllegalStateException("No fue posible guardar el dataset externo", ex);
        }
    }
}
