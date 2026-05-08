import repository.ContactRepository;
import repository.impl.ContactJdbcRepository;
import service.ContactService;
import ui.ContactMenu;

void main() {

    ContactRepository contactRepository = new ContactJdbcRepository();

    ContactService contactService = new ContactService(contactRepository);

    ContactMenu menu = new ContactMenu(contactService);

    menu.start();
}