package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.logic.AuthLogicImpl;
import com.monkey.monkeyshop.error.exceptions.BackendException;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.secondary.postgres.dao.StorageClient;
import com.monkey.monkeyshop.secondary.postgres.dao.StoreDaoImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
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
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestAuthHandler {

	private static final String DUMMY_BEARER_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb25rZXlzaG9wLmNvbSIsImVtYWlsIjoidGVzdEB0ZXN0LmVzIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE2MjcyMDE3NjEsImV4cCI6MTYyNzIwMjM2MX0.ubef1zJf3K0f-O5q0wAJ3Y4avoY3qwBuZLehvKBcoAg";
	private static final String DUMMY_TRACE_ID = "trace";
	private static final String DUMMY_EMAIL = "test@test.es";
	private static final String DUMMY_PWD = "$2a$12$xTQ3pQl28kxLOm5xbkOYi.yqOGtkv2LK4Xv5q5bkp/DHjMTsJrCZK";
	private static final String DUMMY_ROLE = "USER";
	private static final String DUMMY_CREATED_AT = "2021-07-24T11:27:37";
	private static final String DUMMY_CREATED_BY = "test2@test.es";
	private static final String DUMMY_UPDATED_AT = "2021-07-24T13:27:37";
	private static final String DUMMY_UPDATED_BY = "test3@test.es";

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

	@Mock
	private SharedConfig config;

	private ArgumentCaptor<String> responseBody;
	private AuthHandler authHandler;
	private Throwable thrown;

	@BeforeEach
	void setUp() {
		var storeDao = new StoreDaoImpl(storageClient);
		var jwtAuth = JWTAuth.create(
			Vertx.vertx(),
			new JWTAuthOptions().addPubSecKey(
				new PubSecKeyOptions()
					.setAlgorithm("HS256")
					.setBuffer("secret")
			)
		);
		var authLogic = new AuthLogicImpl(storeDao, jwtAuth, config);
		var ctx = Context.builder().withTraceId(DUMMY_TRACE_ID).build();

		authHandler = new AuthHandler(authLogic, jwtAuth, config);
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
	public void should_get_token() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth.json");
		given_storageClient_execute_returns(DUMMY_PWD);

		when_getToken();

		then_HTTP_status_is(HttpResponseStatus.OK);
	}

	@Test
	public void should_not_get_token_if_password_is_not_equals() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth.json");
		given_storageClient_execute_returns("invalidSecret");

		when_getToken();

		then_HTTP_status_is(HttpResponseStatus.UNAUTHORIZED);
	}

	@Test
	public void should_not_get_token_if_storage_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth.json");
		given_storageClient_execute_returns_error();

		when_getToken();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_get_token_if_req_has_invalid_email() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth_invalid_email.json");

		when_getToken();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_get_token_if_req_has_no_password() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth_no_pwd.json");

		when_getToken();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_update_pwd() throws IllegalAccessException {
		when(routingCtx.request().getHeader("Authorization")).thenReturn(DUMMY_BEARER_TOKEN);
		given_buffer_request_like_file("mock/req_auth.json");
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));

		when_updateToken();

		then_HTTP_status_is(HttpResponseStatus.NO_CONTENT);
	}

	@Test
	public void should_not_update_pwd_if_logged_email_is_not_equals() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth_different_email.json");

		when_updateToken();

		then_HTTP_status_is(HttpResponseStatus.UNAUTHORIZED);
	}

	@Test
	public void should_not_update_pwd_if_req_has_no_password() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth_no_pwd.json");

		when_updateToken();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_update_pwd_if_req_has_invalid_email() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_auth_invalid_email.json");

		when_updateToken();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	private void given_buffer_request_like_file(String filename) throws IllegalAccessException {
		var json = createJsonFromFile(filename);
		given_buffer_request_like_jsonObject(json);
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

	private void given_storageClient_execute_returns(String pwd) {
		mock_row_set(pwd);

		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.just(rowSet))
			.thenReturn(Single.just(rowSet));
	}

	private void given_storageClient_execute_returns_error() {
		when(storageClient.execute(any(Context.class), anyString()))
			.thenReturn(Single.error(new BadRequestException("Bad request")));
	}

	private void when_getToken() {
		authHandler.token(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_updateToken() {
		authHandler.updatePwd(routingCtx);
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

	private void mock_row_set(String pwd) {
		when(row.getString(anyString()))
			.thenReturn(DUMMY_EMAIL, pwd, DUMMY_ROLE, DUMMY_CREATED_BY, DUMMY_UPDATED_BY);
		when(row.getLocalDateTime(anyString()))
			.thenReturn(LocalDateTime.parse(DUMMY_CREATED_AT), LocalDateTime.parse(DUMMY_UPDATED_AT));
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
