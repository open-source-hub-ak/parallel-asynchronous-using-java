package com.learnjava.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;

class CheckoutServiceTestCase {
	PriceValidatorService priceValidatorService = new PriceValidatorService();
	CheckoutService checkoutService = new CheckoutService(priceValidatorService);

	@Test
	public void noOfCores() {
		System.out.println("No of cores: " + Runtime.getRuntime().availableProcessors());
	}

	@Test
	void checkout_6Items() {

		// given
		Cart cart = DataSet.createCart(25);

		// when
		CheckoutResponse checkoutResponse = checkoutService.checkout(cart);

		// then
		assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
	}

}
