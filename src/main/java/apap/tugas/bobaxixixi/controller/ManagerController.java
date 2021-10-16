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
public class ManagerController {
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

    @GetMapping("/filter")
    public String viewAllBoba(
            @RequestParam(value = "nameBoba", required = false, defaultValue = "") String namaBoba,
            Model model
    ) {
        List<BobaTeaModel> listBoba = bobaService.getBobaList();
        List<ManagerModel> filterResult = new ArrayList<>();
//        List<StoreBobaModel> filterResult = new ArrayList<>();

        if (namaBoba.equals("")) {
            model.addAttribute( "filterResult", filterResult);
            model.addAttribute( "bobaTemp", new BobaTeaModel());
            model.addAttribute( "nameBoba", "nameBoba");
            model.addAttribute( "listBoba", listBoba);
            return "manager-filter";
        }

        for (BobaTeaModel boba : listBoba) {
            if (boba.getName().contains(namaBoba)) {
                for (StoreBobaModel storeBoba : boba.getListStore()) {
                    filterResult.add(storeBoba.getStore().getManager());
                }
            }
        }

        model.addAttribute( "filterResult", filterResult);
        model.addAttribute( "bobaTemp", new BobaTeaModel());
        model.addAttribute( "nameBoba", "nameBoba");
        model.addAttribute( "listBoba", listBoba);
        return "manager-filter";
    }
}