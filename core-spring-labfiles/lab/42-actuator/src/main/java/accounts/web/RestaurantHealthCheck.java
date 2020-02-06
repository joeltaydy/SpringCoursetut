package accounts.web;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import rewards.internal.restaurant.RestaurantRepository;

/**
 * TODO-17a: Make this class implement HealthIndicator
 *  - Make this class a component
 *  - Add a constructor to pass in the restaurant repository
 *    and use it to implement health().
 *  - health() should return DOWN if the repository is empty
 *    (no restaurants) or UP otherwise.
 *
 * TODO-24 (Extra credit): Experiment with HealthIndicator (Read lab document)
 */
@Component
public class RestaurantHealthCheck implements HealthIndicator {
    private RestaurantRepository restaurantRepository;
    public RestaurantHealthCheck(RestaurantRepository restaurantRepository){
        this.restaurantRepository=restaurantRepository;
    }

    @Override
    public Health health() {
        if (restaurantRepository.getRestaurantCount()>=1){
            return Health.up().build();
        }else{
            return Health.down().build();
        }
    }
}
