package com.command;

import com.config.MongoConfig;
import com.mongodb.client.MongoDatabase;
import com.service.InvoiceService;

import java.math.BigDecimal;
import java.util.Scanner;

public class InvoiceCommand implements Command {
    private static final InvoiceService INVOICE_SERVICE = InvoiceService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
        MongoConfig mongoConfig = new MongoConfig();
        MongoDatabase database = mongoConfig.connect("course_nix");
        database.drop();
        System.out.println("----------Create and save Invoice---------");
        System.out.println("Please enter count Vehicle, which must be in Invoice: ");
        int count = Integer.parseInt(SCANNER.nextLine());
        INVOICE_SERVICE.createAndSaveRandomInvoice(count);
        INVOICE_SERVICE.createAndSaveRandomInvoice(count);
        INVOICE_SERVICE.createAndSaveRandomInvoice(count);
        System.out.println();
        System.out.println("---------Find by id--------");
        System.out.println("Enter id invoice, which you want find: ");
        String idInvoice = SCANNER.nextLine();
        INVOICE_SERVICE.findInvoiceById(idInvoice);
        System.out.println();
        System.out.println("-------Print all invoices--------");
        INVOICE_SERVICE.printAll();
        System.out.println();
        System.out.println("-------Delete invoice---------");
        System.out.println("Enter id invoice, which you want delete: ");
        String idInvoiceDelete = SCANNER.nextLine();
        INVOICE_SERVICE.deleteInvoiceById(idInvoiceDelete);
        System.out.println();
//        System.out.println("----------Update date invoice----------");
//        System.out.println("Enter id invoice, which you want update date: ");
//        String idInvoiceUpdate = SCANNER.nextLine();
//        INVOICE_SERVICE.updateDateInvoice(idInvoiceUpdate);
//        System.out.println();
        System.out.println("---------Count invoices---------");
        INVOICE_SERVICE.printCountInvoiceInDB();
        System.out.println();
        System.out.println("-----------Invoice more limit-----------");
        System.out.println("Please enter limit: ");
        int limit = Integer.parseInt(SCANNER.nextLine());
        INVOICE_SERVICE.printInvoiceMorePrice(BigDecimal.valueOf(limit));
        System.out.println();
//        System.out.println("-----------Group by price-----------");
//        System.out.println(INVOICE_SERVICE.groupInvoiceByPrice());

    }
}
