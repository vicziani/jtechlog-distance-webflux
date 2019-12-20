package cities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityWithTemperature {

    private City city;

    private String temperature;
}
