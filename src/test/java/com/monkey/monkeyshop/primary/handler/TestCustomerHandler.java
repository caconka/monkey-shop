package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.config.SharedConfig;
import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.domain.logic.CustomerLogicImpl;
import com.monkey.monkeyshop.domain.model.Customer;
import com.monkey.monkeyshop.domain.model.command.UpdateCustomerImgCmd;
import com.monkey.monkeyshop.domain.port.CrmDao;
import com.monkey.monkeyshop.error.exceptions.BackendException;
import com.monkey.monkeyshop.error.exceptions.BadRequestException;
import com.monkey.monkeyshop.error.exceptions.ResourceNotFoundException;
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
import io.vertx.rxjava3.ext.web.FileUpload;
import io.vertx.rxjava3.ext.web.RoutingContext;
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
import java.util.Set;
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
public class TestCustomerHandler {

	private static final String DUMMY_CUSTOMER_ID = "1234";
	private static final String DUMMY_EMAIL = "pedro@monkeyshop.es";
	private static final String DUMMY_CREATED_AT = "2021-07-24 11:27:37";
	private static final String DUMMY_CREATED_BY = "test2@test.es";
	private static final String DUMMY_UPDATED_AT = "2021-07-24 13:27:37";
	private static final String DUMMY_UPDATED_BY = "test3@test.es";
	private static final BadRequestException DUMMY_BAD_REQUEST = new BadRequestException("Bad request");

	@Mock
	private CrmDao crmDao;

	@Mock
	private FileUpload fileUpload;

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
	private CustomerHandler customerHandler;
	private Throwable thrown;

	@BeforeEach
	void setUp() {
		var customerLogic = new CustomerLogicImpl(crmDao);
		var jwtAuth = JWTAuth.create(
			Vertx.vertx(),
			new JWTAuthOptions().addPubSecKey(
				new PubSecKeyOptions()
					.setAlgorithm("HS256")
					.setBuffer("secret")
			)
		);

		customerHandler = new CustomerHandler(customerLogic, jwtAuth, SharedConfig.getInstance());
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
	public void should_get_a_list_of_customers() {
		given_crmDao_listCustomers_returns_ok();

		when_listCustomers();

		then_HTTP_status_is(HttpResponseStatus.OK);
	}

	@Test
	public void should_not_get_list_if_crmDao_listCustomers_fails() {
		given_crmDao_listCustomers_returns_error();

		when_listCustomers();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_get_customer() {
		given_customerId();
		given_crmDao_getCustomer_returns_ok();

		when_getCustomer();

		then_HTTP_status_is(HttpResponseStatus.OK);
	}

	@Test
	public void should_not_get_customer_if_crmDao_fails() {
		given_customerId();
		given_crmDao_getCustomer_returns_error();

		when_getCustomer();

		then_HTTP_status_is(HttpResponseStatus.NOT_FOUND);
	}

	@Test
	public void should_create_customer() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer.json");
		given_crmDao_createCustomer_returns_ok();

		when_createCustomer();

		then_HTTP_status_is(HttpResponseStatus.CREATED);
	}

	@Test
	public void should_not_create_customer_if_crmDao_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer.json");
		given_crmDao_createCustomer_returns_error();

		when_createCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_customer_if_req_has_invalid_email() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer_invalid_email.json");

		when_createCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_customer_if_req_has_no_personal_field() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer_no_personal.json");

		when_createCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_customer_if_req_has_no_name() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer_no_name.json");

		when_createCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_create_customer_if_req_has_no_surname() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer_no_surname.json");

		when_createCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_update_customer() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer.json");
		given_crmDao_updateCustomer_returns_ok();

		when_updateCustomer();

		then_HTTP_status_is(HttpResponseStatus.NO_CONTENT);
	}

	@Test
	public void should_not_update_customer_if_req_has_email_and_it_is_invalid() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer_invalid_email.json");

