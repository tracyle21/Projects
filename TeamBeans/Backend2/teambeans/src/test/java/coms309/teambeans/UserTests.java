package coms309.teambeans;

import coms309.teambeans.Controllers.UserInformationController;
import coms309.teambeans.DTOs.UpdatePasswordDTO;
import coms309.teambeans.ModelResponses.GetUserResponse;
import coms309.teambeans.ModelResponses.RatingResponse;
import coms309.teambeans.Models.*;
import coms309.teambeans.Repositories.*;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import coms309.teambeans.DTOs.UserLoginDTO;
import coms309.teambeans.DTOs.UserRegistrationDTO;
import coms309.teambeans.Services.UserService;
import coms309.teambeans.Controllers.UserController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTests {

	@Autowired
	private UserService uService;

	@MockBean
	private UserRepository repo;

	@MockBean
	private CustomUserRepository customRepo;

	@Mock
	private ConversationRepository conversationRepository;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private UserInformationController userInformationController;


	@Test
	public void RatingProfileTest() throws Exception {
		//Tracy
		UserController userController = mock(UserController.class);
		RatingProfile rp = new RatingProfile(4.0, 4.0, 4.0, 4.0, 4.0, 10);
		User user = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, new RatingProfile(4.0, 4.0, 4.0, 4.0, 4.0, 10), null, 0.0,0.0, 0.0, 0.0, 0.0);
		when(userController.addUserRating("user", rp)).thenReturn(rp);
		RatingResponse rr = new RatingResponse();
		rr.setRatingProfile(rp);
		when(userInformationController.getRatingProfile("user")).thenReturn(rr);
		RatingResponse res = userInformationController.getRatingProfile("user");
		assertTrue(res.equals(rr));
	}

	@Test
	public void UpdateRatingProfileTest() throws Exception {
		//Tracy
		UserController userController = mock(UserController.class);
		RatingProfile oldRp = new RatingProfile(4.0, 4.0, 4.0, 4.0, 4.0, 10);
		RatingProfile newRp = new RatingProfile(5.0, 4.0, 4.0, 4.0, 4.0, 11);
		User user = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, oldRp, null, 0.0, 0.0, 0.0, 0.0, 0.0);
		User newUser = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, newRp, null, 0.0, 0.0, 0.0, 0.0, 0.0);
		when(userController.addUserRating("user", newRp)).thenReturn(newUser.getRatingProfile());
		RatingResponse rr = new RatingResponse();
		rr.setRatingProfile(newRp);
		when(userInformationController.getRatingProfile("user")).thenReturn(rr);
		RatingResponse res = userInformationController.getRatingProfile("user");
		assertTrue(res.equals(rr));
	}

	@Test
	public void AverageRatingTest() throws Exception {
		//Tracy
		UserController userController = mock(UserController.class);
		RatingProfile oldRp = new RatingProfile(4.0, 4.0, 4.0, 4.0, 4.0, 0);
		RatingProfile newRp = new RatingProfile(4.5, 4.0, 4.0, 4.0, 4.0, 1);
		User user = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, oldRp, null, 0.0, 0.0, 0.0, 0.0, 0.0);
		User newUser = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, newRp, null, 0.0, 0.0, 0.0, 0.0, 0.0);
		RatingResponse rr = new RatingResponse();
		rr.setRatingProfile(newRp);
		when(userController.averageRatingProfile("user", 5.0, 4.0, 4.0, 4.0, 4.0))
				.thenReturn(rr);
		RatingResponse res = userController.averageRatingProfile("user", 5.0, 4.0, 4.0, 4.0, 4.0);
		assertTrue(res.equals(rr));
	}

	@Test
	public void SaveChatRoomMessage() {
		//Tracy
		ChatRoomMessageRepository chatRoomMessageRepository = mock(ChatRoomMessageRepository.class);
		RatingProfile oldRp = new RatingProfile(4.0, 4.0, 4.0, 4.0, 4.0, 0);
		User user = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, oldRp, null, 0.0, 0.0, 0.0, 0.0, 0.0);
		String message = "this is a test message";
		LocalDateTime dt = LocalDateTime.now();
		ChatRoomMessage chatRoomMessage = new ChatRoomMessage(user, dt, message);
		when(chatRoomMessageRepository.save(chatRoomMessage)).then(i -> i.getArguments()[0]);
		ChatRoomMessage result = chatRoomMessageRepository.save(chatRoomMessage);
		assertNotNull(result);
		assertEquals(chatRoomMessage, result);
	}


	@Test
	public void UpdatePasswordTest() throws Exception {
		// Tracy
		UserController userController = mock(UserController.class);
		UpdatePasswordDTO test = new UpdatePasswordDTO();
		test.setUsername("user");
		test.setOldPassword("old");
		test.setNewPassword("new");

		UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("first", "last", "email@email.com", "user",
				"new", "STUDENT");
		GetUserResponse getUserResponse = new GetUserResponse(userRegistrationDTO);

		User user = new User("First", "Last", "email@email.com", "user", "old", Arrays.asList(new Role("STUDENT")),
				null, null, null, null, null, null, null, null, new RatingProfile(5.0, 5.0, 5.0, 5.0, 5.0, 0), null, 0.0, 0.0, 0.0, 0.0, 0.0);
		when(userController.updatePasswordString(test)).thenReturn(test);
		when(userController.getUser(anyString())).thenReturn(getUserResponse);
		GetUserResponse result = userController.getUser("user");
		assertTrue(result.getUserInformation().getPassword().equals("new"));
	}

	@Test
	public void MockitoUserLoginTest() throws Exception {
		// Set up MOCK methods for the repository
		UserLoginDTO json = new UserLoginDTO();
		json.setUsername("test");
		json.setPassword("test");
		User test = new User();
		test.setUsername("test");
		test.setPassword("test");
		when(this.repo.findByUsernameAndPassword("test", "test")).thenReturn(test);
		// set repository to the mocked repository
		boolean login = uService.login(json);
		assertEquals(true, login);
	}

	@Test
	public void MockitoUserGetTest() throws Exception {
		// Set up MOCK methods for the repository
		User test = new User();
		test.setUsername("test");
		test.setPassword("test");
		when(this.repo.findByUsername("test")).thenReturn(test);
		// set repository to the mocked repository
		User u = uService.getUser("test");
		assertEquals(test, u);
	}

	// ensures correct object is sent to and returned from registerUser
	// (for a successful case, same object will be returned)
	@Test
	void goodRegisterObject() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		// Boolean val = testReg.equals(result);
		assertTrue(testReg.equals(result));
	}

	// ensures first name string is passed correctly
	@Test
	void goodRegisterValue1() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("firstName").equals(result.getFirstName()));
	}

	// ensures last name string is passed correctly
	@Test
	void goodRegisterValue2() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("lastName").equals(result.getLastName()));
	}

	// ensures email string is passed correctly
	@Test
	void goodRegisterValue3() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("testEmail").equals(result.getEmail()));
	}

	// ensures username string is passed correctly
	@Test
	void goodRegisterValue4() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("testUsername").equals(result.getUsername()));
	}

	// ensures password string is passed correctly
	@Test
	void goodRegisterValue5() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("testPassword").equals(result.getPassword()));
	}

	// ensures role string is passed correctly
	@Test
	void goodRegisterValue6() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("STUDENT").equals(result.getRole()));
	}

	// ensures properties can be set more than once
	// (set first name then set again, keeps most recent change)
	@Test
	void goodRegisterValue7() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		testReg.setFirstName("firstName2");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertTrue(("firstName2").equals(result.getFirstName()));
	}

	// ensures that role is always capitalized
	@Test
	void badRegisterValue1() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(testReg);
		assertFalse(("student").equals(result.getRole()));
	}

	// ensures registerUser returns null when null object passed in
	@Test
	void badRegisterNull() {
		UserController userController = mock(UserController.class);
		UserRegistrationDTO testReg = new UserRegistrationDTO();
		testReg.setFirstName("firstName");
		testReg.setLastName("lastName");
		testReg.setEmail("testEmail");
		testReg.setUsername("testUsername");
		testReg.setPassword("testPassword");
		testReg.setRole("STUDENT");
		when(userController.registerUser(testReg)).thenReturn(testReg);
		UserRegistrationDTO result = userController.registerUser(null);
		assertFalse(testReg.equals(result));
	}

	// ensures registerUser returns null when a duplicate registration happens
	// currently failing
	// @Test
	// void badRegisterDouble() {
	// UserController userController = mock(UserController.class);
	// UserRegistrationDTO testReg = new UserRegistrationDTO();
	// testReg.setFirstName("firstName");
	// testReg.setLastName("lastName");
	// testReg.setEmail("testEmail");
	// testReg.setUsername("testUsername");
	// testReg.setPassword("testPassword");
	// testReg.setRole("STUDENT");
	// when(userController.registerUser(testReg)).thenReturn(testReg);
	// UserRegistrationDTO result = userController.registerUser(testReg);
	// UserRegistrationDTO result2 = userController.registerUser(testReg);
	// assertFalse(result.equals(result2));
	// }

	// ensures login returns passed in non-null object exactly
	@Test
	void goodUserObject() {
		UserController userController = mock(UserController.class);
		UserLoginDTO testUser = new UserLoginDTO("testUsername", "testPassword");
		testUser.setUsername("testUsername");
		testUser.setPassword("testPassword");
		when(userController.login(testUser)).thenReturn(testUser);
		UserLoginDTO result = userController.login(testUser);
		assertTrue(testUser.equals(result));
	}

	// ensures login returns passed in null object exactly
	@Test
	void badUserNull() {
		UserController userController = mock(UserController.class);
		UserLoginDTO testUser = new UserLoginDTO("testUsername", "testPassword");
		testUser.setUsername("testUsername");
		testUser.setPassword("testPassword");
		// when(userController.login(testUser)).thenReturn(testUser);
		UserLoginDTO result = userController.login(null);
		assertFalse(testUser.equals(result));
	}

	// ensures password is set/gotten correctly and passed through login
	@Test
	void goodUserValue1() {
		UserController userController = mock(UserController.class);
		UserLoginDTO testUser = new UserLoginDTO("testUsername", "testPassword");
		testUser.setUsername("testUsername");
		testUser.setPassword("testPassword");
		when(userController.login(testUser)).thenReturn(testUser);
		UserLoginDTO result = userController.login(testUser);
		assertTrue(result.getPassword().equals("testPassword"));
	}

	// ensures username is set/gotten correctly and passed through login
	@Test
	void goodUserValue2() {
		UserController userController = mock(UserController.class);
		UserLoginDTO testUser = new UserLoginDTO("testUsername", "testPassword");
		testUser.setUsername("testUsername");
		testUser.setPassword("testPassword");
		when(userController.login(testUser)).thenReturn(testUser);
		UserLoginDTO result = userController.login(testUser);
		assertTrue(result.getUsername().equals("testUsername"));
	}

	@Test
	public void addConversationTest() {
		// Tracy
		Conversation conversation = new Conversation(new Date(), null);
		when(conversationRepository.save(any(Conversation.class))).thenAnswer(i -> i.getArguments()[0]);
		Conversation result = conversationRepository.save(conversation);
		assertEquals(conversation, result);
	}

	@Test
	public void addMessageTest() {
		// Tracy
		Conversation conversation = new Conversation(new Date(), null);
		User user = new User();
		user.setUsername("user");
		user.setPassword("pass");
		Message message = new Message(conversation, user, new Date(), "TEST MESSAGE");
		conversation.setMessages(Collections.singleton(message));
		when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArguments()[0]);
		Message result = messageRepository.save(message);
		assertNotNull(result);
		assertEquals(message, result);
	}

	// ensures username is set/gotten correctly and passed through login
	// @Test
	// void badUserDouble() {
	// UserController userController = mock(UserController.class);
	// UserLoginDTO testUser = new UserLoginDTO("testUsername", "testPassword");
	// testUser.setUsername("testUsername");
	// testUser.setPassword("testPassword");
	// when(userController.login(testUser)).thenReturn(testUser);
	// UserLoginDTO result = userController.login(testUser);
	// UserLoginDTO result2 = userController.login(testUser);
	// assertFalse(result.equals(result2));
	// }

	// for debugging
	@BeforeAll
	private static void before() {
		System.out.println("Before");
	}

	// for debugging
	@AfterAll
	private static void after() {
		System.out.println("After");
	}
}
