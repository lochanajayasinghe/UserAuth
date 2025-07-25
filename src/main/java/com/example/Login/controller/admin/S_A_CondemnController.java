package com.example.Login.controller.admin;

import com.example.Login.model.Asset;
import com.example.Login.model.Condemn;
import com.example.Login.service.S_CondemnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/condemn")
public class S_A_CondemnController {
    @Autowired
    private S_CondemnService condemnService;

    @GetMapping("/assets")
    public List<Asset> getAllAssets() {
        return condemnService.getAllAssets();
    }

    @PostMapping("/condemn")
    public String condemnAsset(@RequestBody Condemn condemn) {
        return condemnService.condemnAsset(condemn);
    }
}
