package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.ManagerModel;

import java.util.List;

public interface ManagerService {
    void addManager(ManagerModel manager);

    void updateManager(ManagerModel manager);

    List<ManagerModel> getManagerList();

    List<ManagerModel> getManagerFreeList();

    ManagerModel getManagerByIdManager(long idManager);

    void deleteManager(Long idManager);
}
