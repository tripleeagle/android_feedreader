package cz.cvut.fit.biand.feedreader.repository.db;

import java.util.Date;

import androidx.room.TypeConverter;

/**
 * Room {@link TypeConverter} to and from {@link Date}.
 */
public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
