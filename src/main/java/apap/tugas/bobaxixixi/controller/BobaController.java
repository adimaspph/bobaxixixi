package apap.tugas.bobaxixixi.controller;

import apap.tugas.bobaxixixi.model.*;
import apap.tugas.bobaxixixi.service.*;
import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.apache.catalina.Store;
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
public class BobaController {
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

    @GetMapping("/boba")
    public String viewAllBoba(Model model) {
        List<BobaTeaModel> listBoba = bobaService.getBobaList();
        model.addAttribute ( "listBoba", listBoba);
        return "boba-viewall";
    }

    @GetMapping("/boba/add")
    public String formAddBoba(Model model) {
        List<ToppingModel> listTopping = toppingService.getToppingList();
        BobaTeaModel boba = new BobaTeaModel();

        model.addAttribute ( "boba", boba);
        model.addAttribute ( "listTopping", listTopping);
        return "boba-form-add";
    }

    @PostMapping(value = ("/boba/add"))
    public String addBobaSave(
            @ModelAttribute BobaTeaModel boba,
            Model model
    ){
        bobaService.addBoba(boba);
        model.addAttribute( "message", boba.getName() + " successfully added!");
        return "boba-message";
    }

    @GetMapping("/boba/update/{idBoba}")
    public String formUpdatBoba(
            @PathVariable Long idBoba,
            Model model
    ){
        List<ToppingModel> listTopping = toppingService.getToppingList();
        BobaTeaModel boba = bobaService.getBobaByIdBoba(idBoba);

        model.addAttribute ( "boba", boba);
        model.addAttribute ( "listTopping", listTopping);
        return "boba-form-update";
    }

    @PostMapping(value = ("/boba/update"))
    public String updateBobaSave(
            @ModelAttribute BobaTeaModel boba,
            Model model
    ){
        boolean result = bobaService.updateBoba(boba);

        if (result) model.addAttribute( "message", boba.getName() + " successfully updated!");
        else model.addAttribute( "message", boba.getName() + " can't be updated because there still an open store that is selling it!");

        return "boba-message";
    }

    @GetMapping("/boba/{idBoba}/assign-store")
    public String assignStoreForm(
            @PathVariable Long idBoba,
            Model model
    ){
        List<StoreModel> listStore = storeService.getStoreList();

        BobaTeaModel boba = bobaService.getBobaByIdBoba(idBoba);
        BobaTeaModel bobaTemp = new BobaTeaModel();

        List<StoreBobaModel> temp = new ArrayList<>();
        for (int i = 0; i < listStore.size(); i++) {
            temp.add(new StoreBobaModel());
        }

        bobaTemp.setListStore(temp);

        model.addAttribute ( "boba", boba);
        model.addAttribute ( "listStore", listStore);
        model.addAttribute ( "bobaTemp", bobaTemp);
        return "boba-form-assign";
    }

    @PostMapping(value = ("/boba/{idBoba}/assign-store"))
    public String assignStoreSave(
            @PathVariable Long idBoba,
            @ModelAttribute BobaTeaModel bobaTemp,
            Model model
    ){
        BobaTeaModel boba = bobaService.getBobaByIdBoba(idBoba);

        if (boba.getListStore() != null) {
            for (StoreBobaModel storeBoba : boba.getListStore()) {
                System.out.println(storeBoba.getIdStoreBoba());
                storeBobaService.deleteStoreBoba(storeBoba);
            }
        }
        List<StoreModel> listStore = new ArrayList<>();
        for (StoreBobaModel storeBoba : bobaTemp.getListStore()) {
            if (storeBoba.getStore() != null) {
                System.out.println(storeBoba.getStore().getName());
                listStore.add(storeBoba.getStore());
                storeBoba.setBoba(boba);
                storeBobaService.addStoreBoba(storeBoba);
            }
        }

        model.addAttribute( "listStore", listStore);
        model.addAttribute ( "boba", boba);
        return "boba-assign";
    }

    @GetMapping("/search")
    public String searchBoba(
            @RequestParam(value = "bobaName", required = false, defaultValue = "") String namaBoba,
            @RequestParam(value = "topping", required = false, defaultValue = "") String namaTopping,
            Model model
    ){
        List<BobaTeaModel> listBoba = bobaService.getBobaList();
        List<BobaTeaModel> hasilCari = new ArrayList<>();
        List<StoreBobaModel> result = new ArrayList<>();

//        System.out.println("Boba " + namaBoba);
//        System.out.println("Topping " + namaTopping);
        if (namaBoba.equals("") || namaTopping.equals("")) {
            model.addAttribute( "result", result);
            model.addAttribute( "hasilCari", hasilCari);
            model.addAttribute( "bobaTemp", new BobaTeaModel());
            model.addAttribute( "listTopping", toppingService.getToppingList());
            model.addAttribute( "namaBoba", "bobaName");
            model.addAttribute( "namaTopping", "topping");
            model.addAttribute( "listBoba", listBoba);
            return "boba-search";
        }

        for (BobaTeaModel boba : listBoba) {
            if (boba.getName().contains(namaBoba)) {
                if (namaTopping.equalsIgnoreCase("Tidak ada topping")) {
                    if (boba.getTopping() == null) {
                        hasilCari.add(boba);
                    }
                } else {
                    if (boba.getTopping() != null) {
                        if (boba.getTopping().getName().contains(namaTopping)) {
                            hasilCari.add(boba);
                        }
                    }
                }
            }
        }

        System.out.println(hasilCari);
        for (BobaTeaModel boba : hasilCari) {
            System.out.println(boba.getListStore());
            for (StoreBobaModel storeBoba : boba.getListStore()) {
                if (storeService.stillOpen(storeBoba.getStore())) {
                    result.add(storeBoba);
                }
            }
        }
        System.out.println(result);

        model.addAttribute( "result", result);
        model.addAttribute( "hasilCari", hasilCari);
        model.addAttribute( "bobaTemp", new BobaTeaModel());
        model.addAttribute( "listTopping", toppingService.getToppingList());
        model.addAttribute( "namaBoba", "bobaName");
        model.addAttribute( "namaTopping", "topping");
        model.addAttribute( "listBoba", listBoba);
        return "boba-search";
    }

    @GetMapping("/searchKey")
    public String searchBonus(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            Model model
    ){
        List<BobaTeaModel> listBoba = bobaService.getBobaList();
        List<StoreModel> listStore = storeService.getStoreList();

        List<BobaTeaModel> hasilCariBoba = new ArrayList<>();
        List<StoreModel> hasilCariStore = new ArrayList<>();

//        System.out.println(listBoba);
//        System.out.println(listStore);
        // Cari Boba
        for (BobaTeaModel boba : listBoba) {
//            System.out.println(boba.getName());
            if (boba.getName().contains(keyword)) {
                hasilCariBoba.add(boba);
            }
        }

        // Cari Store
        for (StoreModel store : listStore) {
//            System.out.println(store.getName());
            if (store.getName().contains(keyword)) {
                hasilCariStore.add(store);
            }
        }

//        System.out.println(hasilCariBoba);
//        System.out.println(hasilCariStore);
        model.addAttribute( "hasilCariBoba", hasilCariBoba);
        model.addAttribute( "hasilCariStore", hasilCariStore);
        return "boba-keyword";
    }

}