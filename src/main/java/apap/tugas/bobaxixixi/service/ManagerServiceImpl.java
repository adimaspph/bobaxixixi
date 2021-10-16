package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.ManagerModel;
import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.repository.ManagerDB;
import apap.tugas.bobaxixixi.repository.StoreDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.*;
import java.util.List;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService{

    @Autowired
    ManagerDB managerDB;

    @Autowired
    StoreDB storeDB;

    @Override
    public void addManager(ManagerModel manager) {
        managerDB.save(manager);
    }

    @Override
    public void updateManager(ManagerModel manager) {
        managerDB.save(manager);
    }

    @Override
    public List<ManagerModel> getManagerList() {
        return managerDB.findAll();
    }

    @Override
    public List<ManagerModel> getManagerFreeList() {
        List<ManagerModel> listManager = managerDB.findAll();
        List<StoreModel> listStore = storeDB.findAll();

        for (StoreModel store : listStore) {
            listManager.remove(store.getManager());
        }

        return listManager;
    }

    @Override
    public ManagerModel getManagerByIdManager(long idManager) {
        Optional<ManagerModel> manager = managerDB.findByIdManager(idManager);
        if (manager.isPresent()) {
            return manager.get();
        }
        return null;
    }

    @Override
    public void deleteManager(Long idManager) {
        managerDB.deleteById(idManager);
    }
}
