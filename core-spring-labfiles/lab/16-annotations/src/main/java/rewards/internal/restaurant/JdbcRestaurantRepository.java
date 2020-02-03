package rewards.internal.restaurant;

import common.money.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads restaurants from a data source using the JDBC API.
 *
 * This implementation should cache restaurants to improve performance. The
 * cache should be populated on initialization and cleared on destruction.
 */

/*
 * TODO-06: Annotate the class with an appropriate stereotype annotation to
 * cause component-scan to detect and load this bean. Configure Dependency
 * Injection for dataSource. Use constructor injection in this case (note the
 * logic in the constructor requires a dataSource).
 */

/*
 * TODO-08: Change the configuration to set the dataSource property using
 * setDataSource(). To do this, you must MOVE the @Autowired annotation
 * you might have set in the previous step on the constructor injecting DataSource.
 * So neither constructor should be annotated with @Autowired now, so Spring uses
 * the default constructor by default.
 *
 * Re-run the test. It should fail. Examine the stack trace and see if you can
 * understand why. (If not, refer to the detailed lab instructions). We will fix
 * this error in the next step."
 */
@Repository
public class JdbcRestaurantRepository implements RestaurantRepository {

	private DataSource dataSource;

	/**
	 * The Restaurant object cache. Cached restaurants are indexed by their merchant
	 * numbers.
	 */
	private Map<String, Restaurant> restaurantCache;

	/**
	 * The constructor sets the data source this repository will use to load
	 * restaurants. When the instance of JdbcRestaurantRepository is created, a
	 * Restaurant cache is populated for read only access
	 */

	public JdbcRestaurantRepository(DataSource dataSource) {
		this.dataSource = dataSource;
		this.populateRestaurantCache();
	}

	public JdbcRestaurantRepository() {
	}
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Restaurant findByMerchantNumber(String merchantNumber) {
		return queryRestaurantCache(merchantNumber);
	}

	/**
	 * Helper method that populates the restaurantCache restaurant object
	 * caches from the rows in the T_RESTAURANT table. Cached restaurants are indexed
	 * by their merchant numbers. This method should be called on initialization.
	 */

	/*
	 * TODO-09: Mark this method with an annotation that will cause it to be
	 * executed by Spring after constructor & setter initialization has occurred.
	 *
	 * Re-run the RewardNetworkTests test. You should see the test succeed.
	 *
	 * Populating the cache is not really a valid construction activity, so using a
	 * post-construct, rather than the constructor, is better practice.
	 */
	@PostConstruct
	void populateRestaurantCache() {
		restaurantCache = new HashMap<String, Restaurant>();
		String sql = "select MERCHANT_NUMBER, NAME, BENEFIT_PERCENTAGE from T_RESTAURANT";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Restaurant restaurant = mapRestaurant(rs);
				// index the restaurant by its merchant number
				restaurantCache.put(restaurant.getNumber(), restaurant);
			}
		} catch (SQLException e) {
			throw new RuntimeException("SQL exception occurred finding by merchant number", e);
		} finally {
			if (rs != null) {
				try {
					// Close to prevent database cursor exhaustion
					rs.close();
				} catch (SQLException ex) {
				}
			}
			if (ps != null) {
				try {
					// Close to prevent database cursor exhaustion
					ps.close();
				} catch (SQLException ex) {
				}
			}
			if (conn != null) {
				try {
					// Close to prevent database connection exhaustion
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	/**
	 * Helper method that simply queries the cache of restaurants.
	 *
	 * @param merchantNumber
	 *            the restaurant's merchant number
	 * @return the restaurant
	 * @throws EmptyResultDataAccessException
	 *             if no restaurant was found with that merchant number
	 */
	private Restaurant queryRestaurantCache(String merchantNumber) {
		Restaurant restaurant = restaurantCache.get(merchantNumber);
		if (restaurant == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return restaurant;
	}

	/**
	 * Helper method that clears the cache of restaurants.
	 * This method should be called when a bean is destroyed.
	 *
	 * TODO-10: To see if this method is being invoked either
	 * (1) add a breakpoint and use the debugger or
	 * (2) use System.out.println
	 * to write a message to the console.
	 *
	 * TODO-11: Re-run RewardNetworkTests. You should see that this method is never
	 * called. Use an annotation to register this method for a destruction lifecycle
	 * callback. Re-run the test and you should be able to see that this method is
	 * now being called.
	 */
	@PreDestroy
	public void clearRestaurantCache() {
		restaurantCache.clear();
		System.out.println("clearRestaurantCache invoked");
	}

	/**
	 * Maps a row returned from a query of T_RESTAURANT to a Restaurant object.
	 *
	 * @param rs
	 *            the result set with its cursor positioned at the current row
	 */
	private Restaurant mapRestaurant(ResultSet rs) throws SQLException {
		// get the row column data
		String name = rs.getString("NAME");
		String number = rs.getString("MERCHANT_NUMBER");
		Percentage benefitPercentage = Percentage.valueOf(rs.getString("BENEFIT_PERCENTAGE"));
		// map to the object
		Restaurant restaurant = new Restaurant(number, name);
		restaurant.setBenefitPercentage(benefitPercentage);
		return restaurant;
	}
}