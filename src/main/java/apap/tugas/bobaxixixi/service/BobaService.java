package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.BobaTeaModel;

import java.util.List;

public interface BobaService {

    void addBoba(BobaTeaModel boba);

    boolean updateBoba(BobaTeaModel boba);

    List<BobaTeaModel> getBobaList();

    BobaTeaModel getBobaByIdBoba(long idBoba);

    boolean deleteBoba(Long idBoba);
}
