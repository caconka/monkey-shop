package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.logic.UserLogicImpl;
import com.monkey.monkeyshop.domain.model.User;
import com.monkey.monkeyshop.domain.model.UserType;
import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.error.exceptions.BackendException;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.error.exceptions.ResourceNotFoundException;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import com.monkey.monkeyshop.secondary.postgres.dao.StoreDaoImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.core.http.HttpServerResponse;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowIterator;
import io.vertx.rxjava3.sqlclient.RowSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestUserHandler {

	private static final String DUMMY_USER_ID = "1234";
	private static final String DUMMY_EMAIL = "pedro@monkeyshop.es";
	private static final String DUMMY_PWD = "superSecret";
	private static final String DUMMY_ROLE = "USER";
	private static final String DUMMY_CREATED_AT = "2021-07-24 11:27:37";
	private static final String DUMMY_CREATED_BY = "test2@test.es";
	private static final String DUMMY_UPDATED_AT = "2021-07-24 13:27:37";
	private static final String DUMMY_UPDATED_BY = "test3@test.es";

	@Mock
	private CrmDao crmDao;

	@Mock
	private StorageClient storageClient;

	@Mock
	private RowSet<Row> rowSet;

	@Mock
	private Row row;

	@Mock
	private RowIterator<Row> rowIterator;

	@Mock
	private Buffer jsonObjectBuffer;

	@Mock
	private RoutingContext routingCtx;

	@Mock
	private HttpServerRequest request;

	@Mock
	private HttpServerResponse response;

	@Mock
	private io.vertx.core.http.HttpServerResponse delegateResponse;

	private ArgumentCaptor<String> responseBody;
	private Context ctx;
	private UserHandler userHandler;
	private Throwable thrown;

	@BeforeEach
	void setUp() {
		var storeDao = new StoreDaoImpl(storageClient);
		var userLogic = new UserLogicImpl(storeDao, crmDao);
		var jwtAuth = JWTAuth.create(
			Vertx.vertx(),
			new JWTAuthOptions().addPubSecKey(
				new PubSecKeyOptions()
					.setAlgorithm("HS256")
					.setBuffer("secret")
			)
		);

		userHandler = new UserHandler(userLogic, jwtAuth, SharedConfig.getInstance());
		ctx = Context.builder().build();
		response = new HttpServerResponse(delegateResponse);
		responseBody = null;
		thrown = null;

		when(routingCtx.get(eq(RequestContextHandler.CONTEXT))).thenReturn(ctx);
		when(routingCtx.request()).thenReturn(request);
		when(routingCtx.response()).thenReturn(response);
		when(delegateResponse.ended()).thenReturn(false);
		when(delegateResponse.closed()).thenReturn(false);
		when(delegateResponse.setStatusCode(anyInt())).thenReturn(delegateResponse);
		when(delegateResponse.putHeader(anyString(), anyString())).thenReturn(delegateResponse);
	}

	@Test
	public void should_get_a_list_of_users() {
		given_crmDao_listUsers_returns_ok();

		when_listUsers();

		then_HTTP_status_is(HttpResponseStatus.OK);
	}

	@Test
	public void should_not_get_list_if_crmDao_listUsers_fails() {
		given_crmDao_listUsers_returns_error();

		when_listUsers();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_get_user() {
		given_crmDao_getUser_returns_ok();

		when_getUser();

		then_HTTP_status_is(HttpResponseStatus.OK);
	}

	@Test
	public void should_not_get_user_if_crmDao_fails() {
		given_crmDao_getUser_returns_error();

		when_getUser();

		then_HTTP_status_is(HttpResponseStatus.NOT_FOUND);
	}

	@Test
	public void should_create_user() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_user.json");
		given_crmDao_createUser_returns_ok();
		given_storageClient_execute_returns_nothing();

		when_createUser();

		then_HTTP_status_is(HttpResponseStatus.CREATED);
	}

	@Test
	public void should_not_create_user_if_req_has_an_invalid_email() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_user_invalid_email.json");

		when_createUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_user_if_req_has_not_password() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_user_no_pwd.json");

		when_createUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_user_if_crmDao_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_user.json");
		given_crmDao_createUser_returns_error();
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));

		when_createUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_user_if_storage_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_user.json");
		given_crmDao_createUser_returns_ok();
		given_storageClient_execute_returns_error();

		when_createUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_update_user() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_update_user.json");
		given_crmDao_updateUser_returns_ok();
		given_storageClient_execute_returns_nothing();

		when_updateUser();

		then_HTTP_status_is(HttpResponseStatus.NO_CONTENT);
	}

	@Test
	public void should_not_update_user_if_req_has_an_invalid_email() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_update_user_invalid_email.json");

		when_updateUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_update_user_if_crmDao_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_update_user.json");
		given_crmDao_updateUser_returns_error();
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));

		when_updateUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_update_user_if_storage_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_update_user.json");
		given_crmDao_updateUser_returns_ok();
		given_storageClient_execute_returns_error();

		when_updateUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_delete_user() {
		given_userId_and_email();
		given_crmDao_deleteUser_returns_ok();
		given_storageClient_execute_returns_nothing();

		when_deleteUser();

		then_HTTP_status_is(HttpResponseStatus.NO_CONTENT);
	}

	@Test
	public void should_not_delete_user_if_invalid_email() {
		when(routingCtx.request().getParam("email")).thenReturn("test");

		when_deleteUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_delete_user_if_crmDao_fails() {
		given_userId_and_email();
		given_crmDao_deleteUser_returns_error();
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));

		when_deleteUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_delete_user_if_storage_fails() {
		given_userId_and_email();
		given_crmDao_deleteUser_returns_ok();
		given_storageClient_execute_returns_error();

		when_deleteUser();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	private void given_userId_and_email() {
		when(routingCtx.request().getParam("email")).thenReturn(DUMMY_EMAIL);
		when(routingCtx.request().getParam("userId")).thenReturn(DUMMY_USER_ID);
	}

	private void given_crmDao_listUsers_returns_ok() {
		when(crmDao.listUsers(ctx)).thenReturn(Single.just(List.of(new User())));
	}

	private void given_crmDao_listUsers_returns_error() {
		when(crmDao.listUsers(ctx)).thenReturn(Single.error(new BadRequestException("Bad request")));
	}

	private void given_crmDao_getUser_returns_ok() {
		var user = new User();
		user.setId(DUMMY_USER_ID);
		user.setEmail(DUMMY_EMAIL);
		user.setType(UserType.USER);
		user.setCreatedAt(DUMMY_CREATED_AT);
		user.setCreatedBy(DUMMY_CREATED_BY);
		user.setUpdatedBy(DUMMY_UPDATED_BY);
		user.setUpdatedAt(DUMMY_UPDATED_AT);
		when(crmDao.getUser(ctx, DUMMY_USER_ID)).thenReturn(Single.just(user));
	}

	private void given_crmDao_getUser_returns_error() {
		when(crmDao.getUser(ctx, DUMMY_USER_ID)).thenReturn(Single.error(new ResourceNotFoundException("User not found")));
	}

	private void given_crmDao_createUser_returns_ok() {
		when(crmDao.createUser(any(Context.class), any(User.class))).thenReturn(Completable.complete());
	}

	private void given_crmDao_createUser_returns_error() {
		when(crmDao.createUser(any(Context.class), any(User.class)))
			.thenReturn(Completable.error(new BadRequestException("Invalid format")));
	}

	private void given_crmDao_updateUser_returns_ok() {
		when(crmDao.updateUser(any(Context.class), any(User.class))).thenReturn(Completable.complete());
	}

	private void given_crmDao_deleteUser_returns_ok() {
		when(crmDao.deleteUser(any(Context.class), anyString())).thenReturn(Completable.complete());
	}

	private void given_crmDao_deleteUser_returns_error() {
		when(crmDao.deleteUser(any(Context.class), anyString()))
			.thenReturn(Completable.error(new BadRequestException("Bad request")));
	}

	private void given_crmDao_updateUser_returns_error() {
		when(crmDao.updateUser(any(Context.class), any(User.class)))
			.thenReturn(Completable.error(new BadRequestException("Invalid format")));
	}

	private void given_buffer_request_like_file(String filename) throws IllegalAccessException {
		var json = createJsonFromFile(filename);
		given_buffer_request_like_jsonObject(json);
	}

	private void given_storageClient_execute_returns() {
		mock_row_set();

		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));
	}

	private void given_storageClient_execute_returns_nothing() {
		when(rowIterator.hasNext()).thenReturn(false);
		when(rowSet.iterator()).thenReturn(rowIterator);
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));
	}

	private void given_storageClient_execute_returns_error() {
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.error(new BadRequestException("Bad request")));
	}

	private void given_buffer_request_like_jsonObject(JsonObject jsonObject) {
		when(jsonObjectBuffer.toJsonObject()).thenReturn(jsonObject);
		Mockito.doAnswer(
			(Answer<HttpServerRequest>) invocation -> {
				var args = invocation.getArguments();
				var bodyHandler = (Handler<Buffer>) args[0];
				bodyHandler.handle(jsonObjectBuffer);
				return request;
			}
		).when(request).bodyHandler(any());
	}

	private void when_listUsers() {
		userHandler.listUsers(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_getUser() {
		when(routingCtx.request().getParam("userId")).thenReturn(DUMMY_USER_ID);
		userHandler.getUser(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_createUser() {
		userHandler.createUser(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_updateUser() {
		userHandler.updateUser(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_deleteUser() {
		userHandler.deleteUser(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void then_HTTP_status_is(HttpResponseStatus httpStatus) {
		var statusCode = httpStatus.code();

		if (thrown != null) {
			if (thrown instanceof BackendException) {
				var err = (BackendException) thrown;
				assertEquals(statusCode, err.getHttpCode());
			} else {
				fail();
			}
		} else {
			verify(delegateResponse).setStatusCode(statusCode);
		}
	}

	private void and_response_is_like_file(String filename) throws IllegalAccessException {
		verify(delegateResponse).end(responseBody.capture());
		var expected = createJsonFromFile(filename);
		assertJsonEquals(expected, responseBody.getValue());
	}

	private void mock_row_set() {
		when(row.getString(anyString()))
			.thenReturn(DUMMY_EMAIL, DUMMY_PWD, DUMMY_ROLE, DUMMY_CREATED_AT, DUMMY_CREATED_BY, DUMMY_UPDATED_AT,
				DUMMY_UPDATED_BY);
		when(rowIterator.hasNext()).thenReturn(true);
		when(rowIterator.next()).thenReturn(row);
		when(rowSet.iterator()).thenReturn(rowIterator);
	}

	private JsonObject createJsonFromFile(String filename) throws IllegalAccessException{
		var resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
		if (resourceAsStream == null) {
			throw new IllegalAccessException();
		}

		var content = new BufferedReader(new InputStreamReader(resourceAsStream)).lines()
			.collect(Collectors.joining());
		return new JsonObject(content);
	}
}
