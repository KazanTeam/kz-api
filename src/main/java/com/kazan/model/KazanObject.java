package com.kazan.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "OBJECT")
public class KazanObject {
	@Id
	@GeneratedValue
	@Column(name = "object_id")
	private Integer objectId;

	@Column(name = "symbol")
	private String symbol;

	@Column(name = "objprop_type")
	private Integer objprop_type;

	@Column(name = "objprop_time1")
	@Temporal(TemporalType.TIMESTAMP)
	private Date objprop_time1;

	@Column(name = "objprop_time2")
	@Temporal(TemporalType.TIMESTAMP)
	private Date objprop_time2;

	@Column(name = "objprop_time3")
	@Temporal(TemporalType.TIMESTAMP)
	private Date objprop_time3;

	@Column(name = "objprop_price1")
	private Double objprop_price1;

	@Column(name = "objprop_price2")
	private Double objprop_price2;

	@Column(name = "objprop_price3")
	private Double objprop_price3;

	@Column(name = "objprop_style")
	private Integer objprop_style;

	@Column(name = "objprop_width")
	private Integer objprop_width;

	@Column(name = "objprop_ray_right")
	private Integer objprop_ray_right;

	@Column(name = "objprop_color")
	private Integer objprop_color;

	@Column(name = "objprop_back")
	private Integer objprop_back;

	@Column(name = "objprop_text")
	private String objprop_text;

	@Column(name = "objprop_font")
	private String objprop_font;

	@Column(name = "objprop_fontsize")
	private Integer objprop_fontsize;

	@Column(name = "objprop_angle")
	private Double objprop_angle;

	@Column(name = "objprop_scale")
	private Double objprop_scale;

	@Column(name = "objprop_deviation")
	private Double objprop_deviation;

	@Column(name = "objprop_direction")
	private Integer objprop_direction;

	@Column(name = "objprop_ellipse")
	private Integer objprop_ellipse;

	@Column(name = "objprop_corner")
	private Integer objprop_corner;

	@Column(name = "objprop_fibolevels")
	private Integer objprop_fibolevels;

	@Column(name = "objprop_levelcolor")
	private Integer objprop_levelcolor;

	@Column(name = "objprop_levelstyle")
	private Integer objprop_levelstyle;

	@Column(name = "objprop_levelwidth")
	private Integer objprop_levelwidth;

	@Column(name = "objprop_levelvalue_1")
	private Double objprop_levelvalue_1;

	@Column(name = "objprop_levelvalue_2")
	private Double objprop_levelvalue_2;

	@Column(name = "objprop_levelvalue_3")
	private Double objprop_levelvalue_3;

	@Column(name = "objprop_levelvalue_4")
	private Double objprop_levelvalue_4;

	@Column(name = "objprop_levelvalue_5")
	private Double objprop_levelvalue_5;

	@Column(name = "objprop_levelvalue_6")
	private Double objprop_levelvalue_6;

	@Column(name = "objprop_levelvalue_7")
	private Double objprop_levelvalue_7;

	@Column(name = "objprop_levelvalue_8")
	private Double objprop_levelvalue_8;

	@Column(name = "objprop_levelvalue_9")
	private Double objprop_levelvalue_9;

	@Column(name = "objprop_levelvalue_10")
	private Double objprop_levelvalue_10;

	@Column(name = "objprop_leveltext_1")
	private String objprop_leveltext_1;

	@Column(name = "objprop_leveltext_2")
	private String objprop_leveltext_2;

	@Column(name = "objprop_leveltext_3")
	private String objprop_leveltext_3;

	@Column(name = "objprop_leveltext_4")
	private String objprop_leveltext_4;

	@Column(name = "objprop_leveltext_5")
	private String objprop_leveltext_5;

	@Column(name = "objprop_leveltext_6")
	private String objprop_leveltext_6;

	@Column(name = "objprop_leveltext_7")
	private String objprop_leveltext_7;

	@Column(name = "objprop_leveltext_8")
	private String objprop_leveltext_8;

	@Column(name = "objprop_leveltext_9")
	private String objprop_leveltext_9;

