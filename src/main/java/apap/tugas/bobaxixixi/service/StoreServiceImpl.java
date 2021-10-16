package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.BobaTeaModel;
import apap.tugas.bobaxixixi.model.StoreBobaModel;
import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.repository.StoreDB;
import org.apache.catalina.Store;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class StoreServiceImpl implements StoreService{
    @Autowired
    StoreDB storeDB;

    @Override
    public void addStore(StoreModel store) {
        storeDB.save(store);
    }

    @Override
    public boolean updateStore(StoreModel store, String oldName, String oldOpenHour, String oldCloseHour) {
        LocalTime now = LocalTime.now();
        boolean adaUbah = false;
//        System.out.println(store.getName());
//        System.out.println(storeOld.getName());
        if (!oldName.equals(store.getName())) adaUbah = true;
        if (!oldOpenHour.equals(store.getOpenHour().toString())) adaUbah = true;
        if (!oldCloseHour.equals(store.getCloseHour().toString())) adaUbah = true;

        if (adaUbah) store = generateStoreCode(store);
//        System.out.println(adaUbah);
        storeDB.save(store);
        return true;
    }

    @Override
    public List<StoreModel> getStoreList() {
        return storeDB.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public StoreModel getStoreByIdStore(long idStore) {
        Optional<StoreModel> store = storeDB.findByIdStore(idStore);
        if (store.isPresent()) {
            return store.get();
        }
        return null;
    }

    @Override
    public int deleteStore(Long idStore) {
        LocalTime now = LocalTime.now();
        StoreModel store = storeDB.findByIdStore(idStore).get();

        if (store.getListBoba().size() != 0 ) {
            return -1;
        }
        if (now.isBefore(store.getOpenHour()) || now.isAfter(store.getCloseHour())) {
            storeDB.deleteById(idStore);
            return 1;
        }


        return 0;
    }

    @Override
    public boolean stillOpen(StoreModel store) {
        LocalTime now = LocalTime.now();
        if (now.isBefore(store.getCloseHour()) && now.isAfter(store.getOpenHour())) {
            return true;
        }
        return false;
    }

    @Override
    public List<BobaTeaModel> getBobaStoreList(StoreModel store) {
        List<StoreBobaModel> temp = store.getListBoba();
        List<BobaTeaModel> result = new ArrayList<>();
        for (StoreBobaModel storeBoba : temp) {
            result.add(storeBoba.getBoba());
        }
        return result;
    }

    @Override
    public StoreModel generateStoreCode(StoreModel store) {
        String storeCode = "SC";
        storeCode += new StringBuilder(store.getName().substring(0,3)).reverse().toString().toUpperCase();
        storeCode += String.format("%02d", store.getOpenHour().getHour());
        storeCode += store.getCloseHour().getHour()/10;

        List<String> allStoreCode = new ArrayList<>();
        for (StoreModel storeModel : getStoreList()) {
            allStoreCode.add(storeModel.getStoreCode());
        }
        String completeStoreCode = storeCode + randomString();
//        System.out.println(completeStoreCode);
        while (allStoreCode.contains(completeStoreCode)){
            completeStoreCode = storeCode + randomString();
            System.out.println("duplicate");
        }
        store.setStoreCode(completeStoreCode);
        return store;
    }



    // Adaptasi code dari https://www.baeldung.com/java-random-string
    private String randomString(){
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 2;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
