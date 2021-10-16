package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.BobaTeaModel;
import apap.tugas.bobaxixixi.model.StoreModel;

import java.time.LocalTime;
import java.util.List;

public interface StoreService {
    void addStore(StoreModel store);

    boolean updateStore(StoreModel store, String oldName, String oldOpenHour, String oldCloseHour);

    List<StoreModel> getStoreList();

    StoreModel getStoreByIdStore(long idStore);

    int deleteStore(Long idStore);

    StoreModel generateStoreCode(StoreModel store);

    boolean stillOpen(StoreModel store);

    List<BobaTeaModel> getBobaStoreList(StoreModel store);
}