		when_updateCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_not_update_customer_if_crmDao_fails() throws IllegalAccessException {
		given_buffer_request_like_file("mock/req_create_customer.json");
		given_crmDao_updateCustomer_returns_error();

		when_updateCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_update_customer_img() {
		given_file_upload();
		given_crmDao_updateCustomerImg_returns_ok();

		when_updateCustomerImg();

		then_HTTP_status_is(HttpResponseStatus.NO_CONTENT);
	}

	@Test
	public void should_not_update_customer_img_if_crmDao_fails() {
		given_file_upload();
		given_crmDao_updateCustomerImg_returns_error();

		when_updateCustomerImg();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	@Test
	public void should_delete_customer() {
		given_customerId();
		given_crmDao_deleteCustomer_returns_ok();

		when_deleteCustomer();

		then_HTTP_status_is(HttpResponseStatus.NO_CONTENT);
	}

	@Test
	public void should_not_delete_customer_if_crmDao_fails() {
		given_customerId();
		given_crmDao_deleteCustomer_returns_error();

		when_deleteCustomer();

		then_HTTP_status_is(HttpResponseStatus.BAD_REQUEST);
	}

	private void given_file_upload() {
		when(routingCtx.fileUploads()).thenReturn(Set.of(fileUpload));
	}

	private void given_customerId() {
		when(routingCtx.request().getParam("customerId")).thenReturn(DUMMY_CUSTOMER_ID);
	}

	private void given_crmDao_listCustomers_returns_ok() {
		when(crmDao.listCustomers(ctx)).thenReturn(Single.just(List.of(new Customer())));
	}

	private void given_crmDao_listCustomers_returns_error() {
		when(crmDao.listCustomers(ctx)).thenReturn(Single.error(DUMMY_BAD_REQUEST));
	}

	private void given_crmDao_getCustomer_returns_ok() {
		when(crmDao.getCustomer(any(Context.class), anyString())).thenReturn(Single.just(new Customer()));
	}

	private void given_crmDao_getCustomer_returns_error() {
		when(crmDao.getCustomer(any(Context.class), anyString()))
			.thenReturn(Single.error(new ResourceNotFoundException("Not found")));
	}

	private void given_crmDao_createCustomer_returns_ok() {
		when(crmDao.createCustomer(any(Context.class), any(Customer.class))).thenReturn(Completable.complete());
	}

	private void given_crmDao_createCustomer_returns_error() {
		when(crmDao.createCustomer(any(Context.class), any(Customer.class)))
			.thenReturn(Completable.error(DUMMY_BAD_REQUEST));
	}

	private void given_crmDao_updateCustomer_returns_ok() {
		when(crmDao.updateCustomer(any(Context.class), any(Customer.class))).thenReturn(Completable.complete());
	}

	private void given_crmDao_updateCustomer_returns_error() {
		when(crmDao.updateCustomer(any(Context.class), any(Customer.class)))
			.thenReturn(Completable.error(DUMMY_BAD_REQUEST));
	}

	private void given_crmDao_updateCustomerImg_returns_ok() {
		when(crmDao.updateCustomerImg(any(Context.class), any(UpdateCustomerImgCmd.class)))
			.thenReturn(Completable.complete());
	}

	private void given_crmDao_updateCustomerImg_returns_error() {
		when(crmDao.updateCustomerImg(any(Context.class), any(UpdateCustomerImgCmd.class)))
			.thenReturn(Completable.error(DUMMY_BAD_REQUEST));
	}

	private void given_crmDao_deleteCustomer_returns_ok() {
		when(crmDao.deleteCustomer(any(Context.class), anyString())).thenReturn(Completable.complete());
	}

	private void given_crmDao_deleteCustomer_returns_error() {
		when(crmDao.deleteCustomer(any(Context.class), anyString())).thenReturn(Completable.error(DUMMY_BAD_REQUEST));
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

	private void when_listCustomers() {
		customerHandler.listCustomers(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_getCustomer() {
		customerHandler.getCustomer(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_createCustomer() {
		customerHandler.createCustomer(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_updateCustomer() {
		customerHandler.updateCustomer(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_updateCustomerImg() {
		customerHandler.updateCustomerImg(routingCtx);
		responseBody = ArgumentCaptor.forClass(String.class);
	}

	private void when_deleteCustomer() {
		customerHandler.deleteCustomer(routingCtx);
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
