package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.joda.time.DateTime;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import java.sql.Timestamp;

public class DateTime2SqlTimestampConverter implements FieldConversion{
	
	public Object javaToSql(Object source) {
		if (source instanceof DateTime) {
			DateTime dateTime = (DateTime) source;
			return new Timestamp(dateTime.getMillis());
		}
		return source;
	}

	public Object sqlToJava(Object source) {
		if (source instanceof java.sql.Timestamp) {
			Timestamp sqlTimestamp = (Timestamp) source;
			return new DateTime(sqlTimestamp.getTime());
		}
		return source;
	}
	
}
