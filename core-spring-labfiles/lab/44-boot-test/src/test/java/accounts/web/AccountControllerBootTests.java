package accounts.web;

import accounts.AccountManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import rewards.internal.account.Account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// TODO-06: Get yourself familiarized with various testing utility classes
// as described in the lab document

// TODO-07: Use `@WebMvcTest` and `@AutoConfigureDataJpa` annotations
@ExtendWith(SpringExtension.class)
@AutoConfigureDataJpa
@WebMvcTest(AccountController.class)
public class AccountControllerBootTests {

	// TODO-08: Autowire MockMvc bean
	@Autowired
	MockMvc bean;
	// TODO-09: Create `AccountManager` mock bean
	@MockBean
	AccountManager accountManager;
	// TODO-12: Experiment with @MockBean vs @Mock
	// - Change `@MockBean` to `@Mock` for the `AccountManager dependency
	// - Run the test and observe a test failure

	// TODO-10: Write positive unit test for GET request for an accont
	@Test
	public void accountDetails() throws Exception {

		// arrange
		given(accountManager.getAccount(0L)).willReturn(new Account("1234567890", "John Doe"));
		// act and assert
		bean.perform(get("/accounts/0")).andExpect(status().isOk()) // for http 200
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)) //json return
				.andExpect(jsonPath("name").value("John Doe")).andExpect(jsonPath("number").value("1234567890"));

		// verify
		verify(accountManager).getAccount(0L);

	}

	// TODO-11: Write negative unit test for GET request for an account
	@Test
	public void accountDetailsFail() throws Exception {
		given(accountManager.getAccount(any(Long.class)))
				.willThrow(new IllegalArgumentException("No such account with id " + 0L));

		bean.perform(get("/accounts/0")).andExpect(status().isNotFound());

		verify(accountManager).getAccount(any(Long.class));



	}

    // TODO-13: Write unit test for `POST` request for an account
	@Test
	public void createAccount() throws Exception {
		Account testAccount = new Account("1234512345", "Mary Jones");
		testAccount.setEntityId(21L);
		given(accountManager.save(any(Account.class))).willReturn(testAccount);

		bean.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(testAccount)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/accounts/21"));

		verify(accountManager).save(any(Account.class));


	}


    // Utility class for converting an object into JSON string
	protected static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
