package com.slt.infobus.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="ib_video_dtl")
public class IBVideoDtl {
	private int videoId;
	private String videoName;
	private String playTime;
	private String videoFormat;
	private Timestamp uploadedDate;
	private String fileSize;
	private String status;
	private Timestamp validFrom;
	private Timestamp validTo;
	private String modifyName;
	private String orientation;
	
	//spnrr_id
	private IBSponserDtl sponser;
	@ManyToOne
    @JoinColumn(name="SPNSR_ID", nullable=false)
	public IBSponserDtl getSponser(){ return sponser;}
	
	public void setSponser(IBSponserDtl sponser) {
		this.sponser = sponser;
	}

	private Set<IBVideoSlotDtl> slotDtl = new HashSet<IBVideoSlotDtl>(0);
	@OneToMany(cascade=CascadeType.ALL, mappedBy="videoDtl")    
	public Set<IBVideoSlotDtl> getSlotDtl() {
		return slotDtl;
	}
	public void setSlotDtl(Set<IBVideoSlotDtl> slotDtl) {
		this.slotDtl = slotDtl;
	}
	
	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="VID_ID", unique = true, nullable = false)
	public int getVideoId() {
		return videoId;
	}
	
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}
	@Column(name="VIDEO_NAME")
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	@Column(name="PLAY_TIME")
	public String getPlayTime() {
		return playTime;
	}
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	@Column(name="VIDEO_FORMAT")
	public String getVideoFormat() {
		return videoFormat;
	}
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
	@Column(name="UPLOADED_DATE")
	public Timestamp getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(Timestamp uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	@Column(name="FILE_SIZE")
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="VALID_FROM")
	public Timestamp getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}
	@Column(name="VALID_TO")
	public Timestamp getValidTo() {
		return validTo;
	}
	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}
	@Column(name="MODIFIED_NAME")
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}	
	@Column(name="ORIENTATION")
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	
}
