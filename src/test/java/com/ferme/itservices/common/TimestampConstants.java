package com.ferme.itservices.common;

import com.ferme.itservices.api.utils.models.DateUtils;
import com.ferme.itservices.api.utils.models.Timestamps;

import java.util.Date;

public class TimestampConstants {
	private static final Date date = DateUtils.parseDate("2024-03-19 22:00:00");
	public static final Timestamps timestamps = new Timestamps(date, date);
}