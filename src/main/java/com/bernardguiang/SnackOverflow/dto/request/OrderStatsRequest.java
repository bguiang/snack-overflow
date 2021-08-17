package com.bernardguiang.SnackOverflow.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class OrderStatsRequest {

	@NotBlank(message = "Please include a `range` query parameter with value of `all`, `month`, or `year`")
	@Pattern(regexp="^(all|month|year)$",
			message = "Please include a `range` query parameter with value of `all`, `month`, or `year`"
	)
	private String range;

	public OrderStatsRequest() {
		super();
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	
	
}
