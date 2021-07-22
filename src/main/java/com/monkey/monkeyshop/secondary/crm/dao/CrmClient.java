package com.monkey.monkeyshop.secondary.crm.dao;

import com.monkey.monkeyshop.domain.core.Context;
import com.monkey.monkeyshop.secondary.crm.model.CustomerCrm;
import com.monkey.monkeyshop.secondary.crm.model.UserCrm;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.client.HttpResponse;

public interface CrmClient {

	Single<HttpResponse<Buffer>> listCustomers(Context ctx);
	Single<HttpResponse<Buffer>> getCustomer(Context ctx, String id);
	Single<HttpResponse<Buffer>> createCustomer(Context ctx, CustomerCrm customer);
	Single<HttpResponse<Buffer>> updateCustomer(Context ctx, CustomerCrm customer);
	Single<HttpResponse<Buffer>> deleteCustomer(Context ctx, String id);

	Single<HttpResponse<Buffer>> listUsers(Context ctx);
	Single<HttpResponse<Buffer>> getUser(Context ctx, String id);
	Single<HttpResponse<Buffer>> createUser(Context ctx, UserCrm user);
	Single<HttpResponse<Buffer>> updateUser(Context ctx, UserCrm user);
	Single<HttpResponse<Buffer>> deleteUser(Context ctx, String id);

}
