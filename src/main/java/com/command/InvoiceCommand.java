package com.command;

import com.config.MongoConfig;
import com.mongodb.client.MongoDatabase;
import com.service.InvoiceService;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.util.Scanner;

public class InvoiceCommand implements Command {
    private static final InvoiceService INVOICE_SERVICE = InvoiceService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void execute() {
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
        System.out.println("----------Update date invoice----------");
        System.out.println("Enter id invoice, which you want update date: ");
        String idInvoiceUpdate = SCANNER.nextLine();
        INVOICE_SERVICE.updateDateInvoice(idInvoiceUpdate);
        System.out.println();
        System.out.println("---------Count invoices---------");
        INVOICE_SERVICE.printCountInvoiceInDB();
        System.out.println();
        System.out.println("-----------Invoice more limit-----------");
        System.out.println("Please enter limit: ");
        int limit = Integer.parseInt(SCANNER.nextLine());
        INVOICE_SERVICE.printInvoiceMorePrice(BigDecimal.valueOf(limit));
        System.out.println();
        System.out.println("-----------Group by price-----------");
        System.out.println(INVOICE_SERVICE.groupInvoiceByPrice());

        Flyway flyway = Flyway.configure()
                .dataSource( "jdbc:postgresql://ec2-44-207-133-100.compute-1.amazonaws.com/d9fblvo32d3uuj" , "xallqvkrezbfkh" , "2afb9e7ebdb2c2ddaada878ee6e6db773497e8fda4096a6dd71e59e19c4cb1b1" )
                .baselineOnMigrate(true)
                .locations("db/migration")
                .load();
        flyway.migrate();
    }
}
