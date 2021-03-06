package com.theironyard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by macbookair on 11/11/15.
 */
@Controller
public class PurchasesController {
    @Autowired
    PurchaseRepository purchases;
    @Autowired
    CustomerRepository customers;

    @PostConstruct
    public void init () throws IOException {

        if (customers.count()==0) {
            String fileContent = readFile("customers.csv");

            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line == lines[0]) {
                    continue;
                }
                String[] columns = line.split(",");
                Customer customer = new Customer();
                customer.name = columns[0];
                customer.email = columns[1];
                customers.save(customer);
            }
        }
        if (purchases.count() == 0) {
            String fileContent = readFile("purchases.csv");

            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line == lines[0]) {
                    continue;
                }
                String[] columns = line.split(",");
                Purchase purchase = new Purchase();
                purchase.date = columns[1];
                purchase.credit_card = columns[2];
                purchase.cvv = (Integer.valueOf(columns[3]));
                purchase.category = columns[4];

                Customer customer = customers.findOne(Integer.valueOf(columns[0]));
                purchase.customer = customer;

                purchases.save(purchase);
            }
        }
    }

    @RequestMapping("/")
    public String home (Model model, String category) {

        if (category != null){
            model.addAttribute("purchases", purchases.findByCategory(category));
        }
        else {
            model.addAttribute("purchases", purchases.findAll());
            model.addAttribute("customers", customers.findAll());
        }
        return "home";
    }

    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }
}

