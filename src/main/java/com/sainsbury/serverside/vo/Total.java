package com.sainsbury.serverside.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Total {
	
	private BigDecimal gross;
	private BigDecimal vat;
	public BigDecimal getGross() {
		return gross;
	}
	public void setGross(BigDecimal gross) {
		this.gross = gross;
	}
	public BigDecimal getVat() {
		return vat;
	}
	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}
	
}
