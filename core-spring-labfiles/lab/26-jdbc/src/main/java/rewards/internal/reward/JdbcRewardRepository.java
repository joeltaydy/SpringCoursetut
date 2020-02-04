package rewards.internal.reward;

import common.datetime.SimpleDate;
import org.springframework.jdbc.core.JdbcTemplate;
import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;

import javax.sql.DataSource;
import java.sql.*;

/**
 * JDBC implementation of a reward repository that records the result of a reward transaction by inserting a reward
 * confirmation record.
 */


// TODO-03: Refactor the cumbersome JDBC code in JdbcRewardRepository with JdbcTemplate.
// - Add a field of type JdbcTemplate.  Refactor the constructor to instantiate it.
// - Refactor the confirmReward(...) and nextConfirmationNumber() methods to use
//   the template.
//
//   DO NOT delete the old JDBC code, just comment out the try/catch block.
//   You will need to refer to the old JDBC code to write the new JdbcTemplate code.
//
// - Save all work, run the JdbcRewardRepositoryTests.  It should pass.

public class JdbcRewardRepository implements RewardRepository {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	public JdbcRewardRepository(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public RewardConfirmation confirmReward(AccountContribution contribution, Dining dining) {
		String sql = "insert into T_REWARD (CONFIRMATION_NUMBER, REWARD_AMOUNT, REWARD_DATE, ACCOUNT_NUMBER, DINING_MERCHANT_NUMBER, DINING_DATE, DINING_AMOUNT) values (?, ?, ?, ?, ?, ?, ?)";
		String confirmationNumber = nextConfirmationNumber();

		// Update the T_REWARD table with the new Reward
		/*try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, confirmationNumber);
			ps.setBigDecimal(2, contribution.getAmount().asBigDecimal());
			ps.setDate(3, new Date(SimpleDate.today().inMilliseconds()));
			ps.setString(4, contribution.getAccountNumber());
			ps.setString(5, dining.getMerchantNumber());
			ps.setDate(6, new Date(dining.getDate().inMilliseconds()));
			ps.setBigDecimal(7, dining.getAmount().asBigDecimal());
			ps.execute();
		} catch (SQLException e) {
			throw new RuntimeException("SQL exception occured inserting reward record", e);
		}*/
		jdbcTemplate.update(sql,confirmationNumber,contribution.getAmount().asBigDecimal(),
				new Date(SimpleDate.today().inMilliseconds()), contribution.getAccountNumber(), dining.getMerchantNumber()
		,new Date(dining.getDate().inMilliseconds()),dining.getAmount().asBigDecimal() );
		return new RewardConfirmation(confirmationNumber, contribution);
	}

	private String nextConfirmationNumber() {
		String sql = "select next value for S_REWARD_CONFIRMATION_NUMBER from DUAL_REWARD_CONFIRMATION_NUMBER";
		String nextValue = jdbcTemplate.queryForObject(sql,String.class);
		
		/*try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			rs.next();
			nextValue = rs.getString(1);
		} catch (SQLException e) {
			throw new RuntimeException("SQL exception getting next confirmation number", e);
		}*/
		
		return nextValue;
	}
}
