package service.impl;

import service.ExportService;
import model.Contact;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ContactCsvExportService implements ExportService<Contact> {

    @Override
    public void export(List<Contact> contacts, String filePath) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            writer.write('\ufeff');

            writer.write("ID;Nome;Email;Categoria;Telefones");
            writer.newLine();

            for (Contact c : contacts) {
                String phones = String.join(" | ", c.getPhones());
                String line = String.format("%d;%s;%s;%s;%s",
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getCategory(),
                        phones
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }
}