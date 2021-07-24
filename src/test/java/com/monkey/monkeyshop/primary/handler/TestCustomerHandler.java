package com.monkey.monkeyshop.primary.handler;

import com.monkey.monkeyshop.domain.port.CrmDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestCustomerHandler {

	@Mock
	private CrmDao crmDao;
}
