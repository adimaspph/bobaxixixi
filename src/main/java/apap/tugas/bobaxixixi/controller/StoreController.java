package apap.tugas.bobaxixixi.controller;

import apap.tugas.bobaxixixi.model.*;
import apap.tugas.bobaxixixi.service.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StoreController {
    @Qualifier("bobaServiceImpl")
    @Autowired
    private BobaService bobaService;

    @Qualifier("storeServiceImpl")
    @Autowired
    private StoreService storeService;

    @Qualifier("managerServiceImpl")
    @Autowired
    private ManagerService managerService;

    @Qualifier("toppingServiceImpl")
    @Autowired
    private ToppingService toppingService;

    @Qualifier("storeBobaServiceImpl")
    @Autowired
    private StoreBobaService storeBobaService;

    @GetMapping("/store")
    public String viewAllStore(Model model) {
        List<StoreModel> listStore = storeService.getStoreList();
        model.addAttribute ( "listStore", listStore);
        return "store-viewall";
    }

    @PostMapping(value = ("/store/add"), params = ("form"))
    public String formAddStore(Model model) {
        List<ManagerModel> listManager = managerService.getManagerFreeList();
        StoreModel store = new StoreModel();

        model.addAttribute ( "store", store);
        model.addAttribute ( "listManager", listManager);
        return "store-form-add";
    }

    @PostMapping(value = ("/store/add"), params = ("save"))
    public String addStoreSave(
            @ModelAttribute StoreModel store,
            Model model
    ){
        store = storeService.generateStoreCode(store);
        storeService.addStore(store);
        model.addAttribute( "store", store);
        return "store-add-success";
    }

    @GetMapping("/store/{idStore}")
    public String viewStore(
            @PathVariable Long idStore,
            Model model
    ){
        StoreModel store = storeService.getStoreByIdStore(idStore);

        List<BobaTeaModel> listBoba = storeService.getBobaStoreList(store);
        model.addAttribute( "listBoba", listBoba);
        model.addAttribute( "store", store);
        return"store-view" ;
    }

    @GetMapping("/store/update/{idStore}")
    public String formUpdateStore(
            @PathVariable Long idStore,
            Model model
    ){
        LocalTime now = LocalTime.now();
        List<ManagerModel> listManager = managerService.getManagerFreeList();
        StoreModel store = storeService.getStoreByIdStore(idStore);

//        if (now.isBefore(store.getOpenHour()) || now.isAfter(store.getCloseHour())) {
//
//        }
        if (now.isAfter(store.getOpenHour()) && now.isBefore((store.getCloseHour()))){
            model.addAttribute( "store", store);
            return "store-update-fail";
        }
//        System.out.println("masuk update");
        model.addAttribute ( "store", store);
        model.addAttribute ( "oldName", store.getName());
        model.addAttribute ( "oldOpenHour", store.getOpenHour().toString());
        model.addAttribute ( "oldCloseHour", store.getCloseHour().toString());
        model.addAttribute ( "listManager", listManager);
        return "store-form-update";
    }

    @PostMapping(value = ("/store/update"))
    public String updateStoreSave(
            @ModelAttribute StoreModel store,
            @ModelAttribute String oldName,
            @ModelAttribute String oldOpenHour,
            @ModelAttribute String oldCloseHour,
            Model model
    ){
//        System.out.println(storeOld);
        storeService.updateStore(store, oldName, oldOpenHour, oldCloseHour);
        model.addAttribute( "store", store);
        return "store-update-success";
    }

    @PostMapping(value = ("/store/delete/{idStore}"))
    public String updateStoreSave(
            @PathVariable Long idStore,
            Model model
    ){
        StoreModel store = storeService.getStoreByIdStore(idStore);
        int result = storeService.deleteStore(idStore);
        if (result == 1) {
            model.addAttribute( "message", store.getName() + " Store is successfully Deleted!");
            return "store-message";
        } else if (result == 0) {
            model.addAttribute( "message", store.getName() + " Store is currently open and can't be deleted!");
            return "store-message";
        }
        model.addAttribute( "message",  store.getName() + " Store still has Boba Tea and Can't be deleted!");
        return "store-message";
    }

    @GetMapping("/store/{idStore}/assign-boba")
    public String assignBobaForm(
            @PathVariable Long idStore,
            Model model
    ){
        List<BobaTeaModel> listBoba = bobaService.getBobaList();
//        List<Long> listBobaTemp = new ArrayList<>(listBoba.size());
        StoreModel store = storeService.getStoreByIdStore(idStore);
        StoreModel storeTemp = new StoreModel();

        List<StoreBobaModel> temp = new ArrayList<>();
        for (int i = 0; i < listBoba.size(); i++) {
            temp.add(new StoreBobaModel());
        }

        storeTemp.setListBoba(temp);

        model.addAttribute ( "store", store);
        model.addAttribute ( "listBoba", listBoba);
        model.addAttribute ( "storeTemp", storeTemp);
        return "store-form-assign";
    }

    @PostMapping(value = ("/store/{idStore}/assign-boba"))
    public String assignBobaSave(
            @PathVariable Long idStore,
//            @ModelAttribute StoreModel store,
            @ModelAttribute StoreModel storeTemp,
            Model model
    ){
        StoreModel store = storeService.getStoreByIdStore(idStore);
        System.out.println(store.getListBoba());
        // Remove all store-boba relation
        if (store.getListBoba() != null) {
            for (StoreBobaModel storeBoba : store.getListBoba()) {
                System.out.println(storeBoba.getIdStoreBoba());
                storeBobaService.deleteStoreBoba(storeBoba);
            }
        }
        List<BobaTeaModel> listBoba = new ArrayList<>();
        // Assign checked store-boba relation
        for (StoreBobaModel storeBoba : storeTemp.getListBoba()) {
            if (storeBoba.getBoba() != null) {
                System.out.println(storeBoba.getBoba().getName());
                listBoba.add(storeBoba.getBoba());
                storeBoba.setStore(store);
                storeBobaService.addStoreBoba(storeBoba);
            }
        }

        model.addAttribute( "listBoba", listBoba);
        model.addAttribute ( "store", store);
        return "store-assign";
    }
}
