import model.Contact;
import repository.ContactRepository;
import repository.impl.ContactJdbcRepository;
import service.ContactService;
import service.ExportService;
import service.impl.ContactCsvExportService;
import ui.ContactMenu;

void main() {

    ContactRepository contactRepository = new ContactJdbcRepository();
    ExportService<Contact> exportService = new ContactCsvExportService();

    ContactService contactService = new ContactService(contactRepository, exportService);

    ContactMenu menu = new ContactMenu(contactService);

    menu.start();
}