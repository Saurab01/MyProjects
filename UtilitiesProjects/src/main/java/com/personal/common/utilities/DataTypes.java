package com.personal.common.utilities;

/**
 * Created by saurabhagrawal on 05/09/16.
 */
public enum DataTypes {
    INT("INT"),BIT("BIT"),VARCHAR("VARCHAR"),DATETIME("DATETIME"),BIGINT("BIGINT"),TIMESTAMP("TIMESTAMP"),DATE("DATE")
    ,REAL("REAL"),FLOAT("FLOAT"),DOUBLE("DOUBLE"),NUMERIC("NUMERIC"),INTEGER("INTEGER"),DECIMAL("DECIMAL");

    private final String value;

    public String getValue() {
        return value;
    }

    DataTypes(String value) {
        this.value = value;
    }

    /*
JDBC type	Java type

CHAR	String
VARCHAR	String
LONGVARCHAR	String

NUMERIC	java.math.BigDecimal
DECIMAL	java.math.BigDecimal
BIT	boolean
TINYINT	byte
SMALLINT	short
INTEGER	int

BIGINT	long
REAL	float
FLOAT	float
DOUBLE	double
BINARY	byte[]
VARBINARY	byte[]
LONGVARBINARY	byte[]
DATE	java.sql.Date
TIME	java.sql.Time
TIMESTAMP	java.sql.Timestamp
     */

}
