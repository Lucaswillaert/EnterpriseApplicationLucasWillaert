package be.rungroup.eelucaswillaert.model;

import be.rungroup.eelucaswillaert.repository.ProductRepository;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//class voor data in de database te zetten
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository UserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(ProductRepository productRepository, be.rungroup.eelucaswillaert.repository.UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.UserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        productRepository.saveAll(List.of(
                createProduct("LED Spotlight 300W",
                        "Een krachtige en energiezuinige LED-spotlight met instelbare kleurtemperatuur. Ideaal voor podiumgebruik en filmsets.",
                        List.of(Tag.Lighting),
                        "src/main/resources/images/spotlight.jpg"),
                createProduct("Heavy-Duty Extension Cable (25m)",
                        "Robuuste verlengkabel van 25 meter met hoge capaciteit, geschikt voor zware apparatuur.",
                        List.of(Tag.Cables),
                        "src/main/resources/images/extension_cable.jpg"),
                createProduct("Portable Fog Machine",
                        "Compacte rookmachine die zachte en dichte mist produceert. Perfect voor dramatische effecten op filmsets.",
                        List.of(Tag.Stage_Elements),
                        "src/main/resources/images/fog_machine.jpg"),
                createProduct("Tripod Dolly with Wheels",
                        "Verstelbare dolly voor statieven, met soepele wielen en stevige remmen voor stabiliteit.",
                        List.of(Tag.Accessories),
                        "src/main/resources/images/tripod_dolly.jpg"),
                createProduct("DMX Lighting Control Console",
                        "Een gebruiksvriendelijk DMX-lichtpaneel voor het programmeren en aansturen van complexe lichtinstallaties.",
                        List.of(Tag.Control_Gear),
                        "src/main/resources/images/dmx_console.jpg"),
                createProduct("Gaffer Tape (Black, 50m)",
                        "Multifunctionele zwarte gaffertape voor het vastzetten van kabels en markeringen op de vloer.",
                        List.of(Tag.Accessories),
                        "src/main/resources/images/gaffer_tape.jpg"),
                createProduct("Fresnel Lens Light 650W",
                        "Professionele fresnel-lenslamp met dimbare functies en verstelbare focus voor nauwkeurige belichting.",
                        List.of(Tag.Lighting),
                        "src/main/resources/images/fresnel_light.jpg"),
                createProduct("XLR Audio Cable (10m)",
                        "Hoogwaardige XLR-kabel van 10 meter, ontworpen voor audiogebruik zonder signaalverlies.",
                        List.of(Tag.Cables),
                        "src/main/resources/images/xlr_cable.jpg"),
                createProduct("Foldable Stage Platform (2m x 1m)",
                        "Stevig en opvouwbaar podiumelement van 2 meter bij 1 meter, eenvoudig op te zetten en af te breken.",
                        List.of(Tag.Stage_Elements),
                        "src/main/resources/images/stage_platform.jpg"),
                createProduct("Wireless Follow Focus System",
                        "Professioneel draadloos systeem voor nauwkeurige scherpstelling op camera's, inclusief afstandsbediening.",
                        List.of(Tag.Control_Gear),
                        "src/main/resources/images/follow_focus.jpg")
        ));

        createAdminUser();
    }

    private Product createProduct(String name, String description, List<Tag> tags, String imagePath) throws IOException, IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setTags(tags);
        product.setTotalStock(10);
        product.setPhoto(Files.readAllBytes(Paths.get(imagePath)));
        return product;
    }

    private void createAdminUser() {
        User admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword(passwordEncoder.encode("adminpassword123"));
        admin.setAdmin(true);
        UserRepository.save(admin);
    }
}
