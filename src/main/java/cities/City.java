package cities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Locale;
import java.util.Scanner;

@Data
@Table("cities")
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    private Long id;

    private String name;

    private double lat;

    private double lon;

    public static City parse(String s) {
        var scanner = new Scanner(s).useDelimiter(",").useLocale(Locale.ENGLISH);
        var city = new City();
        city.name = scanner.next();
        city.lat = scanner.nextDouble();
        city.lon = scanner.nextDouble();
        return city;
    }

}