	@Column(name = "objprop_leveltext_10")
	private String objprop_leveltext_10;

	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_date;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "group_id")
	private Integer groupId;
	
	@Column(name = "objprop_timeframes")
	private Integer objprop_timeframes;

	@Column(name = "mode_id")
	private Integer modeId;
	
	// OBJPROP_LEVELS, OBJPROP_BGCOLOR, OBJPROP_BORDER_TYPE, OBJPROP_BORDER_COLOR, OBJPROP_RAY, OBJPROP_NAME

	@Column(name = "objprop_levels")
	private Integer objprop_levels;

	@Column(name = "objprop_bgcolor")
	private Integer objprop_bgcolor;

	@Column(name = "objprop_border_type")
	private Integer objprop_border_type;

	@Column(name = "objprop_border_color")
	private Integer objprop_border_color;

	@Column(name = "objprop_ray")
	private Integer objprop_ray;

	@Column(name = "objprop_name")
	private String objprop_name;
	
	public Integer getObjprop_timeframes() {
		return objprop_timeframes;
	}

	public void setObjprop_timeframes(Integer objprop_timeframes) {
		this.objprop_timeframes = objprop_timeframes;
	}

	public KazanObject() {

	}	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	@JsonIgnore
	public Integer getObjectId() {
		return objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_type() {
		return objprop_type;
	}

	public void setObjprop_type(Integer objprop_type) {
		this.objprop_type = objprop_type;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Date getObjprop_time1() {
		return objprop_time1;
	}

	public void setObjprop_time1(Date objprop_time1) {
		this.objprop_time1 = objprop_time1;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Date getObjprop_time2() {
		return objprop_time2;
	}

	public void setObjprop_time2(Date objprop_time2) {
		this.objprop_time2 = objprop_time2;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_price1() {
		return objprop_price1;
	}

	public void setObjprop_price1(Double objprop_price1) {
		this.objprop_price1 = objprop_price1;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_price2() {
		return objprop_price2;
	}

	public void setObjprop_price2(Double objprop_price2) {
		this.objprop_price2 = objprop_price2;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_width() {
		return objprop_width;
	}

	public void setObjprop_width(Integer objprop_width) {
		this.objprop_width = objprop_width;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_color() {
		return objprop_color;
	}

	public void setObjprop_color(Integer objprop_color) {
		this.objprop_color = objprop_color;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_scale() {
		return objprop_scale;
	}

	public void setObjprop_scale(Double objprop_scale) {
		this.objprop_scale = objprop_scale;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Date getObjprop_time3() {
		return objprop_time3;
	}

	public void setObjprop_time3(Date objprop_time3) {
		this.objprop_time3 = objprop_time3;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_price3() {
		return objprop_price3;
	}

	public void setObjprop_price3(Double objprop_price3) {
		this.objprop_price3 = objprop_price3;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_style() {
		return objprop_style;
	}

	public void setObjprop_style(Integer objprop_style) {
		this.objprop_style = objprop_style;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_ray_right() {
		return objprop_ray_right;
	}

	public void setObjprop_ray_right(Integer objprop_ray_right) {
		this.objprop_ray_right = objprop_ray_right;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_back() {
		return objprop_back;
	}

	public void setObjprop_back(Integer objprop_back) {
		this.objprop_back = objprop_back;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_text() {
		return objprop_text;
	}

	public void setObjprop_text(String objprop_text) {
		this.objprop_text = objprop_text;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_font() {
		return objprop_font;
	}

	public void setObjprop_font(String objprop_font) {
		this.objprop_font = objprop_font;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_fontsize() {
		return objprop_fontsize;
	}

	public void setObjprop_fontsize(Integer objprop_fontsize) {
		this.objprop_fontsize = objprop_fontsize;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_angle() {
		return objprop_angle;
	}

	public void setObjprop_angle(Double objprop_angle) {
		this.objprop_angle = objprop_angle;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_deviation() {
		return objprop_deviation;
	}

	public void setObjprop_deviation(Double objprop_deviation) {
		this.objprop_deviation = objprop_deviation;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_direction() {
		return objprop_direction;
	}

	public void setObjprop_direction(Integer objprop_direction) {
		this.objprop_direction = objprop_direction;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_ellipse() {
		return objprop_ellipse;
	}

	public void setObjprop_ellipse(Integer objprop_ellipse) {
		this.objprop_ellipse = objprop_ellipse;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_corner() {
		return objprop_corner;
	}

	public void setObjprop_corner(Integer objprop_corner) {
		this.objprop_corner = objprop_corner;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_fibolevels() {
		return objprop_fibolevels;
	}

	public void setObjprop_fibolevels(Integer objprop_fibolevels) {
		this.objprop_fibolevels = objprop_fibolevels;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_levelcolor() {
		return objprop_levelcolor;
	}

	public void setObjprop_levelcolor(Integer objprop_levelcolor) {
		this.objprop_levelcolor = objprop_levelcolor;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_levelstyle() {
		return objprop_levelstyle;
	}

	public void setObjprop_levelstyle(Integer objprop_levelstyle) {
		this.objprop_levelstyle = objprop_levelstyle;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_levelwidth() {
		return objprop_levelwidth;
	}
	
	public void setObjprop_levelwidth(Integer objprop_levelwidth) {
		this.objprop_levelwidth = objprop_levelwidth;
	}

	public Integer getModeId() {
		return modeId;
	}

	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_levels() {
		return objprop_levels;
	}

	public void setObjprop_levels(Integer objprop_levels) {
		this.objprop_levels = objprop_levels;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_bgcolor() {
		return objprop_bgcolor;
	}

	public void setObjprop_bgcolor(Integer objprop_bgcolor) {
		this.objprop_bgcolor = objprop_bgcolor;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_border_type() {
		return objprop_border_type;
	}

	public void setObjprop_border_type(Integer objprop_border_type) {
		this.objprop_border_type = objprop_border_type;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_border_color() {
		return objprop_border_color;
	}

	public void setObjprop_border_color(Integer objprop_border_color) {
		this.objprop_border_color = objprop_border_color;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Integer getObjprop_ray() {
		return objprop_ray;
	}

	public void setObjprop_ray(Integer objprop_ray) {
		this.objprop_ray = objprop_ray;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_name() {
		return objprop_name;
	}

	public void setObjprop_name(String objprop_name) {
		this.objprop_name = objprop_name;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_1() {
		return objprop_levelvalue_1;
	}

	public void setObjprop_levelvalue_1(Double objprop_levelvalue_1) {
		this.objprop_levelvalue_1 = objprop_levelvalue_1;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_2() {
		return objprop_levelvalue_2;
	}

	public void setObjprop_levelvalue_2(Double objprop_levelvalue_2) {
		this.objprop_levelvalue_2 = objprop_levelvalue_2;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_3() {
		return objprop_levelvalue_3;
	}

	public void setObjprop_levelvalue_3(Double objprop_levelvalue_3) {
		this.objprop_levelvalue_3 = objprop_levelvalue_3;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_4() {
		return objprop_levelvalue_4;
	}

	public void setObjprop_levelvalue_4(Double objprop_levelvalue_4) {
		this.objprop_levelvalue_4 = objprop_levelvalue_4;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_5() {
		return objprop_levelvalue_5;
	}

	public void setObjprop_levelvalue_5(Double objprop_levelvalue_5) {
		this.objprop_levelvalue_5 = objprop_levelvalue_5;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_6() {
		return objprop_levelvalue_6;
	}

	public void setObjprop_levelvalue_6(Double objprop_levelvalue_6) {
		this.objprop_levelvalue_6 = objprop_levelvalue_6;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_7() {
		return objprop_levelvalue_7;
	}

	public void setObjprop_levelvalue_7(Double objprop_levelvalue_7) {
		this.objprop_levelvalue_7 = objprop_levelvalue_7;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_8() {
		return objprop_levelvalue_8;
	}

	public void setObjprop_levelvalue_8(Double objprop_levelvalue_8) {
		this.objprop_levelvalue_8 = objprop_levelvalue_8;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_9() {
		return objprop_levelvalue_9;
	}

	public void setObjprop_levelvalue_9(Double objprop_levelvalue_9) {
		this.objprop_levelvalue_9 = objprop_levelvalue_9;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public Double getObjprop_levelvalue_10() {
		return objprop_levelvalue_10;
	}

	public void setObjprop_levelvalue_10(Double objprop_levelvalue_10) {
		this.objprop_levelvalue_10 = objprop_levelvalue_10;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_1() {
		return objprop_leveltext_1;
	}

	public void setObjprop_leveltext_1(String objprop_leveltext_1) {
		this.objprop_leveltext_1 = objprop_leveltext_1;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_2() {
		return objprop_leveltext_2;
	}

	public void setObjprop_leveltext_2(String objprop_leveltext_2) {
		this.objprop_leveltext_2 = objprop_leveltext_2;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_3() {
		return objprop_leveltext_3;
	}

	public void setObjprop_leveltext_3(String objprop_leveltext_3) {
		this.objprop_leveltext_3 = objprop_leveltext_3;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_4() {
		return objprop_leveltext_4;
	}

	public void setObjprop_leveltext_4(String objprop_leveltext_4) {
		this.objprop_leveltext_4 = objprop_leveltext_4;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_5() {
		return objprop_leveltext_5;
	}

	public void setObjprop_leveltext_5(String objprop_leveltext_5) {
		this.objprop_leveltext_5 = objprop_leveltext_5;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_6() {
		return objprop_leveltext_6;
	}

	public void setObjprop_leveltext_6(String objprop_leveltext_6) {
		this.objprop_leveltext_6 = objprop_leveltext_6;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_7() {
		return objprop_leveltext_7;
	}

	public void setObjprop_leveltext_7(String objprop_leveltext_7) {
		this.objprop_leveltext_7 = objprop_leveltext_7;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_8() {
		return objprop_leveltext_8;
	}

	public void setObjprop_leveltext_8(String objprop_leveltext_8) {
		this.objprop_leveltext_8 = objprop_leveltext_8;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_9() {
		return objprop_leveltext_9;
	}

	public void setObjprop_leveltext_9(String objprop_leveltext_9) {
		this.objprop_leveltext_9 = objprop_leveltext_9;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public String getObjprop_leveltext_10() {
		return objprop_leveltext_10;
	}

	public void setObjprop_leveltext_10(String objprop_leveltext_10) {
		this.objprop_leveltext_10 = objprop_leveltext_10;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
		result = prime * result + ((objprop_angle == null) ? 0 : objprop_angle.hashCode());
		result = prime * result + ((objprop_back == null) ? 0 : objprop_back.hashCode());
		result = prime * result + ((objprop_color == null) ? 0 : objprop_color.hashCode());
		result = prime * result + ((objprop_corner == null) ? 0 : objprop_corner.hashCode());
		result = prime * result + ((objprop_deviation == null) ? 0 : objprop_deviation.hashCode());
		result = prime * result + ((objprop_direction == null) ? 0 : objprop_direction.hashCode());
		result = prime * result + ((objprop_ellipse == null) ? 0 : objprop_ellipse.hashCode());
		result = prime * result + ((objprop_fibolevels == null) ? 0 : objprop_fibolevels.hashCode());
		result = prime * result + ((objprop_font == null) ? 0 : objprop_font.hashCode());
		result = prime * result + ((objprop_fontsize == null) ? 0 : objprop_fontsize.hashCode());
		result = prime * result + ((objprop_levelcolor == null) ? 0 : objprop_levelcolor.hashCode());
		result = prime * result + ((objprop_levelstyle == null) ? 0 : objprop_levelstyle.hashCode());
		result = prime * result + ((objprop_levelwidth == null) ? 0 : objprop_levelwidth.hashCode());
		result = prime * result + ((objprop_price1 == null) ? 0 : objprop_price1.hashCode());
		result = prime * result + ((objprop_price2 == null) ? 0 : objprop_price2.hashCode());
		result = prime * result + ((objprop_price3 == null) ? 0 : objprop_price3.hashCode());
		result = prime * result + ((objprop_ray_right == null) ? 0 : objprop_ray_right.hashCode());
		result = prime * result + ((objprop_scale == null) ? 0 : objprop_scale.hashCode());
		result = prime * result + ((objprop_style == null) ? 0 : objprop_style.hashCode());
		result = prime * result + ((objprop_text == null) ? 0 : objprop_text.hashCode());
		result = prime * result + ((objprop_time1 == null) ? 0 : objprop_time1.hashCode());
		result = prime * result + ((objprop_time2 == null) ? 0 : objprop_time2.hashCode());
		result = prime * result + ((objprop_time3 == null) ? 0 : objprop_time3.hashCode());
		result = prime * result + ((objprop_type == null) ? 0 : objprop_type.hashCode());
		result = prime * result + ((objprop_width == null) ? 0 : objprop_width.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((updated_date == null) ? 0 : updated_date.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KazanObject other = (KazanObject) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		if (objprop_angle == null) {
			if (other.objprop_angle != null)
				return false;
		} else if (!objprop_angle.equals(other.objprop_angle))
			return false;
		if (objprop_back == null) {
			if (other.objprop_back != null)
				return false;
		} else if (!objprop_back.equals(other.objprop_back))
			return false;
		if (objprop_color == null) {
			if (other.objprop_color != null)
				return false;
		} else if (!objprop_color.equals(other.objprop_color))
			return false;
		if (objprop_corner == null) {
			if (other.objprop_corner != null)
				return false;
		} else if (!objprop_corner.equals(other.objprop_corner))
			return false;
		if (objprop_deviation == null) {
			if (other.objprop_deviation != null)
				return false;
		} else if (!objprop_deviation.equals(other.objprop_deviation))
			return false;
		if (objprop_direction == null) {
			if (other.objprop_direction != null)
				return false;
		} else if (!objprop_direction.equals(other.objprop_direction))
			return false;
		if (objprop_ellipse == null) {
			if (other.objprop_ellipse != null)
				return false;
		} else if (!objprop_ellipse.equals(other.objprop_ellipse))
			return false;
		if (objprop_fibolevels == null) {
			if (other.objprop_fibolevels != null)
				return false;
		} else if (!objprop_fibolevels.equals(other.objprop_fibolevels))
			return false;
		if (objprop_font == null) {
			if (other.objprop_font != null)
				return false;
		} else if (!objprop_font.equals(other.objprop_font))
			return false;
		if (objprop_fontsize == null) {
			if (other.objprop_fontsize != null)
				return false;
		} else if (!objprop_fontsize.equals(other.objprop_fontsize))
			return false;
		if (objprop_levelcolor == null) {
			if (other.objprop_levelcolor != null)
				return false;
		} else if (!objprop_levelcolor.equals(other.objprop_levelcolor))
			return false;
		if (objprop_levelstyle == null) {
			if (other.objprop_levelstyle != null)
				return false;
		} else if (!objprop_levelstyle.equals(other.objprop_levelstyle))
			return false;
		if (objprop_levelwidth == null) {
			if (other.objprop_levelwidth != null)
				return false;
		} else if (!objprop_levelwidth.equals(other.objprop_levelwidth))
			return false;
		if (objprop_price1 == null) {
			if (other.objprop_price1 != null)
				return false;
		} else if (!objprop_price1.equals(other.objprop_price1))
			return false;
		if (objprop_price2 == null) {
			if (other.objprop_price2 != null)
				return false;
		} else if (!objprop_price2.equals(other.objprop_price2))
			return false;
		if (objprop_price3 == null) {
			if (other.objprop_price3 != null)
				return false;
		} else if (!objprop_price3.equals(other.objprop_price3))
			return false;
		if (objprop_ray_right == null) {
			if (other.objprop_ray_right != null)
				return false;
		} else if (!objprop_ray_right.equals(other.objprop_ray_right))
			return false;
		if (objprop_scale == null) {
			if (other.objprop_scale != null)
				return false;
		} else if (!objprop_scale.equals(other.objprop_scale))
			return false;
		if (objprop_style == null) {
			if (other.objprop_style != null)
				return false;
		} else if (!objprop_style.equals(other.objprop_style))
			return false;
		if (objprop_text == null) {
			if (other.objprop_text != null)
				return false;
		} else if (!objprop_text.equals(other.objprop_text))
			return false;
		if (objprop_time1 == null) {
			if (other.objprop_time1 != null)
				return false;
		} else if (!objprop_time1.equals(other.objprop_time1))
			return false;
		if (objprop_time2 == null) {
			if (other.objprop_time2 != null)
				return false;
		} else if (!objprop_time2.equals(other.objprop_time2))
			return false;
		if (objprop_time3 == null) {
			if (other.objprop_time3 != null)
				return false;
		} else if (!objprop_time3.equals(other.objprop_time3))
			return false;
		if (objprop_type == null) {
			if (other.objprop_type != null)
				return false;
		} else if (!objprop_type.equals(other.objprop_type))
			return false;
		if (objprop_width == null) {
			if (other.objprop_width != null)
				return false;
		} else if (!objprop_width.equals(other.objprop_width))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (updated_date == null) {
			if (other.updated_date != null)
				return false;
		} else if (!updated_date.equals(other.updated_date))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
