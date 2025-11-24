# PROMPT: INC-012-DS - Integración con Sistemas de Versionado de Datos (DVC, Git LFS)

**Incidencia:** INC-012-DS  
**Prioridad:** 🟢 LOW  
**Artículo:** ISO 42001 8.2.2 (data governance)  
**Esfuerzo Estimado:** 5-7 días  
**Tipo:** Python - Microservicio

---

## CONTEXTO

El sistema no se integra con herramientas de versionado de datos (DVC, Git LFS) que muchos equipos usan para gestionar datasets.

**Ubicación Actual:**
- No hay integración con DVC (Data Version Control)
- No hay integración con Git LFS
- Solo soporta upload directo de archivos

---

## REQUISITOS

1. Conector DVC para importar datasets desde repositorios
2. Conector Git LFS para datasets versionados
3. Sincronización automática cuando se actualiza dataset en repositorio
4. Webhook para trigger de evaluación cuando dataset cambia

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Conector DVC

**Archivo:** `bias-detection-service/connectors/dvc_connector.py`

```python
"""
DVC (Data Version Control) Connector
Importa datasets desde repositorios DVC
"""
import subprocess
import os
import tempfile
import logging
from typing import Optional, Dict
from pathlib import Path

logger = logging.getLogger(__name__)


class DVCConnector:
    """
    Conector para importar datasets desde repositorios DVC
    """
    
    def __init__(self, dvc_repo_path: str, dvc_remote: Optional[str] = None):
        """
        Args:
            dvc_repo_path: Ruta al repositorio DVC (puede ser URL de Git)
            dvc_remote: Nombre del remote DVC (opcional)
        """
        self.dvc_repo_path = dvc_repo_path
        self.dvc_remote = dvc_remote
        self.work_dir = None
    
    def clone_and_pull_dataset(
        self,
        dataset_path: str,
        version: Optional[str] = None
    ) -> str:
        """
        Clona repositorio y descarga dataset específico
        
        Args:
            dataset_path: Ruta del dataset en DVC (ej: "data/train.csv.dvc")
            version: Versión/tag de Git (opcional)
            
        Returns:
            Ruta local al archivo del dataset descargado
        """
        try:
            # Crear directorio temporal
            self.work_dir = tempfile.mkdtemp()
            os.chdir(self.work_dir)
            
            # Clonar repositorio Git
            logger.info(f"Cloning repository: {self.dvc_repo_path}")
            subprocess.run(
                ["git", "clone", self.dvc_repo_path, "."],
                check=True,
                capture_output=True
            )
            
            # Checkout versión específica si se proporciona
            if version:
                logger.info(f"Checking out version: {version}")
                subprocess.run(
                    ["git", "checkout", version],
                    check=True,
                    capture_output=True
                )
            
            # Pull dataset desde DVC
            logger.info(f"Pulling dataset: {dataset_path}")
            subprocess.run(
                ["dvc", "pull", dataset_path],
                check=True,
                capture_output=True
            )
            
            # Obtener ruta real del archivo (DVC puede tener .dvc extension)
            if dataset_path.endswith(".dvc"):
                actual_path = dataset_path[:-4]  # Remover .dvc
            else:
                actual_path = dataset_path
            
            full_path = os.path.join(self.work_dir, actual_path)
            
            if not os.path.exists(full_path):
                raise FileNotFoundError(f"Dataset not found after DVC pull: {full_path}")
            
            logger.info(f"Dataset downloaded successfully: {full_path}")
            return full_path
            
        except subprocess.CalledProcessError as e:
            logger.error(f"DVC command failed: {e.stderr.decode()}")
            raise RuntimeError(f"Failed to pull dataset from DVC: {e.stderr.decode()}")
        except Exception as e:
            logger.error(f"Error in DVC connector: {str(e)}")
            raise
    
    def get_dataset_versions(self, dataset_path: str) -> list:
        """
        Obtiene lista de versiones disponibles del dataset
        
        Returns:
            Lista de tags/commits que modificaron el dataset
        """
        try:
            os.chdir(self.work_dir)
            
            # Obtener commits que modificaron el archivo .dvc
            dvc_file = dataset_path if dataset_path.endswith(".dvc") else dataset_path + ".dvc"
            
            result = subprocess.run(
                ["git", "log", "--oneline", "--", dvc_file],
                capture_output=True,
                text=True,
                check=True
            )
            
            versions = []
            for line in result.stdout.strip().split("\n"):
                if line:
                    commit_hash = line.split()[0]
                    versions.append(commit_hash)
            
            return versions
            
        except Exception as e:
            logger.error(f"Error getting dataset versions: {str(e)}")
            return []
    
    def get_dataset_metadata(self, dataset_path: str) -> Dict:
        """
        Obtiene metadata del dataset desde DVC
        
        Returns:
            Diccionario con metadata (hash, tamaño, etc.)
        """
        try:
            os.chdir(self.work_dir)
            
            dvc_file = dataset_path if dataset_path.endswith(".dvc") else dataset_path + ".dvc"
            
            # Leer archivo .dvc
            import yaml
            with open(dvc_file, 'r') as f:
                dvc_data = yaml.safe_load(f)
            
            metadata = {
                "dvc_hash": dvc_data.get("outs", [{}])[0].get("md5"),
                "size": dvc_data.get("outs", [{}])[0].get("size"),
                "path": dataset_path
            }
            
            return metadata
            
        except Exception as e:
            logger.error(f"Error getting dataset metadata: {str(e)}")
            return {}
    
    def cleanup(self):
        """Limpia directorio temporal"""
        if self.work_dir and os.path.exists(self.work_dir):
            import shutil
            shutil.rmtree(self.work_dir)
            logger.info(f"Cleaned up temporary directory: {self.work_dir}")
```

