package com.command;

import com.model.Auto;
import com.model.BusinessAuto;
import com.model.Invoice;
import com.model.Vehicle;
import com.repository.DBAutoRepository;
import com.repository.DBBusinessAutoRepository;
import com.repository.DBInvoiceRepository;
import com.repository.DBSportAutoRepository;
import com.service.AutoService;
import com.service.InvoiceService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceCommand implements Command{
    private static final InvoiceService INVOICE_SERVICE = InvoiceService.getInstance();
    @Override
    public void execute() {
        DBAutoRepository.getInstance().clear();
        DBBusinessAutoRepository.getInstance().clear();
        DBSportAutoRepository.getInstance().clear();
        DBInvoiceRepository.getInstance().clear();
        System.out.println("Create and save Invoice: ");
        INVOICE_SERVICE.createAndSaveRandomInvoice(3);
//        INVOICE_SERVICE.printAll(); -
//        System.out.println();
//        INVOICE_SERVICE.deleteInvoiceById("e8dfc931-2b03-4643-b5dd-327ba466fbcf");
//          System.out.println();
//          INVOICE_SERVICE.getInvoiceMorePrice(BigDecimal.valueOf(800)); -
        System.out.println("Update invoice: ");
        INVOICE_SERVICE.updateDateInvoice("0b11380d-e520-478f-b9af-01d422c14b93");
        System.out.println("Count invoices");
        INVOICE_SERVICE.printCountInvoiceInDB();
//        System.out.println();
        INVOICE_SERVICE.groupInvoiceByPrice();
        System.out.println("Invoice more 10000: ");
        INVOICE_SERVICE.printInvoiceMorePrice(BigDecimal.valueOf(10000));
    }
}
