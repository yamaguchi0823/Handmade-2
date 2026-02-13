package com.example.app.dto;

import java.time.LocalDateTime;

public class SaleCreateParam {
	
	private Long id;
	private LocalDateTime soldAt;
	private Long channelId;
	private String note;
	
	public Long getId() { return id;}
	public void setId(Long id) { this.id = id; }
	
	public LocalDateTime getSoldAt() { return soldAt; }
	public void setSoldAt(LocalDateTime soldAt)  { this.soldAt = soldAt; }
	
	public Long getChannelId() { return channelId;}
	public void setChannelId(Long channelId) { this.channelId = channelId; }
	
	public String getNote() { return note; }
	public void setNote(String note) { this.note = note; }
}
