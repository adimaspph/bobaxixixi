package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.StoreBobaModel;

import java.util.List;

public interface StoreBobaService {
    void addStoreBoba(StoreBobaModel storeBoba);

    void updateStoreBoba(StoreBobaModel storeBoba);

    List<StoreBobaModel> getStoreList();

    StoreBobaModel getStoreBobaByIdStoreBoba(long idStoreBoba);

    void deleteStoreBoba(StoreBobaModel storeBoba);

    void deleteStoreBoba(Long idStoreBoba);
}
