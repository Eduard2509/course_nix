package com.command;

import com.model.Auto;
import com.model.BusinessAuto;
import com.model.Invoice;
import com.model.Vehicle;
import com.repository.DBInvoiceRepository;
import com.service.AutoService;
import com.service.InvoiceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceCommand implements Command{
    @Override
    public void execute() {
        AutoService autoService = AutoService.getInstance();
        List<Auto> andSave = autoService.createAndSave(1);
//        DBInvoiceRepository DBInvoiceRepository = DBInvoiceRepository.getInstance();
//        List<BusinessAuto> businessAutoFromInvoice = DBInvoiceRepository.getBusinessAutoFromInvoice("c58b3e15-d3a3-4f5d-b2ef-6ac9a4453d52");
        List<Vehicle> vehicleList = new ArrayList<>(andSave);
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), LocalDateTime.now(), vehicleList);
        InvoiceService invoiceService = InvoiceService.getInstance();
        DBInvoiceRepository dbInvoiceRepository = DBInvoiceRepository.getInstance();
        dbInvoiceRepository.save(invoice);
    }
}
