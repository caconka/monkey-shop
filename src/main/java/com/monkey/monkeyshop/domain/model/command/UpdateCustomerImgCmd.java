package com.monkey.monkeyshop.domain.model.command;

import io.vertx.rxjava3.ext.web.FileUpload;

public class UpdateCustomerImgCmd {

	private FileUpload image;
	private String customerId;
	private String updatedBy;

	public FileUpload getImage() {
		return image;
	}

	public void setImage(FileUpload image) {
		this.image = image;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}
