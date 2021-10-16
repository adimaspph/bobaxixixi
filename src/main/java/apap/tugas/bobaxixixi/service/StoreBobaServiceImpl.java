package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.ManagerModel;
import apap.tugas.bobaxixixi.model.StoreBobaModel;
import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.repository.BobaDB;
import apap.tugas.bobaxixixi.repository.StoreBobaDB;
import apap.tugas.bobaxixixi.repository.StoreDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StoreBobaServiceImpl implements StoreBobaService{
    @Autowired
    StoreDB storeDB;

    @Autowired
    StoreBobaDB storeBobaDB;

    @Autowired
    BobaDB bobaDB;

    @Override
    public void addStoreBoba(StoreBobaModel storeBoba) {
        storeBobaDB.save(generateProductionCode(storeBoba));
    }

    @Override
    public void updateStoreBoba(StoreBobaModel storeBoba) {
        storeBobaDB.save(storeBoba);
    }

    @Override
    public List<StoreBobaModel> getStoreList() {
        return storeBobaDB.findAll();
    }

    @Override
    public StoreBobaModel getStoreBobaByIdStoreBoba(long idStoreBoba) {
        Optional<StoreBobaModel> storeBoba = storeBobaDB.findByIdStoreBoba(idStoreBoba);
        if (storeBoba.isPresent()) {
            return storeBoba.get();
        }
        return null;
    }

    @Override
    public void deleteStoreBoba(StoreBobaModel storeBoba) {
        storeBobaDB.delete(storeBoba);
    }

    @Override
    public void deleteStoreBoba(Long idStoreBoba) {
        storeBobaDB.deleteById(idStoreBoba);
    }

    private StoreBobaModel generateProductionCode(StoreBobaModel storeBoba) {
        String productionCode = "PC";
        productionCode += String.format("%03d", storeBoba.getStore().getIdStore());

        if (storeBoba.getBoba().getTopping() == null) {
            productionCode += 0;
        } else {
            productionCode += 1;
        }

        productionCode += String.format("%03d", storeBoba.getBoba().getIdBoba());

        storeBoba.setProductionCode(productionCode);
        return storeBoba;
    }
}