### 2. Crear Conector Git LFS

**Archivo:** `bias-detection-service/connectors/git_lfs_connector.py`

```python
"""
Git LFS Connector
Importa datasets desde repositorios Git LFS
"""
import subprocess
import os
import tempfile
import logging
from typing import Optional, Dict

logger = logging.getLogger(__name__)


class GitLFSConnector:
    """
    Conector para importar datasets desde repositorios Git LFS
    """
    
    def __init__(self, git_repo_url: str):
        self.git_repo_url = git_repo_url
        self.work_dir = None
    
    def clone_and_checkout_file(
        self,
        file_path: str,
        version: Optional[str] = None
    ) -> str:
        """
        Clona repositorio y descarga archivo específico desde Git LFS
        
        Args:
            file_path: Ruta del archivo en el repositorio
            version: Tag o commit (opcional)
            
        Returns:
            Ruta local al archivo descargado
        """
        try:
            # Crear directorio temporal
            self.work_dir = tempfile.mkdtemp()
            os.chdir(self.work_dir)
            
            # Clonar repositorio
            logger.info(f"Cloning repository: {self.git_repo_url}")
            subprocess.run(
                ["git", "clone", self.git_repo_url, "."],
                check=True,
                capture_output=True
            )
            
            # Checkout versión
            if version:
                subprocess.run(
                    ["git", "checkout", version],
                    check=True,
                    capture_output=True
                )
            
            # Pull archivo desde Git LFS
            logger.info(f"Pulling file from Git LFS: {file_path}")
            subprocess.run(
                ["git", "lfs", "pull", "--include", file_path],
                check=True,
                capture_output=True
            )
            
            full_path = os.path.join(self.work_dir, file_path)
            
            if not os.path.exists(full_path):
                raise FileNotFoundError(f"File not found after Git LFS pull: {full_path}")
            
            logger.info(f"File downloaded successfully: {full_path}")
            return full_path
            
        except subprocess.CalledProcessError as e:
            logger.error(f"Git LFS command failed: {e.stderr.decode()}")
            raise RuntimeError(f"Failed to pull file from Git LFS: {e.stderr.decode()}")
        except Exception as e:
            logger.error(f"Error in Git LFS connector: {str(e)}")
            raise
    
    def cleanup(self):
        """Limpia directorio temporal"""
        if self.work_dir and os.path.exists(self.work_dir):
            import shutil
            shutil.rmtree(self.work_dir)
```

### 3. Crear Endpoints para Integración

**Archivo:** `bias-detection-service/main.py`

