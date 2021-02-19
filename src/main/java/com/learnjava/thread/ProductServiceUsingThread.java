package com.learnjava.thread;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingThread {
	private ProductInfoService productInfoService;
	private ReviewService reviewService;

	public ProductServiceUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
		this.productInfoService = productInfoService;
		this.reviewService = reviewService;
	}

	public Product retrieveProductDetails(String productId) throws InterruptedException {
		stopWatch.start();

		// offloading the blocking task
		ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
		Thread productInfoThread = new Thread(productInfoRunnable);

		ReviewRunnable reviewRunnable = new ReviewRunnable(productId);
		Thread reviewRunnableThread = new Thread(reviewRunnable);

		productInfoThread.start();
		reviewRunnableThread.start();

		// adding join bec if we dont add join() then it will be executed in any order
		// to make the sequential order execution we add join
		// Once you apply join on particular thread, then current executing thread will
		// stop and
		// it will start executing the thread on which the join is applied and stop only
		// when it dead
		productInfoThread.join();
		reviewRunnableThread.join();

		ProductInfo productInfo = productInfoRunnable.getProductInfo();
		Review review = reviewRunnable.getReview();

		// commenting blocking task
		// ProductInfo productInfo = productInfoService.retrieveProductInfo(productId);
		// // blocking call
		// Review review = reviewService.retrieveReviews(productId); // blocking call

		stopWatch.stop();
		log("Total Time Taken : " + stopWatch.getTime());
		return new Product(productId, productInfo, review);
	}

	public static void main(String[] args) throws InterruptedException {

		ProductInfoService productInfoService = new ProductInfoService();
		ReviewService reviewService = new ReviewService();
		ProductServiceUsingThread productService = new ProductServiceUsingThread(productInfoService, reviewService);
		String productId = "ABC123";
		Product product = productService.retrieveProductDetails(productId);
		log("Product is " + product);

	}

	private class ProductInfoRunnable implements Runnable {

		private ProductInfo productInfo;
		private String productId;

		public ProductInfoRunnable(String productId) {
			this.productId = productId;
		}

		public ProductInfo getProductInfo() {
			return productInfo;
		}

		@Override
		public void run() {
			log("ProductInfo ThreadStarted");

			productInfo = productInfoService.retrieveProductInfo(productId); // offloading the blocking task
			log("ProductInfo ThreadEnded");

		}

	}

	private class ReviewRunnable implements Runnable {

		private String productId;
		private Review review;

		public ReviewRunnable(String productId) {
			this.productId = productId;
		}

		public Review getReview() {
			return review;
		}

		@Override
		public void run() {
			log("Review ThreadStarted");
			review = reviewService.retrieveReviews(productId); // blocking call
			log("Review ThreadEnded");

		}

	}
}
