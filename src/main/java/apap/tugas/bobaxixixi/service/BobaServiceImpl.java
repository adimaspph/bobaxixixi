package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.BobaTeaModel;
import apap.tugas.bobaxixixi.model.StoreBobaModel;
import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.service.StoreService;
import apap.tugas.bobaxixixi.repository.BobaDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BobaServiceImpl implements BobaService{
    @Autowired
    BobaDB bobaDB;

    @Qualifier("storeServiceImpl")
    @Autowired
    private StoreService storeService;

    @Override
    public void addBoba(BobaTeaModel boba) {
        bobaDB.save(boba);
    }

    @Override
    public boolean updateBoba(BobaTeaModel boba) {
        List<StoreBobaModel> listStoreBoba = boba.getListStore();

        if (listStoreBoba != null) {
            for (StoreBobaModel storeboba : listStoreBoba) {
                if (storeService.stillOpen(storeboba.getStore())) {
                    return false;
                }
            }
        }

        bobaDB.save(boba);
        return true;
    }

    @Override
    public List<BobaTeaModel> getBobaList() {
        return bobaDB.findAll();
    }

    @Override
    public BobaTeaModel getBobaByIdBoba(long idBoba) {
        Optional<BobaTeaModel> boba = bobaDB.findByIdBoba(idBoba);
        if (boba.isPresent()) {
            return boba.get();
        }
        return null;
    }

    @Override
    public boolean deleteBoba(Long idBoba) {
        BobaTeaModel boba = getBobaByIdBoba(idBoba);
        List<StoreBobaModel> listStoreBoba = boba.getListStore();

        if (listStoreBoba != null) {
            if (listStoreBoba.size() > 0) {
                return false;
            }
        }

        bobaDB.deleteById(idBoba);
        return true;
    }
}