```python
from connectors.dvc_connector import DVCConnector
from connectors.git_lfs_connector import GitLFSConnector

@app.post("/api/dataset-quality/import-from-dvc")
async def import_from_dvc(
    repo_url: str = Form(...),
    dataset_path: str = Form(...),
    version: Optional[str] = Form(None),
    dvc_remote: Optional[str] = Form(None)
):
    """
    Importa dataset desde repositorio DVC y lo evalúa
    """
    try:
        connector = DVCConnector(repo_url, dvc_remote)
        
        # Descargar dataset
        local_path = connector.clone_and_pull_dataset(dataset_path, version)
        
        # Obtener metadata
        metadata = connector.get_dataset_metadata(dataset_path)
        
        # Leer y evaluar
        df = pd.read_csv(local_path)
        result = data_quality_service.validate_dataset(df)
        
        # Añadir metadata de DVC
        result["source"] = "DVC"
        result["dvc_repo"] = repo_url
        result["dvc_path"] = dataset_path
        result["dvc_version"] = version
        result["dvc_metadata"] = metadata
        
        # Limpiar
        connector.cleanup()
        
        return result
        
    except Exception as e:
        logger.error(f"Error importing from DVC: {str(e)}")
        raise HTTPException(status_code=500, detail=f"DVC import failed: {str(e)}")

@app.post("/api/dataset-quality/import-from-git-lfs")
async def import_from_git_lfs(
    repo_url: str = Form(...),
    file_path: str = Form(...),
    version: Optional[str] = Form(None)
):
    """
    Importa dataset desde repositorio Git LFS y lo evalúa
    """
    try:
        connector = GitLFSConnector(repo_url)
        
        # Descargar archivo
        local_path = connector.clone_and_checkout_file(file_path, version)
        
        # Leer y evaluar
        df = pd.read_csv(local_path)
        result = data_quality_service.validate_dataset(df)
        
        # Añadir metadata
        result["source"] = "Git LFS"
        result["git_repo"] = repo_url
        result["git_path"] = file_path
        result["git_version"] = version
        
        # Limpiar
        connector.cleanup()
        
        return result
        
    except Exception as e:
        logger.error(f"Error importing from Git LFS: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Git LFS import failed: {str(e)}")
```

### 4. Webhook para Sincronización Automática

**Archivo:** `bias-detection-service/main.py`

```python
@app.post("/api/dataset-quality/webhook/dvc-updated")
async def webhook_dvc_updated(
    repo_url: str = Form(...),
    dataset_path: str = Form(...),
    commit_hash: str = Form(...)
):
    """
    Webhook llamado cuando dataset se actualiza en DVC
    Trigger automático de evaluación
    """
    try:
        # Importar y evaluar automáticamente
        result = await import_from_dvc(
            repo_url=repo_url,
            dataset_path=dataset_path,
            version=commit_hash
        )
        
        # Notificar resultado
        notification_service.notify_auto_evaluation_completed(result)
        
        return {"status": "success", "evaluation_id": result.get("evaluation_id")}
        
    except Exception as e:
        logger.error(f"Error in DVC webhook: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))
```

---

## DEPENDENCIAS

**Añadir a `requirements.txt`:**

```txt
dvc>=3.0.0
gitpython>=3.1.0
pyyaml>=6.0
```

---

## VALIDACIONES

1. ✅ DVC connector descarga datasets correctamente
2. ✅ Git LFS connector descarga archivos correctamente
3. ✅ Webhook trigger funciona
4. ✅ Metadata se almacena correctamente
5. ✅ Limpieza de archivos temporales funciona

---

## TESTING

```python
def test_dvc_connector():
    connector = DVCConnector("https://github.com/user/repo.git")
    path = connector.clone_and_pull_dataset("data/train.csv.dvc")
    assert os.path.exists(path)
    connector.cleanup()
```

---

## DOCUMENTACIÓN

Actualizar:
- `bias-detection-service/README.md` - Integración DVC/Git LFS
- Crear guía de configuración de webhooks

---

## CUMPLIMIENTO ISO 42001

**8.2.2:** Data governance
- ✅ Integración con sistemas de versionado de datos
- ✅ Sincronización automática
- ✅ Trazabilidad de versiones

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** MLOps Team

